package com.youxigu.dynasty2.armyout.service.impl;

import java.sql.Timestamp;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.manu.util.UtilDate;
import com.youxigu.dynasty2.armyout.domain.Armyout;
import com.youxigu.dynasty2.armyout.domain.Strategy;
import com.youxigu.dynasty2.armyout.domain.UserArmyout;
import com.youxigu.dynasty2.armyout.domain.action.AssistOutAction;
import com.youxigu.dynasty2.hero.enums.TroopIdle;
import com.youxigu.dynasty2.map.domain.MapCell;
import com.youxigu.dynasty2.map.domain.action.TimeAction;
import com.youxigu.dynasty2.map.service.impl.command.StrategyCommander;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.util.BaseException;

/**
 * 派遣
 * 
 * @author LK
 * @date 2016年3月7日
 */
public class AssistStrategyService extends StrategyCommander {
	public static final Logger log = LoggerFactory
			.getLogger(AssistStrategyService.class);

	public void doCheakStrategy(MapCell fromCell, MapCell toCell,
			Strategy strategy, Map<String, Object> params) {
		if (fromCell.getId() == toCell.getId()) {
			throw new BaseException("不能对出发地派遣");
		}

		if (toCell.isEmpty(true)) {
			throw new BaseException("目标是个空地，不能派遣");
		}

		if (!toCell.isOccupied()) {
			throw new BaseException("目标未被占据不能派遣");
		}
		
		if (toCell.isSurface(MapCell.CAS_TYPE_NPC)) {
			throw new BaseException("不能向野怪派遣");
		}

		if (toCell.getUserId() != fromCell.getUserId()
				&& toCell.getGuildId() != fromCell.getGuildId()) {
			throw new BaseException("该基地不是盟友的基地，不能派遣");
		}

		User fromUser = userService.getUserById(fromCell.getUserId());
		if (fromUser == null) {
			throw new BaseException("您已流亡，不能出征");
		}

		// 校验出征队列
		UserArmyout userArmyout = armyoutService
				.doCheakUserArmyout(fromCell.getUserId(),
				strategy);

		long troopId = (Long) params.get("troopId");

		// 所需时间
		double needSecond = armyoutService.getBaseTimeBetweenPoints(fromUser,
				fromCell, toCell);
		int baseTime = (int) Math.ceil(needSecond);
		Timestamp outDttm = UtilDate.nowTimestamp();
		Timestamp outArriveDttm = UtilDate.moveSecond(outDttm, baseTime);
		Timestamp outBackDttm = UtilDate.NOTAVAIABLE;

		// 锁军团状态
		troopService.doCheakIdleTroopStatus(fromUser.getUserId(), troopId,
				TroopIdle.STATUS_OUT, outBackDttm);

		// 消耗
		this.doConsume(fromUser, strategy, fromCell, toCell);

		// 构造armyout
		Armyout armyout = new Armyout(Strategy.COMMAND_210, fromCell.getId(),
				fromCell.getUserId(), fromCell.getCasId(),
				fromCell.getGuildId(), Armyout.PLAER_TYPE, toCell.getId(),
				toCell.getUserId(), toCell.getCasId(), toCell.getGuildId(),
				Armyout.PLAER_TYPE, troopId, baseTime, outDttm, outArriveDttm,
				outBackDttm, Armyout.STATUS_FORWARD);

		armyoutService.createArmyOut(armyout);

		// 占用出征队列
		armyoutService.doJoinUserArmyout(userArmyout, armyout, strategy);

		// 进入线程队列
		armyoutService.doArmtoutJoinExeQueue(armyout, strategy, fromCell,
				toCell, fromUser);
	}

	@Override
	public Map<String, Object> doExcute(TimeAction action) {
		if (!(action instanceof AssistOutAction)) {
			throw new BaseException("不是一条派遣行为");
		}
		AssistOutAction armyOutAction = (AssistOutAction) action;

		Armyout armyout = armyoutService.lockArmyout(armyOutAction.armyout);

		MapCell toCell = armyOutAction.toCell;
		MapCell fromCell = armyOutAction.fromCell;
		try {
			if (toCell.isEmpty(true)) {
				throw new BaseException("目标是个空地，不能派遣");
			}
			if (!toCell.isOccupied()) {
				throw new BaseException("目标未被占据不能派遣");
			}
			if (toCell.getUserId() != fromCell.getUserId()
					&& toCell.getGuildId() != fromCell.getGuildId()) {
				throw new BaseException("该基地不是盟友的基地，不能派遣");
			}
			// 回城
			combatService.doStartArmyoutBackJob(armyout);
		} catch (Exception e) {
			// TODO: handle exception
		}

		// 派遣到达
		armyout.setOutArriveDttm(new Timestamp(System.currentTimeMillis()));
		armyout.setOutType(Strategy.COMMAND_210);
		armyout.setStatus(Armyout.STATUS_STAY);
		armyoutService.updateArmyout(armyout);

		return null;
	}

}

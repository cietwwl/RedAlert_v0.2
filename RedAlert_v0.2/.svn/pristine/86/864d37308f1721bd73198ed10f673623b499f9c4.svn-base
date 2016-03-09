package com.youxigu.dynasty2.armyout.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.youxigu.dynasty2.armyout.domain.Armyout;
import com.youxigu.dynasty2.armyout.domain.Strategy;
import com.youxigu.dynasty2.armyout.domain.action.OutBackAction;
import com.youxigu.dynasty2.hero.domain.Troop;
import com.youxigu.dynasty2.hero.enums.TroopIdle;
import com.youxigu.dynasty2.map.domain.MapCell;
import com.youxigu.dynasty2.map.domain.action.TimeAction;
import com.youxigu.dynasty2.map.service.impl.command.StrategyCommander;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.util.BaseException;

/**
 * 召回
 * 
 * @author LK
 * @date 2016年3月7日
 */
public class RecallStrategyService extends StrategyCommander {
	public static final Logger log = LoggerFactory
			.getLogger(RecallStrategyService.class);

	public void doCheakStrategy(MapCell fromCell, MapCell toCell,
			Strategy strategy, Map<String, Object> params) {
		
		long armyoutId = (Long)params.get("armyoutId");
		Armyout armyout = armyoutService.getArmyoutForRead(armyoutId);
		
		if (armyout == null) {
			throw new BaseException("出征不存在");
		}

		// 执行召回的君主坐标
		int fromCellId = (Integer) params.get("fromCellId");
		if (fromCellId != armyout.getFromCellId()) {
			throw new BaseException("只能从出发地召回");
		}

		if (armyout.getStatus() == Armyout.STATUS_BACK) {
			throw new BaseException("部队已经在返程中");
		}

		// 执行召回的君主id
		long fromUserId = (Long) params.get("fromUserId");
		Troop troop = troopService.getTroopById(fromUserId,
				armyout.getTroopId());
		if (troop == null || armyout.getAttackerUserId() != fromUserId) {
			throw new BaseException("不是自己的部队，不能召回");
		}

		User fromUser = userService.getUserById(fromUserId);
		if (fromUser == null) {
			throw new BaseException("您已流亡，不能召回");
		}
		// 回城
		combatService.doStartArmyoutBackJob(armyout, Strategy.COMMAND_207);
	}

	@Override
	public Map<String, Object> doExcute(TimeAction action) {
		if (!(action instanceof OutBackAction)) {
			throw new BaseException("不是出征返回");
		}

		// 删除出征
		OutBackAction outBackAction = (OutBackAction) action;

		// timeaction 设置为无效
		outBackAction.armyout.parent.setInValid(true);
		Armyout armyout = armyoutService.lockArmyout(outBackAction.armyout);
		armyoutService.deleteArmyOut(armyout);

		// 释放出征队列
		armyoutService.doExitUserArmyout(armyout);

		// 释放武将和军团
		if (armyout.getTroopId() > 0) {
			troopService.updateTroopStatus(armyout.getAttackerUserId(),
					armyout.getTroopId(), TroopIdle.STATUS_IDLE, null);
		}

		return null;
	}

}

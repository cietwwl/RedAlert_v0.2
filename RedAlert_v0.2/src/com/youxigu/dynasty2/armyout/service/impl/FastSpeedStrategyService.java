package com.youxigu.dynasty2.armyout.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.youxigu.dynasty2.armyout.domain.Armyout;
import com.youxigu.dynasty2.armyout.domain.Strategy;
import com.youxigu.dynasty2.armyout.domain.action.SpyAction;
import com.youxigu.dynasty2.hero.domain.Troop;
import com.youxigu.dynasty2.map.domain.MapCell;
import com.youxigu.dynasty2.map.domain.action.TimeAction;
import com.youxigu.dynasty2.map.service.impl.command.StrategyCommander;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.util.BaseException;

/**
 * 侦查
 * 
 * @author LK
 * @date 2016年2月29日
 */
public class FastSpeedStrategyService extends StrategyCommander {
	public static final Logger log = LoggerFactory
			.getLogger(FastSpeedStrategyService.class);

	@Override
	public void doCheakStrategy(MapCell fromCell, MapCell toCell,
			Strategy strategy, Map<String, Object> params) {
		long armyoutId = (Long) params.get("armyoutId");
		Armyout armyout = armyoutService.getArmyoutForRead(armyoutId);

		if (armyout == null) {
			throw new BaseException("出征不存在");
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
			throw new BaseException("您已流亡，不能加速");
		}
		// 回城
		combatService.doStartArmyoutBackJob(armyout, Strategy.COMMAND_207);
	}

	@Override
	public Map<String, Object> doExcute(TimeAction action) {
		SpyAction spyAction = (SpyAction) action;

		// 回城
		combatService.doStartArmyoutBackJob(spyAction.armyout);

		// TODO 被侦查方发送军情
		// TODO 侦查方发送军情
		// TODO 推送目标广播区动画
		return null;
	}

}

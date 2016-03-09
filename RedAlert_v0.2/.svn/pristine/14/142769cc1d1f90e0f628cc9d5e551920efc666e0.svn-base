package com.youxigu.dynasty2.armyout.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.youxigu.dynasty2.armyout.domain.Armyout;
import com.youxigu.dynasty2.armyout.domain.action.OutBackAction;
import com.youxigu.dynasty2.hero.enums.TroopIdle;
import com.youxigu.dynasty2.map.domain.action.TimeAction;
import com.youxigu.dynasty2.map.service.impl.command.StrategyCommander;
import com.youxigu.dynasty2.util.BaseException;

/**
 * 返回策略
 * 
 * @author LK
 * @date 2016年3月4日
 */
public class OutBackStrategyService extends StrategyCommander {
	public static final Logger log = LoggerFactory
			.getLogger(OutBackStrategyService.class);

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
					armyout.getTroopId(),
					TroopIdle.STATUS_IDLE, null);
		}

		return null;
	}

}

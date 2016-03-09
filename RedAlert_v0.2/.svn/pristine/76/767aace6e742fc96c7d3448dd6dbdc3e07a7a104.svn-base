package com.youxigu.dynasty2.combat.service;

import java.util.Map;

import com.youxigu.dynasty2.combat.domain.Combat;
import com.youxigu.dynasty2.user.domain.User;

/**
 * 战斗后的处理：一些战斗数据的保存，减少武将hp，减少士兵数量，增加战斗奖励等等
 * @author Administrator
 *
 */
public interface IAtferCombatService {
	/**
	 * 一场战斗结束后处理
	 * @param combat
	 * @param allEnd
	 */
	void doSaveAfterCombat(Combat combat,boolean allEnd,Map<String,Object> params);
	

	/**
	 * 取死亡回复率
	 * @param user
	 * @param type
	 * @return
	 */
	int getArmyResume(User user, int type);

	/**
	 * 整场战斗结束后的处理
	 * @param combat
	 * @param allEnd
	 */
//	void doSaveAfterAllCombat(Combat combat);	
}

package com.youxigu.dynasty2.combat.attack.combat;

import com.youxigu.dynasty2.combat.domain.CombatConstants;
import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.combat.domain.action.CombatUnitAction;
import com.youxigu.dynasty2.combat.domain.action.HPChangeAction;

/**
 * 普通攻击
 * @author Dagangzi
 *
 */
public class GeneralPhysicsAttack extends AbstractPhysicsAttack {
	public short getAttackType() {
		return CombatConstants.ACTION_ATTACK_GEN;
	}

	@Override
	public boolean doAttack(CombatUnit attacker, CombatUnit defender, CombatUnitAction action) {
		double oldHarm = defender.getCurrHarm();
		int[] entIds = getTogetherCoreHarmNum(attacker, defender);
		double harm = defender.getCurrHarm() - oldHarm;
		
		int lost = (int) harm;

		//伤血行为
		action.addResult(new HPChangeAction(CombatConstants.ACTION_LOST_ARMYNUM, defender,
				CombatConstants.ACTION_ATTACK_GEN, lost, entIds));

		if (lost > 0) {
			defender.reduceTotalHp(lost);
			// 统计打掉敌人的血量
			attacker.addTotalHarm(Math.abs(lost));

			// 统计损失的血量
			defender.addTotalLostHp(Math.abs(lost));
		}

		if (log.isDebugEnabled()) {
			log.debug("{}对{}执行了普通攻击" + ",打掉血量:" + lost + "[剩余" + defender.getTotalHp() + "]", attacker.getName(),
					defender.getName());

		}
		return true;
	}

}

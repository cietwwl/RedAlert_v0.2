package com.youxigu.dynasty2.combat.attack.combat;

import com.youxigu.dynasty2.combat.domain.CombatConstants;
import com.youxigu.dynasty2.combat.domain.CombatFactor;
import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.combat.domain.action.CombatUnitAction;
import com.youxigu.dynasty2.combat.domain.action.HPChangeAction;

/**
 * 暴击
 * @author Administrator
 * 
 */
public class CriticalPhysicsAttack extends AbstractPhysicsAttack {
	public short getAttackType() {
		return CombatConstants.ACTION_ATTACK_CRITICAL;
	}

	@Override
	public boolean doAttack(CombatUnit attacker, CombatUnit defender, CombatUnitAction action) {
		double oldHarm = defender.getCurrHarm();
		int[] entIds = getTogetherCoreHarmNum(attacker, defender);
		double harm = defender.getCurrHarm() - oldHarm;

		//伤害系数
		CombatFactor combatFactor = attacker.getParent().getParent().getCombatFactors()
				.get(CombatFactor.F_CRITICAL_HARM);
		double haemT = combatFactor.getPara1();

		//a暴击伤害
		double damageA = attacker._getCritDamageAdd();

		//b暴击减免
		double damageB = defender._getCritDamageDec();

		//a暴击伤害率=a暴击伤害/(a暴击伤害+b韧性)*爆伤率系数
		double factor = Math.max(1.0d, 1.0d + damageA / (damageA + damageB) * haemT);

		if (log.isDebugEnabled()) {
			log.debug("harm={}", harm);
			log.debug("伤害系数={}", haemT);
			log.debug("a暴击伤害={}", damageA);
			log.debug("b暴击减免={}", damageB);
			log.debug("暴击比例:{}", factor);
		}

		harm = harm * factor;

		if (log.isDebugEnabled()) {
			log.debug("暴击总伤害:{}", harm);
		}

		int lost = (int) harm;

		//伤血行为
		action.addResult(new HPChangeAction(CombatConstants.ACTION_LOST_ARMYNUM, defender,
				CombatConstants.ACTION_ATTACK_CRITICAL, lost, entIds));

		if (lost > 0) {
			defender.reduceTotalHp(lost);
			// 统计打掉敌人的血量
			attacker.addTotalHarm(Math.abs(lost));

			// 统计损失的血量
			defender.addTotalLostHp(Math.abs(lost));
		}

		if (log.isDebugEnabled()) {
			log.debug("{}对{}执行了暴击" + ",打掉血量：" + lost + "(" + defender.getTotalHp() + ")", attacker.getName(),
					defender.getName());
		}

		return true;
	}
}

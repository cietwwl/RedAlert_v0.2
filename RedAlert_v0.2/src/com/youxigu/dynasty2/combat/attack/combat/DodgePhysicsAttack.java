package com.youxigu.dynasty2.combat.attack.combat;

import com.youxigu.dynasty2.combat.domain.CombatConstants;
import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.combat.domain.action.CombatUnitAction;
import com.youxigu.dynasty2.combat.domain.action.HPChangeAction;

/**
 * 闪避攻击
 * 
 * @author Administrator
 * 
 */
public class DodgePhysicsAttack extends AbstractPhysicsAttack {
	public short getAttackType() {
		return CombatConstants.ACTION_ATTACK_DODGE;
	}

	/**
	 * 攻击被闪避，不造成伤害
	 */
	@Override
	public boolean doAttack(CombatUnit attacker, CombatUnit defender, CombatUnitAction action) {
		CombatUnit[] sources = attacker.getSources();
		int[] entIds = null;
		if (sources != null) {
			entIds = new int[sources.length];
			//合击
			int i = 0;
			for (CombatUnit unit : sources) {
				entIds[i] = unit.getId();
				i = i + 1;
			}
		} else {
			entIds = new int[1];
		}
		
		//闪避行为
		action.addResult(new HPChangeAction(CombatConstants.ACTION_LOST_ARMYNUM, defender,
				CombatConstants.ACTION_ATTACK_DODGE, 0, entIds));
		
		if (log.isDebugEnabled()) {
			log.debug("{}对{}执行了攻击被闪避", attacker.getName(), defender.getName());

		}
		return true;
	}

}

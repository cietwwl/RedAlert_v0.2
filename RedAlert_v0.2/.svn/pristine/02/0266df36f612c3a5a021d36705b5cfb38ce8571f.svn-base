package com.youxigu.dynasty2.combat.attack.combat;

import com.youxigu.dynasty2.combat.domain.CombatConstants;
import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.combat.domain.action.CombatUnitAction;
import com.youxigu.dynasty2.combat.domain.action.ShieldChangeAction;

/**
 * 护盾攻击
 * @author Dagangzi
 */
public class ShieldAttack extends AbstractPhysicsAttack {
	public short getAttackType() {
		return CombatConstants.ACTION_SHIELD_ATTACK;
	}

	@Override
	public boolean doAttack(CombatUnit attacker, CombatUnit defender, CombatUnitAction action) {
		int harm = 0;
		CombatUnit[] sources = attacker.getSources();
		if(sources != null) {
			//合击
			for(CombatUnit unit : sources) {
				if(unit == null || !unit.dead()) {
					continue;
				}
				harm += (int)getShieldHarmNum(attacker, defender);
			}
		}else {
			//普通攻击
			harm = (int)getShieldHarmNum(attacker, defender);
		}
		
		//护盾攻击action
		action.addResult(new ShieldChangeAction(CombatConstants.ACTION_ADD_SHIELD, defender,
				CombatConstants.ACTION_SHIELD_ATTACK, harm));

		if (harm > 0) {
			defender.setShieldHp(defender.getShieldHp() + harm);
		}

		if (log.isDebugEnabled()) {
			log.debug("{}对{}执行了护盾攻击" + ",增加护盾：" + harm + "(" + defender.getShieldHp() + ")", attacker.getName(),
					defender.getName());
		}

		return true;
	}
}

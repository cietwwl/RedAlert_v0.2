package com.youxigu.dynasty2.combat.skill.render;

import com.youxigu.dynasty2.combat.domain.CombatConstants;
import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.combat.domain.action.AbstractCombatAction;
import com.youxigu.dynasty2.combat.domain.action.DotShieldChangeAction;
import com.youxigu.dynasty2.combat.skill.CombatSkillEffect;

/**
 * 持续恢复兵的dot效果
 * @author Dagangzi
 *
 */
public class AddArmyNumDotRender extends DefaultSkillEffectRender {

	@Override
	public AbstractCombatAction applyEffect(CombatSkillEffect effect,
			CombatUnit target) {

		// CombatUnit source = effect.getOwner();
		int add = effect.getValue();// 加兵

		int totalHp = target.getTotalHp();
		// AttackAction action = new ApplyDotEffectAction(effect.getEffId(),
		// source);

		AbstractCombatAction action = null;
		if (!target.dead()) {
			target.addTotalHp(add);

			// DOT护盾action
			action = new DotShieldChangeAction(
					CombatConstants.ACTION_DOT_ADD_SHIELD, target,
					effect.getEffId(), effect.getEffKey(),
					(target.getTotalHp() - totalHp));
		}

		return action;
	}
}

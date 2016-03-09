package com.youxigu.dynasty2.combat.skill.render;

import com.youxigu.dynasty2.combat.domain.CombatConstants;
import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.combat.domain.action.AbstractCombatAction;
import com.youxigu.dynasty2.combat.domain.action.DotHPChangeAction;
import com.youxigu.dynasty2.combat.skill.CombatSkillEffect;

/**
 * 持续死兵的dot效果，例如中毒
 * 
 * @author Dagangzi
 *
 */
public class LostArmyNumDotRender extends DefaultSkillEffectRender {

	@Override
	public AbstractCombatAction applyEffect(CombatSkillEffect effect,
			CombatUnit target) {
		CombatUnit source = effect.getOwner();
		int lost = effect.getValue();// 损血

		int totalHp = target.getTotalHp();

		// source发动dot效果
		// AttackAction action = new ApplyDotEffectAction(effect.getEffId(),
		// source);
		AbstractCombatAction action = null;
		if (!target.dead()) {
			target.reduceTotalHp(lost);

			// Dot 伤血action
			action = new DotHPChangeAction(
					CombatConstants.ACTION_DOT_LOST_ARMYNUM,
					target, (totalHp - target.getTotalHp()), effect.getEffId(),
					effect.getEffKey());
		}

		if (log.isDebugEnabled()) {
			log.debug(
					source.getName() + "对" + target.getName()
							+ "执行了DOT伤血,血量损失[{},剩余{}]",
					lost, target.getTotalHp());
		}
		return action;
	}
}

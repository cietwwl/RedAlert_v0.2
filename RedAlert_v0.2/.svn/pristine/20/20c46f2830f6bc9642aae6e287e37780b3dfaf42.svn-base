package com.youxigu.dynasty2.combat.skill;

import java.util.List;

import com.youxigu.dynasty2.combat.domain.Combat;
import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.combat.domain.action.AbstractCombatAction;
import com.youxigu.dynasty2.combat.domain.action.SkillEffectAction;

/**
 * 技能效果渲染器
 * @author Dagangzi
 * 效果分为3类  1.1次性效果  2.1-n回合效果buff类型  3.1次被作用后，之后的1-n受影响dot类型
 */
public interface ISkillEffectRender {
	/**
	 * 效果渲染:所有目标
	 * 
	 * @param combat
	 * @param skill
	 * @param effect
	 * @param owners
	 *            主体集
	 * @param targets
	 *            目标集
	 * @param action
	 */
	public void doRender(Combat combat, CombatSkill skill,
			CombatSkillEffect effect, List<CombatUnit> owners,
			List<CombatUnit> targets,
			SkillEffectAction action);

	/**
	 * 应用效果，dot类的使用
	 * @param effect
	 */
	public AbstractCombatAction applyEffect(CombatSkillEffect effect,
			CombatUnit target);
}

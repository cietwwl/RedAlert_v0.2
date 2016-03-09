package com.youxigu.dynasty2.combat.skill;

import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.entity.domain.HeroSkillLimit;

/**
 * 技能执行limit校验器
 * @author Dagangzi
 *
 */
public interface ISkillFiredLimitValidator {
	/**
	 * 校验条件
	 * @param skill 技能
	 * @param limit 技能约束条件
	 * @param source 释放技能的人
	 * @return
	 */
	public boolean checked(CombatSkill skill, HeroSkillLimit limit, CombatUnit source);
}

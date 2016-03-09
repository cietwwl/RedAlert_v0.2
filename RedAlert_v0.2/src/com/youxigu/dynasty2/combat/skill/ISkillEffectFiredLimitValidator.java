package com.youxigu.dynasty2.combat.skill;

import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.entity.domain.HeroSkillEffectLimit;

/**
 * 技能效果执行limit校验器
 * @author Dagangzi
 *
 */
public interface ISkillEffectFiredLimitValidator {
	public boolean checked(CombatSkillEffect skill, HeroSkillEffectLimit limit, CombatUnit source, CombatUnit target);
}

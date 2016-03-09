package com.youxigu.dynasty2.combat.skill.cheak.effect;

import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.combat.domain.WallCombatUnit;
import com.youxigu.dynasty2.combat.skill.CombatSkillEffect;
import com.youxigu.dynasty2.combat.skill.ISkillEffectFiredLimitValidator;
import com.youxigu.dynasty2.entity.domain.HeroSkillEffectLimit;

/**
 * 是否对城墙生效，即目标为城墙时，是否发动该技能
 * 
 * @author Administrator
 * 
 */
public class ForbidWallLimit implements ISkillEffectFiredLimitValidator {
	@Override
	public boolean checked(CombatSkillEffect skill, HeroSkillEffectLimit limit, CombatUnit source, CombatUnit target) {
		return !(target instanceof WallCombatUnit);
	}
}

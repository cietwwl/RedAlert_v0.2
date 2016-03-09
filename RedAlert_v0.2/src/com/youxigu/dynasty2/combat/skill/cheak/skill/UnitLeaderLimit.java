package com.youxigu.dynasty2.combat.skill.cheak.skill;

import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.combat.skill.CombatSkill;
import com.youxigu.dynasty2.combat.skill.ISkillFiredLimitValidator;
import com.youxigu.dynasty2.entity.domain.HeroSkillLimit;

/**
 * 是否为队长
 * 
 * @author Administrator
 * 
 */
public class UnitLeaderLimit implements ISkillFiredLimitValidator {

	@Override
	public boolean checked(CombatSkill skill, HeroSkillLimit limit, CombatUnit source) {
		return source._isLeader();
	}

}

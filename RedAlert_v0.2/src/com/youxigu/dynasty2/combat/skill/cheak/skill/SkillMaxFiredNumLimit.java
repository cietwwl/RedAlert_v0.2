package com.youxigu.dynasty2.combat.skill.cheak.skill;

import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.combat.skill.CombatSkill;
import com.youxigu.dynasty2.combat.skill.ISkillFiredLimitValidator;
import com.youxigu.dynasty2.entity.domain.HeroSkillLimit;

/**
 * 技能一场战斗中未到最大施放次数
 * 
 * @author Administrator
 * 
 */
public class SkillMaxFiredNumLimit implements ISkillFiredLimitValidator {

	@Override
	public boolean checked(CombatSkill skill, HeroSkillLimit limit, CombatUnit source) {
		return skill.getFiredCount() < limit.getPara1();
	}

}

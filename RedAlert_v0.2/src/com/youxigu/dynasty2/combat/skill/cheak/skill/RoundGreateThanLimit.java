package com.youxigu.dynasty2.combat.skill.cheak.skill;

import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.combat.skill.CombatSkill;
import com.youxigu.dynasty2.combat.skill.ISkillFiredLimitValidator;
import com.youxigu.dynasty2.entity.domain.HeroSkillLimit;

/**
 * 当前回合数大于等于第几回合
 * 
 * @author Administrator
 * 
 */
public class RoundGreateThanLimit implements ISkillFiredLimitValidator {

	@Override
	public boolean checked(CombatSkill skill, HeroSkillLimit limit, CombatUnit source) {
		return source.getParent().getParent().getRound() >= limit.getPara1();
	}

}

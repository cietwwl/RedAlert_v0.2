package com.youxigu.dynasty2.combat.skill.cheak.skill;

import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.combat.skill.CombatSkill;
import com.youxigu.dynasty2.combat.skill.ISkillFiredLimitValidator;
import com.youxigu.dynasty2.entity.domain.HeroSkillLimit;

/**
 * 当前士气大于初始士气的某百分比
 * 
 * @author Administrator
 * 
 */
public class UnitMoraleGreaterThanLimit implements ISkillFiredLimitValidator {

	@Override
	public boolean checked(CombatSkill skill, HeroSkillLimit limit, CombatUnit source) {
		double percent = source.getCurMorale() * 100d / source.getInitMorale();
		if (percent >= limit.getPara1()) {
			return true;
		}
		return false;
	}

}

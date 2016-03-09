package com.youxigu.dynasty2.combat.skill.cheak.skill;

import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.combat.skill.CombatSkill;
import com.youxigu.dynasty2.combat.skill.ISkillFiredLimitValidator;
import com.youxigu.dynasty2.entity.domain.HeroSkillLimit;

/**
 * 当前血量大于初始血量的某百分比
 * 
 * @author Administrator
 * 
 */
public class UnitArmyNumGreaterThanLimit implements ISkillFiredLimitValidator {

	@Override
	public boolean checked(CombatSkill skill, HeroSkillLimit limit, CombatUnit source) {
		double percent = source.getTotalHp() * 100d / source.getInitHp();
		if (percent >= limit.getPara1()) {
			return true;
		}
		return false;
	}

}

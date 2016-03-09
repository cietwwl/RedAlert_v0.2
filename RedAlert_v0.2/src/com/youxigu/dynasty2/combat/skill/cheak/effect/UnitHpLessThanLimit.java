package com.youxigu.dynasty2.combat.skill.cheak.effect;

import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.combat.skill.CombatSkillEffect;
import com.youxigu.dynasty2.combat.skill.ISkillEffectFiredLimitValidator;
import com.youxigu.dynasty2.entity.domain.HeroSkillEffectLimit;

/**
 * 目标血量小于初始血量的某百分比
 * 
 * @author Administrator
 * 
 */
public class UnitHpLessThanLimit implements ISkillEffectFiredLimitValidator {

	@Override
	public boolean checked(CombatSkillEffect skill, HeroSkillEffectLimit limit, CombatUnit source, CombatUnit target) {
		double percent = target.getTotalHp() * 100d / target.getInitHp();
		if (percent <= limit.getPara1()) {
			return true;
		}
		return false;
	}

}

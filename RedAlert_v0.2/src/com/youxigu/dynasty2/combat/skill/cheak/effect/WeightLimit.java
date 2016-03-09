package com.youxigu.dynasty2.combat.skill.cheak.effect;

import com.manu.util.Util;
import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.combat.skill.CombatSkillEffect;
import com.youxigu.dynasty2.combat.skill.ISkillEffectFiredLimitValidator;
import com.youxigu.dynasty2.entity.domain.HeroSkillEffectLimit;

/**
 * 目标处于被攻击状态下  被普通攻击  被攻击后闪避/被命中  被暴击  被打死 
 * @author Dagangzi
 *
 */
public class WeightLimit implements ISkillEffectFiredLimitValidator {

	@Override
	public boolean checked(CombatSkillEffect skill, HeroSkillEffectLimit limit, CombatUnit source, CombatUnit target) {
		int percent = limit.getPara1();
		//施放概率校验
		if (Util.randInt(1001) < percent) {
			return true;
		}
		return false;
	}

}

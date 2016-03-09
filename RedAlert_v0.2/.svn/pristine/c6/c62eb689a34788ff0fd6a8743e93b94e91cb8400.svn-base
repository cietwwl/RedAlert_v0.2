package com.youxigu.dynasty2.combat.skill.cheak.effect;

import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.combat.skill.CombatSkillEffect;
import com.youxigu.dynasty2.combat.skill.ISkillEffectFiredLimitValidator;
import com.youxigu.dynasty2.entity.domain.HeroSkillEffectLimit;

/**
 * 目标受到制定buff影响
 * @author Dagangzi
 *
 */
public class TargetUnderBuffLimit implements ISkillEffectFiredLimitValidator {

	@Override
	public boolean checked(CombatSkillEffect skill, HeroSkillEffectLimit limit, CombatUnit source, CombatUnit target) {
		String effs = limit.getPara2();
		if(effs != null && !effs.equals("")) {
			String[] effKeys = effs.trim().split(",");
			for(String key : effKeys) {
				if(!target.containsEffect(key)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

}

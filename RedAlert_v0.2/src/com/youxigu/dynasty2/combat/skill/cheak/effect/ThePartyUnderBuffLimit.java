package com.youxigu.dynasty2.combat.skill.cheak.effect;

import java.util.List;

import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.combat.skill.CombatSkillEffect;
import com.youxigu.dynasty2.combat.skill.ISkillEffectFiredLimitValidator;
import com.youxigu.dynasty2.entity.domain.HeroSkillEffectLimit;

/**
 * 本方受到指定buff影响
 * @author Dagangzi
 *
 */
public class ThePartyUnderBuffLimit implements ISkillEffectFiredLimitValidator {

	@Override
	public boolean checked(CombatSkillEffect skill, HeroSkillEffectLimit limit, CombatUnit source, CombatUnit target) {
		String effs = limit.getPara2();
		List<CombatUnit> units = source.getParent().getUnits();
		if(effs != null && !effs.equals("")) {
			String[] effKeys = effs.trim().split(",");
			for(String key : effKeys) {
				boolean flag = false;
				for(CombatUnit unit : units) {
					if(!unit.dead() && unit.containsEffect(key)) {
						flag = true;
						break;
					}
				}
				if (!flag) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

}

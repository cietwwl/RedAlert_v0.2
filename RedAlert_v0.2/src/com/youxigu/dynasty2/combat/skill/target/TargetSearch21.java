package com.youxigu.dynasty2.combat.skill.target;

import java.util.ArrayList;
import java.util.List;

import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.combat.skill.CombatSkillEffect;
import com.youxigu.dynasty2.combat.skill.ITargetSearcher;

/**
 * 21：本方在指定buff或状态下的人
 * 
 * @author Administrator
 * 
 */
public class TargetSearch21 implements ITargetSearcher {

	@Override
	public List<CombatUnit> searchTarget(CombatSkillEffect skillEffect, CombatUnit source, CombatUnit target) {
		List<CombatUnit> units = source.getParent().getUnits();
		String key = skillEffect.getEffect().getPara2();
		List<CombatUnit> targets = new ArrayList<CombatUnit>();
		if(units != null && units.size() >0) {
			for(CombatUnit unit : units) {
				if(unit != null && !unit.dead() && unit.containsEffect(key)) {
					targets.add(unit);
				}
			}
		}
		return targets;
	}
}

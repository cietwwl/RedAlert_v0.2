package com.youxigu.dynasty2.combat.skill.target;

import java.util.ArrayList;
import java.util.List;

import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.combat.skill.CombatSkillEffect;
import com.youxigu.dynasty2.combat.skill.ITargetSearcher;

/**
 * 18：本方血量最低的单位
 * 
 * @author Administrator
 * 
 */
public class TargetSearch18 implements ITargetSearcher {

	@Override
	public List<CombatUnit> searchTarget(CombatSkillEffect skillEffect, CombatUnit source, CombatUnit target) {
		List<CombatUnit> units = source.getParent().getUnits();
		CombatUnit _target = null;
		if(units != null && units.size() >0) {
			for(CombatUnit unit : units) {
				if(unit != null && !unit.dead()) {
					if(_target == null || _target.getTotalHp() > unit.getTotalHp()) {
						_target = unit;
					}
				}
			}
		}
		List<CombatUnit> targets = new ArrayList<CombatUnit>();
		if (_target != null) {
			targets.add(_target);
		}
		return targets;
	}
}

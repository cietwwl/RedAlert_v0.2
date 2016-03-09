package com.youxigu.dynasty2.combat.skill.target;

import java.util.ArrayList;
import java.util.List;

import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.combat.skill.CombatSkillEffect;
import com.youxigu.dynasty2.combat.skill.ITargetSearcher;

/**
 * 16：对敌方目标周围
 * 
 * @author Administrator
 * 
 */
public class TargetSearch16 implements ITargetSearcher {

	@Override
	public List<CombatUnit> searchTarget(CombatSkillEffect skillEffect, CombatUnit source, CombatUnit target) {
		CombatUnit _target = DefaultTargetSearcher.byFrontrowSearch(skillEffect, source, target);
		List<CombatUnit> targets = new ArrayList<CombatUnit>();
		if (_target != null) {
			int[] ids = DefaultTargetSearcher.nearbySearch[_target.getId()];
			for (int unitId : ids) {
				CombatUnit combatUnit = source.getParent().getParent().getCombatUnitById(unitId);
				if (combatUnit != null && !combatUnit.dead()) {
					targets.add(combatUnit);
				}
			}
		}
		return targets;
	}
}

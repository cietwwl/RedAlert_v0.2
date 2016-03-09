package com.youxigu.dynasty2.combat.skill.target;

import java.util.ArrayList;
import java.util.List;

import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.combat.skill.CombatSkillEffect;
import com.youxigu.dynasty2.combat.skill.ITargetSearcher;

/**
 * 4：对前排3个单位造成伤害
 * 
 * @author Administrator
 * 
 */
public class TargetSearch4 implements ITargetSearcher {

	@Override
	public List<CombatUnit> searchTarget(CombatSkillEffect skillEffect, CombatUnit source, CombatUnit target) {
		CombatUnit _target = DefaultTargetSearcher.byFrontrowSearch(skillEffect, source, target);
		List<CombatUnit> targets = new ArrayList<CombatUnit>(3);
		if (_target != null) {
			int[] ids = getRaw(_target.getId());
			for (int unitId : ids) {
				CombatUnit combatUnit = source.getParent().getParent().getCombatUnitById(unitId);
				if (combatUnit != null && !combatUnit.dead()) {
					targets.add(combatUnit);
				}
			}
		}
		return targets;
	}
	
	/**
	 * 找所在行
	 * @return
	 */
	public int[] getRaw(int id) {
		if (id <= 3) {
			return new int[] { 1, 2, 3 };
		} else if (id <= 6) {
			return new int[] { 4, 5, 6 };
		} else if (id <= 9) {
			return new int[] { 7, 8, 9 };
		} else if (id <= 12) {
			return new int[] { 10, 11, 12 };
		}
		return null;
	}
}

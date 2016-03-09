package com.youxigu.dynasty2.combat.skill.target;

import java.util.ArrayList;
import java.util.List;

import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.combat.skill.CombatSkillEffect;
import com.youxigu.dynasty2.combat.skill.ITargetSearcher;

/**
 * 6：对1列敌人造成伤害
 * 
 * @author Administrator
 * 
 */
public class TargetSearch6 implements ITargetSearcher {
	@Override
	public List<CombatUnit> searchTarget(CombatSkillEffect skillEffect, CombatUnit source, CombatUnit target) {
		CombatUnit _target = DefaultTargetSearcher.byBackrowSearch(skillEffect, source, target);
		List<CombatUnit> targets = new ArrayList<CombatUnit>(2);
		if (_target != null) {
			int[] ids = getColumn(_target.getId());
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
	 * 取得列编号
	 * @return
	 */
	public int[] getColumn(int id) {
		if (id == 1 || id == 4) {
			return new int[] { 1, 4 };
		} else if (id == 2 || id == 5) {
			return new int[] { 2, 5 };
		} else if (id == 3 || id == 6) {
			return new int[] { 3, 6 };
		} else if (id == 7 || id == 10) {
			return new int[] { 7, 10 };
		} else if (id == 8 || id == 11) {
			return new int[] { 8, 11 };
		} else if (id == 9 || id == 12) {
			return new int[] { 9, 12 };
		}
		return null;
	}
}

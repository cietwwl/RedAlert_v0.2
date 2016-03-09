package com.youxigu.dynasty2.combat.skill.target;

import java.util.ArrayList;
import java.util.List;

import com.manu.util.Util;
import com.youxigu.dynasty2.combat.domain.CanotAttackedCombatUnit;
import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.combat.domain.WallCombatUnit;
import com.youxigu.dynasty2.combat.skill.CombatSkillEffect;
import com.youxigu.dynasty2.combat.skill.ITargetSearcher;

/**
 * 11：敌方随机n个部队(不包括城墙)
 * @author Dagangzi
 *
 */
public class TargetSearch11 implements ITargetSearcher {

	@Override
	public List<CombatUnit> searchTarget(CombatSkillEffect skillEffect, CombatUnit source, CombatUnit target) {
		int num = skillEffect.getEffect().getTargetNum();
		List<CombatUnit> targets = new ArrayList<CombatUnit>(num);
		List<CombatUnit> all = source.getParent().getEnemyTeam().getUnits();
		for (CombatUnit unit : all) {
			if (!unit.dead()) {
				if (!(unit instanceof CanotAttackedCombatUnit)
						&& !(unit instanceof WallCombatUnit)) {
					targets.add(unit);
				}
			}
		}
		int total = targets.size();
		if (num <= 0) {
			num = 1;
		}
		while (total > num) {
			targets.remove(Util.randInt(total));
			total--;
		}

		return targets;
	}
}

package com.youxigu.dynasty2.combat.skill.target;

import java.util.ArrayList;
import java.util.List;

import com.youxigu.dynasty2.combat.domain.CanotAttackedCombatUnit;
import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.combat.domain.WallCombatUnit;
import com.youxigu.dynasty2.combat.skill.CombatSkillEffect;
import com.youxigu.dynasty2.combat.skill.ITargetSearcher;

/**
 * 13：敌方特定兵种(不包括城墙)
 * @author Dagangzi
 *
 */
public class TargetSearch13 implements ITargetSearcher {
	@Override
	public List<CombatUnit> searchTarget(CombatSkillEffect skillEffect, CombatUnit source, CombatUnit target) {
		List<CombatUnit> targets = new ArrayList<CombatUnit>(6);
		List<CombatUnit> all = source.getParent().getEnemyTeam().getUnits();
		for (CombatUnit unit : all) {
			if (!unit.dead() && unit.getArmyEntId() == skillEffect.getEffect().getTargetArmy()) {
				if (!(unit instanceof CanotAttackedCombatUnit) && !(unit instanceof WallCombatUnit)) {
					targets.add(unit);
				}
			}
		}

		return targets;
	}
}

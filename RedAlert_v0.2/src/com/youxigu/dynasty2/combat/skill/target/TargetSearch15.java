package com.youxigu.dynasty2.combat.skill.target;

import java.util.ArrayList;
import java.util.List;

import com.youxigu.dynasty2.combat.domain.CanotAttackedCombatUnit;
import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.combat.domain.NPCHeroCombatUnit;
import com.youxigu.dynasty2.combat.domain.PlayHeroCombatUint;
import com.youxigu.dynasty2.combat.domain.WallCombatUnit;
import com.youxigu.dynasty2.combat.skill.CombatSkillEffect;
import com.youxigu.dynasty2.combat.skill.ITargetSearcher;
import com.youxigu.dynasty2.entity.domain.SysHero;

/**
 * 15：我方特定国家的单位(不包括城墙)
 * @author Dagangzi
 *
 */
public class TargetSearch15 implements ITargetSearcher {
	@Override
	public List<CombatUnit> searchTarget(CombatSkillEffect skillEffect, CombatUnit source, CombatUnit target) {
		List<CombatUnit> targets = new ArrayList<CombatUnit>(6);
		List<CombatUnit> all = source.getParent().getUnits();
		for (CombatUnit unit : all) {
			if (!unit.dead() && !(unit instanceof CanotAttackedCombatUnit) && !(unit instanceof WallCombatUnit)) {
				SysHero sysHero = null;
				if (unit instanceof NPCHeroCombatUnit) {
					sysHero = ((NPCHeroCombatUnit) unit)._getHero().getSysHero();
				} else if (unit instanceof PlayHeroCombatUint) {
					sysHero = ((PlayHeroCombatUint) unit)._getHero().getSysHero();
				}

				if (sysHero != null && sysHero.getCountryId() == skillEffect.getEffect().getTargetCountryId()) {
					targets.add(unit);
				}
			}
		}

		return targets;
	}
}

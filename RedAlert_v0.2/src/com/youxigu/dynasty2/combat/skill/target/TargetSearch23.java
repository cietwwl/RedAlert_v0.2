package com.youxigu.dynasty2.combat.skill.target;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.manu.util.Util;
import com.youxigu.dynasty2.combat.domain.CanotAttackedCombatUnit;
import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.combat.domain.WallCombatUnit;
import com.youxigu.dynasty2.combat.skill.CombatSkillEffect;
import com.youxigu.dynasty2.combat.skill.ITargetSearcher;

/**
 * 23：全体随机n个部队(不包括城墙)
 * @author Dagangzi
 *
 */
public class TargetSearch23 implements ITargetSearcher {

	@Override
	public List<CombatUnit> searchTarget(CombatSkillEffect skillEffect, CombatUnit source, CombatUnit target) {
		//数量上限
		int num = skillEffect.getEffect().getTargetNum();
		
		List<CombatUnit> targets = new ArrayList<CombatUnit>();
		targets.addAll(source.getParent().getUnits());
		targets.addAll(source.getParent().getEnemyTeam().getUnits());
		Iterator<CombatUnit> itl = targets.iterator();
		while (itl.hasNext()) {
			CombatUnit unit = itl.next();
			if (!unit.dead()) {
				if (!(unit instanceof CanotAttackedCombatUnit) && !(unit instanceof WallCombatUnit)) {
					itl.remove();
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

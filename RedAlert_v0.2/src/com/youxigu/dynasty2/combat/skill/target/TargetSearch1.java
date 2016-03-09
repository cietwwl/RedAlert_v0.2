package com.youxigu.dynasty2.combat.skill.target;

import java.util.ArrayList;
import java.util.List;

import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.combat.skill.CombatSkillEffect;
import com.youxigu.dynasty2.combat.skill.ITargetSearcher;

/**
 * 1：自己；
 * @author Administrator
 *
 */
public class TargetSearch1 implements ITargetSearcher {
	@Override
	public List<CombatUnit> searchTarget(CombatSkillEffect skillEffect, CombatUnit source, CombatUnit target) {
		List<CombatUnit> targets = new ArrayList<CombatUnit>(1);
		if(!source.dead()) {
			targets.add(source);
		}
		return targets;
	}

}

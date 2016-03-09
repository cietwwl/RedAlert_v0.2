package com.youxigu.dynasty2.combat.skill.target;

import java.util.ArrayList;
import java.util.List;

import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.combat.skill.CombatSkillEffect;
import com.youxigu.dynasty2.combat.skill.ITargetSearcher;

/**
 * 3：对后排单体造成伤害
 * 
 * @author Administrator
 * 
 */
public class TargetSearch3 implements ITargetSearcher {
	
	@Override
	public List<CombatUnit> searchTarget(CombatSkillEffect skillEffect, CombatUnit source, CombatUnit target) {
		CombatUnit _target = DefaultTargetSearcher.byBackrowSearch(skillEffect, source, target);
		List<CombatUnit> targets = new ArrayList<CombatUnit>(1);
		if(_target != null && !_target.dead()) {
			targets.add(_target);
		}
		return targets;
	}
}

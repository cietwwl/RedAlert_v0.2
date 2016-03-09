package com.youxigu.dynasty2.combat.skill.target;

import java.util.ArrayList;
import java.util.List;

import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.combat.skill.CombatSkillEffect;
import com.youxigu.dynasty2.combat.skill.ITargetSearcher;

/**
 * 10：对自己进行攻击的战斗单元；
 * 
 * @author Administrator
 * 
 */
public class TargetSearch10 implements ITargetSearcher {

	@Override
	public List<CombatUnit> searchTarget(CombatSkillEffect skillEffect, CombatUnit source, CombatUnit target) {
		//对自己进行攻击的战斗单元有可能已经死亡，要顺序找下去
		CombatUnit _target = DefaultTargetSearcher.byTargetSearch(skillEffect, source, target);
		List<CombatUnit> targets = new ArrayList<CombatUnit>(1);
		if (_target != null && !_target.dead()) {
			targets.add(_target);
		}
		return targets;
	}

}

package com.youxigu.dynasty2.combat.skill.target;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.combat.skill.CombatSkillEffect;
import com.youxigu.dynasty2.combat.skill.ITargetSearcher;

/**
 * 24：本方指定的多个sysheroId
 * 
 * @author Administrator heroSkillEffect.getPara2() 配数"sysHeroId1,sysHeroId2"
 */
public class TargetSearch24 implements ITargetSearcher {

	@Override
	public List<CombatUnit> searchTarget(CombatSkillEffect skillEffect, CombatUnit source, CombatUnit target) {
		// 指定的系统武将id
		int[] entIds = skillEffect.getEffect().getEntIds();
		List<CombatUnit> targets = new ArrayList<CombatUnit>();
		if(entIds != null && entIds.length >0){
			Map<Integer, Integer> map = new HashMap<Integer, Integer>();
			for (int entId : entIds) {
				map.put(entId, entId);
			}
			List<CombatUnit> units = source.getParent().getUnits();
			
			if(units != null && units.size() >0) {
				for(CombatUnit unit : units) {
					if (unit != null && !unit.dead()
							&& map.containsKey(unit.getUnitEntId())) {
						targets.add(unit);
					}
				}
			}
		}
		return targets;
	}
}

package com.youxigu.dynasty2.combat.skill.target;

import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.combat.skill.CombatSkillEffect;

/**
 * 选取目标的规则
 * @author Dagangzi
 *
 */
public class DefaultTargetSearcher {
	/**
	 * 前排搜索优先级
	 */
	public final static int[][] priorityFrontrowSearch = { { 0, 0, 0, 0, 0, 0 }, { 7, 8, 9, 10, 11, 12 },
			{ 8, 7, 9, 11, 10, 12 }, { 9, 8, 7, 12, 11, 10 }, { 7, 8, 9, 10, 11, 12 }, { 8, 7, 9, 11, 10, 12 },
			{ 9, 8, 7, 12, 11, 10 }, { 1, 2, 3, 4, 5, 6 }, { 2, 1, 3, 5, 4, 6 }, { 3, 2, 1, 6, 5, 4 },
			{ 1, 2, 3, 4, 5, 6 }, { 2, 1, 3, 5, 4, 6 }, { 3, 2, 1, 6, 5, 4 }, };

	/**
	 * 后排搜索优先级
	 */
	public final static int[][] priorityBackrowSearch = { { 0, 0, 0, 0, 0, 0 }, { 10, 11, 12, 7, 8, 9 },
			{ 11, 10, 12, 8, 7, 9 }, { 12, 11, 10, 9, 8, 7 }, { 10, 11, 12, 7, 8, 9 }, { 11, 10, 12, 8, 7, 9 },
			{ 12, 11, 10, 9, 8, 7 }, { 4, 5, 6, 1, 2, 3 }, { 5, 4, 6, 2, 1, 3 }, { 6, 5, 4, 3, 2, 1 },
			{ 4, 5, 6, 1, 2, 3 }, { 5, 4, 6, 2, 1, 3 }, { 6, 5, 4, 3, 2, 1 }, };

	/**
	 * 附近搜索优先级
	 */
	public final static int[][] priorityNearbySearch = { { 0, 0, 0, 0, 0, 0 }, { 1, 2, 3, 4, 5, 6 },
			{ 2, 1, 3, 5, 4, 6 }, { 3, 2, 1, 6, 5, 4 }, { 4, 5, 6, 1, 2, 3 }, { 5, 4, 6, 2, 1, 3 },
			{ 6, 5, 4, 3, 2, 1 }, { 7, 8, 9, 10, 11, 12 }, { 8, 7, 9, 11, 10, 12 }, { 9, 8, 7, 12, 11, 10 },
			{ 10, 11, 12, 7, 8, 9 }, { 11, 10, 12, 8, 7, 9 }, { 12, 11, 10, 9, 8, 7 } };
	
	/**
	 * 附近目标
	 */
	public final static int[][] nearbySearch = { { 0, 0, 0, 0 }, { 1, 2, 4, 0 },
			{ 2, 1, 3, 5 }, { 3, 2, 6, 0 }, { 4, 5, 1, 0 }, { 5, 4, 6, 2 },
			{ 6, 5, 3, 0 }, { 7, 8, 10, 0 }, { 8, 9, 7, 11 }, { 9, 8, 12, 0 },
			{ 10, 11, 7, 0 }, { 11, 12, 10, 0 }, { 12, 11, 9, 0 } };

	/**
	 * 优先选取前排攻击目标
	 * @param skillEffect
	 * @param source
	 * @param target
	 * @return
	 */
	public static CombatUnit byFrontrowSearch(CombatSkillEffect skillEffect, CombatUnit source, CombatUnit target) {
		int[] priority = priorityFrontrowSearch[source.getId()];
		for (int unitId : priority) {
			CombatUnit combatUnit = source.getParent().getParent().getCombatUnitById(unitId);
			if (combatUnit != null && !combatUnit.dead()) {
				return combatUnit;
			}
		}
		return null;
	}

	/**
	 * 优先选取后排攻击目标
	 * @param skillEffect
	 * @param source
	 * @param target
	 * @return
	 */
	public static CombatUnit byBackrowSearch(CombatSkillEffect skillEffect, CombatUnit source, CombatUnit target) {
		int[] priority = priorityBackrowSearch[source.getId()];
		for (int unitId : priority) {
			CombatUnit combatUnit = source.getParent().getParent().getCombatUnitById(unitId);
			if (combatUnit != null && !combatUnit.dead()) {
				return combatUnit;
			}
		}
		return null;
	}

	/**
	 * 从target开始选取目标
	 * @param skillEffect
	 * @param source
	 * @param target
	 * @return
	 */
	public static CombatUnit byTargetSearch(CombatSkillEffect skillEffect, CombatUnit source, CombatUnit target) {
		int[] priority = priorityNearbySearch[target.getId()];
		for (int unitId : priority) {
			CombatUnit combatUnit = source.getParent().getParent().getCombatUnitById(unitId);
			if (combatUnit != null && !combatUnit.dead()) {
				return combatUnit;
			}
		}
		return null;
	}
}

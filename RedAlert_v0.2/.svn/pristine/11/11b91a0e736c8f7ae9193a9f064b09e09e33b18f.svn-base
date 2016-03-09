package com.youxigu.dynasty2.combat.skill;

import java.util.List;

import com.youxigu.dynasty2.combat.domain.CombatUnit;

/**
 * 对某个Effect的施放对象的搜索器
 * @author Administrator
 *
 */
public interface ITargetSearcher {
	/**
	 * 寻找攻击目标
	 * @param skillEffect
	 * @param source
	 * @param target 通常情况下taregt=null 上一次攻打我的unit
	 * @return
	 */
	List<CombatUnit> searchTarget(CombatSkillEffect skillEffect, CombatUnit source, CombatUnit target);
}

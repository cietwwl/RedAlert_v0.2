package com.youxigu.dynasty2.combat.attack;

import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.combat.domain.action.CombatUnitAction;
/**
 * 攻击接口
 * @author Dagangzi
 *
 * @param <T>
 */
public interface IAttack<T extends CombatUnit> {
	/**
	 * 攻击
	 * @param attacker
	 * @param defender
	 * @param action
	 * @return
	 */
	boolean attack(T attacker,T defender,CombatUnitAction action);
	
	/**
	 * 返回攻击方式
	 * @return
	 */
	short getAttackType();
}

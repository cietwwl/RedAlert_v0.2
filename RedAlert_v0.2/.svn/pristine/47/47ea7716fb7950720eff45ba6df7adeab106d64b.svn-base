package com.youxigu.dynasty2.combat.attack.combat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.youxigu.dynasty2.combat.attack.IAttack;
import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.combat.domain.action.CombatUnitAction;

/**
 * 战略战 战斗效果接口
 * 
 * @author Administrator
 * 
 */
public abstract class AbstractCombatAttack implements IAttack<CombatUnit> {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public boolean attack(CombatUnit attacker, CombatUnit defender, CombatUnitAction action) {
		// 战斗前
		doBeforeAttack(attacker, defender, action);

		boolean attack = doAttack(attacker, defender, action);
		// 战斗后
		doAfterAttack(attacker, defender, action);

		return attack;
	}

	protected abstract boolean doAttack(CombatUnit attacker, CombatUnit defender, CombatUnitAction action);

	protected void doBeforeAttack(CombatUnit attacker, CombatUnit defender, CombatUnitAction action) {
		// TODO
	}

	/**
	 * 攻击后的行为
	 * @param attacker
	 * @param defender
	 * @param action
	 */
	protected void doAfterAttack(CombatUnit attacker, CombatUnit defender, CombatUnitAction action) {
		//记录被打击状态
		defender.addTmpStatus("" + this.getAttackType(), attacker.getId());
	}

}

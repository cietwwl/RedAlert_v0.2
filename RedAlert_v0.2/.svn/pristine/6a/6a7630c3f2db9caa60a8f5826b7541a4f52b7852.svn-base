package com.youxigu.dynasty2.combat.domain.action;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.youxigu.dynasty2.combat.domain.Combat;
import com.youxigu.dynasty2.combat.domain.CombatConstants;
import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.combat.proto.CombatMsg;

/**
 * 普通攻击动作
 * 
 * @author Administrator
 * 
 */
public class AttackAction extends CombatUnitAction {
	protected List<AbstractCombatAction> results = new LinkedList<AbstractCombatAction>();
	public AttackAction(){
		super();
	}
	public AttackAction(CombatUnit attack) {
		super(CombatConstants.ACTION_ATTACK, attack);
	}

	public AttackAction(short action, CombatUnit attack) {
		super(action, attack);	
	}

	public void addResult(AbstractCombatAction result) {
		results.add(result);
	}

	public List<AbstractCombatAction> getResults() {
		return results;
	}

	public void setResults(List<AbstractCombatAction> results) {
		this.results = results;
	}

	public void desc(Combat combat, StringBuilder sb) {
		// TODO:
	}
	
	public List<CombatMsg.AbstractCombatAction> getSubActions() {
		List<CombatMsg.AbstractCombatAction> values = new ArrayList<CombatMsg.AbstractCombatAction>();
		List<AbstractCombatAction> results = this.results;
		if (results != null && results.size() > 0) {
			for (AbstractCombatAction tmp : results) {
				CombatMsg.AbstractCombatAction.Builder abstractCombatAction = CombatMsg.AbstractCombatAction
						.newBuilder();
				abstractCombatAction.setActionId(tmp.getAction());
				abstractCombatAction.setActionBytes(tmp.toProBytes());
				values.add(abstractCombatAction.build());
			}
		}
		return values;
	}
}

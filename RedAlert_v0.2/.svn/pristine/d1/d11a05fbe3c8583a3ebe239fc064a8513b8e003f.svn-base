package com.youxigu.dynasty2.combat.domain.action;

import com.google.protobuf.ByteString;
import com.youxigu.dynasty2.combat.domain.CombatUnit;

/**
 * 记录战斗中每一个战斗单元的动作，包括移动，攻击，施放技能等等
 * @author Dagangzi
 *
 */
public class CombatUnitAction extends AbstractCombatAction {

	/**
	 * 执行动作的战斗单元ID
	 */
	protected int ownerId;

	public CombatUnitAction() {
		super();
	}

	public CombatUnitAction(short action, CombatUnit owner) {
		this.action = action;
		if (owner != null) {
			this.ownerId = owner.getId();
		}
	}

	public CombatUnitAction(short action, int ownerId) {
		this.action = action;
		this.ownerId = ownerId;
	}

	public int getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}

	public void addResult(AbstractCombatAction result) {
		//do nothing
	}
	
	@Override
	public ByteString toProBytes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void desc(StringBuilder sb) {
		// TODO Auto-generated method stub

	}
}

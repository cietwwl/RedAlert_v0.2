package com.youxigu.dynasty2.combat.domain.action;

import com.google.protobuf.ByteString;
import com.youxigu.dynasty2.combat.domain.CombatConstants;
import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.combat.proto.CombatMsg;

/**
 * 护盾攻击
 * @author Dagangzi
 *
 */
public class ShieldChangeAction extends CombatUnitAction {
	private short subType;
	private int shield;

	public ShieldChangeAction(short action, CombatUnit unit, short subAttackType, int shield) {
		super(action, unit);
		this.subType = subAttackType;
		this.shield = shield;
	}

	public short getSubType() {
		return subType;
	}

	public void setSubType(short subType) {
		this.subType = subType;
	}

	public int getShield() {
		return shield;
	}

	public void setShield(int shield) {
		this.shield = shield;
	}

	@Override
	public ByteString toProBytes() {
		CombatMsg.ShieldChangeAction.Builder shieldChangeAction = CombatMsg.ShieldChangeAction
				.newBuilder();
		shieldChangeAction.setActionId(this.getAction());
		shieldChangeAction.setOwnerId(this.getOwnerId());
		shieldChangeAction.setShield(this.shield);
		shieldChangeAction.setSubType(this.subType);
		return shieldChangeAction.build().toByteString();
	}

	@Override
	public void desc(StringBuilder sb) {
		sb.append("********执行了 ")
				.append(CombatConstants.actionDescMap.get(this.getAction()))
				.append("\n");
		sb.append(" 参数 ").append("ownerId=").append(ownerId).append("\n");
		sb.append("shield=").append(shield).append("\n");
		sb.append("subType=").append(subType).append("\n");
	}
}

package com.youxigu.dynasty2.combat.domain.action;

import com.google.protobuf.ByteString;
import com.youxigu.dynasty2.combat.domain.CombatConstants;
import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.combat.proto.CombatMsg;

/**
 * 减士气
 * @author Dagangzi
 *
 */
public class MoraleDecAction extends CombatUnitAction {

	private short subType;
	private int morale;

	public MoraleDecAction(short action, CombatUnit unit, short subAttackType, int morale) {
		super(action, unit);
		this.subType = subAttackType;
		this.morale = morale;
	}

	public short getSubType() {
		return subType;
	}

	public void setSubType(short subType) {
		this.subType = subType;
	}

	public int getMorale() {
		return morale;
	}

	public void setMorale(int morale) {
		this.morale = morale;
	}

	@Override
	public ByteString toProBytes() {
		//减士气-213
		CombatMsg.MoraleDecAction.Builder moraleDecAction = CombatMsg.MoraleDecAction.newBuilder();
		moraleDecAction.setActionId(this.getAction());
		moraleDecAction.setOwnerId(this.getOwnerId());
		moraleDecAction.setSubType(this.subType);
		moraleDecAction.setMorale(this.morale);
		return moraleDecAction.build().toByteString();
	}

	@Override
	public void desc(StringBuilder sb) {
		sb.append("********执行了 ")
				.append(CombatConstants.actionDescMap.get(this.getAction()))
				.append("\n");
		sb.append(" 参数 ").append("ownerId=").append(ownerId).append("\n");
		sb.append("subType=").append(subType).append("\n");
		sb.append("morale").append(morale).append("\n");
	}
}

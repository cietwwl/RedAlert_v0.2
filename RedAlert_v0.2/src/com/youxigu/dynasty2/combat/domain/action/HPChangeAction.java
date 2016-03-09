package com.youxigu.dynasty2.combat.domain.action;

import com.google.protobuf.ByteString;
import com.youxigu.dynasty2.combat.domain.CombatConstants;
import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.combat.proto.CombatMsg;

/**
 * 伤血，按实际效果传值，扣到0为止
 * @author Dagangzi
 *
 */
public class HPChangeAction extends CombatUnitAction {

	private short subType;
	private int hp;
	private int[] sourceIds;//发起攻击的人

	public HPChangeAction(short action, CombatUnit unit, short subAttackType, int hp, int[] sourceIds) {
		super(action, unit);
		this.subType = subAttackType;
		this.hp = hp;
		this.sourceIds = sourceIds;
	}

	public short getSubType() {
		return subType;
	}

	public void setSubType(short subType) {
		this.subType = subType;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int[] getSourceIds() {
		return sourceIds;
	}

	public void setSourceIds(int[] sourceIds) {
		this.sourceIds = sourceIds;
	}

	@Override
	public ByteString toProBytes() {
		//伤血-210
		CombatMsg.HPChangeAction.Builder hpChangeAction = CombatMsg.HPChangeAction.newBuilder();
		hpChangeAction.setActionId(this.getAction());
		hpChangeAction.setOwnerId(this.getOwnerId());
		hpChangeAction.setSubType(this.subType);
		hpChangeAction.setHp(this.hp);
		if(sourceIds != null && sourceIds.length >0) {
			for(int uId : sourceIds) {
				hpChangeAction.addSourceIds(uId);
			}
		}
		return hpChangeAction.build().toByteString();
	}
	
	@Override
	public void desc(StringBuilder sb) {
		sb.append("********执行了 ")
				.append(CombatConstants.actionDescMap.get(this.getAction()))
				.append("\n");
		sb.append(" 参数 ").append("ownerId=").append(ownerId).append("\n");
		sb.append("subType=").append(subType).append("\n");
		sb.append("hp=").append(hp).append("\n");
		sb.append("sourceIds=");
		if (sourceIds != null && sourceIds.length > 0) {
			for (int tmp : sourceIds) {
				sb.append(tmp).append(",");
			}
		}
		sb.append("\n");
	}
}

package com.youxigu.dynasty2.combat.domain.action;

import com.google.protobuf.ByteString;
import com.youxigu.dynasty2.combat.domain.CombatConstants;
import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.combat.proto.CombatMsg;

/**
 * DOT伤血，按实际效果传值，扣到0为止
 * 
 * @author Dagangzi
 *
 */
public class DotHPChangeAction extends CombatUnitAction {
	private int hp;
	private int effId;
	private String effKey;

	public DotHPChangeAction(short action, CombatUnit unit, int hp, int effId,
			String effKey) {
		super(action, unit);
		this.hp = hp;
		this.effId = effId;
		this.effKey = effKey;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getEffId() {
		return effId;
	}

	public void setEffId(int effId) {
		this.effId = effId;
	}

	public String getEffKey() {
		return effKey;
	}

	public void setEffKey(String effKey) {
		this.effKey = effKey;
	}

	@Override
	public ByteString toProBytes() {
		// 伤血-210
		CombatMsg.DotHPChangeAction.Builder hpChangeAction = CombatMsg.DotHPChangeAction
				.newBuilder();
		hpChangeAction.setActionId(this.getAction());
		hpChangeAction.setOwnerId(this.getOwnerId());
		hpChangeAction.setHp(this.hp);
		hpChangeAction.setEffId(this.effId);
		hpChangeAction.setEffKey(this.effKey);
		return hpChangeAction.build().toByteString();
	}

	@Override
	public void desc(StringBuilder sb) {
		sb.append("********执行了 ")
				.append(CombatConstants.actionDescMap.get(this.getAction()))
				.append("\n");
		sb.append(" 参数 ").append("ownerId=").append(ownerId).append("\n");
		sb.append("hp=").append(hp).append("\n");
		sb.append("effId=").append(effId).append("\n");
		sb.append("effKey=").append(effKey).append("\n");
	}
}

package com.youxigu.dynasty2.combat.domain.action;

import com.google.protobuf.ByteString;
import com.youxigu.dynasty2.combat.domain.CombatConstants;
import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.combat.proto.CombatMsg;

/**
 * Dot护盾攻击
 * 
 * @author Dagangzi
 *
 */
public class DotShieldChangeAction extends CombatUnitAction {
	private int effId;
	private String effKey;
	private int shield;

	public DotShieldChangeAction(short action, CombatUnit unit, int effId,
			String effKey, int shield) {
		super(action, unit);
		this.effId = effId;
		this.effKey = effKey;
		this.shield = shield;
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

	public int getShield() {
		return shield;
	}

	public void setShield(int shield) {
		this.shield = shield;
	}

	@Override
	public ByteString toProBytes() {
		CombatMsg.DotShieldChangeAction.Builder shieldChangeAction = CombatMsg.DotShieldChangeAction
				.newBuilder();
		shieldChangeAction.setActionId(this.getAction());
		shieldChangeAction.setOwnerId(this.getOwnerId());
		shieldChangeAction.setShield(this.shield);
		shieldChangeAction.setEffId(this.effId);
		shieldChangeAction.setEffKey(this.effKey);
		return shieldChangeAction.build().toByteString();
	}

	@Override
	public void desc(StringBuilder sb) {
		sb.append("********执行了 ")
				.append(CombatConstants.actionDescMap.get(this.getAction()))
				.append("\n");
		sb.append(" 参数 ").append("ownerId=").append(ownerId).append("\n");
		sb.append("shield=").append(shield).append("\n");
		sb.append("effId=").append(effId).append("\n");
		sb.append("effKey=").append(effKey).append("\n");
	}
}

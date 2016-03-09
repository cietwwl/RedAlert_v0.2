package com.youxigu.dynasty2.combat.domain.action;

import com.google.protobuf.ByteString;
import com.youxigu.dynasty2.combat.domain.CombatConstants;
import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.combat.proto.CombatMsg;

/**
 * 给一个战斗单元移除效果
 * @author Dagangzi
 *
 */
public class RemoveCombatUnitEffectAction extends CombatUnitAction {
	private int effId;// 效果Id

	public RemoveCombatUnitEffectAction(CombatUnit owner, int effId) {
		super(CombatConstants.ACTION_REMOVE_UNIT_SKILL_EFFECT, owner.getId());
		this.effId = effId;
	}

	public int getEffId() {
		return effId;
	}

	public void setEffId(int effId) {
		this.effId = effId;
	}

	@Override
	public ByteString toProBytes() {
		//给一个战斗单元移除效果-313
		CombatMsg.RemoveCombatUnitEffectAction.Builder removeCombatUnitEffectAction = CombatMsg.RemoveCombatUnitEffectAction.newBuilder();
		removeCombatUnitEffectAction.setActionId(this.getAction());
		removeCombatUnitEffectAction.setOwnerId(this.getOwnerId());
		removeCombatUnitEffectAction.setEffId(this.effId);
		return removeCombatUnitEffectAction.build().toByteString();
	}

	@Override
	public void desc(StringBuilder sb) {
		sb.append("********执行了 ")
				.append(CombatConstants.actionDescMap.get(this.getAction()))
				.append("\n");
		sb.append(" 参数 ").append("ownerId=").append(ownerId).append("\n");
		sb.append("effId=").append(effId).append("\n");
	}
}

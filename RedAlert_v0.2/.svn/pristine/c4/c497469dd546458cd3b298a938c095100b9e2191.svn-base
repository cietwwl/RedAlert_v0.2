package com.youxigu.dynasty2.combat.domain.action;

import com.google.protobuf.ByteString;
import com.youxigu.dynasty2.combat.domain.CombatConstants;
import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.combat.proto.CombatMsg;

/**
 * DOT类效果生效Action
 * @author Dagangzi
 *
 */
@Deprecated
public class ApplyDotEffectAction extends AttackAction {
	private int effId;

	public ApplyDotEffectAction(int effId, CombatUnit target) {
		super(CombatConstants.ACTION_SKILL_EFFECT_TRIGERED, target);
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
		//DOT类效果生效Action-314
		CombatMsg.ApplyDotEffectAction.Builder applyDotEffectAction = CombatMsg.ApplyDotEffectAction.newBuilder();
		applyDotEffectAction.setActionId(this.getAction());
		applyDotEffectAction.setOwnerId(this.getOwnerId());
		applyDotEffectAction.setEffId(this.getEffId());
		applyDotEffectAction.addAllResults(this.getSubActions());
		return applyDotEffectAction.build().toByteString();
	}
}

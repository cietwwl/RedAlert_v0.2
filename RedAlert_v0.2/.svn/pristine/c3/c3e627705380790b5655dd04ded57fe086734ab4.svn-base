package com.youxigu.dynasty2.combat.domain.action;

import com.google.protobuf.ByteString;
import com.youxigu.dynasty2.combat.domain.CombatConstants;
import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.combat.proto.CombatMsg;

/**
 * 给一个战斗单元增加效果
 * @author Dagangzi
 *
 */
public class AddCombatUnitEffectAction extends CombatUnitAction {
	private int effId;//效果Id
	private int value;//效果值
	private int round;//持续到的回合数

	public AddCombatUnitEffectAction(CombatUnit unit, int effId, int value, int round) {
		super(CombatConstants.ACTION_ADD_UNIT_SKILL_EFFECT, unit.getId());
		this.effId = effId;
		this.value = value;
		this.round = round;
	}

	public int getEffId() {
		return effId;
	}

	public void setEffId(int effId) {
		this.effId = effId;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	@Override
	public ByteString toProBytes() {
		//给一个战斗单元增加效果-312
		CombatMsg.AddCombatUnitEffectAction.Builder addCombatUnitEffectAction = CombatMsg.AddCombatUnitEffectAction
				.newBuilder();
		addCombatUnitEffectAction.setActionId(this.getAction());
		addCombatUnitEffectAction.setOwnerId(this.getOwnerId());
		addCombatUnitEffectAction.setEffId(this.getEffId());
		addCombatUnitEffectAction.setValue(this.getValue());
		addCombatUnitEffectAction.setRound(this.getRound());
		return addCombatUnitEffectAction.build().toByteString();
	}

	@Override
	public void desc(StringBuilder sb) {
		sb.append("********执行了 ")
				.append(CombatConstants.actionDescMap.get(this.getAction()))
				.append("\n");
		sb.append(" 参数 ").append("ownerId=").append(ownerId).append("\n");
		sb.append("effId=").append(effId).append("\n");
		sb.append("value").append(value).append("\n");
		sb.append("round").append(round).append("\n");
	}
}

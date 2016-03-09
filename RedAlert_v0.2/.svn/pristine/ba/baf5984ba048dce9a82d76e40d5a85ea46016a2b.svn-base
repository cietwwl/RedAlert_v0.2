package com.youxigu.dynasty2.combat.domain.action;

import com.google.protobuf.ByteString;
import com.youxigu.dynasty2.combat.domain.CombatConstants;
import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.combat.proto.CombatMsg;

/**
 * 释放技能Action
 * @author Dagangzi
 *
 */
public class FireSkillAction extends AttackAction {
	private int skillId;
	private short cold;//冷却回合数
	private int morale;//消耗士气

	public FireSkillAction() {
		super();
	}

	public FireSkillAction(int skillId, short cold, int morale, CombatUnit attack) {
		super(CombatConstants.ACTION_FIRE_SKILL, attack);
		this.skillId = skillId;
		this.cold = cold;
		this.morale = morale;
	}

	public int getSkillId() {
		return skillId;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}

	public short getCold() {
		return cold;
	}

	public void setCold(short cold) {
		this.cold = cold;
	}

	public int getMorale() {
		return morale;
	}

	public void setMorale(int morale) {
		this.morale = morale;
	}

	@Override
	public ByteString toProBytes() {
		CombatMsg.FireSkillAction.Builder fireSkillAction = CombatMsg.FireSkillAction.newBuilder();
		fireSkillAction.setActionId(this.getAction());
		fireSkillAction.setOwnerId(this.getOwnerId());
		fireSkillAction.setSkillId(this.skillId);
		fireSkillAction.setCold(this.cold);
		fireSkillAction.setMorale(this.morale);
		fireSkillAction.addAllResults(this.getSubActions());
		return fireSkillAction.build().toByteString();
	}

	@Override
	public void desc(StringBuilder sb) {
		sb.append("********执行了 ")
				.append(CombatConstants.actionDescMap.get(this.getAction()))
				.append("\n");
		sb.append(" 参数 ").append("ownerId=").append(ownerId).append("\n");
		sb.append("skillId=").append(skillId).append("\n");
		sb.append("cold=").append(cold).append("\n");
		sb.append("morale=").append(morale).append("\n");
		sb.append("results内容如下").append("\n");
		if (this.results != null && this.results.size() > 0) {
			for (AbstractCombatAction tmp : this.results) {
				tmp.desc(sb);
			}
		}
	}
}

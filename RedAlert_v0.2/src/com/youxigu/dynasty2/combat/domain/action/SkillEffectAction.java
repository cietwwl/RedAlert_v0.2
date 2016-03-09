package com.youxigu.dynasty2.combat.domain.action;

import com.google.protobuf.ByteString;
import com.youxigu.dynasty2.combat.domain.CombatConstants;
import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.combat.proto.CombatMsg;

/**
 * 释放效果Action
 * @author Dagangzi
 *
 */
public class SkillEffectAction extends AttackAction {
	private int effId;
	private String effKey;
	private int[] targetIds;
	private int[] missIds;
	private int[] ownerIds;
	private boolean unShow;// true 不展示 false 展示

	//不需要传递给前台
	private transient FireSkillAction parent;

	public SkillEffectAction() {
		super();
	}

	public SkillEffectAction(String effKey, FireSkillAction parent) {
		super(CombatConstants.ACTION_FIRE_EFFECT, null);
		this.effKey = effKey;
		this.parent = parent;
	}

	public SkillEffectAction(int effId, String effKey, FireSkillAction parent,
			CombatUnit attack, boolean unShow) {
		super(CombatConstants.ACTION_FIRE_EFFECT, attack);
		this.effId = effId;
		this.effKey = effKey;
		this.parent = parent;
		this.unShow = unShow;
	}

	public SkillEffectAction(int effId, String effKey, int[] targetIds,
			int[] ownerIds, FireSkillAction parent, CombatUnit attack,
			boolean unShow) {
		super(CombatConstants.ACTION_FIRE_EFFECT, attack);
		this.effId = effId;
		this.effKey = effKey;
		this.targetIds = targetIds;
		this.ownerIds = ownerIds;
		this.parent = parent;
		this.unShow = unShow;
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

	public int[] getTargetIds() {
		return targetIds;
	}

	public void setTargetIds(int[] targetIds) {
		this.targetIds = targetIds;
	}

	public FireSkillAction getParent() {
		return parent;
	}

	public void setParent(FireSkillAction parent) {
		this.parent = parent;
	}

	public int[] getMissIds() {
		return missIds;
	}

	public void setMissIds(int[] missIds) {
		this.missIds = missIds;
	}

	public int[] getOwnerIds() {
		return ownerIds;
	}

	public void setOwnerIds(int[] ownerIds) {
		this.ownerIds = ownerIds;
	}

	public boolean isUnShow() {
		return unShow;
	}

	public void setUnShow(boolean unShow) {
		this.unShow = unShow;
	}

	@Override
	public ByteString toProBytes() {
		CombatMsg.SkillEffectAction.Builder skillEffectAction = CombatMsg.SkillEffectAction.newBuilder();
		skillEffectAction.setActionId(this.getAction());
		if (ownerIds != null && ownerIds.length > 0) {
			for (int ownerId : ownerIds) {
				skillEffectAction.addOwnerIds(ownerId);
			}
		}
		skillEffectAction.setEffId(this.effId);
		skillEffectAction.setEffKey(this.effKey);
		
		if(targetIds != null && targetIds.length >0) {
			for (int target : this.targetIds) {
				skillEffectAction.addTargetIds(target);
			}
		}
		
		if(missIds != null && missIds.length >0) {
			for (int target : this.missIds) {
				skillEffectAction.addMissIds(target);
			}
		}
		skillEffectAction.addAllResults(this.getSubActions());
		return skillEffectAction.build().toByteString();
		
	}

	@Override
	public void desc(StringBuilder sb) {
		sb.append("********执行了 ")
				.append(CombatConstants.actionDescMap.get(this.getAction()))
				.append("\n");
		sb.append(" 参数 ").append("ownerIds=");
		if(ownerIds != null && ownerIds.length >0){
			for(int tmp : ownerIds){
				sb.append(tmp).append(",");
			}
		}
		sb.append("\n");
		sb.append("targetIds=");
		if(targetIds != null && targetIds.length >0){
			for(int tmp : targetIds){
				sb.append(tmp).append(",");
			}
		}
		sb.append("\n");
		sb.append("missIds=");
		if(missIds != null && missIds.length >0){
			for(int tmp : missIds){
				sb.append(tmp).append(",");
			}
		}
		sb.append("\n");
		sb.append("effId=").append(effId).append("\n");
		sb.append("effKey=").append(effKey).append("\n");

		sb.append("results内容如下").append("\n");
		if (this.results != null && this.results.size() > 0) {
			for (AbstractCombatAction tmp : this.results) {
				tmp.desc(sb);
			}
		}
	}
}

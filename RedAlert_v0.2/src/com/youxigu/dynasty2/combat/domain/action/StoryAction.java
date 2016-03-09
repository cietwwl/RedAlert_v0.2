package com.youxigu.dynasty2.combat.domain.action;

import com.google.protobuf.ByteString;
import com.youxigu.dynasty2.combat.domain.CombatConstants;
import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.combat.proto.CombatMsg;

/**
 * 剧情对话
 * @author Dagangzi
 *
 */
public class StoryAction extends CombatUnitAction {
	private int storyId;//对话id

	public StoryAction(short action, CombatUnit unit, int storyId) {
		super(action, unit);
		this.storyId = storyId;
	}

	public int getStoryId() {
		return storyId;
	}

	public void setStoryId(int storyId) {
		this.storyId = storyId;
	}

	@Override
	public ByteString toProBytes() {
		CombatMsg.StoryAction.Builder storyAction = CombatMsg.StoryAction.newBuilder();
		storyAction.setActionId(this.getAction());
		storyAction.setOwnerId(this.getOwnerId());
		storyAction.setStoryId(this.storyId);
		return storyAction.build().toByteString();
	}

	@Override
	public void desc(StringBuilder sb) {
		sb.append("********执行了 ")
				.append(CombatConstants.actionDescMap.get(this.getAction()))
				.append("\n");
		sb.append(" 参数 ").append("ownerId=").append(ownerId).append("\n");
		sb.append("storyId=").append(storyId).append("\n");
	}
}

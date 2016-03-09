package com.youxigu.dynasty2.combat.domain.action;

import com.google.protobuf.ByteString;
import com.youxigu.dynasty2.combat.domain.Combat;
import com.youxigu.dynasty2.combat.domain.CombatConstants;
import com.youxigu.dynasty2.combat.proto.CombatMsg;

/**
 * 回合发生变化 Action
 * @author Dagangzi
 *
 */
public class RoundChangeAction  extends AbstractCombatAction {

	private byte round;
	public RoundChangeAction(){
		
	}
	public RoundChangeAction(short action,byte round){
		this.action=action;
		this.round=round;
	}

	public byte getRound() {
		return round;
	}

	public void setRound(byte round) {
		this.round = round;
	}
	public void desc(Combat combat,StringBuilder sb){
		sb.append("开始第").append(round).append("回合\n");
	}
	
	@Override
	public ByteString toProBytes() {
		CombatMsg.RoundChangeAction.Builder roundChangeAction = CombatMsg.RoundChangeAction.newBuilder();
		roundChangeAction.setActionId(this.getAction());
		roundChangeAction.setRound(this.round);
		return roundChangeAction.build().toByteString();
	}

	@Override
	public void desc(StringBuilder sb) {
		sb.append("********执行了 ")
				.append(CombatConstants.actionDescMap.get(this.getAction()))
				.append("\n");
		sb.append(" 参数 ").append("round=").append(round).append("\n");
	}
}

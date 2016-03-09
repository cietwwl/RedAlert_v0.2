package com.youxigu.dynasty2.combat.domain.action;

import java.io.Serializable;

import com.google.protobuf.ByteString;
import com.youxigu.dynasty2.combat.proto.CombatMsg;

/**
 * 行为描述
 * @author Dagangzi
 *
 */
public abstract class AbstractCombatAction implements Serializable {
	protected short action;

	public short getAction() {
		return action;
	}

	public void setAction(short action) {
		this.action = action;
	}

	public CombatMsg.AbstractCombatAction toAbstractCombatAction() {
		CombatMsg.AbstractCombatAction.Builder abstractCombatAction = CombatMsg.AbstractCombatAction.newBuilder();
		abstractCombatAction.setActionId(this.getAction());
		return abstractCombatAction.build();
	}
	
	/**
	 * 转换为字节数组
	 * @return
	 */
	public abstract ByteString toProBytes();

	/**
	 * 行为描述
	 * 
	 * @param sb
	 */
	public abstract void desc(StringBuilder sb);
}

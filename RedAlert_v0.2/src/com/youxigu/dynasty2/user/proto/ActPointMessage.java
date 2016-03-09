package com.youxigu.dynasty2.user.proto;

import java.io.Serializable;

import com.google.protobuf.Message;
import com.youxigu.dynasty2.chat.EventMessage;
import com.youxigu.dynasty2.chat.ISendMessage;
import com.youxigu.dynasty2.chat.proto.CommonHead.ResponseHead;

/**
 * 推送行动力变化
 * 
 * @author Dagangzi
 * @date 2016年1月12日
 */
public class ActPointMessage implements ISendMessage, Serializable {
	private int actionPoint;// 当前行动力
	private int actionLimit;// 行动力上限

	public ActPointMessage() {
	}

	public ActPointMessage(int actionPoint, int actionLimit) {
		this.actionPoint = actionPoint;
		this.actionLimit = actionLimit;
	}

	public int getActionPoint() {
		return actionPoint;
	}

	public void setActionPoint(int actionPoint) {
		this.actionPoint = actionPoint;
	}

	public int getActionLimit() {
		return actionLimit;
	}

	public void setActionLimit(int actionLimit) {
		this.actionLimit = actionLimit;
	}

	@Override
	public Message build() {
		//必须包含responseHead
		UserMsg.ActPointSendEvent.Builder sEvent = UserMsg.ActPointSendEvent
				.newBuilder();
		ResponseHead.Builder headBr = ResponseHead.newBuilder();
		headBr.setCmd(EventMessage.TYPE_ACTPOINT_CHANGED);
		headBr.setRequestCmd(EventMessage.TYPE_ACTPOINT_CHANGED);
		sEvent.setResponseHead(headBr.build());
		
		sEvent.setActionPoint(this.actionPoint);
		sEvent.setActionLimit(this.actionLimit);
		return sEvent.build();
	}

	@Override
	public String toString() {
		return "actionPoint:" + this.actionPoint + ",actionLimit:"
				+ this.actionLimit;
	}
}

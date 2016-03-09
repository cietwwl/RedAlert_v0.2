package com.youxigu.dynasty2.user.proto;

import java.io.Serializable;

import com.google.protobuf.Message;
import com.youxigu.dynasty2.chat.EventMessage;
import com.youxigu.dynasty2.chat.ISendMessage;
import com.youxigu.dynasty2.chat.proto.CommonHead.ResponseHead;

/**
 * 推送体力变化
 * 
 * @author Dagangzi
 * @date 2016年1月12日
 */
public class HpPointMessage implements ISendMessage, Serializable {
	private int hpPoint;// 当前体力
	private int hpLimit;// 体力上限

	public HpPointMessage() {
	}

	public HpPointMessage(int hpPoint, int hpLimit) {
		this.hpPoint = hpPoint;
		this.hpLimit = hpLimit;
	}

	public int getHpPoint() {
		return hpPoint;
	}

	public void setHpPoint(int hpPoint) {
		this.hpPoint = hpPoint;
	}

	public int getHpLimit() {
		return hpLimit;
	}

	public void setHpLimit(int hpLimit) {
		this.hpLimit = hpLimit;
	}

	@Override
	public Message build() {
		//必须包含responseHead
		UserMsg.HpPointSendEvent.Builder sEvent = UserMsg.HpPointSendEvent
				.newBuilder();
		ResponseHead.Builder headBr = ResponseHead.newBuilder();
		headBr.setCmd(EventMessage.TYPE_HPPOINT_CHANGED);
		headBr.setRequestCmd(EventMessage.TYPE_HPPOINT_CHANGED);
		sEvent.setResponseHead(headBr.build());

		sEvent.setHpPoint(this.hpPoint);
		sEvent.setHpLimit(this.hpLimit);
		return sEvent.build();
	}

	@Override
	public String toString() {
		return "hpPoint:" + this.hpPoint + ",hpLimit:" + this.hpLimit;
	}
}

package com.youxigu.dynasty2.user.proto;

import java.io.Serializable;

import com.google.protobuf.Message;
import com.youxigu.dynasty2.chat.ISendMessage;
import com.youxigu.dynasty2.chat.proto.CommonHead.ResponseHead;

public class UserTitleEvent implements ISendMessage, Serializable {
	private int eventId;
	private int titleId;

	public UserTitleEvent() {
	}

	public UserTitleEvent(int eventId, int titleId) {
		this.eventId = eventId;
		this.titleId = titleId;
	}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public int getTitleId() {
		return titleId;
	}

	public void setTitleId(int titleId) {
		this.titleId = titleId;
	}

	@Override
	public Message build() {
		// 必须包含responseHead
		UserMsg.UserTitleEvent.Builder sEvent = UserMsg.UserTitleEvent
				.newBuilder();
		ResponseHead.Builder headBr = ResponseHead.newBuilder();
		headBr.setCmd(this.eventId);
		headBr.setRequestCmd(this.eventId);
		sEvent.setResponseHead(headBr.build());
		sEvent.setTitleId(this.titleId);
		return sEvent.build();
	}

	@Override
	public String toString() {
		return "titleId:" + this.titleId;
	}

}

package com.youxigu.dynasty2.develop.service.listener;

import java.io.Serializable;

import com.google.protobuf.Message;
import com.youxigu.dynasty2.chat.EventMessage;
import com.youxigu.dynasty2.chat.ISendMessage;
import com.youxigu.dynasty2.chat.proto.CommonHead.ResponseHead;
import com.youxigu.dynasty2.develop.proto.DevelopMsg;

/**
 * 推城池效果事件
 * @author Dagangzi
 *
 */
public class CastleEffMessage implements ISendMessage, Serializable {
	private String fieldName;//字段名
	private int fieldValue;//字段值

	public CastleEffMessage() {
	}

	public CastleEffMessage(String fieldName, int fieldValue) {
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public int getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(int fieldValue) {
		this.fieldValue = fieldValue;
	}

	@Override
	public Message build() {
		// 必须包含responseHead
		DevelopMsg.CastleEffSendEvent.Builder sEvent = DevelopMsg.CastleEffSendEvent.newBuilder();
		ResponseHead.Builder headBr = ResponseHead.newBuilder();
		headBr.setCmd(EventMessage.TYPE_RESOURCE_CHANGED);
		headBr.setRequestCmd(EventMessage.TYPE_RESOURCE_CHANGED);
		sEvent.setResponseHead(headBr.build());

		sEvent.setFieldName(this.fieldName);
		sEvent.setFieldValue(this.fieldValue);
		return sEvent.build();
	}
}

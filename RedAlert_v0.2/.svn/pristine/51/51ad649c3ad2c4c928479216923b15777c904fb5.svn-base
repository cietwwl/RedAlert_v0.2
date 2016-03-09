package com.youxigu.dynasty2.mission.proto;

import com.google.protobuf.Message;
import com.youxigu.dynasty2.chat.ISendMessage;
import com.youxigu.dynasty2.chat.proto.CommonHead.ResponseHead;

/**
 * 推送玩家主线任务完成未领奖的数量
 * 
 * @author Dagangzi
 * @date 2016年1月13日
 */
public class MainCountEventMsg implements ISendMessage, java.io.Serializable {
	private int mainNum;
	private int eventType;

	public MainCountEventMsg() {
	}

	public MainCountEventMsg(int mainNum, int eventType) {
		this.mainNum = mainNum;
		this.eventType = eventType;
	}

	public int getMainNum() {
		return mainNum;
	}

	public void setMainNum(int mainNum) {
		this.mainNum = mainNum;
	}

	public int getEventType() {
		return eventType;
	}

	public void setEventType(int eventType) {
		this.eventType = eventType;
	}

	@Override
	public Message build() {
		// 必须包含responseHead
		MissionMsg.SendEvent6003Define.Builder sEvent = MissionMsg.SendEvent6003Define
				.newBuilder();
		ResponseHead.Builder headBr = ResponseHead.newBuilder();
		headBr.setCmd(this.eventType);
		headBr.setRequestCmd(this.eventType);
		sEvent.setResponseHead(headBr.build());

		sEvent.setMainNum(this.mainNum);
		return sEvent.build();
	}

	@Override
	public String toString() {
		return "mainNum:" + this.mainNum;
	}
}

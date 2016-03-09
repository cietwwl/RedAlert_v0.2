package com.youxigu.dynasty2.mission.proto;

import com.google.protobuf.Message;
import com.youxigu.dynasty2.chat.ISendMessage;
import com.youxigu.dynasty2.chat.proto.CommonHead.ResponseHead;

/**
 * 广播世界任务完成数
 * 
 * @author Dagangzi
 * @date 2016年1月13日
 */
public class FinishCountBroadMsg implements ISendMessage, java.io.Serializable {
	private int finishNum;
	private int eventType;

	public FinishCountBroadMsg() {
	}

	public FinishCountBroadMsg(int finishNum, int eventType) {
		this.finishNum = finishNum;
		this.eventType = eventType;
	}

	public int getFinishNum() {
		return finishNum;
	}

	public void setFinishNum(int finishNum) {
		this.finishNum = finishNum;
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
		MissionMsg.SendBroad6004Define.Builder sEvent = MissionMsg.SendBroad6004Define
				.newBuilder();
		ResponseHead.Builder headBr = ResponseHead.newBuilder();
		headBr.setCmd(this.eventType);
		headBr.setRequestCmd(this.eventType);
		sEvent.setResponseHead(headBr.build());

		sEvent.setFinishNum(this.finishNum);
		return sEvent.build();
	}

	@Override
	public String toString() {
		return "finishNum:" + this.finishNum;
	}
}

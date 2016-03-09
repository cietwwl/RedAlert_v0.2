package com.youxigu.dynasty2.mission.proto;

import com.google.protobuf.Message;
import com.youxigu.dynasty2.chat.ISendMessage;

/**
 * 玩家任务，传给前台用的，只有任务部分数据
 * 
 * @author Administrator
 * 
 */
public class UserMissionView implements ISendMessage, java.io.Serializable {
	private static final long serialVersionUID = 4886946492622532961L;
	private int id;
	private int missionId;

	private long userId;
	private int status;// 未完成：0 已经完成没提交：1 已经完成提交过：2
	private int num1;// 已经完成的计数，分别对应4种完成条件

	private int num2;//

	private int num3;

	private int num4;

	private int num5;

	/**
	 * 完成次数，联盟循环任务使用
	 */
	private int completeNum;

	private int read;

	private long remainTime;// 世界任务剩余时间

	private int eventType;// event类型

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMissionId() {
		return missionId;
	}

	public void setMissionId(int missionId) {
		this.missionId = missionId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getNum1() {
		return num1;
	}

	public void setNum1(int num1) {
		this.num1 = num1;
	}

	public int getNum2() {
		return num2;
	}

	public void setNum2(int num2) {
		this.num2 = num2;
	}

	public int getNum3() {
		return num3;
	}

	public void setNum3(int num3) {
		this.num3 = num3;
	}

	public int getNum4() {
		return num4;
	}

	public void setNum4(int num4) {
		this.num4 = num4;
	}

	public int getNum5() {
		return num5;
	}

	public void setNum5(int num5) {
		this.num5 = num5;
	}

	public int getCompleteNum() {
		return completeNum;
	}

	public void setCompleteNum(int completeNum) {
		this.completeNum = completeNum;
	}

	public int getRead() {
		return read;
	}

	public void setRead(int read) {
		this.read = read;
	}

	public int getEventType() {
		return eventType;
	}

	public void setEventType(int eventType) {
		this.eventType = eventType;
	}

	public UserMissionView() {
	}

	public UserMissionView(int id, int missionId, long userId, int status,
			int num1, int num2, int num3, int num4, int num5, int completeNum,
			int read, int eventType) {
		this.id = id;
		this.missionId = missionId;
		this.userId = userId;
		this.status = status;
		this.num1 = num1;
		this.num2 = num2;
		this.num3 = num3;
		this.num4 = num4;
		this.num5 = num5;
		this.completeNum = completeNum;
		this.read = read;
		this.eventType = eventType;
	}

	public UserMissionView(int missionId, int status, int num1, int num2,
			int num3, int num4, int num5, int completeNum, int read,
			long remainTime, int eventType) {
		this.missionId = missionId;
		this.status = status;
		this.num1 = num1;
		this.num2 = num2;
		this.num3 = num3;
		this.num4 = num4;
		this.num5 = num5;
		this.completeNum = completeNum;
		this.read = read;
		this.remainTime = remainTime;
		this.eventType = eventType;
	}

	public long getRemainTime() {
		return remainTime;
	}

	public void setRemainTime(long remainTime) {
		this.remainTime = remainTime;
	}

	/**
	 * 任务的显示信息
	 * 
	 * @return
	 */
	public MissionMsg.UserMissionViewMsg convetMessage() {
		MissionMsg.UserMissionViewMsg.Builder sEvent = MissionMsg.UserMissionViewMsg
				.newBuilder();
		if (this.id > 0) {
			sEvent.setId(this.id);
		}
		sEvent.setMissionId(this.missionId);
		if (this.userId > 0) {
			sEvent.setUserId(this.userId);
		}
		sEvent.setStatus(this.status);
		sEvent.setNum1(this.num1);
		sEvent.setNum2(this.num2);
		sEvent.setNum3(this.num3);
		sEvent.setNum4(this.num4);
		sEvent.setNum5(this.num5);
		sEvent.setCompleteNum(this.completeNum);
		sEvent.setRead(this.read);
		sEvent.setRemainTime(this.remainTime);
		return sEvent.build();
	}

	@Override
	public Message build() {
		return null;

		// // 必须包含responseHead
		// MissionMsg.UserMissionSendEvent.Builder sEvent =
		// MissionMsg.UserMissionSendEvent
		// .newBuilder();
		// ResponseHead.Builder headBr = ResponseHead.newBuilder();
		// headBr.setCmd(this.eventType);
		// headBr.setRequestCmd(this.eventType);
		// sEvent.setResponseHead(headBr.build());
		// sEvent.setView(convetMessage());
		// return sEvent.build();
	}
}

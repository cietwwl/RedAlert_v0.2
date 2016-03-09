package com.youxigu.dynasty2.mission.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import com.youxigu.dynasty2.mission.proto.UserMissionView;

public class UserMission implements Serializable {
	private static final long serialVersionUID = -7497605633095845691L;
	public static final int STATUS_NEW = 0;// 未完成
	public static final int STATUS_COMPLETE = 1; // 已经完成没提交
	public static final int STATUS_COMMIT = 2;// 已经完成提交过

	protected int userMissionId;

	protected long userId;

	protected int missionId;

	protected Timestamp completeLimitTime;// 完成任务的最后时间

	protected int status;// 未完成：0 已经完成没提交：1 已经完成提交过：2

	protected int num1;// 消灭军队数量 或 攻打的城堡数量

	protected int num2;// 条件二数量

	protected int num3;

	protected int num4;

	protected int num5;

	/**
	 * 任务的加成值：威望任务使用， 完成条件增加整数倍 完成奖励增加整数倍
	 */
	protected int factor;

	/**
	 * 是否已读 =0 未读 =1已读
	 */
	protected int read0;

	/**
	 * 任务的完成次数
	 * 
	 * 联盟循环任务专用，不持久化
	 */
	protected transient int completeNum;

	/**
	 * 判断当前任务是否已经完成
	 * 
	 * @return
	 */
	public boolean hasComplete() {
		if (this.status == STATUS_COMPLETE)
			return true;
		if (this.status == STATUS_COMMIT)
			return true;
		return false;
	}

	public int getUserMissionId() {
		return userMissionId;
	}

	public void setUserMissionId(int userMissionId) {
		this.userMissionId = userMissionId;
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

	public Timestamp getCompleteLimitTime() {
		return completeLimitTime;
	}

	public void setCompleteLimitTime(Timestamp completeLimitTime) {
		this.completeLimitTime = completeLimitTime;
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

	public int getNum(int index) {
		if (index == 1) {
			return num1;
		} else if (index == 2) {
			return num2;
		} else if (index == 3) {
			return num3;
		} else if (index == 4) {
			return num4;
		} else {
			return num5;
		}
	}

	public void setNum(int index, int value) {
		if (index == 1) {
			num1 = value;
		} else if (index == 2) {
			num2 = value;
		} else if (index == 3) {
			num3 = value;
		} else if (index == 4) {
			num4 = value;
		} else {
			num5 = value;
		}
	}

	public int getFactor() {
		return factor < 1 ? 1 : factor;
	}

	public void setFactor(int factor) {
		this.factor = factor;
	}

	public int getRead0() {
		return read0;
	}

	public void setRead0(int read0) {
		this.read0 = read0;
	}

	public UserMissionView getView(int eventType) {
		UserMissionView view = new UserMissionView(userMissionId, missionId,
				userId, status, num1, num2, num3, num4, num5, completeNum,
				read0, eventType);
		return view;
	}

}

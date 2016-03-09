package com.youxigu.dynasty2.activity.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import com.youxigu.dynasty2.activity.enums.ActivityType;

public class OperateActivity implements Serializable {
	private static final long serialVersionUID = -8086578308039513159L;
	/**
	 * 活动id，平台生成唯一id
	 */
	private long actId;
	/** @see ActivityType */
	private int type;// 活动类型
	private String actName;// 活动名称
	private Timestamp startTime;// 开始时间
	private Timestamp endTime;// 结束时间
	private Timestamp maxTime;// 最大结束时间
	private String rewardContext;// 奖励相关，
	/** 1到期后自动发送奖励 */
	private int autoReward;
	/** 状态值。。标记奖励是否已经发放 */
	private int status;

	private transient BaseReward reward;
	/** 状态的值的第一位表示是否已经自动发奖 */
	public static final int PUBLIC_AUTO_REWARD = 1;

	public long getActId() {
		return actId;
	}

	public void setActId(long actId) {
		this.actId = actId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getActName() {
		return actName;
	}

	public void setActName(String actName) {
		this.actName = actName;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public Timestamp getMaxTime() {
		return maxTime;
	}

	public void setMaxTime(Timestamp maxTime) {
		this.maxTime = maxTime;
	}

	public String getRewardContext() {
		return rewardContext;
	}

	public void setRewardContext(String rewardContext) {
		this.rewardContext = rewardContext;
	}

	public BaseReward getReward() {
		return reward;
	}

	public void setReward(BaseReward reward) {
		this.reward = reward;
	}

	public int getAutoReward() {
		return autoReward;
	}

	/**
	 * true表示自动发奖
	 * 
	 * @return
	 */
	public boolean isAutoReward() {
		return getAutoReward() == 1;
	}

	public void setAutoReward(int autoReward) {
		this.autoReward = autoReward;
	}

	public ActivityType getActivityType() {
		return ActivityType.valueOf(getType());
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * 判断是否已经自动发奖了
	 * 
	 * @return
	 */
	public boolean isPublicAutoReward() {
		return (this.status & PUBLIC_AUTO_REWARD) == PUBLIC_AUTO_REWARD;
	}

	public void updatePublicAutoReward() {
		this.status = ((this.status << 1) | PUBLIC_AUTO_REWARD);
	}
}

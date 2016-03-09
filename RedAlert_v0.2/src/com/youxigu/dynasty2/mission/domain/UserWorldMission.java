package com.youxigu.dynasty2.mission.domain;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 
 * @author Dagangzi
 * @date 2016年1月8日
 */
public class UserWorldMission implements Serializable {
	private int id;
	private long userId;
	private int missionId;
	private Timestamp finishDttm;
	private Timestamp awardDttm;

	public UserWorldMission(long userId, int missionId, Timestamp finishDttm,
			Timestamp awardDttm) {
		this.userId = userId;
		this.missionId = missionId;
		this.finishDttm = finishDttm;
		this.awardDttm = awardDttm;
	}

	public UserWorldMission() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getMissionId() {
		return missionId;
	}

	public void setMissionId(int missionId) {
		this.missionId = missionId;
	}

	public Timestamp getFinishDttm() {
		return finishDttm;
	}

	public void setFinishDttm(Timestamp finishDttm) {
		this.finishDttm = finishDttm;
	}

	public Timestamp getAwardDttm() {
		return awardDttm;
	}

	public void setAwardDttm(Timestamp awardDttm) {
		this.awardDttm = awardDttm;
	}
}

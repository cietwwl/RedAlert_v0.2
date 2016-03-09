package com.youxigu.dynasty2.mission.domain;

import java.io.Serializable;

import com.youxigu.dynasty2.mission.service.IMissionCompleteChecker;

/**
 * 任务的完成条件
 * 
 * @author Administrator
 *
 */
public class MissionLimit implements Serializable {
	private static final long serialVersionUID = 5306685683848542389L;
	private int missioncompleteId;
	private int entId;
	private int entNum;
	private String entType;
	private String octType;// 计数器类型

	private transient IMissionCompleteChecker limitChecker;

	public int getEntId() {
		return entId;
	}

	public void setEntId(int entId) {
		this.entId = entId;
	}

	public String getEntType() {
		return entType;
	}

	public void setEntType(String entType) {
		this.entType = entType;
	}

	public String getOctType() {
		return octType;
	}

	public void setOctType(String octType) {
		this.octType = octType;
	}

	public int getEntNum() {
		return entNum;
	}

	public void setEntNum(int entNum) {
		this.entNum = entNum;
	}

	public int getMissioncompleteId() {
		return missioncompleteId;
	}

	public void setMissioncompleteId(int missioncompleteId) {
		this.missioncompleteId = missioncompleteId;
	}

	public void setLimitChecker(IMissionCompleteChecker limitChecker) {
		this.limitChecker = limitChecker;
	}

	public IMissionCompleteChecker getLimitChecker() {
		return limitChecker;
	}
}

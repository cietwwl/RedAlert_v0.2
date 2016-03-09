package com.youxigu.dynasty2.risk.domain;

import java.sql.Timestamp;

public class UserRiskData implements java.io.Serializable {
	private long userId;
	/**
	 * CD的到期时间
	 */
	private Timestamp cdDttm;
	/**
	 * 本日执行清除CD的次数
	 */
	private int cdNum;
	/**
	 * 执行清除CD的最后的日期(天)
	 */
	private int cdDay;
	
	/**
	 * 本日挑战关卡次数清除的次数
	 */
	private int clearJoinNum;
	private int clearDay;
	
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}

	public Timestamp getCdDttm() {
		return cdDttm;
	}
	public void setCdDttm(Timestamp cdDttm) {
		this.cdDttm = cdDttm;
	}
	public int getCdNum() {
		return cdNum;
	}
	public void setCdNum(int cdNum) {
		this.cdNum = cdNum;
	}
	public int getCdDay() {
		return cdDay;
	}
	public void setCdDay(int cdDay) {
		this.cdDay = cdDay;
	}
	public int getClearJoinNum() {
		return clearJoinNum;
	}
	public void setClearJoinNum(int clearJoinNum) {
		this.clearJoinNum = clearJoinNum;
	}
	public int getClearDay() {
		return clearDay;
	}
	public void setClearDay(int clearDay) {
		this.clearDay = clearDay;
	}


}

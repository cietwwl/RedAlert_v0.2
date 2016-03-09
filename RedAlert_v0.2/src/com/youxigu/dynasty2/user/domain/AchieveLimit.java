package com.youxigu.dynasty2.user.domain;

import java.io.Serializable;

import com.youxigu.dynasty2.user.service.IAchieveCompleteChecker;

/**
 * 成就完成个数检查
 * 
 * @author Dagangzi
 * @date 2016年1月25日
 */
public class AchieveLimit implements Serializable {
	private int achieveId;
	private String octType;// 计数器类型
	private int para1;
	private int para2;
	private int para3;
	private int para4;
	private int para5;

	private transient IAchieveCompleteChecker limitChecker;

	public int getAchieveId() {
		return achieveId;
	}

	public void setAchieveId(int achieveId) {
		this.achieveId = achieveId;
	}

	public String getOctType() {
		return octType;
	}

	public void setOctType(String octType) {
		this.octType = octType;
	}

	public int getPara1() {
		return para1;
	}

	public void setPara1(int para1) {
		this.para1 = para1;
	}

	public int getPara2() {
		return para2;
	}

	public void setPara2(int para2) {
		this.para2 = para2;
	}

	public int getPara3() {
		return para3;
	}

	public void setPara3(int para3) {
		this.para3 = para3;
	}

	public int getPara4() {
		return para4;
	}

	public void setPara4(int para4) {
		this.para4 = para4;
	}

	public int getPara5() {
		return para5;
	}

	public void setPara5(int para5) {
		this.para5 = para5;
	}

	public IAchieveCompleteChecker getLimitChecker() {
		return limitChecker;
	}

	public void setLimitChecker(IAchieveCompleteChecker limitChecker) {
		this.limitChecker = limitChecker;
	}
}

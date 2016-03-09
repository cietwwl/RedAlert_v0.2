package com.youxigu.dynasty2.mission.domain;

import java.io.Serializable;

/**
 * 任务奖励:
 * 由于之前的设计已经固定，数据库不存在这个表，由Mission中的award配置转化出来 
 * 
 * @author Administrator
 *
 */
public class MissionAward implements Serializable {
	private static final long serialVersionUID = -4483106171775267814L;
	/**
	 * 奖励类型
	 */
	private String awardtype;
	/**
	 * 奖励实体ID
	 */
	private int entId;
	
	/**
	 * 奖励数量：可以为负值
	 */
	private int num;

	public MissionAward(){
		
	}
	public MissionAward(String awardtype,int entId,int num){
		this.awardtype = awardtype;
		this.entId = entId;
		this.num = num;
		
	}
	public String getAwardtype() {
		return awardtype;
	}

	public void setAwardtype(String awardtype) {
		this.awardtype = awardtype;
	}

	public int getEntId() {
		return entId;
	}

	public void setEntId(int entId) {
		this.entId = entId;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	
}

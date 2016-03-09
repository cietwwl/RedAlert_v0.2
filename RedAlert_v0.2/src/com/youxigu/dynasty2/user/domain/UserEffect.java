package com.youxigu.dynasty2.user.domain;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 用户当前使用的效果
 * 存放科技和buff
 * @author Administrator
 * 
 */
public class UserEffect implements Serializable {
	
	public static final String USER_EFFECT_LOCKER_PREFIX="USER_EFFECT_";
	
	public static final String EFFECT_POPUL_INCREASE_SPEED = "POPUL_INCREASE_SPEED";//城池人口增度
	
	private int id;
	private long userId;
	// private int effId;// 效果ID
	private String effTypeId;// 效果类型，相同效果类型可能有多个效果，根据实体类型来确定
	// private String entTypeId;// 实体类型,同类型实体产生的同类型效果要累加或者延时间
	private int entId;// 实体ID,同实体产生的同类型效果要累加或者覆盖或者延时间
	private int absValue;
	private int perValue;
//	private int showFlag;
	private Timestamp expireDttm;

	public int getEntId() {
		return entId;
	}

	public void setEntId(int entId) {
		this.entId = entId;
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

	public String getEffTypeId() {
		return effTypeId;
	}

	public void setEffTypeId(String effTypeId) {
		this.effTypeId = effTypeId;
	}

	public int getAbsValue() {
		return absValue;
	}

	public void setAbsValue(int absValue) {
		this.absValue = absValue;
	}

	public int getPerValue() {
		return perValue;
	}

	public void setPerValue(int perValue) {
		this.perValue = perValue;
	}

//	public int getShowFlag() {
//		return showFlag;
//	}
//
//	public void setShowFlag(int showFlag) {
//		this.showFlag = showFlag;
//	}

	public Timestamp getExpireDttm() {
		return expireDttm;
	}

	public void setExpireDttm(Timestamp expireDttm) {
		this.expireDttm = expireDttm;
	}

	public boolean isExpired() {
		if (expireDttm == null)// null永不过期
			return false;
		else {
			return (expireDttm.getTime() - System.currentTimeMillis() < 0);
		}
	}

}
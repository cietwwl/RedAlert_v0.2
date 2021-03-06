package com.youxigu.dynasty2.develop.domain;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 城池的当前使用的效果
 * 
 * @author Administrator
 * 
 */
public class CastleEffect implements Serializable {
    private static final long serialVersionUID = 7906695604736722288L;

	public static final String CASTLE_EFFECT_LOCKER_PREFIX = "CAS_EFFECT_";

	public static final int ENTID_ILLUSTRATION = -9001;
	private int id;
	private long casId;
	// entId=-9001被 图鉴占用，
	private int entId;// 实体ID,同实体产生的同类型效果要累加或者覆盖或者延时间
	// private int effId;// 效果ID
	private String effTypeId;// 效果类型，相同效果类型可能有多个效果，根据实体类型来确定
	// private String entTypeId;// 实体类型,同类型实体产生的同类型效果要累加或者延时间

	private int absValue;
	private int perValue;
	// private int showFlag;
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

	public long getCasId() {
		return casId;
	}

	public void setCasId(long casId) {
		this.casId = casId;
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

	// public int getShowFlag() {
	// return showFlag;
	// }
	//
	// public void setShowFlag(int showFlag) {
	// this.showFlag = showFlag;
	// }

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

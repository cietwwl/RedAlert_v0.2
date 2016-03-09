package com.youxigu.dynasty2.entity.service;

import com.youxigu.dynasty2.entity.domain.EntityLimit;
/**
 * 约束结果集
 */
public class EntityLimitResult {
	private int id;
	private int entId;//实体id
	private int level;//实体的等级
	private int needEntId;//约束实体id
	private String needEntName;//约束实体名
	private String needEntTypeDesc;//约束实体类型描述
	private int needLevel;//需要实体等级
	private int leastNum;//至少需要的数量

	private int actualLevel;//实际需要的等级
	private int actualNum;//实际需要的数量

	public EntityLimitResult(EntityLimit limit) {
		this.id = limit.getId();
		this.entId = limit.getEntId();
		this.level = limit.getLevel();
		this.needEntId = limit.getNeedEntId();
		this.needLevel = limit.getNeedLevel();
		this.leastNum = limit.getLeastNum();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getEntId() {
		return entId;
	}

	public void setEntId(int entId) {
		this.entId = entId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getNeedEntId() {
		return needEntId;
	}

	public void setNeedEntId(int needEntId) {
		this.needEntId = needEntId;
	}

	public String getNeedEntName() {
		return needEntName;
	}

	public void setNeedEntName(String needEntName) {
		this.needEntName = needEntName;
	}

	public String getNeedEntTypeDesc() {
		return needEntTypeDesc;
	}

	public void setNeedEntTypeDesc(String needEntTypeDesc) {
		this.needEntTypeDesc = needEntTypeDesc;
	}

	public int getNeedLevel() {
		return needLevel;
	}

	public void setNeedLevel(int needLevel) {
		this.needLevel = needLevel;
	}

	public int getLeastNum() {
		return leastNum;
	}

	public void setLeastNum(int leastNum) {
		this.leastNum = leastNum;
	}

	public int getActualLevel() {
		return actualLevel;
	}

	public void setActualLevel(int actualLevel) {
		this.actualLevel = actualLevel;
	}

	public int getActualNum() {
		return actualNum;
	}

	public void setActualNum(int actualNum) {
		this.actualNum = actualNum;
	}

	public boolean isMatch() {
		if (this.getActualNum() < this.getLeastNum()
				|| this.getActualLevel() < this.getNeedLevel())
			return false;
		else
			return true;
	}
}

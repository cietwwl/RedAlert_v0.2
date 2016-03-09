package com.youxigu.dynasty2.entity.service;

import com.youxigu.dynasty2.entity.domain.EntityConsume;

/**
 * 根据EntityConsume检查现有资源后得到的结果
 * 
 * 前台需要显示这些信息，对于后台，这个类实际上不需要
 * 
 * @author Administrator
 * 
 */
public class EntityConsumeResult {

	private int id;
	private int entId; //实体id
	private int level;//实体等级
	private int needEntId; //需要的实体id
	private int needEntNum;//原需消耗的数量
	private String needEntName; // 所需实体名
	private int actualNum; // 实需消耗的数量

	public EntityConsumeResult(EntityConsume consume) {
		this.id = consume.getId();
		this.entId = consume.getEntId();
		this.level = consume.getLevel();
		this.needEntId = consume.getNeedEntId();
		this.needEntNum = consume.getNeedEntNum();
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

	public int getNeedEntNum() {
		return needEntNum;
	}

	public void setNeedEntNum(int needEntNum) {
		this.needEntNum = needEntNum;
	}

	public String getNeedEntName() {
		return needEntName;
	}

	public void setNeedEntName(String needEntName) {
		this.needEntName = needEntName;
	}

	public int getActualNum() {
		return actualNum;
	}

	public void setActualNum(int actualNum) {
		this.actualNum = actualNum;
	}

	public boolean isMatch() {
		if (this.getActualNum() >= this.getNeedEntNum())
			return true;
		else
			return false;
	}
}

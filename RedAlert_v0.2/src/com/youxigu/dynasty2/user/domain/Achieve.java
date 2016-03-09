package com.youxigu.dynasty2.user.domain;

import java.io.Serializable;

/**
 * 成就定义--数据由策划来配
 * @author Dagangzi
 * 
 * 支持3种类型的成就
 * 1. entId数量达到entNum
 * 2. 行为type数量达到entNum
 * 3. 多条件约束的行为-最多可带5个参数，实现接口IAchieveCompleteChecker
 *
 */
public class Achieve implements Serializable{
	private int achieveId;
	private String name; //成就名称
	private int type; // 类型-资源获取，科技成长...
	private int entId; // 成就实体,特定资源，科技...
	private int entNum; // 达成需要的数据
	private int jungong; // 达成后奖励的军功点数
	private String icon;//图标
	private AchieveType achieveType;// 任务类型
	private int isCumulative;// 成就计数方式 >0累计 0覆盖 资源获取/装备收集/坦克收集 这几类目前为累计
	private AchieveLimit achieveLimit;// 约束

	public int getAchieveId() {
		return achieveId;
	}

	public void setAchieveId(int achieveId) {
		this.achieveId = achieveId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getEntId() {
		return entId;
	}

	public void setEntId(int entId) {
		this.entId = entId;
	}

	public int getEntNum() {
		return entNum;
	}

	public void setEntNum(int entNum) {
		this.entNum = entNum;
	}

	public int getJungong() {
		return jungong;
	}

	public void setJungong(int jungong) {
		this.jungong = jungong;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public AchieveType getAchieveType() {
		return achieveType;
	}

	public void setAchieveType(AchieveType achieveType) {
		this.achieveType = achieveType;
	}

	public int getIsCumulative() {
		return isCumulative;
	}

	public void setIsCumulative(int isCumulative) {
		this.isCumulative = isCumulative;
	}

	public boolean isCumulative() {
		return this.isCumulative > 0;
	}

	public AchieveLimit getAchieveLimit() {
		return achieveLimit;
	}

	public void setAchieveLimit(AchieveLimit achieveLimit) {
		this.achieveLimit = achieveLimit;
	}

	public int cheakAchieveLimit(long userId, int entNum) {
		if (achieveLimit != null) {
			return achieveLimit.getLimitChecker().check(userId, this,
					achieveLimit,
					entNum);
		}
		return entNum;
	}

	@Override
	public boolean equals(Object obj) {
		if (this.achieveId == ((Achieve) obj).getAchieveId()) {
			return true;
		}
		return false;
	}
}

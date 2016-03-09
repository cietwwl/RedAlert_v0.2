package com.youxigu.dynasty2.entity.domain;

/**
 * 科技实体定义
 * 
 */
public class Technology extends Entity {
	private static final long serialVersionUID = 5817231881766066542L;
	public static final byte TARGET_USER = 0;
	public static final byte TARGET_GUILD = 1;
	public static final byte TARGET_USER_ADV = 2;// 高级科技

	private String techType;// 科技类型

	private String techName;// 科技名称

	private String techDesc;// 科技描述

	private String iconPath;// 图片

	private int maxLevel;// 最大等级

	private int orderIndex;

	private byte target;// 科技所属 :0 玩家科技 1=联盟科技

	public String getTechType() {
		return techType;
	}

	public void setTechType(String techType) {
		this.techType = techType;
	}

	public String getTechName() {
		return techName;
	}

	public void setTechName(String techName) {
		this.techName = techName;
	}

	public String getTechDesc() {
		return techDesc;
	}

	public void setTechDesc(String techDesc) {
		this.techDesc = techDesc;
	}

	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	public int getMaxLevel() {
		return maxLevel;
	}

	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}

	public int getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(int orderIndex) {
		this.orderIndex = orderIndex;
	}

	@Override
	public int compareTo(Object o) {
		if (o instanceof Technology) {
			return (this.orderIndex - ((Technology) o).getOrderIndex());
		} else {
			return super.compareTo(o);
		}

	}

	public String getEntityName() {
		return this.techName;
	}

	public byte getTarget() {
		return target;
	}

	public void setTarget(byte target) {
		this.target = target;
	}

}

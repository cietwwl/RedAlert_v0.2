package com.youxigu.dynasty2.common.domain;

/*
 * 系统常量定义
 */
public class Enumer {

	private String enumId;// 常量id
	private String enumGroup;// 常量组
	private String enumValue;// 常量值
	private int orderBy;
	private String enumDesc;// 常量描述
	private transient int intValue = Integer.MIN_VALUE;//enumValue的整数值

	public String getEnumId() {
		return enumId;
	}

	public void setEnumId(String enumId) {
		this.enumId = enumId;
	}

	public String getEnumGroup() {
		return enumGroup;
	}

	public void setEnumGroup(String enumGroup) {
		this.enumGroup = enumGroup;
	}

	public String getEnumValue() {
		return enumValue;
	}

	public void setEnumValue(String enumValue) {
		this.enumValue = enumValue;
	}

	public int getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(int orderBy) {
		this.orderBy = orderBy;
	}

	public String getEnumDesc() {
		return enumDesc;
	}

	public void setEnumDesc(String enumDesc) {
		this.enumDesc = enumDesc;
	}

	public int getIntValue() {
		if (this.intValue == Integer.MIN_VALUE) {
			this.intValue = Integer.parseInt(this.enumValue);
		}
		return this.intValue;
	}

	public void setIntValue(int intValue) {
		this.intValue = intValue;
	}

}

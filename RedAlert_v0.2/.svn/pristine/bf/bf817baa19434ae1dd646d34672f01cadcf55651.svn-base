package com.youxigu.dynasty2.common.domain;

import java.sql.Timestamp;

/*
 * 系统变量在此定义
 */
public class SysPara implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private String paraId;// 变量id
	private String paraGroup;// 变量组
	private String paraType;// 变量类型
	private String paraValue;// 变量值
	private String paraName;// 变量名
	private String paraDesc;// 变量描述

	private transient int intValue = Integer.MAX_VALUE;
	private transient Timestamp dttm = null;

	public String getParaId() {
		return paraId;
	}

	public void setParaId(String paraId) {
		this.paraId = paraId;
	}

	public String getParaGroup() {
		return paraGroup;
	}

	public void setParaGroup(String paraGroup) {
		this.paraGroup = paraGroup;
	}

	public String getParaType() {
		return paraType;
	}

	public void setParaType(String paraType) {
		this.paraType = paraType;
	}

	public String getParaValue() {
		return paraValue;
	}

	public void setParaValue(String paraValue) {
		this.paraValue = paraValue;
		intValue = Integer.MAX_VALUE;
		dttm = null;
	}

	public String getParaName() {
		return paraName;
	}

	public void setParaName(String paraName) {
		this.paraName = paraName;
	}

	public String getParaDesc() {
		return paraDesc;
	}

	public void setParaDesc(String paraDesc) {
		this.paraDesc = paraDesc;
	}

	public int getIntValue() {
		if (intValue == Integer.MAX_VALUE) {
			intValue = Integer.parseInt(paraValue);
		}
		return intValue;
	}

	public Timestamp getTimestampValue() {
		if (dttm == null) {
			dttm = Timestamp.valueOf(paraValue);
		}
		return dttm;
	}

}

package com.youxigu.dynasty2.user.domain;

import java.sql.Timestamp;

/**
 * 白名单
 * @author Administrator
 *
 */
public class AccountWhiteList implements java.io.Serializable {

	public static int STATUS_LOCK = 0;
	public static int STATUS_UNLOCK = 1;	
	private String openId;
	private Timestamp dttm;
	private int status;
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public Timestamp getDttm() {
		return dttm;
	}
	public void setDttm(Timestamp dttm) {
		this.dttm = dttm;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
}

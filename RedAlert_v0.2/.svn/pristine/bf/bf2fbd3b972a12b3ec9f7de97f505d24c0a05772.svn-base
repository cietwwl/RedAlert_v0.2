package com.youxigu.dynasty2.user.domain;

import java.sql.Timestamp;

/**
 * 玩家元宝消费记录
 * 
 * @author Administrator
 * 
 */
public class UserConsumeLog implements java.io.Serializable {

	private int id;
	/**
	 * 角色id
	 */
	private long userId;
	/**
	 * 备用字段
	 */
	private String reason;
	/**
	 * 消费元宝数
	 */
	private int cash;
	/**
	 * 消费后余额
	 */
	private int balance;
	/**
	 * 消费日期
	 */
	private Timestamp dttm;
	
	private String pf;
	
	public UserConsumeLog(){
		
	}
	public UserConsumeLog(long userId,String reason,int cash,int balance,Timestamp dttm,String pf){
		this.userId=userId;
		this.reason = reason;
		this.cash=cash;
		this.balance=balance;
		this.dttm=dttm;
		this.pf = pf;
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

	public int getCash() {
		return cash;
	}
	public void setCash(int cash) {
		this.cash = cash;
	}
	public int getBalance() {
		return balance;
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}
	
	public Timestamp getDttm() {
		return dttm;
	}
	public void setDttm(Timestamp dttm) {
		this.dttm = dttm;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getPf() {
		return pf;
	}
	public void setPf(String pf) {
		this.pf = pf;
	}
	
	
}

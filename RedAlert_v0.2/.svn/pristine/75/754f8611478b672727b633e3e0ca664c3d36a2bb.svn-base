package com.youxigu.dynasty2.user.domain;

import java.sql.Timestamp;

/**
 * 玩家冲值记录
 * 
 * @author Administrator
 * 
 */
public class UserRechargeLog implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6902176005809607084L;
	private int id;
	/**
	 * 角色id
	 */
	private long userId;
	/**
	 * 备用字段
	 */
	private short type;
	/**
	 * 冲值元宝数
	 */
	private int cash;
	/**
	 * 冲值后余额
	 */
	private int balance;
	/**
	 * 冲值使用的QB
	 */
	private int qb;
	/**
	 * 冲值使用的金券
	 */
	private int quan;
	/**
	 * 冲值日期
	 */
	private Timestamp dttm;
	
	/**
	 * 订单id
	 */
	private String orderId;
	
	/**
	 * 充值平台
	 */
	private String pf;
	
	public UserRechargeLog(){
		
	}
	public UserRechargeLog(long userId,short type,int cash,int balance,int qb,int quan,Timestamp dttm,String orderId,String pf){
		this.userId=userId;
		this.type=type;
		this.cash=cash;
		this.balance=balance;
		this.qb=qb;
		this.quan=quan;
		this.dttm=dttm;
		this.orderId = orderId;
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
	public short getType() {
		return type;
	}
	public void setType(short type) {
		this.type = type;
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
	public int getQb() {
		return qb;
	}
	public void setQb(int qb) {
		this.qb = qb;
	}
	public int getQuan() {
		return quan;
	}
	public void setQuan(int quan) {
		this.quan = quan;
	}
	public Timestamp getDttm() {
		return dttm;
	}
	public void setDttm(Timestamp dttm) {
		this.dttm = dttm;
	}
	/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}
	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getPf() {
		return pf;
	}
	public void setPf(String pf) {
		this.pf = pf;
	}
	
	
}

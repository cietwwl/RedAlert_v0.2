package com.youxigu.dynasty2.hero.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import com.youxigu.dynasty2.hero.enums.HeroDrawType;

/**
 * 酒馆抽奖记录
 * 
 * @author fengfeng
 *
 */
public class UserPubAttr implements Serializable {
	private static final long serialVersionUID = -3593156538421325189L;
	private long userId;
	private int hireNum1;// // 普通招将总次数
	private Timestamp hireCD1; // 普通免费招将冷却结束时间
	private int freetimes1;// 每日已经抽奖的次数

	private int hireNum2;// // 中级招将总次数
	private Timestamp hireCD2; // 中级免费招将刷新冷却结束时间
	private int freetimes2;// 每日已经抽奖的次数

	private int hireNum3;// // 高级招将总次数
	private Timestamp hireCD3; // 高级免费招将刷新冷却结束时间
	private int freetimes3;// 每日已经抽奖的次数

	private int version;// 当前数据刷新的版本..就是每日刷新已经抽奖的次数

	private int status;// 记录每个活动是否为首次抽取 采用位运算来做 根据不同的活动类型来进行

	public UserPubAttr() {
		setStatus(0);
	}

	public UserPubAttr(long userId) {
		this();
		this.userId = userId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getHireNum1() {
		return hireNum1;
	}

	public void setHireNum1(int hireNum1) {
		this.hireNum1 = hireNum1;
	}

	public Timestamp getHireCD1() {
		return hireCD1;
	}

	public void setHireCD1(Timestamp hireCD1) {
		this.hireCD1 = hireCD1;
	}

	public int getHireNum2() {
		return hireNum2;
	}

	public void setHireNum2(int hireNum2) {
		this.hireNum2 = hireNum2;
	}

	public Timestamp getHireCD2() {
		return hireCD2;
	}

	public void setHireCD2(Timestamp hireCD2) {
		this.hireCD2 = hireCD2;
	}

	public int getHireNum3() {
		return hireNum3;
	}

	public void setHireNum3(int hireNum3) {
		this.hireNum3 = hireNum3;
	}

	public Timestamp getHireCD3() {
		return hireCD3;
	}

	public void setHireCD3(Timestamp hireCD3) {
		this.hireCD3 = hireCD3;
	}

	public int getHireNumByType(HeroDrawType type) {
		return type.getHireNum(this);
	}

	public Timestamp getHireCDByType(HeroDrawType type) {
		return type.getHireCD(this);
	}

	public void setHireNumByType(HeroDrawType type, int num) {
		type.setHireNum(this, num);
	}

	public void setHireCDByType(HeroDrawType type, Timestamp cd) {
		type.setHireCD(this, cd);
	}

	public int getFreetimes1() {
		return freetimes1;
	}

	public void setFreetimes1(int freetimes1) {
		this.freetimes1 = freetimes1;
	}

	public int getFreetimes2() {
		return freetimes2;
	}

	public void setFreetimes2(int freetimes2) {
		this.freetimes2 = freetimes2;
	}

	public int getFreetimes3() {
		return freetimes3;
	}

	public void setFreetimes3(int freetimes3) {
		this.freetimes3 = freetimes3;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * 刷新每日抽奖数据
	 */
	public void refreshDailyData() {
		for (HeroDrawType tp : HeroDrawType.values()) {
			tp.refreshDailyData(this);
		}
	}

	public void addFreetimes(HeroDrawType type) {
		type.addFreetimes(this);
	}

	public int getFreetimes(HeroDrawType type) {
		return type.getFreetimes(this);
	}

	/**
	 * 设置某个抽奖类型已经使用首次抽奖
	 * 
	 * @param type
	 */
	public void setFirstDraw(HeroDrawType type) {
		this.status = this.status | (1 << type.getIndex());
	}

	/**
	 * 判断是否为首次抽奖
	 * 
	 * @param type
	 * @return
	 */
	public boolean isFirstDraw(HeroDrawType type) {
		int mask = 1 << type.getIndex();
		if ((mask & status) == mask) {
			return false;
		}
		return true;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}

package com.youxigu.dynasty2.activity.domain;

import java.sql.Timestamp;

/**
 * 节日或者系统维护时赠送玩家的奖励 运营配置
 * 
 * @author Administrator
 * 
 */
public class AwardActivity implements java.io.Serializable {
	private static final long serialVersionUID = 4335679965383466450L;
	public static final int TYPE_HOLIDAY = 1;
	public static final int TYPE_MAINTAIN = 2;

	private int id;
	/**
	 * 活动名称
	 */
	private String actName;
	/**
	 * 活动描述
	 */
	private String actDesc;
	/**
	 * 活动图标
	 */
	private String icon;

	/**
	 * 奖励活动类型: 1 节日礼包 ,2 维护礼包
	 */
	private int type = TYPE_HOLIDAY;
	/**
	 * 开始日期
	 */
	private Timestamp startDttm;
	/**
	 * 结束日期
	 */
	private Timestamp endDttm;
	/**
	 * 最小用户等级
	 */
	private int minUsrLv;
	/**
	 * 最大用户等级
	 * 
	 */
	private int maxUsrLv;
	/**
	 * 道具1
	 */
	private int itemId1;
	/**
	 * 数量1
	 */
	private int num1;
	/**
	 * 道具2
	 */
	private int itemId2;
	private int num2;
	private int itemId3;
	private int num3;
	private int itemId4;
	private int num4;
	private int itemId5;
	private int num5;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getActName() {
		return actName;
	}

	public void setActName(String actName) {
		this.actName = actName;
	}

	public String getActDesc() {
		return actDesc;
	}

	public void setActDesc(String actDesc) {
		this.actDesc = actDesc;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Timestamp getStartDttm() {
		return startDttm;
	}

	public void setStartDttm(Timestamp startDttm) {
		this.startDttm = startDttm;
	}

	public Timestamp getEndDttm() {
		return endDttm;
	}

	public void setEndDttm(Timestamp endDttm) {
		this.endDttm = endDttm;
	}

	public int getMinUsrLv() {
		return minUsrLv;
	}

	public void setMinUsrLv(int minUsrLv) {
		this.minUsrLv = minUsrLv;
	}

	public int getMaxUsrLv() {
		return maxUsrLv;
	}

	public void setMaxUsrLv(int maxUsrLv) {
		this.maxUsrLv = maxUsrLv;
	}

	public int getItemId1() {
		return itemId1;
	}

	public void setItemId1(int itemId1) {
		this.itemId1 = itemId1;
	}

	public int getNum1() {
		return num1;
	}

	public void setNum1(int num1) {
		this.num1 = num1;
	}

	public int getItemId2() {
		return itemId2;
	}

	public void setItemId2(int itemId2) {
		this.itemId2 = itemId2;
	}

	public int getNum2() {
		return num2;
	}

	public void setNum2(int num2) {
		this.num2 = num2;
	}

	public int getItemId3() {
		return itemId3;
	}

	public void setItemId3(int itemId3) {
		this.itemId3 = itemId3;
	}

	public int getNum3() {
		return num3;
	}

	public void setNum3(int num3) {
		this.num3 = num3;
	}

	public int getItemId4() {
		return itemId4;
	}

	public void setItemId4(int itemId4) {
		this.itemId4 = itemId4;
	}

	public int getNum4() {
		return num4;
	}

	public void setNum4(int num4) {
		this.num4 = num4;
	}

	public int getItemId5() {
		return itemId5;
	}

	public void setItemId5(int itemId5) {
		this.itemId5 = itemId5;
	}

	public int getNum5() {
		return num5;
	}

	public void setNum5(int num5) {
		this.num5 = num5;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}

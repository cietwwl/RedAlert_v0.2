package com.youxigu.dynasty2.user.domain;

import java.io.Serializable;
import java.util.List;

import com.youxigu.dynasty2.entity.domain.ItemProperty;

/**
 * 军衔奖励
 * 
 * @author Dagangzi
 * @date 2016年1月19日
 */
public class TitleAward implements Serializable {
	/**
	 * 掉落的类型
	 */
	public static final String AWARDTYPE_HERO_PRO = "HERO_PRO";
	public static final String AWARDTYPE_COLOR = "COLOR";
	public static final String AWARDTYPE_DROPITEM = "DROPITEM";

	private int awardId;
	private String name;
	private String icon;
	private String awardType;// 类型 HERO_PRO：指挥官武将属性 COLOR：品质提升 DROPITEM：掉落包
	private int junGong;// 领取需要的军功
	/**
	 * 奖励内容
	 * 
	 * type:HERO_PRO：指挥官武将属性时 HERO_AGILE,26,A;HERO_HP,104,A;
	 * 
	 * type:COLOR：品质提升 品质 1-7 白 绿 蓝 紫 橙 金 红
	 * 
	 * type:ITEM：掉落包 掉落包entId
	 */
	private String awardDetail;
	private String awardTips;// 描述或是tips使用

	private transient TitleAward parentAward;// 前置奖励
	private transient TitleAward childAward;// 后置奖励
	private transient Title parent;// 所属的军衔
	private transient List<ItemProperty> heroProp;// 指挥官属性加成

	public int getAwardId() {
		return awardId;
	}

	public void setAwardId(int awardId) {
		this.awardId = awardId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getAwardType() {
		return awardType;
	}

	public void setAwardType(String awardType) {
		this.awardType = awardType;
	}

	public int getJunGong() {
		return junGong;
	}

	public void setJunGong(int junGong) {
		this.junGong = junGong;
	}

	public String getAwardDetail() {
		return awardDetail;
	}

	public void setAwardDetail(String awardDetail) {
		this.awardDetail = awardDetail;
	}

	public String getAwardTips() {
		return awardTips;
	}

	public void setAwardTips(String awardTips) {
		this.awardTips = awardTips;
	}

	public TitleAward getParentAward() {
		return parentAward;
	}

	public void setParentAward(TitleAward parentAward) {
		this.parentAward = parentAward;
	}

	public TitleAward getChildAward() {
		return childAward;
	}

	public void setChildAward(TitleAward childAward) {
		this.childAward = childAward;
	}

	public Title getParent() {
		return parent;
	}

	public void setParent(Title parent) {
		this.parent = parent;
	}

	public List<ItemProperty> getHeroProp() {
		return heroProp;
	}

	public void setHeroProp(List<ItemProperty> heroProp) {
		this.heroProp = heroProp;
	}
}

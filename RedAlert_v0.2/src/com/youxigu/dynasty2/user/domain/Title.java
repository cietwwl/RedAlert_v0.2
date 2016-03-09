package com.youxigu.dynasty2.user.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 军衔
 * 
 * @author Dagangzi
 * @date 2016年1月19日
 */
public class Title implements Serializable {
	private int titleId;
	private String name;
	private String icon;
	private int parentId;// 父id
	private int color;// 品质 -1：没有
	private int award1;// 奖励1,父子关系：parent<-child(award1<-award2<award3<award4<award5)
	private int award2;
	private int award3;
	private int award4;
	private int award5;
	private int entId;// 晋升奖励

	// 效果key和效果值加成关系
	private transient Map<String, Integer> absBonus = new HashMap<String, Integer>();
	private transient Map<String, Integer> percentBonus = new HashMap<String, Integer>();
	private transient Title parent;// 前置军衔
	private transient Title child;// 后置军衔
	private transient int[] awards;// 勋章

	public int getTitleId() {
		return titleId;
	}

	public void setTitleId(int titleId) {
		this.titleId = titleId;
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

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getAward1() {
		return award1;
	}

	public void setAward1(int award1) {
		this.award1 = award1;
	}

	public int getAward2() {
		return award2;
	}

	public void setAward2(int award2) {
		this.award2 = award2;
	}

	public int getAward3() {
		return award3;
	}

	public void setAward3(int award3) {
		this.award3 = award3;
	}

	public int getAward4() {
		return award4;
	}

	public void setAward4(int award4) {
		this.award4 = award4;
	}

	public int getAward5() {
		return award5;
	}

	public void setAward5(int award5) {
		this.award5 = award5;
	}

	public int getEntId() {
		return entId;
	}

	public void setEntId(int entId) {
		this.entId = entId;
	}

	/**
	 * 增加绝对值加成
	 * 
	 * @param key
	 * @param bonus
	 */
	public void addAbsBonus(String key, int bonus) {
		if(absBonus.containsKey(key)){
			bonus = absBonus.get(key) + bonus;
		}
		absBonus.put(key, bonus);
	}

	/**
	 * 增加百分比加成
	 * 
	 * @param key
	 * @param bonus
	 */
	public void addPercentBonus(String key, int bonus) {
		if (percentBonus.containsKey(key)) {
			bonus = percentBonus.get(key) + bonus;
		}
		percentBonus.put(key, bonus);
	}

	/**
	 * 取得绝对值效果
	 * 
	 * @param key
	 * @return
	 */
	public int getAbsBonus(String key) {
		if (absBonus.containsKey(key)) {
			return absBonus.get(key);
		}
		return 0;
	}

	/**
	 * 取得千分比效果
	 * 
	 * @param key
	 * @return
	 */
	public int getPerBonus(String key) {
		if (percentBonus.containsKey(key)) {
			return percentBonus.get(key);
		}
		return 0;
	}

	public Map<String, Integer> getAbsBonus() {
		return absBonus;
	}

	public void setAbsBonus(Map<String, Integer> absBonus) {
		this.absBonus = absBonus;
	}

	public Map<String, Integer> getPercentBonus() {
		return percentBonus;
	}

	public void setPercentBonus(Map<String, Integer> percentBonus) {
		this.percentBonus = percentBonus;
	}

	public Title getParent() {
		return parent;
	}

	public void setParent(Title parent) {
		this.parent = parent;
	}

	public Title getChild() {
		return child;
	}

	public void setChild(Title child) {
		this.child = child;
	}

	public int[] getAwards() {
		return awards;
	}

	public void setAwards(int[] awards) {
		this.awards = awards;
	}
}

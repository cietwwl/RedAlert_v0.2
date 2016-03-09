package com.youxigu.dynasty2.hero.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.youxigu.dynasty2.entity.domain.HeroProperty;

/**
 * 武将进阶限制
 */
public class RelifeLimit implements Serializable {
	private static final long serialVersionUID = -4885545506584472185L;
	private int id;//
	private int relifeNum;// 进阶次数
	private int sysHeroId;// 系统武将Id
	private String relifeDesc;//
	private int addGrowing;// 进阶后增加的成长值
	private int heroCardNum;// 需要的武将卡数量
	private String attrValue;// 进阶加的基础属性
	private int heroStrengthId;// 强化的开始id(对应强化等级表的初始id)

	private transient List<HeroProperty> properties = new ArrayList<HeroProperty>();// 进阶升级的属性值
	/** 强化等级配数 */
	private transient List<HeroStrength> heroStrengthLimits;
	private HeroStrength initHeroStrength = null;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRelifeNum() {
		return relifeNum;
	}

	public void setRelifeNum(int relifeNum) {
		this.relifeNum = relifeNum;
	}

	public int getSysHeroId() {
		return sysHeroId;
	}

	public void setSysHeroId(int sysHeroId) {
		this.sysHeroId = sysHeroId;
	}

	public String getRelifeDesc() {
		return relifeDesc;
	}

	public void setRelifeDesc(String relifeDesc) {
		this.relifeDesc = relifeDesc;
	}

	public int getAddGrowing() {
		return addGrowing;
	}

	public void setAddGrowing(int addGrowing) {
		this.addGrowing = addGrowing;
	}

	public int getHeroCardNum() {
		return heroCardNum;
	}

	public void setHeroCardNum(int heroCardNum) {
		this.heroCardNum = heroCardNum;
	}

	public String getAttrValue() {
		return attrValue;
	}

	public void setAttrValue(String attrValue) {
		this.attrValue = attrValue;
	}

	public List<HeroProperty> getProperties() {
		return properties;
	}

	public void setProperties(List<HeroProperty> properties) {
		this.properties = properties;
	}

	public int getHeroStrengthId() {
		return heroStrengthId;
	}

	public void setHeroStrengthId(int heroStrengthId) {
		this.heroStrengthId = heroStrengthId;
	}

	public List<HeroStrength> getHeroStrengthLimits() {
		return heroStrengthLimits;
	}

	/**
	 * 通过强化id返回强化的数据
	 * 
	 * @param id
	 * @return
	 */
	public HeroStrength getHeroStrength(int id) {
		for (HeroStrength hs : heroStrengthLimits) {
			if (hs.getId() == id) {
				return hs;
			}
		}
		return null;
	}

	public void setHeroStrengthLimits(List<HeroStrength> heroStrengthLimits) {
		this.heroStrengthLimits = heroStrengthLimits;
	}

	public HeroStrength getInitHeroStrength() {
		return initHeroStrength;
	}

	public void setInitHeroStrength(HeroStrength initHeroStrength) {
		this.initHeroStrength = initHeroStrength;
	}

}

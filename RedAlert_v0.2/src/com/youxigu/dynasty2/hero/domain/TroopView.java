package com.youxigu.dynasty2.hero.domain;

/**
 * troop的可变属性
 * 
 * @author Dagangzi
 *
 */
public class TroopView implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private long troopId;
	private String name;// 军团名称

	// /////////////五个武将的ID,同时表示在阵型中的位置
	// =0表示该位置没有配置武将,任何一个都可能为0，有可能heroId1为0，但是后续的herId2-5不为0
	private long[] heroIds;

	public long getTroopId() {
		return troopId;
	}

	public void setTroopId(long troopId) {
		this.troopId = troopId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long[] getHeroIds() {
		return heroIds;
	}

	public void setHeroIds(long[] heroIds) {
		this.heroIds = heroIds;
	}
}

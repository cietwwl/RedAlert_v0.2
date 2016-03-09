package com.youxigu.dynasty2.hero.domain;

/**
 * 武将强化配数表
 * 
 * @author fengfeng
 *
 */
public class HeroStrength {
	public static final int FIX = 10000;

	private int id;// 唯一id 此id值 是 武将的进阶数*10000+强化等级(如2阶的强化等级3 的id为
					// 2*10000+3=20003)
	private int level;// 当前强化等级
	private int nextLvevlId;// 下一个强化等级id(0表示强化等级满了，可以进阶了)
	private int addGrowing;// 加成长值
	// 消耗的物品1
	private int itemId1;
	private int count1;
	// 消耗的物品2
	private int itemId2;
	private int count2;

	/** 下一等级强化 **/
	private transient HeroStrength nextHeroStrength;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getNextLvevlId() {
		return nextLvevlId;
	}

	public void setNextLvevlId(int nextLvevlId) {
		this.nextLvevlId = nextLvevlId;
	}

	public int getAddGrowing() {
		return addGrowing;
	}

	public void setAddGrowing(int addGrowing) {
		this.addGrowing = addGrowing;
	}

	public int getItemId1() {
		return itemId1;
	}

	public void setItemId1(int itemId1) {
		this.itemId1 = itemId1;
	}

	public int getCount1() {
		return count1;
	}

	public void setCount1(int count1) {
		this.count1 = count1;
	}

	public int getItemId2() {
		return itemId2;
	}

	public void setItemId2(int itemId2) {
		this.itemId2 = itemId2;
	}

	public int getCount2() {
		return count2;
	}

	public void setCount2(int count2) {
		this.count2 = count2;
	}

	public HeroStrength getNextHeroStrength() {
		return nextHeroStrength;
	}

	public void setNextHeroStrength(HeroStrength nextHeroStrength) {
		this.nextHeroStrength = nextHeroStrength;
	}

}

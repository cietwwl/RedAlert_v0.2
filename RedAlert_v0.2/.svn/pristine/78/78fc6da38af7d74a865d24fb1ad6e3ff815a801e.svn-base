package com.youxigu.dynasty2.npc.domain;

/**
 * npc 军团定义
 * 
 * @author Dagangzi
 *
 */
public class NPCTroop implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8584480633578796892L;
	private int troopId;
	private int heroId1;
	private int heroId2;
	private int heroId3;
	private int heroId4;
	private int heroId5;
	private int heroId6;
	private transient NPCHero[] heros;
	private transient String[] heroFates;// 每个NPC武将对应的情缘

	public NPCTroop() {
	}

	public NPCTroop(int troopId, int heroId1, int heroId2, int heroId3, int heroId4, int heroId5, int heroId6) {
		this.troopId = troopId;
		this.heroId1 = heroId1;
		this.heroId2 = heroId2;
		this.heroId3 = heroId3;
		this.heroId4 = heroId4;
		this.heroId5 = heroId5;
		this.heroId6 = heroId6;
	}

	public int getTroopId() {
		return troopId;
	}

	public void setTroopId(int troopId) {
		this.troopId = troopId;
	}

	public int getHeroId1() {
		return heroId1;
	}

	public void setHeroId1(int heroId1) {
		this.heroId1 = heroId1;
	}

	public int getHeroId2() {
		return heroId2;
	}

	public void setHeroId2(int heroId2) {
		this.heroId2 = heroId2;
	}

	public int getHeroId3() {
		return heroId3;
	}

	public void setHeroId3(int heroId3) {
		this.heroId3 = heroId3;
	}

	public int getHeroId4() {
		return heroId4;
	}

	public void setHeroId4(int heroId4) {
		this.heroId4 = heroId4;
	}

	public int getHeroId5() {
		return heroId5;
	}

	public void setHeroId5(int heroId5) {
		this.heroId5 = heroId5;
	}

	public int getHeroId6() {
		return heroId6;
	}

	public void setHeroId6(int heroId6) {
		this.heroId6 = heroId6;
	}

	public NPCHero[] getHeros() {
		return heros;
	}

	public void setHeros(NPCHero[] heros) {
		this.heros = heros;
	}

	public boolean hasHero() {
		return (heroId1 != 0 || heroId2 != 0 || heroId3 != 0 || heroId4 != 0 || heroId5 != 0 || heroId6 != 0);
	}

	public String[] getHeroFates() {
		return heroFates;
	}

	public void setHeroFates(String[] heroFates) {
		this.heroFates = heroFates;
	}

}

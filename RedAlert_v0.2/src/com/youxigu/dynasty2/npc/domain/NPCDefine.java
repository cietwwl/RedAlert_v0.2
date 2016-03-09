package com.youxigu.dynasty2.npc.domain;

/**
 * NPC定义
 * 
 * @author Administrator
 *
 */
public class NPCDefine implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8068689497276040156L;

	private int npcId;

	private int level;

	private short npcType;

	private String npcName;

	private String npcDesc;

	private String iconPath;

	private int heroId1;

	private int heroId2;

	private int heroId3;
	
	private int heroId4;
	
	private int heroId5;
	
	private int heroId6;

	private int combatPower;//战斗力
	
	private short leaderPos;//队长所在的位置

	// NpcTroop
	private transient NPCTroop troop;

	public int getNpcId() {
		return npcId;
	}

	public void setNpcId(int npcId) {
		this.npcId = npcId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public short getNpcType() {
		return npcType;
	}

	public void setNpcType(short npcType) {
		this.npcType = npcType;
	}

	public String getNpcName() {
		return npcName;
	}

	public void setNpcName(String npcName) {
		this.npcName = npcName;
	}

	public String getNpcDesc() {
		return npcDesc;
	}

	public void setNpcDesc(String npcDesc) {
		this.npcDesc = npcDesc;
	}

	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
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

	public int getCombatPower() {
		return combatPower;
	}

	public void setCombatPower(int combatPower) {
		this.combatPower = combatPower;
	}

	public short getLeaderPos() {
		return leaderPos;
	}

	public void setLeaderPos(short leaderPos) {
		this.leaderPos = leaderPos;
	}

	public NPCTroop getTroop() {
		return troop;
	}

	public void setTroop(NPCTroop troop) {
		this.troop = troop;
	}
}

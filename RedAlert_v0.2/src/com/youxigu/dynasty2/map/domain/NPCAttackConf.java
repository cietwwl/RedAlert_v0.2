package com.youxigu.dynasty2.map.domain;

/**
 * 怪配置 主要用于大地图，野怪，个人，联盟，国家 资源点防守怪的定义
 * 
 * @author LK
 * @date 2016年3月8日
 */
public class NPCAttackConf {
	private int id;
	private String model;// 模型
	private int level;// 等级
	private int npcId;// npcDefine.npcId
	private int heroExp;
	private int dropPackId;// 掉落包id
	private int nextId;

	/**
	 * 下一个NPCAttackConf
	 */
	private NPCAttackConf next;
	/**
	 * 上一个NPCAttackConf
	 */
	private NPCAttackConf prev;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getNpcId() {
		return npcId;
	}

	public void setNpcId(int npcId) {
		this.npcId = npcId;
	}

	public int getHeroExp() {
		return heroExp;
	}

	public void setHeroExp(int heroExp) {
		this.heroExp = heroExp;
	}

	public int getDropPackId() {
		return dropPackId;
	}

	public void setDropPackId(int dropPackId) {
		this.dropPackId = dropPackId;
	}

	public int getNextId() {
		return nextId;
	}

	public void setNextId(int nextId) {
		this.nextId = nextId;
	}

	public NPCAttackConf getNext() {
		return next;
	}

	public void setNext(NPCAttackConf next) {
		this.next = next;
	}

	public NPCAttackConf getPrev() {
		return prev;
	}

	public void setPrev(NPCAttackConf prev) {
		this.prev = prev;
	}
}

package com.youxigu.dynasty2.hero.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.youxigu.dynasty2.entity.domain.enumer.EquipPosion;

/**
 * 军团格子。。。一个军团里面默认是6个格子(可以扩充到12个格子)
 * 
 * @author fengfeng
 *
 */
public class TroopGrid implements Serializable {
	private static final long serialVersionUID = 1L;
	private long troopGridId;// 格子的id
	private long userId;// 格子所属的用户
	private long troopId;// 格子所属的军团id
	// private int position;// 格子的位置编号0到6表示上阵的6个武将

	private long heroId;// 表示格子上阵的武将id,0表示没有上阵
	private int sysHeroId;// 系统武将id。。策划配置武将表里面的id

	private long equ1;// 格子里面穿的装备-treasuryId
	private long equ2;//
	private long equ3;//
	private long equ4;//
	private long equ5;//
	private long equ6;//

	private String equipFate;// 装备情缘

	public TroopGrid() {
		super();
	}

	public TroopGrid(long userId, long troopId, long heroId, int sysHeroId) {
		super();
		this.userId = userId;
		this.troopId = troopId;
		// this.position = position.getIndex();
		this.heroId = heroId;
		this.sysHeroId = sysHeroId;
		initEquip();
	}

	public void initEquip() {
		for (EquipPosion ep : EquipPosion.values()) {
			ep.setEqu(this, 0);
		}
	}

	public long getTroopGridId() {
		return troopGridId;
	}

	public void setTroopGridId(long troopGridId) {
		this.troopGridId = troopGridId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	// public int getPosition() {
	// return position;
	// }
	//
	// public void setPosition(int position) {
	// this.position = position;
	// }

	public long getHeroId() {
		return heroId;
	}

	public void setHeroId(long heroId) {
		this.heroId = heroId;
	}

	public long getEqu1() {
		return equ1;
	}

	public void setEqu1(long equ1) {
		this.equ1 = equ1;
	}

	public long getEqu2() {
		return equ2;
	}

	public void setEqu2(long equ2) {
		this.equ2 = equ2;
	}

	public long getEqu3() {
		return equ3;
	}

	public void setEqu3(long equ3) {
		this.equ3 = equ3;
	}

	public long getEqu4() {
		return equ4;
	}

	public void setEqu4(long equ4) {
		this.equ4 = equ4;
	}

	public long getEqu5() {
		return equ5;
	}

	public void setEqu5(long equ5) {
		this.equ5 = equ5;
	}

	public long getEqu6() {
		return equ6;
	}

	public void setEqu6(long equ6) {
		this.equ6 = equ6;
	}

	/**
	 * 获取所有的装备id
	 * 
	 * @return
	 */
	public List<Long> getEquip() {
		List<Long> ids = new ArrayList<Long>();
		ids.add(equ1);
		ids.add(equ2);
		ids.add(equ3);
		ids.add(equ4);
		ids.add(equ5);
		ids.add(equ6);
		return ids;

	}

	public int getSysHeroId() {
		return sysHeroId;
	}

	public void setSysHeroId(int sysHeroId) {
		this.sysHeroId = sysHeroId;
	}

	public long getTroopId() {
		return troopId;
	}

	public void setTroopId(long troopId) {
		this.troopId = troopId;
	}

	public String getEquipFate() {
		return equipFate;
	}

	public void setEquipFate(String equipFate) {
		this.equipFate = equipFate;
	}

	/**
	 * 穿戴的装备个数
	 * 
	 * @return
	 */
	public int getEquipNum() {
		int num = 0;
		if (equ1 > 0)
			num++;
		if (equ2 > 0)
			num++;
		if (equ3 > 0)
			num++;
		if (equ4 > 0)
			num++;
		if (equ5 > 0)
			num++;
		if (equ6 > 0)
			num++;
		return num;
	}
}

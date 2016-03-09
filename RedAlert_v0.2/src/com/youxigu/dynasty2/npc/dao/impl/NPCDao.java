package com.youxigu.dynasty2.npc.dao.impl;

import java.util.List;

import com.manu.core.base.BaseDao;
import com.youxigu.dynasty2.npc.dao.INPCDao;
import com.youxigu.dynasty2.npc.domain.NPCDefine;
import com.youxigu.dynasty2.npc.domain.NPCHero;

public class NPCDao  extends BaseDao implements INPCDao{

	@Override
	public List<NPCHero> getNPCHeros() {
		return this.getSqlMapClientTemplate().queryForList("getNPCHeros");
	}

//	@Override
//	public List<NPCMapCell> getNPCMapCells() {
//		return this.getSqlMapClientTemplate().queryForList("getNPCMapCells");
//	}

//	@Override
//	public List<NPCTroop> getNPCTroops() {
//		return this.getSqlMapClientTemplate().queryForList("getNPCTroops");
//	}
//
//	@Override
//	public List<NPCWall> getNPCWalls() {
//		return this.getSqlMapClientTemplate().queryForList("getNPCWalls");
//	}

	@Override
	public List<NPCDefine> getNPCDefines() {
		return this.getSqlMapClientTemplate().queryForList("getNPCDefines");
	}
//
//	@Override
//	public List<NPCAttackConf> getNPCAttackConfs(){
//		return this.getSqlMapClientTemplate().queryForList("getNPCAttackConfs");		
//	}
}

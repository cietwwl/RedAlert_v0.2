package com.youxigu.dynasty2.npc.dao;

import java.util.List;

import com.youxigu.dynasty2.npc.domain.NPCDefine;
import com.youxigu.dynasty2.npc.domain.NPCHero;

public interface INPCDao {
	List<NPCHero> getNPCHeros();

//	List<NPCMapCell> getNPCMapCells();
//
//	List<NPCWall> getNPCWalls();

	List<NPCDefine> getNPCDefines();

//	List<NPCTroop> getNPCTroops();

//	List<NPCAttackConf> getNPCAttackConfs();
}

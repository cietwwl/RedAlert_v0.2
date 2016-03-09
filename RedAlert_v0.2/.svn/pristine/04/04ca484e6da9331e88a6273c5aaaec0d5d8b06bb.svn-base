package com.youxigu.dynasty2.npc.service;

import java.util.List;
import java.util.Map;

import com.youxigu.dynasty2.npc.domain.NPCDefine;
import com.youxigu.dynasty2.npc.domain.NPCHero;
/**
 * npc service 接口
 * @author Dagangzi
 *
 */
public interface INPCService {

	Map<Integer, NPCDefine> getAllNpcDefines();
	/**
	 * 根据id获得NPC定义
	 * @param npcId
	 * @return
	 */
	NPCDefine getNPCDefine(int npcId);
	
	
	
	/**
	 * 根据坐标获得NPC定义
	 * @param posX
	 * @param posY
	 * @return
	 */
//	NPCDefine getNPCDefine(int posX,int posY);	
	
	
	/**
	 * 根据npc类型与等级获得npc
	 * @param npcType
	 * @param npcLv
	 * @return
	 */
	NPCDefine getNPCDefineByTypeLv(int npcType,int npcLv);
	
	
	/**
	 * 得到战略站战斗力
	 * @param troop
	 * @return
	 */
	int getNPCHerosCombatPower(List<NPCHero> heros);
	int getNPCHerosCombatPower(NPCHero[] heros);
	
	/**
	 * 取得一个NPC武将
	 * @param npcHeroId
	 * @return
	 */
	NPCHero getNPCHero(int npcHeroId);
	
	/**
	 * 得到所有的NPC武将
	 * @return
	 */
	Map<Integer,NPCHero> getNPCHeros();
	
//	/**
//	 * 获取城墙，城防信息
//	 * @param wallId
//	 * @return
//	 */
//	NPCWall getNPCWall(int wallId);
//	
//	NPCAttackConf getNPCAttackConf(int npcId);
//	NPCAttackConf getFirstNPCAttackConf();	
//	
//	/**
//	 * 按难度取取抗击匈奴的npc
//	 * @return
//	 */
//	List<NPCDefine> getHunsNpcsByLevel(int level);
}

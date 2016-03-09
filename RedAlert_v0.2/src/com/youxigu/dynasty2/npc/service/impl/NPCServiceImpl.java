package com.youxigu.dynasty2.npc.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.youxigu.dynasty2.entity.domain.SysHero;
import com.youxigu.dynasty2.entity.service.IEntityService;
import com.youxigu.dynasty2.hero.domain.HeroFate;
import com.youxigu.dynasty2.npc.dao.INPCDao;
import com.youxigu.dynasty2.npc.domain.NPCDefine;
import com.youxigu.dynasty2.npc.domain.NPCHero;
import com.youxigu.dynasty2.npc.domain.NPCTroop;
import com.youxigu.dynasty2.npc.service.INPCService;

public class NPCServiceImpl implements INPCService, ApplicationListener {
	public static final Logger log = LoggerFactory.getLogger(NPCServiceImpl.class);
	private INPCDao npcDao;
	// private IHeroDao heroDao;
	private IEntityService entityService;

	private Map<Integer, NPCHero> npcHeros = null;

	/**
	 * NPC战队的英雄列表
	 */
	//	private Map<Integer, NPCTroop> npcTroops = null;

	/**
	 * NPC地图位置<br>
	 * key=posX*10000+posY
	 */
	//	private Map<Integer, NPCMapCell> npcMapCells = null;
	// private Map<Integer, NPCMapCell> npcMapCells = new HashMap<Integer,
	// NPCMapCell>();

	//	private Map<Integer, NPCWall> npcWalls = null;

	private Map<Integer, NPCDefine> npcDefines;

	//	private Map<Integer, NPCAttackConf> npcAttackConfs;
	// 第一个要攻打的NPC
	//	private NPCAttackConf firstAttacked = null;

	//	private Map<Integer, List<NPCDefine>> hunsNpcs;//抗击匈奴使用的npc

	private boolean init = false;

	// public void setHeroDao(IHeroDao heroDao) {
	// this.heroDao = heroDao;
	// }

	public void setEntityService(IEntityService entityService) {
		this.entityService = entityService;
	}

	public void setNpcDao(INPCDao npcDao) {
		this.npcDao = npcDao;
	}

	public void init() {
		if (init) {
			return;
		}
		init = true;

		log.info("初始化NPC模块数据.......");

		// npc英雄
		initNPCHeros();

		//		// npc地图位置
		//		initNPCMapCells();
		//
		//		// npc城墙定义
		//		initNPCWalls();

		// npc troops
		//		initNPCTroops();

		initNPCDefine();

		//		initNPCAttackConf();
		log.info("初始化NPC模块数据完成.......");

	}

	/**
	 * 初始化npcdefine
	 */
	private void initNPCDefine() {
		List<NPCDefine> defines = npcDao.getNPCDefines();
		npcDefines = new HashMap<Integer, NPCDefine>();
		//
		//		if (hunsNpcs == null) {
		//			hunsNpcs = new HashMap<Integer, List<NPCDefine>>();
		//		}

		for (NPCDefine npc : defines) {
			NPCTroop troop = new NPCTroop(npc.getNpcId(), npc.getHeroId1(), npc.getHeroId2(), npc.getHeroId3(),
					npc.getHeroId4(), npc.getHeroId5(), npc.getHeroId6());
			npc.setTroop(troop);
			this.initNPCTroops(troop);
			
			//设置队长标志
			if(troop.getHeros() != null) {
				NPCHero hero = troop.getHeros()[npc.getLeaderPos() - 1];
				if(hero != null) {
					hero.setTeamleader(true);
				}
			}
			npcDefines.put(npc.getNpcId(), npc);
		}

	}

	/**
	 * 初始化npcTroop
	 * @param troop
	 */
	private void initNPCTroops(NPCTroop troop) {
		Map<Integer, Integer> sysHeroIdMaps = new HashMap<Integer, Integer>(9);
		NPCHero[] heros = new NPCHero[6];
		if (troop.getHeroId1() > 0) {
			NPCHero hero = npcHeros.get(troop.getHeroId1());
			if (hero != null) {
				heros[0] = hero;
				Integer sysHeroId = hero.getSysHeroId();
				sysHeroIdMaps.put(sysHeroId, sysHeroId);
			}
		}
		if (troop.getHeroId2() > 0) {
			NPCHero hero = npcHeros.get(troop.getHeroId2());
			if (hero != null) {
				heros[1] = hero;
				Integer sysHeroId = hero.getSysHeroId();
				sysHeroIdMaps.put(sysHeroId, sysHeroId);

			}
		}
		if (troop.getHeroId3() > 0) {
			NPCHero hero = npcHeros.get(troop.getHeroId3());
			if (hero != null) {
				heros[2] = hero;
				Integer sysHeroId = hero.getSysHeroId();
				sysHeroIdMaps.put(sysHeroId, sysHeroId);

			}
		}
		if (troop.getHeroId4() > 0) {
			NPCHero hero = npcHeros.get(troop.getHeroId4());
			if (hero != null) {
				heros[3] = hero;
				Integer sysHeroId = hero.getSysHeroId();
				sysHeroIdMaps.put(sysHeroId, sysHeroId);

			}
		}
		if (troop.getHeroId5() > 0) {
			NPCHero hero = npcHeros.get(troop.getHeroId5());
			if (hero != null) {
				heros[4] = hero;
				Integer sysHeroId = hero.getSysHeroId();
				sysHeroIdMaps.put(sysHeroId, sysHeroId);

			}
		}

		if (troop.getHeroId6() > 0) {
			NPCHero hero = npcHeros.get(troop.getHeroId6());
			if (hero != null) {
				heros[5] = hero;
				Integer sysHeroId = hero.getSysHeroId();
				sysHeroIdMaps.put(sysHeroId, sysHeroId);

			}
		}

		//军团中的武将
		troop.setHeros(heros);

		// 情缘
		String[] troopFates = new String[9];
		troop.setHeroFates(troopFates);
		for (int i = 0; i < heros.length; i++) {
			NPCHero hero = heros[i];
			if (hero != null) {
				SysHero sysHero = hero.getSysHero();
				if (sysHero != null) {
					List<HeroFate> fates = hero.getSysHero().getHeroFates();
					if (fates != null) {
						StringBuilder sb = new StringBuilder();
						for (HeroFate fate : fates) {

							boolean match = true;
							int[] reqSysHeroIds = fate.getReqEntIds();
							for (Integer sysHeroId : reqSysHeroIds) {
								if (!sysHeroIdMaps.containsKey(sysHeroId)) {
									match = false;
									break;
								}
							}
							// 有情缘
							if (match) {
								if (sb.length() > 0) {
									sb.append(",");
								}
								sb.append(fate.getFateId());
							}

							if (sb.length() > 0) {
								troopFates[i] = sb.toString();
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 初始化npc武将
	 */
	private void initNPCHeros() {
		npcHeros = new HashMap<Integer, NPCHero>();
		List<NPCHero> heros = npcDao.getNPCHeros();
		for (NPCHero hero : heros) {
			if (hero.getSysHeroId() != 0) {
				SysHero sysHero = (SysHero) entityService.getEntity(hero.getSysHeroId());
				if (sysHero != null) {
					hero.setSysHero(sysHero);
				} else {
					log.warn("npc武将没有关联的SysHero定义：npcHeroId={}", hero.getHeroId());
				}
				if (hero.getArmy() == null) {
					log.warn("npc武将没有关联的Army定义：npcHeroId={}", hero.getHeroId());

				}
			}
			npcHeros.put(hero.getHeroId(), hero);
		}
	}

	@Override
	public NPCDefine getNPCDefine(int npcId) {
		if (npcDefines == null) {
			init();
		}
		return npcDefines.get(npcId);
	}

	//	@Override
	//	public NPCDefine getNPCDefine(int posX, int posY) {
	//		if (npcDefines == null) {
	//			init();
	//		}
	//		NPCMapCell mapCell = npcMapCells.get(posX * 10000 + posY);
	//		if (mapCell != null && mapCell.getNpcId() != 0) {
	//			return npcDefines.get(mapCell.getNpcId());
	//		}
	//		return null;
	//	}

	@Override
	public NPCDefine getNPCDefineByTypeLv(int npcType, int npcLv) {
		if (npcDefines == null) {
			init();
		}
		Iterator<NPCDefine> lit = npcDefines.values().iterator();
		while (lit.hasNext()) {
			NPCDefine npc = lit.next();
			if (npc.getNpcType() == npcType && npc.getLevel() == npcLv) {
				return npc;
			}
		}
		return null;
	}

	@Override
	public int getNPCHerosCombatPower(List<NPCHero> heros) {
		int sum = 0;
		if (heros != null) {
			for (NPCHero hero : heros) {
				if (hero != null) {
					sum = sum + hero.getCurrentPower();
				}
			}
		}
		return sum;
	}

	@Override
	public int getNPCHerosCombatPower(NPCHero[] heros) {
		int sum = 0;
		if (heros != null) {
			for (NPCHero hero : heros) {
				if (hero != null) {
					sum = sum + hero.getCurrentPower();
				}
			}
		}
		return sum;
	}

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof ContextRefreshedEvent) {
			init();
		}

	}

	@Override
	public NPCHero getNPCHero(int npcHeroId) {
		return npcHeros.get(npcHeroId);
	}

	@Override
	public Map<Integer, NPCHero> getNPCHeros() {
		return npcHeros;
	}

	//	@Override
	//	public NPCWall getNPCWall(int wallId) {
	//
	//		return npcWalls.get(wallId);
	//	}
	//
	//	@Override
	//	public NPCAttackConf getFirstNPCAttackConf() {
	//		return firstAttacked;
	//	}
	//
	//	@Override
	//	public NPCAttackConf getNPCAttackConf(int npcId) {
	//		// TODO Auto-generated method stub
	//		return npcAttackConfs.get(npcId);
	//	}

	@Override
	public Map<Integer, NPCDefine> getAllNpcDefines() {
		return npcDefines;
	}

	//	@Override
	//	public List<NPCDefine> getHunsNpcsByLevel(int level) {
	//		return hunsNpcs.get(level);
	//	}
}

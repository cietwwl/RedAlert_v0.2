package com.youxigu.dynasty2.combat.service.impl;

import com.youxigu.dynasty2.combat.domain.CombatTeam;
import com.youxigu.dynasty2.combat.domain.NPCHeroCombatUnit;
import com.youxigu.dynasty2.combat.service.ICombatTeamService;
import com.youxigu.dynasty2.hero.domain.Troop;
import com.youxigu.dynasty2.map.domain.NPCAttackConf;
import com.youxigu.dynasty2.map.service.IMapService;
import com.youxigu.dynasty2.npc.domain.NPCDefine;
import com.youxigu.dynasty2.npc.domain.NPCHero;
import com.youxigu.dynasty2.npc.domain.NPCTroop;
import com.youxigu.dynasty2.npc.service.INPCService;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.util.BaseException;

/**
 * NPC队伍构造器
 * 
 * @author Dagangzi
 *
 */
public class NPCCombatTeamService implements ICombatTeamService {

	private INPCService npcService;
	private IMapService mapService;

	public void setNpcService(INPCService npcService) {
		this.npcService = npcService;
	}

	public void setMapService(IMapService mapService) {
		this.mapService = mapService;
	}

	@Override
	public CombatTeam getCombatTeam(long casId) {
		NPCDefine npcDefine = npcService.getNPCDefine((int) casId);
		return this.getCombatTeam(npcDefine.getNpcId(),
				npcDefine.getTroop().getTroopId(), true);
	}

	@Override
	public CombatTeam getCombatTeam(long casId, boolean isDefender) {
		NPCDefine npcDefine = npcService.getNPCDefine((int) casId);
		return this.getCombatTeam(npcDefine.getNpcId(),
				npcDefine.getTroop().getTroopId(), isDefender);
	}

	@Override
	public CombatTeam getCombatTeam(NPCDefine npcDefine, boolean isDefender) {
		CombatTeam team = null;
		if (npcDefine != null) {
			NPCTroop troop = npcDefine.getTroop();
			team = new CombatTeam(CombatTeam.TEAM_TYPE_NPCHERO);

			NPCHero[] heros = null;
			if (troop != null) {
				heros = troop.getHeros();
				for (int j = 0; j < heros.length; j++) {
					NPCHero hero = heros[j];
					if (hero != null) {
						team.addUint(new NPCHeroCombatUnit(hero, j + 7));
					}
				}

			}
			team.setUserId(npcDefine.getNpcId());
			team.setTeamName(npcDefine.getNpcName());
			team.setIcon(npcDefine.getIconPath());
			team.setCasId(npcDefine.getNpcId());
			team.setLevel(npcDefine.getLevel());
			team.setTroopId(troop.getTroopId());
			team.setTroopName(npcDefine.getNpcName());
			team.setTeamPower(npcDefine.getCombatPower());
		}
		return team;
	}

	@Override
	public CombatTeam getCombatTeam(long casId, long troopId,
			boolean isDefender) {
		NPCDefine npcDefine = npcService.getNPCDefine((int) casId);
		return getCombatTeam(npcDefine, isDefender);
	}

	public CombatTeam getCombatTeamByUser(User user, Troop troop,
			boolean isDefender, boolean fullArmy) {
		throw new BaseException("not support........");
	}

	@Override
	public CombatTeam getCombatTeamByUser(User user, Troop troop, NPCDefine npc,
			boolean isDefender, boolean fullArmy) {
		throw new BaseException("not support........");
	}

	@Override
	public CombatTeam getNextDefenceCombatTeam(long casId) {
		NPCAttackConf npcAttackConf = mapService.getNPCAttackConf((int) casId);
		if (npcAttackConf == null) {
			throw new BaseException("npcAttackConf配置不存在");
		}
		NPCDefine npcDefine = npcService.getNPCDefine(npcAttackConf.getNpcId());
		if (npcDefine == null) {
			throw new BaseException("npcdefine配置不存在");
		}

		return this.getCombatTeam(npcDefine, true);
	}
}

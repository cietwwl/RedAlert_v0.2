package com.youxigu.dynasty2.combat.service.impl;

import java.util.Map;

import com.youxigu.dynasty2.combat.domain.Combat;

public class EVPAfterCombatService extends PVEAfterCombatService {

	@Override
	public void doSaveAfterCombat(Combat combat, boolean allEnd,
			Map<String, Object> params) {
		// // 发送战斗消息
		// // Combat tmp = (Combat)MemcachedManager.get("aaa");
		// sendDefenderCombatMessage(combat, true);
		// // 进攻方野怪全恢复,不必更新数据库
		// for (CombatUnit unit : combat.getAttacker().getUnits()) {
		// unit
		// .setResumeArmyNum(unit.getInitArmyNum()
		// - unit.getCurrArmyNum());
		// }
		//
		// CombatTeam team = combat.getDefender();
		// updateUserArmyAdviser(team);
		//
		// // 防守方回复
		// // 回复守方兵力
		//
		// if (team != null) {
		// resumeDefenderCombatTeam(team, false);
		//
		// // 更新协防玩家的出征记录
		// ArmyOut armyout1 = (ArmyOut) team.getParams();
		// if (armyout1 != null) {
		// armyout1 = lockArmyout(armyout1);
		// armyout1.setCombatId(combat.getCombatId());
		// combatDao.updateArmyOut(armyout1);
		// }
		// }
		//
		// // 更新出征记录与战斗ID的关联
		// ArmyOut armyout = (ArmyOut) combat.getParams();
		// if (armyout != null) {
		// armyout = lockArmyout(armyout);
		// armyout.setCombatId(combat.getCombatId());
		// combatDao.updateArmyOut(armyout);
		// }
		//
		// int npcId = (int) combat.getAttacker().getUserId();
		// NPCDefine npcDefine = npcService.getNPCDefine(npcId);
		// User defUser = userService
		// .getUserById(combat.getDefender().getUserId());// 可能是协防玩家
		// User attackedUser = defUser;// 被攻击的城池用户
		// if (armyout != null
		// && armyout.getDefenderId() != defUser.getMainCastleId()) {
		// Castle castle = castleService
		// .getCastleById(armyout.getDefenderId());
		// attackedUser = userService.getUserById(castle.getCasId());
		// }
		//
		// Collection<DroppedEntity> dropItems = null;
		// Map<Long, Integer> heroExps = null;
		// int userExp = 0;
		// if (allEnd && combat.getWinType() == CombatConstants.WIN_DEF) {
		// CombatRob combatRob = combat.getCombatRob();
		// if (combatRob == null) {
		// combatRob = new CombatRob();
		// combat.setCombatRob(combatRob);
		// }
		// // 掉落包处理
		// // 如果是协防玩家胜利，则掉落给协防玩家
		//
		// // 掉落道具
		// dropItems = doDropPackItem(npcDefine, defUser);
		// int num = dropItems == null ? 0 : dropItems.size();
		// if (num > 0) {
		// DroppedEntity[] winItems = new DroppedEntity[num];
		// Iterator<DroppedEntity> lit = dropItems.iterator();
		// num = 0;
		// while (lit.hasNext()) {
		// winItems[num] = lit.next();
		// num++;
		// }
		// combatRob.setWinItems(winItems);
		// }
		//
		// // 掉落武将经验
		// List<CombatUnit> units = team.getUnits();
		// List<Hero> heros = new ArrayList<Hero>();
		// for (CombatUnit unit : units) {
		// if (unit instanceof PlayHeroCombatUint) {
		// heros.add(((PlayHeroCombatUint) unit)._getHero());
		// }
		// }
		//
		// if (heros != null && heros.size() > 0) {
		// heroExps = doDropPackHeroExp(npcDefine, defUser, heros);
		// if (heroExps != null) {
		// // 设置武将经验奖励，前台使用
		// for (CombatUnit unit : units) {
		// if (unit instanceof PlayHeroCombatUint) {
		// Integer exp = heroExps
		// .get(((PlayHeroCombatUint) unit)._getHero()
		// .getHeroId());
		// if (exp != null) {
		// unit.setAwardExp(exp);
		// }
		// }
		// }
		// }
		// }
		//
		// // 君主经验
		// userExp = doUpdateUserExps(combat, npcDefine, defUser);
		// combatRob.setWinHonor(userExp);
		// if (npcId == AppConstants.MISSION_EVPMISSION_NPC_ID) {
		// // if (attackedUser==defUser){
		// // 通知任务:EVP任务
		// missionService.notifyMissionModule(attackedUser,
		// Mission.QCT_TYPE_WAR,
		// AppConstants.MISSION_EVPMISSION_NPC_ID, 1);
		// // }
		// }
		//
		// } else {
		// if (npcId == AppConstants.MISSION_EVPMISSION_NPC_ID) {
		// if (attackedUser == defUser) {
		// // evp任务失败。自动开始下一次NPC出征
		// combatService.doStartNPCArmyOutJob(
		// AppConstants.MISSION_EVPMISSION_NPC_ID,
		// attackedUser);
		// }
		// }
		// }
		//
		// // 仇恨值更新
		// // combatService.addUserNpcAnimus(defUser.getUserId(), npcDefine
		// // .getNpcType(), 0,
		// // combat.getWinType() == CombatConstants.WIN_DEF ? (short)-100 :
		// // (short)-200 /*仇恨值*/,
		// // combat.getWinType() == CombatConstants.WIN_DEF ? (short)1 :
		// (short)-1
		// // /*获胜次数*/);
		//
		// // 缓存最后一次野怪攻击
		// // combatService.setLastNpcAttackTime(defUser.getUserId(),new
		// //
		// Timestamp(System.currentTimeMillis()),npcDefine.getLevel(),combat.getWinType());
		//
		// // 发送邮件
		// // doSendCombatReportMail(combat, npcDefine, defUser, attackedUser,
		// // dropItems, heroExps, userExp);
		// combat.setParams(null);
		// combatService.saveCombat(combat);
		// // // 保存战斗对象，目前是保存在远程缓存中,12小时,这里只保存到远程缓存，目的是去除transient属性值
		// // MemcachedManager.set(combat.getCombatId(),
		// // combat,CacheModel.CACHE_ALL,cacheSeconds);
	}

}

package com.youxigu.dynasty2.combat.service.impl;

import java.util.Map;

import com.youxigu.dynasty2.combat.domain.Combat;
import com.youxigu.dynasty2.entity.service.IEntityService;
import com.youxigu.dynasty2.mission.service.IMissionService;
import com.youxigu.dynasty2.npc.service.INPCService;

public class PVEAfterCombatService extends DefaultAfterCombatService {
	protected INPCService npcService;
	protected IEntityService entityService;
	protected IMissionService missionService;

	public void setMissionService(IMissionService missionService) {
		this.missionService = missionService;
	}

	public void setNpcService(INPCService npcService) {
		this.npcService = npcService;
	}

	public void setEntityService(IEntityService entityService) {
		this.entityService = entityService;
	}

	@Override
	public void doSaveAfterCombat(Combat combat, boolean allEnd,
			Map<String, Object> params) {
		// // 发送战斗消息
		// sendAttackerCombatMessage(combat, true);
		// // sendDefenderCombatMessage(combat);
		//
		// // 回复进攻方
		// CombatTeam atkTeam = combat.getAttacker();
		// updateUserArmyAdviser(atkTeam);
		// resumeAttackerCombatTeam(atkTeam);
		//
		// // 防守方野怪全恢复,不必更新数据库
		// // 策划要求不恢复
		// // for (CombatUnit unit : combat.getDefender().getUnits()) {
		// // unit
		// // .setResumeArmyNum(unit.getInitArmyNum()
		// // - unit.getCurrArmyNum());
		// // }
		//
		// // 更新出征记录与战斗ID的关联
		// ArmyOut armyout = (ArmyOut) combat.getParams();
		// if (armyout != null) {
		// armyout = lockArmyout(armyout);
		// armyout.setCombatId(combat.getCombatId());
		// //
		// 设置成战斗状态，战斗后返回会改成Back状态，这里设置战斗状态的目的是防止战斗导致被攻击方流亡，流亡功能会强制返回所有前进以及驻守状态的出征。
		// armyout.setStatus(ArmyOut.STATUS_COMBAT);
		// combatDao.updateArmyOut(armyout);
		// }
		//
		// int npcId = (int) combat.getDefender().getUserId();
		//
		// NPCDefine npcDefine = npcService.getNPCDefine(npcId);
		// User attackUser = userService.getUserById(combat.getAttacker()
		// .getUserId());
		//
		// NPCAttackConf npcAttackConf = npcService.getNPCAttackConf(npcId);
		// if (npcAttackConf == null) { // 任务NPC
		// // 掉落包处理
		// Collection<DroppedEntity> dropItems = null;
		// Map<Long, Integer> heroExps = null;
		// int userExp = 0;
		// if (combat.getWinType() == CombatConstants.WIN_ATK) {
		// CombatRob combatRob = combat.getCombatRob();
		// if (combatRob == null) {
		// combatRob = new CombatRob();
		// combat.setCombatRob(combatRob);
		// }
		// // 君主经验
		// userExp = doUpdateUserExps(combat, npcDefine, attackUser);
		// combatRob.setWinHonor(userExp);
		// // 掉落道具
		// dropItems = doDropPackItem(npcDefine, attackUser);
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
		// List<CombatUnit> units = atkTeam.getUnits();
		// List<Hero> heros = new ArrayList<Hero>();
		// for (CombatUnit unit : units) {
		// if (unit instanceof PlayHeroCombatUint) {
		// heros.add(((PlayHeroCombatUint) unit)._getHero());
		// }
		// }
		// heroExps = doDropPackHeroExp(npcDefine, attackUser, heros);
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
		//
		// }
		// } else {
		// // 出征野怪
		// Collection<DroppedEntity> dropItems = null;
		// if (combat.getWinType() == CombatConstants.WIN_ATK) {
		//
		// UserNpcAttacked una = combatService
		// .lockGetUserNpcAttacked(attackUser.getUserId());
		//
		// NPCAttackConf maxNpc = npcService.getNPCAttackConf(una
		// .getMaxNpcId());
		// if (maxNpc == null) {
		// maxNpc = npcService.getFirstNPCAttackConf();
		// }
		// if (npcAttackConf == maxNpc) {
		// NPCAttackConf next = maxNpc.getNext();
		// if (next != null) {
		// una.setMaxNpcId(next.getNpcId());
		// combatDao.updateUserNpcAttacked(una);
		// }
		// }
		//
		// CombatRob combatRob = combat.getCombatRob();
		// if (combatRob == null) {
		// combatRob = new CombatRob();
		// combat.setCombatRob(combatRob);
		// }
		// // 掉落资源，铜币
		// doDropResource(combat, npcAttackConf, attackUser);
		//
		// // 掉落道具doDropPackItem
		// dropItems = doDropPackItem(npcAttackConf, attackUser);
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
		// int heroExp = npcAttackConf.getHeroExp();
		// int finalExp = 0;
		// if (heroExp > 0) {
		// heroExp = heroService.getActualExp(heroExp, attackUser
		// .getUserId(), attackUser.getMainCastleId(),
		// AppConstants.GET_PVE_EXP);
		// List<CombatUnit> units = atkTeam.getUnits();
		// // List<Hero> heros = new ArrayList<Hero>();
		// for (CombatUnit unit : units) {
		// if (unit instanceof PlayHeroCombatUint) {
		// Hero hero = ((PlayHeroCombatUint) unit)._getHero();
		// hero = heroService.lockAndGetHero(hero.getUserId(),
		// hero.getHeroId());
		// finalExp = heroService.doChangeHeroExp(hero,
		// heroExp, false, true);
		// unit.setAwardExp(finalExp);
		// }
		// }
		// }
		// // 发送邮件
		// doSendCombatReportMail(combat, finalExp);
		// } else {
		// // 发送邮件
		// doSendCombatReportMail(combat, 0);
		// }
		//
		// }
		// // 仇恨值更新 TODO 测试临时改成50
		// // combatService.addUserNpcAnimus(attackUser.getUserId(), npcDefine
		// // .getNpcType(),
		// // combat.getWinType() == CombatConstants.WIN_ATK ? npcDefine
		// // .getNpcId() : 0,
		// // AppConstants.NPC_ANIMUS, (short) 0);
		//
		// // doSendCombatReportMail(combat, dropItems, heroExps, userExp);
		//
		// // 通知任务模块
		// // PVE任务完成,胜利才通知
		// if (combat.getWinType() == CombatConstants.WIN_ATK) {
		// missionService.notifyMissionModule(attackUser,
		// Mission.QCT_TYPE_KILL, npcDefine.getNpcId(), 1);
		// }
		// combat.setParams(null);
		// combatService.saveCombat(combat, false);
		// // // 保存战斗对象，目前是保存在远程缓存中,12小时,这里只保存到远程缓存，目的是去除transient属性值
		// // MemcachedManager.set(combat.getCombatId(), combat,
		// // CacheModel.CACHE_ALL, cacheSeconds);
	}

	/**
	 * 发送战报邮件
	 * 
	 * @param combat
	 */
	protected void doSendCombatReportMail(Combat combat, int heroExp) {

		// CombatTeam atkTeam = combat.getAttacker();
		// CombatTeam defTeam = combat.getDefender();
		//
		// Map<String, Object> reportMap = new HashMap<String, Object>();// 战报结果
		// // reportMap.put("type", combat.getCombatType());
		//
		// User atkUser = userService.getUserById(atkTeam.getUserId());
		//
		// reportMap.put("atkUserName", atkTeam.getTeamName());
		// reportMap.put("dfcUserName", defTeam.getTeamName());
		//
		// // 战斗部队
		// reportMap.put("atkTeams", convertCombatUnit2Map(atkTeam.getUnits()));
		// reportMap.put("dfcTeams", convertCombatUnit2Map(defTeam.getUnits()));
		//
		// // 城防消耗 ,策划说不显示
		// // int[] consumeTowerArmys =
		// convertTowerCombatUnit(defTeam.getUnits());
		// // if (consumeTowerArmys != null) {
		// // reportMap.put("towerLost", consumeTowerArmys);
		// // }
		//
		// // 掉落道具
		// CombatRob combatRob = combat.getCombatRob();
		// if (combatRob != null) {
		// reportMap.put("items", combat.getCombatRob().getWinItems());
		// }
		// if (heroExp != 0) {
		// reportMap.put("heroExp", heroExp);
		// }
		//
		// // if ()
		// // 发送进攻方邮件
		// Map<String, Object> params = new HashMap<String, Object>();
		//
		// if (combat.getWinType() == CombatConstants.WIN_ATK) {
		//
		// // 攻方获胜，
		// reportMap.put("win", true);
		//
		// convertCombatRobWin(combat, reportMap);// 资源与繁荣
		// params.put("toUserName", defTeam.getTeamName());
		// params.put("heroExp", heroExp);
		//
		// String title = AppConstants.FONT_COLOR_GREEN + "战斗胜利</font>";
		//
		// String context = mailMessageService.getContextByTemplate(params,
		// "/template/war_pve1.vm");
		// sendPlayerWarReport(title, context, atkUser.getUserId(), reportMap);
		//
		// } else {
		// // 攻方失败，
		//
		// reportMap.put("win", false);
		// // convertCombatRobLost(combat, reportMap);// 资源与繁荣
		// params.put("toUserName", defTeam.getTeamName());
		//
		// String title = AppConstants.FONT_COLOR_RED + "战斗失败</font>";
		//
		// String context = mailMessageService.getContextByTemplate(params,
		// "/template/war_pve2.vm");
		// sendPlayerWarReport(title, context, atkUser.getUserId(), reportMap);
		//
		// }

	}

	// protected int doUpdateUserExps(Combat combat, NPCDefine npcDefine, User
	// user) {
		// double exp = npcDefine.getMerit();// 野怪经验
		// double desc = 1d;
		//
		// if (user.getUsrLv() - 5 > npcDefine.getLevel()) {
		// desc = Double.valueOf(5.0d / (user.getUsrLv()
		// - npcDefine.getLevel() - 5)) / 4.0d;
		// }
		//
		// int addExp = (int) (desc * exp);
		// // TODO:在线时间衰减
		// if (addExp > 0) {
		// userService.updateUserHonor(user, addExp);
		// }
		//
		// return addExp;
	// }

	/*
	 * protected List<Map<String, Object>> convertCombatUnit2Map(
	 * List<CombatUnit> units, Map<Long, Integer> heroExps) { List<Map<String,
	 * Object>> rtn = new ArrayList<Map<String, Object>>(); for (CombatUnit unit
	 * : units) { if (unit instanceof WallCombatUnit || unit instanceof
	 * TowerCombatUnit) { continue; } Map<String, Object> map = new
	 * HashMap<String, Object>(); // map.put("teamName",
	 * team.getHero().getShowName()); map.put("teamName", unit.getName()); if
	 * (unit.getLevel() <= 0) { map.put("heroLv", "--"); } else {
	 * map.put("heroLv", unit.getLevel()); } String tmp = unit._getArmyName();
	 * if (tmp == null) { tmp = "--"; } map.put("armyName", tmp);
	 * map.put("initNum", unit.getInitArmyNum());// 初始数量 map.put("currNum",
	 * unit.getCurrArmyNum());// 最终数量 map.put("resumeNum",
	 * unit.getResumeArmyNum());// 恢复数量 map.put("loseNum", unit.getInitArmyNum()
	 * - unit.getCurrArmyNum());// 伤亡数量 Integer exp = null; if (unit instanceof
	 * PlayHeroCombatUint) { if (heroExps != null) { exp =
	 * heroExps.get(((PlayHeroCombatUint) unit)._getHero() .getHeroId()); } } if
	 * (exp != null) { map.put("exp", exp.intValue()); } else { map.put("exp",
	 * 0); } rtn.add(map); } int diff = 5 - rtn.size(); for (int i = 0; i <
	 * diff; i++) { Map<String, Object> map = new HashMap<String, Object>();
	 * map.put("teamName", "--"); map.put("heroLv", "--"); map.put("armyName",
	 * "--"); map.put("initNum", "--"); map.put("currNum", "--");
	 * map.put("loseNum", "--"); map.put("resumeNum", "--"); map.put("exp",
	 * "--"); rtn.add(map); }
	 * 
	 * return rtn; }
	 */

	// /**
	// * 掉落的道具 + 武将经验
	// *
	// * @param combat
	// * @return
	// */
	// protected Collection<DroppedEntity> doDropPackItem(NPCDefine npcDefine,
	// User attackUser) {
//		Collection<DroppedEntity> dropedEntitys = null;
//		if (npcDefine != null) {
//			try {
//				// 掉落道具
//				int itemPackId = npcDefine.getCombatItemPackId();
//				if (itemPackId != 0) {
//					Entity entity = entityService.getEntity(itemPackId);
//					if (entity != null) {
//						Map<String, Object> params = new HashMap<String, Object>();
//						params.put("user", attackUser);
//						params.put("iAction", "PVE");
//						Map<String, Object> dropItems = entityService.doAction(
//								entity, Entity.ACTION_USE, params);
//						if (dropItems != null) {
//							Map<Integer, DroppedEntity> datas = (Map<Integer, DroppedEntity>) dropItems
//									.get("items");
//							if (datas != null) {
//								dropedEntitys = datas.values();
//							}
//						}
//					} else {
//						log.warn("掉落包不存在:{}", itemPackId);
//					}
//				}
//			} catch (Exception e) {
//				// 忽略异常
//				log.error("战斗掉落异常", e);
//			}
//		}
//		return dropedEntitys;
	// }
	//
	// /**
	// * 掉落的道具 + 武将经验
	// *
	// * @param combat
	// * @return
	// */
	// protected Collection<DroppedEntity> doDropPackItem(
	// NPCAttackConf npcAttackConf, User attackUser) {
	// Collection<DroppedEntity> dropedEntitys = null;
	//
	// try {
	// // 掉落道具
	// int itemPackId = npcAttackConf.getDropPackId();
	//
	// if (itemPackId != 0) {
	// Entity entity = entityService.getEntity(itemPackId);
	// if (entity != null) {
	// if (entity instanceof DropPackEntity
	// && ((DropPackEntity) entity).getChildType() != Item.ITEM_TYPE_BOX_DROP) {
	//
	// Map<String, Object> params = new HashMap<String, Object>();
	// params.put("user", attackUser);
	// params.put("iAction", "攻打NPC掉落");
	// Map<String, Object> dropItems = entityService.doAction(
	// entity, Entity.ACTION_USE, params);
	// if (dropItems != null && dropItems.size() > 0) {
	// Map<Integer, DroppedEntity> datas = (Map<Integer, DroppedEntity>)
	// dropItems
	// .get("items");
	// if (datas != null) {
	// dropedEntitys = datas.values();
	// }
	// }
	//
	// } else {
	// // 直接掉道具
	// ITreasuryService treasuryService = (ITreasuryService) ServiceLocator
	// .getSpringBean("treasuryService");
	// treasuryService.addItemToTreasury(attackUser
	// .getUserId(), itemPackId, 1, 1, -1, false,
	// true, "攻打NPC掉落");
	// dropedEntitys = new ArrayList<DroppedEntity>(1);
	// DroppedEntity droppedEntity = new DroppedEntity(
	// itemPackId, 1);
	// dropedEntitys.add(droppedEntity);
	//
	// }
	// } else {
	// log.warn("掉落包不存在:{}", itemPackId);
	// }
	//
	// }
	//
	// } catch (Exception e) {
	// // 忽略异常
	// log.error("战斗掉落异常", e);
	// }
	//
	// return dropedEntitys;
	// }
	//
	// /**
	// *
	// * @param npcDefine
	// * @param attackUser
	// * @return
	// */
	// protected void doDropResource(Combat combat, NPCAttackConf npcAttackConf,
	// User attackUser) {
	// Collection<DroppedEntity> dropedEntitys = null;
	// // 资源，铜币
	// int money = npcAttackConf.getMoney();
	// int resource = npcAttackConf.getResource();
	// CombatRob combatRob = combat.getCombatRob();
	// combatRob.setWinBronzeNum(resource);
	// combatRob.setWinFoodNum(resource);
	// combatRob.setWinStoneNum(resource);
	// combatRob.setWinWoodNum(resource);
	// combatRob.setWinMoneyNum(money);
	// combatRob.setLostBronzeNum(resource);
	// combatRob.setLostFoodNum(resource);
	// combatRob.setLostStoneNum(resource);
	// combatRob.setLostWoodNum(resource);
	// combatRob.setLostMoneyNum(money);
	//
	// long userId = attackUser.getUserId();
	// int percent = accountService.getOnlineUserEffect(userId);
	// if (percent != 0) {
	// int tmp = (int) (money * (100d + percent) / 100);
	// castleService.onlineAddicted(userId, percent, money, tmp, "铜币");
	// money = tmp;
	//
	// tmp = (int) (resource * (100d + percent) / 100);
	// castleService.onlineAddicted(userId, percent, resource, tmp, "粮食");
	// castleService.onlineAddicted(userId, percent, resource, tmp, "木材");
	// castleService.onlineAddicted(userId, percent, resource, tmp, "石料");
	// castleService.onlineAddicted(userId, percent, resource, tmp, "青铜");
	// resource = tmp;
	// }
	// CastleResource castleRes = castleService.lockCasRes(combat
	// .getAttacker().getCasId());
	//
	// if (money > 0) {
	// castleRes.setMoneyNum(castleRes.getMoneyNum() + money);
	// }
	// if (resource > 0) {
	// castleRes.setFoodNum(castleRes.getFoodNum() + resource);
	// castleRes.setStoneNum(castleRes.getStoneNum() + resource);
	// castleRes.setWoodNum(castleRes.getWoodNum() + resource);
	// castleRes.setBronzeNum(castleRes.getBronzeNum() + resource);
	// }
	//
	// castleService.updateCastleResource(castleRes, attackUser.getUserId());//
	// 更新数据
	//
	// }
	//
	// // 武将经验掉落
	// protected Map<Long, Integer> doDropPackHeroExp(NPCDefine npcDefine,
	// User attackUser, List<Hero> heros) {
	// Map<Long, Integer> result = null;
	// int expPackId = npcDefine.getCombatExpPackId();
	// if (expPackId != 0) {
	// try {
	// Entity entity = entityService.getEntity(expPackId);
	// if (entity != null) {
	//
	// Map<String, Object> params = new HashMap<String, Object>();
	// params.put("user", attackUser);
	// params.put("heros", heros);
	// params.put("pveLv", npcDefine.getLevel());
	// params.put("expType", AppConstants.GET_PVE_EXP);
	//
	// Map<String, Object> dropItems = entityService.doAction(
	// entity, Entity.ACTION_USE, params);
	// if (dropItems != null) {
	// result = (Map<Long, Integer>) dropItems.get("heroExp");
	// }
	// } else {
	// log.warn("掉落包不存在:{}", expPackId);
	// }
	// } catch (Exception e) {
	// // 忽略异常
	// log.error("战斗掉落异常", e);
	// }
	// }
	// return result;
	// }

}

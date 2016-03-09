package com.youxigu.dynasty2.tower.service.impl;

import com.ibatis.sqlmap.engine.cache.memcached.MemcachedManager;
import com.youxigu.dynasty2.activity.domain.Activity;
import com.youxigu.dynasty2.activity.service.IActivityService;
import com.youxigu.dynasty2.activity.service.IOperateActivityService;
import com.youxigu.dynasty2.combat.domain.Combat;
import com.youxigu.dynasty2.combat.domain.CombatConstants;
import com.youxigu.dynasty2.combat.domain.CombatTeam;
import com.youxigu.dynasty2.combat.service.IAtferCombatService;
import com.youxigu.dynasty2.combat.service.ICombatEngine;
import com.youxigu.dynasty2.combat.service.ICombatTeamService;
import com.youxigu.dynasty2.common.AppConstants;
import com.youxigu.dynasty2.common.service.ICommonService;
import com.youxigu.dynasty2.develop.service.ICastleService;
import com.youxigu.dynasty2.entity.domain.DropPackEntity;
import com.youxigu.dynasty2.entity.domain.DroppedEntity;
import com.youxigu.dynasty2.entity.domain.Entity;
import com.youxigu.dynasty2.entity.domain.Item;
import com.youxigu.dynasty2.entity.service.IEntityService;
import com.youxigu.dynasty2.hero.domain.Hero;
import com.youxigu.dynasty2.hero.domain.Troop;
import com.youxigu.dynasty2.hero.service.IHeroService;
import com.youxigu.dynasty2.hero.service.ITroopService;
import com.youxigu.dynasty2.log.ILogService;
import com.youxigu.dynasty2.log.LogBeanFactory;
import com.youxigu.dynasty2.log.imp.BattleLog;
import com.youxigu.dynasty2.log.imp.LogBattleAct;
import com.youxigu.dynasty2.log.imp.LogItemAct;
import com.youxigu.dynasty2.mission.domain.Mission;
import com.youxigu.dynasty2.mission.service.IMissionService;
import com.youxigu.dynasty2.npc.domain.NPCDefine;
import com.youxigu.dynasty2.npc.service.INPCService;
import com.youxigu.dynasty2.tower.dao.ITowerDao;
import com.youxigu.dynasty2.tower.domain.Tower;
import com.youxigu.dynasty2.tower.domain.TowerSectionBonus;
import com.youxigu.dynasty2.tower.domain.TowerUser;
import com.youxigu.dynasty2.tower.service.ITowerService;
import com.youxigu.dynasty2.treasury.service.ITreasuryService;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.user.service.IAccountService;
import com.youxigu.dynasty2.user.service.IUserAchieveService;
import com.youxigu.dynasty2.user.service.IUserService;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.dynasty2.util.EffectValue;
import com.youxigu.dynasty2.util.StringUtils;
import com.youxigu.dynasty2.vip.service.IVipService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeoutException;

public class TowerService implements ITowerService {
    private Logger log = LoggerFactory.getLogger(TowerService.class);

    private Map<Integer, Tower> towers = new HashMap<Integer, Tower>();// 各关的配置信息
    private List<TowerSectionBonus> towerSectionBonusList = new ArrayList<TowerSectionBonus>();

    private static final String MEMCACHE_TOWER_KEY_NAME = "MEMCACHE_TOWER_KEY_NAME_";
    private static final String TOWER_LOCK_PREFIX = "TOWER_LOCK_";

    public static final int TOWER_SELECT_ITEM = 1030109;// 作战实验室磁卡

    private ITowerDao towerDao;
    private ICommonService commonService;
    private ITroopService troopService;
    private IHeroService heroService;
    private IUserService userService;
    //	private IRankService rankService;
    private ITreasuryService treasuryService;
    private ICombatTeamService npcCombatTeamService;
    private ICombatTeamService playerCombatTeamService;
    private ICombatEngine combatEngine;
    private IEntityService entityService;
    private IUserAchieveService userAchieveService;
    private INPCService npcService;
    private IAtferCombatService afterCombatService;
    private IActivityService activityService;
    private IMissionService missionService;
    private ICastleService castleService;
    private ILogService logService;
    private ILogService tlogService;
    private IAccountService accountService;
    private IVipService vipService;
    private IOperateActivityService operateActivityService;

    public void setCastleService(ICastleService castleService) {
        this.castleService = castleService;
    }

    public void setOperateActivityService(
            IOperateActivityService operateActivityService) {
        this.operateActivityService = operateActivityService;
    }

    public void setAccountService(IAccountService accountService) {
        this.accountService = accountService;
    }

    public void setMissionService(IMissionService missionService) {
        this.missionService = missionService;
    }

    public void setTowerDao(ITowerDao towerDao) {
        this.towerDao = towerDao;
    }

    public void setVipService(IVipService vipService) {
        this.vipService = vipService;
    }

    public void setCommonService(ICommonService commonService) {
        this.commonService = commonService;
    }

    public void setTroopService(ITroopService troopService) {
        this.troopService = troopService;
    }

    public void setHeroService(IHeroService heroService) {
        this.heroService = heroService;
    }

    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

//	public void setRankService(IRankService rankService) {
//		this.rankService = rankService;
//	}

    public void setTreasuryService(ITreasuryService treasuryService) {
        this.treasuryService = treasuryService;
    }

    public void setNpcCombatTeamService(ICombatTeamService npcCombatTeamService) {
        this.npcCombatTeamService = npcCombatTeamService;
    }

    public void setPlayerCombatTeamService(
            ICombatTeamService playerCombatTeamService) {
        this.playerCombatTeamService = playerCombatTeamService;
    }

    public void setCombatEngine(ICombatEngine combatEngine) {
        this.combatEngine = combatEngine;
    }

    public void setEntityService(IEntityService entityService) {
        this.entityService = entityService;
    }

    public void setUserAchieveService(IUserAchieveService userAchieveService) {
        this.userAchieveService = userAchieveService;
    }

    public void setNpcService(INPCService npcService) {
        this.npcService = npcService;
    }

    public void setAfterCombatService(IAtferCombatService afterCombatService) {
        this.afterCombatService = afterCombatService;
    }

    public void setActivityService(IActivityService activityService) {
        this.activityService = activityService;
    }

    public void setLogService(ILogService logService) {
        this.logService = logService;
    }

    public void setTlogService(ILogService tlogService) {
        this.tlogService = tlogService;
    }

    public void init() {
        log.info("初始化打塔数据开始");

        List<Tower> towerList = towerDao.getTowers();
        for (Tower tower : towerList) {
            NPCDefine npc = npcService.getNPCDefine(tower.getNpcId());
            if (npc == null) {
                log.warn("npcdefine不存在:{}", tower.getNpcId());
            }
            tower.setNpc(npc);

            Map<String, EffectValue> attrValues = new HashMap<String, EffectValue>();
            String effectStr = tower.getAdditionEffect();
            if (effectStr != null && !StringUtils.isEmpty(effectStr) && !"0".equals(effectStr)) {
                String ats[] = effectStr.split(",");
                if (ats.length != 3) {
                    throw new BaseException(
                            "[tower npc effects属性key,absValue,perValue]关卡配置的加成属性格式错误" +
                                    tower.getStageId() + "," + effectStr);
                }
                EffectValue val = new EffectValue(Integer.valueOf(ats[1]),
                        Integer.valueOf(ats[2]));
                attrValues.put(ats[0], val);
            }
            tower.setAttrValues(attrValues);

            towers.put(tower.getStageId(), tower);
        }

        towerSectionBonusList = towerDao.getTowerSectionBonuses();

        log.info("初始化打塔数据结束");
    }

    @Override
    public TowerUser doGetAndUpdateTowerUser(long userId) {
        lockTower(userId);
        TowerUser towerUser = towerDao.getTowerUser(userId);
        if (towerUser == null) {
            Timestamp date = new Timestamp(System.currentTimeMillis());
            towerUser = new TowerUser();
            towerUser.setUserId(userId);
            towerUser.setStageId(1);
            towerUser.setTopStageId(0);

            towerUser.setLastJoinDttm(date);
            towerUser.setSeasonWinItemId("");
            towerUser.setReliveTimes(getReliveUpperLimit());
            towerDao.createTowerUser(towerUser);
        } else {
            if (towerUser.getJoinStatus() == TowerUser.JOIN_STATUS_FREE) {
                if (towerUser.getLastJoinDttm() != null) {
                    Calendar c = Calendar.getInstance();
                    int day2 = c.get(Calendar.DAY_OF_YEAR);

                    c.setTime(towerUser.getLastJoinDttm());
                    int day1 = c.get(Calendar.DAY_OF_YEAR);

                    if (day1 != day2) {
                        towerUser.setFreeJoinTime(0);
                        towerUser.setItemJoinTime(0);
                        towerUser.setScore(0);
                        towerUser.setWinItemId(0);
                        towerUser.setSeasonWinItemId("");
                        towerUser.setLastJoinDttm(new Timestamp(System
                                .currentTimeMillis()));
                        towerDao.updateTowerUser(towerUser);
                    }
                }
            }
        }
        return towerUser;
    }

    //list内容：
    //0：towerUser
    //1：Map<Integer, Integer> 扫荡获得的物品。key为entId，value为num
    @Override
    public Map<Integer, Integer> doEnter(User user) {
        lockTower(user.getUserId());
        // 验证用户
        this.validateUser(user);

        TowerUser towerUser = doGetAndUpdateTowerUser(user.getUserId());

        // 当前在打塔中，直接返回
        if (towerUser.getJoinStatus() == TowerUser.JOIN_STATUS_JOIN) {
            return new HashMap<Integer, Integer>();
        }

        long userId = user.getUserId();
        int freeJoinTime = towerUser.getFreeJoinTime();
        int totalFreeTime = this.getTotalFreeTime(userId);

        int itemJoinTime = towerUser.getItemJoinTime();
        int totalItemTime = this.getTotalItemTime(userId);
        if (freeJoinTime < totalFreeTime) {// 如果还有免费入场机会
            towerUser.setFreeJoinTime(towerUser.getFreeJoinTime() + 1);
            // 重置复活次数
            towerUser.setReliveTimes(getReliveUpperLimit());
        } else if (itemJoinTime < totalItemTime) {// 道具入场机会
            int itemNum = treasuryService.getTreasuryCountByEntId(userId,
                    TOWER_SELECT_ITEM);
            // add by hhq 根据进入次数消耗重楼令
            int curT = this.getItemCountFactor(towerUser.getItemJoinTime());
            if (itemNum >= curT) {
                treasuryService.deleteItemFromTreasury(userId,
                        TOWER_SELECT_ITEM, curT, true,
                        com.youxigu.dynasty2.log.imp.LogItemAct.LOGITEMACT_70);
            } else {
                Item item = (Item) entityService.getEntity(TOWER_SELECT_ITEM);
                throw new BaseException("【" + item.getItemName() + "】不足，请去商城购买");
            }
            towerUser.setItemJoinTime(towerUser.getItemJoinTime() + 1);
            towerUser.setReliveTimes(getReliveUpperLimit());
        } else {
            throw new BaseException("今天进入次数已达上限，请明天再来");
        }
        // 进入嗒
        towerUser.setJoinStatus(TowerUser.JOIN_STATUS_JOIN);
        towerDao.updateTowerUser(towerUser);

        Map<Integer, Integer> items = doFastCombat(user, towerUser);

        //todo: 日常活动、任务等更新
//		udaService.addUserDailyActivity(userId, DailyActivity.ACT_TOWER,
//				(byte) 1);
//		setTowerTLog(LogTypeConst.TYPE_TOWER_JOIN, user, stageId - 1);
//		// 挑战N次千重楼 重新进入千重楼算1次
//		missionService.notifyMissionModule(user, Mission.QCT_TYPE_TOWERENTER,
//				0, 1);
        return items;
    }

    @Override
    public void doExit(long userId) {
        lockTower(userId);
        TowerUser towerUser = towerDao.getTowerUser(userId);

        if (towerUser.getJoinStatus() != TowerUser.JOIN_STATUS_JOIN) {
            throw new BaseException("您当前不在活动中");
        }

        // 记录退出重楼的日志
        User user = userService.getUserById(userId);
//		setTowerTLog(LogTypeConst.TYPE_TOWER_THROW, user, towerUser
//				.getStageId() - 1);

        towerUser.setJoinStatus(TowerUser.JOIN_STATUS_FREE);
        towerUser.setReliveTimes(getReliveUpperLimit());
        towerUser.setScore(0);
        towerUser.setWinItemId(0);
        towerUser.setSeasonWinItemId("");
        towerUser.setCombatId(null);
        towerDao.updateTowerUser(towerUser);
    }

//
//    public List<DotaRank> getTopN(int num) {
//		List<DotaRank> daraRankList = rankService.getDotaRankByTypeAndPageNo(
//				RankData.RANK_TYPE_DOTA, 1);
//		List<DotaRank> resList = null;
//		if (!Util.isEmpty(daraRankList)) {
//			resList = new ArrayList<DotaRank>();
//			for (DotaRank dataRank : daraRankList) {
//				if (resList.size() < num) {
//					resList.add(dataRank);
//				} else {
//					break;
//				}
//			}
//		}
//		return resList;
//	}

    // 打塔次数
    @Override
    public int getTotalFreeTime(long userId) {
        int totalFreeTime = commonService.getSysParaIntValue(
                AppConstants.SYS_TOWER_FREE_JOIN_TIME,
                AppConstants.SYS_TOWER_FREE_JOIN_TIME_DEFVALUE);
        return totalFreeTime + getActivityFreeTime(userId);
    }

    public int getActivityFreeTime(long userId) {
        int add = activityService.getSumEffectAbsValueByEffectType(userId,
                Activity.TYPE_EF_TOWER_FREE_NUM);

        return add;
    }

    // 打塔经验加成
    @Override
    public int getHeroExpEffect(long userId) {

        int percent = activityService.getSumEffectPercentValueByEffectType(
                userId, Activity.TYPE_EF_TOWER_HERO_EXP);
        return percent;
    }

    @Override
    public int getTotalItemTime(long userId) {
        int totalItemTime = commonService.getSysParaIntValue(
                AppConstants.SYS_TOWER_ITEM_JOIN_TIME,
                AppConstants.SYS_TOWER_ITEM_JOIN_TIME_DEFVALUE);

        // vip加成
//		int vip = vipService.getVipEffctValue(userId,
//				VipEffect.VIP_QIANCHONGLOU_COUNT);
//		return totalItemTime + vip;
        return totalItemTime;
    }

    @Override
    public Tower getTower(int stageId) {
        return this.towers.get(stageId);
    }

    @Override
    public TowerUser doCombat(long userId, NPCDefine npc,
                              Map<String, Object> params) {
        long time = System.currentTimeMillis();
        User user = userService.getUserById(userId);
        TowerUser towerUser = this.doGetAndUpdateTowerUser(userId);

        // 验证是否可以战斗
        this.validateCombat(towerUser);

        Tower tower = null;
        if (npc == null) {
            tower = this.getTower(towerUser.getStageId());
            npc = tower.getNpc();
        }

        if (npc == null) {
            throw new BaseException("未定义防守的NPC");
        }

        _doCombat(towerUser, user, tower, npc, params);

        if (log.isDebugEnabled()) {
            log.debug("作战实验室一场战斗时间:{} ms", (System.currentTimeMillis() - time));
        }
        return towerUser;
    }

    private Combat _doCombat(TowerUser towerUser, User user, Tower tower,
                             NPCDefine npc, Map<String, Object> context) {
        //取得防守方队伍
        CombatTeam defenceTeam = npcCombatTeamService.getCombatTeam(npc.getNpcId());
        if (defenceTeam == null) {
            throw new BaseException("NPC定义不存在");
        }
        // 处理加成属性
        Map<String, com.youxigu.dynasty2.util.EffectValue> vs = tower.getAttrValues();
        for (Map.Entry<String, EffectValue> en : vs.entrySet()) {
            defenceTeam.addEffect(en.getKey(), en.getValue());
        }

        // 获取进攻方队伍
        heroService.lockHero(user.getUserId());
        long troopId = user.getTroopId();

        Troop troop = troopService.getTroopById(user.getUserId(), troopId);
        if (troop == null) {
            throw new BaseException("军团不存在");
        }
        if (!troop.getRealStatus().isCombat()) {
            throw new BaseException("军团不在空闲状态");
        }
        Hero[] heros = troop.getHeros();
        for (Hero hero : heros) {
            if (hero != null) {
                if (!hero.idle()) {
                    throw new BaseException("军团中有武将不是空闲状态");
                }
            }
        }
        CombatTeam attackTeam = playerCombatTeamService.getCombatTeamByUser(user,
                troop, false, true);
        if (attackTeam == null) {
            throw new BaseException("没有进攻队伍");
        }

        int curStageId = tower.getStageId();
        if (log.isDebugEnabled()) {
            log.debug("{} 开始作战实验室第[{}]关。", user.getUserName(), curStageId);
        }
        Combat combat = new Combat(CombatConstants.COMBAT_TYPE_TOWER,
                CombatConstants.SCORETYPE_UNITLIVE, attackTeam, defenceTeam,
                null);

        combatEngine.execCombat(combat);

        String combatId = MEMCACHE_TOWER_KEY_NAME + user.getUserId();
        combat.setCombatId(combatId);
        afterCombatService.doSaveAfterCombat(combat, true, null);

        towerUser.setCombatId(combatId);
        towerUser.setScore(combat.getAttacker().getScore());

        boolean win = combat.getWinType() == CombatConstants.WIN_ATK ? true : false;
        if (win) {
            //todo: 排行功能做完后更新此处
            // 排行
//				rankService.updateTopStageId(towerUser.getUserId(),
//						currStageId, UtilDate.nowTimestamp());

            // 成就
            // userAchieveService.doSaveUserAchieve(towerUser.getUserId(),
            // UserAchieve.TYPE_COMBAT_BABEL, currStageId);

            towerUser.setTopStageId(towerUser.getStageId());
            towerUser.setTopDttm(new Timestamp(System.currentTimeMillis()));
            towerUser.setStageId(towerUser.getStageId() + 1);

            // 通知任务
            missionService.notifyMissionModule(user,
                    Mission.QCT_TYPE_TOWER_STAGE, 0, towerUser.getStageId());

            // // 通知使命
            // dutyClientService.doNotifyDuty(user, Duty.DUTY_TYPE_TOWER, 0,
            // towerUser.getTopStageId());

            // 掉落物品
            Collection<DroppedEntity> des = dropItemPack(user, tower.getFirstBonusId(), LogItemAct.LOGITEMACT_71);
            context.put("bonus", des);

        } else {
            towerUser.setReliveTimes(towerUser.getReliveTimes() - 1);
            if (log.isDebugEnabled()) {
                log.debug("作战实验室第{}层斗失败。", tower.getStageId());
            }
        }
        towerDao.updateTowerUser(towerUser);
		BattleLog.setTlogBattle(user, LogBattleAct.QIANCHONGLOU, combat
                .getWinType() == CombatConstants.WIN_ATK, towerUser
                .getStageId(), 0);
        logService.log(LogBeanFactory.buildTowerLog(user, curStageId, win, combatId));
        context.put("combat", combat);
        return combat;
    }

    private Map<Integer, Integer> doFastCombat(User user, TowerUser towerUser) {
        // 验证是否可以战斗
        this.validateCombat(towerUser);
        int stageId = towerUser.getStageId();

        // 发放道具奖励
        Map<Integer, Integer> items = new HashMap<Integer, Integer>();

        List<Integer> bonusEntIds = new ArrayList<Integer>();
        //每层奖励的掉落包
        for (int i = 1; i < stageId; i++) {
            Tower tower = this.getTower(i);
            if (tower == null) {
                continue;
            }
            int dropEntId = tower.getBonusId();
            if (dropEntId != 0) {
                bonusEntIds.add(dropEntId);
            }
        }
        //根据所处楼层区间进行奖励的保底掉落包
        for (TowerSectionBonus tsb : towerSectionBonusList) {
            if (stageId >= tsb.getStartStage() && stageId <= tsb.getEndStage()) {
                bonusEntIds.add(tsb.getDropEntId());
            }
        }

        //执行掉落逻辑并将结果添加到集合中
        for (int dropEntId : bonusEntIds) {
            Collection<DroppedEntity> des = dropItemPack(user, dropEntId, LogItemAct.LOGITEMACT_71);

            Iterator<DroppedEntity> itl = des.iterator();
            while (itl.hasNext()) {
                DroppedEntity drop = itl.next();
                int entId = drop.getEntId();
                Object old = items.get(entId);
                if (old != null) {
                    items.put(entId, ((Integer) old) + drop.getNum());
                } else {
                    items.put(entId, drop.getNum());
                }
            }
        }

        return items;
    }

    private Collection<DroppedEntity> dropItemPack(User user, int dropEntId, LogItemAct logItemAct) {
        if (dropEntId != 0) {
            Entity entity = entityService.getEntity(dropEntId);
            if (entity != null) {
                if (entity instanceof DropPackEntity
                        && ((DropPackEntity) entity).getChildType() != Item.ITEM_TYPE_BOX_DROP) {
                    //执行掉落包
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("user", user);
                    params.put("iAction", logItemAct);
                    Map<String, Object> dropItems = entityService.doAction(entity, Entity.ACTION_USE, params);

                    //添加掉落的道具到结果
                    if (dropItems != null && dropItems.size() > 0) {
                        Map<Integer, DroppedEntity> datas = (Map<Integer, DroppedEntity>) dropItems
                                .get("items");
                        if (datas != null && datas.size()>0) {
                            return datas.values();
                        }
                    }
                } else {
                    // 直接掉道具
                    treasuryService.addItemToTreasury(user.getUserId(), dropEntId,
                            1, 1, -1, false, true, logItemAct);
                    Collection<DroppedEntity> des = new LinkedList<DroppedEntity>();
                    des.add(new DroppedEntity(dropEntId, 1));
                }
            } else {
                throw new BaseException("掉落包不存在");
            }
        }
        return new LinkedList<DroppedEntity>();
    }

    /**
     * 验证是够可以挑战
     *
     * @param towerUser
     */
    private void validateCombat(TowerUser towerUser) {
        if (towerUser.getStageId() <= 0
                || towerUser.getStageId() > towers.size()) {
            throw new BaseException("已打完最高层 " + towers.size() + " 层，请重新开始");
        }
//		if (towerUser.getTopStageId() + 1 < towerUser.getStageId()) {
//			throw new BaseException("该层未开放，只能逐层往上打，当前层"
//					+ towerUser.getStageId() + "，打过的最高层为"
//					+ towerUser.getTopStageId());
//		}
        if (towerUser.getReliveTimes() < 1) {
            String mes = "您挑战第" + towerUser.getStageId()
                    + "层已经失败，复活次数已经用完。请重新开始";
            throw new BaseException(mes);
        }
        if (towerUser.getJoinStatus() != TowerUser.JOIN_STATUS_JOIN) {
            throw new BaseException("没有进入活动，不能开始战斗");
        }
    }

    /**
     * 根据当前进入次数获得需要消耗的道具个数
     *
     * @param curCount 当前进入次数
     * @return
     */
    private int getItemCountFactor(int curCount) {
        String itemC = commonService.getSysParaValue(
                AppConstants.SYS_TOWER_ITEM_FACTOR, "1");
        String[] arr = StringUtils.split(itemC, ",");
        if (curCount >= arr.length) {
            return Integer.parseInt(arr[arr.length - 1]);
        }
        return Integer.parseInt(arr[curCount]);
    }

    @Override
    public int getNextKeyStageBonusId(int stageId) {
        int keyStageSpan = commonService.getSysParaIntValue(
                AppConstants.SYS_TOWER_KEY_STAGE_SPAN,
                AppConstants.SYS_TOWER_KEY_STAGE_SPAN_DEFAULT_VALUE);
        int nextKeyStage = (int) (Math.floor(stageId / keyStageSpan) + 1) * keyStageSpan;
        Tower tower = towers.get(nextKeyStage);
        if (tower == null) {
            if (nextKeyStage < towers.size()) {
                throw new BaseException("关键关卡配置错误");
            } else {
                return 0;
            }
        }
        return tower.getFirstBonusId();
    }

    @Override
    public void validateUser(User user) {
        int opened = commonService.getSysParaIntValue(
                AppConstants.SYS_TOWER_OPEN_STATUS,
                AppConstants.SYS_OPNE_STATUS_DEFAULTVALUE);
        if (opened != 1) {
            throw new BaseException("此功能未开放");
        }
        int lvLimit = commonService.getSysParaIntValue(
                AppConstants.SYS_TOWER_USER_LEVEL_LIMIT,
                AppConstants.SYS_TOWER_USER_LEVEL_LIMIT_DEFVALUE);
        if (user.getUsrLv() < lvLimit) {
            throw new BaseException("您的指挥官等级必须大于" + lvLimit + "级才能参加此活动");
        }
    }

    @Override
    public int getReliveUpperLimit() {
        int reliveTime = commonService.getSysParaIntValue(
                AppConstants.SYS_TOWER_RELIVE_TIME,
                AppConstants.SYS_TOWER_RELIVE_TIME_DEFVALUE);
        return reliveTime;
    }

    private void lockTower(long userId){
        try {
            MemcachedManager.lock(TOWER_LOCK_PREFIX+userId);
		} catch (TimeoutException e) {
			throw new BaseException(e.toString());
		}
    }
}

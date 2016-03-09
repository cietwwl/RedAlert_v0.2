package com.youxigu.dynasty2.develop.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibatis.sqlmap.engine.cache.memcached.MemcachedManager;
import com.manu.util.UtilDate;
import com.youxigu.dynasty2.chat.ChatChannelManager;
import com.youxigu.dynasty2.chat.EventMessage;
import com.youxigu.dynasty2.chat.client.IChatClientService;
import com.youxigu.dynasty2.common.AppConstants;
import com.youxigu.dynasty2.common.service.ICommonService;
import com.youxigu.dynasty2.core.event.Event;
import com.youxigu.dynasty2.core.event.EventDispatcher;
import com.youxigu.dynasty2.core.event.EventTypeConstants;
import com.youxigu.dynasty2.core.event.IEventListener;
import com.youxigu.dynasty2.core.wolf.IWolfClientService;
import com.youxigu.dynasty2.develop.dao.ICastleBuilderDao;
import com.youxigu.dynasty2.develop.dao.ICastleDao;
import com.youxigu.dynasty2.develop.dao.ICastleResDao;
import com.youxigu.dynasty2.develop.domain.Castle;
import com.youxigu.dynasty2.develop.domain.CastleBuilder;
import com.youxigu.dynasty2.develop.domain.CastleBuilderMessage;
import com.youxigu.dynasty2.develop.domain.CastleBuilding;
import com.youxigu.dynasty2.develop.domain.CastleBuildingMessage;
import com.youxigu.dynasty2.develop.domain.CastleEffect;
import com.youxigu.dynasty2.develop.domain.CastleResource;
import com.youxigu.dynasty2.develop.domain.UserTechMessage;
import com.youxigu.dynasty2.develop.domain.UserTechnology;
import com.youxigu.dynasty2.develop.enumer.SpeedType;
import com.youxigu.dynasty2.develop.service.ICastleEffectService;
import com.youxigu.dynasty2.develop.service.ICastleResService;
import com.youxigu.dynasty2.develop.service.ICastleService;
import com.youxigu.dynasty2.develop.service.IFlushCastleService;
import com.youxigu.dynasty2.develop.service.ISpeedService;
import com.youxigu.dynasty2.entity.dao.IEntityDao;
import com.youxigu.dynasty2.entity.domain.Building;
import com.youxigu.dynasty2.entity.domain.Entity;
import com.youxigu.dynasty2.entity.domain.Item;
import com.youxigu.dynasty2.entity.domain.Resource;
import com.youxigu.dynasty2.entity.domain.Technology;
import com.youxigu.dynasty2.entity.domain.effect.EffectDefine;
import com.youxigu.dynasty2.entity.domain.effect.EffectTypeDefine;
import com.youxigu.dynasty2.entity.service.EntityConsumeResultSet;
import com.youxigu.dynasty2.entity.service.EntityLimitResultSet;
import com.youxigu.dynasty2.entity.service.IEntityService;
import com.youxigu.dynasty2.log.AbsLogLineBuffer;
import com.youxigu.dynasty2.log.ILogService;
import com.youxigu.dynasty2.log.LogBeanFactory;
import com.youxigu.dynasty2.log.LogTypeConst;
import com.youxigu.dynasty2.log.TlogHeadUtil;
import com.youxigu.dynasty2.log.imp.LogCashAct;
import com.youxigu.dynasty2.mission.domain.Mission;
import com.youxigu.dynasty2.mission.service.IMissionService;
import com.youxigu.dynasty2.tips.domain.BuffDefine;
import com.youxigu.dynasty2.tips.domain.BuffTip;
import com.youxigu.dynasty2.tips.service.IBuffTipService;
import com.youxigu.dynasty2.treasury.service.ITreasuryService;
import com.youxigu.dynasty2.user.domain.AchieveType;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.user.service.IUserAchieveService;
import com.youxigu.dynasty2.user.service.IUserService;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.dynasty2.util.EffectValue;
import com.youxigu.dynasty2.util.MathUtils;
import com.youxigu.dynasty2.vip.service.IVipService;
import com.youxigu.wolf.node.job.Job;

import edu.emory.mathcs.backport.java.util.Collections;

public class CastleService implements ICastleService, IEventListener {
	public final static int AUTO_BUILD = 1;
	public final static int NOT_AUTO_BUILD = 0;
    public static final int BUFF_USE_CASH = 1;
    public static final int BUFF_DONT_USE_CASH = 0;

	private static final String JOB_BUILDING_GROUP_NAME = "CASBUI";
	private static final String JOB_TECH_GROUP_NAME = "CASTECH";
    //todo: 临时使用，VIP系统开发后用VIP系统中的配置代替
    private static final int FREE_BUILD_TIME_SECONDS = 300;

    public static final Logger log = LoggerFactory
            .getLogger(CastleService.class);
    private CastleBuildingComparator compartor = new CastleBuildingComparator();

	private ICastleDao castleDao;
	private IEntityDao entityDao;
	private IEntityService entityService;
	private ICastleEffectService castleEffectService;
	private IWolfClientService jobServerClient;
    private ICommonService commonService;
    private ICastleResDao castleResDao;
    private ICastleBuilderDao castleBuilderDao;
    private IFlushCastleService flushCastleService;
    private IChatClientService messageService;
    private ILogService logService;
    private ILogService tlogService;
    private IUserService userService;
    private ITreasuryService treasuryService;
    private IVipService vipService;
    private ICastleResService castleResService;
    private IBuffTipService buffTipService;
    private ISpeedService speedService;
	private IMissionService missionService;
	private IUserAchieveService userAchieveService;

    public void setSpeedService(ISpeedService speedService) {
        this.speedService = speedService;
    }

    public void setBuffTipService(IBuffTipService buffTipService) {
        this.buffTipService = buffTipService;
    }

    public void setCastleResService(ICastleResService castleResService) {
        this.castleResService = castleResService;
    }

    public void setVipService(IVipService vipService) {
        this.vipService = vipService;
    }

    public void setTreasuryService(ITreasuryService treasuryService) {
        this.treasuryService = treasuryService;
    }

    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    public void setTlogService(ILogService tlogService) {
        this.tlogService = tlogService;
    }

    public void setLogService(ILogService logService) {
        this.logService = logService;
    }

    public void setMessageService(IChatClientService messageService) {
        this.messageService = messageService;
    }

    public void setFlushCastleService(IFlushCastleService flushCastleService) {
        this.flushCastleService = flushCastleService;
    }

    public void setCastleBuilderDao(ICastleBuilderDao castleBuilderDao) {
        this.castleBuilderDao = castleBuilderDao;
    }

    public void setCastleResDao(ICastleResDao castleResDao) {
        this.castleResDao = castleResDao;
    }

    public void setCommonService(ICommonService commonService) {
        this.commonService = commonService;
    }

	public void setJobServerClient(IWolfClientService jobServerClient) {
		this.jobServerClient = jobServerClient;
	}

	public void setCastleEffectService(ICastleEffectService castleEffectService) {
		this.castleEffectService = castleEffectService;
	}

	public void setEntityService(IEntityService entityService) {
		this.entityService = entityService;
	}

	public void setEntityDao(IEntityDao entityDao) {
		this.entityDao = entityDao;
	}

	public void setCastleDao(ICastleDao castleDao) {
		this.castleDao = castleDao;
	}

	public void setMissionService(IMissionService missionService) {
		this.missionService = missionService;
	}

	public void setUserAchieveService(IUserAchieveService userAchieveService) {
		this.userAchieveService = userAchieveService;
	}

	public void init() {
        EventDispatcher.registerListener(EventTypeConstants.EVT_USER_LEVEL_UP, this);
    }

	@Override
	public Castle lockAndGetCastle(long casId) {
		try {
            MemcachedManager.lockClass(Castle.class, casId);
            return this.getCastleById(casId);
        } catch (TimeoutException e) {
			throw new BaseException(e.toString());
		}
	}

	@Override
	public Castle lockMainCastle(Castle castle) {
		Castle mainCastle = null;
		try {
			mainCastle = (Castle) MemcachedManager.lockObject(castle,
					castle.getCasId());
		} catch (TimeoutException e) {
			throw new BaseException(e.toString());
		}
		return mainCastle;
	}
	
	@Override
	public void doUpdateCastle(Castle castle) {
		castleDao.updateCastle(castle);
	}

    @Override
	public Castle createCastle(long userId, int stateId) {
    	User user = userService.getUserById(userId);
        Castle castle = _createCastle(user, stateId);

        // 创建建筑队列
        long casId = castle.getCasId();

        //第一队列默认开启，第二和第三队列刚创建用户和城池时默认不开启
        int builder1OpenStatus = CastleBuilder.BUILDER_IDLE;
        int builder2OpenStatus = CastleBuilder.BUILDER_NOTOPEN;
        int builder3OpenStatus = CastleBuilder.BUILDER_NOTOPEN;

        createCastleBuilder(userId, casId, CastleBuilder.BUILDER_INDEX_FIRST, builder1OpenStatus);
        createCastleBuilder(userId, casId, CastleBuilder.BUILDER_INDEX_SECOND, builder2OpenStatus);
        createCastleBuilder(userId, casId, CastleBuilder.BUILDER_INDEX_THIRD, builder3OpenStatus);

        //创建初始需要创建的建筑，并初始化相关资源
        List<Entity> buildings = entityService.getEntityByEntTypes(Entity.TYPE_BUILDING);
        createCastleBuildingAndUpdateResource(castle, buildings, true);

        // TODO: 其它需要在创建城池的时候要做的初始化工作
        return castle;
    }

    private Castle _createCastle(User user, int stateId) {
        Timestamp now = UtilDate.nowTimestamp();

        Castle castle = new Castle();// 初始化城池
        castle.setUserId(user.getUserId());
        castle.setCasName(user.getUserName());
        castle.setStateId(stateId);
        castle.setCalcuDttm(now);
        castle.setQuarCalcuDttm(now);
        castle.setChangeCountryDttm(null);
        castle.setAutoBuild(NOT_AUTO_BUILD);

//		int initialPosX = Util.randInt(100);
//		int initialPosY = Util.randInt(100);
//
//		castle.setPosX(initialPosX);
//		castle.setPosY(initialPosY);

        castleDao.createCastle(castle);// 创建城池
        return castle;
    }

    private void createCastleBuilder(long userId, long casId, int builderIndex, int openStatus) {
        CastleBuilder casBuilder = new CastleBuilder();
        casBuilder.setUserId(userId);
        casBuilder.setCasId(casId);
        casBuilder.setStatus(openStatus);
        casBuilder.setBeginDttm(null);
        casBuilder.setEndDttm(null);
        casBuilder.setBuiId(0);
        casBuilder.setBuilderIndex(builderIndex);
        castleDao.createCastleBuilder(casBuilder);
    }

    @Override
    public List<CastleBuilding> createOpenedCastleBuildings(long casId){
        Castle castle = getCastleById(casId);
        if(castle == null){
            throw new BaseException("城池不存在："+String.valueOf(casId));
        }
        List<Entity> buildings = entityService.getEntityByEntTypes(Entity.TYPE_BUILDING);
        return createCastleBuildingAndUpdateResource(castle, buildings, true);
    }

    @Override
    public List<CastleBuilding> createCastleBuildingsWithoutLimit(long casId, List<Integer> buildingIds){
        if(buildingIds == null || buildingIds.size()==0){
            return new ArrayList<CastleBuilding>();
        }

        Castle castle = getCastleById(casId);
        if(castle == null){
            throw new BaseException("城池不存在："+String.valueOf(casId));
        }

        List<Entity> toBeCreated = new ArrayList<Entity>();
        for (int buildingId : buildingIds){
            Building building = (Building) entityService.getEntity(buildingId);
            toBeCreated.add(building);
        }

        return createCastleBuildingAndUpdateResource(castle, toBeCreated, false);
    }

    //根据指定的building列表创建建筑，并初始化效果。如果checkLimit为false，则不校验创建的Limit约束
    private List<CastleBuilding> createCastleBuildingAndUpdateResource(Castle castle, List<Entity>
            toBeCreatedBuildings, boolean checkLimit) {
        long casId = castle.getCasId();
        //如果是该用户创建新用户阶段，还没有创建CastleResource，先创建
        CastleResource castleRes = castleResService.getCastleResourceById(casId);
        if(castleRes == null){
            castleRes = new CastleResource();
            castleRes.setCasId(casId);
            castleRes.setLastBuyDttm(null);
            castleResDao.createCastleResource(castleRes);
        }
        castleRes = castleResService.lockCasRes(casId);

        List<CastleBuilding> createdCasBuildings = new ArrayList<CastleBuilding>();

        List<CastleBuilding> existCasBuildings = doGetCastleBuildingsByCasId(casId);
        //创建建筑，初始化建筑效果，并初始化资源建筑内的初始资源
        for (Entity entity : toBeCreatedBuildings) {
            Building building = (Building)entity;
            //如果需要检查Limit约束
            if(checkLimit) {
                //满足开放条件才创建，否则跳过
                Map<String, Object> para = new HashMap<String, Object>();
                para.put("casId", casId);
                para.put("userId", castle.getUserId());
                EntityLimitResultSet limit = entityService.checkLimit(
                        building.getLimitsByLevel(building.getInitialLevel()), para);
                if (limit != null && !limit.isMatch()) {
                    continue;
                }
            }

            //如果城池内已经有该建筑，则不再创建
            if(checkCastBuildingExist(existCasBuildings, building)){
                continue;
            }

            CastleBuilding casBui = new CastleBuilding();
            casBui.setCasId(castle.getCasId());
            casBui.setBuiEntId(building.getEntId());
            casBui.setBuilderIndex(CastleBuilder.BUILDER_INDEX_NONE);
            casBui.setBeginDttm(null);
            casBui.setEndDttm(null);
            casBui.setLevel(building.getInitialLevel());
            casBui.setAutoBuild(NOT_AUTO_BUILD);
            castleDao.createCastleBuilding(casBui);

            createdCasBuildings.add(casBui);

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("level", casBui.getLevel());
            params.put("casBuilding", casBui);
            params.put("mainCastle", castle);
            entityService.doAction(
                    entityService.getEntity(casBui.getBuiEntId()),
                    Entity.ACTION_LEVEL_UP, params);

            if (building.getEntId() == AppConstants.ENT_BUILDING_GOLD) {
                castleRes.setCasGoldNum(commonService.getSysParaIntValue(AppConstants.INIT_GOLD_IN_BUILDING, 0));
            } else if (building.getEntId() == AppConstants.ENT_BUILDING_OIL) {
                castleRes.setCasOilNum(commonService.getSysParaIntValue(AppConstants.INIT_OIL_IN_BUILDING, 0));
            } else if (building.getEntId() == AppConstants.ENT_BUILDING_URANIUM) {
                castleRes.setCasUranium(commonService.getSysParaIntValue(AppConstants.INIT_URANIUM_IN_BUILDING, 0));
            } else if (building.getEntId() == AppConstants.ENT_BUILDING_IRON) {
                castleRes.setCasIronNum(commonService.getSysParaIntValue(AppConstants.INIT_IRON_IN_BUILDING, 0));
            }
        }
        castleResService.updateCastleResource(castleRes);

        return createdCasBuildings;
    }

    private boolean checkCastBuildingExist(List<CastleBuilding> existCasBuildings, Building building) {
        for(CastleBuilding cb : existCasBuildings){
            if(cb.getBuiEntId() == building.getEntId()){
                return true;
            }
        }
        return false;
    }

    @Override
	public Castle getCastleByUserId(long userId) {
		return castleDao.getCastleByUserId(userId);
	}

	@Override
	public Castle getCastleById(long casId) {
		return castleDao.getCastleById(casId);
	}

	@Override
	public List<CastleBuilding> doGetCastleBuildingsByCasId(long casId) {
        List<CastleBuilding> cbs = castleDao.getCastBuildingsByCasId(casId);
        //如果已过建筑升级结束时间但仍未正确结束，此处补充处理
        long now = System.currentTimeMillis();
        for (CastleBuilding cb : cbs) {
            Timestamp endDttm = cb.getEndDttm();
            if (endDttm != null && endDttm.getTime() < now) {
                //结束升级job未正确执行，处理。
                doEndUpgradeCastleBuilding(casId, cb.getCasBuiId(), cb.getLevel());
            }
        }
        return cbs;
    }

	@Override
	public int validateBuildingUpgradeCondition(long casId, long castleBuildingId) {
        CastleBuilding castleBuilding = getCastBuildingById(casId, castleBuildingId);
        if(castleBuilding == null){
            throw new BaseException("建筑不存在");
        }
		Entity entity = entityService.getEntity(castleBuilding.getBuiEntId());
		if (!(entity instanceof Building)) {
			throw new BaseException("建筑数据错误");
		}

		Building building = (Building) entity;

		// 检查限制
		int currLv = castleBuilding.getLevel();
		if (currLv >= building.getMaxLevel()) {
			return AppConstants.UPGRADE_UNSATISFIED;
		}
		int nextLv = currLv + 1;
		EntityLimitResultSet limit = entityService.checkLimit(
				building.getLimitsByLevel(nextLv), casId);
		if (limit != null && !limit.isMatch()) {
            return AppConstants.UPGRADE_UNSATISFIED;
		}

		EntityConsumeResultSet checkResult = entityService
				.getEntityConsumeCheckResult(
						building.getConsumesByLevel(nextLv), casId);
		if (!checkResult.isMatch()) {
            return AppConstants.UPGRADE_UNSATISFIED;
		}
        return AppConstants.UPGRADE_SATISFIED;
	}

    @Override
	public int getBuildingNextLevelUpgradeTime(long casId, long castleBuildingId) {
        CastleBuilding castleBuilding = getCastBuildingById(casId, castleBuildingId);
        if(castleBuilding == null){
            throw new BaseException("建筑不存在");
        }
        Entity entity = entityService.getEntity(castleBuilding.getBuiEntId());
		if (!(entity instanceof Building)) {
			throw new BaseException("建筑数据错误");
		}

		Building building = (Building) entity;
		// 检查限制
		int currLv = castleBuilding.getLevel();
		if (currLv >= building.getMaxLevel()) {
			// 不能再升级，返回0，表示值无效
			return 0;
		}

		int nextLv = currLv + 1;
		EntityConsumeResultSet checkResult = entityService
				.getEntityConsumeCheckResult(
						building.getConsumesByLevel(nextLv), casId);

		int time = castleEffectService.getActualTime(casId,
				checkResult.getTime(),
				EffectTypeDefine.B_BUILD_QUEUE_SPEED);
		if (time <= AppConstants.MIN_BUILD_SECONDS) {
			time = AppConstants.MIN_BUILD_SECONDS;
		}

		return time;
	}

    @Override
    public CastleBuilding doCancelBuilding(Castle castle, long castleBuildingId) {
        long casId = castle.getCasId();
        CastleBuilding casBui = lockAndGetCastleBuildingById(casId, castleBuildingId);
        if (casBui == null || casBui.getCasId() != casId) {
            throw new BaseException("该建筑不属于您的基地。");
        } else if (casBui.getBeginDttm() == null) {
            throw new BaseException("建筑不在升级中。");
        }

        casBui.setBeginDttm(null);
        casBui.setEndDttm(null);
        casBui.setBuilderIndex(CastleBuilder.BUILDER_INDEX_NONE);
        castleDao.updateCastleBuilding(casBui);

        Building building = (Building) entityService.getEntity(casBui.getBuiEntId());

        // 返还1/2资源
        entityService.updateByEntityConsume(building.getConsumesByLevel(casBui
                .getLevel() + 1), casId, -1, 2);

        //删除原先的完成建筑升级的Job
        deleteBuildingJob(casId, castleBuildingId);

        // 修改建筑队列
        List<CastleBuilder> casBuilders = lockAndGetCastleBuilders(castle.getUserId());
        for (CastleBuilder casBuilder : casBuilders) {
            if (casBuilder.getBuiId() == castleBuildingId
                    && casBuilder.getCasId() == casId) {
                casBuilder.setStatus(CastleBuilder.BUILDER_IDLE);
                casBuilder.setBeginDttm(null);
                casBuilder.setEndDttm(null);
                casBuilder.setBuiId(0);
                castleBuilderDao.updateCastleBuilder(casBuilder);
            }
        }

        // 取消自动建造并通知前端
        castle = this.doCancelAutoBuild(casId);
        flushCastleService.sendFlushCasEffEvent(castle.getUserId(), "autoBuild", castle.getAutoBuild());
        if (log.isDebugEnabled()) {
            log.debug("取消建筑升级，casId={},casBuiId={}", casId, castleBuildingId);
        }

        return casBui;
    }

    private void deleteBuildingJob(long casId, long casBuiId) {
        StringBuilder sb = new StringBuilder();
        sb.append(casId).append("_").append(casBuiId);
        jobServerClient.deleteJob(JOB_BUILDING_GROUP_NAME, sb.toString());
    }

    //region 升级建筑

    @Override
	public CastleBuilding doUpgradeCastleBuilding(Castle castle,
                                                  int builderIndex, long castleBuildingId, boolean isAuto) {
        CastleBuilding casBui = lockAndGetCastleBuildingById(castle.getCasId(), castleBuildingId);
		if (casBui == null || casBui.getCasId() != castle.getCasId()) {
			throw new BaseException("建筑不存在。");
		}

		Entity entity = entityService.getEntity(casBui.getBuiEntId());

		return upgradeCastleBuilding(builderIndex, castle, casBui,
				(Building) entity, isAuto);
	}

    private CastleBuilding upgradeCastleBuilding(int builderIndex,
                                                 Castle castle, CastleBuilding casBuilding, Building building,
                                                 boolean isAuto) {
        if (casBuilding.getLevel() >= building.getMaxLevel()) {
            throw new BaseException("达到升级上限，无法升级。");
        }
        if (casBuilding.getBeginDttm() != null) {
            throw new BaseException("建筑正在升级中。");
        }

        // 检查限制
        int nextLv = casBuilding.getLevel() + 1;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("castle", castle);
        EntityLimitResultSet limit = entityService.checkLimit(
                building.getLimitsByLevel(nextLv), params);
        if (limit != null && !limit.isMatch()) {
            throw new BaseException(limit.getExceptionString());
        }

        return _upgradeCastleBuilding(builderIndex, castle, casBuilding, building, isAuto);
    }

    private CastleBuilding _upgradeCastleBuilding(int builderIndex, Castle castle,
                                                  CastleBuilding casBuilding, Building building,
                                                  boolean isAuto) {
        int nextLv = casBuilding.getLevel() + 1;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("castle", castle);
        EntityConsumeResultSet consume = entityService.updateByEntityConsume(
                building.getConsumesByLevel(nextLv), params);

        //计算经过Buff加成后的升级时间
        int buildTimeInSeconds = consume.getTime();
        buildTimeInSeconds = getLengthOfTimeWithBuffEffect(castle,
                buildTimeInSeconds);
        buildTimeInSeconds = buildTimeInSeconds > AppConstants.MIN_BUILD_SECONDS ? buildTimeInSeconds
                : AppConstants.MIN_BUILD_SECONDS;

        //更新建筑队列
        long now = System.currentTimeMillis();
        Timestamp beginTime = new Timestamp(now);
        Timestamp endTime = new Timestamp(now + buildTimeInSeconds * 1000);

        long userId = castle.getUserId();
        CastleBuilder builder = lockAndGetCastleBuilderByIndex(userId, builderIndex);
        if(builder == null){
            throw new BaseException("指定建筑队列不存在。");
        }
        if(builder.getStatus()!=CastleBuilder.BUILDER_IDLE){
            throw new BaseException("指定建筑队列当前无法使用。");
        }

        builder.setBeginDttm(beginTime);
        builder.setEndDttm(endTime);
        builder.setStatus(CastleBuilder.BUILDER_WORKING);
        builder.setBuiId(casBuilding.getCasBuiId());
        castleDao.updateCastleBuilder(builder);

        //更新建筑的建造时间和占用的建筑队列
        casBuilding = lockAndGetCastleBuildingById(casBuilding.getCasId(), casBuilding.getCasBuiId());
        casBuilding.setBeginDttm(beginTime);
        casBuilding.setEndDttm(endTime);
        casBuilding.setBuilderIndex(builderIndex);
        castleDao.updateCastleBuilding(casBuilding);

        // 启动到时间后完成升级的job
        startEndUpgradeBuildingJob(castle.getCasId(), casBuilding);

        if (isAuto) {// 推消息通知前台建筑队列状态及正在建造的建筑
            //推送建筑队列状态变化消息
            List<CastleBuilder> builders = getCastleBuilders(userId);
            messageService.sendEventMessage(userId, EventMessage.TYPE_CASTLE_BUILDER_CHANGED,
                    new CastleBuilderMessage(builders));
            //推送正在升级的建筑的信息。此时是否满足升级状态置为SATISFIED
            messageService.sendEventMessage(userId, EventMessage.TYPE_CASTLE_BUILDING_CHANGED,
                    new CastleBuildingMessage(casBuilding, buildTimeInSeconds,
                            AppConstants.UPGRADE_SATISFIED));
        }

        return casBuilding;
    }

    private void startEndUpgradeBuildingJob(long casId, CastleBuilding casBuilding) {
        Job job = new Job();
        job.setJobGroupName(JOB_BUILDING_GROUP_NAME);
        StringBuilder sb = new StringBuilder();
        sb.append(casId).append("_")
                .append(casBuilding.getCasBuiId());
        job.setJobIdInGroup(sb.toString());
        job.setJobExcuteTime(casBuilding.getEndDttm().getTime());
        job.setJobParams(new Object[] { casId,
                casBuilding.getCasBuiId(), casBuilding.getLevel() });
        job.setJobType(Job.JOB_DB);
        job.setServiceName("castleService");
        job.setMethodName("doEndUpgradeCastleBuilding");

        // 异步启动
        jobServerClient.startJob(job);
    }

    //endregion

    @Override
    public CastleBuilding doUpgradeCastleBuildingWithoutLimit(long casId, int builderIndex,
                                                              long castleBuildingId){
        Castle castle = getCastleById(casId);
        if(castle == null){
            throw new BaseException("城池Id错误。");
        }
        CastleBuilding casBui = lockAndGetCastleBuildingById(casId, castleBuildingId);
		if (casBui == null || casBui.getCasId() != casId) {
			throw new BaseException("建筑不存在。");
		}

		Building building = (Building) entityService.getEntity(casBui.getBuiEntId());

        CastleBuilding castleBuilding = lockAndGetCastleBuildingById(casId, castleBuildingId);

        if (castleBuilding.getLevel() >= building.getMaxLevel()) {
            throw new BaseException("达到升级上限，无法升级。");
        }
        if (castleBuilding.getBeginDttm() != null) {
            throw new BaseException("建筑正在升级中。");
        }

        return _upgradeCastleBuilding(builderIndex, castle, castleBuilding, building, false);
    }

    @Override
    public void doEndUpgradeCastleBuilding(long casId, int casBuiId, int level) {
        if (log.isDebugEnabled()) {
            log.debug("升级建筑job开始执行：casId:{},buiId:{}", casId, casBuiId);
        }
        Castle castle = this.getCastleById(casId);

        CastleBuilding casBuilding = this.lockAndGetCastleBuildingById(casId, casBuiId);
        if (casBuilding == null) {
            if (log.isWarnEnabled()) {
                log.warn("升级的建筑不存在:casId={},casBuiId={}", casId, casBuiId);
            }
            return;
        }
        if (casBuilding.getLevel() != level) {
            if (log.isWarnEnabled()) {
                log.warn("升级的建筑等级错误:casId={},casBuiId={}", casId, casBuiId);
            }
            return;
        }
        if (casBuilding.getBeginDttm() == null && casBuilding.getLevel() > 0) {
            if (log.isWarnEnabled()) {
                log.warn("升级的建筑不在升级状态:casId={},casBuiId={}", casId, casBuiId);
            }
            return;
        }

        _doEndUpgradeCastleBuilding(castle, casBuilding);

        // 推消息建筑列表及建筑状态
//        Map<String, Object> params = this.convertCasBui2Map(casBuilding);// 在建建筑信息
//        this.sendFlushCastleBuilderEvent(mainCastle.getUserId(), params);

        this._autoUpgradeCastleBuildings(castle, true);
    }

    private void _doEndUpgradeCastleBuilding(Castle castle, CastleBuilding casBuilding) {
        long userId = castle.getUserId();
        CastleBuilder casBuilder = lockAndGetCastleBuilderByIndex(userId, casBuilding.getBuilderIndex());
        casBuilder.setBeginDttm(null);
        casBuilder.setEndDttm(null);
        casBuilder.setStatus(CastleBuilder.BUILDER_IDLE);
        casBuilder.setBuiId(0);
        castleBuilderDao.updateCastleBuilder(casBuilder);

        Building building = (Building) entityService.getEntity(casBuilding
                .getBuiEntId());
        casBuilding = lockAndGetCastleBuildingById(casBuilding.getCasId(), casBuilding.getCasBuiId());
        int level = casBuilding.getLevel();
        int newLv = level + 1;
        casBuilding.setLevel(newLv);
        casBuilding.setBeginDttm(null);
        casBuilding.setEndDttm(null);
        casBuilding.setBuilderIndex(CastleBuilder.BUILDER_INDEX_NONE);
        castleDao.updateCastleBuilding(casBuilding);

        // 升级当前建筑效果
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("level", newLv);
        params.put("mainCastle", castle);
        entityService.doAction(building, Entity.ACTION_LEVEL_UP, params);

        User user = userService.lockGetUser(userId);

		// 升级建筑 “建筑ID”到N级: 建筑entId/当前等级
		missionService.notifyMissionModule(user, Mission.QCT_TYPE_LEVEL,
				casBuilding.getBuiEntId(), casBuilding.getLevel());

		// 成就
		userAchieveService.doNotifyAchieveModule(castle.getUserId(),
				AchieveType.TYPE_BUILDING, casBuilding.getBuiEntId(),
				casBuilding.getLevel());

        //推送建筑队列状态变化消息
        List<CastleBuilder> builders = getCastleBuilders(userId);
        messageService.sendEventMessage(userId, EventMessage.TYPE_CASTLE_BUILDER_CHANGED,
                new CastleBuilderMessage(builders));

        //推送刚升级完的建筑的信息。
        long casId = castle.getCasId();
        long castleBuildingId = casBuilding.getCasBuiId();
        int upgradeTime = getBuildingNextLevelUpgradeTime(casId, castleBuildingId);
        int upgradeSatisfied = validateBuildingUpgradeCondition(casId, castleBuildingId);
        messageService.sendEventMessage(userId, EventMessage.TYPE_CASTLE_BUILDING_CHANGED,
                new CastleBuildingMessage(casBuilding, upgradeTime, upgradeSatisfied));

        // 发升级建筑消息
        StringBuilder message = new StringBuilder();
        message.append(building.getBuiName()).append("达到")
                .append(casBuilding.getLevel()).append("级。");

        messageService.sendMessage(0, userId,
                ChatChannelManager.CHANNEL_SYSTEM, null, message.toString());

        // tlog日志
        this.tlog(LogTypeConst.TLOG_TYPE_BuildFlow, user, casBuilding
                .getBuiEntId(), level, newLv);

        logService.log(LogBeanFactory.buildLvBuildLog(user, casBuilding
                .getBuiEntId(), building.getBuiName(), level, newLv));
    }

    //region 自动建造

    @Override
    public List<CastleBuilding> doAutoUpgradeCastleBuilding(long casId, List<Long> casBuiIds) {
        final Castle castle = this.lockAndGetCastle(casId);

        //todo: 做城墙功能时根据需求修改
//        if (castle.getStatus() == Castle.STATUS_OVER) {
//            throw new BaseException("城池已经流亡。");
//        }

        // 将所有原先为自动的建筑取消自动
        List<CastleBuilding> casBuiList = lockAndGetCastleBuildingsByCasId(casId);
        for (CastleBuilding cb : casBuiList) {
            if (cb.getAutoBuild() == AUTO_BUILD) {
                cb.setAutoBuild(NOT_AUTO_BUILD);
                castleDao.updateCastleBuilding(cb);
            }
        }

        // 如果casBuiIds等于null，说明是自动建造过程中对后续建造的调用，跳过本段
        // 如果等于casBuiIds的size为0，则说明是客户端要取消自动建造。此时修改自动建造开关，并返回空集合
        if(casBuiIds != null) {
            if (casBuiIds.size() == 0) {
                if (castle.getAutoBuild() != NOT_AUTO_BUILD) {
                    castle.setAutoBuild(NOT_AUTO_BUILD);
                    castleDao.updateCastle(castle);
                }
                return new ArrayList<CastleBuilding>();
            }

            //修改城池的autobuild状态
            if (castle.getAutoBuild() != AUTO_BUILD) {
                castle.setAutoBuild(AUTO_BUILD);
                castleDao.updateCastle(castle);
            }

            for (Object tmp : casBuiIds) {
                int casBuiId = MathUtils.getInt(tmp);
                CastleBuilding casBui = lockAndGetCastleBuildingById(casId, casBuiId);

                if (casBui == null || casBui.getCasId() != casId) {
                    continue;
                }

                if (casBui.getAutoBuild() != AUTO_BUILD) {
                    casBui.setAutoBuild(AUTO_BUILD);
                    castleDao.updateCastleBuilding(casBui);
                }
            }
        }

		// 执行自动建造，累计值
		missionService.notifyMissionModule(
				userService.getUserById(castle.getUserId()),
				Mission.QCT_TYPE_MOREBUILD, 0, 1);

        return _autoUpgradeCastleBuildings(castle, false);
    }

    private List<CastleBuilding> _autoUpgradeCastleBuildings(Castle castle, boolean notThrowException) {
        // 判断当前是否处于自动建造状态
        if (castle.getAutoBuild() != AUTO_BUILD) {
            return new ArrayList<CastleBuilding>();
        }

        User user = userService.getUserById(castle.getUserId());

        // 取空闲建筑队列
        List<CastleBuilder> builders = lockAndGetCastleBuilders(castle.getUserId());
        List<CastleBuilder> idleBuilders = new ArrayList<CastleBuilder>();
        for (CastleBuilder cb : builders) {
            if (cb.getStatus() == CastleBuilder.BUILDER_IDLE) {
                idleBuilders.add(cb);
            }
        }

        // 没有空闲的建筑队列
        if (idleBuilders.size() == 0) {
            return new ArrayList<CastleBuilding>();
        }

        List<CastleBuilding> castleBuildings = new ArrayList<CastleBuilding>();
        for (CastleBuilder cbr : idleBuilders) {
            try {
                CastleBuilding cb = pickAndUpgradeCastleBuilding(castle, cbr.getBuilderIndex());
                if (cb != null) {
                    castleBuildings.add(cb);
                }
            } catch (BaseException e) {
                if (!notThrowException) {
                    throw e;
                }
            }
        }

        //条件不足，没有任何建筑开始新的自动建造，则停止自动建造
        if(castleBuildings.size() == 0){
            doCancelAutoBuild(castle.getCasId());
            if(notThrowException){
                flushCastleService.sendFlushCasEffEvent(castle.getUserId(),
                        "autoBuild", castle.getAutoBuild());
            }
            else{
                throw new BaseException("条件不足，无法继续自动建造。请检查建筑所需的资源或者先决条件。");
            }
        }
        return castleBuildings;
    }

    /**
     * 从自动建造的建筑列表里取出一个优先级最高的建筑，并开始升级。
     * @param castle
     * @param idleBuilderIndex
     * @return 开始自动建造的建筑。如果所有建筑都不满足条件，则返回null。
     */
    private CastleBuilding pickAndUpgradeCastleBuilding(Castle castle, int idleBuilderIndex) {
        CastleBuilding mostSuitableBuilding = getEligibleCastleBuilding(castle);
        if(mostSuitableBuilding == null){
            return null;
        }
        return doUpgradeCastleBuilding(castle, idleBuilderIndex, mostSuitableBuilding.getCasBuiId(), true);
    }

    /**
     * 获取自动建造队列中最符合条件的一个建筑，没有则返回null
     */
    private CastleBuilding getEligibleCastleBuilding(Castle castle) {
        List<CastleBuilding> all = doGetCastleBuildingsByCasId(castle.getCasId());
        Collections.sort(all, compartor);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("castle", castle);

        CastleBuilding bestBui = null;
        for (CastleBuilding cb : all) {
            Building building = (Building) entityService.getEntity(cb.getBuiEntId());
            if (cb.getLevel() >= building.getMaxLevel()) {
                continue;
            }

            if(cb.getAutoBuild() != AUTO_BUILD){
                continue;
            }

            int nextLv = cb.getLevel() + 1;
            EntityLimitResultSet limit = entityService.checkLimit(building
                    .getLimitsByLevel(nextLv), params);
            if (limit != null && !limit.isMatch()) {
                continue;
            }

            EntityConsumeResultSet consume = entityService.getEntityConsumeCheckResult(
                    building.getConsumesByLevel(nextLv), cb.getCasId());
            if (consume != null && !consume.isMatch()) {
                continue;
            }

            bestBui = cb;
            break;
        }

        return bestBui;
    }

    @Override
    public Castle doCancelAutoBuild(long casId) {
        Castle castle = this.lockAndGetCastle(casId);// 加锁
        if (castle.getAutoBuild() == AUTO_BUILD) {// 自动
            castle.setAutoBuild(NOT_AUTO_BUILD);// 自动建造
            castleDao.updateCastle(castle);
        }
        return castle;
    }

    //endregion

    private void tlog(String logType, User user, int entId, int preLv,
                      int nextLv) {
        // <entry name="ibuildId" type="int" desc="建筑id" />
        // <entry name="ipreLevel" type="int" desc="升级前等级" />
        // <entry name="inextLevel" type="int" desc="升级后等级" />
        AbsLogLineBuffer buffer = TlogHeadUtil.getTlogHead(logType, user);
        tlogService.log(buffer.append(entId).append(preLv).append(nextLv));
    }

    private CastleBuilder lockAndGetCastleBuilderByIndex(long userId, int builderIndex) {
        List<CastleBuilder> cbs = lockAndGetCastleBuilders(userId);
        for (CastleBuilder cb : cbs) {
            if (cb.getBuilderIndex() == builderIndex) {
                return cb;
            }
        }
        if (log.isErrorEnabled()) {
            log.error("建筑队列获取错误：userId={}, builderIndex={}。", userId, builderIndex);
        }
        throw new BaseException("建筑队列错误。");
    }

    private int getLengthOfTimeWithBuffEffect(Castle castle,
			int buildTimeInSeconds) {
		// todo: 添加Buff效果影响
		// EffectValue value = this.getBuilderSpeedEffect(castle.getCasId());
		// double percent = 1.0d;
		// int abs = 0;
		// if (value != null) {
		// percent = percent + value.getDoublePerValue();
		// abs = value.getAbsValue();
		// }
		return buildTimeInSeconds;
	}

	public EffectValue getBuilderSpeedEffect(long casId) {
		EffectValue value = castleEffectService
				.getSumCastleEffectValueByEffectType(casId,
						EffectTypeDefine.B_BUILD_QUEUE_SPEED);
		return value;
	}

	@Override
	public CastleBuilding getCastBuildingById(long casId, long castleBuildingId) {
		return castleDao.getCastBuildingById(casId, castleBuildingId);
	}

    @Override
    public CastleBuilding lockAndGetCastleBuildingById(long casId, long castleBuildingId) {
        try {
            MemcachedManager.lockClass(CastleBuilding.class, casId);
            return this.getCastBuildingById(casId, castleBuildingId);
        } catch (TimeoutException e) {
            throw new BaseException(e.toString());
        }
    }

	@Override
	public CastleBuilding getMaxLevelCastBuildingByEntId(long casId,
			int entId) {
		List<CastleBuilding> buildings = castleDao
				.getCastBuildingsByCasId(casId);

		CastleBuilding max = null;
		for (CastleBuilding building : buildings) {
			if (building.getBuiEntId() == entId) {
				if (max == null) {
					max = building;
				} else if (building.getLevel() > max.getLevel()) {
					max = building;
				}
			}
		}
		return max;
	}

    @Override
    public List<CastleBuilding> lockAndGetCastleBuildingsByCasId(long casId) {
        try {
            MemcachedManager.lockClass(CastleBuilding.class, casId);
            return doGetCastleBuildingsByCasId(casId);
        } catch (TimeoutException e) {
            throw new BaseException(e.toString());
        }
    }

    @Override
    public List<CastleBuilder> lockAndGetCastleBuilders(long userId){
        try {
            MemcachedManager.lockClass(CastleBuilder.class, userId);
            return this.getCastleBuilders(userId);
        } catch (TimeoutException e) {
            throw new BaseException(e.toString());
        }
    }

	@Override
	public List<CastleBuilder> getCastleBuilders(long userId) {
		return castleDao.getCastleBuilders(userId);
	}

    @Override
    public void doEvent(Event event) {
        if(event.getEventType() == EventTypeConstants.EVT_USER_LEVEL_UP){
			Map<String, Object> params = (Map<String, Object>) event
					.getParams();
			User user = (User) params.get("user");
			createOpenedCastleBuildings(user.getMainCastleId());
        }
    }

    static class CastleBuildingComparator implements Comparator<CastleBuilding> {
        @Override
        public int compare(CastleBuilding o1, CastleBuilding o2) {
            int lv = o1.getLevel() - o2.getLevel();
            if (lv == 0) {
                long casId = o1.getCasId() - o2.getCasId();
                if (casId == 0) {
                    return o1.getCasBuiId() - o2.getCasBuiId();
                } else {
                    return casId > 0 ? 1 : -1;
                }
            } else {
                return lv;
            }
        }
    }

    //region 加速建造
    @Override
    public CastleBuilding doSpeedupUpgradeCastleBuilding(long userId, long casId, long casBuiId,
                                                         int speedUpType,
                                                         int entId, int num) {
        Castle castle = lockAndGetCastle(casId);
        if (castle.getStatus() == Castle.STATUS_OVER) {
            throw new BaseException("城池已经流亡。");
        }

        CastleBuilding casBui = lockAndGetCastleBuildingById(casId, casBuiId);
        if (casBui == null || casBui.getCasId() != casId) {
            throw new BaseException("建筑不存在。");
        } else if (casBui.getBeginDttm() == null) {
            throw new BaseException("建筑不在升级中。");
        }

        List<CastleBuilder> casBuilders = lockAndGetCastleBuilders(userId);

        if(casBui.getEndDttm() == null){
            throw new BaseException("建筑升级结束时间错误。");
        }

        int remainTimeInSeconds = (int)UtilDate.secondDistance(casBui.getEndDttm());


        if (remainTimeInSeconds <= FREE_BUILD_TIME_SECONDS) {
            _doEndUpgradeCastleBuilding(castle, casBui);
        } else {
            //是否使用元宝
            boolean useCash = speedUpType == AppConstants.BUILD_SPEED_UP_CASH;
            //扣减元宝或道具，返回扣减的元宝或道具对应的可以减少的时间，单位秒
            int consumedSeconds = speedService.doSpeedUp(userId, SpeedType.BUILDING_SPEED_UP,
                    remainTimeInSeconds, useCash, entId, num);

            //扣减量足够，就完成升级；不够，则只是更新升级剩余时间。
            if (consumedSeconds >= remainTimeInSeconds) {
                _doEndUpgradeCastleBuilding(castle, casBui);
            } else {
                Timestamp newEndTime = UtilDate.moveSecond(casBui.getEndDttm(), -1 * consumedSeconds);
                doUpdateBuildingEndTime(castle, casBui, newEndTime);

                //更新结束升级的任务
                deleteBuildingJob(casId, casBuiId);
                startEndUpgradeBuildingJob(casId, casBui);

                return casBui;
            }
        }

        // 删除建筑JOB
        deleteBuildingJob(casId, casBuiId);

        // 如果是自动建造，继续自动建造
        if (castle.getAutoBuild() == AUTO_BUILD) {
            List<CastleBuilding> cbs = this.doAutoUpgradeCastleBuilding(castle.getCasId(), null);
            //如果新加入建造队列的建筑是当前刚刚加速完成的建筑，返回该建筑状态
            for (CastleBuilding cb : cbs) {
                if (cb.getCasBuiId() == casBui.getCasBuiId()
                        && cb.getCasId() == casBui.getCasBuiId()) {
                    casBui = cb;
                }
            }
        }
        return casBui;
    }

    @Override
    public void doCheckBuilderOpenStatus(long userId){
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new BaseException("用户不存在。");
        }

        List<CastleBuilder> allBuilders = lockAndGetCastleBuilders(userId);
        List<CastleBuilder> notOpenedBuilders = new ArrayList<CastleBuilder>();
        for (CastleBuilder cb : allBuilders) {
            if (cb.getStatus() == CastleBuilder.BUILDER_NOTOPEN) {
                notOpenedBuilders.add(cb);
            }
        }

        //如果所有建筑队列都已开放，不再需要检查，返回
        if (notOpenedBuilders.size() == 0) {
            return;
        }

        for (CastleBuilder builder : notOpenedBuilders) {
            //第一队列默认开启，不检查，只检查第二、第三队列
            if (builder.getBuilderIndex() == CastleBuilder.BUILDER_INDEX_SECOND) {
                //第二队列根据用户等级开启
                int openUserLv = commonService.getSysParaIntValue(
                        AppConstants.SECOND_BUILDER_OPEN_USER_LEVEL, 10);
                if (user.getUsrLv() >= openUserLv) {
                    builder.setStatus(CastleBuilder.BUILDER_IDLE);
                    castleDao.updateCastleBuilder(builder);
                }
            } else if (builder.getBuilderIndex() == CastleBuilder.BUILDER_INDEX_THIRD) {
                //第三队列根据VIP等级开启
                //todo: vip功能开启后删除此处注释
//                int openVipLv = commonService.getSysParaIntValue(
//                        AppConstants.THIRD_BUILDER_OPEN_VIP_LEVEL, 6);
//                int vipLv = vipService.getUserVip(user.getUserId()).getVipLv();
//                if (vipLv >= openVipLv) {
//                    builder.setStatus(CastleBuilder.BUILDER_IDLE);
//                    castleDao.updateCastleBuilder(builder);
//                }
            }
        }
    }

    private void doUpdateBuildingEndTime(Castle castle, CastleBuilding casBuilding, Timestamp newEndTime){
        long userId = castle.getUserId();
        CastleBuilder casBuilder = lockAndGetCastleBuilderByIndex(userId, casBuilding.getBuilderIndex());
        casBuilder.setEndDttm(newEndTime);
        castleBuilderDao.updateCastleBuilder(casBuilder);

        casBuilding = lockAndGetCastleBuildingById(casBuilding.getCasId(), casBuilding.getCasBuiId());
        casBuilding.setEndDttm(newEndTime);
        castleDao.updateCastleBuilding(casBuilding);
    }
    //endregion

    @Override
    public void doActivateBuff(long casId, int buffId, int useCash, String pfEx) {
        Castle castle = getCastleById(casId);
        long userId = castle.getUserId();

        BuffDefine buffDefine = buffTipService.getBuffDefine(buffId);
        if (buffDefine == null || buffDefine.getItemId() <= 0) {
            throw new BaseException("此Buff不能通过道具激活。");
        }
        int entId = buffDefine.getItemId();

        //扣减道具或元宝
        int buffItemCount = treasuryService.getTreasuryCountByEntId(userId, entId);
        if(buffItemCount <= 0){
            if(useCash == BUFF_USE_CASH){
                //todo: 商城功能做完后完善此处逻辑
                int cashNum = 50;
                userService.doConsumeCash(userId, 1 * cashNum, LogCashAct.ACTIVATE_BUFF_ACTION);
                Entity entity = entityService.getEntity(entId);
                if (entity == null) {
                    throw new BaseException("道具数据错误。");
                }
                Map<String, Object> params = new HashMap<String, Object>();
                User user = userService.getUserById(userId);
                if (user == null) {
                    throw new BaseException("用户ID错误。");
                }
                params.put("user", user);
                entityService.doAction(entity, Entity.ACTION_ACTIVATE_BUFF, params);
            }
            else if(useCash == BUFF_DONT_USE_CASH){
                throw new BaseException("道具不足，无法激活Buff。");
            }
            else{
                throw new BaseException("使用元宝参数错误。");
            }
        }
        else {
            treasuryService.doUseItem(userId, entId, 1, pfEx);
        }
    }

    @Override
    public void doCancelBuff(long userId, long casId, int buffId) {
        BuffDefine buffDefine = buffTipService.getBuffDefine(buffId);
        if (buffDefine == null || buffDefine.getItemId() <= 0) {
            throw new BaseException("此Buff不能通过道具激活。");
        }
         List<BuffTip> buffTips = buffTipService.getBuffTipsByUserId(userId);
        if(buffTips != null && buffTips.size()>0){
            for(BuffTip buffTip: buffTips){
                if(buffTip.getBuffName().equals(buffDefine.getBuffName())){
                    buffTipService.deleteBuffTipByBuffName(userId, buffTip.getBuffName());
                    break;
                }
            }
        }
        int entId = buffDefine.getItemId();

        Item item = (Item)entityService.getEntity(entId);
        if(item != null) {
            List<EffectDefine> eds = item.getEffects();
            if (eds != null && eds.size() > 0) {
                for (EffectDefine ed : eds) {
                    List<CastleEffect> ces = castleEffectService.getCastleEffectByEffTypeIdWithTimeout(casId, ed
                            .getEffTypeId(), AppConstants.ENT_PARTY_TIME);
                    if (ces != null && ces.size() > 0) {
                        for (CastleEffect ce : ces) {
                            castleEffectService.deleteCastleEffect(ce);
                        }
                    }

                }
            }
        }
    }

    @Override
    public void doBuyResource(long userId, int resEntId, int cashNum) {
        Entity entity = entityService.getEntity(resEntId);
        if (entity == null || !(entity instanceof Resource)) {
            throw new BaseException("错误的道具");
        }
        if (cashNum <= 0) {
            throw new BaseException("钻石数量错误");
        }

        User user = userService.getUserById(userId);
        if (user == null) {
            throw new BaseException("指挥官不存在");
        }

        userService.doConsumeCash(userId, 1 * cashNum, LogCashAct.BYE_RESOURCE);
        long resNum = calcBuyResNum(resEntId, cashNum);
        castleResService.doAddRes(user.getMainCastleId(), resEntId, resNum, true);
    }

    private long calcBuyResNum(int resEntId, int cashNum) {
        int resPerCash;
        if (resEntId == AppConstants.ENT_RES_OIL) {
            resPerCash = commonService.getSysParaIntValue(AppConstants.SYS_OIL_RES_NUM_PER_DIAMOND, 40);
        } else if (resEntId == AppConstants.ENT_RES_IRON) {
            resPerCash = commonService.getSysParaIntValue(AppConstants.SYS_IRON_RES_NUM_PER_DIAMOND, 24);
        } else if (resEntId == AppConstants.ENT_RES_URANIUM) {
            resPerCash = commonService.getSysParaIntValue(AppConstants.SYS_URANIUM_RES_NUM_PER_DIAMOND, 1);
        } else if (resEntId == AppConstants.ENT_RES_GOLD) {
            resPerCash = commonService.getSysParaIntValue(AppConstants.SYS_GOLD_RES_NUM_PER_DIAMOND, 10);
        } else {
            throw new BaseException("错误的资源实体ID");
        }

        return resPerCash * cashNum;
    }

	@Override
	public UserTechnology lockTech(UserTechnology userTech) {
		try {
			userTech = (UserTechnology) MemcachedManager.lockObject(userTech, userTech.getEntId());
		} catch (TimeoutException e) {
			throw new BaseException(e.toString());
		}
		return userTech;
	}

	@Override
	public UserTechnology lockAndGetTech(long userId, long techId) {
		try {
            MemcachedManager.lockClass(UserTechnology.class, techId);
            return this.getUserTechnologyById(userId, techId);
        } catch (TimeoutException e) {
			throw new BaseException(e.toString());
		}
	}

    @Override
    public UserTechnology getUserTechnologyById(long userId, long id) {
        return castleDao.getUserTechnologyById(userId, id);
    }

    @Override
    public List<UserTechnology> getUserTechnologysByUserId(long userId) {
        return castleDao.getUserTechnologysByUserId(userId);
    }

    @Override
    public UserTechnology getUserTechnologyByEntId(long userId, int entId) {
        List<UserTechnology> uts = castleDao.getUserTechnologysByUserId(userId);
        for (UserTechnology ut : uts) {
            if (ut.getEntId() == entId) {
                return ut;
            }
        }
        return null;
        // return castleDao.getUserTechnologyByEntId(userId, entId);
    }

    private UserTechnology getUpgradeUserTechnology(long userId) {
        List<UserTechnology> uts = castleDao.getUserTechnologysByUserId(userId);
        if (uts != null) {
            for (UserTechnology ut : uts) {
                Timestamp t = ut.getEndDttm();
                if (t != null) {
                    return ut;
                }
            }
        }
        return null;
    }


    @Override
    public List<UserTechnology> doRefreshUserTechnologys(long userId, long casId) {
        long now = System.currentTimeMillis();

        //找到所有已解锁的Technology
        List<Entity> techs = entityService.getEntityByEntTypes(Entity.TYPE_TECH);
		List<Entity> openedTechs = new ArrayList<Entity>();
        for (Entity entity : techs) {
            if (!(entity instanceof Technology)) {
                log.error("科技实体配置错误。entId: " + entity.getEntId());
                continue;
            }
            //科技的初始等级为0级
            EntityLimitResultSet result = entityService.checkLimit(entity.getLimitsByLevel(0), casId);
            if (result != null && !result.isMatch()) {
                //不满足开放条件
                continue;
            }
            openedTechs.add(entity);
        }

        //查找并更新用户当前已经有的UserTechnology
        List<UserTechnology> uts = castleDao.getUserTechnologysByUserId(userId);
        if (uts != null) {
            for (UserTechnology ut : uts) {
                Timestamp t = ut.getEndDttm();
                if (t != null) {
                    if (now - t.getTime() > 0) {
                        Castle castle = lockAndGetCastle(casId);
                        ut = lockTech(ut);
                        _doEndUpgradeUserTechnology(castle, ut);
                    }
                }
            }
        }

        //查找已解锁但仍未初始化Technology，创建UserTechnology，并合并到已有的uts集合中，返回
		List<UserTechnology> result = new ArrayList<UserTechnology>(uts);
        for (Entity tech : techs) {
            UserTechnology ut = findUserTechnology(uts, tech.getEntId());
            if (ut == null) {
                // 新建科技0级
                ut = new UserTechnology();
                ut.setEntId(tech.getEntId());
                ut.setLevel(0);
                ut.setBeginDttm(null);
                ut.setEndDttm(null);
                ut.setUserId(userId);
                castleDao.createUserTechnology(ut);

                result.add(ut);
            }
        }

        return result;
    }

    private UserTechnology findUserTechnology(List<UserTechnology> uts, int techEntId) {
        for (UserTechnology ut : uts) {
            if (ut != null && ut.getEntId() == techEntId) {
                return ut;
            }
        }
        return null;
    }

    @Override
    public UserTechnology doUpgradeUserTechnology(long casId, int entId) {
        // 升级科技
        Entity entity = entityService.getEntity(entId);
        if (entity == null || !(entity instanceof Technology)) {
            throw new BaseException("科技类型不对");
        }
        Technology tech = (Technology) entity;
        if (tech.getTarget() != Technology.TARGET_USER
                && tech.getTarget() != Technology.TARGET_USER_ADV) {
            throw new BaseException("非玩家科技，不能升级");
        }
        Castle castle = this.lockAndGetCastle(casId);

        UserTechnology ut = null;
        List<UserTechnology> uts = this.getUserTechnologysByUserId(castle.getUserId());
        if (uts != null) {
            for (UserTechnology tmp : uts) {
                if (tmp.getEndDttm() != null) {
                    throw new BaseException("还有正在升级的科技，您只能升级一个科技");
                }
                if (tmp.getEntId() == entId) {
                    ut = tmp;
                }
            }
        }

        if (ut == null) {
            // 新建科技0级
            ut = new UserTechnology();
            ut.setEntId(entId);
            ut.setLevel(0);
            ut.setBeginDttm(null);
            ut.setEndDttm(null);
            ut.setUserId(castle.getUserId());
            castleDao.createUserTechnology(ut);
        }

        // 检查限制
        ut = lockTech(ut);

        int currLv = ut.getLevel();
        if (currLv >= tech.getMaxLevel()) {
            throw new BaseException(tech.getTechName() + "达到最大等级");
        }
        int nextLv = currLv + 1;
		Map<String, Object> params = new HashMap<String, Object>();
        params.put("castle", castle);
        params.put("userId", castle.getUserId());
        EntityLimitResultSet limit = entityService.checkLimit(tech
                .getLimitsByLevel(nextLv), params);
        if (limit != null && !limit.isMatch()) {
            throw new BaseException(limit.getExceptionString());
        }

        // 更新资源消耗
        EntityConsumeResultSet consume = entityService.updateByEntityConsume(
                tech.getConsumesByLevel(nextLv), castle.getCasId());

        // 科技研究时间
        int time = castleEffectService.getActualTime(casId, consume.getTime(),
                EffectTypeDefine.B_TECH_QUEUE_SPEED);// seconds
        if (time <= AppConstants.MIN_TECH_UPGRADE_SECONDS) {
            time = AppConstants.MIN_TECH_UPGRADE_SECONDS;
        }
        long now = System.currentTimeMillis();
        ut.setBeginDttm(new Timestamp(now));
        ut.setEndDttm(new Timestamp(now + time * 1000L));
        castleDao.updateUserTechnology(ut);

        //启动结束升级的Job
        startEndUpgradeTechJob(casId, ut);

        return ut;
    }

    private void startEndUpgradeTechJob(long casId, UserTechnology ut) {
        // 启动job
        Job job = new Job();
        job.setJobGroupName(JOB_TECH_GROUP_NAME);

        StringBuilder sb = new StringBuilder();
        sb.append(casId).append("_").append(ut.getId());

        job.setJobIdInGroup(sb.toString());
        job.setJobExcuteTime(ut.getEndDttm().getTime());
        job.setJobParams(new Object[] { casId, ut.getId(),
                ut.getLevel() });
        job.setJobType(Job.JOB_DB);
        job.setServiceName("castleService");
        job.setMethodName("doEndUpgradeUserTechnology");

        // 异步启动
        jobServerClient.startJob(job);
    }

    @Override
    public void doEndUpgradeUserTechnology(long casId, int utId, int level) {
        Castle castle = this.lockAndGetCastle(casId);

        UserTechnology ut = this.getUserTechnologyById(castle.getUserId(), utId);
        if (ut == null) {
            log.warn("升级的科技不存在:casId={},utId={}", casId, utId);
            return;
        }
        if (ut.getLevel() != level) {
            log.warn("升级的科技等级错误:casId={},utId={}", casId, utId);
            return;
        }

        if (ut.getEndDttm() == null && ut.getLevel() > 0) {
            if (log.isWarnEnabled()) {
                log.warn("升级的科技不在升级状态:casId={},utId={}", casId, utId);
            }
            return;

        }

        ut = lockTech(ut);
        _doEndUpgradeUserTechnology(castle, ut);

    }

    private void _doEndUpgradeUserTechnology(Castle castle, UserTechnology ut) {
        Technology tech = (Technology) entityService.getEntity(ut.getEntId());
        if (tech == null) {
            log.error("当前升级的科技不存在");
        }
		int oldLv = ut.getLevel();
        int nextLv = ut.getLevel() + 1;
        ut.setLevel(nextLv);
        ut.setBeginDttm(null);
        ut.setEndDttm(null);
        castleDao.updateUserTechnology(ut);

        // 加科技效果
        Map<String, Object> params = new HashMap<String, Object>(4);
        params.put("level", nextLv);
        params.put("castle", castle);
        params.put("mainCastle", castle);
//        params.put("userTech", ut);

        entityService.doAction(tech, Entity.ACTION_LEVEL_UP, params);

        // 发送消息
        StringBuilder sb = new StringBuilder();
        sb.append("您的科技").append(tech.getTechName()).append("达到")
                .append(nextLv).append("级。");
        messageService.sendMessage(0, castle.getUserId(),
                ChatChannelManager.CHANNEL_SYSTEM, null, sb.toString());

		// 科技升级:科技entId/科技当前等级
		missionService.notifyMissionModule(
				userService.getUserById(ut.getUserId()), Mission.QCT_TYPE_Study,
				ut.getEntId(), ut.getLevel());

		// 成就
		userAchieveService.doNotifyAchieveModule(castle.getUserId(),
				AchieveType.TYPE_TECH, ut.getEntId(), ut.getLevel());

        // 推送科技完成的消息
        int upgradeTime = getTechNextLevelUpgradeTime(castle.getCasId(), castle.getUserId(), ut.getEntId());
        messageService.sendEventMessage(ut.getUserId(),
                EventMessage.TYPE_USER_TECH_CHANGED, new UserTechMessage(ut, upgradeTime));

		// 日志
		User user = userService.getUserById(castle.getUserId());
		logService.log(LogBeanFactory.buildLvTechLog(user, tech.getEntId(),
				tech.getEntityName(), oldLv, nextLv));
    }

    @Override
    public UserTechnology doCancelUpgradeUserTechnology(long casId, int entId) {
        Castle castle = this.lockAndGetCastle(casId);
        UserTechnology ut = null;
        if (entId <= 0) {
            ut = this.getUpgradeUserTechnology(castle.getUserId());
        } else {
            ut = this.getUserTechnologyByEntId(castle.getUserId(), entId);
        }
        if (ut == null) {
            throw new BaseException("科技不存在");
        }
        if (ut.getEndDttm() == null) {
            throw new BaseException("科技升级已完成。");
        }
        ut = lockTech(ut);
        ut.setBeginDttm(null);
        ut.setEndDttm(null);
        castleDao.updateUserTechnology(ut);

        Technology tech = (Technology) entityService.getEntity(ut.getEntId());
        // 返还1/2
        entityService.updateByEntityConsume(tech.getConsumesByLevel(ut
                .getLevel() + 1), castle.getCasId(), -1, 2);

        deleteTechJob(casId, ut.getId());
        return ut;
    }

    @Override
    public UserTechnology doFastUpgradeUserTechnology(long casId, int techEntId,
                                                      int speedUpType, int itemEntId, int itemNum) {
        Castle castle = this.lockAndGetCastle(casId);
        long userId = castle.getUserId();

        UserTechnology ut;
        if (techEntId <= 0) {
            ut = this.getUpgradeUserTechnology(castle.getUserId());
        } else {
            ut = this.getUserTechnologyByEntId(castle.getUserId(), techEntId);
        }
        if (ut == null) {
            throw new BaseException("科技不存在");
        }
        Timestamp t = ut.getEndDttm();
        if (t == null) {
            throw new BaseException("科技升级已完成。");
        }

        ut = lockTech(ut);

        int remainSeconds = (int)UtilDate.secondDistance(ut.getEndDttm());
        if (remainSeconds <= commonService.getSysParaIntValue(
                AppConstants.TECH_UPGRADE_FREE_SECONDS,
                AppConstants.TECH_UPGRADE_FREE_SECONDS_DEFAULT)) {
            _doEndUpgradeUserTechnology(castle, ut);
        }
        else{
            //是否使用元宝
            boolean useCash = speedUpType == AppConstants.TECH_SPEED_UP_CASH;
            //扣减元宝或道具，返回扣减的元宝或道具对应的可以减少的时间，单位秒
            int consumedSeconds = speedService.doSpeedUp(userId, SpeedType.TECH_SPEED_UP,
                    remainSeconds, useCash, itemEntId, itemNum);

            //扣减量足够，就完成升级；不够，则只是更新升级剩余时间。
            if (consumedSeconds >= remainSeconds) {
                _doEndUpgradeUserTechnology(castle, ut);
            } else {
                Timestamp newEndTime = UtilDate.moveSecond(ut.getEndDttm(), -1 * consumedSeconds);
                ut.setEndDttm(newEndTime);
                castleDao.updateUserTechnology(ut);

                //更新Job执行时间
                deleteTechJob(casId, ut.getId());
                startEndUpgradeTechJob(casId, ut);

                return ut;
            }
        }

        //删除已加速完成的升级任务的job
        deleteTechJob(casId, ut.getId());

        return ut;
    }

    private void deleteTechJob(long casId, long userTechId) {
        StringBuilder sb = new StringBuilder();
        sb.append(casId).append("_").append(userTechId);
        // 即时没删除掉，也不影响
        jobServerClient.deleteJob(JOB_TECH_GROUP_NAME, sb.toString());
    }

    @Override
	public int getTechNextLevelUpgradeTime(long casId, long userId, int techEntId) {
        Entity entity = entityService.getEntity(techEntId);
		if (!(entity instanceof Technology)) {
			throw new BaseException("科技实体数据错误");
		}

        Technology tech = (Technology)entity;

        UserTechnology userTech = getUserTechnologyByEntId(userId, techEntId);
        int currLv = 0;
        if(userTech != null){//如果没有userTech记录，说明还没有升级过，初始级别是0
            currLv = userTech.getLevel();
        }
		// 检查限制
		if (currLv >= tech.getMaxLevel()) {
			// 不能再升级，返回0，表示值无效
			return 0;
		}

		int nextLv = currLv + 1;
		EntityConsumeResultSet checkResult = entityService
				.getEntityConsumeCheckResult(
						tech.getConsumesByLevel(nextLv), casId);

		int time = castleEffectService.getActualTime(casId,
				checkResult.getTime(),
				EffectTypeDefine.B_TECH_QUEUE_SPEED);
		if (time <= AppConstants.MIN_TECH_UPGRADE_SECONDS) {
			time = AppConstants.MIN_TECH_UPGRADE_SECONDS;
		}

		return time;
	}
}

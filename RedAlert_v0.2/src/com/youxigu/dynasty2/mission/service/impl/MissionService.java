package com.youxigu.dynasty2.mission.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibatis.sqlmap.engine.cache.memcached.MemcachedManager;
import com.youxigu.dynasty2.chat.EventMessage;
import com.youxigu.dynasty2.chat.client.IChatClientService;
import com.youxigu.dynasty2.core.event.Event;
import com.youxigu.dynasty2.core.event.EventDispatcher;
import com.youxigu.dynasty2.core.event.EventTypeConstants;
import com.youxigu.dynasty2.core.event.IEventListener;
import com.youxigu.dynasty2.log.ILogService;
import com.youxigu.dynasty2.log.LogBeanFactory;
import com.youxigu.dynasty2.mission.dao.IMissionDao;
import com.youxigu.dynasty2.mission.domain.Mission;
import com.youxigu.dynasty2.mission.domain.MissionAward;
import com.youxigu.dynasty2.mission.domain.MissionLimit;
import com.youxigu.dynasty2.mission.domain.MissionType;
import com.youxigu.dynasty2.mission.domain.UserMission;
import com.youxigu.dynasty2.mission.proto.MainCountEventMsg;
import com.youxigu.dynasty2.mission.proto.UserMissionView;
import com.youxigu.dynasty2.mission.service.IMissionAwardService;
import com.youxigu.dynasty2.mission.service.IMissionCompleteChecker;
import com.youxigu.dynasty2.mission.service.IMissionService;
import com.youxigu.dynasty2.mission.service.IWorldMissionClientService;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.user.service.IAccountService;
import com.youxigu.dynasty2.user.service.IUserService;
import com.youxigu.dynasty2.util.BaseException;

public class MissionService implements IMissionService, IEventListener {
	public static final Logger log = LoggerFactory
			.getLogger(MissionService.class);

	public static final String MISSION_LOCK_KEY_PREFIX = "L_MISSION_";
	public static final String GUILD_MISSION_LOCK_KEY_PREFIX = "L_G_MISSION_";
	// public static final int FIRST_MISSION_ID = 10100;

	private IMissionDao missionDao;
	private IUserService userService;
	// private ICastleService castleService;

	private IChatClientService messageService;
	// private ICommonService commonService;
	// private ICombatService combatService;
	// private IVipService vipService;
	// private ITreasuryService treasuryService;
	private ILogService logService;
	private ILogService tlogService;
	private IAccountService accountService;
	private IWorldMissionClientService worldMissionClientService;

	private final Map<String, MissionType> missionTypeMap = new HashMap<String, MissionType>();
	/**
	 * 所有的任务
	 */
	private final Map<Integer, Mission> missionMap = new HashMap<Integer, Mission>();

	/**
	 * 君主等级对应的任务--parentMissionId == -2
	 */
	private final Map<Integer, List<Mission>> usrlv4MissionMap = new HashMap<Integer, List<Mission>>();

	/**
	 * 所有的根任务：parentMissionId=0
	 */
	private final Map<Integer, Mission> rootMissionMap = new HashMap<Integer, Mission>();
	/**
	 * 按类型分类的任务：missionType
	 */
	private final Map<String, List<Mission>> missionMapByType = new HashMap<String, List<Mission>>();

	// /**
	// * 按冒险场景产生的任务
	// */
	// private final Map<Integer, Mission> sceneIdMissionMap = new
	// HashMap<Integer, Mission>();

	/**
	 * 所有的任务完成条件
	 */
	private final Map<Integer, MissionLimit> missionLimitMap = new HashMap<Integer, MissionLimit>();

	// /**
	// * 随机任务配置
	// */
	// private final Map<Integer, RandomMissionConfig> randomMissionConfigMap =
	// new HashMap<Integer, RandomMissionConfig>();
	// /**
	// * 所有的随机威望任务:按hardLv分组
	// */
	// private final Map<Integer, List<Mission>> honorRandomMissionMap = new
	// HashMap<Integer, List<Mission>>();
	//
	// private int randomMissionConfig_maxLv = 0;
	// /**
	// * 所有的联盟循环任务
	// */
	// private final Map<Integer, Map<Integer, List<Mission>>>
	// guildRandomMissionMap = new HashMap<Integer, Map<Integer,
	// List<Mission>>>();
	// private int guildMissionConfig_maxLv = 0;

	// /**
	// * 君主任务模板数据
	// */
	// private final Map<Integer, UserExpTemp> userExps = new HashMap<Integer,
	// UserExpTemp>();
	// /**
	// * 君主任务积分奖励模板数据
	// */
	// private final Map<Integer, UserExpScoreTemp> userExpScores = new
	// HashMap<Integer, UserExpScoreTemp>();
	// /**
	// * 所有君主任务权重之和
	// */
	// private int userMissiomSumWeight = 0;

	/**
	 * 任务完成检查器
	 * 
	 * 
	 * QCT_Item //背包是否有某种道具 <BR>
	 * QCT_Kill // 杀怪 <BR>
	 * QCT_Defeat <BR>
	 * QCT_Move <BR>
	 * QCT_Dig <BR>
	 * QCT_Side <BR>
	 * QCT_Story <BR>
	 * QCT_Scene <BR>
	 * QCT_Hero <BR>
	 * QCT_Drill <BR>
	 * QCT_Defence// 制造城防 <BR>
	 * QCT_Resource// 囤积资源 <BR>
	 * QCT_War// 攻陷营寨 <BR>
	 * QCT_Collection// 收集道具 <BR>
	 * QCT_Payment//// 捐献资源 <BR>
	 * QCT_Use// 使用道具 <BR>
	 * QCT_Level//升级建筑 <BR>
	 * QCT_Study//研究科技 <BR>
	 * QCT_Pay// 买进资源 <BR>
	 * QCT_Sell// 卖出资源 <BR>
	 * QCT_Enlarge //扩建城池 <BR>
	 * QCT_Uplevel// 君主升级 <BR>
	 * QCT_Farm //农场开工 <BR>
	 * QCT_Upleague"// 联盟升级 <BR>
	 * QCT_Speedup <BR>
	 * QCT_Recruit <BR>
	 * QCT_Hunt// 寻宝 --不要了
	 */

	private Map<String, IMissionCompleteChecker> limitCheckers = new HashMap<String, IMissionCompleteChecker>();

	/**
	 * 奖励 铜币 money”； “点券 gold”； “粮食 food”； “木材 wood”； “石料 stone”； “矿物metal”；
	 * “四种资源resource”； “特殊人口superpop”； “道具 item”； “军队或城防 arm”； “人口pop”；
	 * “君主经验value1”； “繁荣度rangeValue”； “威望honor”； “联盟贡献value2”； “联盟荣誉value3”；
	 * “联盟建设value4”；
	 */
	private Map<String, IMissionAwardService> missionAwards = new HashMap<String, IMissionAwardService>();

	public void setAccountService(IAccountService accountService) {
		this.accountService = accountService;
	}

	public void setLogService(ILogService logService) {
		this.logService = logService;
	}

	public void setTlogService(ILogService tlogService) {
		this.tlogService = tlogService;
	}

	// public void setLinePlatformFriendService(
	// ILinePlatformFriendService linePlatformFriendService) {
	// this.linePlatformFriendService = linePlatformFriendService;
	// }
	//
	// public void setUserDailyActivityService(
	// IUserDailyActivityService userDailyActivityService) {
	// this.userDailyActivityService = userDailyActivityService;
	// }
	//
	// public void setUserFuncationService(
	// IUserFuncationService userFuncationService) {
	// this.userFuncationService = userFuncationService;
	// }
	//
	// public void setCombatService(ICombatService combatService) {
	// this.combatService = combatService;
	// }

	public void setMessageService(IChatClientService messageService) {
		this.messageService = messageService;
	}

	// public void setGuildService(IGuildService guildService) {
	// this.guildService = guildService;
	// }

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public void setMissionDao(IMissionDao missionDao) {
		this.missionDao = missionDao;
	}

	public void setCheckers(Map<String, IMissionCompleteChecker> checkers) {
		this.limitCheckers = checkers;
	}

	public void setMissionAwards(
			Map<String, IMissionAwardService> missionAwards) {
		this.missionAwards = missionAwards;
	}

	// public void setCommonService(ICommonService commonService) {
	// this.commonService = commonService;
	// }
	//
	// public void setVipService(IVipService vipService) {
	// this.vipService = vipService;
	// }
	//
	// public void setTreasuryService(ITreasuryService treasuryService) {
	// this.treasuryService = treasuryService;
	// }

	public void setWorldMissionClientService(
			IWorldMissionClientService worldMissionClientService) {
		this.worldMissionClientService = worldMissionClientService;
	}

	public void init() {
		log.info("========开始加载Mission数据===========");

		List<MissionType> missionTypes = missionDao.getMissionTypes();

		for (MissionType type : missionTypes) {
			missionTypeMap.put(type.getMissionChildType(), type);
		}

		// 任务完成条件定义
		List<MissionLimit> list2 = missionDao.getAllMissionLimits();
		for (MissionLimit limit : list2) {
			// 这两个类型是一样的，这里改成相同
			// if (Mission.QCT_TYPE_ARMY.equals(limit.getOctType())) {
			// limit.setOctType(Mission.QCT_TYPE_ARMY_DRILL);
			// }

			// if (Mission.QCT_TYPE_COLLECTHERO.equals(limit.getOctType())) {
			// //收集名将任务，要动态配置每个朝代的名将数量
			// }

			IMissionCompleteChecker checker = limitCheckers
					.get(limit.getOctType());
			if (checker == null) {
				log.error("missionLimit:{} not found limit checker",
						limit.getMissioncompleteId());
			}
			limit.setLimitChecker(checker);
			missionLimitMap.put(limit.getMissioncompleteId(), limit);
		}

		// 任务定义
		// 普通任务
		List<Mission> list = missionDao.getAllMissions();
		// 活动任务
		// list.addAll(missionDao.getAllActivityMissions());

		for (Mission mission : list) {
			missionMap.put(mission.getMissionId(), mission);
			
			// 按君主等级初始化的任务
			int needLv = mission.getMissionLevel();
			if (!mission.getMissionType().equals(Mission.MISSION_TYPE_WORLD)
					&& needLv > 0 && mission.getParentMissionId() == -2) {
				List<Mission> lvList = usrlv4MissionMap.get(needLv);
				if (lvList == null) {
					lvList = new ArrayList<Mission>();
					usrlv4MissionMap.put(needLv, lvList);
				}
				lvList.add(mission);
			}

			if (mission.getParentMissionId() == 0) {
				rootMissionMap.put(mission.getMissionId(), mission);
			}
			// if (mission.getIssueMissionId() > 0) {
			// sceneIdMissionMap.put(mission.getIssueMissionId(), mission);
			// }

			// 按type分类
			List<Mission> typeMissions = missionMapByType
					.get(mission.getMissionType());
			if (typeMissions == null) {
				typeMissions = new ArrayList<Mission>();
				missionMapByType.put(mission.getMissionType(), typeMissions);
			}
			typeMissions.add(mission);

			// 关联完成条件
			MissionLimit limit = null;

			if (mission.getMissioncompleteId1() > 0) {
				limit = missionLimitMap.get(mission.getMissioncompleteId1());
				if (limit != null) {
					mission.addLimit(limit, 1);
				} else {
					log.error("任务{}的完成条件ID={}不存在", mission.getMissionId(),
							mission.getMissioncompleteId1());
				}
			}

			if (mission.getMissioncompleteId2() > 0) {
				limit = missionLimitMap.get(mission.getMissioncompleteId2());
				if (limit != null) {
					mission.addLimit(limit, 2);
				} else {
					log.error("任务{}的完成条件ID={}不存在", mission.getMissionId(),
							mission.getMissioncompleteId2());
				}
			}

			if (mission.getMissioncompleteId3() > 0) {
				limit = missionLimitMap.get(mission.getMissioncompleteId3());
				if (limit != null) {
					mission.addLimit(limit, 3);
				} else {
					log.error("任务{}的完成条件ID={}不存在", mission.getMissionId(),
							mission.getMissioncompleteId3());
				}
			}

			if (mission.getMissioncompleteId4() > 0) {
				limit = missionLimitMap.get(mission.getMissioncompleteId4());
				if (limit != null) {
					mission.addLimit(limit, 4);
				} else {
					log.error("任务{}的完成条件ID={}不存在", mission.getMissionId(),
							mission.getMissioncompleteId4());
				}
			}

			// 奖励

			if (mission.getAwardNum1() != 0) {
				if (!missionAwards.containsKey(mission.getAwardtype1())) {
					log.error("任务{}奖励类型错误:{}", mission.getMissionId(),
							mission.getAwardtype1());
				}
				mission.addMissionAward(
						new MissionAward(mission.getAwardtype1(),
								mission.getAwardId1(), mission.getAwardNum1()));
			}
			if (mission.getAwardNum2() != 0) {
				if (!missionAwards.containsKey(mission.getAwardtype2())) {
					log.error("任务{}奖励类型错误:{}", mission.getMissionId(),
							mission.getAwardtype2());
				}

				mission.addMissionAward(
						new MissionAward(mission.getAwardtype2(),
								mission.getAwardId2(), mission.getAwardNum2()));
			}
			if (mission.getAwardNum3() != 0) {
				if (!missionAwards.containsKey(mission.getAwardtype3())) {
					log.error("任务{}奖励类型错误:{}", mission.getMissionId(),
							mission.getAwardtype3());
				}

				mission.addMissionAward(
						new MissionAward(mission.getAwardtype3(),
								mission.getAwardId3(), mission.getAwardNum3()));
			}
			if (mission.getAwardNum4() != 0) {
				if (!missionAwards.containsKey(mission.getAwardtype4())) {
					log.error("任务{}奖励类型错误:{}", mission.getMissionId(),
							mission.getAwardtype4());
				}
				mission.addMissionAward(
						new MissionAward(mission.getAwardtype4(),
								mission.getAwardId4(), mission.getAwardNum4()));
			}

			List<MissionLimit> limits = mission
					.getLimitsByOctType(Mission.QCT_TYPE_COLLECTION);
			if (limits != null && limits.size() > 0) {
				// 这里完成条件需要删除,作为负数的奖励
				for (MissionLimit limitTmp : limits) {
					mission.addMissionAward(new MissionAward(
							Mission.AWARD_TYPE_ITEM, limitTmp.getEntId(),
							-1 * limitTmp.getEntNum()));
				}
			}
		}

		// 父子关联
		for (Mission mission : list) {
			if (mission.getParentMissionId() > 0) {
				Mission parent = missionMap.get(mission.getParentMissionId());
				if (parent == null) {
					log.error("配置数据错误,任务的父任务不存在:{}", mission.getMissionId());
					continue;
				}
				mission.setParent(parent);
				parent.addChild(mission);
			}
		}

		// 君主升级事件
		EventDispatcher.registerListener(EventTypeConstants.EVT_USER_LEVEL_UP,
				this);

		// // 随机任务(声望任务/联盟随机任务)
		// List<RandomMissionConfig> configList = missionDao
		// .getAllRandMissionConfig();
		// for (RandomMissionConfig conf : configList) {
		//
		// randomMissionConfigMap.put(conf.getMissionId(), conf);
		// if (Mission.MISSION_TYPE_HONOR.equals(conf.getMissionType())) {//
		// 声望任务
		// List<Mission> missions = honorRandomMissionMap.get(conf
		// .getHardLevel());
		// if (missions == null) {
		// missions = new ArrayList<Mission>();
		// honorRandomMissionMap.put(conf.getHardLevel(), missions);
		// }
		// Mission currMission = missionMap.get(conf.getMissionId());
		// if (currMission != null) {
		// missions.add(currMission);
		// if (randomMissionConfig_maxLv < conf.getHardLevel()) {
		// randomMissionConfig_maxLv = conf.getHardLevel();
		// }
		// } else {
		// log.error(com.youxigu.dynasty2.i18n.MarkupMessages.getString("MissionService_3"),
		// conf.getMissionId());
		// }
		//
		// } else {// 联盟随机任务
		// Map<Integer, List<Mission>> subMaps = guildRandomMissionMap
		// .get(conf.getHardLevel());
		// if (subMaps == null) {
		// subMaps = new HashMap<Integer, List<Mission>>();
		// guildRandomMissionMap.put(conf.getHardLevel(), subMaps);
		// }
		// Mission currMission = missionMap.get(conf.getMissionId());
		// if (currMission != null) {
		// List<Mission> missions = subMaps.get(currMission
		// .getMissionHardLevel());
		// if (missions == null) {
		// missions = new ArrayList<Mission>();
		// subMaps
		// .put(currMission.getMissionHardLevel(),
		// missions);
		// }
		// missions.add(missionMap.get(conf.getMissionId()));
		// if (guildMissionConfig_maxLv < conf.getHardLevel()) {
		// guildMissionConfig_maxLv = conf.getHardLevel();
		// }
		// } else {
		// log.error(com.youxigu.dynasty2.i18n.MarkupMessages.getString("MissionService_17"),
		// conf.getMissionId());
		// }
		// }
		// // 删除随机根任务
		// rootMissionMap.remove(conf.getMissionId());
		// }
		//
		// //君主任务模板数据加载
		// List<UserExpTemp> expList = missionDao.getAllUserExpTempList();
		// List<UserExpScoreTemp> scoreList =
		// missionDao.getAllUserExpScoreTempList();
		// for(UserExpTemp exp : expList){
		// userExps.put(exp.getId(), exp);
		// userMissiomSumWeight += exp.getWeight();
		// }
		// for(UserExpScoreTemp score : scoreList){
		// score.init();
		// userExpScores.put(score.getScore(), score);
		// }
		log.info("========加载Mission数据结束===========");
	}

	@Override
	public void doEvent(Event event) {
		if (event.getEventType() == EventTypeConstants.EVT_USER_LEVEL_UP) {
			Map<String, Object> params = (Map<String, Object>) event
					.getParams();
			User user = (User) params.get("user");
			int oldLv = (Integer) params.get("oldLv");
			int newLv = (Integer) params.get("newLv");

			// 初始化等级任务
			this.doAddUserLevelMission(user, oldLv, newLv);

			// add new mission
			this.doAddUserMission(user);
		}
	}

	@Override
	public Mission getMissionById(int id) {
		return missionMap.get(id);
		// if (mission == null) {
		// throw new BaseException("任务定义不存在");
		// }
		// return mission;
	}

	@Override
	public IMissionAwardService getAwardService(String awardtype) {
		return missionAwards.get(awardtype);
	}

	@Override
	public List<Mission> getMissionByType(String type) {
		return missionMapByType.get(type);
	}

	@Override
	public Map<Integer, Mission> getRootMission() {
		// TODO Auto-generated method stub
		return rootMissionMap;
	}

	@Override
	public MissionLimit getMissionLimitById(int id) {
		return missionLimitMap.get(id);
	}

	@Override
	public UserMission getUserMissionById(int id, long userId) {
		return missionDao.getUserMissionById(id, userId);
	}

	@Override
	public List<UserMissionView> doRefreshAndGetUserMissionByUserId(User user,
			String missionType) {
		// lockUserMission(user.getUserId());
		List<UserMission> userMissions = missionDao
				.getUserMissionByUserId(user.getUserId());
		// if (AppConstants.IS_TEST_VERSION) {// /测试时策划总是改变任务树，在测试环境下重新生成
		// if (userMissions == null || userMissions.isEmpty()) {
		// doInitUserMission(user);
		// userMissions = missionDao.getUserMissionByUserId(user
		// .getUserId());
		// }
		// }
		// UserMission honorMission = null;// 威望任务
		List<UserMissionView> missList = new ArrayList<UserMissionView>();
		for (UserMission userMission : userMissions) {
			if (userMission.getStatus() == UserMission.STATUS_COMMIT) {// 过滤已提交的
				continue;
			}
			Mission mission = this.getMissionById(userMission.getMissionId());
			if (mission != null) {
				if (missionType == null
						|| mission.getMissionType().equals(missionType)) {
					// if (userMission.getStatus() !=
					// UserMission.STATUS_COMPLETE) {
					doRefreshUserMissionStatus(user, userMission);
					// }
					// if (Mission.MISSION_TYPE_HONOR.equals(mission
					// .getMissionType())) {
					// // 威望任务
					// honorMission = userMission;
					// } else {
					missList.add(userMission.getView(-1));
					// }

				}
			}
		}

		// if(honorMissionView == null) {
		// UserMission um = this.doInitHonorRandomMission(user);
		// if (um != null) {
		// honorMissionView = um.getView();
		// }
		// }

		// if (honorMission != null) {
		// UserDailyMissionNum dailyNum = this
		// .doRefreshAndGetUserDailyMissionNum(user,
		// Mission.MISSION_TYPE_HONOR);
		// int maxNum = this.getDailyMissionMaxNum(user,
		// Mission.MISSION_TYPE_HONOR);
		// int cost = this.getCash4ChangeRandomMission();
		// int finishCost = this.getCash4FinishRandomMission();
		//
		// HonorUserMissionView honorUserMissionView = new HonorUserMissionView(
		// honorMission, maxNum, cost, finishCost, (maxNum - dailyNum
		// .getNum()), honorMission.getRead0());
		// missList.add(honorUserMissionView);
		// }
		return missList;
	}

	// /**
	// * 联盟任务的Id为 -1,-2,-3,-4,-5
	// *
	// * @param user
	// * @return
	// */
	// @Deprecated
	// public List<UserMissionView> doGetUserRandomGuildMissions(User user) {
	// if (user.getGuildId() > 0) {
	// GuildMission guildMission = missionDao.getGuildMission(user
	// .getGuildId());
	// if (guildMission == null) {
	// // //这里是为了以前测试数据需要
	// synchronized (this) {
	// guildMission = missionDao
	// .getGuildMission(user.getGuildId());
	// if (guildMission == null) {
	// guildMission = doInitGuildRandomMission(guildService
	// .getGuildById(user.getGuildId()));
	// }
	// }
	// }
	// if (guildMission != null) {
	// List<UserMissionView> usermissions = new ArrayList<UserMissionView>();
	// if (guildMission.getMissionId1() > 0) {
	// UserMission um = new UserMission();
	// um.setMissionId(guildMission.getMissionId1());
	// um.setCompleteNum(guildMission.getNum1());
	// um.setUserMissionId(-1);
	// um.setUserId(user.getUserId());
	// um.setFactor(1);
	// _doRefreshUserMissionStatus(user, um, this
	// .getMissionById(guildMission.getMissionId1()));
	// usermissions.add(um.getView());
	// }
	//
	// if (guildMission.getMissionId2() > 0) {
	// UserMission um = new UserMission();
	// um.setMissionId(guildMission.getMissionId2());
	// um.setCompleteNum(guildMission.getNum2());
	// um.setUserMissionId(-2);
	// um.setUserId(user.getUserId());
	// um.setFactor(1);
	// _doRefreshUserMissionStatus(user, um, this
	// .getMissionById(guildMission.getMissionId2()));
	// usermissions.add(um.getView());
	// }
	//
	// if (guildMission.getMissionId3() > 0) {
	// UserMission um = new UserMission();
	// um.setMissionId(guildMission.getMissionId3());
	// um.setCompleteNum(guildMission.getNum3());
	// um.setUserMissionId(-3);
	// um.setUserId(user.getUserId());
	// um.setFactor(1);
	// _doRefreshUserMissionStatus(user, um, this
	// .getMissionById(guildMission.getMissionId3()));
	// usermissions.add(um.getView());
	// }
	//
	// if (guildMission.getMissionId4() > 0) {
	// UserMission um = new UserMission();
	// um.setMissionId(guildMission.getMissionId4());
	// um.setCompleteNum(guildMission.getNum4());
	// um.setUserMissionId(-4);
	// um.setUserId(user.getUserId());
	// um.setFactor(1);
	// _doRefreshUserMissionStatus(user, um, this
	// .getMissionById(guildMission.getMissionId4()));
	// usermissions.add(um.getView());
	// }
	//
	// if (guildMission.getMissionId5() > 0) {
	// UserMission um = new UserMission();
	// um.setMissionId(guildMission.getMissionId5());
	// um.setCompleteNum(guildMission.getNum5());
	// um.setUserMissionId(-5);
	// um.setUserId(user.getUserId());
	// um.setFactor(1);
	// _doRefreshUserMissionStatus(user, um, this
	// .getMissionById(guildMission.getMissionId5()));
	// usermissions.add(um.getView());
	// }
	// return usermissions;
	// }
	// }
	// return null;
	// }

	@Override
	public int getFinishUserMissionNum(long userId, String missionType) {
		int num = 0;
		List<UserMission> userMissions = missionDao
				.getUserMissionByUserId(userId);
		if (userMissions != null && userMissions.size() > 0) {
			for (UserMission userMission : userMissions) {
				if (userMission.getStatus() == UserMission.STATUS_COMMIT) {// 过滤已提交的
					continue;
				}
				Mission mission = this
						.getMissionById(userMission.getMissionId());
				if (mission != null) {
					if (missionType == null
							|| mission.getMissionType().equals(missionType)) {
						if (userMission
								.getStatus() == UserMission.STATUS_COMPLETE) {
							num = num + 1;
						}
					}
				}
			}
		}
		return num;
	}

	@Override
	public UserMission getUserMissionByMissionId(long userId, int missionId) {
		return missionDao.getUserMissionByMissionId(userId, missionId);
	}

	/**
	 * 接任务,
	 * 
	 * 活动任务玩家接任务后就完成
	 * 
	 * 
	 * 其他任务不是由玩家主动接任务，而是后台在完成一个任务后，自动生成后续任务
	 * 
	 * @param userId
	 * @param missionId
	 */
	// private void aquireMission(long userId, int missionId) {
	// Mission mission = this.getMissionById(missionId);
	// User user = userService.getUserById(userId);
	// aquireMission(user, mission);
	// }

	// /**
	// * 日常任务的每日最大可接次数
	// *
	// * @param user
	// * @param missionType
	// * @return
	// */
	// public int getDailyMissionMaxNum(User user, String missionType) {
	// // 增加vip效果
	// VipEffect eff = vipService.getVipEffect(user.getUserId(),
	// VipEffect.VIP_PRESTIGE);
	//
	// int max = commonService.getSysParaIntValue(
	// AppConstants.MISSION_DAILY_NUM,
	// AppConstants.MISSION_DAILY_NUM_DEFAULT);
	//
	// max = max + eff.getAbsValue();
	// // 目前没有按类型区分
	// return max;
	// }
	//
	// /**
	// * 增加每日随机任务执行的记录数
	// *
	// * @param userId
	// */
	// private void addDailyRandMissionActionNum(User user, String missionType)
	// {
	// UserDailyMissionNum ud = doRefreshAndGetUserDailyMissionNum(user,
	// missionType);
	// int maxNum = this.getDailyMissionMaxNum(user, missionType);
	// if (ud.getNum() >= maxNum) {
	// throw new
	// BaseException(com.youxigu.dynasty2.i18n.MarkupMessages.getString("MissionService_6"));
	// } else {
	// ud.setNum(ud.getNum() + 1);
	// missionDao.updateUserDailyMissionNum(ud);
	// }
	// }
	//
	// public UserDailyMissionNum doRefreshAndGetUserDailyMissionNum(User user,
	// String missionType) {
	// // TODO：这里用不用锁？
	// Timestamp now = new Timestamp(System.currentTimeMillis());
	// UserDailyMissionNum ud = missionDao.getUserDailyMissionNum(user
	// .getUserId(), missionType);
	// if (ud == null) {
	// ud = new UserDailyMissionNum();
	// ud.setUserId(user.getUserId());
	// ud.setMissionType(missionType);
	// ud.setLastDttm(now);
	// missionDao.createUserDailyMissionNum(ud);
	// } else {
	// if (!DateUtils.isSameDay(ud.getLastDttm(), now)) {
	// ud.setLastDttm(now);
	// ud.setNum(0);
	// missionDao.updateUserDailyMissionNum(ud);
	// }
	// }
	// return ud;
	// }

	@Override
	public UserMission doCommitMission(User user, int userMissionId,
			boolean isConsume) {

		lockUserMission(user.getUserId());
		UserMission userMission = this.getUserMissionById(userMissionId,
				user.getUserId());

		if (userMission == null
				|| userMission.getUserId() != user.getUserId()) {
			throw new BaseException("没有此任务");
		}
		Mission missionEnt = this.getMissionById(userMission.getMissionId());
		if (missionEnt == null) {
			throw new BaseException("任务定义不存在");
		}
		// 再次判断任务是否完毕
		_doRefreshUserMissionStatus(user, userMission, missionEnt);

		if (userMission.getStatus() == UserMission.STATUS_COMPLETE) {// 可交任务状态
			// if
			// (missionEnt.getMissionType().equals(Mission.MISSION_TYPE_HONOR))
			// {
			// // 每日随机声望任务
			// commitHonorRandomMission(user, userMission, missionEnt, true);
			// } else {
			// 内政任务/冒险任务//名将任务// 联盟普通任务
			commitGeneralMission(user, userMission, missionEnt);
			// }
		} else {
			// if (!isConsume) {// 使用元宝直接完成
			// if (missionEnt.getMissionType().equals(
			// Mission.MISSION_TYPE_HONOR)) {
			// // 每日随机声望任务
			// commitHonorRandomMission(user, userMission, missionEnt,
			// false);
			// }
			// } else {
			throw new BaseException("任务没有完成！");
			// }
		}
		// userFuncationService.doUpdateUserFunction(user.getUserId(),
		// userMission.getMissionId());
		return userMission;
	}

	@Override
	public UserMission doMissionRead(long userId, int userMissionId) {
		lockUserMission(userId);
		UserMission userMission = this.getUserMissionById(userMissionId,
				userId);

		if (userMission == null || userMission.getUserId() != userId) {
			throw new BaseException("没有此任务");
		}

		if (userMission.getRead0() == 0) {
			userMission.setRead0(1);
			missionDao.updateUserMission(userMission);
		}
		return userMission;
	}

	/**
	 * 提交已经完成的内政任务
	 * 
	 * @param user
	 * @param userMission
	 * @param missionEnt
	 */
	private void commitGeneralMission(User user, UserMission userMission,
			Mission missionEnt) {

		// 先删除，再奖励，防止奖励又触发任务完成
		// 成长任务直接删除
		missionDao.deleteUserMission(userMission);

		doConsume(user, userMission, missionEnt);

		doRewardMission(user, userMission, missionEnt);// 发放奖励
		// 通知检查完成冒险任务n次的任务
		// if (Mission.MISSION_TYPE_RISK.equals(missionEnt.getMissionType())) {
		// this.notifyMissionModule(user, Mission.QCT_TYPE_RISK, 0, 1);
		// }

		// userMission.setStatus(UserMission.STATUS_COMMIT);// 设置成提交后的状态
		// 父任务提交完成后，自动接子任务
		List<Mission> childs = missionEnt.getChildMissions();
		if (childs != null) {
			for (Mission child : childs) {
				_createUserMission(user, child, false);
			}
		}
	}

	/**
	 * 完成任务时，需要减少的实体
	 * 
	 * @param user
	 * @param userMission
	 * @param mission
	 */
	private void doConsume(User user, UserMission userMission,
			Mission mission) {
		Collection<MissionLimit> limits = mission.getLimitIndexs().keySet();

		if (limits == null || limits.size() == 0) {
			return;
		}

		Iterator<MissionLimit> lit = limits.iterator();

		while (lit.hasNext()) {
			MissionLimit limit = lit.next();
			limit.getLimitChecker().doConsume(user, userMission, mission,
					limit);

		}

	}

	// /**
	// * 提交随机声望任务
	// *
	// * @param user
	// * @param userMission
	// * @param missionEnt
	// * @param isConsume
	// * 是否检查任务消耗
	// */
	// private void commitHonorRandomMission(User user, UserMission userMission,
	// Mission missionEnt, boolean isConsume) {
	//
	// if (isConsume) {
	// doConsume(user, userMission, missionEnt);
	// } else {
	// // 扣除花费的元宝
	// int cost = this.getCash4FinishRandomMission();
	// userService.doConsumeCash(user.getUserId(), -1 * cost,
	// LogCashAct.WEIWANGRENWU_ACTION);
	// }
	//
	// addDailyRandMissionActionNum(user, Mission.MISSION_TYPE_HONOR);// 增加执行记录
	//
	// doRewardMission(user, userMission, missionEnt);// 发放奖励
	// // 通知检查完成声望任务n次的任务
	// // this.notifyMissionModule(user, Mission.QCT_TYPE_HONOR, 0, 1);
	//
	//// userDailyActivityService.addUserDailyActivity(user.getUserId(),
	//// DailyActivity.ACT_USER_PRESTIGE_TASK, (byte) 1);
	//
	// // 继续生成随机任务
	// Mission mission = getRandomMission(user.getUsrLv(), missionEnt);
	// if (mission == null) {
	// mission = missionEnt;
	// }
	//
	// // if
	// // (Mission.MISSION_TYPE_TREASUREHUNT.equals(mission.getMissionType())){
	// // treasureHuntService.generateNewTreasureHunt(user.getUserId());
	// // }
	//
	// userMission.setMissionId(mission.getMissionId());
	// userMission.setNum1(0);
	// userMission.setNum2(0);
	// userMission.setNum3(0);
	// userMission.setNum4(0);
	// userMission.setNum5(0);
	// if (mission.getMissionId() == 30005) {
	// userMission.setFactor(Util.randInt(4) + 2);
	// } else {
	// userMission.setFactor(Util.randInt(5) + 1);
	// }
	// userMission.setStatus(UserMission.STATUS_NEW);
	// this.updateUserMission(userMission, user, true);
	//
	// setMissionTLog(mission.getMissionType(), mission.getMissionId(),
	// mission.getMissionName(), 1, user);
	// //
	// sendMissionEvent(user.getUserId(),EventMessage.TYPE_NEW_MISSION,userMission.getView());
	// // doRefreshUserMissionStatus(user, userMission);
	//
	// }

	public void sendMissionEvent(User user, int eventType,
			UserMission userMission) {
		// Mission mission = this.getMissionById(userMission.getMissionId());
		// UserMissionView view = null;
		// view = userMission.getView(eventType);
		//
		// messageService.sendEventMessage(user.getUserId(), eventType, view);
		// if (log.isDebugEnabled()) {
		// log.debug("通知前台任务变更：{},{}", view, eventType);
		// }
		this.sendFinishUserMissionNum(userMission);
	}

	/**
	 * 推送用户任务完成未领奖的数量
	 * 
	 * @param userMission
	 */
	private void sendFinishUserMissionNum(UserMission userMission) {
		if (userMission.getStatus() != UserMission.STATUS_COMPLETE) {
			return;
		}
		long userId = userMission.getUserId();
		if (userId > 0) {
			int eventType = EventMessage.TYPE_MAIN_MISSION_FINISHCOUNT;
			// 普通任务未领奖的数量
			int mainNum = this.getFinishUserMissionNum(userId,
					Mission.MISSION_TYPE_MAIN);
			messageService.sendEventMessage(userId, eventType,
					new MainCountEventMsg(mainNum, eventType));
		}
	}

	// /**
	// * 提交联盟随机任务，领取奖励
	// *
	// * @param userId
	// * @param userMissionId
	// */
	// @Deprecated
	// public GuildMission doCommitGuildRandomMission(User user, int
	// userMissionId) {
	// lockGuildMission(user.getGuildId());
	// GuildMission guildMission = missionDao.getGuildMission(user
	// .getGuildId());
	// if (guildMission == null) {
	// throw new
	// BaseException(com.youxigu.dynasty2.i18n.MarkupMessages.getString("MissionService_22"));
	// }
	//
	// int missionId = 0;
	// int num = 0;// 完成次数
	// if (userMissionId == -1) {
	// missionId = guildMission.getMissionId1();
	// num = guildMission.getNum1();
	// } else if (userMissionId == -2) {
	// missionId = guildMission.getMissionId2();
	// num = guildMission.getNum2();
	// } else if (userMissionId == -3) {
	// missionId = guildMission.getMissionId3();
	// num = guildMission.getNum3();
	// } else if (userMissionId == -4) {
	// missionId = guildMission.getMissionId4();
	// num = guildMission.getNum4();
	// } else if (userMissionId == -5) {
	// missionId = guildMission.getMissionId5();
	// num = guildMission.getNum5();
	// }
	//
	// if (missionId == 0) {
	// throw new
	// BaseException(com.youxigu.dynasty2.i18n.MarkupMessages.getString("MissionService_0"));
	// }
	//
	// Mission missionEnt = this.getMissionById(missionId);
	// RandomMissionConfig config = randomMissionConfigMap.get(missionId);
	// if (missionEnt == null || config == null) {
	// throw new
	// BaseException(com.youxigu.dynasty2.i18n.MarkupMessages.getString("MissionService_0"));
	// }
	//
	// UserMission um = new UserMission();
	// um.setMissionId(missionId);
	// um.setUserId(user.getUserId());
	// um.setFactor(1);
	// _doRefreshUserMissionStatus(user, um, missionEnt);
	//
	// if (um.getStatus() != UserMission.STATUS_COMPLETE) {
	// throw new
	// BaseException(com.youxigu.dynasty2.i18n.MarkupMessages.getString("MissionService_31"));
	// }
	//
	// doConsume(user, um, missionEnt);
	//
	// // 发放奖励
	// doRewardMission(user, um, missionEnt);// 发放奖励
	//
	// if (num < config.getNum()) {
	// // 累计次数+1
	// num = num + 1;
	// } else {
	// // 生成新的随机任务
	// Guild guild = guildService.getGuildById(user.getGuildId());
	// Mission newMission = getRandomGuildMission(guild.getLevel(),
	// missionEnt.getMissionHardLevel(), missionEnt);
	// num = 0;
	// missionId = newMission.getMissionId();
	// }
	// if (userMissionId == -1) {
	// guildMission.setMissionId1(missionId);
	// guildMission.setNum1(num);
	// } else if (userMissionId == -2) {
	// guildMission.setMissionId2(missionId);
	// guildMission.setNum2(num);
	// } else if (userMissionId == -3) {
	// guildMission.setMissionId3(missionId);
	// guildMission.setNum3(num);
	// } else if (userMissionId == -4) {
	// guildMission.setMissionId4(missionId);
	// guildMission.setNum4(num);
	// } else if (userMissionId == -5) {
	// guildMission.setMissionId5(missionId);
	// guildMission.setNum5(num);
	// }
	// missionDao.updateGuildMission(guildMission);
	//
	// return guildMission;
	// }
	//
	// /**
	// * 放弃威望任务
	// *
	// * @param user
	// * @param userMissionId
	// */
	// public UserMission doGiveupRandomMission(User user, int userMissionId) {
	//
	// lockUserMission(user.getUserId());
	//
	// UserMission userMission = this.getUserMissionById(userMissionId, user
	// .getUserId());
	//
	// if (userMission == null || userMission.getUserId() != user.getUserId()) {
	// throw new BaseException("没有此任务");
	// }
	//
	// Mission missionEnt = this.getMissionById(userMission.getMissionId());
	// if (missionEnt == null) {
	// throw new BaseException("任务定义不存在");
	// }
	// if (!missionEnt.getMissionType().equals(Mission.MISSION_TYPE_HONOR)) {
	// throw new
	// BaseException(com.youxigu.dynasty2.i18n.MarkupMessages.getString("MissionService_11"));
	// }
	// // 增加执行记录
	// addDailyRandMissionActionNum(user, Mission.MISSION_TYPE_HONOR);
	//
	// // 放弃任务
	// setMissionTLog(missionEnt.getMissionType(), missionEnt.getMissionId(),
	// missionEnt.getMissionName(), 3, user);
	//
	// // 继续生成随机任务
	// Mission mission = getRandomMission(user.getUsrLv(), missionEnt);
	// if (mission != null) {
	//
	// // if
	// // (Mission.MISSION_TYPE_TREASUREHUNT.equals(mission.getMissionType())){
	// // treasureHuntService.generateNewTreasureHunt(user.getUserId());
	// // }
	//
	// userMission.setMissionId(mission.getMissionId());
	// userMission.setNum1(0);
	// userMission.setNum2(0);
	// userMission.setNum3(0);
	// userMission.setNum4(0);
	// userMission.setNum5(0);
	// userMission.setStatus(UserMission.STATUS_NEW);
	// this.updateUserMission(userMission, user, true);
	//
	// doRefreshUserMissionStatus(user, userMission);
	//
	// // 新任务
	// setMissionTLog(missionEnt.getMissionType(), missionEnt
	// .getMissionId(), missionEnt.getMissionName(), 1, user);
	// }
	//
	// return userMission;
	//
	// }
	//
	// /**
	// * 更换声望任务
	// *
	// * @param user
	// * @param userMissionId
	// * @param cashType
	// * @return
	// */
	// public UserMission doChangeRandomMission(User user, int userMissionId) {
	//
	// lockUserMission(user.getUserId());
	//
	// UserMission userMission = this.getUserMissionById(userMissionId, user
	// .getUserId());
	//
	// if (userMission == null || userMission.getUserId() != user.getUserId()) {
	// throw new BaseException("没有此任务");
	// }
	//
	// Mission missionEnt = this.getMissionById(userMission.getMissionId());
	//
	// if (missionEnt == null) {
	// throw new BaseException("任务定义不存在");
	// }
	// if (!missionEnt.getMissionType().equals(Mission.MISSION_TYPE_HONOR)) {
	// throw new
	// BaseException(com.youxigu.dynasty2.i18n.MarkupMessages.getString("MissionService_15"));
	// }
	//
	// if (userMission.getStatus() == UserMission.STATUS_COMPLETE) {
	// throw new
	// BaseException(com.youxigu.dynasty2.i18n.MarkupMessages.getString("MissionService_9"));
	// }
	//
	// // 减元宝
	// // 扣除元宝
	// int cost = getCash4ChangeRandomMission();
	// userService.doConsumeCash(user.getUserId(), -1 * cost,
	// LogCashAct.GENGXINWEIWANG_ACTION);
	//
	// // 放弃任务
	// setMissionTLog(missionEnt.getMissionType(), missionEnt.getMissionId(),
	// missionEnt.getMissionName(), 3, user);
	//
	// // 继续生成随机任务
	// Mission mission = null;
	// int num = 0;
	// do {
	// mission = getRandomMission(user.getUsrLv(), missionEnt);
	// num++;
	// } while (mission.getMissionId() != userMission.getMissionId()
	// && num < 5);
	//
	// if (mission != null) {
	//
	// // if
	// // (Mission.MISSION_TYPE_TREASUREHUNT.equals(mission.getMissionType())){
	// // treasureHuntService.generateNewTreasureHunt(user.getUserId());
	// // }
	//
	// userMission.setMissionId(mission.getMissionId());
	// userMission.setNum1(0);
	// userMission.setNum2(0);
	// userMission.setNum3(0);
	// userMission.setNum4(0);
	// userMission.setNum5(0);
	// if (mission.getMissionId() == 30005) {
	// userMission.setFactor(Util.randInt(4) + 2);
	// } else {
	// userMission.setFactor(Util.randInt(5) + 1);
	// }
	//
	// userMission.setStatus(UserMission.STATUS_NEW);
	// this.updateUserMission(userMission, user, true);
	//
	// doRefreshUserMissionStatus(user, userMission);
	// // 新任务
	// setMissionTLog(missionEnt.getMissionType(), missionEnt
	// .getMissionId(), missionEnt.getMissionName(), 1, user);
	// }
	// return userMission;
	//
	// }
	//
	// /**
	// * 变更声望任务需要消耗的元宝/点券数
	// *
	// * @return
	// */
	// public int getCash4ChangeRandomMission() {
	// return commonService.getSysParaIntValue(
	// AppConstants.MISSION_SPEND_CASH,
	// AppConstants.MISSION_SPEND_CASH_DEFAULT);
	// }
	//
	// /**
	// * 变更声望任务需要消耗的元宝/点券数
	// *
	// * @return
	// */
	// public int getCash4FinishRandomMission() {
	// return commonService.getSysParaIntValue(
	// AppConstants.MISSION_SPEND_FINISH,
	// AppConstants.MISSION_SPEND_FINISH_DEFAULT);
	// }

	/**
	 * 任务奖励
	 * 
	 * @param user
	 * @param mission
	 */
	private void doRewardMission(User user, UserMission userMission,
			Mission mission) {
		if (user == null) {
			throw new BaseException("没有此玩家");
		}
		if (mission == null) {
			throw new BaseException("没有此任务");
		}

		List<MissionAward> awards = mission.getMissionAwards();
		if (awards == null) {
			return;
		}

		for (MissionAward award : awards) {
			IMissionAwardService awardService = missionAwards
					.get(award.getAwardtype());
			if (awardService == null) {
				throw new BaseException("奖励定义错误：" + award.getAwardtype());
			}
			awardService.doAward(user, award, mission, userMission);
		}

	}

	/**
	 * 任务是否完成
	 * 
	 * @param uMission
	 * @param mission
	 * @return
	 */
	private boolean missionIsFinished(User user, UserMission uMission,
			Mission mission) {

		Iterator<Map.Entry<MissionLimit, Integer>> lit = mission
				.getLimitIndexs().entrySet().iterator();
		while (lit.hasNext()) {
			Map.Entry<MissionLimit, Integer> entry = lit.next();
			MissionLimit limit = entry.getKey();
			// limit.getLimitChecker().get
			int index = entry.getValue();
			if (limit.getLimitChecker().getLimitEntNum(user, uMission, mission,
					limit) > uMission.getNum(index)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public void notifyMissionModule(User usr, String otcType, int entId,
			int num) {
		long time = System.currentTimeMillis();
		// 这个方法在各个游戏逻辑中调用，这里加锁很容易产生死锁
		// 先去掉锁，
		// lockUserMission(usr.getUserId());
		List<UserMission> usermissions = missionDao
				.getUserMissionByUserId(usr.getUserId());
		for (UserMission userMission : usermissions) {
			// if (userMission.getStatus() == UserMission.STATUS_NEW) {
			if (userMission.getStatus() != UserMission.STATUS_COMMIT) {
				Mission mission = this
						.getMissionById(userMission.getMissionId());
				if (mission == null) {
					continue;
				}
				List<MissionLimit> limits = mission.getLimitsByOctType(otcType);

				if (limits == null) {
					continue;
				}

				Iterator<MissionLimit> lit = limits.iterator();
				boolean changed = false;
				while (lit.hasNext()) {
					MissionLimit limit = lit.next();
					if (entId == limit.getEntId()) {
						boolean chg = limit.getLimitChecker().check(usr,
								userMission, mission, limit, entId, num);
						if (chg) {
							changed = true;
						}
					}
				}

				if (changed) {
					int oldStatus = userMission.getStatus();
					if (this.missionIsFinished(usr, userMission, mission)) {
						//
						if (userMission
								.getStatus() != UserMission.STATUS_COMPLETE) {
							// setMissionTLog(mission.getMissionType(),
							// mission.getMissionId(),
							// mission.getMissionName(), 2, usr);
						}
						userMission.setStatus(UserMission.STATUS_COMPLETE);

					} else {
						userMission.setStatus(UserMission.STATUS_NEW);
					}

					logService.log(LogBeanFactory.buildMissionLog(usr,
							mission.getMissionType(), mission.getMissionId(),
							mission.getMissionName(), userMission.getStatus()));

					// 前台要改变任务完成条件的数字，即使没有完成任务，只要变化了，就要退消息
					if (oldStatus == UserMission.STATUS_COMPLETE && userMission
							.getStatus() == UserMission.STATUS_COMPLETE) {
						this.updateUserMission(userMission, usr, false);
					} else {
						this.updateUserMission(userMission, usr, true);
					}
					// this.updateUserMission(userMission, usr, true);

				}
			}
		}

		// 异步通知世界任务
		worldMissionClientService.notifyMissionModule(otcType, entId, num);

		if (log.isDebugEnabled()) {
			log.debug("missionService.notifyMissionModule time="
					+ (System.currentTimeMillis() - time));
		}

	}

	@Override
	public void notifyMissionModule(Object sender, Map<String, Object> param) {
		User usr = (User) sender;

		String otcType = (String) param.get("otc");
		int entId = (Integer) param.get("entId");
		int num = Integer.MIN_VALUE;
		Object obj = param.get("num");
		if (obj != null) {
			num = (Integer) obj;
		}
		notifyMissionModule(usr, otcType, entId, num);

	}

	@Override
	public boolean doRefreshUserMissionStatus(User user,
			UserMission userMission) {
		boolean isChange = false;
		Mission mission = this.getMissionById(userMission.getMissionId());
		if (mission != null) {
			isChange = _doRefreshUserMissionStatus(user, userMission, mission);
			if (isChange) {
				this.updateUserMission(userMission, user, true);
			}
		}
		return isChange;
	}

	private boolean _doRefreshUserMissionStatus(User user,
			UserMission userMission, Mission mission) {

		// Mission mission = this.getMissionById(userMission.getMissionId());
		Collection<MissionLimit> limits = mission.getLimitIndexs().keySet();

		if (limits == null || limits.size() == 0) {
			return false;
		}

		Iterator<MissionLimit> lit = limits.iterator();
		boolean changed = false;
		int num = Integer.MIN_VALUE;
		while (lit.hasNext()) {
			MissionLimit limit = lit.next();
			int entId = limit.getEntId();

			boolean chg = limit.getLimitChecker().check(user, userMission,
					mission, limit, entId, num);
			if (chg) {
				changed = true;
			}

		}

		if (changed) {
			if (this.missionIsFinished(user, userMission, mission)) {
				if (userMission.getStatus() == UserMission.STATUS_NEW) {
					userMission.setStatus(UserMission.STATUS_COMPLETE);

					// setMissionTLog(mission.getMissionType(),
					// mission.getMissionId(), mission.getMissionName(), 2,
					// user);

					logService.log(LogBeanFactory.buildMissionLog(user,
							mission.getMissionType(), mission.getMissionId(),
							mission.getMissionName(),
							UserMission.STATUS_COMPLETE));

					// // 通知前台任务完成
					// if (!notNotify) {
					// sendMissionEvent(userMission.getUserId(),
					// EventMessage.TYPE_FINISH_MISSION,
					// userMission.getUserMissionId());
					// }

					// //冒险任务完成--通知冒险模块
					// if (Mission.MISSION_TYPE_RISK.equalsIgnoreCase(
					// mission.getMissionType())) {
					// riskService.finishUserRiskMission(userMission.getUserId(),
					// userMission.getMissionId());
					// }
					//
					if (log.isDebugEnabled()) {
						log.debug("user:{}的任务{}完成", userMission.getUserId(),
								userMission.getMissionId());
					}
				}
			} else {
				if (userMission.getStatus() == UserMission.STATUS_COMPLETE) {
					userMission.setStatus(UserMission.STATUS_NEW);
					// if (mission.getMissionId() ==
					// AppConstants.MISSION_EVPMISSION_ID) {
					// // 特殊处理的NPC任务
					// // 如果Armyout中没有该出征的NPC,则自动出征
					// _startEVPMission(mission, user, true);
					// }
				}
			}
		}
		// if (mission.getMissionId() == AppConstants.MISSION_EVPMISSION_ID
		// && userMission.getStatus() == UserMission.STATUS_NEW) {
		//
		// _startEVPMission(mission, user, true);
		// }
		return changed;

	}

	@Override
	public void doInitUserMission(User usr) {
		doAddUserMission(usr);

		// // 随机声望任务
		// doInitHonorRandomMission(usr);
	}

	// /**
	// * 初始化随机声望任务
	// *
	// * @param usr
	// * @return
	// */
	// public UserMission doInitHonorRandomMission(User usr) {
	// lockUserMission(usr.getUserId());
	// // 初始化 随机任务（声望任务）
	// Mission targetMission = getRandomMission(usr.getUsrLv(), null);
	// if (targetMission != null) {
	// // if (targetMission != null) {//不可能为null
	// UserMission newMission = new UserMission();
	// newMission.setUserId(usr.getUserId());
	// newMission.setMissionId(targetMission.getMissionId());
	// newMission.setStatus(UserMission.STATUS_NEW);// 表示没完成
	// newMission.setCompleteLimitTime(new Timestamp(System
	// .currentTimeMillis()));
	// if (targetMission.getMissionId() == 30005) {
	// // newMission.setFactor(Util.randInt(4) + 2);
	// // 第一个任务强制难度为1
	// newMission.setFactor(1);
	// } else {
	// newMission.setFactor(Util.randInt(5) + 1);
	// }
	//
	// missionDao.insertUserMission(newMission);
	// setMissionTLog(targetMission.getMissionType(), targetMission
	// .getMissionId(), targetMission.getMissionName(), 1, usr);
	//
	// // 推生成的威望任务
	// this.sendMissionEvent(usr, EventMessage.TYPE_FRESH_MISSION,
	// newMission);
	// // if
	// //
	// (Mission.MISSION_TYPE_TREASUREHUNT.equals(targetMission.getMissionType())){
	// // treasureHuntService.generateNewTreasureHunt(usr.getUserId());
	// // }
	//
	// // doRefreshUserMissionStatus(usr, newMission);
	//
	// return newMission;
	// }
	// return null;
	// }

	// /**
	// * 初始化随机联盟任务
	// *
	// * @param guild
	// * @return
	// */
	// @Deprecated
	// public GuildMission doInitGuildRandomMission(Guild guild) {
	// lockGuildMission(guild.getGuildId());
	// GuildMission gm = missionDao.getGuildMission(guild.getGuildId());
	// if (gm == null) {
	// List<Mission> missions = getRandomGuildMissions(guild.getLevel());
	// gm = new GuildMission();
	// gm.setGuildId(guild.getGuildId());
	// int num = missions.size();
	// if (num > 0) {
	// int missionId = missions.get(0).getMissionId();
	// gm.setMissionId1(missionId);
	// }
	//
	// if (num > 1) {
	// int missionId = missions.get(1).getMissionId();
	// gm.setMissionId2(missionId);
	// }
	//
	// if (num > 2) {
	// int missionId = missions.get(2).getMissionId();
	// gm.setMissionId3(missionId);
	// }
	//
	// if (num > 3) {
	// int missionId = missions.get(3).getMissionId();
	// gm.setMissionId4(missionId);
	// }
	//
	// if (num > 4) {
	// int missionId = missions.get(4).getMissionId();
	// gm.setMissionId5(missionId);
	// }
	//
	// missionDao.createGuildMission(gm);
	// }
	// return gm;
	// }

	@Override
	public void doAddUserMission(User usr) {
		lockUserMission(usr.getUserId());
		// Castle castle = null;
		Iterator<Mission> lit = rootMissionMap.values().iterator();
		while (lit.hasNext()) {
			Mission mission = lit.next();
			MissionType type = missionTypeMap
					.get(mission.getMissionChildType());
			if (type != null) {
				if (type.getLimitType() == MissionType.LIMIT_TYPE_USER_LV) {
					// 用户等级约束
					if (usr.getUsrLv() != type.getLimitValue()) {
						continue;
					}
				}

				// else if (type.getLimitType() ==
				// MissionType.LIMIT_TYPE_CASTLE_LV) {
				// // 城池等级约束
				// //TODO:由于城池有降级情况，这里会有问题，可能会重复产生任务
				// if (castle == null) {
				// castle = castleService.getCastleById(usr
				// .getMainCastleId());
				// }
				// if (castle == null) {
				// continue;
				// }
				// if (castle.getCasLv() != type.getLimitValue()) {
				// continue;
				// }
				// }
			}

			// 初始化根任务
			_createUserMission(usr, mission, true);
			// doRefreshUserMissionStatus(usr, newMission);
		}
	}

	@Override
	public void doAddUserLevelMission(User usr, int oldLv, int newLv) {
		if(oldLv >= newLv){
			return;
		}
		for (int missionLv = newLv; missionLv > oldLv; missionLv--) {
			List<Mission> list = usrlv4MissionMap.get(missionLv);
			if (list != null && list.size() > 0) {
				for (Mission mission : list) {
					// 初始化根任务
					_createUserMission(usr, mission, true);
				}
			}
		}

		// Iterator<Mission> lit = missionMap.values().iterator();
		// lockUserMission(usr.getUserId());
		// while (lit.hasNext()) {
		// Mission mission = lit.next();
		// int missionLv = mission.getMissionLevel();
		// if (missionLv > oldLv && missionLv <= newLv
		// && mission.getParentMissionId() == -2) {
		//
		// // 初始化根任务
		// _createUserMission(usr, mission, true);
		// }
		// // doRefreshUserMissionStatus(usr, newMission);
		// }

		// int userLv = commonService.getSysParaIntValue(
		// AppConstants.MISSION_HONOR_OPEN_USERLV, 10);
		// if (oldLv < userLv && newLv >= userLv) {
		// // 初始化威望任务
		// this.doInitHonorRandomMission(usr);
		// }
	}

	// public UserMission createUserMissionByNpcId(long userId, int
	// issueMissionId) {
	// User user = userService.getUserById(userId);
	// return createUserMissionByNpcId(user, issueMissionId);
	// }

	// public UserMission createUserMissionByNpcId(User usr, int issueMissionId)
	// {
	//
	// Mission mission = sceneIdMissionMap.get(issueMissionId);
	// if (mission != null) {
	// return _createUserMission(usr, mission, true);
	// }
	// return null;
	// }

	@Override
	public UserMission createUserMission(long userId, int missionId) {
		User user = userService.getUserById(userId);
		Mission mission = this.getMissionById(missionId);
		if (user == null) {
			throw new BaseException("用户不存在");
		}
		if (mission == null) {
			throw new BaseException("任务定义不存在");
		}
		return createUserMission(user, mission);
	}

	@Override
	public UserMission createUserMission(User user, int missionId) {
		Mission mission = this.getMissionById(missionId);
		if (mission == null) {
			throw new BaseException("任务定义不存在");
		}
		return createUserMission(user, mission);
	}

	@Override
	public UserMission createUserMission(User user, Mission mission) {

		UserMission um = _createUserMission(user, mission, true);
		return um;
	}

	// /**
	// * 记录任务tlog
	// */
	// private void setMissionTLog(String missionType, int missionId,
	// String missionName, int flag, User user) {
	// Date now = new Date();
	// long userId = user.getUserId();
	// int usrLv = user.getUsrLv();
	// // Account account = accountService.getAccount(user.getAccId());
	// String via = null;
	// String areaId = null;
	// String openId = null;
	// String pf = null;
	// String ip = null;
	// UserSession us = OnlineUserSessionManager
	// .getUserSessionByUserId(userId);
	// if (us != null) {
	// via = us.getVia();
	// areaId = us.getAreaId();
	// openId = us.getOpenid();
	// pf = us.getPfEx();
	// ip = us.getUserip();
	// } else {
	// Account account = accountService.getAccount(user.getAccId());
	// via = account.getVia();
	// areaId = account.getAreaId();
	// openId = account.getAccName();
	// pf = Constant.getPfEx(account.getPf());
	// ip = account.getLoginIp();
	// }
	//
	// logService.log(AbsLogLineBuffer
	// .getBuffer(areaId, TLogTable.TLOG_MISSION.getName())
	// .append(missionType).append(userId).append(usrLv)
	// .append(missionId).append(missionName).append(now).append(flag)
	// .append(via).append(user.getCreateDate()));
	//
	// // <struct name="MissionFlow" version="1" desc="(必填)任务数据流水">
	// // <entry name="vGameSvrId" type="string" size="25"
	// // desc="(必填)登录的游戏服务器编号" />
	// // <entry name="dtEventTime" type="datetime"
	// // desc="(必填)游戏事件的时间, 格式 YYYY-MM-DD HH:MM:SS" />
	// // <entry name="vGameAppid" type="string" size="32" desc="(必填)游戏APPID"
	// // />
	// // <entry name="iPlatID" type="int" desc="(必填)ios 0/android 1"/>
	// // <entry name="iZoneID" type="int"
	// // desc="(必填)针对分区分服的游戏填写分区id，用来唯一标示一个区；非分区分服游戏请填写0"/>
	// // <entry name="vopenid" type="string" size="64" desc="(必填)玩家" />
	// // <entry name="vMissionType" type="string" size="64" desc="(必填)任务类型" />
	// // <entry name="iMissionId" type="int" desc="任务id" />
	// // </struct>
	// AbsLogLineBuffer buf = TlogHeadUtil
	// .getTlogHead(LogTypeConst.TLOG_TYPE_MissionFlow, user);
	// tlogService.log(
	// buf.appendLegacy(missionType).append(missionId).append(flag));
	// }

	private UserMission _createUserMission(User user, Mission mission,
			boolean checkOtherMissionComplete) {
		if (mission.getMissionType().equals(Mission.MISSION_TYPE_WORLD)) {
			// 过滤掉世界任务
			return null;
		}

		lockUserMission(user.getUserId());

		UserMission userMission = missionDao.getUserMissionByMissionId(
				user.getUserId(), mission.getMissionId());
		if (userMission != null) {
			return userMission;
		}
		userMission = new UserMission();
		userMission.setUserId(user.getUserId());
		userMission.setMissionId(mission.getMissionId());
		userMission.setFactor(1);
		userMission.setStatus(UserMission.STATUS_NEW);// 表示没完成
		userMission.setCompleteLimitTime(
				new Timestamp(System.currentTimeMillis()));
		missionDao.insertUserMission(userMission);

		// setMissionTLog(mission.getMissionType(), mission.getMissionId(),
		// mission.getMissionName(), 1, user);

		logService.log(LogBeanFactory.buildMissionLog(user,
				mission.getMissionType(), mission.getMissionId(),
				mission.getMissionName(), UserMission.STATUS_NEW));

		// 任务可能刚创建就已经达到完成状态
		boolean isChange = doRefreshUserMissionStatus(user, userMission);

		if (!isChange) {// 未更改
			// 通知客户端刷新任务
			sendMissionEvent(user, EventMessage.TYPE_FRESH_MISSION,
					userMission);
		}

		// UserMission userMission = this.getUserMissionByMissionId(user
		// .getUserId(), mission.getMissionId());
		// if (userMission == null) {// 新任务
		// userMission = new UserMission();
		// userMission.setUserId(user.getUserId());
		// userMission.setMissionId(mission.getMissionId());
		// userMission.setFactor(1);
		// userMission.setStatus(UserMission.STATUS_NEW);// 表示没完成
		// userMission.setCompleteLimitTime(new Timestamp(System
		// .currentTimeMillis()));
		// missionDao.insertUserMission(userMission);
		//
		// // 任务可能刚创建就已经达到完成状态
		// boolean isChange = doRefreshUserMissionStatus(user, userMission);
		//
		// if (!isChange) {// 未更改
		// // 通知客户端刷新任务
		// sendMissionEvent(user, EventMessage.TYPE_FRESH_MISSION,
		// userMission);
		// }
		//
		// } else if (userMission.getStatus() == UserMission.STATUS_COMMIT) {
		// // 更新状态
		// userMission.setStatus(UserMission.STATUS_NEW);
		// userMission.setNum1(0);
		// userMission.setNum2(0);
		// userMission.setNum3(0);
		// userMission.setNum4(0);
		// userMission.setNum5(0);
		// userMission.setFactor(1);
		// this.updateUserMission(userMission, user, true);
		// if (mission.getMissionId() == AppConstants.MISSION_EVPMISSION_ID) {
		// _startEVPMission(mission, user, true);
		// }
		//
		// // 任务可能刚创建就已经达到完成状态
		// // doRefreshUserMissionStatus(user, userMission);
		//
		// } else {
		// log.error("任务已经存在，并且未提交");
		// }

		// if (checkOtherMissionComplete) {
		// this.notifyMissionModule(user, Mission.QCT_MISSION, userMission
		// .getMissionId(), 1);
		// }

		return userMission;
	}

	// /**
	// * 特殊处理EVP任务
	// *
	// * @param mission
	// * @param user
	// */
	// private void _startEVPMission(Mission mission, User user,
	// boolean checkArmyout) {
	// // /TODO:特殊处理EVP任务
	// // 检查Armyout,看看
	// List<ArmyOut> armyous = combatService
	// .getArmyOutsByCasId(user.getMainCastleId());
	// boolean find = false;
	// if (armyous != null) {
	// for (ArmyOut armyout : armyous) {
	// if (armyout.getAttackerType() == ArmyOut.TYPE_COMPUTER
	// && armyout
	// .getAttackerId() == AppConstants.MISSION_EVPMISSION_NPC_ID) {
	// find = true;
	// }
	// }
	// }
	// if (!find) {
	// combatService.doStartNPCArmyOutJob(
	// AppConstants.MISSION_EVPMISSION_NPC_ID, user);
	// }
	// }

	/**
	 * 通知前台任务发生变化
	 * 
	 * @param userMission
	 * @param user
	 */
	private void updateUserMission(UserMission userMission, User user,
			boolean sendMessage) {
		missionDao.updateUserMission(userMission);

		if (sendMessage) {
			// 通知客户端刷新任务
			sendMissionEvent(user, EventMessage.TYPE_FRESH_MISSION,
					userMission);
		}
	}

	// /**
	// * 取得随机声望任务
	// *
	// * @param userLv
	// * @return
	// */
	// private Mission getRandomMission(int userLv, Mission oldMission) {
	// // int level = (userLv - 1) / 5 + 1;
	// int level = (int) ((userLv - 1) / 5.4d + 1);
	// List<Mission> missions = honorRandomMissionMap.get(level);
	// if (missions == null) {
	// missions = honorRandomMissionMap.get(randomMissionConfig_maxLv);
	// }
	// return _getRandomMission(missions, oldMission);
	// }
	//
	// /**
	// * 按联盟等级/任务难度等级随机取得联盟任务
	// *
	// * @param guildLv
	// * @param hardLv
	// * @return
	// */
	// private Mission getRandomGuildMission(int guildLv, int hardLv,
	// Mission oldMission) {
	// Map<Integer, List<Mission>> subMaps = guildRandomMissionMap
	// .get((guildLv - 1) / 5 + 1);
	// if (subMaps == null) {
	// subMaps = guildRandomMissionMap.get(guildMissionConfig_maxLv);
	// }
	// if (subMaps != null) {
	// List<Mission> missions = subMaps.get(hardLv);
	// if (missions != null) {
	// return _getRandomMission(missions, oldMission);
	// }
	// }
	// return null;
	// }
	//
	// private Mission _getRandomMission(List<Mission> missions, Mission
	// oldMission) {
	// int count = missions == null ? 0 : missions.size();
	// if (count > 1) {
	// if (oldMission == null) {
	// return missions.get(Util.randInt(count));
	// } else {
	// int num = 0;
	// while (num < 5) {
	// Mission m = missions.get(Util.randInt(count));
	// if (m.getMissionId() != oldMission.getMissionId()) {
	// return m;
	// }
	// num++;
	// }
	// }
	// } else if (count == 1) {
	// if (oldMission != null) {
	// return oldMission;
	// } else {
	// return missions.get(0);
	// }
	// }
	// if (oldMission != null) {
	// return oldMission;
	// }
	// return null;
	// }
	//
	// /**
	// * 按联盟等级随机取联盟任务，每个难度等级取一个
	// *
	// * @param guildLv
	// * @return
	// */
	// private List<Mission> getRandomGuildMissions(int guildLv) {
	// List<Mission> returnMissions = new ArrayList<Mission>();
	// Map<Integer, List<Mission>> subMaps = guildRandomMissionMap
	// .get((guildLv - 1) / 5 + 1);
	// if (subMaps == null) {
	// subMaps = guildRandomMissionMap.get(guildMissionConfig_maxLv);
	// }
	// Iterator<List<Mission>> lit = subMaps.values().iterator();
	// while (lit.hasNext()) {
	// List<Mission> missions = lit.next();
	// returnMissions.add(missions.get(Util.randInt(missions.size())));
	// }
	// return returnMissions;
	// }

	// private UserMission lockUserMission(UserMission usermission) {
	// try {
	// return (UserMission) MemcachedManager.lockObject(usermission,
	// usermission.getUserId(), usermission.getMissionId());
	// } catch (TimeoutException e) {
	// throw new BaseException(e.toString());
	// }
	// }

	// @Override
	// public UserMission doRefreshHonorMissionStar(long userId, int
	// userMissionId) {
	//
	// lockUserMission(userId);
	// UserMission userMission = this
	// .getUserMissionById(userMissionId, userId);
	//
	// if (userMission == null || userMission.getUserId() != userId) {
	// throw new BaseException("没有此任务");
	// }
	// Mission missionEnt = this.getMissionById(userMission.getMissionId());
	// if (missionEnt == null) {
	// throw new BaseException("任务定义不存在");
	// }
	// if (!Mission.MISSION_TYPE_HONOR.equals(missionEnt.getMissionType())) {
	// throw new
	// BaseException(com.youxigu.dynasty2.i18n.MarkupMessages.getString("MissionService_26"));
	// }
	//
	// if (userMission.getStatus() == UserMission.STATUS_COMMIT) {// 可交任务状态
	// throw new
	// BaseException(com.youxigu.dynasty2.i18n.MarkupMessages.getString("MissionService_1"));
	// }
	//
	// userService.doConsumeCash(userId, -10, LogCashAct.SHUAXINWEIWANG_ACTION);
	//
	// int oldFactor = userMission.getFactor();
	// User user = userService.getUserById(userId);
	//
	// int factor = 1;
	// if (missionEnt.getMissionId() == 30005) {
	// factor = Util.randInt(4) + 2;
	// } else {
	// factor = Util.randInt(5) + 1;
	// }
	//
	// if (factor == oldFactor) {
	// if (factor == 5) {
	// factor = 4;
	// } else {
	// factor++;
	// }
	// }
	//
	// userMission.setFactor(factor);
	// int oldStatus = userMission.getStatus();
	// if (this.missionIsFinished(user, userMission, missionEnt)) {
	// userMission.setStatus(UserMission.STATUS_COMPLETE);
	// setMissionTLog(missionEnt.getMissionType(), missionEnt
	// .getMissionId(), missionEnt.getMissionName(), 2, user);
	// } else {
	// userMission.setStatus(UserMission.STATUS_NEW);
	// }
	//
	// // if (oldStatus == UserMission.STATUS_COMPLETE
	// // && oldStatus == userMission.getStatus()) {
	// // this.updateUserMission(userMission, user, false);
	// // } else {
	// // this.updateUserMission(userMission, user, true);
	// // }
	// this.updateUserMission(userMission, user, true);
	//
	// // 再次判断任务是否完毕
	// // _doRefreshUserMissionStatus(user, userMission, missionEnt);
	//
	// return null;
	// }

	@Override
	public void lockUserMission(long userId) {
		try {
			MemcachedManager.lock(MISSION_LOCK_KEY_PREFIX + userId);
		} catch (TimeoutException e) {
			throw new BaseException(e);
		}
	}

	private void lockGuildMission(long guildId) {
		try {
			MemcachedManager.lock(GUILD_MISSION_LOCK_KEY_PREFIX + guildId);
		} catch (TimeoutException e) {
			throw new BaseException(e);
		}
	}

	// /**
	// * 通知声望任务每日最大次数变更
	// *
	// * @param user
	// */
	// public void notifyChangeHonorMissionDailyCount(User user) {
	// List<UserMission> usermissions = missionDao.getUserMissionByUserId(user
	// .getUserId());
	// for (UserMission userMission : usermissions) {
	// Mission mission = this.getMissionById(userMission.getMissionId());
	// if (mission != null
	// && Mission.MISSION_TYPE_HONOR.equals(mission
	// .getMissionType())) {
	//
	// this.sendMissionEvent(user, EventMessage.TYPE_FRESH_MISSION,
	// userMission);
	// // continue;
	// }
	// }
	// }

	// @Override
	// public UserExp doGetUserExpByUserId(long userId){
	// //TODO 这里取数据的时候，可能存在数据过期需要更新的操作，所以加上事务，不知道有没有更好的办法
	// this.lockUserMission(userId);
	// UserExp exp = missionDao.getUserExp(userId);
	// if(exp == null){
	// exp = new UserExp();
	// exp.setUserId(userId);
	// exp.setExecuteDateStr(DateUtils.date2Text(new Date()));
	// //第一次需生成一组君主任务
	// exp.setMissionList(this.randomUserExpMissions());
	// missionDao.insertUserExp(exp);
	// return exp;
	// }
	// if(!exp.isTodayData()){
	// //最后执行君主任务的时间不是今日，所以需重置数据并生成新的一组新的君主任务
	// exp.setAwardStr("");
	// exp.setExecuteDateStr(DateUtils.date2Text(new Date()));
	// exp.setRefNum(0);
	// exp.setScore(0);
	// exp.setTaskNum(0);
	// exp.setMissionList(this.randomUserExpMissions());
	// missionDao.updateUserExp(exp);
	// }
	// return exp;
	// }
	//
	// @Override
	// public UserExp doRefreshUserExpMission(long userId) {
	// int refNum = commonService.getSysParaIntValue(
	// AppConstants.DAILY_MISSION_MAX_FREE_REFRESH_CNT, 0);
	// int refCash = commonService.getSysParaIntValue(
	// AppConstants.DAILY_MISSION_REFRESH_INGOT_CONSUME, 20);
	// this.lockUserMission(userId);
	// UserExp exp = doGetUserExpByUserId(userId);
	// if(exp.getRefNum() >= refNum){
	// //免费次数用完
	// userService.doConsumeCash(userId, -refCash,
	// LogCashAct.JUNZHURENWU_ACTION);
	// }
	// exp.setMissionList(this.randomUserExpMissions());
	// exp.setRefNum(exp.getRefNum() + 1);
	// missionDao.updateUserExp(exp);
	// this.userMissionTLog(userId,
	// com.youxigu.dynasty2.i18n.MarkupMessages.getString("MissionService_34"),0,
	// 0, exp.getRefNum(), exp.getTaskNum());
	// return exp;
	// }
	//
	// /**
	// * 随机一组君主任务
	// * @return
	// */
	// private List<Map<String, Integer>> randomUserExpMissions(){
	// int missionNum = commonService.getEnumIntValueById
	// (AppConstants.ENUMER_USER_MISSION_NUM, 3);
	// List<Map<String, Integer>> list = new ArrayList<Map<String,Integer>>();
	// List<UserExpTemp> exps = new ArrayList<UserExpTemp>(userExps.values());
	// int sumWeight = userMissiomSumWeight;
	// for(int i = 0; i < missionNum; i++){
	// int random = Util.randInt(sumWeight);
	// int weightTmp = 0;
	// //遍历判断权重
	// for(UserExpTemp exp : exps){
	// if(random >= weightTmp && random < exp.getWeight() + weightTmp){
	// Map<String, Integer> map = new HashMap<String, Integer>();
	// map.put("id", exp.getId());//任务id
	// map.put("status", 0);//刚随机出的任务，状态为未执行
	// list.add(map);
	// exps.remove(exp);//随机出来的任务移除掉，防止重复
	// sumWeight -= exp.getWeight();
	// break;
	// }
	// weightTmp += exp.getWeight();
	// }
	// }
	// return list;
	// }
	//
	//
	// @Override
	// public UserExp doExecUserExpMission(long userId, int taskId) {
	// int taskLimit = commonService.getSysParaIntValue(
	// AppConstants.DAILY_MISSION_MAX_EXECUTE_CNT_ONEDAY, 0);
	// UserExpTemp temp = this.userExps.get(taskId);
	// if(temp == null){
	// throw new
	// BaseException(com.youxigu.dynasty2.i18n.MarkupMessages.getString("MissionService_19"));
	// }
	// this.lockUserMission(userId);
	// UserExp exp = doGetUserExpByUserId(userId);
	// if(exp.getTaskNum() >= taskLimit){
	// throw new
	// BaseException(com.youxigu.dynasty2.i18n.MarkupMessages.getString("MissionService_27"));
	// }
	// List<Map<String, Integer>> missionList = exp.getMissionList();
	// if(Util.isEmpty(missionList)){
	// throw new
	// BaseException(com.youxigu.dynasty2.i18n.MarkupMessages.getString("MissionService_12"));
	// }
	// for(Map<String, Integer> map : missionList){
	// Integer mapMid = map.get("id");
	// if(mapMid != null && mapMid == taskId){
	// Integer status = map.get("status");
	// if(status == 0){
	// //任务可执行
	// exp.setTaskNum(exp.getTaskNum() + 1);//执行任务次数加1
	// map.put("status", 1);//设置为已执行
	// exp.setMissionList(missionList);
	// exp.setScore(exp.getScore() + temp.getScore());//增加积分
	// missionDao.updateUserExp(exp);
	// userService.updateUserHonor(userId,
	// temp.getExp(),LogUserExpAct.User_EXP_6);
	// this.userMissionTLog(userId,
	// com.youxigu.dynasty2.i18n.MarkupMessages.getString("MissionService_18"),taskId,
	// 0, exp.getRefNum(), exp.getTaskNum());
	//
	// userDailyActivityService.addUserDailyActivity(userId,
	// DailyActivity.ACT_DAILY_MISSION, (byte) 1);
	// linePlatformFriendService.doNotifyLineFriend(userId,
	// FriendRankEvent.EVENT_2);
	// return exp;
	// }else{
	// throw new
	// BaseException(com.youxigu.dynasty2.i18n.MarkupMessages.getString("MissionService_24"));
	// }
	// }
	// }
	// throw new
	// BaseException(com.youxigu.dynasty2.i18n.MarkupMessages.getString("MissionService_23"));
	// }
	//
	// @Override
	// public void doGetUserExpMissionAward(long userId, int score) {
	// UserExpScoreTemp scoreTemp = this.userExpScores.get(score);
	// if(scoreTemp == null){
	// throw new
	// BaseException(com.youxigu.dynasty2.i18n.MarkupMessages.getString("MissionService_32"));
	// }
	// this.lockUserMission(userId);
	// UserExp exp = doGetUserExpByUserId(userId);
	// if(exp.getScore() < score){
	// throw new
	// BaseException(com.youxigu.dynasty2.i18n.MarkupMessages.getString("MissionService_8"));
	// }
	// if(exp.getAwardStatus(score) != 0){
	// throw new
	// BaseException(com.youxigu.dynasty2.i18n.MarkupMessages.getString("MissionService_5"));
	// }
	// exp.award(score);//标记为已领取
	// missionDao.updateUserExp(exp);
	// //发奖
	// treasuryService.lockTreasury(userId);
	// Map<Integer, Integer> itemMap = scoreTemp.getItemMap();
	// for(Map.Entry<Integer, Integer> award : itemMap.entrySet()){
	// treasuryService.addItemToTreasury(userId, award.getKey(),
	// award.getValue(), 1, 1, true,
	// false,com.youxigu.dynasty2.log.imp.LogItemAct.LOGITEMACT_66);
	// }
	// this.userMissionTLog(userId,
	// com.youxigu.dynasty2.i18n.MarkupMessages.getString("MissionService_2"),
	// 0, score, exp.getRefNum(), exp.getTaskNum());
	// }
	//
	// @Override
	// public Set<Integer> getuserExpScoresKeySet() {
	// return this.userExpScores.keySet();
	// }

	// private void userMissionTLog(long userId, String logType, int missionId,
	// int scoreAwardId, int refNum, int execNum) {
	// User user = userService.getUserById(userId);
	// String areaId = null;
	// UserSession us = OnlineUserSessionManager
	// .getUserSessionByUserId(userId);
	// if (us != null) {
	// areaId = us.getAreaId();
	// } else {
	// Account account = accountService.getAccount(user.getAccId());
	// areaId = account.getAreaId();
	// }
	//
	// logService
	// .log(AbsLogLineBuffer
	// .getBuffer(areaId,
	// TLogTable.TLOG_USER_EXP_MISSION.getName())
	// .append(userId).append(user.getUserName())
	// .append(user.getUsrLv()).append(logType)
	// .append(missionId).append(scoreAwardId).append(refNum)
	// .append(execNum).append(new Date()));
	//
	// }
}

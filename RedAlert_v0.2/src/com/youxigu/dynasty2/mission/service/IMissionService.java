package com.youxigu.dynasty2.mission.service;

import java.util.List;
import java.util.Map;

import com.youxigu.dynasty2.mission.domain.Mission;
import com.youxigu.dynasty2.mission.domain.MissionLimit;
import com.youxigu.dynasty2.mission.domain.UserMission;
import com.youxigu.dynasty2.mission.proto.UserMissionView;
import com.youxigu.dynasty2.user.domain.User;

/**
 * 任务接口
 * 
 * @author Dagangzi
 * @date 2016年1月8日
 */
public interface IMissionService {
	/**
	 * 根据任务Id获得任务实体
	 * 
	 * @param id
	 * @return
	 */
	Mission getMissionById(int id);

	/**
	 * 取得奖励接口
	 * 
	 * @param awardtype
	 * @return
	 */
	IMissionAwardService getAwardService(String awardtype);

	/**
	 * 分类任务列表
	 * 
	 * @param type
	 * @return
	 */
	List<Mission> getMissionByType(String type);

	/**
	 * 根任务
	 * 
	 * @return
	 */
	Map<Integer, Mission> getRootMission();

	/**
	 * 根据任务完成条件Id获得任务完成条件实体
	 * 
	 * @param id
	 * @return
	 */
	MissionLimit getMissionLimitById(int id);

	/**
	 * 根据Id查询玩家任务
	 * 
	 * @param id
	 * @param userId
	 * @return
	 */
	UserMission getUserMissionById(int id, long userId);

	/**
	 * 查询玩家任务列表
	 * 
	 * @param userId
	 * @return
	 */
	List<UserMissionView> doRefreshAndGetUserMissionByUserId(User user,
			String missionType);

	/**
	 * 取得完成未领奖的君主任务数量
	 * 
	 * @param userId
	 * @param missionType
	 * @return
	 */
	int getFinishUserMissionNum(long userId, String missionType);

	// /**
	// * 联盟任务的Id为 -1,-2,-3,-4,-5
	// *
	// * @param user
	// * @return
	// */
	// @Deprecated
	// List<UserMissionView> doGetUserRandomGuildMissions(User user);

	/**
	 * 根据missionId查询任务
	 * 
	 * @param param
	 * @return
	 */
	UserMission getUserMissionByMissionId(long userId, int missionId);
	// /**
	// * 接任务,
	// *
	// * 活动任务玩家接任务后就完成
	// *
	// *
	// * 其他任务不是由玩家主动接任务，而是后台在完成一个任务后，自动生成后续任务
	// *
	// * @param userId
	// * @param missionId
	// */
	// private void aquireMission(long userId, int missionId) {
	// Mission mission = this.getMissionById(missionId);
	// User user = userService.getUserById(userId);
	// aquireMission(user, mission);
	// }
	//
	// /**
	// * 日常任务的每日最大可接次数
	// *
	// * @param user
	// * @param missionType
	// * @return
	// */
	// int getDailyMissionMaxNum(User user, String missionType);
	//
	//
	// UserDailyMissionNum doRefreshAndGetUserDailyMissionNum(User user,
	// String missionType) ;

	/**
	 * 提交玩家任务，领取奖励
	 * 
	 * @param user
	 * @param userMissionId
	 * @param isConsume
	 *            是否检查任务消耗，目前只用于声望任务
	 * @return
	 */
	UserMission doCommitMission(User user, int userMissionId,
			boolean isConsume);

	UserMission doMissionRead(long userId, int userMissionId);

	// /**
	// * 提交联盟随机任务，领取奖励
	// *
	// * @param userId
	// * @param userMissionId
	// */
	// @Deprecated
	// GuildMission doCommitGuildRandomMission(User user, int userMissionId);

	// /**
	// * 放弃威望任务
	// *
	// * @param user
	// * @param userMissionId
	// */
	// UserMission doGiveupRandomMission(User user, int userMissionId);
	// /**
	// * 更换声望任务
	// *
	// * @param user
	// * @param userMissionId
	// * @param cashType
	// * @return
	// */
	// UserMission doChangeRandomMission(User user, int userMissionId) ;
	//
	// /**
	// * 变更声望任务需要消耗的元宝/点券数
	// *
	// * @return
	// */
	// int getCash4ChangeRandomMission();
	//
	// /**
	// * 完成声望任务的花费的元宝
	// * @return
	// */
	// int getCash4FinishRandomMission();

	/**
	 * 通知任务模块
	 */
	void notifyMissionModule(User usr, String otcType, int entId, int num);

	void notifyMissionModule(Object sender, Map<String, Object> param);

	/**
	 * 玩家注册的时候调用 创建起始任务
	 * 
	 * @param usr
	 */
	void doInitUserMission(User usr);

	// /**
	// * 初始化随机声望任务
	// *
	// * @param usr
	// * @return
	// */
	// UserMission doInitHonorRandomMission(User usr);

	// /**
	// * 初始化随机联盟任务
	// *
	// * @param guild
	// * @return
	// */
	// @Deprecated
	// GuildMission doInitGuildRandomMission(Guild guild);

	/**
	 * 按用户等级，玩家等级初始化玩家的任务树的根
	 * 
	 * @param usr
	 */
	void doAddUserMission(User usr);

	/**
	 * 生成玩家等级任务
	 * 
	 * @param usr
	 */
	void doAddUserLevelMission(User usr, int oldLv, int newLv);

	/**
	 * 非自动生成任务：前台主动请求产生的任务 需要通知前台任务按钮闪烁
	 * 
	 * @param userId
	 * @param missionId
	 * @return
	 */
	UserMission createUserMission(long userId, int missionId);

	UserMission createUserMission(User user, int missionId);

	UserMission createUserMission(User user, Mission mission);

	/**
	 * 锁任务
	 * 
	 * @param userId
	 */
	void lockUserMission(long userId);

	// UserMission createUserMissionByNpcId(long userId, int issueMissionId) ;

	// UserMission createUserMissionByNpcId(User user, int issueMissionId);

	/**
	 * 某些任务接任务的时候就要判断是否满足条件
	 * 
	 * @param sender
	 */
	boolean doRefreshUserMissionStatus(User user, UserMission userMission);

	// /**
	// * 刷新威望任务难度
	// * @param user
	// * @param userMissionId
	// * @return
	// */
	// public UserMission doRefreshHonorMissionStar(long userId, int
	// userMissionId);
	//
	// /**
	// * 通知声望任务每日最大次数变更
	// * @param user
	// */
	// public void notifyChangeHonorMissionDailyCount(User user);

	// /**
	// * 刷新君主任务
	// * @param userId
	// * @return
	// */
	// UserExp doRefreshUserExpMission(long userId);
	// /**
	// * 获取玩家君主任务及相关数据，若任务过期则重置并生成新的君主任务
	// * @param userId
	// * @return
	// */
	// UserExp doGetUserExpByUserId(long userId);
	//
	// /**
	// * 执行君主任务
	// * @param userId
	// * @param taskId 君主任务id
	// * @return
	// */
	// UserExp doExecUserExpMission(long userId, int taskId);
	//
	// /**
	// * 领取君主任务积分奖励
	// * @param userId
	// * @param score
	// */
	// void doGetUserExpMissionAward(long userId, int score);
	//
	// Set<Integer> getuserExpScoresKeySet();
}

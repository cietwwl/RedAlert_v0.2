package com.youxigu.dynasty2.mission.service;


import com.youxigu.dynasty2.mission.domain.Mission;
import com.youxigu.dynasty2.mission.domain.MissionLimit;
import com.youxigu.dynasty2.mission.domain.UserMission;
import com.youxigu.dynasty2.user.domain.User;

/**
 * 任务完成状况检查器
 * @author Administrator
 *
 */
public interface IMissionCompleteChecker {

	/**
	 * 
	 * @param user
	 * @param mission
	 * @param missionLimit
	 * @param param
	 * @param missionService
	 * @return  返回值=0,没有任务发生变化，=1有任务发生变化
	 */
	boolean check(User user, UserMission userMission,Mission mission ,MissionLimit missionLimit,int entId,int num);

	/**
	 * 消耗
	 * @param user
	 * @param userMission
	 * @param mission
	 * @param missionLimit
	 * @return
	 */
	void doConsume(User user, UserMission userMission,Mission mission ,MissionLimit missionLimit);
	
	/**
	 * 得到动态的limitNum
	 * @param user
	 * @param missionLimit
	 * @return
	 */
	int getLimitEntNum(User user, UserMission userMission,Mission mission ,MissionLimit missionLimit);
	
}

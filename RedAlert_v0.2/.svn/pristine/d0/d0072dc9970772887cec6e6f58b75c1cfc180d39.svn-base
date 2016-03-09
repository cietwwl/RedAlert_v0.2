package com.youxigu.dynasty2.mission.service;

import com.youxigu.dynasty2.mission.domain.Mission;
import com.youxigu.dynasty2.mission.domain.MissionAward;
import com.youxigu.dynasty2.mission.domain.UserMission;
import com.youxigu.dynasty2.user.domain.User;

/**
 * 任务奖励接口
 * @author Administrator
 *
 */
public interface IMissionAwardService {
	void doAward(User user,MissionAward award,Mission mission,UserMission userMission);
	/**
	 * 根据系数计算奖励数量
	 * @param num
	 * @param factor
	 */
	int calcAward(int num,UserMission userMission);
}

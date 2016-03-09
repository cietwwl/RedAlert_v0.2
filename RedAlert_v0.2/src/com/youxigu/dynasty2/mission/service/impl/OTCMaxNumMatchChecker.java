package com.youxigu.dynasty2.mission.service.impl;

import com.youxigu.dynasty2.mission.domain.Mission;
import com.youxigu.dynasty2.mission.domain.MissionLimit;
import com.youxigu.dynasty2.mission.domain.UserMission;
import com.youxigu.dynasty2.user.domain.User;

/**
 * 
 * 最大数量匹配任务:只要达到某个值，即使以后降到这个值以下，也算任务任务完成
 * 
 * 目前没有这类任务 
 * @author Administrator
 * 
 */
public class OTCMaxNumMatchChecker extends AbstractMissionCompleteChecker {

	@Override
	public boolean check(User user, UserMission userMission, Mission mission,
			MissionLimit missionLimit, int entId, int num) {
		if (num != Integer.MIN_VALUE) {
			int limitIndex = mission.getLimitIndex(missionLimit);
			int oldNum = userMission.getNum(limitIndex);
			if (oldNum < num) {
				userMission.setNum(limitIndex, num);
				return true;
			}
		}
		return false;
	}
	@Override
	public void doConsume(User user, UserMission userMission,Mission mission ,MissionLimit missionLimit){
		//不消耗
	}

}

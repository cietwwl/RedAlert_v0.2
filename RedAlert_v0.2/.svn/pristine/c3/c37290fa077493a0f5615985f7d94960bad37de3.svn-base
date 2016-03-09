package com.youxigu.dynasty2.mission.service.impl;

import com.youxigu.dynasty2.mission.domain.Mission;
import com.youxigu.dynasty2.mission.domain.MissionLimit;
import com.youxigu.dynasty2.mission.domain.UserMission;
import com.youxigu.dynasty2.user.domain.User;

public class NoneMissionChecker extends AbstractMissionCompleteChecker{

	@Override
	public boolean check(User user, UserMission userMission, Mission mission,
			MissionLimit missionLimit, int entId, int num) {
		// 任务产生就自动完成，没有任何岳限制
		return true;
	}

	@Override
	public void doConsume(User user, UserMission userMission, Mission mission,
			MissionLimit missionLimit) {
		// do nothing
		
	}

}

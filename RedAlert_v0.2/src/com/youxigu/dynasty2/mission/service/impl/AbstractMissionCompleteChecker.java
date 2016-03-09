package com.youxigu.dynasty2.mission.service.impl;

import com.youxigu.dynasty2.mission.domain.Mission;
import com.youxigu.dynasty2.mission.domain.MissionLimit;
import com.youxigu.dynasty2.mission.domain.UserMission;
import com.youxigu.dynasty2.mission.service.IMissionCompleteChecker;
import com.youxigu.dynasty2.user.domain.User;

public abstract class AbstractMissionCompleteChecker implements
		IMissionCompleteChecker {
	// public boolean missionIsFinished(UserMission uMission,Mission mission) {
	//		
	// Iterator<Map.Entry<MissionLimit,Integer>> lit =
	// mission.getLimitIndexs().entrySet().iterator();
	// while (lit.hasNext()){
	// Map.Entry<MissionLimit,Integer> entry = lit.next();
	// MissionLimit limit = entry.getKey();
	// int index = entry.getValue();
	// if (limit.getEntNum() > uMission.getNum(index)){
	// return false;
	// }
	// }
	//
	// return true;
	// }
	@Override
	public int getLimitEntNum(User user, UserMission userMission,
			Mission mission, MissionLimit missionLimit) {
		return missionLimit.getEntNum() * userMission.getFactor();
	}
}

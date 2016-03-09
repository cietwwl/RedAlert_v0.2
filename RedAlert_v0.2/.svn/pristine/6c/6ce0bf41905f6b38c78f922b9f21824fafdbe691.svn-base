package com.youxigu.dynasty2.mission.service.impl;

import com.youxigu.dynasty2.mission.domain.UserMission;
import com.youxigu.dynasty2.mission.service.IMissionAwardService;

public abstract class AbstractMissionAward implements IMissionAwardService {

	@Override
	public int calcAward(int num,UserMission userMission) {
		
		num = (int)(num*(0.3d*(userMission.getFactor()-1)+1));
		return num;
	}



}

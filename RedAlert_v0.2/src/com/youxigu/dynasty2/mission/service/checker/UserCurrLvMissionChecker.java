package com.youxigu.dynasty2.mission.service.checker;

import com.youxigu.dynasty2.mission.domain.Mission;
import com.youxigu.dynasty2.mission.domain.MissionLimit;
import com.youxigu.dynasty2.mission.domain.UserMission;
import com.youxigu.dynasty2.mission.service.impl.OTCCurrentNumMatchChecker;
import com.youxigu.dynasty2.user.domain.User;

/**
 * 角色当前等级任务
 * @author Administrator
 *
 */
public class UserCurrLvMissionChecker extends OTCCurrentNumMatchChecker{

	@Override
	public boolean check(User user, UserMission userMission, Mission mission,
			MissionLimit missionLimit, int entId, int num) {
		if (num==Integer.MIN_VALUE){
			num = user.getUsrLv();
		}
		return super.check(user, userMission, mission, missionLimit, entId, num);
	}
}

package com.youxigu.dynasty2.mission.service.award;

import com.youxigu.dynasty2.log.imp.LogUserExpAct;
import com.youxigu.dynasty2.mission.domain.Mission;
import com.youxigu.dynasty2.mission.domain.MissionAward;
import com.youxigu.dynasty2.mission.domain.UserMission;
import com.youxigu.dynasty2.mission.service.impl.AbstractMissionAward;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.user.service.IUserService;

/**
 * 奖励：君主经验
 * 
 * @author Administrator
 *
 */
public class UserHonorAward extends AbstractMissionAward {
	private IUserService userService;

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	@Override
	public void doAward(User user, MissionAward award, Mission mission,
			UserMission userMission) {
		userService.doUpdateUserHonor(user,
				this.calcAward(award.getNum(), userMission),
				LogUserExpAct.User_EXP_6);
	}
}

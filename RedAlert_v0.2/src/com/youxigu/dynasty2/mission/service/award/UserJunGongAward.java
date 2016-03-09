package com.youxigu.dynasty2.mission.service.award;

import com.youxigu.dynasty2.log.imp.JunGongAct;
import com.youxigu.dynasty2.mission.domain.Mission;
import com.youxigu.dynasty2.mission.domain.MissionAward;
import com.youxigu.dynasty2.mission.domain.UserMission;
import com.youxigu.dynasty2.mission.service.impl.AbstractMissionAward;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.user.service.IUserService;

/**
 * 奖励：君主军功
 * 
 * @author Dagangzi
 * @date 2016年1月21日
 */
public class UserJunGongAward extends AbstractMissionAward {

	private IUserService userService;

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	@Override
	public void doAward(User user, MissionAward award, Mission mission,
			UserMission userMission) {
		int num = this.calcAward(award.getNum(), userMission);
		userService.doAddjunGong(user, num, JunGongAct.TYPE_MISSI4ON);
	}
}

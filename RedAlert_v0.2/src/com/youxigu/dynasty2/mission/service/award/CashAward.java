package com.youxigu.dynasty2.mission.service.award;

import com.youxigu.dynasty2.log.imp.LogCashAct;
import com.youxigu.dynasty2.mission.domain.Mission;
import com.youxigu.dynasty2.mission.domain.MissionAward;
import com.youxigu.dynasty2.mission.domain.UserMission;
import com.youxigu.dynasty2.mission.service.impl.AbstractMissionAward;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.user.service.IUserService;

/**
 * 奖励元宝/钻石
 * 
 * @author Dagangzi
 * @date 2016年1月12日
 */
public class CashAward extends AbstractMissionAward {
	private IUserService userService;

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	@Override
	public void doAward(User user, MissionAward award, Mission mission,
			UserMission userMission) {
		userService.addGiftCash(user.getUserId(),
				this.calcAward(award.getNum(), userMission),
				LogCashAct.MISSION_AWARD);
	}
}

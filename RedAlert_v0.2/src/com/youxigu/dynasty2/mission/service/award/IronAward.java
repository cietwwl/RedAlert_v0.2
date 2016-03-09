package com.youxigu.dynasty2.mission.service.award;

import com.youxigu.dynasty2.common.AppConstants;
import com.youxigu.dynasty2.develop.service.ICastleResService;
import com.youxigu.dynasty2.mission.domain.Mission;
import com.youxigu.dynasty2.mission.domain.MissionAward;
import com.youxigu.dynasty2.mission.domain.UserMission;
import com.youxigu.dynasty2.mission.service.impl.AbstractMissionAward;
import com.youxigu.dynasty2.user.domain.User;

/**
 * 奖励：金矿
 * 
 * @author Dagangzi
 * @date 2016年1月12日
 */
public class IronAward extends AbstractMissionAward {
	private ICastleResService castleResService;

	public void setCastleResService(ICastleResService castleResService) {
		this.castleResService = castleResService;
	}

	@Override
	public void doAward(User user, MissionAward award, Mission mission,
			UserMission userMission) {
		int awardNum = this.calcAward(award.getNum(), userMission);
		if (awardNum == 0) {
			return;
		}
		castleResService.doAddRes(user.getMainCastleId(),
				AppConstants.ENT_RES_IRON, awardNum,
				true);
	}

}

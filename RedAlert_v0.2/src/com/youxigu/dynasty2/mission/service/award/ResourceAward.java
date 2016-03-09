package com.youxigu.dynasty2.mission.service.award;

import com.youxigu.dynasty2.common.AppConstants;
import com.youxigu.dynasty2.develop.service.ICastleResService;
import com.youxigu.dynasty2.mission.domain.Mission;
import com.youxigu.dynasty2.mission.domain.MissionAward;
import com.youxigu.dynasty2.mission.domain.UserMission;
import com.youxigu.dynasty2.mission.service.impl.AbstractMissionAward;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.user.service.IAccountService;

/**
 * 奖励：四种资源
 * 
 * @author Dagangzi
 * @date 2016年1月12日
 */
public class ResourceAward extends AbstractMissionAward {
	private ICastleResService castleResService;
	private IAccountService accountService;

	public void setAccountService(IAccountService accountService) {
		this.accountService = accountService;
	}

	public void setCastleResService(ICastleResService castleResService) {
		this.castleResService = castleResService;
	}

	@Override
	public void doAward(User user, MissionAward award, Mission mission,
			UserMission userMission) {
		// long userId = user.getUserId();
		int num = this.calcAward(award.getNum(), userMission);

		// int percent = accountService.getOnlineUserEffect(userId);
		// if (percent != 0) {
		// int tmp = (int) (num * (100d + percent) / 100);
		//
		//// castleService.onlineAddicted(userId, percent, num, tmp, "粮食");
		//// castleService.onlineAddicted(userId, percent, num, tmp, "木材");
		//// castleService.onlineAddicted(userId, percent, num, tmp, "石料");
		//// castleService.onlineAddicted(userId, percent, num, tmp, "矿物");
		// num = tmp;
		// }
		if (num == 0) {
			return;
		}

		int[] entIds = { AppConstants.ENT_RES_OIL, AppConstants.ENT_RES_IRON,
				AppConstants.ENT_RES_URANIUM, AppConstants.ENT_RES_GOLD };

		long[] resNums = { num * 1L, num * 1L, num * 1L, num * 1L };

		castleResService.doAddRes(user.getMainCastleId(), entIds, resNums,
				true);

	}

}

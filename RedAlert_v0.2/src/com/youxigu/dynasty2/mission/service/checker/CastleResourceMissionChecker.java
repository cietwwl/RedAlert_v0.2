package com.youxigu.dynasty2.mission.service.checker;

import com.youxigu.dynasty2.common.AppConstants;
import com.youxigu.dynasty2.develop.domain.CastleResource;
import com.youxigu.dynasty2.develop.service.ICastleResService;
import com.youxigu.dynasty2.mission.domain.Mission;
import com.youxigu.dynasty2.mission.domain.MissionLimit;
import com.youxigu.dynasty2.mission.domain.UserMission;
import com.youxigu.dynasty2.mission.service.impl.OTCCurrentNumMatchChecker;
import com.youxigu.dynasty2.user.domain.User;

/**
 * 城池资源达到某个值
 * 
 * @author Administrator
 * 
 */
public class CastleResourceMissionChecker extends OTCCurrentNumMatchChecker {

	private ICastleResService castleResService;

	public void setCastleResService(ICastleResService castleResService) {
		this.castleResService = castleResService;
	}

	@Override
	public boolean check(User user, UserMission userMission, Mission mission,
			MissionLimit missionLimit, int entId, int num) {
		if (num == Integer.MIN_VALUE) {
			num = 0;
			long tmp = 0;
			CastleResource cr = castleResService
					.getCastleResourceById(user.getMainCastleId());

			if (entId == AppConstants.ENT_RES_OIL) {
				tmp = cr.getOilNum();
			} else if (entId == AppConstants.ENT_RES_IRON) {
				tmp = cr.getIronNum();
			} else if (entId == AppConstants.ENT_RES_GOLD) {
				tmp = cr.getGoldNum();
			} else if (entId == AppConstants.ENT_RES_URANIUM) {
				tmp = cr.getUranium();
			}
			if (tmp > Integer.MAX_VALUE) {
				num = Integer.MAX_VALUE;
			} else {
				num = (int) tmp;
			}
		}
		return super.check(user, userMission, mission, missionLimit, entId,
				num);
	}

}

package com.youxigu.dynasty2.mission.service.checker;

import com.youxigu.dynasty2.develop.domain.CastleBuilding;
import com.youxigu.dynasty2.develop.service.ICastleService;
import com.youxigu.dynasty2.mission.domain.Mission;
import com.youxigu.dynasty2.mission.domain.MissionLimit;
import com.youxigu.dynasty2.mission.domain.UserMission;
import com.youxigu.dynasty2.mission.service.impl.OTCCurrentNumMatchChecker;
import com.youxigu.dynasty2.user.domain.User;

/**
 * QCT_Level 升级建筑 “建筑ID”到N级<br>
 * 
 * @author Administrator
 * 
 */
public class BuildCurrLvMissionChecker extends OTCCurrentNumMatchChecker {
	private ICastleService castleService;

	public void setCastleService(ICastleService castleService) {
		this.castleService = castleService;
	}

	@Override
	public boolean check(User user, UserMission userMission, Mission mission,
			MissionLimit missionLimit, int entId, int num) {
		// int lv = 0;
		if (num == Integer.MIN_VALUE) {
			num = 0;
			CastleBuilding casBui = castleService
					.getMaxLevelCastBuildingByEntId(user.getMainCastleId(),
							entId);
			if (casBui != null && casBui.getLevel() > num) {
				num = casBui.getLevel();
			}
		}
		// num = lv;
		return super.check(user, userMission, mission, missionLimit, entId,
				num);
	}

}

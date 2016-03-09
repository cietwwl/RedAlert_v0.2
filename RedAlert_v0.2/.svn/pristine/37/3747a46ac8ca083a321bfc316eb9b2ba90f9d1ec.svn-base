package com.youxigu.dynasty2.mission.service.checker;

import com.youxigu.dynasty2.hero.domain.Troop;
import com.youxigu.dynasty2.hero.service.ITroopService;
import com.youxigu.dynasty2.mission.domain.Mission;
import com.youxigu.dynasty2.mission.domain.MissionLimit;
import com.youxigu.dynasty2.mission.domain.UserMission;
import com.youxigu.dynasty2.mission.service.impl.OTCCurrentNumMatchChecker;
import com.youxigu.dynasty2.user.domain.User;

/**
 * QCT_MainTroopHeroNum 主力军团位置上有N个武将
 * 
 * @author Dagangzi
 * @date 2016年1月12日
 */
public class MainTroopHeroNumChecker extends OTCCurrentNumMatchChecker {
	private ITroopService troopService;

	public void setTroopService(ITroopService troopService) {
		this.troopService = troopService;
	}

	@Override
	public boolean check(User user, UserMission userMission, Mission mission,
			MissionLimit missionLimit, int entId, int num) {
		if (num == Integer.MIN_VALUE) {
			num = 0;
			if (user.getTroopId() > 0) {
				Troop troop = troopService.getTroopById(user.getUserId(),
						user.getTroopId());
				num = troop.getHeroNum();
			}
		}
		return super.check(user, userMission, mission, missionLimit, entId,
				num);
	}

}

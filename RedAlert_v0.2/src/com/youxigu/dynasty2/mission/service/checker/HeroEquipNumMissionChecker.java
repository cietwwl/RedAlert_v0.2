package com.youxigu.dynasty2.mission.service.checker;

import java.util.List;

import com.youxigu.dynasty2.hero.domain.TroopGrid;
import com.youxigu.dynasty2.hero.service.ITroopGridService;
import com.youxigu.dynasty2.mission.domain.Mission;
import com.youxigu.dynasty2.mission.domain.MissionLimit;
import com.youxigu.dynasty2.mission.domain.UserMission;
import com.youxigu.dynasty2.mission.service.impl.OTCMaxNumMatchChecker;
import com.youxigu.dynasty2.user.domain.User;

/**
 * QCT_heroEquipNum 任意武将身上装备达到N个
 * 
 * @author Dagangzi
 * @date 2016年1月12日
 */
public class HeroEquipNumMissionChecker extends OTCMaxNumMatchChecker {
	private ITroopGridService troopGridService;

	public void setTroopGridService(ITroopGridService troopGridService) {
		this.troopGridService = troopGridService;
	}

	@Override
	public boolean check(User user, UserMission userMission, Mission mission,
			MissionLimit missionLimit, int entId, int num) {
		if (num == Integer.MIN_VALUE) {
			num = 0;
			List<TroopGrid> list = troopGridService
					.getTroopGridByUserId(user.getUserId());
			if (list != null) {
				for (TroopGrid troopGrid : list) {
					if (troopGrid.getEquipNum() > num) {
						num = troopGrid.getEquipNum();
					}
				}
			}
		}
		return super.check(user, userMission, mission, missionLimit, entId,
				num);
	}

}

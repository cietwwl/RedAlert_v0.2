package com.youxigu.dynasty2.mission.service.checker;

import java.util.List;

import com.youxigu.dynasty2.hero.domain.Hero;
import com.youxigu.dynasty2.hero.service.IHeroService;
import com.youxigu.dynasty2.mission.domain.Mission;
import com.youxigu.dynasty2.mission.domain.MissionLimit;
import com.youxigu.dynasty2.mission.domain.UserMission;
import com.youxigu.dynasty2.mission.service.impl.OTCCurrentNumMatchChecker;
import com.youxigu.dynasty2.user.domain.User;

/**
 * QCT_HeroNumLevel 拥有N个Y等级的武将 missionLimit entId:heroLv
 * 
 * @author Dagangzi
 * @date 2016年1月12日
 */
public class HeroNumLevelChecker extends OTCCurrentNumMatchChecker {
	private IHeroService heroService;

	public void setHeroService(IHeroService heroService) {
		this.heroService = heroService;
	}

	@Override
	public boolean check(User user, UserMission userMission, Mission mission,
			MissionLimit missionLimit, int entId, int num) {
		int level = missionLimit.getEntId();
		List<Hero> heros = heroService.getUserHeroList(user.getUserId());
		if (heros != null) {
			for (Hero hero : heros) {
				int tmp = hero.getLevel();
				if (level <= tmp) {
					num = num + 1;
				}
			}
		} else {
			num = 0;
		}
		return super.check(user, userMission, mission, missionLimit, entId,
				num);
	}

}

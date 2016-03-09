package com.youxigu.dynasty2.mission.service.checker;

import java.util.List;

import com.youxigu.dynasty2.hero.domain.Hero;
import com.youxigu.dynasty2.hero.service.IHeroService;
import com.youxigu.dynasty2.mission.domain.Mission;
import com.youxigu.dynasty2.mission.domain.MissionLimit;
import com.youxigu.dynasty2.mission.domain.UserMission;
import com.youxigu.dynasty2.mission.service.impl.OTCMaxNumMatchChecker;
import com.youxigu.dynasty2.user.domain.User;

/**
 * QCT_HeroLevel 任意武将升级到N级
 * 
 * @author Administrator
 * 
 */
public class HeroLvMissionChecker extends OTCMaxNumMatchChecker {
	private IHeroService heroService;

	public void setHeroService(IHeroService heroService) {
		this.heroService = heroService;
	}

	@Override
	public boolean check(User user, UserMission userMission, Mission mission,
			MissionLimit missionLimit, int entId, int num) {
		if (num == Integer.MIN_VALUE) {
			List<Hero> heros = heroService.getUserHeroList(user.getUserId());
			if (heros != null) {
				for (Hero hero : heros) {
					if (hero.getLevel() > num) {
						num = hero.getLevel();
					}
				}
			} else {
				num = 0;
			}

		}
		return super
				.check(user, userMission, mission, missionLimit, entId, num);
	}

}

package com.youxigu.dynasty2.mission.service.award;

import com.youxigu.dynasty2.hero.service.IHeroService;
import com.youxigu.dynasty2.log.imp.LogHeroAct;
import com.youxigu.dynasty2.mission.domain.Mission;
import com.youxigu.dynasty2.mission.domain.MissionAward;
import com.youxigu.dynasty2.mission.domain.UserMission;
import com.youxigu.dynasty2.mission.service.impl.AbstractMissionAward;
import com.youxigu.dynasty2.user.domain.User;

/**
 * 奖励武将
 * 
 * @author Administrator
 * 
 */
public class HeroAward extends AbstractMissionAward {
	private IHeroService heroService;

	public void setHeroService(IHeroService heroService) {
		this.heroService = heroService;
	}

	@Override
	public void doAward(User user, MissionAward award, Mission mission,
			UserMission userMission) {
		int num = this.calcAward(award.getNum(), userMission);
		for (int i = 0; i < num; i++) {
			heroService.doCreateAHero(user.getUserId(), award.getEntId(),
					LogHeroAct.Mission_Hero_ADD);
		}
	}
}

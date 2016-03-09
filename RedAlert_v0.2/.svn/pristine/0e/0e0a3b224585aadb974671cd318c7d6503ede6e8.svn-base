package com.youxigu.dynasty2.user.service.impl.achieve;

import java.util.List;

import com.youxigu.dynasty2.hero.domain.Hero;
import com.youxigu.dynasty2.hero.service.IHeroService;
import com.youxigu.dynasty2.user.domain.Achieve;
import com.youxigu.dynasty2.user.domain.AchieveLimit;
import com.youxigu.dynasty2.user.service.IAchieveCompleteChecker;

/**
 * heroLvColorNum X辆Z级Y品质坦克
 * color para1 品质 
 * heroLv para2 等级
 * num para3 个数
 * 
 * @author Dagangzi
 * @date 2016年1月12日
 */
public class HeroLvColorNumAchieveChecker implements IAchieveCompleteChecker {
	private IHeroService heroService;

	public void setHeroService(IHeroService heroService) {
		this.heroService = heroService;
	}

	@Override
	public int check(long userId, Achieve achieve, AchieveLimit achieveLimit,
			int entNum) {
		int color = achieveLimit.getPara1();
		int heroLv = achieveLimit.getPara2();
		int num = achieveLimit.getPara3();
		entNum = 0;
		List<Hero> heros = heroService.getUserHeroList(userId);
		if (heros != null && heros.size() > 0) {
			for (Hero hero : heros) {
				if (hero.getColorInt() == color && hero.getLevel() >= heroLv) {
					entNum = entNum + 1;
				}
			}
		}
		return Math.min(entNum, num);
	}

}

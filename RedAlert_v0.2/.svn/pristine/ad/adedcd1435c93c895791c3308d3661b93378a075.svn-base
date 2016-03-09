package com.youxigu.dynasty2.entity.service;

import com.youxigu.dynasty2.hero.domain.HeroFate;
import com.youxigu.dynasty2.hero.domain.RelifeHeroBackItem;

public interface ISysHeroService {
	HeroFate getHeroFate(String fateId);

	HeroFate getHeroFate(int fateId);

	/**
	 * 获取坦克重生或者回炉返还的物品数据
	 * 
	 * @param sysHeroId
	 *            武将的id
	 * @param relifeNum
	 *            进阶的次数
	 * @param heroStrength
	 *            强化的等级。。 这里不是强化等级对应的id
	 * @return
	 */
	RelifeHeroBackItem getRelifeHeroBackItem(int sysHeroId, int relifeNum,
			int heroStrength);
}

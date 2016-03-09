package com.youxigu.dynasty2.treasury.dao;

import java.util.List;

import com.youxigu.dynasty2.hero.domain.HeroEquipDebris;
import com.youxigu.dynasty2.treasury.domain.Treasury;

public interface ITreasuryDao {
	/**
	 * 根据id获得实例
	 * 
	 * @param id
	 * @return
	 */
	Treasury getTreasuryById(long userId, long id);

	List<Treasury> getTreasurysByUserId(long userId);

	/**
	 * 道具加入背包
	 * 
	 * @param treasury
	 */
	void createTreasury(Treasury treasury);

	/**
	 * 删除背包中道具
	 * 
	 * @param treasury
	 */
	void deleteTreasury(Treasury treasury);

	/**
	 * 更新背包中道具
	 * 
	 * @param treasury
	 */
	void updateTreasury(Treasury treasury);

	/**
	 * 根据entId获得实例
	 * 
	 * @param id
	 * @return
	 */
	List<Treasury> getTreasurysByEntId(long userId, int entId);

	/**
	 * 装备碎片
	 */
	HeroEquipDebris getHeroEquipDebris(long userId);

	void createHeroEquipDebris(HeroEquipDebris card);

	void updateHeroEquipDebris(HeroEquipDebris card);

}

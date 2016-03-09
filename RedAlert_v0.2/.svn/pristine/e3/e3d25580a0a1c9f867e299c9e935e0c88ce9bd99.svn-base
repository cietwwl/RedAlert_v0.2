package com.youxigu.dynasty2.tower.dao;

import java.util.List;

import com.youxigu.dynasty2.tower.domain.Tower;
import com.youxigu.dynasty2.tower.domain.TowerSectionBonus;
import com.youxigu.dynasty2.tower.domain.TowerUser;

/**
 * 打塔dao接口定义
 * @author Dagangzi
 *
 */
public interface ITowerDao {
	/**
	 * 获取所有配置的塔信息,按关由小到大排序的
	 * @return
	 */
	List<Tower> getTowers();

	/**
	 * 获取所有配置的塔区间保底奖励
	 * @return
	 */
	List<TowerSectionBonus> getTowerSectionBonuses();

	/**
	 * 获取用户的打塔信息
	 * @param userId
	 * @return
	 */
	TowerUser getTowerUser(long userId);
	
	/**
	 * 创建用户打塔信息
	 * @param towerUser
	 */
	void createTowerUser(TowerUser towerUser);
	
	/**
	 * 更新用户打塔信息
	 * @param towerUser
	 */
	void updateTowerUser(TowerUser towerUser);
}

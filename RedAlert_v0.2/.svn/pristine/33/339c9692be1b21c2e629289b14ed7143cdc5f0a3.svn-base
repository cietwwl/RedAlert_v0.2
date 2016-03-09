package com.youxigu.dynasty2.hero.service;

import java.util.List;

import com.youxigu.dynasty2.hero.domain.TroopGrid;

/**
 * 军团格子
 * 
 * @author fengfeng
 *
 */
public interface ITroopGridService {

	/**
	 * 锁格子
	 * 
	 * @param userId
	 */
	void lockTroopGrid(long userId);

	/**
	 * 创建格子
	 * 
	 * @param grid
	 */
	void doCreateTroopGrid(TroopGrid grid);

	/**
	 * 通过格子id获取格子
	 * 
	 * @param troopGridId
	 * @return
	 */
	TroopGrid getTroopGrid(long userId, long troopGridId);

	/**
	 * 取得所有的格子
	 * 
	 * @param userId
	 * @return
	 */
	List<TroopGrid> getTroopGridByUserId(long userId);

	/**
	 * 更新格子
	 * 
	 * @param grid
	 */
	void updateTroopGrid(TroopGrid grid);

}

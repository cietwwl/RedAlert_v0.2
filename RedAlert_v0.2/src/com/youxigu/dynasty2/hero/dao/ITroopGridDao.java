package com.youxigu.dynasty2.hero.dao;

import java.util.List;

import com.youxigu.dynasty2.hero.domain.TroopGrid;

/**
 * 军团DAO
 * 
 * @author Administrator
 *
 */
public interface ITroopGridDao {

	/**
	 * 创建格子 返回创建的格子的id主键
	 * 
	 * @param troop
	 */
	void createTroopGrid(TroopGrid grid);

	/**
	 * 更新格子
	 * 
	 * @param troop
	 */
	void updateTroopGrid(TroopGrid grid);

	/**
	 * 获取用户的所有格子
	 * 
	 * @param userId
	 * @return
	 */
	List<TroopGrid> getTroopGridsByUserId(long userId);

	/**
	 * 根据格子id获取格子
	 * 
	 * @param troopGridId
	 * @return
	 */
	TroopGrid getTroopGridById(long userId, long troopGridId);

}

package com.youxigu.dynasty2.map.dao;

import java.util.List;

import com.youxigu.dynasty2.map.domain.CollectPoint;

public interface ICollectPointDao {
	/**
	 * 获取所有玩家收藏的点
	 * 
	 * @param userId
	 * @return
	 */
	public List<CollectPoint> getAllCollectPoints(long userId);

	/**
	 * 保存收藏点
	 * 
	 * @param p
	 * @return
	 */
	public void insertCollectPoint(CollectPoint p);

	/**
	 * 更新收藏点
	 * 
	 * @param p
	 * @return
	 */
	public void updateCollectPoint(CollectPoint p);

	/**
	 * 删除收藏点
	 * 
	 * @param id
	 * @return
	 */
	public void deleteCollectPoint(CollectPoint p);

	/**
	 * 通过id获取收藏点
	 * @param userId
	 * @param id
	 * @return
	 */
	public CollectPoint getCollectPoint(long userId, long id);

}

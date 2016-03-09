package com.youxigu.dynasty2.map.service;

import com.youxigu.dynasty2.chat.ISendMessage;
import com.youxigu.dynasty2.map.domain.MapCell;

/**
 * 管理地图上面的所有区块
 * 
 * @author fengfeng
 *
 */
public interface IAreaService {

	/**
	 * 玩家进入世界地图后主动调用的方法，主要记录玩家当前进入了那个区块
	 * 
	 * @param userId
	 *            用户id
	 * @param posX
	 * @see {@link MapCell}<br>
	 * @param posY
	 * @see {@link MapCell}<br>
	 */
	public void playerEnterArea(long userId, int posX, int posY);

	/**
	 * 如果需要广播消息则广播传入坐标所在区块,系统会自动广播周围8个区块
	 * 
	 * @param posX
	 * @see {@link MapCell}<br>
	 * @param posY
	 * @see {@link MapCell}<br>
	 * @param eventType
	 *            消息编号 @see EventMessage
	 * @param msg要广播的消息
	 */
	public void broadcastAreaMsg(int eventType, ISendMessage msg, int posX,
			int posY);

	/**
	 * 玩家离开世界地图<br>
	 * 1.玩家离开世界地图<br>
	 * 2.玩家退出游戏<br>
	 * 
	 * @param userId
	 */
	public void playerExitWorldMap(long userId);
}

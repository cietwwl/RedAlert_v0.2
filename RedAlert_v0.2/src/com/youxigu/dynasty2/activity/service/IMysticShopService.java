package com.youxigu.dynasty2.activity.service;

import java.util.Map;

/**
 * 文件名    IMysticShopService.java
 *
 * 描  述    神秘商店接口
 *
 * 时  间    2014-8-27
 *
 * 作  者    huhaiquan
 *
 * 版  本   v0.3  
 */
public interface IMysticShopService {
	
	/**
	 * 物品列表，购买状态
	 * 免费使用次数
	 * 道具个数
	 * 下次刷新时间
	 * 
	 * @param userId
	 * @return
	 */
	public Map<String,Object> doGetShopItemView(long userId,int shopId);

	/**
	 * 刷新，返回 doGetShopItemView
	 * 
	 * @param userId
	 * @return
	 */
	public  Map<String,Object> doFreashShopIems(long userId,int shopId);

	public  Map<String,Object> doBuyShopIem(long userId,int shopId,int pos);
}

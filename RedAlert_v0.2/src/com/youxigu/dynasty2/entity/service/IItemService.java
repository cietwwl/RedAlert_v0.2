package com.youxigu.dynasty2.entity.service;

import java.util.List;
import java.util.Map;

import com.youxigu.dynasty2.entity.domain.Item;

public interface IItemService {

	/**
	 * 获取所有npc可以卖的Item
	 */
	List<Item> getAllNpcSellItems();

	/**
	 * 取得符合npc级别的可卖得Item
	 * 
	 * @param level
	 * @return
	 */
	List<Item> getNpcSellItemsByNpcLevel(int level);

	/**
	 * 
	 * @param conds
	 *            * type childType minLevel maxLevel minColor maxColor
	 * @return
	 */
	List<Item> getItemsByCodition(Map<String, Object> conds);

	/**
	 * 取得特定道具类型
	 * 
	 * @param entityId
	 *            道具类型id
	 * @return
	 */
//	public Item getItemByEntId(int entityId);
}

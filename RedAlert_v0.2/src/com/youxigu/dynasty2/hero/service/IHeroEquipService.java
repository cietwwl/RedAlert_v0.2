package com.youxigu.dynasty2.hero.service;

import java.util.List;
import java.util.Map;

import com.youxigu.dynasty2.entity.domain.enumer.EquipPosion;
import com.youxigu.dynasty2.hero.domain.Hero;
import com.youxigu.dynasty2.hero.proto.EquipInfoMsg;
import com.youxigu.dynasty2.treasury.domain.Treasury;

public interface IHeroEquipService {

	/**
	 * 脱装备
	 * 
	 * @param hero
	 * @param treasuryId
	 * @param pos
	 * @param update
	 * @param resetEquipFate
	 * @return
	 */
	public Hero doTakeOffEquip(Hero hero, long treasuryId, EquipPosion ep,
			boolean update, boolean resetEquipFate);

	/**
	 * 武将穿装备
	 * 
	 * @param hero
	 * @param treasury
	 * @return
	 */
	Hero doTakeEquip(long userId, long heroId, long treasuryId);

	/**
	 * 一键换装
	 * 
	 * @param userId
	 * @param heroId
	 * @param treasuryId
	 * @return
	 */
	Hero doAutoTakeAllEquip(long userId, long heroId);

	/**
	 * 武将卸装备
	 * 
	 * @param hero
	 * @param treasury
	 * @return
	 */
	Hero doTakeOffEquip(long userId, long heroId, long treasuryId);

	/**
	 * 脱掉所有装备
	 */
	Hero doTakeOffAllEquip(long userId, long heroId);

	/**
	 * 装备强化
	 * 
	 * @param userId
	 * @param treasuryId
	 * @param num
	 * @return
	 */
	public EquipInfoMsg doEquipLevelup(long userId, long treasuryId, int num);

	/**
	 * 装备合成
	 * 
	 * @param treasuryId
	 * @param userId
	 * @param casId
	 */
	void doEquipCompose(long userId, int entId);

	/**
	 * 装备生产
	 * 
	 * @param treasuryId
	 * @param userId
	 * @param casId
	 */
	Treasury doEquipBuild(long userId, int entId);

	/**
	 * 背包装备加锁
	 */
	void doEquipLock(long userId, long treasuryId, boolean isLock);

	/**
	 * 分解装备
	 * 
	 * @param userId
	 * @param casId
	 * @param treasuryIds
	 * @return
	 */
	void doEquipDestroy(long userId, List<Long> treasuryIds);

	/**
	 * 装备回炉
	 * 
	 * @param userId
	 * @param casId
	 * @param treasuryIds
	 * @return
	 */
	void doEquipRebirth(long userId, List<Long> treasuryIds);

	/**
	 * 获取装备和图纸信息
	 * 
	 * @param userId
	 */
	Map<String, Object> equipCardAndDebris(long userId);
	
	
	/**
	 * 出售商品
	 * 
	 * @param items
	 *            key 配数id，val 物品数量
	 */
	public void doSellItem(long userId, Map<Integer, Integer> items);

}

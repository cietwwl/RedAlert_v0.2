package com.youxigu.dynasty2.treasury.service;

import java.util.List;
import java.util.Map;

import com.youxigu.dynasty2.entity.domain.DroppedEntity;
import com.youxigu.dynasty2.entity.domain.Equip;
import com.youxigu.dynasty2.entity.domain.Item;
import com.youxigu.dynasty2.entity.domain.ItemProperty;
import com.youxigu.dynasty2.hero.domain.HeroEquipDebris;
import com.youxigu.dynasty2.log.imp.LogItemAct;
import com.youxigu.dynasty2.treasury.domain.Treasury;

public interface ITreasuryService {
	public static final String TreasuryLocker = "Treasury_";
	public static final String TREASURY_HERO_EQUIP_DEBRIS_LOCKER = "Treasury_hero_Equip_Debris";

	/**
	 * 得到装备各项属性
	 * 
	 * @param treasury
	 * @return
	 */
	public Map<String, ItemProperty> getEquipAttr(Treasury treasury,
			int sysHeroId);

	/**
	 * 按主键取道具
	 * 
	 * @param userId
	 * @param treasuryId
	 * @return
	 */
	Treasury getTreasuryById(long userId, long treasuryId);

	/**
	 * 得到国库中某物品的未装备的数量
	 * 
	 * @param userId
	 * @param entId
	 * @return
	 */
	int getNotEquippedTreasuryCountByEntId(long userId, int entId);

	/**
	 * 向国库中新增道具
	 * 
	 * @param userId
	 * @param itemId
	 * @param itemNum
	 * 
	 * @return 返回用户道具的id
	 * 
	 */

	boolean doAddItemToTreasury(long userId, int itemId, int itemNum);

	/**
	 * 
	 * @param userId
	 * @param itemId
	 * @param itemNum
	 * @param isGift
	 *            通常系统给的都是赠品（宝石，装备总是非赠品） ，用户买的不是 1=赠品 0=非赠品
	 * @param isBand
	 *            通常使用Item中的默认配置，用户用元宝买的通常是不绑定的，用其他买的是绑定的 1=绑定，0=不绑定 -1=按策划配数
	 * @param isThrowException
	 *            背包满了是否抛异常
	 * @param toMail
	 *            背包满了是否放入邮件
	 * @param addReason
	 * @return
	 */
	boolean addItemToTreasury(long userId, int itemId, int itemNum,
			int isGift, int isBand, boolean isThrowException, boolean toMail,
			LogItemAct addReason);

	/**
	 * 是否具有某种道具
	 * 
	 * @param userId
	 * @param itemId
	 * @return
	 */
	boolean hasTreasury(long userId, int itemId);

	/**
	 * 从国库中减去一定数量的某种物品
	 * 
	 * @param userId
	 * @param itemId
	 * @param itemNum
	 * @param itemType
	 */
	void deleteItemFromTreasury(long userId, int itemId, int itemNum,
			boolean isThrowException, LogItemAct delReason);

	/**
	 * 删除指定的道具
	 * 
	 * @param userId
	 * @param treasuryId
	 * @param itemNum
	 */
	int deleteTreasury(long userId, long treasuryId, int itemNum,
			LogItemAct reason);

	/**
	 * 丢弃道具,丢弃的时候要判断是否可丢弃
	 * 
	 * @param userId
	 * @param treasuryId
	 * @param itemNum
	 */
	void deleteTreasuryByDiscard(long userId, long treasuryId, int num);

	/**
	 * 获取全部背包道具
	 * 
	 * @param userId
	 * @return
	 */
	List<Treasury> getTreasurysByUserId(long userId);

	/**
	 * 取得所有的装备
	 * 
	 * @param userId
	 * @return
	 */
	List<Treasury> getAllEquipByUserId(long userId);

	/**
	 * 获取未装备的全部背包道具
	 * 
	 * @param userId
	 * @return
	 */
	List<Treasury> getNotEquippedTreasurysByUserId(long userId);

	/**
	 * 加锁
	 * 
	 * @param userId
	 */
	void lockTreasury(long userId);

	/**
	 * 背包装备加锁
	 */
	void doEquipLock(long userId, long treasuryId, int isLock);

	/**
	 * 使用道具
	 * 
	 * @param treasury
	 */
	int doUseItem(long userId, long treasuryId, int num);

	/**
	 * 高迁令的使用是带地图坐标参数的
	 * 
	 * @param userId
	 * @param treasuryId
	 * @param num
	 * @param pfFLag
	 *            用户所在平台
	 * @param params
	 */
	int doUseItem(long userId, long treasuryId, int num, String pfFLag,
			Map<String, Object> params);

	/**
	 * 按道具ID使用道具
	 * 
	 * @param userId
	 * @param itemId
	 * @param num
	 * @param pfFLag
	 *            用户所在平台
	 * @return Treasury 使用的道具
	 */
	void doUseItem(long userId, int itemId, int num, String pfFLag);

	void doUseItem(long userId, int itemId, int num, String pfFLag,
			Map<String, Object> params);

	/***
	 * 根据entId查询道具
	 * 
	 * @param userId
	 * @param entId
	 * @return
	 */
	List<Treasury> getTreasurysByEntId(long userId, int entId);

	/**
	 * 获得某种道具数量
	 * 
	 * @param userId
	 * @param entId
	 * @return
	 */
	int getTreasuryCountByEntId(long userId, int entId);

	/***
	 * 按装备类型和子类型查询，-1为无条件
	 * 
	 * @param userId
	 * @param itemType
	 * @param childType
	 * @return
	 */
	List<Treasury> getUserItemByItemTypeAndChildType(long userId, int itemType,
			int childType);

	/**
	 * 后台推刷新道具事件
	 * 
	 * @param treasury
	 */
	void sendFlushTreasuryEvent(Treasury treasury);

	/**
	 * 获得背包空闲格子数
	 * 
	 * @param userId
	 * @return
	 */
	int getBagFreeCount(long userId);

	/**
	 * 背包是否满了
	 * 
	 * @param userId
	 * @param num
	 *            需要的格子数量
	 * @return
	 */
	boolean hasFull(long userId, int num);

	/**
	 * 背包是否满了
	 * 
	 * @param userId
	 * @return
	 */
	boolean hasFull(long userId);

	/**
	 * 发送log mylog&Tlog
	 * 
	 * @param logType
	 * @param userId
	 * @param item
	 * @param itemNum
	 * @param addReason
	 */
	void setItemTLog(String logType, long userId, Item item, int itemNum,
			LogItemAct addReason);

	void setItemTLog(String logType, long userId, Item item, int itemNum,
			int afterItemNum, LogItemAct addReason);

	/**
	 * 更新装备的equip属性，=0表示卸掉，>0表示装备的军团的格子ID
	 * 
	 * @param treasury
	 */
	void updateEquipToEquipped(Treasury treasury, long troopGridId);

	/**
	 * 更新物品
	 * 
	 * @param treasury
	 */
	void updateTreasury(Treasury treasury);

	void lockEquipDebris(long userId);
	
	HeroEquipDebris lockAndGetEquipDebris(long userId);
	/**
	 * 获取准备碎片
	 * 
	 * @param userId
	 * @return
	 */
	HeroEquipDebris getEquipDebris(long userId);

	/**
	 * 装备碎片
	 */
	HeroEquipDebris addHeroEquipDebris(long userId, Item equipDebris, int num,
			LogItemAct reason);

	/**
	 * 装备图纸
	 */
	HeroEquipDebris addHeroEquipCards(long userId, Item equipCard, int num,
			LogItemAct reason);

	/**
	 * 背包中可兑换道具-兑换
	 * 
	 * @param userId
	 * @param tId
	 * @param num
	 *            兑换次数
	 * @return
	 */
	List<DroppedEntity> doExchangeItem(long userId, long tId, int num);

	List<DroppedEntity> doExchangeItem(long userId, Item item, int num);
	
	/**
	 * 创建一件打造装备物品
	 * @param userId
	 * @param itemId
	 * @param itemNum
	 * @return
	 */
	Treasury doCreateEquipItemToTreasury(long userId, Equip equip, int itemNum,LogItemAct addReason);
}
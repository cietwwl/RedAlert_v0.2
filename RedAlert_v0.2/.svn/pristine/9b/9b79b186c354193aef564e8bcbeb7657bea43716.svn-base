package com.youxigu.dynasty2.treasury;

public class _package_ {
/**
 * 背包相关模块
 * 
 * 
 *  前台缓存的bean
 * 
 * ======================(1)bean结构 begain=========================
 *  private long id;// 主键

	private long userId;// 用户Id

	private int entId;// 道具Id
	private int itemCount;// 道具数量
	private int isBind;// 是否已绑定 0:否 1:是

	private long isEquiped;// 是否已装备 0:否  ,>0使用这个装备的武将ID


	private int isGift; // 是否是赠品
	private int level;// 装备强化等级，default=0
	private int holeNum;// 装备打孔数，,如果有开孔功能，这个数可变，否则default=Equip.currHoleNum
	private String gemIds;// 镶嵌的宝石Id,用,分开的entId,有顺序，表示镶嵌的位置,default=nul
	
	private Item item;//物品
	private Equip equip;//装备
	private Gem gem;//宝石
	
	======================(1)bean结构 end=========================
	
	======================(2)service结构 begain=========================
	//根据实体ID获得道具列表
	List<Treasury> getTreasurysByEntId(long userId, int entId);
	
	//根据实体ID获得宝石信息
	Gem getGemByEntId(int entId);
	
	//根据实体ID获得装备信息
	Equip getEquipEntityByEntId(int entId);
	
	//根据背包ID获得 物品信息
	TreasuryView getTreasuryViewById(int id);
	
	//获得背包物品列表[不包含已装配的]
	List<TreasuryView> getTreasuryViewListById(long userId);
	
	//根据entId获得该Id所有的物品信息
	List<TreasuryView> getTreasuryViewListByEntId(int entId);
	
	//根据物品ID获得总数量
	int getTreasuryViewCountByEntId(int entId);
	
	//根据物品类型和物品小类获得物品信息
	TreasuryView getTreasuryViewByItemTypeAndChildType(int type, int childType);
	
	//根据entId来移除该Id的所有缓存数据
	public function removePackByEntId(entId:int):void
	
	//获得武将身上装备 
	List<TreasuryView> getEquipByHero(long heroId);
	
	//取得铁匠铺可卖出的装备(从自己的装备中过滤) item-type=6/sellPrice>0/npcSellLevel>=铁匠铺等级
	List<TreasuryView> getSmithyCanSellEquip(int level);
	
	//取得铁匠铺可买入的装备(从所有的Item中过滤) item-type=6/buyPrice>0/npcSellLevel>=铁匠铺等级
	List<TreasuryView> getSmithyCanBuyEquip(int level);
	
	======================(2)service结构 end=========================
	
	
	======================(3)action 结构 begain=========================
	//[前台缓存用]显示背包道具列表 输入-61022
	public Object loadTreasuryViewByUserId(Map<String, Object> params, Response context);
	
	//[前台缓存用]按实体id刷道具-61023
	public Object loadTreasuryViewByEntId(Map<String, Object> params, Response context);
	
	//[前台缓存用]加载快捷栏-61024
	public Object getShortcutDatas(Map<String, Object> params, Response context)
	======================(3)action 结构 end=========================
 */
}

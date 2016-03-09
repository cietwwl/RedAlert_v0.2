package com.youxigu.dynasty2.entity.domain;

import java.util.ArrayList;
import java.util.List;

import com.manu.core.ServiceLocator;
import com.youxigu.dynasty2.common.AppConstants;
import com.youxigu.dynasty2.entity.domain.enumer.ColorType;
import com.youxigu.dynasty2.entity.domain.enumer.ItemType;
import com.youxigu.dynasty2.entity.service.IEntityService;
import com.youxigu.dynasty2.treasury.domain.OnlinePack;
import com.youxigu.dynasty2.treasury.enums.SellType;

/**
 * 道具实体定义
 * 
 */
public class Item extends Entity {
	private static final long serialVersionUID = 8551824900785910454L;

	public static final int ITEM_TYPE_BOX_DROP = 88;// 掉落包宝箱，直接进背包

	private String itemName;// 名字

	private String itemDesc;// 描述

	private String iconPath;// 图片路径

	private int type;// 类型 6:装备 7:宝物

	private int sumAble;// 可否堆叠 0:否 1:可以 下同

	private int bandAble;// 是否是绑定的 0 :不是 1: 是

	private int throwAble;// 可否丢弃

	private int useAble;// 可否使用 0:（使用和批量使用都不可以） 1：（使用和批量使用都可以） 2：不能批量使用

	private int useMaxNum;// 最大可使用次数-批量使用的上限

	private int childType;// 根据具体的type类型来确认子类型的用途子类型 0:无 默认

	private int level;// 级别

	private int color;// 品质 1-6 灰 绿 蓝 紫 橙 金 红

	private int missionId;// 平台id

	private int time;// 存在时间

	private int userHasMaxNum;//

	private int stackNum;// 可堆叠数量上限

	private int exchangeId;// 兑换物品表的id

	private byte notJoinPack;// 是否进背包 0进背包 1不进背包

	private int sysHeroEntId;// 系统武将id--针对图纸和碎片
	
	/**@see SellType*/
	private int sellType;//物品卖出类型
	private int sellPrice;//卖出获得的货币数量

	private transient String chatName;
	private transient ItemType itemType = null;
	private transient ItemType itemChildType = null;
	private transient ColorType colorType = null;
	private transient ItemExchange exchange;
	private transient IEntityService entityService;
	private transient OnlinePack onlinePack;//运营礼包

	public Item() {
		super();
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemDesc() {
		return itemDesc;
	}

	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}

	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getChildType() {
		return childType;
	}

	public void setChildType(int childType) {
		this.childType = childType;
	}

	public int getSumAble() {
		return sumAble;
	}

	public void setSumAble(int sumAble) {
		this.sumAble = sumAble;
	}

	public int getBandAble() {
		return bandAble;
	}

	public void setBandAble(int bandAble) {
		this.bandAble = bandAble;
	}

	public int getThrowAble() {
		return throwAble;
	}

	public void setThrowAble(int throwAble) {
		this.throwAble = throwAble;
	}

	public int getUseAble() {
		return useAble;
	}

	public void setUseAble(int useAble) {
		this.useAble = useAble;
	}

	public int getUseMaxNum() {
		return useMaxNum;
	}

	public void setUseMaxNum(int useMaxNum) {
		this.useMaxNum = useMaxNum;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getMissionId() {
		return missionId;
	}

	public void setMissionId(int missionId) {
		this.missionId = missionId;
	}

	public IEntityService getEntityService() {
		if (entityService == null) {
			entityService = (IEntityService) ServiceLocator.getSpringBean("entityService");
		}
		return entityService;
	}

	public OnlinePack getOnlinePack() {
		return onlinePack;
	}

	public void setOnlinePack(OnlinePack onlinePack) {
		this.onlinePack = onlinePack;
	}

	/**
	 * 聊天那里的文字
	 * 
	 * @return
	 */
	public String getChatName() {
		if (chatName == null) {
			chatName = new StringBuilder(64).append("<font color='").append(this.getColorStr()).append("'>")
					.append(this.getItemName()).append("</font>").toString();
		}
		return chatName;
	}

	public String getColorStr() {
		return colorType.getColorVal();
	}

	public String getEntityName() {
		return this.getItemName();
	}

	/**
	 * 运营礼包信息
	 * @return
	 */
	public OnlinePack toOnlinePack() {
		if (this.getEntId() >= AppConstants.ENT_DYNAMIC_ID_MIN
				&& this.getEntId() <= AppConstants.ENT_DYNAMIC_ID_MAX) {// 运营配置礼包
			if(onlinePack != null) {
				return onlinePack;
			}
			onlinePack = new OnlinePack();
			onlinePack.setEntId(this.getEntId());
			onlinePack.setName(this.getEntityName());
			onlinePack.setDesc(this.getItemDesc());
			onlinePack.setColor(this.getColor());
			onlinePack.setIcon(this.getIconPath());
			onlinePack.setSumAble(this.getSumAble());
			onlinePack.setThrowAble(this.getThrowAble());
			onlinePack.setUseAble(this.getUseAble());
			onlinePack.setType(this.getType());
			onlinePack.setChildType(this.getChildType());
			onlinePack.setStackNum(this.getStackNum());
			onlinePack.setExchangeId(this.getExchangeId());

			List<DropPackItem> subItems = null;
			if (this instanceof DropPackEntity) {
				subItems = ((DropPackEntity) this).getItems();

			} else if (this instanceof BoxEntity) {
				subItems = ((BoxEntity) this).getItems();
			}
			if (subItems != null) {
				List<OnlinePack.DroppackInfo> dropItem = new ArrayList<OnlinePack.DroppackInfo>(subItems.size());
				onlinePack.setItems(dropItem);
				for (DropPackItem subItem : subItems) {
					String name = getEntityService().getEntity(subItem.getEntId()).getEntityName();
					dropItem.add(new OnlinePack.DroppackInfo(subItem.getDropEntId(), name, subItem.getMinValue(), subItem
							.getMaxValue2()));
				}

			}
		}
		
		return onlinePack;
	}

	//	public Map<String, Object> toMap(Map<String, Object> maps) {
	//		maps.put("entId", this.getEntId());
	//		maps.put("name", this.getEntityName());
	//		maps.put("desc", this.getItemDesc());
	//		maps.put("color", this.getColor());
	//		maps.put("icon", this.getIconPath());
	//		maps.put("sumAble", this.getSumAble());
	//		maps.put("throwAble", this.getThrowAble());
	//		maps.put("useAble", this.getUseAble());
	//		maps.put("type", this.getType());
	//		maps.put("childType", this.getChildType());
	//
	//		List<DropPackItem> subItems = null;
	//		if (this instanceof DropPackEntity) {
	//			subItems = ((DropPackEntity) this).getItems();
	//
	//		} else if (this instanceof BoxEntity) {
	//			subItems = ((BoxEntity) this).getItems();
	//		}
	//		if (subItems != null) {
	//			List<Map<String, Object>> subMaps = new ArrayList<Map<String, Object>>(
	//					subItems.size());
	//			maps.put("items", subMaps);
	//			for (DropPackItem subItem : subItems) {
	//				Map<String, Object> subItemMap = new HashMap<String, Object>(2);
	//				subItemMap.put("entId", subItem.getDropEntId());
	//				subItemMap.put("minNum", subItem.getMinValue());
	//				subItemMap.put("maxNum", subItem.getMaxValue2());
	//				subMaps.add(subItemMap);
	//			}
	//
	//		}
	//		return maps;
	//	}
	//
	//	public Map<String, Object> toMap() {
	//		return toMap(new HashMap<String, Object>(19));
	//	}

	public void check() {
		itemType = ItemType.valueOf(getType());
		itemChildType = ItemType.valueOf(getChildType());
		if (itemType == null) {
			String msg = String.format("entId:%d物品配置类型错误type:%d", getEntId(), getType());
			throw new IllegalArgumentException(msg);
		}
		itemType.checkChildType(getType(), childType);
		colorType = ColorType.valueOf(getColor());
		if (colorType == null) {
			String msg = String.format("entId:%d物品颜色配置错误color:%d", getEntId(), getColor());
			throw new IllegalArgumentException(msg);
		}

	}

	public boolean isEquip() {
		return getItemType().isEquip();
	}

	public ItemType getItemType() {
		if(itemType == null){
			check();
		}
		return itemType;
	}

	public ItemType getItemChildType() {
		if(itemChildType==null){
			check();
		}
		return itemChildType;
	}

	public ColorType getColorType() {
		if(colorType==null){
			check();
		}
		return colorType;
	}

	public int getUserHasMaxNum() {
		return userHasMaxNum;
	}

	public void setUserHasMaxNum(int userHasMaxNum) {
		this.userHasMaxNum = userHasMaxNum;
	}

	public int getStackNum() {
		return stackNum;
	}

	public void setStackNum(int stackNum) {
		this.stackNum = stackNum;
	}

	public int getExchangeId() {
		return exchangeId;
	}

	public void setExchangeId(int exchangeId) {
		this.exchangeId = exchangeId;
	}

	public byte getNotJoinPack() {
		return notJoinPack;
	}

	public void setNotJoinPack(byte notJoinPack) {
		this.notJoinPack = notJoinPack;
	}

	public int getSysHeroEntId() {
		return sysHeroEntId;
	}

	public void setSysHeroEntId(int sysHeroEntId) {
		this.sysHeroEntId = sysHeroEntId;
	}

	public ItemExchange getExchange() {
		return exchange;
	}

	public void setExchange(ItemExchange exchange) {
		this.exchange = exchange;
	}

	public int getSellType() {
		return sellType;
	}

	public void setSellType(int sellType) {
		this.sellType = sellType;
	}

	public int getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(int sellPrice) {
		this.sellPrice = sellPrice;
	}

}

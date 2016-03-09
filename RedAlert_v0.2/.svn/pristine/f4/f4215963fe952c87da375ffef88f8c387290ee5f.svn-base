package com.youxigu.dynasty2.treasury.domain;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.manu.core.ServiceLocator;
import com.youxigu.dynasty2.entity.domain.Item;
import com.youxigu.dynasty2.entity.domain.ItemProperty;
import com.youxigu.dynasty2.entity.domain.enumer.EquipPosion;
import com.youxigu.dynasty2.entity.domain.enumer.ItemType;
import com.youxigu.dynasty2.entity.service.IEntityService;
import com.youxigu.dynasty2.util.StringUtils;

public class Treasury implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	// equip 是否已装备 0:否 1:是
	public transient static final int EQUIP_OFF = 0;// 没有装备
	public transient static final int EQUIP_ON = 1;// 已装备

	// 实体类型查看Item中的定义

	// 61-头盔 62-衣服 63-武器 64-鞋 65-饰品 66-披风

	private transient IEntityService entityService = null;
	private long id;// 主键

	private long userId;// 用户Id

	private int entId;// 道具Id

	private int entType;// 道具类型:其中细分装备类型

	private int itemCount;// 道具数量

	private int useCount;// 道具已经使用次数

	private int band;// 是否已绑定 0:否 1:是 (绑定即不可交易,不绑定即可交易)

	private long equip;// 是否已装备 0:否 ,>0使用这个 装备的军团格子ID

	private int throwAble;// 可否丢弃

	private int childType;// 子类型

	private int isGift; // 是否是赠品

	private Timestamp existEndTime;// 道具有效期截止时间

	// 以下是装备专用,不想要EquipStronger表,也不想另外建一个表
	private int level;// 装备强化等级，default=0
	private int holeNum;// 装备打孔数，,如果有开孔功能，这个数可变，否则default=Equip.currHoleNum
	private String gemIds;// 镶嵌的宝石Id,用,分开的entId,有顺序，表示镶嵌的位置,default=null
	/**
	 * 装备的随机属性,保存格式 id,value;id2,value2 id=EquipRefAtt.id value=随机出来的属性值
	 */
	private String randomProp;// 目前保存的是装备打造出来的属性
	private String specialAttr = "";// 里面保存的是不会变的属性

	private String randomPropTmp;// 新随机出来，尚未确定的随机属性
	private int spiritLv;// 注灵等级
	private int gemExp;// 宝石经验
	/** 装备是否加锁 */
	private int isLock; // 1表示锁定 0表示没锁
	private transient Item item;

	public Item getItem() {

		if (item == null) {
			if (entityService == null) {
				entityService = (IEntityService) ServiceLocator
						.getSpringBean("entityService");
			}
			this.item = (Item) entityService.getEntity(entId);
		}
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getHoleNum() {
		return holeNum;
	}

	public void setHoleNum(int holeNum) {
		this.holeNum = holeNum;
	}

	public String getGemIds() {
		return gemIds;
	}

	public void setGemIds(String gemIds) {
		this.gemIds = gemIds;
	}

	public int getEntType() {
		return entType;
	}

	public boolean isEquip() {
		ItemType t = ItemType.valueOf(entType);
		if (t == null) {
			return false;
		}
		return t.isEquip();
	}

	public void setEntType(int entType) {
		this.entType = entType;
	}

	public int getThrowAble() {
		return throwAble;
	}

	public void setThrowAble(int throwAble) {
		this.throwAble = throwAble;
	}

	public int getUseCount() {
		return useCount;
	}

	public void setUseCount(int useCount) {
		this.useCount = useCount;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getEntId() {
		return entId;
	}

	public void setEntId(int entId) {
		this.entId = entId;
	}

	public int getItemCount() {
		return itemCount;
	}

	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}

	public int getBand() {
		return band;
	}

	public void setBand(int band) {
		this.band = band;
	}

	/**
	 * true表示已经装备到武将上了
	 * 
	 * @return
	 */
	public boolean isEquipToHero() {
		return equip != 0;
	}

	public long getEquip() {
		return equip;
	}

	public void setEquip(long equip) {
		this.equip = equip;
	}

	public int getChildType() {
		return childType;
	}

	/**
	 * 获取准备位置
	 * 
	 * @return
	 */
	public EquipPosion getEquipPosion() {
		if (!isEquip()) {
			return null;
		}
		return EquipPosion.valueOf(childType);
	}

	public void setChildType(int childType) {
		this.childType = childType;
	}

	public Timestamp getExistEndTime() {
		return existEndTime;
	}

	public void setExistEndTime(Timestamp existEndTime) {
		this.existEndTime = existEndTime;
	}

	public String getChildTypeChn() {
		if (this.childType == 61) {
			return "头盔";
		} else if (this.childType == 62) {
			return "盔甲";
		} else if (this.childType == 63) {
			return "武器";
		} else if (this.childType == 64) {
			return "护腿";
		} else if (this.childType == 65) {
			return "饰品";
		} else if (this.childType == 66) {
			return "披风";
		} else {
			return "";
		}
	}

	public int getIsGift() {
		return isGift;
	}

	public void setIsGift(int isGift) {
		this.isGift = isGift;
	}

	/**
	 * 按孔的位置返回镶嵌宝石ID,没有镶嵌的位置=0
	 * 
	 * @return
	 */
	public int[] getGemIdArray() {

		int[] datas = new int[holeNum];

		if (gemIds != null && gemIds.length() > 0) {
			String[] ids = StringUtils.split(gemIds, ",");
			int min = ids.length;
			if (min > datas.length)
				min = datas.length;
			for (int i = 0; i < min; i++) {
				if (ids[i] != null && ids[i].length() > 0) {
					datas[i] = Integer.parseInt(ids[i]);
				}
			}
		}

		return datas;
	}

	/**
	 * 返回镶嵌宝石的id,不关心位置
	 * 
	 * @return
	 */
	public List<Integer> getGemIdList() {
		if (gemIds == null || gemIds.length() == 0) {
			return null;
		}
		List<Integer> datas = new ArrayList<Integer>();
		String[] ids = StringUtils.split(gemIds, ",");
		for (int i = 0; i < ids.length; i++) {
			if (ids[i] != null && ids[i].length() > 0) {
				datas.add(Integer.parseInt(ids[i]));
			}
		}
		return datas;
	}

	/**
	 * 是否有宝石
	 * 
	 * @return
	 */
	public boolean hasGem() {
		if (gemIds == null || gemIds.length() == 0) {
			return false;
		}
		String[] ids = StringUtils.split(gemIds, ",");
		for (int i = 0; i < ids.length; i++) {
			if (ids[i] != null && ids[i].length() > 0) {
				return true;
			}
		}

		return false;

	}

	public String getRandomProp() {
		return randomProp;
	}

	public int getGemExp() {
		return gemExp;
	}

	public void setGemExp(int gemExp) {
		this.gemExp = gemExp;
	}

	public void setRandomProp(String randomProp) {
		this.randomProp = randomProp;
	}

	public String getRandomPropTmp() {
		return randomPropTmp;
	}

	public void setRandomPropTmp(String randomPropTmp) {
		this.randomPropTmp = randomPropTmp;
	}

	public int getSpiritLv() {
		return spiritLv;
	}

	public void setSpiritLv(int spiritLv) {
		this.spiritLv = spiritLv;
	}

	public TreasuryMessage getView() {
		return new TreasuryMessage(this, true);
	}

	public TreasuryMessage getOtherUserView() {
		return new TreasuryMessage(this, false);
	}

	// private Map<String, Object> toMap(boolean owner) {
	// Map<String, Object> maps = null;
	// if (entId >= AppConstants.ENT_DYNAMIC_ID_MIN
	// && entId <= AppConstants.ENT_DYNAMIC_ID_MAX) {
	// maps = new HashMap<String, Object>(19);
	// IEntityService entityService = (IEntityService) ServiceLocator
	// .getSpringBean("entityService");
	// Entity entity = entityService.getEntity(entId);
	// if (entity != null) {
	// if (entity instanceof Item) {
	// Item item = (Item) entity;
	// maps.put("name", item.getEntityName());
	// maps.put("desc", item.getItemDesc());
	// maps.put("color", item.getColor());
	// maps.put("icon", item.getIconPath());
	// maps.put("sumAble", item.getSumAble());
	// maps.put("throwAble", item.getThrowAble());
	// maps.put("useAble", item.getUseAble());
	// if (owner) {
	// maps.put("type", item.getType());
	// maps.put("childType", item.getChildType());
	// }
	//
	// List<DropPackItem> subItems = null;
	// if (entity instanceof DropPackEntity) {
	// subItems = ((DropPackEntity) entity).getItems();
	//
	// } else if (entity instanceof BoxEntity) {
	// subItems = ((BoxEntity) entity).getItems();
	// }
	// if (subItems != null) {
	// List<Map<String, Object>> subMaps = new ArrayList<Map<String, Object>>(
	// subItems.size());
	// maps.put("items", subMaps);
	// for (DropPackItem subItem : subItems) {
	// Map<String, Object> subItemMap = new HashMap<String, Object>(
	// 2);
	// subItemMap.put("entId", subItem.getDropEntId());
	// subItemMap.put("minNum", subItem.getMinValue());
	// subItemMap.put("maxNum", subItem.getMaxValue2());
	// subMaps.add(subItemMap);
	// }
	//
	// }
	// }
	// }
	//
	// } else {
	// maps = new HashMap<String, Object>(12);
	// }
	//
	// maps.put("id", id);
	// maps.put("userId", userId);
	// maps.put("entId", entId);
	// maps.put("itemCount", itemCount);
	// maps.put("isBind", band);
	// maps.put("isGift", isGift);
	// maps.put("isEquiped", equip);
	// maps.put("level", level);
	// maps.put("holeNum", holeNum);
	// maps.put("gemIds", gemIds);
	// maps.put("gemExp", gemExp);
	// maps.put("isLock", isLock);
	// // if (this.randomProp != null || this.randomPropTmp != null) {
	// // // 解析随机属性
	// // ITreasuryService treasuryService = (ITreasuryService) ServiceLocator
	// // .getSpringBean("treasuryService");
	// //
	// // maps.put("randomProps", treasuryService
	// // .paraseEquipRefinings(this.randomProp));
	// //
	// // if (owner) {
	// // maps.put("randomTmpProps", treasuryService
	// // .paraseEquipRefinings(this.randomPropTmp));
	// // }
	// // }
	// maps.put("spiritLv", spiritLv);// 注灵等级
	// return maps;
	// }

	public int getIsLock() {
		return isLock;
	}

	public void setIsLock(int isLock) {
		this.isLock = isLock;
	}

	public void setLock(boolean isLock) {
		if (isLock) {
			this.isLock = 1;
		} else {
			this.isLock = 0;
		}

	}

	/**
	 * true表示锁定
	 * 
	 * @return
	 */
	public boolean isLock() {
		return this.isLock == 1;

	}

	/**
	 * true表示装备已经打造了
	 * 
	 * @return
	 */
	public boolean isEquipBuild() {
		if (!StringUtils.isEmpty(getRandomProp())) {
			return true;
		}
		return false;
	}

	public String getSpecialAttr() {
		return specialAttr;
	}

	public void setSpecialAttr(String specialAttr) {
		this.specialAttr = specialAttr;
	}

	/**
	 * 增加装备的特殊属性
	 * 
	 * @param att
	 */
	public void addSpecialAttr(ItemProperty att) {
		StringBuffer sb = new StringBuffer();
		if (!StringUtils.isEmpty(getSpecialAttr())) {
			sb.append(getSpecialAttr());
			sb.append(";");
		}
		sb.append(att.toAttrString());
		setSpecialAttr(sb.toString());
	}

}

package com.youxigu.dynasty2.entity.domain.enumer;

import java.util.List;

import com.youxigu.dynasty2.entity.domain.Item;
import com.youxigu.dynasty2.util.IndexEnum;

/**
 * 定义所有的准备类型类型
 * 
 * @author fengfeng
 *
 */
public enum ItemType implements IndexEnum {
	ITEM_DEFAULT(0, null, "默认物品类型"), //
	ITEM_EQUIP(1, null, "装备类型物品") {
		@Override
		public void checkChildType(int type, int childType) {
			EquipPosion ep = EquipPosion.valueOf(childType);
			if (ep == null) {
				throw new IllegalArgumentException("物品配置子类型错误type:" + type + ",childType:" + childType);
			}
		}
	}, //
	ITEM_DRAW(3, null, "抽奖符"), //
	ITEM_TYPE_HERO_CARD(4, null, "坦克图纸-信物"), //
	ITEM_TYPE_HERO_EXP_CARD(5, null, "武将经验卡"), //
	ITEM_TYPE_EQUIP_DEBRIS(6, null, "装备碎片"), //
	ITEM_TYPE_HERO_SOUL(7, null, "坦克碎片"), //

	ITEM_TYPE_DROPPACK(8, null, "掉落包"), //
	ITEM_TYPE_DROPPACK_CHILD1(81, ITEM_TYPE_DROPPACK, "掉落包-宝箱"), //
	ITEM_TYPE_DROPPACK_CHILD2(82, ITEM_TYPE_DROPPACK, "掉落包-钥匙"), //
	ITEM_TYPE_DROPPACK_CHILD3(83, ITEM_TYPE_DROPPACK, "掉落包-掉落包宝箱，直接进背包"), //

	ITEM_TYPE_SPEED_UP(9, null, "加速道具") {
		@Override
		public void checkChildType(int type, int childType) {
			SpeedUpItemType ep = SpeedUpItemType.valueOf(childType);
			if (ep == null) {
				throw new IllegalArgumentException("加速道具子类型配置错误type:" + type + ",childType:" + childType);
			}
		}
	}, //
	ITEM_TYPE_EQUIP_CARD(10, null, "装备图纸"), //
	;

	private int type;
	private String desc;
	private ItemType parent;

	ItemType(int type, ItemType parent, String desc) {
		this.type = type;
		this.desc = desc;
		this.parent = parent;
	}

	public ItemType getParent() {
		return parent;
	}

	/**
	 * 校验物品的子类型
	 * 
	 * @param childType
	 */
	public void checkChildType(int type, int childType) {

	}

	public int getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}

	@Override
	public int getIndex() {
		return type;
	}

	/**
	 * 是否属于指定的item大类
	 * @param type 类型常量
	 * @return
	 */
	public boolean isParentType(int type) {
		if (this.getParent() != null) {
			//用子类校验
			return this.getParent().getType() == type;
		}
		return getType() == type;
	}
	
	/**
	 * 是否匹配指定的item类型(可以是大类，也可以是小类)
	 * @param type 类型常量
	 * @return
	 */
	public boolean isCurType(int type) {
		return getType() == type;
	}

	/**
	 * 判断是否为经验道具
	 * 
	 * @return
	 */
	public boolean isExpItem() {
		return ITEM_TYPE_HERO_EXP_CARD.equals(this);
	}

	/**
	 * 判断是否为装备物品
	 * 
	 * @return
	 */
	public boolean isEquip() {
		return ITEM_EQUIP.equals(this);
	}

	/**
	 * 判断是否为坦克图纸-信物
	 * 
	 * @return
	 */
	public boolean isHeroCard() {
		return ITEM_TYPE_HERO_CARD.equals(this);
	}

	/**
	 * 是否为坦克碎片
	 * 
	 * @return
	 */
	public boolean isHeroSoul() {
		return ITEM_TYPE_HERO_SOUL.equals(this);
	}

	/**
	 * 判断是否为装备碎片
	 * 
	 * @return
	 */
	public boolean isEquipDebris() {
		return ITEM_TYPE_EQUIP_DEBRIS.equals(this);
	}

	/**
	 * 是否为装备图纸
	 * 
	 * @return
	 */
	public boolean isEquipCard() {
		return ITEM_TYPE_EQUIP_CARD.equals(this);
	}

	public boolean isDropPack() {
		return ITEM_TYPE_DROPPACK.equals(this);
	}

	/**
	 * 判断是否为加速道具
	 * 
	 * @return
	 */
	public boolean isSpeedUpItem() {
		return ITEM_TYPE_SPEED_UP.equals(this);
	}

	/**
	 * 判断是否为期望的加速道具子类型
	 * 
	 * @param item
	 * @param type
	 * @return
	 */
	public boolean isExpectSpeedUpItem(Item item, SpeedUpItemType type) {
		if (type == null || item == null) {
			return false;
		}
		return type.equals(SpeedUpItemType.valueOf(item.getChildType()));
	}

	/**
	 * 获取装备物品的装备位置
	 * 
	 * @param posion
	 * @return
	 */
	public EquipPosion getEquipPosion(int posion) {
		if (isEquip()) {
			return EquipPosion.valueOf(posion);
		}
		return null;
	}

	static List<ItemType> result = IndexEnumUtil.toIndexes(ItemType.values());

	public static ItemType valueOf(int index) {
		return result.get(index);
	}

}

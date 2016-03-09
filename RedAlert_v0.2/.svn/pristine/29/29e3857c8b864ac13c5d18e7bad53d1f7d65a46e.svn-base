package com.youxigu.dynasty2.activity.domain;

import java.io.Serializable;

import com.youxigu.dynasty2.activity.enums.MysticShopCostType;
import com.youxigu.dynasty2.util.BaseException;

/**
 * 神秘商店里面出售的物品
 * 
 * @author fengfeng
 *
 */
public class MysticShopItem implements Serializable {
	private static final long serialVersionUID = 4845575443244589449L;

	private int shopItemId;

	/** 类型 */
	private int type;

	/** 不同等级的来区分物品1-20 **/
	private String level;

	private int itemId;

	private int itemNum;
	/** 消耗，1，金矿 2，钻石 */
	private int costType;

	private int costNum;

	private int wight1;
	private int wightNum;

	private int wight2;

	private transient MysticShopCostType shopCostType = null;
	private transient int minLevle = 0;
	private transient int maxLevel = 0;

	public void check() {
		shopCostType = MysticShopCostType.valueOf(costType);
		if (shopCostType == null) {
			throw new BaseException("购买神秘商店物品货币类型未定义" + costType);
		}
		String s[] = level.split("-");
		if (s.length != 2) {
			throw new BaseException("物品等级区间错误" + level);
		}

		minLevle = Integer.valueOf(s[0]);
		maxLevel = Integer.valueOf(s[1]);
		if (minLevle >= maxLevel) {
			throw new BaseException("物品等级区间错误" + level);
		}
	}

	public int getShopItemId() {
		return shopItemId;
	}

	public void setShopItemId(int shopItemId) {
		this.shopItemId = shopItemId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getItemNum() {
		return itemNum;
	}

	public void setItemNum(int itemNum) {
		this.itemNum = itemNum;
	}

	public int getCostType() {
		return costType;
	}

	public void setCostType(int costType) {
		this.costType = costType;
	}

	public int getCostNum() {
		return costNum;
	}

	public void setCostNum(int costNum) {
		this.costNum = costNum;
	}

	public int getWight1() {
		return wight1;
	}

	public void setWight1(int wight1) {
		this.wight1 = wight1;
	}

	public int getWightNum() {
		return wightNum;
	}

	public void setWightNum(int wightNum) {
		this.wightNum = wightNum;
	}

	public int getWight2() {
		return wight2;
	}

	public void setWight2(int wight2) {
		this.wight2 = wight2;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public MysticShopCostType getShopCostType() {
		return shopCostType;
	}

	/**
	 * 获取一个等级区间id
	 * 
	 * @return
	 */
	public int getLevelId() {
		return MysticShopItem.countLevelId(minLevle);
	}

	/**
	 * 计算一个等级唯一id
	 * 
	 * @param level
	 * @return
	 */
	public static int countLevelId(int level) {
		return (1 << 20) | level;
	}

	public int getMinLevle() {
		return minLevle;
	}

	public int getMaxLevel() {
		return maxLevel;
	}

}

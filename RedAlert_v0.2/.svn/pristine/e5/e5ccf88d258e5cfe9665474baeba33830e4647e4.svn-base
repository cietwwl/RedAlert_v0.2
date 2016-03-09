package com.youxigu.dynasty2.activity.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件名 UserMysticShop.java
 * 
 * 描 述 玩家神秘商店数据记录
 * 
 * 版 本 v0.3
 */
public class UserMysticShop implements Serializable {
	private static final long serialVersionUID = 5508005872106490947L;
	public static final String SPLIST_ = ":";
	public static final String SPLIST_ITEM = ",";

	private long userId;
	/**
	 * 商店唯一id
	 */
	private int shopId;
	/**
	 * 当前刷出到物品id，
	 */
	private String curShopIds;

	/**
	 * 当前购买的状态 从右到左按位记录状态1为购买状态
	 */
	private int curBuyStatus;

	/**
	 * 已经购买的次数 shopItemId:num,
	 */
	private String buyShopIds;

	/**
	 * 剩余免费刷新次数
	 */
	private int freeCount;

	/**
	 * 购买的次数累计剩余次数(物品购买和元宝购买的和)
	 */
	private int buyCount;

	/**
	 * 上次刷新时间---系统自动增加免费次数的时间
	 */
	private Timestamp lastTimes;

	/**
	 * 元宝消耗数量
	 */
	private int cashCount;

	/**
	 * 当前状态记录
	 */
	private transient List<int[]> curShopList = new ArrayList<int[]>(6);

	private transient Map<Integer, Integer> countMap = new HashMap<Integer, Integer>(
			8);

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getShopId() {
		return shopId;
	}

	public void setShopId(int shopId) {
		this.shopId = shopId;
	}

	public String getCurShopIds() {
		return curShopIds;
	}

	public void setCurShopIds(String curShopIds) {
		this.curShopIds = curShopIds;
	}

	public int getCurBuyStatus() {
		return curBuyStatus;
	}

	public void setCurBuyStatus(int curBuyStatus) {
		this.curBuyStatus = curBuyStatus;
	}

	public String getBuyShopIds() {
		return buyShopIds;
	}

	public void setBuyShopIds(String buyShopIds) {
		this.buyShopIds = buyShopIds;
	}

	public boolean hasFreeCount() {
		return getFreeCount() > 0;
	}

	public int getFreeCount() {
		return freeCount;
	}

	public void setFreeCount(int freeCount) {
		this.freeCount = freeCount;
	}

	public Timestamp getLastTimes() {
		return lastTimes;
	}

	public int getCashCount() {
		return cashCount;
	}

	public void setCashCount(int cashCount) {
		this.cashCount = cashCount;
	}

	public void setLastTimes(Timestamp lastTimes) {
		this.lastTimes = lastTimes;
	}

	public List<int[]> getShopItems() {
		this.init();
		return curShopList;
	}

	public Map<Integer, Integer> getCountMap() {
		this.init();
		return countMap;
	}

	public List<int[]> getCurShopList() {
		return curShopList;
	}

	public void setCurShopList(List<int[]> curShopList) {
		this.curShopList = curShopList;
	}

	public void setCountMap(Map<Integer, Integer> countMap) {
		this.countMap = countMap;
	}

	/**
	 * 设置购买数据 1，记录购买状态 2，记录购买次数
	 * 
	 * @param pos
	 */
	public void buyItemByPos(int pos) {
		this.init();
		this.curBuyStatus = this.curBuyStatus | (1 << pos);
		int[] shop = curShopList.get(pos);
		shop[1] = 1;
		Integer count = countMap.get(shop[0]);
		countMap.put(shop[0], count == null ? 1 : count.intValue() + 1);
		StringBuilder context = new StringBuilder();
		for (Map.Entry<Integer, Integer> data : countMap.entrySet()) {
			context.append(data.getKey()).append(SPLIST_ITEM)
					.append(data.getValue()).append(SPLIST_);
		}
		this.buyShopIds = context.substring(0, context.length() - 1);
	}

	private void init() {
		if (curShopList == null || curShopList.size() <= 0
				&& (curShopIds != null && curShopIds.length() > 0)) {
			List<int[]> temp = new ArrayList<int[]>(6);
			String[] arr = curShopIds.split(SPLIST_);
			int status = curBuyStatus;
			for (int i = 0; i < arr.length; i++) {
				int[] iid = { Integer.parseInt(arr[i]), (status >> i) & 1 };
				temp.add(iid);
			}
			curShopList = temp;
		}

		if ((countMap == null || countMap.size() <= 0)) {
			countMap = new HashMap<Integer, Integer>(8);
			if (buyShopIds != null && !buyShopIds.equals("")) {
				String[] arr = buyShopIds.split(SPLIST_);
				for (int i = 0; i < arr.length; i++) {
					String[] items = arr[i].split(SPLIST_ITEM);
					countMap.put(Integer.valueOf(items[0]),
							Integer.valueOf(items[1]));
				}
			}

		}
	}

	public void reset() {
		if (curShopList != null) {
			curShopList.clear();
		}
		if (countMap != null) {
			countMap.clear();
		}
	}

	public int getBuyCount() {
		return buyCount;
	}

	public void setBuyCount(int buyCount) {
		this.buyCount = buyCount;
	}

	public boolean hasBuyCount() {
		return getBuyCount() > 0;
	}

}

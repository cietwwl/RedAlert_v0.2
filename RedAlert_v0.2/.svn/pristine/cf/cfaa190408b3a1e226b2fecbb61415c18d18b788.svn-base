package com.youxigu.dynasty2.activity.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.manu.core.ServiceLocator;
import com.youxigu.dynasty2.activity.service.impl.MysticShopService;
import com.youxigu.dynasty2.util.DateUtils;

/**
 * 神秘商店类型
 * 
 * @author fengfeng
 *
 */
public class MysticShop implements Serializable {
	private static final long serialVersionUID = -4690900140472517489L;

	/**
	 * 商店id
	 */
	private int shopId;

	/**
	 * vip免费次数上限 0,5:1,10----
	 */
	private String vipFreeCount;

	private String vipdaytime;

	/**
	 * 元宝消耗
	 */
	private String cashCost;

	/**
	 * 每次刷出的数量
	 */
	private int num;

	/**
	 * 消耗道具数量
	 */
	private int itemId;

	/**
	 * 道具类型
	 */
	private int type;

	/**
	 * 刷新时间戳 12:00;18:00;21:00
	 */
	private String freashTime;

	private List<Long> date = new ArrayList<Long>();

	private transient Map<Integer, Integer> vipFreeCounts = new HashMap<Integer, Integer>();
	private transient Map<Integer, Integer> vipdaytimes = new HashMap<Integer, Integer>();

	public void init() {
		String[] arr = vipFreeCount.split(":");
		for (String id : arr) {
			String[] ss = id.split(",");
			vipFreeCounts.put(Integer.valueOf(ss[0]), Integer.valueOf(ss[1]));
		}

		arr = vipdaytime.split(":");
		for (String id : arr) {
			String[] ss = id.split(",");
			vipdaytimes.put(Integer.valueOf(ss[0]), Integer.valueOf(ss[1]));
		}
	}

	/**
	 * 获得当日时间戳列表
	 * 
	 * @return
	 */
	@SuppressWarnings("static-access")
	public List<Long> getDateDttm() {
		if (date.size() <= 0
				|| !DateUtils.isSameDay(new Date(System.currentTimeMillis()),
						new Date(date.get(0)))) {
			synchronized (date) {
				if (date.size() <= 0
						|| !DateUtils.isSameDay(
								new Date(System.currentTimeMillis()), new Date(
										date.get(0)))) {
					date.clear();
					String[] arr = freashTime.split(";");
					MysticShopService mysticShopService = (MysticShopService) ServiceLocator
							.getInstance().getSpringBean("mysticShopService");
					String cur = mysticShopService.curTime(new Date());
					// 12:00;18:00;21:00
					for (String dat : arr) {
						String dateS = cur + " " + dat + ":00";
						date.add(mysticShopService.parseToLong(dateS));

					}
				}
			}
		}
		return date;
	}

	public int getFreeCountByLv(int vipLv) {
		Integer count = vipFreeCounts.get(vipLv);
		if (count != null) {
			return count.intValue();
		}
		String[] arr = vipFreeCount.split(";");
		return Integer.parseInt(arr[arr.length - 1].split(",")[1]);
	}

	public int getBuyCountByLv(int vipLv) {
		Integer count = vipdaytimes.get(vipLv);
		if (count != null) {
			return count.intValue();
		}
		String[] arr = vipdaytime.split(";");
		return Integer.parseInt(arr[arr.length - 1].split(",")[1]);
	}

	public int cashCostByCount(int count) {
		String[] arr = cashCost.split(",");
		count = count >= arr.length ? (arr.length - 1) : count;
		return Integer.parseInt(arr[count]);
	}

	public int getShopId() {
		return shopId;
	}

	public void setShopId(int shopId) {
		this.shopId = shopId;
	}

	public String getVipFreeCount() {
		return vipFreeCount;
	}

	public void setVipFreeCount(String vipFreeCount) {
		this.vipFreeCount = vipFreeCount;
	}

	public String getCashCost() {
		return cashCost;
	}

	public void setCashCost(String cashCost) {
		this.cashCost = cashCost;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getFreashTime() {
		return freashTime;
	}

	public void setFreashTime(String freashTime) {
		this.freashTime = freashTime;
	}

	public String getVipdaytime() {
		return vipdaytime;
	}

	public void setVipdaytime(String vipdaytime) {
		this.vipdaytime = vipdaytime;
	}

}

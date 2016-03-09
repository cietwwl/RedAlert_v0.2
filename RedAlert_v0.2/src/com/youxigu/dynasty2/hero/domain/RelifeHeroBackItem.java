package com.youxigu.dynasty2.hero.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 武将重生或退役返还的物品..服务器起服动态计算出来的
 * 
 * @author fengfeng
 *
 */
public class RelifeHeroBackItem {
	// private Logger log = LoggerFactory.getLogger(RelifeHeroBackItem.class);
	private int sysHeroId;// 系统武将id
	private int relifeNum;// 进阶的等级
	private int heroStrength;// 强化的等级
	// 强化退还的物品
	private Map<Integer, Integer> items = null;
	// 进阶退还的物品
	private int cardNum;

	public RelifeHeroBackItem(int sysHeroId, int relifeNum, int heroStrength,
			int cardNum, HeroStrength curItem, RelifeHeroBackItem preItem) {
		super();
		this.items = new HashMap<Integer, Integer>();
		this.sysHeroId = sysHeroId;
		this.relifeNum = relifeNum;
		this.heroStrength = heroStrength;

		if (curItem != null) {
			handBackHeroStrength(curItem);
		}

		this.cardNum += cardNum;
		if (preItem != null) {
			for (Entry<Integer, Integer> en : preItem.getItems().entrySet()) {
				addItem(en.getKey(), en.getValue());
			}
			this.cardNum += preItem.getCardNum();
		}
		// log.error(this.toString());
	}

	private void addItem(int itemId, int count) {
		Integer it = items.get(itemId);
		if (it == null) {
			items.put(itemId, count);
		} else {
			items.put(itemId, it + count);
		}
	}

	private void handBackHeroStrength(HeroStrength h) {
		addItem(h.getItemId1(), h.getCount1());
		addItem(h.getItemId2(), h.getCount2());
	}

	public int getRelifeNum() {
		return relifeNum;
	}

	public void setRelifeNum(int relifeNum) {
		this.relifeNum = relifeNum;
	}

	public int getHeroStrength() {
		return heroStrength;
	}

	public void setHeroStrength(int heroStrength) {
		this.heroStrength = heroStrength;
	}

	public Map<Integer, Integer> getItems() {
		return items;
	}

	public int getCardNum() {
		return cardNum;
	}

	public void setCardNum(int cardNum) {
		this.cardNum = cardNum;
	}

	public int getSysHeroId() {
		return sysHeroId;
	}

	public void setSysHeroId(int sysHeroId) {
		this.sysHeroId = sysHeroId;
	}

	public static int getId(int relifeNum, int heroStrength) {
		return (relifeNum << 10) | heroStrength;
	}

	public int getId() {
		return RelifeHeroBackItem.getId(getRelifeNum(), getHeroStrength());
	}

	@Override
	public String toString() {
		return getId() + "RelifeHeroBackItem [sysHeroId=" + sysHeroId
				+ ", relifeNum=" + relifeNum + ", heroStrength=" + heroStrength
				+ ", items=" + items + ", cardNum=" + cardNum + "]";
	}

}

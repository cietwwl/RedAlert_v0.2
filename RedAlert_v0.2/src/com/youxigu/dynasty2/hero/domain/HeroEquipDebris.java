package com.youxigu.dynasty2.hero.domain;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * 玩家拥有的装备碎片,和装备图纸
 * 
 * @author Administrator
 * 
 */
public class HeroEquipDebris implements java.io.Serializable {
	private static final long serialVersionUID = 2205471548385086600L;
	private long userId;
	/**
	 * 碎片id1,数量;碎片id2,数量;碎片id3,数量 .......
	 */
	private String debris;

	/**
	 * 图纸id1,数量;图纸id2,数量;图纸id3,数量 .......
	 */
	private String cards;

	public HeroEquipDebris() {
		super();
	}

	public HeroEquipDebris(long userId, String debris, String cards) {
		super();
		this.userId = userId;
		this.debris = debris;
		this.cards = cards;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getDebris() {
		return debris;
	}

	public void setDebris(String debris) {
		this.debris = debris;
	}

	public Map<Integer, Integer> getDebrisMap() {
		return resolve(this.getDebris());
	}

	public Map<Integer, Integer> getCardsMap() {
		return resolve(this.getCards());
	}

	private Map<Integer, Integer> resolve(String val) {
		Map<Integer, Integer> datas = new HashMap<Integer, Integer>();
		if (!StringUtils.isEmpty(val)) {
			String[] arr = StringUtils.split(val, ";");
			for (String one : arr) {
				String[] oneArr = StringUtils.split(one, ",");
				datas.put(Integer.valueOf(oneArr[0]),
						Integer.valueOf(oneArr[1]));
			}

		}
		return datas;
	}

	public void setDebris(Map<Integer, Integer> cards) {
		this.debris = build(cards);
	}

	public void setCards(Map<Integer, Integer> cards) {
		this.cards = build(cards);
	}

	private String build(Map<Integer, Integer> cards) {
		int size = cards.size();
		if (size > 0) {
			Iterator<Map.Entry<Integer, Integer>> lit = cards.entrySet()
					.iterator();
			StringBuilder sb = new StringBuilder(size * 20);
			while (lit.hasNext()) {
				Map.Entry<Integer, Integer> entry = lit.next();
				if (entry.getValue() <= 0) {
					continue;
				}
				sb.append(entry.getKey()).append(",").append(entry.getValue());
				if (lit.hasNext()) {
					sb.append(";");
				}
			}
			return sb.toString();
		}
		return null;
	}

	public void setCards(String cards) {
		this.cards = cards;
	}

	public String getCards() {
		return cards;
	}

}

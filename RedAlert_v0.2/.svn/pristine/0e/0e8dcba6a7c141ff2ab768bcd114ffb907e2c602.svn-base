package com.youxigu.dynasty2.hero.enums;

import java.util.List;

import com.youxigu.dynasty2.util.IndexEnum;

/**
 * 武将上阵状态
 * 
 * @author fengfeng
 *
 */
public enum HeroTroopIng implements IndexEnum {
	HERO_TROOP_ING(2, true, "武将上阵 "), //
	HERO_TROOP_OUT(3, true, "武将未上阵"), //
	HERO_NONE(4, false, "未获得,表示是武将图纸或者碎片"), //
	;

	private int type;
	private String desc;
	private boolean hero;// 标记是否为武将

	HeroTroopIng(int type, boolean hero, String desc) {
		this.type = type;
		this.desc = desc;
		this.hero = hero;
	}

	/**
	 * 判断是否为武将
	 * 
	 * @return
	 */
	public boolean isHero() {
		return hero;
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

	static List<HeroTroopIng> result = IndexEnumUtil.toIndexes(HeroTroopIng
			.values());

	public static HeroTroopIng valueOf(int index) {
		return result.get(index);
	}

}

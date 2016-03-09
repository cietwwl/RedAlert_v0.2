package com.youxigu.dynasty2.hero.enums;

import java.util.List;

import com.youxigu.dynasty2.util.IndexEnum;

/**
 * 武将的状态
 * 
 * @author fengfeng
 *
 */
public enum HeroIdle implements IndexEnum {
	STATUS_IDLE(1, true, "武将空闲状态"), //
	STATUS_OUT(2, false, "武将出征状态"), ;
	private int type;
	/** 判断是否可以出征 **/
	private boolean combat;
	private String desc;

	HeroIdle(int type, boolean combat, String desc) {
		this.type = type;
		this.desc = desc;
		this.combat = combat;
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

	static List<HeroIdle> result = IndexEnumUtil.toIndexes(HeroIdle.values());

	public static HeroIdle valueOf(int index) {
		return result.get(index);
	}

	public boolean isCombat() {
		return combat;
	}

}

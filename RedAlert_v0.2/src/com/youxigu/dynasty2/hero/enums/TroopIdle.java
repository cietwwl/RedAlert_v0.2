package com.youxigu.dynasty2.hero.enums;

import java.util.List;

import com.youxigu.dynasty2.util.IndexEnum;

/**
 * 军团的状态
 * 
 * @author fengfeng
 *
 */
public enum TroopIdle implements IndexEnum {
	STATUS_IDLE(1, true, "军团空闲状态"), //
	STATUS_OUT(2, false, "军团出征状态"), ;
	private int type;
	/** 判断是否可以出征 **/
	private boolean combat;
	private String desc;

	TroopIdle(int type, boolean combat, String desc) {
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

	static List<TroopIdle> result = IndexEnumUtil.toIndexes(TroopIdle.values());

	public static TroopIdle valueOf(int index) {
		return result.get(index);
	}

	public boolean isCombat() {
		return combat;
	}

}

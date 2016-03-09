package com.youxigu.dynasty2.hero.enums;

import java.util.List;

import com.youxigu.dynasty2.util.IndexEnum;

/**
 * 军团类型
 * 
 * @author fengfeng
 *
 */
public enum TroopType implements IndexEnum {
	TROOP_MAIN(1, true, "主力军团"), //
	TROOP_COMMON1(2, false, "第一军团"), //
	TROOP_COMMON2(3, false, "第二军团"), //
	;
	private static final int MAX_TROOP = 3;
	private int type;
	private String desc;
	private boolean mainTroop;

	TroopType(int type, boolean mainTroop, String desc) {
		this.type = type;
		this.mainTroop = mainTroop;
		this.desc = desc;
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

	public boolean isMainTroop() {
		return mainTroop;
	}

	/**
	 * 最多配置的军团
	 * 
	 * @return
	 */
	public static final int getMaxTroop() {
		return MAX_TROOP;
	}

	static List<TroopType> result = IndexEnumUtil.toIndexes(TroopType.values());

	public static TroopType valueOf(int index) {
		return result.get(index);
	}
}

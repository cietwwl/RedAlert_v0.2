package com.youxigu.dynasty2.risk.enums;

import java.util.List;

import com.youxigu.dynasty2.util.IndexEnum;

/**
 * 普通关, 装备关，BOSS关，坦克碎片关，
 * 
 * @author fengfeng
 *
 */
public enum RiskSceneType implements IndexEnum {
	NORMAL(0, "普通关"), //
	EQUIP(1, "装备关"), //
	HERO_FRAGMENT(2, "坦克碎片关"), //
	BOSS(3, "BOSS关"), //
	;
	private int type;
	private String desc;

	private RiskSceneType(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	static List<RiskSceneType> result = IndexEnumUtil.toIndexes(RiskSceneType
			.values());

	@Override
	public int getIndex() {
		return type;
	}

	public static RiskSceneType valueOf(int type) {
		return result.get(type);
	}

}

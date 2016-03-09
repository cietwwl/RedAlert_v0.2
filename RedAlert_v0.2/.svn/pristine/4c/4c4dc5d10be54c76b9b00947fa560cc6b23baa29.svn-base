package com.youxigu.dynasty2.entity.domain.enumer;

import java.util.List;

import com.youxigu.dynasty2.util.IndexEnum;

/**
 * 加速道具类型
 * 
 * @author fengfeng
 *
 */
public enum SpeedUpItemType implements IndexEnum {
	BUILDING_SPEED_UP_TYPE(1, "建筑加速道具"), //
	TECH_SPEED_UP_TYPE(2, "研究加速道具"), //
	PRODUCE_SPEED_UP_TYPE(3, "生产加速道具"), //
	TIMER_SPEED_UP_TYPE(4, "通用加速道具"), //
	RESOURCE_SPEED_UP_TYPE(5, "资源加速道具"), //
	;
	private int type;
	private String desc;

	SpeedUpItemType(int type, String desc) {
		this.type = type;
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

	static List<SpeedUpItemType> result = IndexEnumUtil
			.toIndexes(SpeedUpItemType.values());

	public static SpeedUpItemType valueOf(int index) {
		return result.get(index);
	}

}

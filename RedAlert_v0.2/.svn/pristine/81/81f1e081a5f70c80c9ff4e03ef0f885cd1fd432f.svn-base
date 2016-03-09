package com.youxigu.dynasty2.map.enums;

import java.util.List;

import com.youxigu.dynasty2.util.IndexEnum;

/**
 * 坐标点收藏的类型
 * 
 * @author fengfeng
 *
 */
public enum CollectType implements IndexEnum {
	LIKE(1, "喜爱 "), //
	FRIEND(2, "朋友"), //
	ENEMY(3, "敌人");
	private int type;
	private String desc;

	CollectType(int type, String desc) {
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

	static List<CollectType> result = IndexEnumUtil.toIndexes(CollectType
			.values());

	public static CollectType valueOf(int index) {
		return result.get(index);
	}

}

package com.youxigu.dynasty2.activity.enums;

import java.util.List;

import com.youxigu.dynasty2.util.IndexEnum;

/**
 * 活动类型
 * 
 * @author fengfeng
 *
 */
public enum ActivityType implements IndexEnum {
	// 1累计充值，2，累计消费，3累计充值排行，4，累计消费排行，5单日累计充值，6,单日累计消费，7限时每日登录;8额外加成活动
	EXTRA_ACTIVITY_TYPE(8, "额外加成活动"), //
	// CASH_TYPE(2, "钻石"), //
	// ITEM_TYPE(3, "物品消耗"), //
	;
	private int type;
	private String desc;

	ActivityType(int type, String desc) {
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

	static List<ActivityType> result = IndexEnumUtil.toIndexes(ActivityType
			.values());

	public static ActivityType valueOf(int index) {
		return result.get(index);
	}
}

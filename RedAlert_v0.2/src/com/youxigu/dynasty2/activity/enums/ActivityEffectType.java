package com.youxigu.dynasty2.activity.enums;

import java.util.List;

import com.youxigu.dynasty2.util.IndexEnum;

/**
 * 目前这个效果类型在活动类型为@see ActivityType.EXTRA_ACTIVITY_TYPE的时候有效<br/>
 * 描 述 活动加成
 */
public enum ActivityEffectType implements IndexEnum {
	/**
	 * 出征奖励声望百分比加成
	 */
	CONQUER_SHENGWANG(1, "出征奖励声望百分比加成"), //
	TOWER_EXP(2, "千重楼经验百分比加成（君主经验和武将经验）"), //
	TOWER_HURT(3, "千重楼伤害加成"), //
	RISK_REWARD_ITEM(4, "战役道具掉落数量加成"), //
	RISK_REWARD_EXP(5, "战役经验君主经验武将经验加成"), //
	TIMER_SHOP(6, "限时商城"), //
	TIMER_SALE(7, "拍卖行"), //
	;
	private int effectId;
	private String desc;

	private ActivityEffectType(int effectId, String desc) {
		this.effectId = effectId;
		this.desc = desc;
	}

	public int getEffectId() {
		return effectId;
	}

	public String getDesc() {
		return desc;
	}

	static List<ActivityEffectType> result = IndexEnumUtil
			.toIndexes(ActivityEffectType.values());

	@Override
	public int getIndex() {
		return effectId;
	}

}

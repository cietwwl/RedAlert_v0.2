package com.youxigu.dynasty2.hero.enums;

import java.util.List;

import com.youxigu.dynasty2.util.IndexEnum;

/**
 * 武将是否为队长
 * 
 * @author fengfeng
 *
 */
public enum TeamLeaderType implements IndexEnum {
	HERO_TEAM_LEADER(1, "队长"), //
	HERO_COMMON(2, "不是队长,普通武将"), ;
	private int type;
	private String desc;

	TeamLeaderType(int type, String desc) {
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

	static List<TeamLeaderType> result = IndexEnumUtil.toIndexes(TeamLeaderType
			.values());

	public static TeamLeaderType valueOf(int index) {
		return result.get(index);
	}

	public boolean isLeader() {
		return this.equals(HERO_TEAM_LEADER);
	}

}

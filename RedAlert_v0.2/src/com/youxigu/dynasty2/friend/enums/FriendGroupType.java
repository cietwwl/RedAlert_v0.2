package com.youxigu.dynasty2.friend.enums;

import java.util.List;

import com.youxigu.dynasty2.util.IndexEnum;

/**
 * 好友分组
 * 
 * @author fengfeng
 *
 */
public enum FriendGroupType implements IndexEnum {
	GROUP_ALL(1, true, "好友"), //
	GROUP_BLACK(2, false, "黑名单"), //
	;
	private int id;
	private String desc;
	private boolean friend;

	private FriendGroupType(int id, boolean friend, String desc) {
		this.id = id;
		this.friend = friend;
		this.desc = desc;
	}

	public boolean isFriend() {
		return friend;
	}

	public int getId() {
		return id;
	}

	public String getDesc() {
		return desc;
	}

	static List<FriendGroupType> result = IndexEnumUtil
			.toIndexes(FriendGroupType.values());

	@Override
	public int getIndex() {
		return id;
	}

	public static FriendGroupType valueOf(int index) {
		return result.get(index);
	}
}

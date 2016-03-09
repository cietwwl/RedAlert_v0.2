package com.youxigu.dynasty2.friend.enums;

import java.util.List;

import com.youxigu.dynasty2.util.IndexEnum;

/**
 * 好友状态
 * 
 * @author fengfeng
 *
 */
public enum FriendStatusType implements IndexEnum {
	FRIEND_STATUS_NEW_MSG(0, 0x1, "新消息"), //
	FRIEND_STATUS_ONLINE(1, 0x2, "是否在线"), //
	FRIEND_STATUS_FRIEND(2, 0x4, "是否为好友"), //
	FRIEND_STATUS_HP_GIFT(3, 0x8, "我是否赠送给他体力"), //
	FRIEND_STATUS_HP_GIVEME(4, 0x10, "是否有赠送给我体力"), //
	FRIEND_STATUS_HP_RECEIVE(5, 0x20, "是否领取赠送给我的体力"), //
	;
	private int statu;
	private int mask;
	private String desc;

	private FriendStatusType(int statu, int mask, String desc) {
		this.statu = statu;
		this.mask = mask;
		this.desc = desc;
	}

	public int getMask() {
		return mask;
	}

	public String getDesc() {
		return desc;
	}

	static List<FriendStatusType> result = IndexEnumUtil
			.toIndexes(FriendStatusType.values());

	public int processStatus(int srcStatus) {
		srcStatus = srcStatus | getMask();
		return srcStatus;
	}

	@Override
	public int getIndex() {
		return statu;
	}

	public static FriendStatusType valueOf(int statu) {
		return result.get(statu);
	}
}

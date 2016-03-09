package com.youxigu.dynasty2.user.enums;

import java.util.List;

import com.youxigu.dynasty2.util.IndexEnum;

/**
 * 聊天的一些状态设置
 * 
 * @author fengfeng
 *
 */
public enum ChatStatusType implements IndexEnum {
	CHAT_STRANGE_MSG(0, 0x1, "拒绝陌生人消息"), //
	;
	private int statu;
	private int mask;
	private String desc;

	private ChatStatusType(int statu, int mask, String desc) {
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

	static List<ChatStatusType> result = IndexEnumUtil.toIndexes(ChatStatusType
			.values());

	/**
	 * 重置状态值，0变成1 1变成0
	 * 
	 * @param srcStatus
	 * @return
	 */
	public int changeStatus(int srcStatus) {
		if (checkOpen(srcStatus)) {// 把1位重置为0位
			// 重置
			srcStatus = (srcStatus & ~getMask());
		} else {
			// 设置1位
			srcStatus = srcStatus | getMask();
		}
		return srcStatus;
	}

	/**
	 * 检查开关是否开启的
	 * 
	 * @return
	 */
	public boolean checkOpen(int status) {
		boolean b = (status & getMask()) == getMask();
		return b;
	}

	@Override
	public int getIndex() {
		return statu;
	}

	public static ChatStatusType valueOf(int statu) {
		return result.get(statu);
	}
}

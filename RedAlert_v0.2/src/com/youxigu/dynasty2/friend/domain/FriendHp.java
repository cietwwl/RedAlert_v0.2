package com.youxigu.dynasty2.friend.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import com.youxigu.dynasty2.util.StringUtils;

/**
 * 文件名 FriendHp.java
 * 
 * 描 述 好友赠送体力数据结构
 * 
 * 时 间 2014-8-25
 * 
 * 作 者 huhaiquan
 * 
 */
public class FriendHp implements Serializable {
	private static final long serialVersionUID = 3176156739797177531L;
	public static final String SPLIST_ = ",";
	private long userId;
	/**
	 * 发送给好友的体力 列表
	 */
	private String giftUserIds;
	/**
	 * 赠送给我体力的好友id列表
	 */
	private String giveMeUserIds;

	/**
	 * 已经领取的好友列表id
	 */
	private String receiveUserIds;

	/**
	 * 最后操作时间
	 */
	private Timestamp lastTime;

	// w为减少重复拆串，临时缓存一份
	private transient Map<Long, Long> giftMap;

	private transient Map<Long, Long> giveMeMap;

	private transient Map<Long, Long> receiveMap;

	/**
	 * @return the userId
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}

	/**
	 * @return the giftUserIds
	 */
	public String getGiftUserIds() {
		return giftUserIds;
	}

	/**
	 * @param giftUserIds
	 *            the giftUserIds to set
	 */
	public void setGiftUserIds(String giftUserIds) {
		this.giftUserIds = giftUserIds;
	}

	/**
	 * @return the giveMeUserIds
	 */
	public String getGiveMeUserIds() {
		return giveMeUserIds;
	}

	/**
	 * @param giveMeUserIds
	 *            the giveMeUserIds to set
	 */
	public void setGiveMeUserIds(String giveMeUserIds) {
		this.giveMeUserIds = giveMeUserIds;
	}

	/**
	 * @return the receiveUserIds
	 */
	public String getReceiveUserIds() {
		return receiveUserIds;
	}

	/**
	 * @param receiveUserIds
	 *            the receiveUserIds to set
	 */
	public void setReceiveUserIds(String receiveUserIds) {
		this.receiveUserIds = receiveUserIds;
	}

	/**
	 * @return the lastTime
	 */
	public Timestamp getLastTime() {
		return lastTime;
	}

	private Map<Long, Long> resolveToMap(String val) {
		Map<Long, Long> map = new HashMap<Long, Long>();
		if (StringUtils.isEmpty(val)) {
			return map;
		}
		String rsp[] = val.split(SPLIST_);
		for (String s : rsp) {
			long l = Long.valueOf(s);
			map.put(l, l);
		}
		return map;
	}

	/**
	 * @param lastTime
	 *            the lastTime to set
	 */
	public void setLastTime(Timestamp lastTime) {
		this.lastTime = lastTime;
	}

	/**
	 * 是否有赠送给我
	 * 
	 * @param userId
	 * @return
	 */
	public boolean isGiveMe(long userId) {
		if (giveMeMap == null || giveMeMap.isEmpty()) {
			giveMeMap = resolveToMap(giveMeUserIds);
		}
		return giveMeMap != null && giveMeMap.containsKey(userId);
	}

	/**
	 * 是否领取
	 * 
	 * @param userId
	 * @return
	 */
	public boolean isReceive(long userId) {
		if (receiveMap == null || receiveMap.isEmpty()) {
			receiveMap = resolveToMap(receiveUserIds);
		}
		return receiveMap != null && receiveMap.containsKey(userId);

	}

	/**
	 * 我是否赠送给他
	 * 
	 * @param userId
	 * @return
	 */
	public boolean isGift(long userId) {
		if (giftMap == null || giftMap.isEmpty()) {
			giftMap = resolveToMap(giftUserIds);
		}
		return giftMap != null && giftMap.containsKey(userId);

	}
}

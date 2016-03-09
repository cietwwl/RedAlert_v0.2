package com.youxigu.dynasty2.chat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 离线消息缓存服务.服务器重启后丢失数据
 * 
 * @author fengfeng
 *
 */
public class OfflineMsgCacheService {
	/** 玩家对某个玩家发送的离线消息数量 */
	private static ConcurrentHashMap<Long, Map<Long, Integer>> offLineMsgCount = new ConcurrentHashMap<Long, Map<Long, Integer>>();
	/** 缓存玩家的离线消息 key 玩家的id，value 玩家的id，离线的消息 */
	private static ConcurrentHashMap<Long, Map<Long, List<OffLineMsg>>> offLineMsg = new ConcurrentHashMap<Long, Map<Long, List<OffLineMsg>>>();
	public static int OFFLINE_MSG_MAX = 10;// 离线消息最大数量

	public static ConcurrentHashMap<Long, Map<Long, List<OffLineMsg>>> getOffLineMsg() {
		return offLineMsg;
	}

	/**
	 * 缓存离线消息
	 * 
	 * @param userId
	 * @param msg
	 * @return
	 */
	public static boolean cacheOffLineMsg(long userId, OffLineMsg msg) {
		Map<Long, List<OffLineMsg>> msgs = offLineMsg.get(userId);

		if (msgs == null) {
			msgs = new HashMap<Long, List<OffLineMsg>>();
			offLineMsg.put(userId, msgs);
		}

		Map<Long, Integer> msgsCount = offLineMsgCount.get(msg.getUserId());
		if (msgsCount == null) {
			msgsCount = new HashMap<Long, Integer>();
			offLineMsgCount.put(msg.getUserId(), msgsCount);
		}

		List<OffLineMsg> umsg = msgs.get(msg.getUserId());
		if (umsg == null) {
			umsg = new ArrayList<OffLineMsg>();
			msgs.put(msg.getUserId(), umsg);
		}
		umsg.add(umsg.size(), msg);
		int size = msgs.size();
		if (size > OFFLINE_MSG_MAX) {
			umsg.remove(OFFLINE_MSG_MAX);
		}
		// 记录数量
		msgsCount.put(userId, umsg.size());
		return true;
	}

	/**
	 * 获取给玩家发送离线消息的玩家id
	 * 
	 * @param userId
	 * @return
	 */
	public static Set<Long> getOffLineMsgByUserId(long userId) {
		Map<Long, List<OffLineMsg>> msgs = offLineMsg.get(userId);
		if (msgs == null) {
			return null;
		}
		return msgs.keySet();
	}

	/**
	 * 读取玩家的离线消息
	 * 
	 * @param userId
	 * @return
	 */
	public static List<OffLineMsg> getOffLineMsg(long userId, long friendId) {
		Map<Long, Integer> msgsCount = offLineMsgCount.get(friendId);
		if (msgsCount != null) {
			msgsCount.remove(userId);
		}
		Map<Long, List<OffLineMsg>> msgs = offLineMsg.get(userId);
		if (msgs == null) {
			return null;
		}
		List<OffLineMsg> msg = msgs.remove(friendId);
		return msg;
	}

	/**
	 * 删除离线消息
	 * 
	 * @param userId
	 * @param friendId
	 */
	public static void removeOffLineMsg(long userId, long friendId) {
		getOffLineMsg(userId, friendId);
	}

	/**
	 * true表示可以发送离线消息
	 * 
	 * @param userId
	 * @param friendId
	 * @return
	 */
	public static boolean canSendOfflineMsg(long userId, long friendId) {
		int c = getOffLineMsgCount(userId, friendId);
		return c < OFFLINE_MSG_MAX;
	}

	/**
	 * 获取武将的离线消息数量
	 * 
	 * @param userId
	 * @param friendId
	 * @return
	 */
	public static int getOffLineMsgCount(long userId, long friendId) {
		Map<Long, Integer> map = offLineMsgCount.get(userId);
		if (map == null) {
			return 0;
		}
		Integer c = map.get(friendId);
		if (c == null) {
			c = 0;
		}
		return c;
	}

	/**
	 * 玩家离线消息
	 * 
	 * @author fengfeng
	 *
	 */
	public static class OffLineMsg implements Serializable,
			Comparable<OffLineMsg> {
		private static final long serialVersionUID = -3954687341996928656L;
		private long userId;
		private long times;
		private String msg;

		public OffLineMsg() {
			super();
		}

		public OffLineMsg(long userId, long times, String msg) {
			super();
			this.userId = userId;
			this.times = times;
			this.msg = msg;
		}

		public long getUserId() {
			return userId;
		}

		public void setUserId(long userId) {
			this.userId = userId;
		}

		public long getTimes() {
			return times;
		}

		public void setTimes(long times) {
			this.times = times;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (int) (userId ^ (userId >>> 32));
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			OffLineMsg other = (OffLineMsg) obj;
			if (userId != other.userId)
				return false;
			return true;
		}

		@Override
		public int compareTo(OffLineMsg o) {// 降序排列，时间大的在前面
			if (o.getTimes() == getTimes()) {
				return 0;
			}
			if (o.getTimes() > getTimes()) {
				return 1;
			}
			return -1;
		}
	}
}

package com.youxigu.dynasty2.user.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.youxigu.dynasty2.util.CompressUtils;

/**
 * 用户聊天设置、快捷聊天记录
 * 
 * @author lvkai
 *
 */
public class UserChat implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5636975087848584491L;

	private long userId;

	private String config;// 频道配置 "1,1,1,0" 0表示关闭 1开启

	private byte[] hisRecord;// 加入快捷发送记录，最多10条，用于快速发送

	private byte[] recentTimeFriend;// 好友的最近联系人(保存的是一个java map key用户id val时间)

	private int statu;// 一些状态设置

	public UserChat() {

	}

	public UserChat(long userId, String config, byte[] hisRecord,
			byte[] recentTimeFriend, int status) {
		this.userId = userId;
		this.config = config;
		this.hisRecord = hisRecord;
		this.recentTimeFriend = recentTimeFriend;
		this.statu = status;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}

	public byte[] getHisRecord() {
		return hisRecord;
	}

	public void setHisRecord(byte[] hisRecord) {
		this.hisRecord = hisRecord;
	}

	public byte[] getRecentTimeFriend() {
		return recentTimeFriend;
	}

	public void setRecentTimeFriend(byte[] recentTimeFriend) {
		this.recentTimeFriend = recentTimeFriend;
	}

	public int getStatu() {
		return statu;
	}

	public void setStatu(int statu) {
		this.statu = statu;
	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		RecentTimeFriend r = new RecentTimeFriend(11111,
				System.currentTimeMillis());
		List<RecentTimeFriend> ls = new ArrayList<RecentTimeFriend>();
		ls.add(r);
		byte[] b = CompressUtils.serializeAndCompress(((Object) ls));

		List<RecentTimeFriend> rs = (List<RecentTimeFriend>) CompressUtils
				.decompressAndDeSerialize(b);
		System.out.println(rs);
	}

	/**
	 * 好友最近联系人
	 * 
	 * @author fengfeng
	 *
	 */
	public static class RecentTimeFriend implements Serializable {
		private static final long serialVersionUID = -3954687341996928656L;
		private long userId;
		private long times;

		public RecentTimeFriend() {
			super();
		}

		public RecentTimeFriend(long userId, long times) {
			super();
			this.userId = userId;
			this.times = times;
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
			RecentTimeFriend other = (RecentTimeFriend) obj;
			if (userId != other.userId)
				return false;
			return true;
		}

	}

}

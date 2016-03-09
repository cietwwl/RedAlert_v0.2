package com.youxigu.dynasty2.friend.domain;

public class FriendRecommend implements java.io.Serializable {
	private static final long serialVersionUID = 1182852082669969712L;
	private int id;
	private int usrLv;
	private int minLv;
	private int maxLv;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUsrLv() {
		return usrLv;
	}

	public void setUsrLv(int usrLv) {
		this.usrLv = usrLv;
	}

	public int getMinLv() {
		return minLv;
	}

	public void setMinLv(int minLv) {
		this.minLv = minLv;
	}

	public int getMaxLv() {
		return maxLv;
	}

	public void setMaxLv(int maxLv) {
		this.maxLv = maxLv;
	}

}

package com.youxigu.dynasty2.friend.domain;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 好友申请定义
 * 
 * @author Dagangzi
 *
 */
public class FriendApp implements Serializable {
	private static final long serialVersionUID = 1426513689177704451L;
	private int id;
	private long userId;// 所属君主id
	private String userName;// 君主名
	private long friendUserId;// 好友君主id
	private String friendUserName;// 好友君主名
	private Timestamp addTime;// 好友的添加时间,用在好友显示时排序

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public long getFriendUserId() {
		return friendUserId;
	}

	public void setFriendUserId(long friendUserId) {
		this.friendUserId = friendUserId;
	}

	public String getFriendUserName() {
		return friendUserName;
	}

	public void setFriendUserName(String friendUserName) {
		this.friendUserName = friendUserName;
	}

	public Timestamp getAddTime() {
		return addTime;
	}

	public void setAddTime(Timestamp addTime) {
		this.addTime = addTime;
	}
}

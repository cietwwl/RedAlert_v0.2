package com.youxigu.dynasty2.friend.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import com.youxigu.dynasty2.friend.enums.FriendGroupType;
import com.youxigu.dynasty2.util.BaseException;

/**
 * 好友定义
 * 
 * @author Dagangzi
 *
 */
public class Friend implements Serializable {
	private static final long serialVersionUID = 7287922748778183372L;
	public transient static final int PAGE_SIZE = 12;// 每页的长度
	public transient static final int APP_TIMEOUT_TIME = 1 * 24 * 60 * 60;

	private int id;
	private long userId;// 所属君主id
	private long friendUserId;// 好友君主id
	private String friendUserName;// 好友君主名
	private long friendMainCasId;// 好友主城id
	private int groupId;// 所属组(显示是通过组来过滤)
	private Timestamp addTime;// 好友的添加时间,用在好友显示时排序
	private String note;// 备注

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getFriendUserId() {
		return friendUserId;
	}

	public void setFriendUserId(long friendUserId) {
		this.friendUserId = friendUserId;
	}

	public long getFriendMainCasId() {
		return friendMainCasId;
	}

	public void setFriendMainCasId(long friendMainCasId) {
		this.friendMainCasId = friendMainCasId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getGroupId() {
		return groupId;
	}

	public FriendGroupType getGroup() {
		return FriendGroupType.valueOf(groupId);
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public void setGroup(FriendGroupType group) {
		if (group == null) {
			throw new BaseException("好友分组错误");
		}
		this.groupId = group.getIndex();
	}

	public Timestamp getAddTime() {
		return addTime;
	}

	public void setAddTime(Timestamp addTime) {
		this.addTime = addTime;
	}

	public String getFriendUserName() {
		return friendUserName;
	}

	public void setFriendUserName(String friendUserName) {
		this.friendUserName = friendUserName;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}

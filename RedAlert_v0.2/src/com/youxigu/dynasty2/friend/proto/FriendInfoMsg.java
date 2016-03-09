package com.youxigu.dynasty2.friend.proto;

import java.io.Serializable;

import com.google.protobuf.Message;
import com.youxigu.dynasty2.chat.ISendMessage;
import com.youxigu.dynasty2.chat.proto.CommonHead.SexType;
import com.youxigu.dynasty2.friend.proto.FriendMsg.FriendInfo;

public class FriendInfoMsg implements Serializable, ISendMessage {
	private static final long serialVersionUID = 1L;
	private long userId;// 添加的好友id
	private String name;// 好友名字
	private int level;// 等级
	private int sex;
	private int vipLevel;// vip等级
	private String icon;// 图像id
	private int countryId;// 国家id
	private int militaryRank;// 军衔等级
	private String guildName;// 联盟
	private int status;// 扩充状态信息用位表示，0位表示是否有新消息,1位表示是否在线，2位表示是否为好友

	public FriendInfoMsg() {
		super();
	}

	public FriendInfoMsg(long userId, String name, int level) {
		super();
		this.userId = userId;
		this.name = name;
		this.level = level;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	public int getMilitaryRank() {
		return militaryRank;
	}

	public void setMilitaryRank(int militaryRank) {
		this.militaryRank = militaryRank;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getGuildName() {
		return guildName;
	}

	public void setGuildName(String guildName) {
		this.guildName = guildName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public Message build() {
		FriendInfo.Builder info = FriendInfo.newBuilder();
		info.setUserId(userId);
		info.setName(name);
		info.setLevel(level);
		info.setVipLevel(vipLevel);
		info.setIcon(icon);
		info.setCountryId(countryId);
		info.setMilitaryRank(militaryRank);
		info.setGuildName(guildName);
		info.setStatus(status);
		SexType type = SexType.valueOf(sex);
		if (type == null) {
			type = SexType.MAN;
		}
		info.setSex(type);
		return info.build();
	}
}

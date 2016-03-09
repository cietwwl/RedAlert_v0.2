package com.youxigu.dynasty2.chat;

import java.util.ArrayList;
import java.util.List;

/**
 * 频道中的玩家信息
 * 
 * @author Phoeboo
 * @version 1.0 2011.01.19
 */
public class ChatUser {
	private long userId;
	private String userName;
	// 头像
	private String icon;
	private int sex; // 性别
	private int usrLv;// 君主等级
	// private String systemKey;
	private int countryId; // 国家Id
	private long guildId; // 联盟id
	private long lastChatTime = 0;
	private int vip;// vip等级
	private int title;// 军衔
	private Object tag;// 其他信息 平台相关的信息

	public ChatUser() {

	}

	public ChatUser(long userId, String userName, String icon, int sex,
			int countryId, long guildId, int usrLv, int vip, int title,
			Object tag) {

		this.userId = userId;
		this.userName = userName;
		this.icon = icon;
		this.sex = sex;
		this.countryId = countryId;
		this.guildId = guildId;
		this.usrLv = usrLv;
		this.vip = vip;
		this.title = title;
		this.tag = tag;
	}

	//
	// //玩家所在频道列表增加新频道
	// public void addChatUserChannel( ChatUserChannel cuc ) {
	//
	// if ( cuc == null ) return;
	// channelList.put( cuc.getChannelName(), cuc );
	// }
	//
	// //玩家所在频道列表增加新频道
	// public void addChatUserChannel( String channelType, String channelId ) {
	//
	// ChatUserChannel cuc = new ChatUserChannel(channelType, channelId);
	// channelList.put( cuc.getChannelName(), cuc );
	// }
	//
	// //玩家所在频道列表移除频道
	// public void removeChatUserChannel( ChatUserChannel cuc ) {
	//
	// if ( cuc == null ) return;
	// channelList.remove( cuc.getChannelName() );
	// }
	//
	// //玩家所在频道列表移除频道
	// public void removeChatUserChannel( String channelType, String channelId )
	// {
	//
	// channelList.remove( ChatChannelManager.getChannelName(channelType,
	// channelId) );
	// }
	//
	// /**
	// * 校验是否输入过快
	// * @param channelId
	// */
	// public void TimeIsUp( String channelType, String channelId ) {
	//
	// ChatUserChannel chatUserChannel = this.channelList.get(
	// ChatChannelManager.getChannelName(channelType, channelId) );
	// if ( chatUserChannel == null ) return;
	//
	// Timestamp timestamp = chatUserChannel.getLastTalk();
	// if ( timestamp == null ) return;
	//
	// Timestamp now = UtilDate.nowTimestamp();
	// if (UtilDate.secondDistance(now, timestamp) < 10) {
	// throw new ManuAppException("请不要输入过快");
	// }
	//
	// chatUserChannel.setLastTalk( now );
	// return;
	// }

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

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	public long getGuildId() {
		return guildId;
	}

	public void setGuildId(long guildId) {
		this.guildId = guildId;
	}

	public long getLastChatTime() {
		return lastChatTime;
	}

	public void setLastChatTime(long lastChatTime) {
		this.lastChatTime = lastChatTime;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public Object getTag() {
		return tag;
	}

	public void setTag(Object tag) {
		this.tag = tag;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getUsrLv() {
		return usrLv;
	}

	public void setUsrLv(int usrLv) {
		this.usrLv = usrLv;
	}

	public int getVip() {
		return vip;
	}

	public void setVip(int vip) {
		this.vip = vip;
	}

	public int getTitle() {
		return title;
	}

	public void setTitle(int title) {
		this.title = title;
	}

}

package com.youxigu.dynasty2.chat;

import java.sql.Timestamp;

/**
 * 玩家所在频道配置
 * 
 * @author Phoeboo
 * @version 1.0   2011.01.19
 */
public class ChatUserChannel {

	private String 		channelType;			// 频道类别
	private String 		channelId;				// 频道id
	
	private String 		color;					// 字颜色
	private int 		isMask;					// 是否屏蔽频道   0:不屏蔽  1:屏蔽
	private Timestamp	lastTalk;				// 玩家最后发言时间
	
	
	public ChatUserChannel( String channelType, String channelId  ) {
		
		this.channelType  	= channelType;
		this.channelId		= channelId;
	}
	

	public String getChannelName( ){
		return ChatChannelManager.getInstance().getChannelName(channelType, channelId);
	}

	public String getChannelType() {
		return channelType;
	}

	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getIsMask() {
		return isMask;
	}

	public void setIsMask(int isMask) {
		this.isMask = isMask;
	}

	public Timestamp getLastTalk() {
		return lastTalk;
	}

	public void setLastTalk(Timestamp lastTalk) {
		this.lastTalk = lastTalk;
	}

}

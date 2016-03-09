package com.youxigu.dynasty2.chat;


import java.util.Map;



/**
 * 有些消息发送是发送给某一个特定频道内的玩家，但是前台显示是在另外一个频道
 * 这里用新频道做频道转换
 * 
 * @author Administrator
 * 
 */
public class ConverChannelBroadcastChatMessage extends ChatMessage {
	private String newChannelType;
	private String newChannelId;

	public String getNewChannelType() {
		return newChannelType;
	}

	public void setNewChannelType(String newChannelType) {
		this.newChannelType = newChannelType;
	}

	public String getNewChannelId() {
		return newChannelId;
	}

	public void setNewChannelId(String newChannelId) {
		this.newChannelId = newChannelId;
	}

	public ConverChannelBroadcastChatMessage() {
		super();
	}

	public ConverChannelBroadcastChatMessage(ChatUser fromUser, String channel,
			String channelId, Object context) {

		super(fromUser, null, channel, channelId, context);
	}

	public ConverChannelBroadcastChatMessage(long fromUserId, String channel,
			String channelId, Object context) {
		super(fromUserId, 0, channel, channelId, context);

	}
	public ConverChannelBroadcastChatMessage(long fromUserId, String channel,
			String channelId, Object context,byte priority) {
		super(fromUserId, 0, channel, channelId, context);

	}
	public ConverChannelBroadcastChatMessage(long fromUserId, String channel,
			String channelId, Object context, String newChannelType,
			String newChannelId) {
		super(fromUserId, 0, channel, channelId, context);
		this.newChannelType = newChannelType;
		this.newChannelId = newChannelId;
	}
	public ConverChannelBroadcastChatMessage(long fromUserId, String channel,
			String channelId, Object context, String newChannelType,
			String newChannelId,byte priority) {
		super(fromUserId, 0, channel, channelId, context,priority);
		this.newChannelType = newChannelType;
		this.newChannelId = newChannelId;
	}
	public Map<String, Object> toMap() {
		Map<String, Object> map = super.toMap();
		map.put(ChatInterface.CHAT_CHANNEL, this.newChannelType);
		map.put(ChatInterface.CHAT_CHANNELID, this.newChannelId);
		return map;
	}

}

package com.youxigu.dynasty2.chat;

import java.util.HashMap;
import java.util.Map;

import com.google.protobuf.Message;
import com.youxigu.dynasty2.chat.proto.ChatMsg.Response10009Define;
import com.youxigu.dynasty2.chat.proto.CommonHead.ResponseHead;
import com.youxigu.dynasty2.core.flex.amf.IAMF3Action;
import com.youxigu.dynasty2.util.CommonUtil;
import com.youxigu.dynasty2.util.StringUtils;

/**
 * 与客户端交互的聊天信息类
 * 
 * @author Phoeboo
 * @version 1.0 2011.01.19
 */
public class ChatMessage implements java.io.Serializable {
	private static final long serialVersionUID = -2204843521436909067L;
	public static byte PRIORITY_4 = 4;// 千里传音道具
	public static byte PRIORITY_6 = 6;// 各平台喇叭道具
	public static byte PRIORITY_8 = 8;// 城池10级及以上公告
	public static byte PRIORITY_9 = 9;// 千重楼80层、100层公告
	public static byte PRIORITY_MIN = 127;// default
	// int userId, String channel, String message,String toUserName

	private long fromUserId; // 发送方名称 玩家发送，非玩家发送没有
	private String fromUserName = ""; // 发送方名称 玩家发送，非玩家发送没有
	private String icon = "";
	private int sex; // 发送方性别
	private int countryId;
	private Object tag; // 发送方其他信息
	private long toUserId; // 接收方名称，私聊频道
	private String toUserName; // 接收方名称，私聊频道

	// private User fromUser; // 发送方
	// private User toUser; // 接收方

	private String channelType; // 所属频道类别 world league private system help title
	// combat
	private String channelId; // 子频道ID
	// private int viewOrder; // 显示顺序:1:在下方显示 0:在上方显示

	private Object context; // 消息内容
	private String url;
	/**
	 * 消息前台显示的优先级
	 */
	private byte priority = PRIORITY_MIN;
	/**
	 * 消息要发送给的平台玩家
	 */
	private String pf;
	
	/**
	 * 是否发送给自己
	 */
	private boolean chatBack = true;
	
	/**
	 * 发送发君主等级
	 */
	private int fromUsrLv;
	
	/**
	 * 发送发vip等级
	 */
	private int fromVip;
	
	/**
	 * 发送发军衔
	 */
	private int fromTitle;

	public ChatMessage() {

	}

	public ChatMessage(ChatUser fromUser, ChatUser toUser, String channel,
			String channelId, Object context) {

		// this.fromUser = fromUser;
		// this.toUser = toUser;
		this.channelType = channel;
		this.channelId = channelId;
		// this.viewOrder = 0;
		this.context = context;
		this.setFromUser(fromUser);

		this.setToUser(toUser);

	}

	public ChatMessage(ChatUser fromUser, ChatUser toUser, String channel,
			String channelId, Object context, byte priority) {

		// this.fromUser = fromUser;
		// this.toUser = toUser;
		this.channelType = channel;
		this.channelId = channelId;
		// this.viewOrder = 0;
		this.context = context;
		this.setFromUser(fromUser);

		this.setToUser(toUser);
		this.priority = priority;

	}

	public ChatMessage(long fromUserId, long toUserId, String channel,
			String channelId, Object context) {
		this.channelType = channel;
		this.channelId = channelId;
		this.context = context;
		this.fromUserId = fromUserId;
		this.toUserId = toUserId;

	}

	public ChatMessage(long fromUserId, long toUserId, String channel,
			String channelId, Object context, byte priority) {
		this.channelType = channel;
		this.channelId = channelId;
		this.context = context;
		this.fromUserId = fromUserId;
		this.toUserId = toUserId;
		this.priority = priority;
	}
	
	public ChatMessage(long fromUserId, long toUserId, String channel,
			String channelId, Object context, byte priority, boolean chatBack) {
		this.channelType = channel;
		this.channelId = channelId;
		this.context = context;
		this.fromUserId = fromUserId;
		this.toUserId = toUserId;
		this.priority = priority;
		this.chatBack = chatBack;
	}


	public void setFromUser(ChatUser fromUser) {

		if (fromUser != null) {
			this.fromUserId = fromUser.getUserId();
			this.fromUserName = fromUser.getUserName();
			this.icon = fromUser.getIcon();
			this.countryId = fromUser.getCountryId();
			this.sex = fromUser.getSex();
			this.fromUsrLv = fromUser.getUsrLv();
			this.fromVip = fromUser.getVip();
			this.fromTitle = fromUser.getTitle();
			this.tag = fromUser.getTag();

		}
	}

	public void setToUser(ChatUser toUser) {

		if (toUser != null) {
			this.toUserId = toUser.getUserId();
			this.toUserName = toUser.getUserName();
		}
	}

	public long getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(long fromUserId) {
		this.fromUserId = fromUserId;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public long getToUserId() {
		return toUserId;
	}

	public void setToUserId(long toUserId) {
		this.toUserId = toUserId;
	}

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
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

	// public int getViewOrder() {
	// return viewOrder;
	// }
	//
	// public void setViewOrder(int viewOrder) {
	// this.viewOrder = viewOrder;
	// }

	public Object getContext() {
		return context;
	}

	public void setContext(Object context) {
		this.context = context;
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

	public int getFromUsrLv() {
		return fromUsrLv;
	}

	public void setFromUsrLv(int fromUsrLv) {
		this.fromUsrLv = fromUsrLv;
	}

	public int getFromVip() {
		return fromVip;
	}

	public void setFromVip(int fromVip) {
		this.fromVip = fromVip;
	}

	public int getFromTitle() {
		return fromTitle;
	}

	public void setFromTitle(int fromTitle) {
		this.fromTitle = fromTitle;
	}

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(ChatInterface.CHAT_FROMUSERID, fromUserId);
		map.put(ChatInterface.CHAT_FROMCOUNTRY, countryId);
		// if (fromUserId > 0) {
		// UserSession us = OnlineUserSessionManager
		// .getUserSessionByUserId(fromUserId);
		// int qqFlag = us.getQqFlag();
		// if (qqFlag != 0) {
		// map.put(ChatInterface.CHAT_QQFLAG, qqFlag);
		// map.put(ChatInterface.CHAT_QQFLAG_LEVEL, us.getQqFlagLv());
		// }
		// }
		map.put(ChatInterface.CHAT_FROMUSERNAME, fromUserName);
		map.put(ChatInterface.CHAT_CHANNEL, channelType);
		if (this.channelId != null) {
			map.put(ChatInterface.CHAT_CHANNELID, this.channelId);
		}
		map.put(ChatInterface.CHAT_SEX, this.sex);
		map.put(ChatInterface.CHAT_CONTEXT, context);

		if (this.tag != null) {
			if (this.tag instanceof Map) {
				map.putAll((Map) this.tag);
			} else {
				map.put(ChatInterface.CHAT_TAG, this.tag);
			}
		}
		if (this.url != null) {
			map.put("url", this.url);
		}
		
		if (this.fromUsrLv > 0) {
			map.put(ChatInterface.CHAT_USRLV, this.fromUsrLv);
		}

		if (this.fromVip > 0) {
			map.put(ChatInterface.CHAT_VIP, this.fromVip);
		}
		
		if(this.fromTitle >0){
			map.put(ChatInterface.CHAT_TITLE, this.fromTitle);
		}

		if (this.priority != PRIORITY_MIN) {
			map.put(ChatInterface.CHAT_PRIORITY, this.priority);
		}
		map.put(ChatInterface.CHAT_ICON, this.icon);

		return map;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPf() {
		return pf;
	}

	public void setPf(String pf) {
		this.pf = pf;
	}

	public byte getPriority() {
		return priority;
	}

	public void setPriority(byte priority) {
		this.priority = priority;
	}

	public boolean isChatBack() {
		return chatBack;
	}

	public void setChatBack(boolean chatBack) {
		this.chatBack = chatBack;
	}

	public Message build() {
		Response10009Define.Builder response = Response10009Define.newBuilder();
		ResponseHead.Builder headBr = ResponseHead.newBuilder();
		headBr.setCmd(IAMF3Action.CMD_CHAT_SEND_RETURN);
		headBr.setErrCode(0);
		headBr.setErr("");
		headBr.setRequestCmd(IAMF3Action.CMD_CHAT_SEND);
		response.setResponseHead(headBr.build());

		response.setFromUserId(this.fromUserId);
		response.setFromUserName(StringUtils.trim(this.fromUserName));
		response.setIcon(StringUtils.trim(this.icon));
		response.setSex(this.sex);
		response.setCountry(this.countryId);
		response.setFromVip(this.fromVip);
		response.setChannel(StringUtils.trim(this.channelType));
		response.setContext(StringUtils.trim((String) this.context));
		response.setChannelId(StringUtils.trim(this.channelId));
		if (CommonUtil.isNotEmpty(this.url)) {
			response.setUrl(this.url);
		}
		response.setPrio(this.priority);
		response.setFromUsrLv(this.fromUsrLv);
		response.setFromTitle(this.fromTitle);
		return response.build();
	}
}

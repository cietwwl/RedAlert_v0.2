package com.youxigu.dynasty2.chat.client.impl;

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.youxigu.dynasty2.chat.BroadMessage;
import com.youxigu.dynasty2.chat.ChatChannelManager;
import com.youxigu.dynasty2.chat.ChatMessage;
import com.youxigu.dynasty2.chat.EventMessage;
import com.youxigu.dynasty2.chat.ISendMessage;
import com.youxigu.dynasty2.chat.client.IChatClientService;
import com.youxigu.dynasty2.core.wolf.IWolfClientService;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.wolf.net.AsyncWolfTask;
import com.youxigu.wolf.net.OnlineUserSessionManager;

/**
 * 
 * 
 * @author Administrator
 * 
 */
public class ChatClientService implements IChatClientService {
	public static final Logger log = LoggerFactory
			.getLogger(ChatClientService.class);

	public static final String chatServiceName = "chatService";
	private IWolfClientService wolfService;

	private boolean sendByLocal = true;

	/**
	 * 是否合并相同类型的event到list中
	 * 
	 * 只合并同一线程（同一事务）中相同userId,相同eventType的事件消息
	 */
	// private boolean mergeEventMsg = true;

	// public void setMergeEventMsg(boolean mergeEventMsg) {
	// this.mergeEventMsg = mergeEventMsg;
	// }

	public void setWolfService(IWolfClientService wolfService) {
		this.wolfService = wolfService;
	}

	public void setSendByLocal(boolean sendByLocal) {
		this.sendByLocal = sendByLocal;
	}

	public void register(User user) throws BaseException {

		AsyncWolfTask task = new AsyncWolfTask();
		task.setServiceName(chatServiceName);
		task.setMethodName("register");
		task.setParams(new Object[] { user.getUserId(), user.getUserName(),
				user.getSex(), user.getCountryId(), user.getGuildId(), null });
		// 注册到聊天Server
		wolfService.asynSendTask(task);
	}

	public void unRegister(long userId) {
		AsyncWolfTask task = new AsyncWolfTask();
		task.setServiceName(chatServiceName);
		task.setMethodName("quit");
		task.setParams(new Object[] { userId });
		// 注销聊天Server上的User
		wolfService.asynSendTask(task);
	}

	@Override
	public void sendMessage(long fromUserId, long toUserId, String channelType,
			String channelId, Object message) {
		sendMessage(fromUserId, null, toUserId, null, channelType, channelId,
				message, false);

	}

	@Override
	public void sendMessage(long fromUserId, long toUserId, String channelType,
			String channelId, Object message, boolean ingoreTrans) {
		sendMessage(fromUserId, null, toUserId, null, channelType, channelId,
				message, ingoreTrans);

	}

	@Override
	public void sendMessage(long fromUserId, String fromUserName,
			long toUserId, String toUserName, String channelType,
			String channelId, Object message) {
		sendMessage(fromUserId, fromUserName, toUserId, toUserName,
				channelType, channelId, message, false);
	}

	@Override
	public void sendMessage(long fromUserId, String fromUserName,
			long toUserId, String toUserName, String channelType,
			String channelId, Object message, boolean ingoreTrans) {
		ChatMessage chatMsg = new ChatMessage();

		chatMsg.setFromUserId(fromUserId);
		chatMsg.setToUserId(toUserId);
		chatMsg.setFromUserName(fromUserName);
		chatMsg.setToUserName(toUserName);
		chatMsg.setChannelType(channelType);
		chatMsg.setChannelId(channelId);
		chatMsg.setContext(message);
		sendMessage(chatMsg, ingoreTrans);
	}

	@Override
	public void sendMessage(ChatMessage message) {
		sendMessage(message, false);
	}

	@Override
	public void sendMessage(ChatMessage message, boolean ingoreTrans) {
		// 事件过滤
		// if (!EventMessageFilter.doFilter(message)) {
		if (!ingoreTrans && ThreadLocalMessageCache.isInTrans()) {
			// 在事务中,要等到事务结束时发送，参见MessageTransactionListener
			ThreadLocalMessageCache.addData(message);
		} else {

			if (!sendByLocal(message)) {
				AsyncWolfTask task = new AsyncWolfTask();
				task.setServiceName(chatServiceName);
				task.setMethodName("systemSendMessage");
				task.setParams(new Object[] { message });
				wolfService.asynSendTask(task);
				// if (log.isDebugEnabled()) {
				// log.debug("send by remote message:{}", JSONUtil
				// .toJson(message));
				// }
			}
		}
		// }
	}

	public void sendMessages(List<ChatMessage> messages) {
		sendMessages(messages, false);
	}

	/**
	 * 发送消息到服务器
	 * 
	 * 
	 * 
	 */
	public void sendMessages(List<ChatMessage> messages, boolean ingoreTrans) {
		// 事件过滤
		// EventMessageFilter.doFilter(messages);

		if (messages != null && messages.size() > 0) {
			if (!ingoreTrans && ThreadLocalMessageCache.isInTrans()) {
				// 在事务中,要等到事务结束时发送，参见MessageTransactionListener
				ThreadLocalMessageCache.addAll(messages);
			} else {
				// 本地消息过滤
				sendByLocal(messages);
				// 通过主服务器发送
				if (messages.size() > 0) {
					AsyncWolfTask task = new AsyncWolfTask();
					task.setServiceName(chatServiceName);
					task.setMethodName("systemSendMessages");
					task.setParams(new Object[] { messages });
					wolfService.asynSendTask(task);
					// if (log.isDebugEnabled()) {
					// log.debug("send by remote message:{}", JSONUtil
					// .toJson(messages));
					// }
				}
			}
		}
		// filterLocalUserMessage(messages)
	}

	@Override
	public void sendEventMessage(long toUserId, int eventType,
			ISendMessage params) {
		// long time = System.currentTimeMillis();
		if (ThreadLocalMessageCache.isInTrans()) {
			List<ChatMessage> allMsgs = ThreadLocalMessageCache.getData();
			if (allMsgs != null) {
				for (ChatMessage msg : allMsgs) {
					if (msg.getToUserId() == toUserId
							&& msg.getContext() instanceof EventMessage) {
						EventMessage eventMsg = (EventMessage) msg.getContext();
						if (eventMsg.getEventType() == eventType) {
							if ((params == null && eventMsg.getParams() == null)
									|| params == eventMsg.getParams()
									|| params.equals(eventMsg.getParams())) {
								// if (log.isDebugEnabled()) {
								// log.debug("同一事务中相同类型的消息发送了多次,忽略");
								// }
								return;
							}
						}
					}
				}
			}
		}

		EventMessage message = new EventMessage();
		message.setEventType(eventType);
		message.setParams(params);
		ChatMessage chatMsg = new ChatMessage();
		chatMsg.setFromUserId(0);
		chatMsg.setToUserId(toUserId);
		chatMsg.setChannelType(ChatChannelManager.CHANNEL_EVENT);
		chatMsg.setContext(message);
		this.sendMessage(chatMsg);
	}

	@Override
	public void sendBroadMessage(String channelType, String channelId,
			int eventType, ISendMessage params) {
		BroadMessage message = new BroadMessage();
		message.setEventType(eventType);
		message.setParams(params);
		ChatMessage chatMsg = new ChatMessage();
		chatMsg.setFromUserId(0);
		chatMsg.setToUserId(0);
		chatMsg.setChannelType(channelType);
		chatMsg.setChannelId(channelId);
		chatMsg.setContext(message);
		this.sendMessage(chatMsg);
	}

	public void sendMessagesAfterCommit(List<ChatMessage> messages) {
		// if (log.isDebugEnabled()) {
		// for (ChatMessage message : messages) {
		// if (message.getContext() instanceof EventMessage) {
		// EventMessage e = (EventMessage) message.getContext();
		//
		// log.debug("推Event消息{}:{}", e.getEventType(),e.getParams());
		//
		// //}
		// }
		// }
		// }
		// 本地消息过滤
		sendByLocal(messages);
		// 通过主服务器发送
		if (messages.size() > 0) {
			AsyncWolfTask task = new AsyncWolfTask();
			task.setServiceName(chatServiceName);
			task.setMethodName("systemSendMessages");
			task.setParams(new Object[] { messages });
			wolfService.asynSendTask(task);
		}
	}

	/**
	 * 能用本地连接发送的消息，先用本地连接发送
	 * 
	 * @param messages
	 */
	private void sendByLocal(List<ChatMessage> messages) {
		if (!sendByLocal)
			return;
		Iterator<ChatMessage> lit = messages.iterator();
		while (lit.hasNext()) {
			ChatMessage chatMessage = lit.next();
			if (chatMessage.getToUserId() > 0) {
				if (sendByLocal(chatMessage)) {
					lit.remove();
				}
			}
		}
	}

	private boolean sendByLocal(ChatMessage chatMessage) {
		if (!sendByLocal)
			return false;

		long toUserId = chatMessage.getToUserId();
		if (toUserId > 0 && OnlineUserSessionManager.isOnline(toUserId)) {
			// 本地发送
			Object obj = chatMessage.getContext();
			if (obj instanceof String) {
				// 普通的聊天消息
				OnlineUserSessionManager.sendMessage(toUserId,
						chatMessage.build());
				return true;
			} else if (obj instanceof EventMessage) {
				EventMessage event = (EventMessage) obj;
				OnlineUserSessionManager.sendMessage(toUserId, event.build());
				return true;
			} else if (obj instanceof BroadMessage) {
				BroadMessage event = (BroadMessage) obj;
				OnlineUserSessionManager.sendMessage(toUserId, event.build());
				return true;
			}
		}
		return false;
	}

	// private boolean sendByLocal(ChatMessage chatMessage) {
	// if (!sendByLocal)
	// return false;
	//
	// long toUserId = chatMessage.getToUserId();
	// if (toUserId > 0 && OnlineUserSessionManager.isOnline(toUserId)) {
	// // 本地发送
	// if (ChatChannelManager.isEventChannel(chatMessage.getChannelType())) {//
	// 事件消息前台要求不走聊天协议，改称独立的协议
	// Object obj = chatMessage.getContext();
	// if (obj instanceof EventMessage) {
	// EventMessage event = (EventMessage) obj;
	// int eventType = event.getEventType();
	// Map<String, Object> map = null;
	// if (eventType == 0) { // 正常请求通过消息频道返回.
	// map = (Map<String, Object>) event.getParams();
	// } else {
	// map = new HashMap<String, Object>(3);
	// map.put(IAMF3Action.ACTION_CMD_KEY, event
	// .getEventType());
	// map.put("params", event.getParams());
	// map.put(IAMF3Action.ACTION_ERROR_CODE_KEY, 0);
	// }
	// OnlineUserSessionManager.sendMessage(toUserId, map);
	//
	// return true;
	// }
	// }
	// // 普通消息 已经有cmd的，不用走CMD_CHAT_SEND
	// Object obj = chatMessage.getContext();
	// if (obj instanceof Map) {
	// if (((Map) obj).containsKey(IAMF3Action.ACTION_CMD_KEY)) {
	// OnlineUserSessionManager.sendMessage(toUserId, obj);
	// return true;
	// }
	// }
	// Map<String, Object> map = chatMessage.toMap();
	// if (!map.containsKey(IAMF3Action.ACTION_CMD_KEY)) {
	// map.put(IAMF3Action.ACTION_CMD_KEY, IAMF3Action.CMD_CHAT_SEND);
	// map.put(IAMF3Action.ACTION_ERROR_CODE_KEY, 0);
	// }
	// OnlineUserSessionManager.sendMessage(toUserId, map);
	//
	// // if (log.isDebugEnabled()) {
	// // log.debug("本地下发系统消息:{}", chatMessage.getContext());
	// // }
	// return true;
	// }
	// return false;
	// }

	@Override
	public void createChannel(String channelType, String channelId, long userId) {

		AsyncWolfTask task = new AsyncWolfTask();
		task.setServiceName(chatServiceName);
		task.setMethodName("createChannel");
		task.setParams(new Object[] { channelType, channelId, userId });
		// 注册到聊天Server
		wolfService.asynSendTask(task);

	}

	public void createChannel(String channelType, String channelId) {
		AsyncWolfTask task = new AsyncWolfTask();
		task.setServiceName(chatServiceName);
		task.setMethodName("createChannel");
		task.setParams(new Object[] { channelType, channelId });
		// 注册到聊天Server
		wolfService.asynSendTask(task);
	}

	@Override
	public void deleteChannel(String channelType, String channelId) {
		AsyncWolfTask task = new AsyncWolfTask();
		task.setServiceName(chatServiceName);
		task.setMethodName("deleteChannel");
		task.setParams(new Object[] { channelType, channelId });
		// 注册到聊天Server
		wolfService.asynSendTask(task);

	}

	@Override
	public void exitChannel(long userId, String channelType, String channelId) {
		wolfService.sendTask(Void.class, chatServiceName, "quitChannel",
				new Object[] { channelType, channelId, userId });

	}

	public void exitChannel(long userId, String channelType, String channelId,
			boolean async) {
		if (async) {
			ChatMessage chatMsg = new ChatMessage();
			chatMsg.setFromUserId(userId);
			chatMsg.setToUserId(-1);
			chatMsg.setChannelType(channelType);
			chatMsg.setChannelId(channelId);
			chatMsg.setContext(null);
			sendMessage(chatMsg);
		} else {
			exitChannel(userId, channelType, channelId);
		}
	}

	public void changeUserCountryIcon(long userId, int country, String icon) {
		wolfService
				.sendTask(Void.class, chatServiceName, "changeUserCountryIcon",
						new Object[] { userId, country, icon });
	}

	public void changeUserNameSexUsrLv(long userId, String userName, int sex,
			int usrLv) {
		wolfService.sendTask(Void.class, chatServiceName,
				"changeUserNameSexUsrLv", new Object[] { userId, userName, sex,
						usrLv });
	}

	public void changeUserGuildId(long userId, long guildId) {
		wolfService.sendTask(Void.class, chatServiceName, "changeUserGuildId",
				new Object[] { userId, guildId });
	}

	public void changeUserTitlelId(long userId, int titleId, long titleDttm) {
		wolfService.sendTask(Void.class, chatServiceName, "changeUserTitlelId",
				new Object[] { userId, titleId, titleDttm });
	}

	@Deprecated
	public void changeUserOfficalId(long userId, int officalId, long officalDttm) {
		wolfService.sendTask(Void.class, chatServiceName,
				"changeUserOfficalId", new Object[] { userId, officalId,
						officalDttm });
	}

	@Override
	public void removeAllChannelUsers(String channelType, String channelId) {
		AsyncWolfTask task = new AsyncWolfTask();
		task.setServiceName(chatServiceName);
		task.setMethodName("removeAllChannelUsers");
		task.setParams(new Object[] { channelType, channelId });
		// 注册到聊天Server
		wolfService.asynSendTask(task);
	}

	/**
	 * 得到频道内用户数
	 * 
	 * @param channelType
	 * @param channelId
	 * @return
	 */
	@Override
	public int getChannelUserNum(String channelType, String channelId) {
		return wolfService.sendTask(Integer.class, chatServiceName,
				"getChannelUserNum", new Object[] { channelType, channelId });
	}

	/**
	 * 得到频道内所有用户ID
	 * 
	 * @param channelType
	 * @param channelId
	 * @return
	 */
	@Override
	public List<Long> getChannelUserIds(String channelType, String channelId) {
		return wolfService.sendTask(List.class, chatServiceName,
				"getChannelUserIds", new Object[] { channelType, channelId });
	}

	@Override
	public void joinChannel(long userId, String channelType, String channelId) {
		AsyncWolfTask task = new AsyncWolfTask();
		task.setServiceName(chatServiceName);
		task.setMethodName("joinChannel");
		task.setParams(new Object[] { channelType, channelId, userId });
		// 注册到聊天Server
		wolfService.asynSendTask(task);
	}

	@Override
	public void joinChannelSync(long userId, String channelType,
			String channelId) throws BaseException {
		wolfService.sendTask(Void.class, chatServiceName, "joinChannel",
				new Object[] { channelType, channelId, userId });
	}

	public void mergeChannel(String channelType, String fromChannelId,
			String toChannelId) {
		AsyncWolfTask task = new AsyncWolfTask();
		task.setServiceName(chatServiceName);
		task.setMethodName("mergeChannel");
		task.setParams(new Object[] { channelType, fromChannelId, toChannelId });
		// 注册到聊天Server
		wolfService.asynSendTask(task);
	}

	@Override
	public void disableChat(long userId, long seconds) {

		disableChat(userId, seconds, true);
	}

	public void disableChat(long userId, long seconds, boolean gm) {
		AsyncWolfTask task = new AsyncWolfTask();
		task.setServiceName(chatServiceName);
		task.setMethodName("addToBlackList");

		task.setParams(new Object[] { userId, seconds, gm });
		// 注册到聊天Server
		wolfService.asynSendTask(task);
	}

	@Override
	public int isInBlackList(long userId) {

		return wolfService.sendTask(Integer.class, chatServiceName,
				"isInBlackList", new Object[] { userId });

	}

	@Override
	public void updateChatUserVip(long userId, int vip) {
		AsyncWolfTask task = new AsyncWolfTask();
		task.setServiceName(chatServiceName);
		task.setMethodName("updateChatUserVip");
		task.setParams(new Object[] { userId, vip });
		// 注销聊天Server上的User
		wolfService.asynSendTask(task);

	}

	@Override
	public boolean removeFromBlackList(long userId) {
		return removeFromBlackList(userId, true);
	}

	public boolean removeFromBlackList(long userId, boolean gm) {
		return wolfService.sendTask(Boolean.class, chatServiceName,
				"removeFromBlackList", new Object[] { userId, gm });
	}

}

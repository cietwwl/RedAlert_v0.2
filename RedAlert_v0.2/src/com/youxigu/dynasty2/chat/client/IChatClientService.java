package com.youxigu.dynasty2.chat.client;

import java.util.List;

import com.youxigu.dynasty2.chat.ChatMessage;
import com.youxigu.dynasty2.chat.ISendMessage;
import com.youxigu.dynasty2.user.domain.User;

/**
 * 
 * 聊天客户端服务 game server或其他server向聊天服务器发送消息
 * 
 * @author Administrator
 * 
 */
public interface IChatClientService {

	/**
	 * 注册用户到wolfServer
	 * 
	 * @param user
	 * @throws Exception
	 */
	void register(User user);

	void changeUserCountryIcon(long userId, int country, String icon);

	void changeUserNameSexUsrLv(long userId, String userName, int sex, int usrLv);

	void changeUserGuildId(long userId, long guildId);

	public void changeUserTitlelId(long userId, int titleId, long titleDttm);

	@Deprecated
	public void changeUserOfficalId(long userId, int officalId, long officalDttm);

	/**
	 * 注销用户
	 * 
	 * @param userId
	 */
	void unRegister(long userId);

	/**
	 * 向服务器发送消息
	 * 
	 * 当需要消息有序的在服务器端处理的时候调用
	 * 
	 * @param message
	 */
	void sendMessage(ChatMessage message);

	/**
	 * 向服务器发送消息
	 * 
	 * 当需要消息有序的在服务器端处理的时候调用
	 * 
	 * @param message
	 * @param ingoreTrans
	 *            =true ，不考虑事务处理，立即发出，即使事务失败
	 */
	void sendMessage(ChatMessage message, boolean ingoreTrans);

	void sendMessages(List<ChatMessage> messages);

	void sendMessages(List<ChatMessage> messages, boolean ingoreTrans);

	/**
	 * 不要调用该方法，只是为了事务处理用的
	 */
	void sendMessagesAfterCommit(List<ChatMessage> messages);

	/**
	 * 
	 * 向服务器发送消息
	 * 
	 * @param fromUserId
	 * @param toUserId
	 * @param channelType
	 * @param channelId
	 * @param message
	 */
	void sendMessage(long fromUserId, long toUserId, String channelType,
			String channelId, Object message);

	/**
	 * 向服务器发送消息
	 * 
	 * @param fromUserId
	 * @param fromUserName
	 * @param toUserId
	 * @param toUserName
	 * @param channelType
	 * @param channelId
	 * @param message
	 * @param ingoreTrans
	 *            =true ，不考虑事务处理，立即发出，即使事务失败
	 */
	void sendMessage(long fromUserId, long toUserId, String channelType,
			String channelId, Object message, boolean ingoreTrans);

	/**
	 * 向服务器发送消息
	 * 
	 * @param fromUserId
	 * @param fromUserName
	 * @param toUserId
	 * @param toUserName
	 * @param channelType
	 * @param channelId
	 * @param message
	 */
	void sendMessage(long fromUserId, String fromUserName, long toUserId,
			String toUserName, String channelType, String channelId,
			Object message);

	/**
	 * 向服务器发送消息
	 * 
	 * @param fromUserId
	 * @param fromUserName
	 * @param toUserId
	 * @param toUserName
	 * @param channelType
	 * @param channelId
	 * @param message
	 * @param ingoreTrans
	 *            =true ，不考虑事务处理，立即发出，即使事务失败
	 */
	void sendMessage(long fromUserId, String fromUserName, long toUserId,
			String toUserName, String channelType, String channelId,
			Object message, boolean ingoreTrans);

	/**
	 * 向前台发送事件消息
	 * 
	 * @param toUserId
	 * @param eventType
	 * @param params
	 */
	void sendEventMessage(long toUserId, int eventType, ISendMessage params);

	/**
	 * 发送广播消息
	 * 
	 * @param channelType
	 * @param channelId
	 * @param eventType
	 * @param params
	 */
	void sendBroadMessage(String channelType, String channelId, int eventType,
			ISendMessage params);

	/**
	 * 创建一个频道并将用户加入频道
	 * 
	 * @param channelType
	 * @param channelId
	 * @param userId
	 *            userId<=0则只创建频道
	 */
	void createChannel(String channelType, String channelId, long userId);

	void createChannel(String channelType, String channelId);

	/**
	 * 删除一个频道
	 * 
	 * @param channelId
	 */
	void deleteChannel(String channelType, String channelId);

	/**
	 * 用户加入频道 异步方法
	 * 
	 * @param userId
	 * @param channelId
	 * 
	 * 
	 */
	void joinChannel(long userId, String channelType, String channelId);

	/**
	 * 用户加入频道 同步方法
	 * 
	 * @param userId
	 * @param channelId
	 * 
	 * 
	 */
	void joinChannelSync(long userId, String channelType, String channelId);

	/**
	 * 用户退出频道
	 * 
	 * @param userId
	 * @param channelId
	 */
	void exitChannel(long userId, String channelType, String channelId);

	/**
	 * 退出频道--异步，此方法的目的是防止有些消息发不出去
	 * 
	 * @param userId
	 * @param channelType
	 * @param channelId
	 * @param async
	 */
	void exitChannel(long userId, String channelType, String channelId,
			boolean async);

	/**
	 * 删除频道内所有用户
	 * 
	 * @param channelType
	 * @param channelId
	 */
	void removeAllChannelUsers(String channelType, String channelId);

	/**
	 * 得到频道内用户数
	 * 
	 * @param channelType
	 * @param channelId
	 * @return
	 */
	int getChannelUserNum(String channelType, String channelId);

	/**
	 * 得到频道内所有用户ID
	 * 
	 * @param channelType
	 * @param channelId
	 * @return
	 */
	List<Long> getChannelUserIds(String channelType, String channelId);

	/**
	 * 合并频道：联盟合并使用
	 * 
	 * @param channelType
	 * @param fromChannelId
	 * @param toChannelId
	 */
	public void mergeChannel(String channelType, String fromChannelId,
			String toChannelId);

	/**
	 * 用户禁言
	 * 
	 * @param userId
	 * @param seconds
	 * 
	 */
	void disableChat(long userId, long seconds);

	void disableChat(long userId, long seconds, boolean gm);

	/**
	 * 是否在禁言名单中,返回剩余禁言时间
	 * 
	 * @param userId
	 * @return
	 */
	int isInBlackList(long userId);

	/**
	 * 解除禁言
	 * 
	 * @param userName
	 */
	public boolean removeFromBlackList(long userId);

	public boolean removeFromBlackList(long userId, boolean gm);

	/**
	 * 更新VIP等级
	 * 
	 * @param userId
	 * @param vip
	 */
	void updateChatUserVip(long userId, int vip);
}

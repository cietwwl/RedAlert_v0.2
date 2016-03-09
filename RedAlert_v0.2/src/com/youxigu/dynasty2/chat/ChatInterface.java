package com.youxigu.dynasty2.chat;

import java.util.List;

/**
 * 聊天系统对外功能的统一接口
 * 
 * @author Phoeboo
 * @version 1.0 2011.01.19
 */
public interface ChatInterface {
	public static String CHAT_FROMUSERID = "fromUserId";
	public static String CHAT_FROMUSERNAME = "fromUserName";
	public static String CHAT_FROMCOUNTRY = "country";// 国家Id
	public static String CHAT_CHANNEL = "channel";
	public static String CHAT_CHANNELID = "channelId";
	public static String CHAT_CONTEXT = "context";
	public static String CHAT_SEX = "sex";// 性别
	public static String CHAT_TAG = "tag";// 其他
	public static String CHAT_VIP = "vip";// VIP级别
	public static String CHAT_USRLV = "usrLv";// 君主级别
	public static String CHAT_TITLE = "title";// VIP级别
	// 2=兵神 3=军神
	public static String CHAT_PRIORITY = "prio";// 优先级

	public static String CHAT_ICON = "icon";// 头像
	// public static String CHAT_QQFLAG = "qf";// QQ标志
	// public static String CHAT_QQFLAG_LEVEL = "qfl";// QQ标志等级

	/**
	 * 系统发送消息（个人，全局）
	 * 
	 * @param chatMessage
	 */
	public void systemSendMessage(ChatMessage chatMessage);

	/**
	 * 系统发送多条消息（个人，全局）
	 * 
	 * @param List
	 *            <ChatMessage>
	 */
	public void systemSendMessages(List<ChatMessage> list);

	/**
	 * 玩家发送消息（世界，国家，联盟，战场,私聊）
	 * 
	 * @param chatMessage
	 */
	public void userSendMessage(ChatMessage chatMessage);

	/**
	 * 玩家注册所有频道--比如世界，国家，联盟等固定频道 一般在玩家登录时调用
	 * 
	 * @param userId
	 * @return
	 */
	public ChatUser register(long userId, String userName, String icon, int sex,
			int countryId, long guildId, int usrLv, int vip, int title,
			Object tag);

	/**
	 * 修改名称，性别，等级 时需要调用
	 * 
	 * @param userId
	 * @param name
	 * @param sex
	 * @param usrLv
	 */
	void changeUserNameSexUsrLv(long userId, String name, int sex, int usrLv);

	/**
	 * 转国，改头像时调用
	 * 
	 * @param userId
	 * @param country
	 * @param icon
	 */
	public void changeUserCountryIcon(long userId, int country, String icon);

	/**
	 * 联盟id变化时调用
	 * 
	 * @param userId
	 * @param guildId
	 */
	public void changeUserGuildId(long userId, long guildId);

	/**
	 * vip等级变化时调用
	 * 
	 * @param userId
	 * @param vip
	 */
	public void updateChatUserVip(long userId, int vip);

	/**
	 * 军衔变化时调用
	 * 
	 * @param userId
	 * @param title
	 */
	public void updateChatUserTitle(long userId, int title);

	/**
	 * 玩家注销所有频道--比如世界，国家，联盟频道 一般在玩家退出时调用
	 * 
	 * @param userId
	 * @return
	 */
	public ChatUser quit(long userId);

	/**
	 * 创建频道
	 */
	public void createChannel(String channelType, String channelId,
			long userId);

	public void createChannel(String channelType, String channelId);

	// public void createChannel( String channelType, String channelId,int
	// maxUsers,boolean removeUserWhenClose);

	/**
	 * 删除某个频道内的所有用户
	 * 
	 * @param channelType
	 * @param channelId
	 */
	public void removeAllChannelUsers(String channelType, String channelId);

	/**
	 * 销毁频道
	 */
	public void deleteChannel(String channelType, String channelId);

	/**
	 * 玩家加入指定类型频道
	 * 
	 * @param userId
	 * @param channelType
	 * @param channelId
	 */
	public void joinChannel(String channelType, String channelId, long userId);

	/**
	 * 退出指定类型频道
	 * 
	 * @param userId
	 * @param channelType
	 * @param channelId
	 */
	public void quitChannel(String channelType, String channelId, long userId);

	/**
	 * 得到频道内用户数
	 * 
	 * @param channelType
	 * @param channelId
	 * @return
	 */
	public int getChannelUserNum(String channelType, String channelId);

	/**
	 * 得到频道内所有用户ID
	 * 
	 * @param channelType
	 * @param channelId
	 * @return
	 */
	public List<Long> getChannelUserIds(String channelType, String channelId);

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
	 * 玩家禁言
	 * 
	 * @param userName
	 * @param hour
	 * @param min
	 */
	public void addToBlackList(long userId, long seconds);

	public void addToBlackList(long userId, long seconds, boolean gm);

	public int isInBlackList(long userId);

	/**
	 * 解除禁言
	 * 
	 * @param userName
	 */
	public boolean removeFromBlackList(long userId);

	public boolean removeFromBlackList(long userId, boolean gm);

	/**
	 * gm发送系统消息
	 * 
	 * @param channel
	 *            频道 system 一般系统消息 title 公告
	 * @param message
	 *            消息内容
	 * @param toUserName
	 *            只有对单个特定玩家时使用 channel 为 system ，其他情况为null
	 * @param startWeekDay
	 *            起始的星期数 sun:1 mon :2 sat:7 <=0 每天
	 * @param endWeekDay
	 * @param startDayHour
	 *            起始小时数 0-23
	 * @param endDayHour
	 * @param startMin
	 *            0-59
	 * @param endMin
	 * @param repeatNum
	 *            时间段内 重复次数
	 * @param order
	 *            优先级
	 */
	public void gmAddSysMessage(String channel, String message,
			String toUserName, int startWeekDay, int endWeekDay,
			int startDayHour, int endDayHour, int startMin, int endMin,
			int repeatNum, int order);

	public void echoError(long userId, String error);

	public void echoError(long userId, String error, String channel,
			String channelId);

	// /**
	// * 更新所有玩家的聊天官职数据
	// *
	// * @param allUserOfficials
	// */
	// public void resetAllOfficial(Map<Long, UserOfficial>
	// allUserOfficials,Map<Long, UserOfficial> allCivilOfficials);

}

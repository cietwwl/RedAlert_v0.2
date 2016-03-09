package com.youxigu.dynasty2.chat;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.youxigu.wolf.net.ISessionListener;
import com.youxigu.wolf.net.Response;
import com.youxigu.wolf.net.UserSession;

/**
 * 所有频道管理类
 * 
 * @author Phoeboo
 * @version 1.0 2011.01.19
 */
public class ChatChannelManager implements ISessionListener {
	private static Logger log = LoggerFactory
			.getLogger(ChatChannelManager.class);
	public static final String CHANNEL_SYSTEM = "system";
	public static final String CHANNEL_HELP = "help";
	public static final String CHANNEL_PRIVATE = "private";
	public static final String CHANNEL_WORLD = "world";
	public static final String CHANNEL_NOTICE = "notice";// 公告

	public static final String CHANNEL_COUNTRY = "country";
	public static final String CHANNEL_ITEM = "item";
	// @Deprecated
	// public static final String CHANNEL_LEAGUE = "league";// 联赛频道
	public static final String CHANNEL_GUILD = "guild"; // 联盟频道
	// @Deprecated
	// public static final String CHANNEL_COMBAT = "combat";
	// @Deprecated
	// public static final String CHANNEL_TITLE = "title";
	// @Deprecated
	// public static final String CHANNEL_TITLE_SMALL = "title_small";
	// @Deprecated
	// public static final String CHANNEL_TITLE_BIG = "title_big";
	public static final String CHANNEL_EVENT = "event";
	// @Deprecated
	// public static final String CHANNEL_ERROR = "error";// 错误提示频道
	// @Deprecated
	// public static final String CHANNEL_RISKCOPY = "risk_copy";// 冒险副本
	// @Deprecated
	// public static final String CHANNEL_GUILD_WORLD =
	// "guild_world";//世界频道广播给联盟内的所有人

	///// public static final String CHANNEL_HALL = "hall"; //大厅频道：对应某个玩法的频道

	// 冒险副本目前用的频道
	// public static final String CHANNEL_RISK_HALL = "risk_hall";// 冒险副本频道
	// public static final String CHANNEL_RISK_DATA = "risk_data";// 冒险数据频道
	// public static final String CHANNEL_MIGIC_CAVE = "magic_cave";//神秘洞频道
	// public static final String CHANNEL_GUILD_PK = "guild_pk";//联盟PK频道

	// //逐鹿中原频道列表
	// public static final String CHANNEL_COUNTRY_WAR =
	// "c_war";//逐鹿中原频道，攻击防御双方的频道,后台用来广播消息
	// public static final String CHANNEL_COUNTRY_WAR_ATK =
	// "c_war_atk";//逐鹿中原频道，攻击，前台聊天，喊话用
	// public static final String CHANNEL_COUNTRY_WAR_DEF = "c_war_def";//逐鹿中原频道
	// ，防御，前台聊天，喊话用
	//
	// public static final String CHANNEL_GUILD_WAR =
	// "g_war";//联盟战（古迹）频道，攻击防御双方的频道,后台用来广播消息
	// public static final String CHANNEL_GUILD_WAR_ATK =
	// "g_war_atk";//联盟战（古迹）频道，攻击，前台聊天，喊话用
	// public static final String CHANNEL_GUILD_WAR_DEF =
	// "g_war_def";//联盟战（古迹）频道 ，防御，前台聊天，喊话用
	//
	//
	// public static final String CHANNEL_COUNTRY_OFFICAL = "c_offical";//国家官职频道
	//
	//
	// public static final String CHANNEL_LUCYY_RANK = "luck_rank";//幸运榜频道
	// //public static final String CHANNEL_MUTI_FISH = "muti_fish";//多人捕鱼频道
	//
	// public static final String CHANNEL_EGG = "egg";//砸蛋频道
	// public static final String CHANNEL_CONSUME = "consume";//消费反利频道
	// @Deprecated
	// public static final String CHANNEL_ARENA = "arena";//竞技场频道
	// public static final String CHANNEL_LANTERNFESTIVAL = "lanternFestival";//
	// 元宵节活动频道
	// public static final String CHANNEL_SPRINGFESTIVAL = "springFestival";//
	// 春节活动频道
	// public static final String CHANNEL_RECHARGE = "recharge";//充值返利频道
	//
	//
	// //PVP出征频道
	// public static final String CHANNEL_PVP_OUT_WAR = "pvp_out";
	//
	// //抗击匈奴频道
	// public static final String CHANNEL_HUNS_WAR = "huns_war";
	//
	//
	// public static final String CHANNEL_TIMER_SHOP = "timer_shop";

	private static final HashMap<String, String> userCanChatChannels = new HashMap<String, String>();

	private static final HashMap<String, String> eventChannels = new HashMap<String, String>();

	private ConcurrentHashMap<String, ChatChannel> channelList = new ConcurrentHashMap<String, ChatChannel>(); // 当前所有频道列表

	// 世界频道 -- 所有在线玩家缓存
	private ConcurrentHashMap<Long, ChatUser> worldChannel = new ConcurrentHashMap<Long, ChatUser>();

	// 世界频道 -- 所有在线玩家缓存
	private ConcurrentHashMap<String, ChatUser> worldChannelStr = new ConcurrentHashMap<String, ChatUser>();

	private static ChatChannelManager chatChannelManager = new ChatChannelManager();;

	private ChatChannelManager() {
		userCanChatChannels.put(CHANNEL_WORLD, CHANNEL_WORLD);
		userCanChatChannels.put(CHANNEL_ITEM, CHANNEL_ITEM);
		userCanChatChannels.put(CHANNEL_NOTICE, CHANNEL_NOTICE);

		userCanChatChannels.put(CHANNEL_PRIVATE, CHANNEL_PRIVATE);
		userCanChatChannels.put(CHANNEL_COUNTRY, CHANNEL_COUNTRY);
		// userCanChatChannels.put(CHANNEL_LEAGUE, CHANNEL_LEAGUE);
		userCanChatChannels.put(CHANNEL_GUILD, CHANNEL_GUILD);
		eventChannels.put(CHANNEL_EVENT, CHANNEL_EVENT);
		// userCanChatChannels.put(CHANNEL_COMBAT, CHANNEL_COMBAT);
		// userCanChatChannels.put(CHANNEL_RISK_DATA, CHANNEL_RISK_DATA);

		// userCanChatChannels.put(CHANNEL_COUNTRY_WAR, CHANNEL_COUNTRY_WAR);
		// userCanChatChannels.put(CHANNEL_COUNTRY_WAR_ATK,
		// CHANNEL_COUNTRY_WAR_ATK);
		// userCanChatChannels.put(CHANNEL_COUNTRY_WAR_DEF,
		// CHANNEL_COUNTRY_WAR_DEF);
		//
		// userCanChatChannels.put(CHANNEL_GUILD_WAR, CHANNEL_GUILD_WAR);
		// userCanChatChannels.put(CHANNEL_GUILD_WAR_ATK,
		// CHANNEL_GUILD_WAR_ATK);
		// userCanChatChannels.put(CHANNEL_GUILD_WAR_DEF,
		// CHANNEL_GUILD_WAR_DEF);
		//
		//
		//
		// userCanChatChannels.put(CHANNEL_PVP_OUT_WAR, CHANNEL_PVP_OUT_WAR);
		// userCanChatChannels.put(CHANNEL_HUNS_WAR, CHANNEL_HUNS_WAR);
	}

	public static ChatChannelManager getInstance() {

		return chatChannelManager;
	}

	// 系统发送消息时的频道是否为单发频道（即只发送给指定玩家）
	public boolean isSingleChannel(String channel) {

		if (channel.equals(CHANNEL_PRIVATE) || channel.equals(CHANNEL_HELP)
		/*
		 * || channel.equals(CHANNEL_TITLE) ||
		 * channel.equals(CHANNEL_TITLE_SMALL) ||
		 * channel.equals(CHANNEL_TITLE_BIG)
		 */)
			return true;
		return false;
	}

	// 判断频道是否对用户开放
	public boolean isValidChannel(String channel) {

		return (userCanChatChannels.containsKey(channel));
	}

	// 获取最后的频道名称（频道标志Key）
	public String getChannelName(String channelType, String channelId) {

		if (channelType == null)
			channelType = CHANNEL_COUNTRY; // 空则默认为国家频道
		if (channelId == null)
			return channelType;
		// if ( CHANNEL_SYSTEM.equals( channelType ) || CHANNEL_HELP.equals(
		// channelType ) || CHANNEL_PRIVATE.equals( channelType ) ||
		// CHANNEL_WORLD.equals( channelType ) ) return channelType;

		return channelType + channelId;
	}

	// 获取频道
	public ChatChannel getChatChannel(String channelType, String channelId) {

		return createChatChannel(channelType, channelId);
	}

	// 创建频道

	public ChatChannel createChatChannel(String channelType, String channelId) {

		String channelName = getChannelName(channelType, channelId);
		ChatChannel channel = channelList.get(channelName);
		if (channel == null) {
			synchronized (this) {// 保证不重复创建
				channel = channelList.get(channelName);
				if (channel == null) {
					channel = new ChatChannel(channelType, channelId);
					ChatChannel tmp = channelList.putIfAbsent(channelName,
							channel);
					if (tmp != null) {
						channel = tmp;
					}
				}
			}
		}

		return channel;
	}

	// 销毁频道
	public ChatChannel removeChatChannel(String channelType, String channelId) {
		//
		// ChatChannel cc = getChatChannel( channelType, channelId );
		// if ( cc == null ) return;

		// Map<String, ChatUser> cuList = cc.getUserList();
		//
		// if ( cuList != null )
		// for ( ChatUser cu: cuList.values() ) {
		// cu.getChannelList().remove( cu.getUserName() );
		// }

		String channelName = getChannelName(channelType, channelId);
		return channelList.remove(channelName);
	}

	// 获取世界频道1
	public Map<Long, ChatUser> getWorldChannel() {

		return worldChannel;
	}

	// 获取世界频道2
	public Map<String, ChatUser> getWorldChannelStr() {

		return worldChannelStr;
	}

	// 玩家注册到世界频道
	public void registerWorld(ChatUser chatUser) {

		if (chatUser == null)
			return;
		worldChannel.put(chatUser.getUserId(), chatUser);
		worldChannelStr.put(chatUser.getUserName(), chatUser);
	}

	// 玩家注销世界频道
	public void removeWorld(ChatUser chatUser) {

		if (chatUser == null)
			return;
		worldChannel.remove(chatUser.getUserId());
		worldChannelStr.remove(chatUser.getUserName());
	}

	// 获取ChatUser
	public ChatUser getChatUser(long userId) {

		return worldChannel.get(userId);
	}

	// 获取ChatUser
	public ChatUser getChatUser(String userName) {

		return worldChannelStr.get(userName);
	}

	// 获取频道列表
	public Map<String, ChatChannel> getChannelList() {
		return channelList;
	}

	// 用户退出
	public ChatUser quit(long userId) {
		// 从世界频道获取玩家
		if (log.isDebugEnabled()) {
			log.debug("用户{}退出聊天系统", userId);
		}
		ChatUser chatUser = getChatUser(userId);
		if (chatUser != null) {

			removeWorld(chatUser);

			// Iterator<Entry<String,ChatChannel>> lit =
			// getChannelList().entrySet().iterator();
			// while (lit.hasNext()){
			// Entry<String,ChatChannel> entry = lit.next();
			// entry.getValue().removeUser(chatUser);
			// entry.getValue().
			// }
			for (ChatChannel chatChannel : getChannelList().values()) {
				if (chatChannel != null /*
										 * &&
										 * chatChannel.isRemoveUserWhenClose()
										 */) {
					chatChannel.removeUser(chatUser);
					// TODO: 没有用户的情况下是否删除频道？
				}
			}

		}
		return chatUser;
	}

	@Override
	public void close(Response response) {
		IoSession ioSession = response.getSession();
		UserSession us = (UserSession) ioSession
				.getAttribute(UserSession.KEY_USERSESSION);
		if (us != null && us.getUserId() != 0) {
			quit(us.getUserId());
		}
	}

	@Override
	public void open(Response response) {

	}

	public static boolean isEventChannel(String channelType) {
		return eventChannels.containsKey(channelType);
	}
}

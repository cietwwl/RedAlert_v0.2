package com.youxigu.dynasty2.chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.youxigu.dynasty2.chat.client.impl.ThreadLocalMessageCache;

/**
 * IChatClientService.sendEventMessage向前台发送一些事件
 * 
 * 
 * 有些从Action来的事件，前台要求在Action中返回改变的数据，不通过后台sendEventMessane向前台发送， 这里是为了过滤这类事件
 * 
 * @author Administrator
 * 
 */
public class MessageFilter {
	public static final Logger log = LoggerFactory
			.getLogger(MessageFilter.class);

	// 要过滤的频道ID：
	private static ThreadLocal<Map<String, Object>> channelFilters = new ThreadLocal<Map<String, Object>>();

	// 要过滤的事件ID:事件都是通过系统频道发送的
	private static ThreadLocal<Map<Integer, Object>> eventFilters = new ThreadLocal<Map<Integer, Object>>();

	// 要过滤的消息的用户ID :如果是0，则指所有用户
	private static ThreadLocal<Long> userIdLocal = new ThreadLocal<Long>();

	// 是否已经过滤
	private static ThreadLocal<Boolean> hasFiltered = new ThreadLocal<Boolean>() {
		protected Boolean initialValue() {
			return false;
		}
	};

	/**
	 * 被过滤掉的消息缓存 key = userId value = map key= 事件ID,value = 事件值
	 */
	private static ThreadLocal<Map<Long, FilterMessages>> events = new ThreadLocal<Map<Long, FilterMessages>>() {
		protected Map<Long, FilterMessages> initialValue() {
			return new HashMap<Long, FilterMessages>();
		}
	};

	public static void setUserId(long userId) {
		userIdLocal.set(userId);
	}

	public static void doFilter(List<ChatMessage> messages,
			Map<Long, FilterMessages> filterEvents) {
		hasFiltered.set(true);

		if (messages==null || messages.size() == 0) {
			return;
		}
		Map<Integer, Object> eventFilter = eventFilters.get();
		Map<String, Object> channelFilter = channelFilters.get();
		if (eventFilter == null && channelFilter == null) {
			return;
		}
		Long uId = userIdLocal.get();
		if (uId != null) {
			Iterator<ChatMessage> lit = messages.iterator();
			while (lit.hasNext()) {
				ChatMessage msg = lit.next();
				long msgUserId = msg.getToUserId();
				if (uId == 0 || msgUserId == uId) {
					if (msg.getContext() instanceof EventMessage) {
						// 事件消息
						EventMessage eventMsg = (EventMessage) msg.getContext();
						if ((eventFilter != null && eventFilter
								.containsKey(eventMsg.getEventType()))
								|| (channelFilter != null && channelFilter
										.containsKey(msg.getChannelType()))) {
							FilterMessages userMessages = filterEvents
									.get(msgUserId);
							if (userMessages == null) {
								userMessages = new FilterMessages();
								userMessages.setUserId(msgUserId);
								filterEvents.put(msgUserId, userMessages);
							}
							userMessages.addFilterMessage(eventMsg);
							lit.remove();
						}

					} else {
						// 普通文本消息
						if (channelFilter != null
								&& channelFilter.containsKey(msg
										.getChannelType())) {
							FilterMessages userMessages = filterEvents
									.get(msgUserId);
							if (userMessages == null) {
								userMessages = new FilterMessages();
								userMessages.setUserId(msgUserId);
								filterEvents.put(msgUserId, userMessages);
							}
							userMessages.addFilterMessage(msg.toMap());
							lit.remove();
						}
					}
				}
			}

		}

	}

	public static boolean hasFiltered() {
		return hasFiltered.get();
	}

	public static Map<Long, FilterMessages> getEvents() {
		return events.get();
	}

	public static FilterMessages getEventsByUserId(long userId) {
		Map<Long, FilterMessages> datas = events.get();
		if (datas != null) {
			return datas.get(userId);
		}
		return null;
	}

	// private static void setEventsByUserId(long userId, FilterMessages
	// userMsgs) {
	// Map<Long, FilterMessages> datas = events.get();
	// if (datas == null) {
	// datas = new HashMap<Long, FilterMessages>();
	// events.set(datas);
	// }
	// datas.put(userId, userMsgs);
	//
	// }

	public static List<FilterMessages> getAndRemoveFilterEvents() {

		Map<Long, FilterMessages> filterMessages = new HashMap<Long, FilterMessages>();
		doFilter(ThreadLocalMessageCache.getData(), filterMessages);
		int count = filterMessages.size();
		if (count > 0) {
			// 这里如何才能不转换成List?
			List<FilterMessages> msgs = new ArrayList<FilterMessages>(count);
			msgs.addAll(filterMessages.values());
			return msgs;
		}
		return null;

	}

	// public static Map<String, List<Object>> doFilterInTrans() {
	// List<ChatMessage> messages = ThreadLocalMessageCache.getData();
	//
	// Map<String, List<Object>> datas = null;// 返回的过滤消息
	//
	// Map<Integer, Object> tmp = filters.get();
	// if (tmp == null) {
	// return null;
	// }
	// Long uId = userIdLocal.get();
	// if (uId != null) {
	// Iterator<ChatMessage> lit = messages.iterator();
	// while (lit.hasNext()) {
	// ChatMessage msg = lit.next();
	// if (msg.getToUserId() == uId
	// && msg.getContext() instanceof EventMessage) {
	// EventMessage eventMsg = (EventMessage) msg.getContext();
	// if (eventMsg.getParams() != null) {
	// int eventType = eventMsg.getEventType();
	// if (tmp.containsKey(eventType)) {
	// if (datas == null) {
	// datas = new HashMap<String, List<Object>>();
	// }
	// String eventTypeKey = EventMessage
	// .getEventKey(eventType);
	//
	// List<Object> list = datas.get(eventTypeKey);
	// if (list == null) {
	// list = new ArrayList<Object>();
	// datas.put(eventTypeKey, list);
	// }
	// Object params = eventMsg.getParams();
	// if (params instanceof List) {
	// list.addAll((List) eventMsg.getParams());
	// } else if (params!=null){
	// list.add(eventMsg.getParams());
	// }
	// lit.remove();
	// }
	// }
	// }
	// }
	// if (log.isDebugEnabled()) {
	// log.debug("分段过滤消息:" + datas);
	// }
	// return datas;
	// }
	// return null;
	// }

	public static void addFilterEventType(int type) {
		Map<Integer, Object> tmp = eventFilters.get();
		if (tmp == null) {
			tmp = new HashMap<Integer, Object>();
			eventFilters.set(tmp);
		}
		tmp.put(type, type);
	}

	public static void addFilterEventTypes(Map<Integer, Object> eventTypes) {
		if (eventTypes == null) {
			return;
		}
		Map<Integer, Object> tmp = eventFilters.get();
		if (tmp == null) {
			eventFilters.set(eventTypes);
		} else {
			tmp.putAll(eventTypes);
		}
	}

	public static void addFilterChannel(String channel) {
		Map<String, Object> tmp = channelFilters.get();
		if (tmp == null) {
			tmp = new HashMap<String, Object>();
			channelFilters.set(tmp);
		}
		tmp.put(channel, channel);
	}

	public static void addFilterChannels(Map<String, Object> channels) {
		if (channels == null) {
			return;
		}

		Map<String, Object> tmp = channelFilters.get();
		if (tmp == null) {
			channelFilters.set(channels);
		} else {
			tmp.putAll(channels);
		}
	}

	public static void setFilterEventTypes(Map<Integer, Object> eventTypes) {
		eventFilters.set(eventTypes);
	}

	public static void clear() {
		channelFilters.remove();
		eventFilters.remove();
		events.remove();
		userIdLocal.remove();
		hasFiltered.remove();
	}

	public static class FilterMessages implements java.io.Serializable {
		private long userId;
		private List<Object> messages;

		public long getUserId() {
			return userId;
		}

		public void setUserId(long userId) {
			this.userId = userId;
		}

		public List<Object> getMessages() {
			return messages;
		}

		public void setMessages(List<Object> messages) {
			this.messages = messages;
		}

		public void addFilterMessage(Object msg) {
			if (messages == null) {
				messages = new ArrayList<Object>();
			}
			messages.add(msg);
		}

	}
}

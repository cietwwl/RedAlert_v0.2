package com.youxigu.dynasty2.chat.client.impl;

import java.util.LinkedList;
import java.util.List;

import com.youxigu.dynasty2.chat.ChatChannelManager;
import com.youxigu.dynasty2.chat.ChatMessage;

public class ThreadLocalMessageCache {
	private static ThreadLocal<Boolean> inTrans = new ThreadLocal<Boolean>() {
		protected Boolean initialValue() {
			return Boolean.FALSE;
		}

	};

	private static ThreadLocal<List<ChatMessage>> messages = new ThreadLocal<List<ChatMessage>>() {
		// protected List<ChatMessage> initialValue() {
		// return new ArrayList<ChatMessage>();
		// }
	};

	public static void clean() {
		messages.remove();
		inTrans.set(false);
	}

	public static void commit() {
		// do nothing
	}

	public static List<ChatMessage> getData() {
		return messages.get();
	}

	public static void addData(ChatMessage msg) {
		List<ChatMessage> datas = messages.get();
		if (datas == null) {
			datas = new LinkedList<ChatMessage>();
			messages.set(datas);
		} else {
			// 这里频道类型都是常量，因此可以用==来判断
			if (ChatChannelManager.CHANNEL_SYSTEM == msg.getChannelType()
					&& (msg.getContext() instanceof String)) {
				// 系统频道的发送给同一个玩家的字符串类型消息，合并成一个
				for (ChatMessage tmp : datas) {
					if (tmp.getFromUserId() == msg.getFromUserId()
							&& tmp.getToUserId() == msg.getToUserId()
							&& ChatChannelManager.CHANNEL_SYSTEM == tmp
									.getChannelType()
							&& (tmp.getContext() instanceof String)) {
						String sub1=(String)tmp.getContext();
						String sub2=(String)msg.getContext();						
						StringBuilder sb= new StringBuilder(sub1.length()+sub2.length()+5);
						sb.append(sub1).append("<br/>").append(sub2);
						tmp.setContext(sb.toString());
						
						return;
					}
				}
			}
		}
		datas.add(msg);
	}

	public static void addAll(List<ChatMessage> objs) {
		List<ChatMessage> datas = messages.get();
		datas.addAll(objs);
	}

	public static void setInTrans(boolean trans) {
		inTrans.set(trans);
	}

	public static boolean isInTrans() {
		return inTrans.get();
	}

}

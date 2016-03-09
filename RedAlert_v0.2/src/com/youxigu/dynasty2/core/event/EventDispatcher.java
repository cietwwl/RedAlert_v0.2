package com.youxigu.dynasty2.core.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 事件分发器
 * 
 * @author Administrator
 * 
 */
public class EventDispatcher {

	private static Map<Integer, List<IEventListener>> listeners = new HashMap<Integer, List<IEventListener>>();

	public static void registerListener(int eventType, IEventListener listener) {
		List<IEventListener> list = listeners.get(eventType);
		if (list == null) {
			list = new ArrayList<IEventListener>();
			listeners.put(eventType, list);
		}
		list.add(listener);
	}

	/**
	 * 注册成拦截所有事件的listener
	 * 
	 * @param listener
	 */
	public static void registerListener(IEventListener listener) {
		registerListener(EventTypeConstants.EVT_DEFAULT, listener);
	}

	/**
	 * 分发事件
	 * 
	 * @param event
	 */
	public static void fireEvent(Event event) {
		List<IEventListener> list = listeners.get(event.getEventType());
		if (list != null) {
			for (IEventListener listener : list) {
				listener.doEvent(event);
			}
		}

		// 查找default listener
		list = listeners.get(EventTypeConstants.EVT_DEFAULT);
		if (list != null) {
			for (IEventListener listener : list) {
				listener.doEvent(event);
			}
		}
	}
}

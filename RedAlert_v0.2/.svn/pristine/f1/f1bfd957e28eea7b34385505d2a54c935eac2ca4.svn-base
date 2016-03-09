package com.youxigu.wolf.minicache.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import com.youxigu.wolf.minicache.IRemoteLRUQueueCacheService;

public class RemoteLRUQueueCacheService implements IRemoteLRUQueueCacheService {
	private Map<Object, ArrayBlockingQueue<Object>> cacheDatas = new ConcurrentHashMap<Object, ArrayBlockingQueue<Object>>();

	private int defaultCapacity = 100;

	public void setDefaultCapacity(int defaultCapacity) {
		this.defaultCapacity = defaultCapacity;
	}

	public void setInitQueues(List<Map<String, String>> params) {
		for (Map<String, String> map : params) {
			String groupKey = map.get("queueName");
			String capacity = map.get("capacity");
			createCache(groupKey, Integer.parseInt(capacity));
		}
	}

	@Override
	public void add(Object groupKey, Object value, boolean async) {
		ArrayBlockingQueue<Object> queue = cacheDatas.get(groupKey);
		if (queue == null) {
			queue = new ArrayBlockingQueue<Object>(defaultCapacity);
			cacheDatas.put(groupKey, queue);
		}
		while (!queue.offer(value)) {
			queue.poll();
		}

	}

	@Override
	public void clear(Object groupKey) {
		ArrayBlockingQueue<Object> queue = cacheDatas.get(groupKey);
		if (queue != null) {
			queue.clear();
		}
	}

	@Override
	public void createCache(Object groupKey, int capacity) {
		cacheDatas.put(groupKey, new ArrayBlockingQueue<Object>(capacity));

	}

	@Override
	public Object[] getAll(Object groupKey) {
		ArrayBlockingQueue<Object> queue = cacheDatas.get(groupKey);
		if (queue != null) {
			return queue.toArray();
		}
		return null;

	}

}

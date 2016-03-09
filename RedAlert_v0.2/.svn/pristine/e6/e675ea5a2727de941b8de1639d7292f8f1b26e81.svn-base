package com.youxigu.wolf.minicache.client;

import com.youxigu.dynasty2.core.wolf.IWolfClientService;
import com.youxigu.wolf.minicache.IRemoteLRUQueueCacheService;
import com.youxigu.wolf.net.AsyncWolfTask;

public class RemoteLRUQueueCacheServiceClient implements
		IRemoteLRUQueueCacheService {

	public static final String MAPCACHE_SERVICE = "wolf_LRUCacheService";

	private IWolfClientService wolfService;

	public void setWolfService(IWolfClientService wolfService) {
		this.wolfService = wolfService;
	}

	@Override
	public void add(Object groupKey, Object value, boolean async) {
		if (async) {
			AsyncWolfTask task = new AsyncWolfTask();
			task.setServiceName(MAPCACHE_SERVICE);
			task.setMethodName("add");
			task.setParams(new Object[] { groupKey, value,async });
			// 注册到聊天Server
			wolfService.asynSendTask(task);
		} else {
			wolfService.sendTask(Void.class, MAPCACHE_SERVICE, "add",
					new Object[] { groupKey, value });
		}

	}

	@Override
	public void clear(Object groupKey) {
		wolfService.sendTask(Void.class, MAPCACHE_SERVICE, "clear",
				new Object[] { groupKey });

	}

	@Override
	public void createCache(Object groupKey, int capacity) {
		wolfService.sendTask(Void.class, MAPCACHE_SERVICE, "createCache",
				new Object[] { groupKey, capacity });

	}

	@Override
	public Object[] getAll(Object groupKey) {
		return wolfService.sendTask(Object[].class, MAPCACHE_SERVICE, "getAll",
				new Object[] { groupKey });
	}

}

package com.youxigu.wolf.minicache.client;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.youxigu.dynasty2.core.wolf.IWolfClientService;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.wolf.minicache.IRemoteMapCacheService;
import com.youxigu.wolf.net.AsyncWolfTask;

public class RemoteMapCacheServiceClient implements IRemoteMapCacheService {
	public static final Logger log = LoggerFactory.getLogger(RemoteMapCacheServiceClient.class);
	
	public static final String MAPCACHE_SERVICE="wolf_CacheService";
	
	private IWolfClientService wolfService;
	
	public void setWolfService(IWolfClientService wolfService) {
		this.wolfService = wolfService;
	}

	@Override
	public boolean containsKey(Object groupKey, Object key) {
		Boolean tmp = null;
		try {
			tmp =  wolfService.sendTask(Boolean.class, MAPCACHE_SERVICE, "containsKey",
					new Object[]{groupKey,key});
		} catch (Exception e) {
			log.error("send task error", e);
			throw new BaseException(e.getMessage());
		}
		return tmp==null ? false : tmp.booleanValue();
	}

	@Override
	public Object get(Object groupKey, Object key) {
		Object tmp = null;
		try {
			tmp =  wolfService.sendTask(Object.class, MAPCACHE_SERVICE, "get",
					new Object[]{groupKey,key});
		} catch (Exception e) {
			log.error("send task error", e);
			throw new BaseException(e.getMessage());
		}
		return tmp;
	}

	@Override
	public Map<Object, Object> getAll(Object groupKey,boolean delete) {
		Map<Object, Object> tmp = null;
		try {
			tmp =  wolfService.sendTask(Map.class, MAPCACHE_SERVICE, "getAll",
					new Object[]{groupKey,delete});
		} catch (Exception e) {
			log.error("send task error", e);
			throw new BaseException(e.getMessage());
		}
		return tmp;
	}

	@Override
	public void put(Object groupKey, Object key, Object value) {
		AsyncWolfTask jobTask = new AsyncWolfTask(MAPCACHE_SERVICE,
				"put", new Object[] { groupKey, key,value });

		wolfService.asynSendTask(jobTask);
		
	}

	@Override
	public void putAll(Object groupKey, Map<Object, Object> values) {
		AsyncWolfTask jobTask = new AsyncWolfTask(MAPCACHE_SERVICE,
				"putAll", new Object[] { groupKey, values });

		wolfService.asynSendTask(jobTask);
		
	}

	@Override
	public void remove(Object groupKey, Object key) {
		AsyncWolfTask jobTask = new AsyncWolfTask(MAPCACHE_SERVICE,
				"remove", new Object[] { groupKey, key });

		wolfService.asynSendTask(jobTask);
		
	}

	@Override
	public void removeAll(Object groupKey) {
		AsyncWolfTask jobTask = new AsyncWolfTask(MAPCACHE_SERVICE,
				"removeAll", new Object[] { groupKey, groupKey });

		wolfService.asynSendTask(jobTask);
		
	}

	@Override
	public int size(Object groupKey) {
		Integer tmp = null;
		try {
			tmp =  wolfService.sendTask(Integer.class, MAPCACHE_SERVICE, "size",
					new Object[]{groupKey});
		} catch (Exception e) {
			log.error("send task error", e);
			throw new BaseException(e.getMessage());
		}
		return tmp==null ? 0 : tmp.intValue();	
	}
}

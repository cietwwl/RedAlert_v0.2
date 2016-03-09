package com.youxigu.wolf.minicache.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.youxigu.wolf.minicache.IRemoteMapCacheService;

public class RemoteMapCacheService implements IRemoteMapCacheService {
	private Map<Object,Map<Object,Object>> cacheDatas = new ConcurrentHashMap<Object,Map<Object,Object>>();
	@Override
	public Object get(Object groupKey, Object key) {
		Map<Object,Object> datas = cacheDatas.get(groupKey);
		if (datas!=null){
			return datas.get(key);
		}
		return null;
	}

	@Override
	public Map<Object, Object> getAll(Object groupKey,boolean delete) {
		Map<Object, Object> data = null;
		if (delete){
			data =  cacheDatas.remove(groupKey);
		}else{
			data= cacheDatas.get(groupKey);	
		}
		if (data!=null && data.size()>0){
			return data;
		}
		else{
			return null;
		}
		
		//System.out.println("data="+data);

		
	}

	@Override
	public void put(Object groupKey, Object key, Object value) {
		Map<Object,Object> datas = cacheDatas.get(groupKey);
		if (datas==null){
			datas = new ConcurrentHashMap<Object,Object>();
			cacheDatas.put(groupKey, datas);
		}
		datas.put(key, value);
	}

	@Override
	public void putAll(Object groupKey, Map<Object, Object> values) {
		Map<Object,Object> datas = cacheDatas.get(groupKey);
		if (datas==null){
			datas = new ConcurrentHashMap<Object,Object>();
			cacheDatas.put(groupKey, datas);
		}
		datas.putAll(values);
		
	}

	@Override
	public void remove(Object groupKey, Object key) {
		Map<Object,Object> datas = cacheDatas.get(groupKey);
		if (datas!=null){
			datas.remove(key);
		}
	}

	@Override
	public void removeAll(Object groupKey) {
		cacheDatas.remove(groupKey);
	
	}

	@Override
	public boolean containsKey(Object groupKey, Object key) {
		Map<Object,Object> datas = cacheDatas.get(groupKey);
		if (datas!=null){
			return datas.containsKey(key);
		}
		return false;
	}

	@Override
	public int size(Object groupKey) {
		Map<Object,Object> datas = cacheDatas.get(groupKey);
		if (datas!=null){
			return datas.size();
		}
		return 0;
	}

}

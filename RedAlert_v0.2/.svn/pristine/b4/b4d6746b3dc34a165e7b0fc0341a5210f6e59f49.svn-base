package com.youxigu.wolf.minicache;

import java.util.List;

/**
 * 远程Map集合缓存<br>
 * 
 * 
 * 目的：提供对远程集合的操作,替代memcache的集合操作<br>
 * 在memcache中放置一个list或者map类数据，操作的时候必须先取出集合，加入数据，再更新到memcache，<br>
 * 有时候我们只是想在集合中加入数据，不必取回整个集合，这里做一个简单的远程集合缓存，来存放数据<br>
 * 
 * 这里是模拟远程的List
 * TODO：未来修改成范型格式？
 * @author Administrator
 */
public interface IRemoteListCacheService {
	void add(Object groupKey,Object value);
	void addAll(Object groupKey,List<Object> values);	
	void remove(Object groupKey,int index);
	void removeAll(Object groupKey);	
	Object get(Object groupKey,int index);
	List<Object> getAll(Object groupKey);	
}

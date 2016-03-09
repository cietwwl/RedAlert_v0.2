package com.youxigu.dynasty2.asyncdb.service;

import java.util.List;

import com.ibatis.sqlmap.engine.cache.memcached.AsyncDBData;
import com.ibatis.sqlmap.engine.cache.memcached.IAsyncDBUpdater;



public interface IAsyncDBService extends IAsyncDBUpdater{
	public void asyncUpdate(List<AsyncDBData> datas);
}

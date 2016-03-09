package com.youxigu.dynasty2.asyncdb.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

import com.ibatis.sqlmap.engine.cache.memcached.AsyncDBData;
import com.youxigu.dynasty2.asyncdb.service.IAsyncDBService;
import com.youxigu.dynasty2.core.wolf.IWolfClientService;
import com.youxigu.dynasty2.util.BaseException;

public class AsyncDBServiceClient implements IAsyncDBService {
	public static Logger log = LoggerFactory
			.getLogger(AsyncDBServiceClient.class);
	private IWolfClientService asyncDBServerClient;

	private SqlMapClientTemplate sqlMapClientTemplate;

	private boolean isValidate = true;

	public void setSqlMapClientTemplate(
			SqlMapClientTemplate sqlMapClientTemplate) {
		this.sqlMapClientTemplate = sqlMapClientTemplate;
	}

	public void setAsyncDBServerClient(IWolfClientService asyncDBServerClient) {
		this.asyncDBServerClient = asyncDBServerClient;
	}

	// @Override
	// public void asyncUpdate(List<AsyncDBData> datas) {
	// // AsyncWolfTask task = new AsyncWolfTask();
	// // task.setServiceName("asyncDBService");
	// // task.setMethodName("asyncUpdate");
	// // task.setParams(new Object[]{datas});
	// boolean success =asyncDBServerClient.isConnected();
	// if (success){
	// try{
	// //asyncDBServerClient.asynSendTask(task);
	// asyncDBServerClient.sendTask(Void.class, "asyncDBService", "asyncUpdate",
	// datas);
	// }
	// catch(Exception e){
	// success = false;
	// log.error("异步数据库服务异常:",e);
	// }
	// }
	// if (!success){
	// log.warn("异步数据库服务异常,使用同步更新");
	// updateDatas(datas);
	// }
	//
	// }

	@Override
	public void asyncUpdate(List<AsyncDBData> datas) {
		// AsyncWolfTask task = new AsyncWolfTask();
		// task.setServiceName("asyncDBService");
		// task.setMethodName("asyncUpdate");
		// task.setParams(new Object[] { datas });
		// try {
		// asyncDBServerClient.asynSendTask(task);
		// 修改成同步更新
		try {
			if (isValidate) {
				asyncDBServerClient.sendTask(Void.class, "asyncDBService",
						"asyncUpdate", datas);
			} else {
				isValidate = asyncDBServerClient.sendTask(Boolean.class,
						"asyncDBService", "isValidate", (Object[]) null);
				if (isValidate) {
					asyncDBServerClient.sendTask(Void.class, "asyncDBService",
							"asyncUpdate", datas);

				}
			}

		} catch (Exception e) {
			log.error("异步更新错误", e);
			isValidate = false;
			throw new BaseException("异步更新错误");
			// log.error("异步数据库服务异常,使用同步更新:", e);
			// updateDatas(datas);
			//
		}

	}

	@SuppressWarnings("unused")
	private void updateDatas(List<AsyncDBData> datas) {
		if (datas.size() > 0) {
			for (AsyncDBData data : datas) {
				updateData(data);
			}
		}
	}

	private void updateData(AsyncDBData data) {
		if (data.getType() == AsyncDBData.UPDATE) {
			// System.out.println("update db:"+data.getStatementId());
			// AsyncDBService.this.sqlMapClientTemplate.getSqlMapClient().
			// 如果缓存中存在数据，总是取缓存最新的数据,防止更新顺序不一致带来的问题
			// if (data.getKey() != null) {
			// Object tmp = MemcachedManager
			// .getRemote(data.getKey());
			// if (tmp != null) {
			// data.setData(tmp);
			// }
			// }
			try {
				this.sqlMapClientTemplate.update(data.getStatementId(), data);
			} catch (Exception e) {
				// TODO:出错的sql如何处理?
				log.error("同步更新数据库出错", e);
				log.error("同步sql:{}", data);
			}
		} else {
			log.error("not support async insert or delete,only support update");
		}

	}

	@Override
	public void update(List<AsyncDBData> datas) {
		asyncUpdate(datas);
	}

	@Override
	public boolean isValidate() {
		if (isValidate) {
			return isValidate;
		} else {
			isValidate = asyncDBServerClient.sendTask(Boolean.class,
					"asyncDBService", "isValidate", (Object[]) null);
		}
		return isValidate;

	}

}

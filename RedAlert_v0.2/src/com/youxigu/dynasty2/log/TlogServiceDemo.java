package com.youxigu.dynasty2.log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.shardbatis.spring.jdbc.transaction.DefaultTransactionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionStatus;

import com.ibatis.sqlmap.engine.cache.memcached.broadcast.IBroadcastProducer;
import com.youxigu.dynasty2.core.wolf.IWolfClientService;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.wolf.net.AsyncWolfTask;

public class TlogServiceDemo extends DefaultTransactionListener implements
		ILogService {

	public static final Logger log = LoggerFactory
			.getLogger(TlogServiceDemo.class);

	// 最多支持21个字段
	// private static String[] keys = new String[] { "0", "1", "2", "3", "4",
	// "5",
	// "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17",
	// "18", "19", "20","21","22","23","24","25" };

	// private static Map<String, String> sqlIdMaps = new HashMap<String,
	// String>();

	/*
	 * 事务中的缓存
	 */
	private static ThreadLocal<List<AbsLogLineBuffer>> messages = new ThreadLocal<List<AbsLogLineBuffer>>();
	private static ThreadLocal<Boolean> inTrans = new ThreadLocal<Boolean>() {
		protected Boolean initialValue() {
			return Boolean.FALSE;
		}

	};

	private IBroadcastProducer broadcastMgr;

	private IWolfClientService logClient;
	/**
	 * 是否开放tlog
	 */
	private int open = 0;
	@SuppressWarnings("unused")
	private boolean shutDown;

	// 大区Id
	// private int areaId;

	public void setBroadcastMgr(IBroadcastProducer broadcastMgr) {
		this.broadcastMgr = broadcastMgr;
	}

	public void setOpen(int open) {
		this.open = open;
	}

	public void setOpenFlag(int open, boolean broadcast) {
		this.open = open;
		if (broadcast && broadcastMgr != null) {
			broadcastMgr.sendNotification(new AsyncWolfTask("logService",
					"setOpen", new Object[] { open, false }));
		}
	}

	public void setLogClient(IWolfClientService logClient) {
		this.logClient = logClient;
	}

	// public void setAreaId(int areaId) {
	// this.areaId = areaId;
	// }

	public synchronized void init() {
		if (open != 1) {
			log.warn("myLog没有开启......");
		}
	}

	@Override
	public void log(AbsLogLineBuffer obj) {
		if (open == 0) {
			return;
		}
		if (inTrans.get()) {
			List<AbsLogLineBuffer> logs = messages.get();
			if (logs == null) {
				logs = new ArrayList<AbsLogLineBuffer>();
				messages.set(logs);
			}
			logs.add(obj);
		} else {
			sendTlog(obj);
		}
	}

	// @Override
	// public void log(String logType, Object... objs) {
	// if (open == 0) {
	// return;
	// }
	// AbsLogLineBuffer buf = new LogLineBufferDemo(logType);
	// buf.append(objs);
	// log(buf);
	// }

	protected void sendTlog(AbsLogLineBuffer obj) {
		try {
			Map<String, Object> logdata = generateTlogData(obj);

			AsyncWolfTask task = new AsyncWolfTask();
			task.setServiceName("logService");
			task.setMethodName("log");
			task.setParams(new Object[] { logdata });

			logClient.asynSendTask(task);
			// asyncDBServerClient.sendTask(Void.class, "asyncDBService",
			// "asyncUpdate", datas);
		} catch (Exception e) {
			log.error("发送日志异常:", e);
		}

	}

	protected void sendTlog(List<AbsLogLineBuffer> logs) {
		try {
			List<Map<String, Object>> logDatas = new ArrayList<Map<String, Object>>(
					logs.size());
			for (AbsLogLineBuffer obj : logs) {
				Map<String, Object> logdata = generateTlogData(obj);
				logDatas.add(logdata);
			}
			AsyncWolfTask task = new AsyncWolfTask();
			task.setServiceName("logService");
			task.setMethodName("logs");
			task.setParams(new Object[] { logDatas });

			logClient.asynSendTask(task);
			// asyncDBServerClient.sendTask(Void.class, "asyncDBService",
			// "asyncUpdate", datas);
		} catch (Exception e) {
			log.error("发送日志异常:", e);
		}
	}

	@SuppressWarnings("unchecked")
	protected Map<String, Object> generateTlogData(AbsLogLineBuffer obj) {
		AbsLogLineBuffer buffer = (AbsLogLineBuffer) obj;

		List<Object> logDatas = (List<Object>) buffer.end();
		String tableName = logDatas.remove(0).toString();
		String areaId = buffer.getAreaId();
//		int areaId = Integer.valueOf(buffer.getAreaId());
//		logDatas.set(0, areaId);
		TLogTable tlog = TLogTable.getEnum(tableName);
		if (tlog == null)
			return null;

		String[] columns = tlog.getColumn();
		int columnsNum = columns.length;
		// Map<String, Object> dataMap = new HashMap<String,
		// Object>(columnsNum);
		//
		// int i = 0;
		// for (Object tmp : logDatas) {
		// dataMap.put(columns[i], tmp);
		// i++;
		// }

		Map<String, Object> logMap = new HashMap<String, Object>(3);
		logMap.put("a", Integer.valueOf(areaId));// 大区Id
		logMap.put("t", tableName);// 表名
		logMap.put("d", logDatas);// 数据
		if (log.isDebugEnabled()) {
			log.debug("日志:{}", logMap);
		}
		return logMap;
	}

	// protected void sendTlog() {
	// // Map<String, Object> params = new HashMap<String, Object>(10);
	// Object one = null;
	// List<Object> datas = new ArrayList<Object>(101);
	// while (!shutDown) {
	// try {
	// if ((one = queue.poll(10, TimeUnit.SECONDS)) != null) {
	// datas.add(one);
	// queue.drainTo(datas, 100);
	//
	// for (Object obj : datas) {
	// if (obj instanceof List) {
	// List<AbsLogLineBuffer> subDatas = (List<AbsLogLineBuffer>) obj;
	// for (AbsLogLineBuffer buffer : subDatas) {
	//
	// List<Object> logDatas = (List<Object>) buffer
	// .end();
	// String tableName = logDatas.get(0).toString();
	// logDatas.set(0, areaId);
	//
	// _sendMyLog(tableName, logDatas);
	//
	// }
	// } else {
	// AbsLogLineBuffer buffer = (AbsLogLineBuffer) obj;
	//
	// List<Object> logDatas = (List<Object>) buffer.end();
	// String tableName = logDatas.get(0).toString();
	// logDatas.set(0, areaId);
	//
	// _sendMyLog(tableName, logDatas);
	//
	// }
	// }
	// }
	//
	// } catch (Exception e) {
	// log.error("TlogJob异常", e);
	// } finally {
	// datas.clear();
	//
	// }
	//
	// }
	//
	// }

	public void destroy() {
		shutDown = true;

	}

	@Override
	public void doBeginAfter(Object transaction,
			TransactionDefinition definition) {
		inTrans.set(true);
	}

	@Override
	public void doCommitAfter(DefaultTransactionStatus status) {
		try {
			List<AbsLogLineBuffer> logs = messages.get();
			if (logs != null && logs.size() > 0) {
				sendTlog(logs);
			}
		} finally {
			inTrans.set(false);
			messages.remove();
		}
	}

	@Override
	public void doRollbackAfter(DefaultTransactionStatus status) {
		inTrans.set(false);
		messages.remove();
	}

	@Override
	public void logDB(Object[] params) {
		throw new BaseException("unsupports.........");
	}
}

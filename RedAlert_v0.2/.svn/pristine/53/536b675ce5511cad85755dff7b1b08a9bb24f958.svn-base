package com.youxigu.dynasty2.log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.shardbatis.spring.jdbc.transaction.DefaultTransactionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionStatus;

import com.ibatis.sqlmap.engine.cache.memcached.broadcast.IBroadcastProducer;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.wolf.net.AsyncWolfTask;
@Deprecated
public class TlogServiceDemo1 extends DefaultTransactionListener implements
		ILogService {

	public static final Logger log = LoggerFactory
			.getLogger(TlogServiceDemo1.class);

	// 最多支持21个字段
	// private static String[] keys = new String[] { "0", "1", "2", "3", "4",
	// "5",
	// "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17",
	// "18", "19", "20","21","22","23","24","25" };

	// private static Map<String, String> sqlIdMaps = new HashMap<String,
	// String>();

	// 缓存
	private BlockingQueue<Object> queue = null;
	/*
	 * 事务中的缓存
	 */
	private static ThreadLocal<List<AbsLogLineBuffer>> messages = new ThreadLocal<List<AbsLogLineBuffer>>();
	private static ThreadLocal<Boolean> inTrans = new ThreadLocal<Boolean>() {
		protected Boolean initialValue() {
			return Boolean.FALSE;
		}

	};
	// // 随机数
	// private static long eventGroupId = System.currentTimeMillis()
	// + UUID.randomUUID().hashCode() * 100;
	// private String category = "test";
	// private String configPath = null;
	private int cacheSize = 12000;

	private ITlogDemoDao dao = null;
	private IBroadcastProducer broadcastMgr;
	/**
	 * 是否开放tlog
	 */
	private int open = 0;
	private boolean shutDown;

	// 大区Id
	private int areaId;

	public void setBroadcastMgr(IBroadcastProducer broadcastMgr) {
		this.broadcastMgr = broadcastMgr;
	}

	public void setDao(ITlogDemoDao dao) {
		this.dao = dao;
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

	public void setCacheSize(int cacheSize) {
		this.cacheSize = cacheSize;
	}

	public void setAreaId(int areaId) {
		this.areaId = areaId;
	}

	public synchronized void init() {
		log.info("初始化mylog...........");
		queue = new ArrayBlockingQueue<Object>(cacheSize);

		new Thread(new Runnable() {

			@Override
			public void run() {

				TlogServiceDemo1.this.sendTlog();

			}
		}, "MylogThread").start();

		log.info("初始化mylog完成...........");

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
			boolean success = queue.offer(obj);
			if (!success) {
				log.error("mylog日志队列已达上限:{},丢弃tlog",cacheSize);
			}
		}
	}

//	@Override
//	public void log(String logType, Object... objs) {
//		if (open == 0) {
//			return;
//		}
//		AbsLogLineBuffer buf = new LogLineBufferDemo(logType);
//		buf.append(objs);
//		log(buf);
//	}

	@SuppressWarnings("unchecked")
	protected void sendTlog() {
		// Map<String, Object> params = new HashMap<String, Object>(10);
		Object one = null;
		List<Object> datas = new ArrayList<Object>(101);
		while (!shutDown) {
			try {
				if ((one = queue.poll(10, TimeUnit.SECONDS)) != null) {
					datas.add(one);
					queue.drainTo(datas, 100);

					for (Object obj : datas) {
						if (obj instanceof List) {
							List<AbsLogLineBuffer> subDatas = (List<AbsLogLineBuffer>) obj;
							for (AbsLogLineBuffer buffer : subDatas) {

								List<Object> logDatas = (List<Object>) buffer
										.end();
								String tableName = logDatas.get(0).toString();
								logDatas.set(0, areaId);

								_sendMyLog(tableName, logDatas);

							}
						} else {
							AbsLogLineBuffer buffer = (AbsLogLineBuffer) obj;

							List<Object> logDatas = (List<Object>) buffer.end();
							String tableName = logDatas.get(0).toString();
							logDatas.set(0, areaId);

							_sendMyLog(tableName, logDatas);

						}
					}
				}

			} catch (Exception e) {
				log.error("TlogJob异常", e);
			} finally {
				datas.clear();

			}

		}

	}

	/**
	 * 游戏谷经分log
	 * 
	 * @param tableName
	 * @param logDatas
	 */
	private void _sendMyLog(String tableName, List<Object> logDatas) {
		try {
			int i = 0;
			TLogTable tlog = TLogTable.getEnum(tableName);
			if (tlog == null)
				return;
			String[] columns = tlog.getColumn();
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			for (Object tmp : logDatas) {
				// params.put(keys[i], tmp);
				jsonMap.put(columns[i], tmp);
				i++;
			}
			// System.out.println(jsonMap);
			// dao.insertTlog(sqlId, params);

			dao.insertTlog(areaId, tableName, jsonMap);
		} catch (Exception e) {
			log.error("mylog异常", e);
		}
	}



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
				boolean add = queue.offer(logs);
				if (!add){
					log.error("tlog日志队列已达上限:{},丢弃tlog",cacheSize);
				}	
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
	public void logDB(Object[] params){
		throw new BaseException("unsupports.........");
	}
}

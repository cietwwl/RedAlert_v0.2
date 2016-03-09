package com.youxigu.dynasty2.log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.shardbatis.spring.jdbc.transaction.DefaultTransactionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionStatus;

import com.ibatis.sqlmap.engine.cache.memcached.broadcast.IBroadcastProducer;
import com.youxigu.wolf.net.AsyncWolfTask;

/**
 * 腾讯tlog
 * 
 * @author Administrator
 * 
 */
public class TlogService extends DefaultTransactionListener implements
		ILogService {
	public static final Logger log = LoggerFactory.getLogger(TlogService.class);
	private DataSource dataSource;

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
	// 随机数

	private int cacheSize = 120000;

	private boolean shutDown;

	private int open;

	/**
	 * 腾讯tlogd ip
	 */
	private String tlogIp;
	/**
	 * 腾讯tlogd port
	 */
	private int tlogPort;
	/**
	 * 腾讯tlogd 超时时间
	 */
	private int tlogTimeout = 3000;

	private DatagramSocket udpClient;
	private InetAddress inetAddress;

	private IBroadcastProducer broadcastMgr;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void setOpen(int open) {
		this.open = open;
	}

	public void setBroadcastMgr(IBroadcastProducer broadcastMgr) {
		this.broadcastMgr = broadcastMgr;
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

	public void setTlogIp(String tlogIp) {
		this.tlogIp = tlogIp;
	}

	public void setTlogPort(int tlogPort) {
		this.tlogPort = tlogPort;
	}

	public void setTlogTimeout(int tlogTimeout) {
		this.tlogTimeout = tlogTimeout;
	}

	public synchronized void init() {
		queue = new ArrayBlockingQueue<Object>(cacheSize);
		log.info("初始化tlog...........");
		if (tlogPort > 0 && tlogIp != null && tlogIp.length() > 0) {
			try {
				log.info("tlog ip ={}", tlogIp);
				log.info("tlog port ={}", tlogPort);
				inetAddress = InetAddress.getByName(tlogIp);
				udpClient = new DatagramSocket();
				udpClient.setSoTimeout(tlogTimeout);
			} catch (Exception e) {
				log.error("tlog init error", e);
			}
		} else {
			log.warn("没有配置tx tlog的ip port,不能启动tx_tlog服务");
		}

		new Thread(new Runnable() {

			@Override
			public void run() {

				log.info("tlog 输出线程启动.......");
				TlogService.this.sendTlog();

			}
		}, "TlogThread").start();

		log.info("初始化tlog完成...........");
	}

	@Override
	public void log(AbsLogLineBuffer obj) {
		if (open == 0)
			return;
		if (inTrans.get()) {
			List<AbsLogLineBuffer> logs = messages.get();
			if (logs == null) {
				logs = new ArrayList<AbsLogLineBuffer>();
				messages.set(logs);
			}
			logs.add(obj);
		} else {
			boolean add = queue.offer(obj);
			if (!add) {
				log.error("tlog日志队列已达上限:{},丢弃tlog", cacheSize);
			}
		}
	}

	// @Override
	// public void log(String logType, Object... objs) {
	// if (open == 0)
	// return;
	// LogLineBuffer buf = new LogLineBuffer(logType);
	// buf.append(objs);
	// log(buf);
	// }

	@SuppressWarnings("unchecked")
	protected void sendTlog() {

		Object one = null;
		List<Object> datas = new ArrayList<Object>(101);
		while (!shutDown) {
			try {
				if ((one = queue.poll(10, TimeUnit.SECONDS)) != null) {
					datas.add(one);
					queue.drainTo(datas, 100);

					for (Object obj : datas) {
						try {
							if (obj instanceof List) {
								List<LogLineBuffer> subDatas = (List<LogLineBuffer>) obj;
								for (LogLineBuffer buffer : subDatas) {
									String tlog = buffer.end().toString();

									if (log.isDebugEnabled()) {
										log.debug("tlog日志:{}", tlog);
									}
									byte[] sendBuf = tlog.getBytes("UTF-8");
									DatagramPacket sendPacket = new DatagramPacket(
											sendBuf, sendBuf.length,
											inetAddress, tlogPort);
									udpClient.send(sendPacket);

								}
							} else if (obj instanceof LogLineBuffer) {
								LogLineBuffer buffer = (LogLineBuffer) obj;
								String tlog = buffer.end().toString();

								if (log.isDebugEnabled()) {
									log.debug("tlog日志:{}", tlog);
								}
								byte[] sendBuf = tlog.getBytes("UTF-8");
								DatagramPacket sendPacket = new DatagramPacket(
										sendBuf, sendBuf.length, inetAddress,
										tlogPort);
								udpClient.send(sendPacket);

							} else {
								// db log
								execSql((Object[]) obj);
							}
						} catch (Exception e) {
							log.error("tlog异常", e);
						}
					}

				}

			} catch (Throwable e) {
				log.error("TlogJob异常", e);
			} finally {
				datas.clear();
			}

		}

		log.info("tlog服务线程停止");
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
				if (!add) {
					log.error("tlog日志队列已达上限:{},丢弃tlog", cacheSize);
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

	/**
	 * 腾讯tlog
	 * 
	 * @param tableName
	 * @param logDatas
	 */
	// private void _sendTLog(String tableName, List<Object> logDatas) {
	//
	// try {
	//
	// LogLineBuffer buffer = new LogLineBuffer(tableName);
	// for (Object tmp : logDatas) {
	// buffer.append(tmp);
	// }
	// String logStr = buffer.end().toString();
	// if (log.isDebugEnabled()) {
	// log.debug("tlog日志:{}", logStr);
	// }
	// byte[] sendBuf = logStr.getBytes("UTF-8");
	// DatagramPacket sendPacket = new DatagramPacket(sendBuf,
	// sendBuf.length, inetAddress, tlogPort);
	// udpClient.send(sendPacket);
	//
	// } catch (Exception e) {
	// log.error("tlog异常", e);
	// }
	// }
	@Override
	public void logDB(Object[] params) {
		if (open != 0) {
			queue.offer(params);
		}
	}

	private void execSql(Object[] params) {
		Connection _conn = null;
		PreparedStatement pstmt = null;

		String sql = (String) params[0];
		try {
			_conn = dataSource.getConnection();
			_conn.setAutoCommit(false);
			pstmt = _conn.prepareStatement(sql);
			int fileNum = params.length;
			for (int i = 1; i < fileNum; i++) {
				Object param = params[i];
				if (param instanceof String) {
					pstmt.setString(i, (String) param);
				} else if (param instanceof Integer) {
					pstmt.setInt(i, (Integer) param);
				} else if (param instanceof Timestamp) {
					pstmt.setTimestamp(i, (Timestamp) param);
				} else if (param instanceof Long) {
					pstmt.setLong(i, (Long) param);
				}

			}

			pstmt.execute();
			_conn.commit();

			pstmt.close();

		} catch (Exception e) {
			try {
				_conn.rollback();
			} catch (Exception e1) {
			}
			throw new RuntimeException(e);
		} finally {

			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e) {
				}
			}
			if (_conn != null) {
				try {
					_conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

package com.youxigu.dynasty2.asyncdb.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectOutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.engine.cache.memcached.AsyncDBData;
import com.ibatis.sqlmap.engine.impl.ExtendedSqlMapClient;
import com.ibatis.sqlmap.engine.mapping.parameter.ParameterMap;
import com.ibatis.sqlmap.engine.mapping.statement.MappedStatement;
import com.youxigu.dynasty2.asyncdb.service.IAsyncDBService;
import com.youxigu.dynasty2.util.AlarmTool;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.dynasty2.util.DateUtils;

/**
 * 异步数据库更新： TODO：目前只可以异步update ,insert 与delte没有处理 TODO:出现异常如何处理？
 * 
 * 定时更新方式
 * 
 * @author Administrator
 * 
 */
@SuppressWarnings({ "deprecation", "rawtypes" })
public class AsyncDBServiceEx implements IAsyncDBService, ApplicationListener {
	public static Logger log = LoggerFactory.getLogger(AsyncDBServiceEx.class);
	private static final String WORK_NAME_PREFIX = "ASYNCDBTHREAD";

	private int maxThreadNum = 12;
	private SqlMapClientTemplate sqlMapClientTemplate;

	private ExtendedSqlMapClient client;
	private List<Worker> workers = new ArrayList<Worker>();

	private int maxPoolSize = 50000;
	private int period = 30000;// 毫秒，更新数据库线程的启动时间间隔

	/**
	 * 出错后的保存目录
	 */
	private String path;
	private boolean errorAsSql = true;// 保存成2进制还是文本sql

	private boolean pause = false;
	private long pauseTime = 0;

	public void setMaxThreadNum(int maxThreadNum) {

		this.maxThreadNum = maxThreadNum;
	}

	public void setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public void setSqlMapClientTemplate(
			SqlMapClientTemplate sqlMapClientTemplate) {
		this.sqlMapClientTemplate = sqlMapClientTemplate;
		if (sqlMapClientTemplate != null) {
			SqlMapClient tmpClient = sqlMapClientTemplate.getSqlMapClient();
			if (tmpClient instanceof ExtendedSqlMapClient) {
				client = (ExtendedSqlMapClient) tmpClient;
			}
		}
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setErrorAsSql(boolean errorAsSql) {
		this.errorAsSql = errorAsSql;
	}

	public void init() {
		log.info("异步数据存储服务启动,最大线程数:{}...............", maxThreadNum);
		log.info("每线程缓存池大小：{},每线程最大更新时间间隔{}毫秒...............", maxPoolSize,
				period);

		if (this.path == null) {
			this.path = System.getProperty("user.dir");
		}
		this.path = this.path + File.separator + "asyncdb_err";
		if (!this.path.endsWith(File.separator)) {
			this.path = this.path + File.separator;
		}

		File tmp = new File(path);
		if (tmp.isDirectory()) {
			String[] files = tmp.list();
			if (files != null && files.length > 0) {
				log.info("备份上次运行为处理的错误sql...");
				tmp.renameTo(new File(tmp.getPath() + "_"
						+ System.currentTimeMillis()));
			}
		}

		tmp = new File(path);
		if (!tmp.exists()) {
			tmp.mkdir();
		} else if (!tmp.isDirectory()) {
			tmp.delete();
			tmp.mkdir();
		}

		for (int i = 0; i < maxThreadNum; i++) {
			Worker worker = new Worker(WORK_NAME_PREFIX + i);
			workers.add(worker);
		}

		// /通过spring 来停止，防止 mysql 先停止了
		/**
		 * 检查是否有 没处理完的请求
		 */
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				shutdown();

			}
		}));
	}

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof ContextClosedEvent) {
			shutdown();
		}
	}

	public void shutdown() {
		log.info("AsyncDBService 开始检查退出条件了！");
		int remainSize = 0;

		do {
			remainSize = 0;
			for (Worker worker : workers) {
				int size = worker.size();
				if (size > 0) {
					worker.weakup();
				}
				remainSize += size;
			}
			log.info("AsyncDBService 剩余Sql数量：" + remainSize);
			if (remainSize > 0) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} while (remainSize > 0);

		for (Worker worker : workers) {
			worker.setStart(false);
			worker.weakup();
		}
		log.info("AsyncDBService 退出条件达成！");
	}

	@Override
	public void asyncUpdate(List<AsyncDBData> datas) {
		// for test
		// if (!pause){
		// pause=true;
		// pauseTime = System.currentTimeMillis();
		// throw new BaseException("异步数据库服务异常,请稍后再试.....");
		// }
		if (pause) {
			if (System.currentTimeMillis() - pauseTime > 600000) {
				pause = false;
			} else {
				throw new BaseException("异步数据库服务异常,请稍后再试.....");
			}
		}
		for (AsyncDBData data : datas) {
			Worker worker = getWorker(data);
			// worker.offer(data);
			worker.put(data);
		}
		// 清除，避免远程调用回传数据
		datas.clear();
	}

	// private void updateDatas(List<AsyncDBData> datas) {
	// if (datas.size() > 0) {
	// // if (log.isDebugEnabled()){
	// // log.debug("异步批量执行{}条sql开始：",datas.size());
	// // }
	// Iterator<AsyncDBData> lit = datas.iterator();
	// while (lit.hasNext()) {
	// AsyncDBData data = lit.next();
	// updateData(data);
	// lit.remove();
	// }
	// // if (log.isDebugEnabled()){
	// // log.debug("异步批量执行sql完成：");
	// // }
	// }
	// }

	// private void updateData(AsyncDBData data) {
	// if (data.getType() == AsyncDBData.UPDATE) {
	// // System.out.println("update db:"+data.getStatementId());
	// // AsyncDBService.this.sqlMapClientTemplate.getSqlMapClient().
	// // 如果缓存中存在数据，总是取缓存最新的数据,防止更新顺序不一致带来的问题
	// // if (data.getKey() != null) {
	// // Object tmp = MemcachedManager
	// // .getRemote(data.getKey());
	// // if (tmp != null) {
	// // data.setData(tmp);
	// // }
	// // }
	// int retryCount = 3;
	// while (retryCount > 0) {
	// try {
	// this.sqlMapClientTemplate.update(data.getStatementId(),
	// data);
	// retryCount = 0;
	// } catch (DataAccessResourceFailureException de) {
	// Throwable th = de.getCause();
	// while (th != null) {
	// if (th instanceof
	// com.mysql.jdbc.exceptions.jdbc4.CommunicationsException) {
	// // 数据库连接异常
	// try {
	// log.error("数据库连接中断，1分钟后重试", de);
	// retryCount--;
	// Thread.sleep(60000);
	// } catch (Exception e) {
	// }
	// break;
	// }
	// th = th.getCause();
	// }
	// if (retryCount == 0) {
	// throw de;
	// }
	// }
	// }
	//
	// // File tmp = new File(path + data.getKey() );
	// // if (tmp.exists()){
	// // tmp.delete();
	// // }
	// // data.getKey()
	// } else {
	// log.error("not support async insert or delete,only support update");
	// }
	//
	// }

	private void saveErrorData(AsyncDBData data) {
		AlarmTool.alarm(AlarmTool.ALARM_DB_CONNECT, "异步数据更新异常");
		//
		// 生成文件名，如果相同的key,保留最后一个
		String fileName = path + data.getKey();
		File tmp = new File(fileName);
		if (errorAsSql && client != null) {
			MappedStatement ms = client.getMappedStatement(data
					.getStatementId());
			ParameterMap map = ms.getParameterMap();
			Object[] params = map
					.getParameterObjectValues(null, data.getData());
			String sql = ms.getSql().getSql(null, data.getData());

			for (int i = 0; i < params.length; i++) {
				Object param = params[i];
				if (param == null) {
					sql = sql.replaceFirst("\\?", "NULL");
				} else if (param instanceof Integer || param instanceof Long
						|| param instanceof Short || param instanceof Byte) {
					sql = sql.replaceFirst("\\?", param.toString());
				} else if (param instanceof Double || param instanceof String) {
					sql = sql.replaceFirst("\\?", "'" + param.toString() + "'");
				} else if (param instanceof java.util.Date) {
					sql = sql
							.replaceFirst(
									"\\?",
									"'"
											+ DateUtils
													.datetime2Text((java.util.Date) param)
											+ "'");
				} else {
					sql = sql.replaceFirst("\\?", "'" + param.toString() + "'");
				}
			}

			Writer w = null;
			try {
				w = new BufferedWriter(new FileWriter(tmp));
				w.write(sql);
				w.write("\n");
				w.flush();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (w != null) {
					try {
						w.close();
					} catch (Exception e) {
					}
				}
			}

			// os.writeObject(sqlStr);
		} else {
			ObjectOutputStream os = null;
			try {
				os = new ObjectOutputStream(new FileOutputStream(tmp));
				os.writeObject(data);
				os.flush();
				os.close();
			} catch (Exception e) {
				e.printStackTrace();

			} finally {
				if (os != null) {
					try {
						os.close();
					} catch (Exception e) {
					}
				}
			}

		}

	}

	private Worker getWorker(AsyncDBData data) {
		int index = data.getTarget();// 1--maxThreadNum
		if (index <= 0) {
			index = 1;
		}
		if (index >= maxThreadNum) {
			index = index % maxThreadNum;
		}
		Worker worker = workers.get(index);
		if (!worker.isStart()) {
			synchronized (worker) {
				if (!worker.isStart()) {
					try {
						// worker = new Worker(WORK_NAME_PREFIX + index);
						worker.setStart(true);
						worker.start();
						workers.set(index, worker);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (log.isDebugEnabled()) {
						log.debug("start asyncDBThread:{} ", worker.getName());
					}
				}
			}
		}
		return worker;

	}

	/**
	 * 2个缓存在接受数据与写数据库线程间切换
	 * 
	 */
	class Worker extends Thread {
		private Map<String, AsyncDBData> one = new HashMap<String, AsyncDBData>();// new
		// ConcurrentHashMap<String,
		// AsyncDBData>();
		private Map<String, AsyncDBData> two = new HashMap<String, AsyncDBData>();// new
		// ConcurrentHashMap<String,
		// AsyncDBData>();

		private Map<String, AsyncDBData> produce = null;
		private Map<String, AsyncDBData> consume = null;
		// private BlockingQueue<SoftReference<AsyncDBData>> queue = new
		// LinkedBlockingQueue<SoftReference<AsyncDBData>>();
		private boolean start = false;
		private int mergeNum = 0;
		private int mergedNum = 0;

		public Worker(String name) {
			this.setDaemon(true);
			this.setName(name);
			produce = one;
			consume = null;
		}

		public boolean isStart() {
			return start;
		}

		public void setStart(boolean start) {
			this.start = start;
		}

		@Override
		public void run() {
			try {
				while (start) {
					int errorNum = 0;
					try {
						synchronized (this) {
							if (consume == null) {
								this.wait(period);
							}
						}
						synchronized (this) {
							if (consume == null && produce.size() > 0) {//
								exchangeProduceComsume();
							}
						}
						if (consume == null) {
							if (!start) {
								break;
							}
							continue;
						}
						int num = consume.size();
						if (num >= maxPoolSize) {
							log.warn("线程{}的异步缓存池超过最大限制:{}", this.getName(), num);
						}

						long time = System.currentTimeMillis();
						Iterator<Map.Entry<String, AsyncDBData>> lit = consume
								.entrySet().iterator();
						while (lit.hasNext()) {
							Map.Entry<String, AsyncDBData> entry = lit.next();
							AsyncDBData data = entry.getValue();
							try {

								AsyncDBServiceEx.this.sqlMapClientTemplate
										.update(data.getStatementId(), data);

								// AsyncDBServiceEx.this.updateData(data);
							} catch (Throwable e) {
								log.error("异步更新数据库出错", e);
								log.error("异步sql:{}", data);
								try {
									errorNum++;
									if (start) {
										String key = entry.getKey();
										if (data.getErrorNum() < 10) {
											// 出错,加回缓存
											synchronized (this) {
												if (!produce.containsKey(key)) {
													data.setErrorNum((byte) (data
															.getErrorNum() + 1));
													produce.put(key, data);
												} else {
													log.warn(
															"缓存中已经存在新数据,丢弃异常数据:{}",
															key);
												}
											}
										} else {
											AsyncDBServiceEx.this
													.saveErrorData(data);
											log.warn("异常数据retry超过次数，丢弃:{}", key);
										}
									} else {
										AsyncDBServiceEx.this
												.saveErrorData(data);
									}
								} catch (Throwable e1) {
									log.error("异步更新数据库出错2:", e1);
								}
							}
							lit.remove();
						}
						if (log.isInfoEnabled()) {
							log.info("本次更新{}条数据，其中{}条失败", num, errorNum);
							log.info("本次更新合并{}条数据", this.mergedNum);
							log.info("本次耗时{}ms",
									(System.currentTimeMillis() - time));
						}
						if (errorNum > 30 && errorNum * 10 > num) {
							// /一次出现超过1/10的错误，则任务异常，停止异步服务
							AlarmTool.alarm(AlarmTool.ALARM_DB_ASYNC,
									"异步数据更新出现异常,暂停......");
							pause = true;
							pauseTime = System.currentTimeMillis();
						} else {
							pause = false;
							pauseTime = 0;
						}
						synchronized (this) {
							consume = null;
						}

					} catch (Throwable e) {
						log.error("异步更新数据库异常", e);
					}

				}
			} finally {
				start = false;
			}

		}

		public int size() {
			return this.one.size() + this.two.size();
		}

		private void exchangeProduceComsume() {
			consume = produce;
			if (produce == one) {
				produce = two;
			} else {
				produce = one;
			}
			if (log.isDebugEnabled()) {
				this.mergedNum = this.mergeNum;
				this.mergeNum = 0;
			}
		}

		// 唤醒，在退出系统时用
		public void weakup() {
			synchronized (this) {
				this.notifyAll();
			}
		}

		public void put(AsyncDBData data) {
			synchronized (this) {
				try {
					AsyncDBData old = produce.put(data.getKey(), data);
					if (log.isInfoEnabled()) {
						if (old != null) {
							mergeNum++;
						}
					}
					int num = produce.size();
					if (num >= maxPoolSize) {
						if (consume == null) {
							exchangeProduceComsume();
							this.notifyAll();
						}
						// else {
						// log
						// .warn("线程{}的异步缓存池超过最大限制:{}",
						// this.getName(), num);
						// }
					}
				} catch (Exception e) {
					log.error("添加数据库更新失败：{}", data);
				}

			}

		}

	}

	@Override
	public boolean isValidate() {
		if (pause && System.currentTimeMillis() - pauseTime > 600000) {
			pause = false;
		}
		return !pause;
	}

	@Override
	public void update(List<AsyncDBData> datas) {
		// TODO Auto-generated method stub

	}

}

package com.youxigu.dynasty2.asyncdb.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

import com.ibatis.sqlmap.engine.cache.memcached.AsyncDBData;
import com.youxigu.dynasty2.asyncdb.service.IAsyncDBService;

public class AsyncPersistenceDBService implements IAsyncDBService {
	public static Logger log = LoggerFactory.getLogger(AsyncDBService.class);
	private static final String WORK_NAME_PREFIX = "ASYNCDBTHREAD_";
	private static final String PERSISTENCE_PATH = "asyncdata";
	
	
	private static Comparator<File> comparator = new Comparator<File>() {
		public int compare(File f1, File f2) {
			return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
		}
	};
	
	/*
	 * counter：
	 * 作为缓存文件的后缀名，保证每个文件后缀名不同
	 * 每次启动服务器，以当前系统时间戳作为启动级数
	 */
	public static AtomicLong counter = new AtomicLong(System.currentTimeMillis());

	private String path;

	private int maxThreadNum = 32;
	private SqlMapClientTemplate sqlMapClientTemplate;
	private List<Worker> workers = new ArrayList<Worker>();

	public void setPath(String path) {
		this.path = path;

	}

	public void setMaxThreadNum(int maxThreadNum) {

		this.maxThreadNum = maxThreadNum;
	}

	public void setSqlMapClientTemplate(
			SqlMapClientTemplate sqlMapClientTemplate) {
		this.sqlMapClientTemplate = sqlMapClientTemplate;
	}

	public void init() {
		if (this.path == null) {
			this.path = System.getProperty("user.dir")+File.separator+ PERSISTENCE_PATH+File.separator;
		}
		if (!this.path.endsWith(File.separator)) {
			this.path = this.path + File.separator;
		}
		
		createPath(this.path);
		log.info("异步数据存储服务启动,异步数据临时存储目录:{}", this.path);

		for (int i = 0; i < maxThreadNum; i++) {
			Worker worker = new Worker(WORK_NAME_PREFIX + i);
			workers.add(worker);
			if (worker.loadData()>0) {
				worker.start();
			}
		}

		/**
		 * 检查是否有 没处理完的请求
		 */
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				//这里不检查的情况下，下次启动可能导致memcache中是脏数据
				log.info("AsyncDBService 开始检查退出条件了！");
				int remainSize = 0;
				do {
					remainSize = 0;
					for (Worker worker : workers) {
						if (worker.isAlive()){
							int size = worker.hasData();
							if (size==0){
								worker.destroy();
							}
							remainSize += size;
						}
					}
					log.info("AsyncDBService 剩余Sql数量：" + remainSize);
					if (remainSize > 0) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				} while (remainSize > 0);
				log.info("AsyncDBService 退出条件达成！");

			}
		}));
	}

	@Override
	public void asyncUpdate(List<AsyncDBData> datas) {
		//TODO:这里是整体保存成1个文件还是分开保存？目前分开保存，因为每个数据可能分配到不同的线程中执行
		for (AsyncDBData data : datas) {
			Worker worker = getWorker(data);
			worker.offer(data);
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
		if (!worker.isAlive()) {
			synchronized (worker) {
				if (!worker.isAlive()) {
					worker = new Worker(WORK_NAME_PREFIX + index);
					worker.start();
					workers.set(index, worker);
					if (log.isDebugEnabled()) {
						log.debug("start asyncDBThread:{} ", worker.getName());
					}
				}
			}
		}
		return worker;

	}

	class Worker extends Thread {
		private BlockingQueue<File> queue = new LinkedBlockingQueue<File>();

		private String path;
		private String errPath;
		private boolean running;

		public Worker(String name) {
			this.setDaemon(true);
			this.setName(name);
			// this.errPath =
			// AsyncPersistenceDBService.this.path+name+"ERR"+File.separator;
			path = AsyncPersistenceDBService.this.path + name
					+ File.separator;
			createPath(path);
			errPath = AsyncPersistenceDBService.this.path + name + "_ERR"
					+ File.separator;
			createPath(errPath);
		}

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public String getErrPath() {
			return errPath;
		}

		public void setErrPath(String errPath) {
			this.errPath = errPath;
		}

		public int hasData() {
			File errorPath = new File(this.errPath);
			String[] files = errorPath.list();
			if (files !=null && files.length > 0) {
				log.warn("目录：{}下存在无法处理的异常数据", this.errPath);
			}

			File dataPath = new File(this.path);
			files = dataPath.list();
			if (files !=null && files.length > 0) {
				log.info("存在未处理的异步数据");
				// for (File file:files){
				// queue.offer(file);
				// }
				return files.length;
			}
			return 0;
		}
		
		public int loadData(){
			File dataPath = new File(this.path);
			File[] files = dataPath.listFiles();
			Arrays.sort(files, comparator);
			if (files !=null && files.length > 0) {
				for (File file : files){
					queue.offer(file);
				}
				return files.length;
			}
			return 0;
			
			//load error data?
		}
		@Override
		public void run() {
			running = true;
			List<File> datas = new ArrayList<File>();
			while (running) {
				try {
					synchronized (this) {
						queue.drainTo(datas);
						if (datas.size() == 0) { // 如果没有任何数据，则休息10s，继续判断
							this.wait(10000);
							continue;							
						}						
					}

					ObjectInputStream in = null;
					AsyncDBData data = null;
					for (File file : datas) {
						try {
							in = new ObjectInputStream(
									new FileInputStream(file));
							data = (AsyncDBData) in.readObject();
						} catch (Exception e) {
							log.error("读数据错误", e);
							// TODO:转到error目录下
							removeFile(file, new File(errPath + file.getName()));

						} finally {
							if (in != null) {
								in.close();
							}
						}
						if (data != null) {
							try {
								updateData(data);
								file.delete();
							} catch (Exception e) {
								log.error("更新数据库错误", e);
								// TODO:转到error目录下
								removeFile(file, new File(errPath
										+ file.getName()));
							}
						}

					}

				} catch (Exception e) {
					log.error(e.toString(), e);
				} finally {
					datas.clear();
				}
			}

		}

		private void updateData(AsyncDBData data) {
			if (data.getType() == AsyncDBData.UPDATE) {
				// System.out.println("update db:"+data.getStatementId());
				// AsyncDBService.this.sqlMapClientTemplate.getSqlMapClient().
				// 如果缓存中存在数据，总是取缓存最新的数据,防止更新顺序不一致带来的问题
//				if (data.getKey() != null) {
//					Object tmp = MemcachedManager.getRemote(data.getKey());
//					if (tmp != null) {
//						data.setData(tmp);
//					}
//				}

				AsyncPersistenceDBService.this.sqlMapClientTemplate.update(data
						.getStatementId(), data);

			} else {
				log
						.error("not support async insert or delete,only support update");
			}

		}

		public boolean offer(AsyncDBData data) {
			ObjectOutputStream os = null;
			// 生成唯一的文件名，可能文件名相同，后缀不同
			String fileName = path + data.getKey() + "."
					+ counter.getAndIncrement();
			try {
				File tmp = new File(fileName);
				os = new ObjectOutputStream(new FileOutputStream(tmp));
				os.writeObject(data);
				os.flush();
				os.close();

				synchronized (this) {
					queue.offer(tmp);
					this.notify();
				}

			} catch (Exception e) {
				e.printStackTrace();
				return false;
			} finally {
				if (os != null) {
					try {
						os.close();
					} catch (Exception e) {
					}
				}
			}
			return true;
		}

		public void destroy() {
			running = false;
			synchronized (this) {
				this.notify();
			}			
		}

	}
	
	private void removeFile(File source, File dest) {
		if (dest.exists()) {
			dest.delete();
		}
		source.renameTo(dest);
	}
	
	private void createPath(String path) {
		File tmp = new File(path);
		if (!tmp.exists()) {
			tmp.mkdir();
		} else if (!tmp.isDirectory()) {
			tmp.delete();
			tmp.mkdir();
		}
	}

	@Override
	public boolean isValidate() {
		return true;
	}

	@Override
	public void update(List<AsyncDBData> datas) {
		// TODO Auto-generated method stub
		
	}

}

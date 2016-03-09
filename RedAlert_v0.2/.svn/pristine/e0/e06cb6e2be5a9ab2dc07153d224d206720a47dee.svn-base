package com.youxigu.dynasty2.asyncdb.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

import com.ibatis.sqlmap.engine.cache.memcached.AsyncDBData;
import com.youxigu.dynasty2.asyncdb.service.IAsyncDBService;

/**
 * 异步数据库更新：
 * TODO：目前只可以异步update ,insert 与delte没有处理
 * TODO:出现异常如何处理？
 * 
 * @author Administrator
 *
 */
public class AsyncDBService1 implements IAsyncDBService {
	public static Logger log = LoggerFactory.getLogger(AsyncDBService1.class);
	private static final String WORK_NAME_PREFIX = "ASYNCDBTHREAD";

	private int maxThreadNum = 32;
	private SqlMapClientTemplate sqlMapClientTemplate;
	private List<Worker> workers = new ArrayList<Worker>();

	/**
	 * 出错后的保存目录
	 */
	private String path;
	
	public void setMaxThreadNum(int maxThreadNum) {

		this.maxThreadNum = maxThreadNum;
	}

	public void setSqlMapClientTemplate(
			SqlMapClientTemplate sqlMapClientTemplate) {
		this.sqlMapClientTemplate = sqlMapClientTemplate;
	}

	public void init() {
		log.info("异步数据存储服务启动...............");
		if (this.path == null) {
			this.path = System.getProperty("user.dir");
		}
		this.path = this.path+File.separator+ "asyncdb_err";
		if (!this.path.endsWith(File.separator)) {
			this.path = this.path + File.separator;
		}
		
		File tmp = new File(path);
			if (tmp.isDirectory()){
			String[] files = tmp.list();
			if (files!=null && files.length>0){
				log.info("备份上次运行为处理的错误sql...");
				tmp.renameTo(new File(tmp.getPath()+"_"+System.currentTimeMillis()));
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

		/**
		 * 检查是否有 没处理完的请求
		 */
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				log.info("AsyncDBService1 开始检查退出条件了！");
				int remainSize = 0;

				do {
					remainSize = 0;
					for (Worker worker : workers) {
						remainSize += worker.size();
					}
					log.info("AsyncDBService1 剩余Sql数量：" + remainSize);
					if (remainSize > 0) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				} while (remainSize > 0);

				log.info("AsyncDBService1 退出条件达成！");

			}
		}));
	}

	@Override
	public void asyncUpdate(List<AsyncDBData> datas) {
		for (AsyncDBData data : datas) {
			Worker worker = getWorker(data);
			worker.offer(data);
		}

	}

	private void updateDatas(List<AsyncDBData> datas ){
		if (datas.size() > 0) {
//			if (log.isDebugEnabled()){
//				log.debug("异步批量执行{}条sql开始：",datas.size());
//			}
			Iterator<AsyncDBData> lit = datas.iterator();
			while (lit.hasNext()){
				AsyncDBData data= lit.next();
				updateData(data);
				lit.remove();
			}
//			if (log.isDebugEnabled()){
//				log.debug("异步批量执行sql完成：");
//			}
		}
	}


	private void updateData(AsyncDBData data) {
		if (data.getType() == AsyncDBData.UPDATE) {
			// System.out.println("update db:"+data.getStatementId());
			// AsyncDBService.this.sqlMapClientTemplate.getSqlMapClient().
			// 如果缓存中存在数据，总是取缓存最新的数据,防止更新顺序不一致带来的问题
//			if (data.getKey() != null) {
//				Object tmp = MemcachedManager
//						.getRemote(data.getKey());
//				if (tmp != null) {
//					data.setData(tmp);
//				}
//			}
			try {
				this.sqlMapClientTemplate
						.update(data.getStatementId(), data);
//				File tmp = new File(path + data.getKey() );
//				if (tmp.exists()){
//					tmp.delete();
//				}
				//data.getKey()
			} catch (Exception e) {
				// TODO:出错的sql如何处理?
				log.error("异步更新数据库出错", e);
				log.error("异步sql:{}", data);
				saveErrorData(data);
			}
		} else {
			log
					.error("not support async insert or delete,only support update");
		}
		
	}
	private void saveErrorData(AsyncDBData data){

		ObjectOutputStream os = null;
		// 生成文件名，如果相同的key,保留最后一个
		String fileName = path + data.getKey() ;
		try {
			File tmp = new File(fileName);
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
					try{
						worker = new Worker(WORK_NAME_PREFIX + index);
						worker.start();
						workers.set(index, worker);
					}
					catch (Exception e){
						
					}
					if (log.isDebugEnabled()) {
						log.debug("start asyncDBThread:{} ", worker.getName());
					}
				}
			}
		}
		return worker;

	}

	class Worker extends Thread {
		private BlockingQueue<AsyncDBData> queue = new LinkedBlockingQueue<AsyncDBData>();

		public Worker(String name) {
			this.setDaemon(true);
			this.setName(name);
		}

		@Override
		public void run() {
			List<AsyncDBData> datas = new ArrayList<AsyncDBData>();
			while (true) {
				try {
					synchronized (this) {
						int size = queue.size(); 
						if (size>100000){
							log.warn("异步sql队列太多，目前sql数量:{}",size);
						}
						queue.drainTo(datas,1000);
						if (datas.size() == 0) { // 如果没有任何数据，则休息10s，继续判断

							this.wait(10000);
						}
					}
					AsyncDBService1.this.updateDatas(datas);
					
				} catch (Exception e) {
					log.error(e.toString(), e);
				} finally {
					datas.clear();
				}
			}
		}

		public int size() {
			return this.queue.size();
		}

		public boolean offer(AsyncDBData data) {

			boolean isSuccess = queue.offer(data);
			if (isSuccess) {
				synchronized (this) {
					this.notifyAll();
				}
			} else {
				log.error("添加数据库更新失败：{}", data);
			}
			return isSuccess;
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

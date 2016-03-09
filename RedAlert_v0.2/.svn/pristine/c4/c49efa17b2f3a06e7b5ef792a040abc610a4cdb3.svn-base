package com.youxigu.wolf.node.job.service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Runnable类型对象的任务队列
 * 单线程无sleep，以最快速度执行
 * 后调用addTask(Runnable r)方法加入队列
 * @author wuyj
 *
 */
public class SimpleTaskQueueService {

	public static final Logger log = LoggerFactory.getLogger(SimpleTaskQueueService.class);
	
	private static final BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>(10000);

	private static SimpleTaskQueueService instance = new SimpleTaskQueueService();
	
	public void init() {
		Worker thread = new Worker();
		thread.setName("SimpleTaskQueueService_thread");
		thread.setDaemon(false);
		thread.start();
		log.info("SimpleTaskQueueService初始化完成。");
	}
	
	public static SimpleTaskQueueService getInstance(){
		return instance;
	}
	
	private SimpleTaskQueueService(){
		
	}

	/**
	 * 向任务队列中加入任务,将新任务加入队列尾部 最多等待1秒直到队列有空间
	 * @param r 需要加入队列的任务
	 */
	public void addTask(Runnable r) {
		if(r == null){
			return;
		}
		try {
			queue.offer(r, 100, TimeUnit.MILLISECONDS);
			if(log.isDebugEnabled()){
				log.debug("addTask:" + r.toString());
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 执行队列任务线程 会不断的读取队列中的任务并执行它们然后删除
	 */
	class Worker extends Thread {
		public void run() {
			while (true) {
				try {
					// 取出队列里的第一个任务并执行，然后删除
					queue.take().run();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}

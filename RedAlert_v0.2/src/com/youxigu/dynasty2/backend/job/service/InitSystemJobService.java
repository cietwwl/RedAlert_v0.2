package com.youxigu.dynasty2.backend.job.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.youxigu.wolf.net.NodeSessionMgr;
import com.youxigu.wolf.node.job.Job;
import com.youxigu.wolf.node.job.service.JobService;

public class InitSystemJobService {
	public static final Logger log = LoggerFactory
			.getLogger(InitSystemJobService.class);
	private List<Job> systemJobs;

	private JobService jobService;
	private boolean shutdown;

	public void setSystemJobs(List<Job> systemJobs) {
		this.systemJobs = systemJobs;
	}

	public void setJobService(JobService jobService) {
		this.jobService = jobService;
	}

	public void init() {

		new Thread(new Runnable() {

			@Override
			public void run() {
				_init();

			}
		},"initSystemJobThread").start();
	}

	public void _init() {

		while (NodeSessionMgr.getGameNodeCount() <= 0 && !shutdown) {
			log.info("加载系统job,等待NodeServer...........");
			try {
				Thread.sleep(10000L);
			} catch (Exception e) {
			}
		}
		if (shutdown) {
			return;
		}
		log.info("加载系统job...........");
		for (Job job : systemJobs) {
			try {
				// System.out.println(job.getJobParams().length);
				jobService.startJob(job);
			} catch (Exception e) {
				log.error("error start job,jobid=" + job.getJobParams());
				e.printStackTrace();
			}
		}
		log.info("加载系统job 完成............");
		//等待40秒，保证其他任务执行完毕用于执行系统job,主要是load MapCell 
		try{
			Thread.sleep(30000);
		}catch (Exception e){
			
		}
		log.info("加载持久化 job............");
		jobService.loadSerialJob();
		log.info("加载持久化完成............");

	}

	public void destory() {
		shutdown = true;
	}
}

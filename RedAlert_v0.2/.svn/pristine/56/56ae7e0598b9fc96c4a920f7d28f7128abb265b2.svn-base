package com.youxigu.wolf.node.job.service;

import com.youxigu.wolf.node.job.Job;

public interface IJobService {

	/**
	 * 启动job服务
	 */
	void start();

	/**
	 * 加载持久化job
	 */
	void loadSerialJob();

	/**
	 * 启动一个job
	 * 
	 * @param job
	 */
	boolean startJob(Job job);

	/**
	 * 删除一个job
	 * 
	 * @param groupName
	 * @param groupId
	 */
	boolean deleteJob(String groupName, String groupId);

	/**
	 * 判断job是否在job池中
	 * 
	 * @param jobKey
	 * @return
	 */
	boolean hasJob(String jobKey);
}

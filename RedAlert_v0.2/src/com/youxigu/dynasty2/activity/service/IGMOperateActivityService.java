package com.youxigu.dynasty2.activity.service;

import com.youxigu.dynasty2.activity.domain.OperateActivity;

/**
 * 文件名    IConquerService.java
 *
 * 描  述    gm对活动进行操作
 *
 * 时  间    2014-4-9
 *
 * 作  者    huhaiquan
 *
 * 版  本    v1.2
 */
public interface IGMOperateActivityService {
	/**
	 * 添加活动
	 * 
	 * @param activity
	 */
	void addActivity(OperateActivity activity);
	
	/**
	 * 修改活动
	 * 
	 * @param activity
	 */
	void updateActivity(OperateActivity activity);
	
	/**
	 * 删除活动----TODO
	 * 
	 * @param activity
	 */
	void deleteActivity(OperateActivity activity);
	
	/**
	 * 服务器启动时，对未开始的活动启动线程，通知客户端
	 */
	void doStartActivitys();
	
	public void broadcastActivity(long actId);
	public void reloadRechargeActivity(long actId);
	
}

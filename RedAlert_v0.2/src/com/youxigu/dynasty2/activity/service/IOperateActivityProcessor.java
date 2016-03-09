package com.youxigu.dynasty2.activity.service;

import com.youxigu.dynasty2.activity.domain.BaseReward;
import com.youxigu.dynasty2.activity.domain.OperateActivity;
import com.youxigu.dynasty2.activity.enums.ActivityType;
import com.youxigu.dynasty2.activity.proto.ActivityView;

/**
 * 
 * 描 述 活动处理器 ---每种类型的活动都应该有一个子类，与OperateActivity类型处理
 * 
 * 版 本 v1.2
 */
public interface IOperateActivityProcessor {
	// 通知类型 充值1，消费2，登录4
	int NOTICE_1 = 0x1;
	int NOTICE_2 = 0x2;
	int NOTICE_3 = 0x4;

	/**
	 * OperateActivity奖励部分解析器
	 * 
	 * @param reward
	 * @param activity
	 * @return
	 */
	BaseReward parseReward(String reward, OperateActivity activity);

	/**
	 * 获得具体活动的试图
	 * 
	 * @param userId
	 * @param activity
	 * @param params
	 * @return
	 */
	ActivityView doGetActById(long userId, OperateActivity activity);

	/**
	 * 领奖
	 * 
	 * @param userId
	 * @param activity
	 * @param params
	 * @return
	 */
	ActivityView doReward(long userId, OperateActivity activity);

	/**
	 * 自动发奖
	 * 
	 * @param activity
	 */
	void doAutoReward(OperateActivity activity);

	ActivityType getProcessorKey();

	/**
	 * 判断是否有奖励
	 * 
	 * @param userId
	 * @return
	 */
	boolean hasReward(long userId, OperateActivity activity);

	/**
	 * 获得通知类型 充值1，消费2，登录4
	 * 
	 * @return
	 */
	int getNoticeType();

}

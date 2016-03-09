package com.youxigu.dynasty2.activity.service;

import java.util.Map;

import com.youxigu.dynasty2.activity.domain.OperateActivity;
import com.youxigu.dynasty2.activity.enums.ActivityEffectType;
import com.youxigu.dynasty2.activity.proto.ActivityView;
import com.youxigu.dynasty2.util.EffectValue;

/**
 * 文件名 IOperateActivityService.java
 * 
 * 描 述 把所有运营活动放在一起做统一管理， 该接口主要负责调度和管理{@link IOperateActivityProcessor}接口</br>
 * 每种类型的活动都应该是{@link IOperateActivityProcessor}的一个具体实现</br> 对于运营配置的活动，通过
 * {@link OperateActivity}数据结构去定义，不同活动的多态性</br> 通过rewardContext自定义字符串格式，通过
 * {@link IOperateActivityProcessor}来进行字符串的解析工作
 * 
 * 
 * 时 间 2014-10-23
 * 
 * 作 者 huhaiquan
 * 
 * 版 本 v1.2
 */
public interface IOperateActivityService {

	/**
	 * 
	 * 获得所有活动列表
	 * 
	 * @return
	 */
	Map<String, Object> getActivitys(long userId);

	ActivityView doGetActById(long userId, long actId,
			Map<String, Object> params);

	ActivityView doReward(long userId, long actId, Map<String, Object> params);

	OperateActivity getOperateActivity(long actId);

	void doAutoReward(long actId);

	/**
	 * 充值通知
	 * 
	 * @param userId
	 */
	void addCashNotify(long userId);

	/**
	 * 消费通知
	 * 
	 * @param userId
	 */
	void consumeNotify(long userId);

	/**
	 * 登陆通知
	 * 
	 * @param userId
	 */
	void loginNotify(long userId);

	public void sendEventMessage(boolean open, long actId);

	EffectValue getEffectValue(ActivityEffectType effect);

	/**
	 * 获得加成后的效果值
	 * 
	 * @param base
	 *            加成前
	 * @param effect
	 *            应该计算的效果
	 * @return
	 */
	public int getExpByActivityEffect(int base, ActivityEffectType effect);

}

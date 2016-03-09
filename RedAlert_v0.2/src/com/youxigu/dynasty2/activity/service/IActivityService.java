package com.youxigu.dynasty2.activity.service;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.youxigu.dynasty2.activity.domain.Activity;
import com.youxigu.dynasty2.activity.domain.AwardActivity;
import com.youxigu.dynasty2.util.EffectValue;

public interface IActivityService {

	/**
	 * 创建活动
	 */
	void createActivity(Activity activity);

	/**
	 * 更新活动
	 */
	void updateActivity(Activity activity);

	/**
	 * 删除活动
	 */
	void deleteActivity(int actId, boolean broadCast);

	Activity getActivityById(int actId);

	/**
	 * 转换成Map的该日期内有效的，开放的活动<br>
	 * 
	 * @param cal
	 * @return
	 */
	List<Map<String, Object>> getActivityMaps(Calendar cal, long userId,String pf);

	/**
	 * 获取当前时间某一种活动效果的总和
	 * 
	 * @param userId
	 * @param effTypeId
	 * @return
	 */
	EffectValue getSumEffectValueByEffectType(long userId, String effTypeId);

	int getSumEffectAbsValueByEffectType(long userId, String effTypeId);

	int getSumEffectPercentValueByEffectType(long userId, String effTypeId);

	/**
	 * 重新加载某一个activity
	 * 
	 * @param actId
	 */
	void reloadActivity(int actId, boolean broadcast);

	List<Activity> getActivitys();

	/**
	 * 创建奖励活动
	 */
	void createAwardActivity(AwardActivity activity);

	/**
	 * 更新奖励活动
	 */
	void updateAwardActivity(AwardActivity activity);

	/**
	 * 删除奖励活动
	 */
	void deleteAwardActivity(int actId, boolean broadCast);

	/**
	 * 按Id取得奖励活动
	 * 
	 * @param actId
	 * @return
	 */
	AwardActivity getAwardActivityById(int actId);

	/**
	 * 重新加载某一个activity
	 * 
	 * @param actId
	 */
	void reloadAwardActivity(int actId, boolean broadcast);

	Collection<AwardActivity> getAwardActivitys();

	/**
	 * 今天是否有奖励活动
	 * 
	 * @return
	 */
	int hasAwardActivity();

}

package com.youxigu.dynasty2.mission.service;

import com.youxigu.dynasty2.mission.service.impl.WorldMissionService.NotifyInfo;
import com.youxigu.dynasty2.util.PagerResult;

/**
 * 世界任务服务端接口
 * 
 * @author Dagangzi
 * @date 2016年1月8日
 */
public interface IWorldMissionService {
	/**
	 * 查询任务列表
	 * 
	 * @param userId
	 * @param pageNo
	 * @return
	 */
	PagerResult getUserMissionByUserId(long userId, int pageNo);

	/**
	 * 异步通知任务
	 * 
	 * @param otcType
	 * @param entId
	 * @param num
	 */
	void asynNotifyMissionModule(String otcType, int entId, int num);

	/**
	 * 在线程队列中调用
	 * 
	 * @param notifyInfo
	 */
	void doNotifyMissionModule(NotifyInfo notifyInfo);

	/**
	 * gmtool加任务
	 * 
	 * @param missionId
	 */
	void doGmtoolCreateMission(int missionId);

}

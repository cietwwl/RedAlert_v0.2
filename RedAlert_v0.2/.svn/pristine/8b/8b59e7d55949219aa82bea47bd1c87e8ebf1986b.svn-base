package com.youxigu.dynasty2.mission.service;

import com.youxigu.dynasty2.mission.domain.WorldMission;
import com.youxigu.dynasty2.util.PagerResult;

/**
 * 世界任务客户端接口
 * 
 * @author Dagangzi
 * @date 2016年1月8日
 */
public interface IWorldMissionClientService {
	/**
	 * 查询任务列表
	 * 
	 * @param userId
	 * @param pageNo
	 * @return
	 */
	PagerResult getUserMissionByUserId(long userId, int pageNo);

	/**
	 * 取得完成未领奖任务数
	 * 
	 * @param userId
	 * @return
	 */
	int[] getFinishWorldMissionNum(long userId);

	/**
	 * 通知任务模块
	 */
	void notifyMissionModule(String otcType, int entId, int num);

	/**
	 * 领奖
	 * 
	 * @param userId
	 * @param missionId
	 * @return
	 */
	WorldMission doAward(long userId, int missionId);

	/**
	 * gmtool加任务
	 * 
	 * @param missionId
	 */
	void doGmtoolCreateMission(int missionId);
}

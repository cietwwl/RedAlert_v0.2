package com.youxigu.dynasty2.mission.dao;

import java.util.List;

import com.youxigu.dynasty2.mission.domain.UserWorldMission;
import com.youxigu.dynasty2.mission.domain.WorldMission;

/**
 * 世界任务dao接口
 * 
 * @author Dagangzi
 * @date 2016年1月8日
 */
public interface IWorldMissionDao {
	/**
	 * 取得所有任务
	 * 
	 * @return
	 */
	List<WorldMission> getAllWorldMission();

	/**
	 * 按主键取任务
	 * 
	 * @param missionId
	 * @return
	 */
	WorldMission getWorldMission(int missionId);

	/**
	 * 更新任务
	 * 
	 * @param worldMission
	 */
	void updateWorldMission(WorldMission worldMission);

	/**
	 * 插入任务
	 * 
	 * @param worldMission
	 * @return
	 */
	WorldMission insertWorldMission(WorldMission worldMission);

	/**
	 * 取得玩家领奖记录
	 * 
	 * @param userId
	 * @param missionId
	 * @return
	 */
	UserWorldMission getUserWorldMission(long userId, int missionId);

	/**
	 * 插入玩家领奖记录
	 * 
	 * @param userWorldMission
	 */
	void insertUserWorldMission(UserWorldMission userWorldMission);
}

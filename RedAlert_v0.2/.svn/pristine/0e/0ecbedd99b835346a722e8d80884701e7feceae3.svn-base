package com.youxigu.dynasty2.mission.dao;

import java.util.List;

import com.youxigu.dynasty2.mission.domain.Mission;
import com.youxigu.dynasty2.mission.domain.MissionLimit;
import com.youxigu.dynasty2.mission.domain.MissionType;
import com.youxigu.dynasty2.mission.domain.UserMission;

/**
 * 任务dao接口
 * 
 * @author Dagangzi
 * @date 2016年1月8日
 */
public interface IMissionDao {
	 /**
	  * 取得所有任务类型
	  * @return
	  */
	 List<MissionType> getMissionTypes();

	 /**
	  * 取得所有任务
	  * @return
	  */
	 List<Mission> getAllMissions();

	 /**
	  * 取得所有任务条件
	  * @return
	  */
	 List<MissionLimit> getAllMissionLimits();

	 /**
	  * 按主键取任务
	  * @param id
	  * @param userId
	  * @return
	  */
	 UserMission getUserMissionById(int id, long userId);

	 /**
	  * 取君主的所有任务
	  * @param userId
	  * @return
	  */
	 List<UserMission> getUserMissionByUserId(long userId);

	 /**
	  * 按missionId取任务
	  * @param userId
	  * @param missionId
	  * @return
	  */
	 UserMission getUserMissionByMissionId(long userId, int missionId);

	 /**
	  * 更新任务
	  * @param userMission
	  */
	 void updateUserMission(UserMission userMission);

	 /**
	  * 删除任务
	  * @param userMission
	  */
	 void deleteUserMission(UserMission userMission);

	 /**
	  * 插入任务
	  * @param userMission
	  * @return
	  */
	Object insertUserMission(UserMission userMission);
}

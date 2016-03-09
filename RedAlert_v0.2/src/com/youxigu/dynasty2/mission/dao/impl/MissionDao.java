package com.youxigu.dynasty2.mission.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.manu.core.base.BaseDao;
import com.youxigu.dynasty2.mission.dao.IMissionDao;
import com.youxigu.dynasty2.mission.domain.Mission;
import com.youxigu.dynasty2.mission.domain.MissionLimit;
import com.youxigu.dynasty2.mission.domain.MissionType;
import com.youxigu.dynasty2.mission.domain.UserMission;

public class MissionDao extends BaseDao implements IMissionDao {
	@Override
	public List<MissionType> getMissionTypes() {
		return getSqlMapClientTemplate().queryForList("getMissionTypes");
	}

	@Override
	public List<Mission> getAllMissions() {
		return (List<Mission>) getSqlMapClientTemplate()
				.queryForList("getAllMissions");
	}

	@Override
	public List<MissionLimit> getAllMissionLimits() {
		return (List<MissionLimit>) getSqlMapClientTemplate()
				.queryForList("getAllMissionLimits");
	}

	@Override
	public UserMission getUserMissionById(int id, long userId) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userMissionId", id);
		paramMap.put("userId", userId);
		return (UserMission) getSqlMapClientTemplate()
				.queryForObject("getUserMissionById", paramMap);
	}

	@Override
	public List<UserMission> getUserMissionByUserId(long userId) {
		return getSqlMapClientTemplate().queryForList("getUserMissionByUserId",
				userId);
	}

	@Override
	public UserMission getUserMissionByMissionId(long userId, int missionId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userId", userId);
		param.put("missionId", missionId);
		return (UserMission) getSqlMapClientTemplate()
				.queryForObject("getUserMissionByMissionId", param);
	}

	@Override
	public void updateUserMission(UserMission userMission) {
		getSqlMapClientTemplate().update("updateUserMission", userMission);
	}

	@Override
	public void deleteUserMission(UserMission userMission) {
		getSqlMapClientTemplate().delete("deleteUserMission", userMission);
	}

	@Override
	public Object insertUserMission(UserMission userMission) {
		return getSqlMapClientTemplate().insert("insertUserMission",
				userMission);
	}
}

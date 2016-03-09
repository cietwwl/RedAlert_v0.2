package com.youxigu.dynasty2.mission.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.manu.core.base.BaseDao;
import com.youxigu.dynasty2.mission.dao.IWorldMissionDao;
import com.youxigu.dynasty2.mission.domain.UserWorldMission;
import com.youxigu.dynasty2.mission.domain.WorldMission;

public class WorldMissionDao extends BaseDao implements IWorldMissionDao {

	@Override
	public List<WorldMission> getAllWorldMission() {
		return (List<WorldMission>) getSqlMapClientTemplate()
				.queryForList("getAllWorldMission");
	}

	@Override
	public WorldMission getWorldMission(int missionId) {
		return (WorldMission) getSqlMapClientTemplate()
				.queryForObject("getWorldMission", missionId);
	}

	@Override
	public void updateWorldMission(WorldMission worldMission) {
		getSqlMapClientTemplate().update("updateWorldMission", worldMission);
	}

	@Override
	public WorldMission insertWorldMission(WorldMission worldMission) {
		return (WorldMission) getSqlMapClientTemplate()
				.insert("insertWorldMission", worldMission);
	}

	@Override
	public UserWorldMission getUserWorldMission(long userId, int missionId) {
		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("userId", userId);
		params.put("missionId", missionId);
		return (UserWorldMission) getSqlMapClientTemplate()
				.queryForObject("getUserWorldMission", params);
	}

	@Override
	public void insertUserWorldMission(UserWorldMission userWorldMission) {
		getSqlMapClientTemplate().insert("insertUserWorldMission",
				userWorldMission);
	}

}

package com.youxigu.dynasty2.risk.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.manu.core.base.BaseDao;
import com.youxigu.dynasty2.risk.dao.IRiskDao;
import com.youxigu.dynasty2.risk.domain.Risk;
import com.youxigu.dynasty2.risk.domain.RiskParentScene;
import com.youxigu.dynasty2.risk.domain.RiskScene;
import com.youxigu.dynasty2.risk.domain.UserRiskData;
import com.youxigu.dynasty2.risk.domain.UserRiskScene;

@SuppressWarnings("unchecked")
public class RiskDao extends BaseDao implements IRiskDao {

	@Override
	public List<Risk> getRisks() {
		return this.getSqlMapClientTemplate().queryForList(
				"getRisks");
	}
	
	@Override
	public List<RiskParentScene> getRiskParentScenes() {
		return this.getSqlMapClientTemplate().queryForList(
				"getRiskParentScenes");
	}

	@Override
	public List<RiskScene> getRiskScenes(int parentSceneId) {
		return this.getSqlMapClientTemplate().queryForList("getRiskScenes",
				parentSceneId);
	}

	@Override
	public void createUserRiskScene(UserRiskScene urs) {
		this.getSqlMapClientTemplate().insert("insertUserRiskScene", urs);

	}

	@Override
	public UserRiskScene getUserRiskScene(long userId, int pId) {
		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("userId", userId);
		params.put("pid", pId);
		return (UserRiskScene) this.getSqlMapClientTemplate().queryForObject(
				"getUserRiskScene", params);
	}

	@Override
	public List<UserRiskScene> getUserRiskScenes(long userId) {
		return this.getSqlMapClientTemplate().queryForList("getUserRiskScenes",
				userId);
	}

	@Override
	public void updateUserRiskScene(UserRiskScene urs) {
		this.getSqlMapClientTemplate().update("updateUserRiskScene", urs);

	}

	@Override
	public void createUserRiskData(UserRiskData usd) {
		this.getSqlMapClientTemplate().insert("insertUserRiskData", usd);

	}

	@Override
	public UserRiskData getUserRiskData(long userId) {
		return (UserRiskData) this.getSqlMapClientTemplate().queryForObject(
				"getUserRiskData", userId);
	}

	@Override
	public void updateUserRiskData(UserRiskData usd) {
		this.getSqlMapClientTemplate().update("updateUserRiskData", usd);

	}

}

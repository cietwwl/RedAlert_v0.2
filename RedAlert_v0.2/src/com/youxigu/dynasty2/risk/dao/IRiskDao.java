package com.youxigu.dynasty2.risk.dao;

import java.util.List;

import com.youxigu.dynasty2.risk.domain.Risk;
import com.youxigu.dynasty2.risk.domain.RiskParentScene;
import com.youxigu.dynasty2.risk.domain.RiskScene;
import com.youxigu.dynasty2.risk.domain.UserRiskData;
import com.youxigu.dynasty2.risk.domain.UserRiskScene;

public interface IRiskDao {

	/**
	 * 获取所有的章节
	 * 
	 * @return
	 */
	List<Risk> getRisks();

	List<RiskParentScene> getRiskParentScenes();

	List<RiskScene> getRiskScenes(int parentSceneId);

	List<UserRiskScene> getUserRiskScenes(long userId);

	UserRiskScene getUserRiskScene(long userId, int pId);

	void createUserRiskScene(UserRiskScene urs);

	void updateUserRiskScene(UserRiskScene urs);

	void createUserRiskData(UserRiskData usd);

	void updateUserRiskData(UserRiskData usd);

	UserRiskData getUserRiskData(long userId);
}

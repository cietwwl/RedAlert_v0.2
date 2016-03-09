package com.youxigu.dynasty2.user.dao.impl;

import java.util.List;
import java.util.Map;

import com.manu.core.base.BaseDao;
import com.manu.util.UtilMisc;
import com.youxigu.dynasty2.user.dao.IUserAchieveDao;
import com.youxigu.dynasty2.user.domain.Achieve;
import com.youxigu.dynasty2.user.domain.AchieveLimit;
import com.youxigu.dynasty2.user.domain.AchieveType;
import com.youxigu.dynasty2.user.domain.UserAchieve;
/**
 * 君主成就DAO接口实现类
 * @author Dagangzi
 *
 */
public class UserAchieveDao extends BaseDao implements IUserAchieveDao{
	@Override
	public List<Achieve> listAllAchieve() {
		return this.getSqlMapClientTemplate().queryForList("listAllAchieve");
	}

	@Override
	public List<AchieveType> listAchieveType() {
		return this.getSqlMapClientTemplate().queryForList("listAchieveType");
	}

	@Override
	public List<AchieveLimit> listAchieveLimit() {
		return this.getSqlMapClientTemplate().queryForList("listAchieveLimit");
	}

	@Override
	public UserAchieve getUserAchieveByEntId(long userId, int entId) {
		Map params = UtilMisc.toMap("userId", userId, "entId", entId);
		return (UserAchieve) this.getSqlMapClientTemplate()
				.queryForObject("getUserAchieveByEntId", params);
	}
	
	@Override
	public List<UserAchieve> getUserAchieveByUserId(long userId) {
		return this.getSqlMapClientTemplate().queryForList("getUserAchieveByUserId", userId);
	}

	@Override
	public void createUserAchieve(UserAchieve userAchieve) {
		this.getSqlMapClientTemplate().insert("insertUserAchieve", userAchieve);
	}

	@Override
	public void updateUserAchieve(UserAchieve userAchieve) {
		this.getSqlMapClientTemplate().update("updateUserAchieve", userAchieve);
	}

}

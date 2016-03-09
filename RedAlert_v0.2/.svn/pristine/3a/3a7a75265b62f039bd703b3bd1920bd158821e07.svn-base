package com.youxigu.dynasty2.user.dao.impl;

import java.util.HashMap;
import java.util.Map;

import com.manu.core.base.BaseDao;
import com.youxigu.dynasty2.user.dao.IUserCountDao;
import com.youxigu.dynasty2.user.domain.UserCount;
/**
 * 计数器dao实现类
 * @author Dagangzi
 *
 */
public class UserCountDao extends BaseDao implements IUserCountDao {

	@Override
	public UserCount getUserCount(long userId, String type) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("userId", userId);
		params.put("type", type);
		return (UserCount)this.getSqlMapClientTemplate().queryForObject("getUserCount",params);
	}

	@Override
	public void createUserCount(UserCount userCount) {
		this.getSqlMapClientTemplate().insert("insertUserCount",userCount);
	}

	@Override
	public void updateUserCount(UserCount userCount) {
		this.getSqlMapClientTemplate().update("updateUserCount",userCount);
	}

}

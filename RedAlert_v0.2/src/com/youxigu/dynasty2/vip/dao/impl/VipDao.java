package com.youxigu.dynasty2.vip.dao.impl;

import com.manu.core.base.BaseDao;
import com.youxigu.dynasty2.vip.dao.IVipDao;
import com.youxigu.dynasty2.vip.domain.UserVip;

/**
 * vip效果dao
 * 
 *
 */
public class VipDao extends BaseDao implements IVipDao {
	@Override
	public void createUserVip(UserVip userVip) {
		this.getSqlMapClientTemplate().insert("insertUserVip", userVip);
	}

	@Override
	public void updateUserVip(UserVip userVip) {
		this.getSqlMapClientTemplate().update("updateUserVip", userVip);
	}

	@Override
	public UserVip getUserVip(long userId) {
		return (UserVip) this.getSqlMapClientTemplate().queryForObject(
				"getUserVip", userId);
	}
}

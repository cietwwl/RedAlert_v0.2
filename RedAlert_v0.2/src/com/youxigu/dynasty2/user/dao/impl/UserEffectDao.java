package com.youxigu.dynasty2.user.dao.impl;

import java.util.List;

import com.manu.core.base.BaseDao;
import com.youxigu.dynasty2.user.dao.IUserEffectDao;
import com.youxigu.dynasty2.user.domain.UserEffect;

public class UserEffectDao extends BaseDao implements IUserEffectDao {

	@Override
	public void createUserEffect(UserEffect ce) {
		this.getSqlMapClientTemplate().insert("insertUserEffect", ce);
	}

	@Override
	public void deleteUserEffect(UserEffect ce) {
		this.getSqlMapClientTemplate().delete("deleteUserEffectById", ce);

	}

	@Override
	public List<UserEffect> getUserEffectByUserId(long userId) {
		return this.getSqlMapClientTemplate().queryForList("getUserEffectByUserId", userId);
	}

	@Override
	public void updateUserEffect(UserEffect ce) {
		this.getSqlMapClientTemplate().update("updateUserEffect", ce);

	}
}

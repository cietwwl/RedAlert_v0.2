package com.youxigu.dynasty2.common.dao.impl;

import com.manu.core.base.BaseDao;
import com.youxigu.dynasty2.common.dao.ILogOutDao;
import com.youxigu.dynasty2.user.domain.UserChat;

public class LogOutDao extends BaseDao implements ILogOutDao {

	@Override
	public UserChat getUserChat(long userId) {
		return (UserChat) this.getSqlMapClientTemplate().queryForObject(
				"getUserChat", userId);
	}

	@Override
	public void insertUserChat(UserChat userChat) {
		this.getSqlMapClientTemplate().insert("insertUserChat", userChat);
	}

}

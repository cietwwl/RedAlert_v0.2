package com.youxigu.dynasty2.common.service.impl;

import com.youxigu.dynasty2.common.dao.ILogOutDao;
import com.youxigu.dynasty2.common.service.ILogOutService;
import com.youxigu.dynasty2.user.domain.UserChat;

public class LogOutService implements ILogOutService {
	private ILogOutDao logOutDao;

	public void setLogOutDao(ILogOutDao logOutDao) {
		this.logOutDao = logOutDao;
	}

	@Override
	public void insertUserChat(UserChat userChat) {
		logOutDao.insertUserChat(userChat);
	}

	@Override
	public UserChat getUserChat(long userId) {
		return logOutDao.getUserChat(userId);
	}

}

package com.youxigu.dynasty2.user.service.impl;

import com.youxigu.dynasty2.user.dao.IUserCountDao;
import com.youxigu.dynasty2.user.domain.UserCount;
import com.youxigu.dynasty2.user.service.IUserCountService;

public class UserCountService implements IUserCountService {
	private IUserCountDao userCountDao;

	public void setUserCountDao(IUserCountDao userCountDao) {
		this.userCountDao = userCountDao;
	}

	@Override
	public UserCount getUserCount(long userId, String type) {
		return userCountDao.getUserCount(userId, type);
	}

	@Override
	public void createUserCount(UserCount userCount) {
		userCountDao.createUserCount(userCount);
	}

	@Override
	public void updateUserCount(UserCount userCount) {
		userCountDao.updateUserCount(userCount);
	}

}

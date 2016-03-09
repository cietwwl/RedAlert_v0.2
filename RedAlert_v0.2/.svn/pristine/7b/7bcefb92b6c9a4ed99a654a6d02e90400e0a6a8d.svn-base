package com.youxigu.dynasty2.vip.service.impl;

import java.sql.Timestamp;

import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.util.DateUtils;
import com.youxigu.dynasty2.vip.dao.IVipDao;
import com.youxigu.dynasty2.vip.domain.UserVip;
import com.youxigu.dynasty2.vip.service.IVipService;

public class VipService implements IVipService {
	private IVipDao vipDao = null;

	@Override
	public UserVip getUserVip(long userId) {
		return vipDao.getUserVip(userId);
	}

	@Override
	public int getVipLvByUserId(long userId) {
		UserVip userVip = vipDao.getUserVip(userId);
		if (userVip == null) {
			return 0;
		}
		if (userVip.isExpire()) {// 过期
			return 0;
		}
		return userVip.getVipLv();
	}

	@Override
	public void doUpdateUserVip(UserVip userVip) {
		vipDao.updateUserVip(userVip);
	}

	@Override
	public void doUpdateUserVip(User user, int point) {

	}

	@Override
	public void doCreateUserVip(long userId) {
		Timestamp now = DateUtils.nowTimestamp();
		UserVip userVip = new UserVip();
		userVip.setUserId(userId);
		userVip.setLastDttm(now);
		userVip.setVipPoint(0);// 默认所有用户都vip0
		userVip.setVipEndTime(now);
		userVip.setVipStartTime(now);
		userVip.setVipLv(0);
		userVip.setPrizeCount(0);
		vipDao.createUserVip(userVip);
	}

	@Override
	public void sendFlushDevDataEvent(long userId, UserVip userVip) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isVip(long userId) {
		UserVip userVip = this.getUserVip(userId);
		if (userVip == null)
			return false;
		return !userVip.isExpire();
	}

	public void setVipDao(IVipDao vipDao) {
		this.vipDao = vipDao;
	}

}

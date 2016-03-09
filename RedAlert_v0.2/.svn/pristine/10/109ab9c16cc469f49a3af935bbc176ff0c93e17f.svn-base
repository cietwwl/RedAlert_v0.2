package com.youxigu.dynasty2.gmtool.dao.impl;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.manu.core.base.BaseDao;
import com.youxigu.dynasty2.gmtool.dao.IGmToolDao;
import com.youxigu.dynasty2.gmtool.domain.OnlineUser;
import com.youxigu.dynasty2.gmtool.domain.UserCashBonus;

@SuppressWarnings("unchecked")
public class GmToolDao extends BaseDao implements IGmToolDao {

	@Override
	public List<OnlineUser> getOnlineUser(Timestamp start, Timestamp end) {
		Map<String, Timestamp> map = new HashMap<String, Timestamp>(2);
		map.put("start", start);
		map.put("end", end);
		return this.getSqlMapClientTemplate()
				.queryForList("getOnlineUser", map);
	}

	@Override
	public void createUserCashBonus(UserCashBonus ucb) {
		this.getSqlMapClientTemplate().insert("insertUserCashBonus", ucb);
	}

	@Override
	public List<UserCashBonus> getAllUserCashBonus() {

		return this.getSqlMapClientTemplate().queryForList(
				"getAllUserCashBonus");
	}

	@Override
	public void updateUserCashBonus(UserCashBonus ucb) {
		this.getSqlMapClientTemplate().update("updateUserCashBonus", ucb);

	}

	@Override
	public UserCashBonus getUserCashBonus(String openId) {

		return (UserCashBonus) this.getSqlMapClientTemplate().queryForObject(
				"getUserCashBonus", openId);
	}
}

package com.youxigu.dynasty2.activity.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.manu.core.base.BaseDao;
import com.youxigu.dynasty2.activity.dao.IActivityDao;
import com.youxigu.dynasty2.activity.domain.Activity;
import com.youxigu.dynasty2.activity.domain.AwardActivity;
import com.youxigu.dynasty2.activity.domain.MysticShop;
import com.youxigu.dynasty2.activity.domain.MysticShopItem;
import com.youxigu.dynasty2.activity.domain.OperateActivity;
import com.youxigu.dynasty2.activity.domain.UserMysticShop;
import com.youxigu.dynasty2.activity.domain.UserOperateActivity;

@SuppressWarnings("unchecked")
public class ActivityDao extends BaseDao implements IActivityDao {

	public ActivityDao() {
	}

	@Override
	public List<MysticShop> getAllMysticShop() {
		return super.getSqlMapClientTemplate().queryForList("getAllMysticShop");
	}

	@Override
	public List<MysticShopItem> getAllMysticShopItem() {
		return super.getSqlMapClientTemplate().queryForList(
				"getAllMysticShopItem");
	}

	@Override
	public void insertUserMysticShop(UserMysticShop userMysticShop) {
		super.getSqlMapClientTemplate().insert("insertUserMysticShop",
				userMysticShop);

	}

	@Override
	public void updateUserMysticShop(UserMysticShop userMysticShop) {
		super.getSqlMapClientTemplate().update("updateUserMysticShop",
				userMysticShop);

	}

	@Override
	public UserMysticShop getUserMysticShop(long userId, int shopId) {
		Map<String, Object> data = new HashMap<String, Object>(2);
		data.put("userId", userId);
		data.put("shopId", shopId);
		return (UserMysticShop) super.getSqlMapClientTemplate().queryForObject(
				"getUserMysticShop", data);
	}

	@Override
	public void createOperateActivity(OperateActivity act) {
		super.getSqlMapClientTemplate().insert("createOperateActivity", act);

	}

	@Override
	public OperateActivity getOperateActivity(long actId) {
		return (OperateActivity) super.getSqlMapClientTemplate()
				.queryForObject("getOperateActivity", actId);
	}

	@Override
	public List<OperateActivity> getOperateActivitys() {
		return super.getSqlMapClientTemplate().queryForList(
				"getOperateActivitys");
	}

	@Override
	public void updateOperateActivity(OperateActivity act) {
		super.getSqlMapClientTemplate().update("updateOperateActivity", act);

	}

	@Override
	public void deleteOperateActivity(long actId) {
		super.getSqlMapClientTemplate().delete("deleteOperateActivity", actId);

	}

	@Override
	public UserOperateActivity getUserOperateActivity(long userId, int type) {
		Map<String, Object> data = new HashMap<String, Object>(2);
		data.put("userId", userId);
		data.put("type", type);
		return (UserOperateActivity) super.getSqlMapClientTemplate()
				.queryForObject("getUserOperateActivity", data);
	}

	@Override
	public void updateUserOperateActivity(UserOperateActivity act) {
		super.getSqlMapClientTemplate()
				.update("updateUserOperateActivity", act);

	}

	@Override
	public void createUserOperateActivity(UserOperateActivity act) {
		super.getSqlMapClientTemplate()
				.insert("createUserOperateActivity", act);

	}

	@Override
	public void createActivity(Activity act) {
		this.getSqlMapClientTemplate().insert("insertActivity", act);

	}

	@Override
	public void deleteActivity(int actId) {
		this.getSqlMapClientTemplate().delete("deleteActivity", actId);

	}

	@Override
	public Activity getActivity(int actId) {
		return (Activity) this.getSqlMapClientTemplate().queryForObject(
				"getActivity", actId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Activity> getActivitys() {
		return this.getSqlMapClientTemplate().queryForList("getActivitys");
	}

	@Override
	public void updateActivity(Activity act) {
		this.getSqlMapClientTemplate().update("updateActivity", act);
	}

	@Override
	public void createAwardActivity(AwardActivity act) {
		this.getSqlMapClientTemplate().insert("insertAwardActivity", act);

	}

	@Override
	public void deleteAwardActivity(int actId) {
		this.getSqlMapClientTemplate().delete("deleteAwardActivity", actId);

	}

	@Override
	public AwardActivity getAwardActivity(int actId) {

		return (AwardActivity) this.getSqlMapClientTemplate().queryForObject(
				"getAwardActivity", actId);
	}

	@Override
	public List<AwardActivity> getAwardActivitys() {
		return this.getSqlMapClientTemplate().queryForList("getAwardActivitys");
	}

	@Override
	public void updateAwardActivity(AwardActivity act) {
		this.getSqlMapClientTemplate().update("updateAwardActivity", act);

	}
}

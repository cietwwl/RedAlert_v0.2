package com.youxigu.dynasty2.user.dao.impl;

import java.util.Map;

import com.manu.core.base.BaseDao;
import com.manu.util.UtilMisc;
import com.youxigu.dynasty2.user.dao.IUserAttrDao;

public class UserAttrDao extends BaseDao implements IUserAttrDao {

	@Override
	public void insertUserAttr(long userId, String attrName, String attrValue) {
		this.getSqlMapClientTemplate().insert("insertUserAttr", UtilMisc.toMap("userId", userId, "attrName", attrName, "attrValue", attrValue));
	}

	@Override
	public void updateUserAttr(long userId, String attrName, String attrValue) {
		this.getSqlMapClientTemplate().update("updateUserAttr", UtilMisc.toMap("userId", userId, "attrName", attrName, "attrValue", attrValue));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> getAllUserAttr(long userId) {
		return this.getSqlMapClientTemplate().queryForMap("getAllUserAttr", UtilMisc.toMap("userId", userId), "attrName", "attrValue");
	}

	@Override
	public String getUserAttr(long userId, String attrName) {
		return (String)this.getSqlMapClientTemplate().queryForObject("getUserAttr", UtilMisc.toMap("userId", userId, "attrName", attrName));
		
	}

}

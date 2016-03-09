package com.youxigu.dynasty2.cdkey.dao.impl;

import java.util.HashMap;
import java.util.Map;

import com.manu.core.base.BaseDao;
import com.youxigu.dynasty2.cdkey.dao.ICdkeyDao;
import com.youxigu.dynasty2.cdkey.domain.UserCdkey;

public class CdkeyDao extends BaseDao implements ICdkeyDao {

	@Override
	public UserCdkey getUserCdKey(long userId, String cdkeyType) {

		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("userId", userId);
		params.put("keyType", cdkeyType);
		return (UserCdkey) this.getSqlMapClientTemplate().queryForObject(
				"getUserCdkey", params);
	}

	@Override
	public void insertUserCdKey(UserCdkey cdkey) {
		this.getSqlMapClientTemplate().insert("insertUserCdkey", cdkey);
	}

}

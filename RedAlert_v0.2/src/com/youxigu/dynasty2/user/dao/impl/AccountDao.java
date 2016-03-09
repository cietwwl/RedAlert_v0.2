package com.youxigu.dynasty2.user.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.manu.core.base.BaseDao;
import com.youxigu.dynasty2.user.dao.IAccountDao;
import com.youxigu.dynasty2.user.domain.Account;
import com.youxigu.dynasty2.user.domain.AccountWhiteList;

public class AccountDao extends BaseDao implements IAccountDao {

	@Override
	public void insertAccount(Account account) {

		this.getSqlMapClientTemplate().insert("insertAccount", account);

	}

	@Override
	public void updateAccount(Account account) {
		this.getSqlMapClientTemplate().update("updateAccount", account);
	}

	@Override
	public Account getAccountByAccId(long accId) {
		return (Account) this.getSqlMapClientTemplate().queryForObject(
				"getAccountByAccId", accId);
	}

	@Override
	public Account getAccountByAccNameAreaId(String accName, String areaId) {
		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("accName", accName);
		params.put("areaId", areaId);
		return (Account) this.getSqlMapClientTemplate().queryForObject(
				"getAccountByAccName", params);
	}

	@Override
	public int getSumAccount() {
		return (Integer) this.getSqlMapClientTemplate().queryForObject(
				"getCountAccount");
	}

	@Override
	public void insertAccountWhiteList(AccountWhiteList accountWhiteList) {
		this.getSqlMapClientTemplate().insert("insertAccounttWhiteList",
				accountWhiteList);
	}

	@Override
	public void updateAccountWhiteList(AccountWhiteList accountWhiteList) {
		this.getSqlMapClientTemplate().update("updateAccounttWhiteList",
				accountWhiteList);

	}

	@Override
	public AccountWhiteList getAccountWhiteList(String openId) {
		return (AccountWhiteList) this.getSqlMapClientTemplate()
				.queryForObject("getAccounttWhiteList", openId);
	}

	@Override
	public List<Account> getBanList(int areaId) {
		return super.getSqlMapClientTemplate().queryForList("getBanList", areaId);
	}

}

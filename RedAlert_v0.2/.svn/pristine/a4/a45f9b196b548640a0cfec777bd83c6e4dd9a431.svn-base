package com.youxigu.dynasty2.user.dao;

import java.util.List;

import com.youxigu.dynasty2.user.domain.Account;
import com.youxigu.dynasty2.user.domain.AccountWhiteList;

public interface IAccountDao {
	void insertAccount(Account account);

	void updateAccount(Account account);

	Account getAccountByAccId(long accId);

	Account getAccountByAccNameAreaId(String accName,String areaId);
	
	int getSumAccount();
	
	
	void insertAccountWhiteList(AccountWhiteList accountWhiteList);

	void updateAccountWhiteList(AccountWhiteList accountWhiteList);
	
	AccountWhiteList getAccountWhiteList(String openId);
	List<Account> getBanList(int areaId);

}

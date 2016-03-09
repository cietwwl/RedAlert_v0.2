package com.youxigu.dynasty2.user.service;

import java.util.List;
import java.util.Map;

import com.youxigu.dynasty2.user.domain.Account;
import com.youxigu.dynasty2.user.domain.AccountWhiteList;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.user.proto.UserMsg.Request10001Define;
import com.youxigu.wolf.net.UserSession;

public interface IAccountService {

	/**
	 * login
	 * 
	 * @param params
	 * @return
	 */
	Map<String, Object> login(Request10001Define request, Map<String,Object> params);

	/**
	 * 注销用户
	 */
	void logout(long accId,UserSession session);

	
	void doBindVistor(UserSession us,Map<String, Object> params);
	/**
	 * 处理登陆队列用的
	 * 
	 * @param account
	 * @param us
	 * @param user
	 * @param newAcc
	 */
	void doAfterLogin(Account account, UserSession us, User user, boolean newAcc);

	/**
	 * 
	 * @param accId
	 * @return
	 */
	Account getAccount(long accId);

	Account getAccountByNameAreaId(String accName,String areaId);

	/**
	 * 更新账号
	 * 
	 * @param account
	 */
	void updateAccount(Account account);

	/**
	 * 封禁账号
	 * 
	 * @param accName
	 *            君主角色名
	 * @param second
	 *            封号时间(秒)
	 * @param isBan
	 *            true：封号； false:解封
	 */
	Account doDealBanAccount(String accName, long second, boolean isBan,String areaId,String reason);

	/**
	 * @return 总注册人数
	 */
	int getSumAccount();

	/**
	 * 白名单
	 * @param accountWhiteList
	 */
	void createAccountWhiteList(AccountWhiteList accountWhiteList);
	void updateAccountWhiteList(AccountWhiteList accountWhiteList);
	AccountWhiteList getAccountWhiteList(String openId);
	
	
	List<Account> getBanList(int areaId);

	/**
	 * 取得在线时间减益效果，防沉迷用
	 * 
	 * @param userId
	 * @return
	 */
	int getOnlineUserEffect(long userId);

	int getOnlineUserEffect(UserSession us);

	/**
	 * 
	 * @param queueLen 登陆等待队列长度
	 * @param period	登陆等待队列刷新时间间隔，秒
	 */
	void setLoginQueue(int queueLen,int period);
	
	
	/**
	 * 前台开通QQ会员后，后台调用tx接口更新会员数据
	 * @param userId
	 */
	void updateQQVip(UserSession us,Map<String,Object> params) ;
	
	/**
	 * mainServer调用:mainserver收到平台回调后，通知nodeserver
	 * @param accId
	 * @param qqFlag
	 */
	void updateQQVipByMainServer(long accId,int qqFlag) ;
	
}

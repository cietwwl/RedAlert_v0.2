package com.youxigu.dynasty2.user.service;

import java.util.Map;

import com.youxigu.wolf.net.UserSession;
/**
 * 操作密码接口
 * 即二级密码
 * 
 * 范例：
 <bean class="com.youxigu.dynasty2.core.flex.ActionDefine">
		<property name="cmd" value="60007" />
		<property name="bean" ref="treasuryAction" />
		<property name="methodName" value="delItem" />
        <property name="filterEventTypes" value="7001"/>
        <property name="pwdcheak" value="true" /><!-- 二级密码校验 -->
 </bean>
 * 
 * @author Dagangzi
 *
 */
public interface IOperPasswordService {
	/**
	 * 设置密码
	 * 
	 * @param us
	 * @param newPwd
	 */
	void doSetPassword(UserSession us, String newPwd);

	/**
	 * 修改密码
	 * 
	 * @param us
	 * @param oldPwd
	 * @param newPwd
	 * @return
	 */
	Map<String, Object> doUpdatePassword(UserSession us, String oldPwd,
			String newPwd);

	/**
	 * 删除密码
	 * 
	 * @param us
	 * @param oldPwd
	 * @return
	 */
	Map<String, Object> doDelPassword(UserSession us, String oldPwd);

	/**
	 * 删除密码
	 * 
	 * @param userId
	 * @return
	 */
	boolean doGMDelPassword(long userId);

	void doBroadcastDelPassword(long userId);

	/**
	 * 校验二级密码
	 * 
	 * @param us
	 * @param oldPwd
	 */
	void doCheakPassword(UserSession us, String oldPwd);

	/**
	 * 获取二级密码操作次数信息
	 * 
	 * @param userId
	 * @param params
	 * @return
	 */
	Map<String, Object> getPasswordInfo(long userId, Map<String, Object> params);
}

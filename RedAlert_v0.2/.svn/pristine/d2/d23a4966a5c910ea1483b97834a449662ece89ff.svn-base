package com.youxigu.dynasty2.user.web;

import java.util.Map;

import com.youxigu.dynasty2.core.flex.amf.BaseAction;
import com.youxigu.dynasty2.user.service.IOperPasswordService;
import com.youxigu.wolf.net.Response;
import com.youxigu.wolf.net.UserSession;
/**
 * 操作密码/二级密码协议
 * @author Dagangzi
 * TODO 目前推消息和action还没有改成protobuff，因为不确定这个功能以后有没有，目前只是为了管理管可以正常重置二级密码
 * 
 *  * 范例：
 <bean class="com.youxigu.dynasty2.core.flex.ActionDefine">
		<property name="cmd" value="60007" />
		<property name="bean" ref="treasuryAction" />
		<property name="methodName" value="delItem" />
        <property name="filterEventTypes" value="7001"/>
        <property name="pwdcheak" value="true" /><!-- 二级密码校验 -->
 </bean>
 */
public class OperPasswordAction extends BaseAction {
	private IOperPasswordService operPasswordService;

	public void setOperPasswordService(IOperPasswordService operPasswordService) {
		this.operPasswordService = operPasswordService;
	}

	/**
	 * 设置密码-23001
	 * 
	 * @param params
	 * @param response
	 * @return
	 */
	@Deprecated
	public Object setPassword(Map<String, Object> params, Response response) {
		String newPwd = (String) params.remove("newPwd");
		UserSession us = super.getUserSession(response);
		operPasswordService.doSetPassword(us, newPwd);
		return params;
	}

	/**
	 * 修改密码-23002
	 * 
	 * @param params
	 * @param response
	 * @return
	 */
	@Deprecated
	public Object updatePassword(Map<String, Object> params, Response response) {
		String oldPwd = (String) params.remove("oldPwd");
		String newPwd = (String) params.remove("newPwd");
		UserSession us = super.getUserSession(response);
		return operPasswordService.doUpdatePassword(us, oldPwd, newPwd);
	}

	/**
	 * 删除密码-23003
	 * 
	 * @param params
	 * @param response
	 * @return
	 */
	@Deprecated
	public Object delPassword(Map<String, Object> params, Response response) {
		String oldPwd = (String) params.remove("oldPwd");
		UserSession us = super.getUserSession(response);
		return operPasswordService.doDelPassword(us, oldPwd);
	}

	/**
	 * 校验二级密码-23004
	 * 
	 * @param params
	 * @param response
	 * @return
	 */
	@Deprecated
	public Object cheakPassword(Map<String, Object> params, Response response) {
		String oldPwd = (String) params.remove("oldPwd");
		UserSession us = super.getUserSession(response);
		operPasswordService.doCheakPassword(us, oldPwd);
		return params;
	}

	/**
	 * 获取二级密码次数信息-23005
	 * 
	 * @param params
	 * @param response
	 * @return
	 */
	@Deprecated
	public Object getPasswordInfo(Map<String, Object> params, Response response) {
		UserSession us = super.getUserSession(response);
		return operPasswordService.getPasswordInfo(us.getUserId(), params);
	}
}

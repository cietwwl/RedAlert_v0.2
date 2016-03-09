package com.youxigu.dynasty2.user.web;

import com.youxigu.dynasty2.core.flex.amf.BaseAction;
import com.youxigu.wolf.net.OnlineUserSessionManager;
import com.youxigu.wolf.net.Response;

public class LogoutAction extends BaseAction {

	public Object logout(Object params, Response context) {
		
		OnlineUserSessionManager.unRegister(context);
		return null;
	}
}

package com.youxigu.dynasty2.core.flex.amf;

import com.youxigu.dynasty2.chat.proto.CommonHead.ResponseHead;
import com.youxigu.wolf.net.OnlineUserSessionManager;
import com.youxigu.wolf.net.Response;
import com.youxigu.wolf.net.UserSession;

public abstract class BaseAction implements IAMF3Action {

	public static UserSession getUserSession(Response context) {
		return (UserSession) context.get(UserSession.KEY_USERSESSION);
	}

	public static void registerUserSession(Response context, UserSession session) {
		OnlineUserSessionManager.register(session, context.getSession());
	}
	
	/**
	 * 返回响应头
	 * @param responseCmd
	 * @param requestCmd
	 * @return
	 */
	public static ResponseHead getResponseHead(int requestCmd) {
		//返回值
		ResponseHead.Builder headBr = ResponseHead.newBuilder();
		headBr.setCmd(requestCmd +1);
		headBr.setRequestCmd(requestCmd);
		return headBr.build();
	}

}

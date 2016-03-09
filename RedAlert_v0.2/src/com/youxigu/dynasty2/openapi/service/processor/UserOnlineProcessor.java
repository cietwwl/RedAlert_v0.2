
package com.youxigu.dynasty2.openapi.service.processor;

import java.util.Map;

import org.codehaus.jackson.JsonNode;

import com.youxigu.dynasty2.user.domain.Account;
import com.youxigu.dynasty2.user.domain.User;

public class UserOnlineProcessor   extends AreaAndOpenProcessor{
//    "body" :
//    {
//        "LoginTime" : ,     /* 登录时间点 */
//        "LogoutTime" : ,    /* 登出时间点 */
//        "Online" :          /* 本次在线时长（秒） */
//    }
	@Override
	protected Status doProcessAndViewImp(Account account, User user,
			JsonNode body, Map<String, Object> returnBody) {
		int LoginTime = (int) (account.getLastLoginDttm().getTime()/1000);
		int LogoutTime =(int)(account.getLastLogoutDttm().getTime()/1000);
		int OnLine = LogoutTime - LoginTime;
		if(OnLine<0){//当前在线
			OnLine = (int)(System.currentTimeMillis()/1000 - LoginTime);
		}
		returnBody.put("LoginTime", LoginTime);
		returnBody.put("LogoutTime", LogoutTime);
		returnBody.put("Online", OnLine);
		return new Status();
	}

	@Override
	public int getCmd() {
		return 4115;
	}

	@Override
	public int getRetrunCmd() {
		return 4116;
	}

}

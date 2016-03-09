package com.youxigu.dynasty2.cdkey.web;

import java.util.Map;

import com.youxigu.dynasty2.cdkey.service.ICdkeyService;
import com.youxigu.dynasty2.core.flex.amf.BaseAction;
import com.youxigu.dynasty2.openapi.Constant;
import com.youxigu.dynasty2.user.domain.Account;
import com.youxigu.dynasty2.user.service.IAccountService;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.wolf.net.Response;
import com.youxigu.wolf.net.UserSession;

public class CdkeyAction extends BaseAction {

	private ICdkeyService cdkeyService;
	private IAccountService accountService;

	public void setCdkeyService(ICdkeyService cdkeyService) {
		this.cdkeyService = cdkeyService;
	}

	public void setAccountService(IAccountService accountService) {
		this.accountService = accountService;
	}

	/**
	 * 使用cdkey 16110
	 * @param params
	 * @param context
	 * @return
	 */
	public Object useCdkey(Map<String, Object> params, Response context) {
		UserSession session = getUserSession(context);
		if (session.getdType() == Constant.DEVIDE_TYPE_IOS) {
			throw new BaseException("激活码功能未开放");
		}
		String cdkey = (String) params.get("cdkey");
		if (cdkey==null || cdkey.length()<=6){
			throw new BaseException("激活码格式错误");
		}
		Account account = accountService.getAccount(session.getAccId());
		cdkeyService.doUseCdKey(session.getUserId(),account.getAccName(),account.getAreaId(), cdkey, params);
		return params;
	}
}

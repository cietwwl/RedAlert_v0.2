package com.youxigu.dynasty2.openapi.service.processor;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.codehaus.jackson.JsonNode;

import com.youxigu.dynasty2.openapi.domain.Head;
import com.youxigu.dynasty2.openapi.service.IIdipProcessor;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.user.service.IAccountService;
import com.youxigu.dynasty2.user.service.impl.UserService;

public abstract class AIdipProcessor implements IIdipProcessor {
	protected IAccountService accountService;
	protected UserService userService;

	/**
	 * 错误码,返回码类型：0：处理成功，需要解开包体获得详细信息,1：处理成功，但包体返回为空，不需要处理包体（eg：查询用户角色，用户角色不存在等），-1:
	 * 网络通信异常,-2：超时,-3：数据库操作异常,-4：API返回异常,-5：服务器忙,-6：其他错误,小于-100
	 * ：用户自定义错误，需要填写szRetErrMsg *
	 */
	@Override
	public String doIdipProcess(Head buildHead, JsonNode body) {
		Map<String, Object> returnBody = new HashMap<String, Object>();
		Status status = doProcessAndView(body, returnBody);
		buildHead.setCmdid(this.getRetrunCmd());
		buildHead.setResult(status.errCode);
		buildHead.setRetErrMsg(status.errDesc);
		Map packet = new HashMap();
		packet.put("head", JSONObject.fromObject(buildHead.toMap()));
		packet.put("body", JSONObject.fromObject(returnBody));
		return JSONObject.fromObject(packet).toString();
	}

	/**
	 * @param idip
	 * @param returnBody返回体，有错误不需要加值
	 * @return
	 */
	protected abstract Status doProcessAndView(JsonNode body,
			Map<String, Object> returnBody);

	protected Map<String,Object> getUserBaseInfo(int Partition,User user){
		Map<String, Object> data = new HashMap<String, Object>(8);
		data.put("Partition", Partition);
		data.put("RoleName", user.getUserName());
		data.put("RoleId", user.getUserId());
		data.put("UseLevel", user.getUsrLv());	
		return data;
	}
	
	
	protected static class Status {
		protected int errCode;
		protected String errDesc="";
		
		public Status(){}
		public Status(int errCode,String errDesc){
			this.errCode = errCode;
			this.errDesc = errDesc;
		}
	}


	/**
	 * @param accountService the accountService to set
	 */
	public void setAccountService(IAccountService accountService) {
		this.accountService = accountService;
	}

	/**
	 * @param userService the userService to set
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}

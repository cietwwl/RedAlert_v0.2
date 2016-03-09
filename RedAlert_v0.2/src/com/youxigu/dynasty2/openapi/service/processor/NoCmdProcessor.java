package com.youxigu.dynasty2.openapi.service.processor;

import java.util.Map;

import org.codehaus.jackson.JsonNode;

public class NoCmdProcessor extends AIdipProcessor {

	@Override
	protected Status doProcessAndView(JsonNode body,
			Map<String, Object> returnBody) {
		Status status = new Status();
		status.errCode = ERR_CODE_1005;
		status.errDesc = "请求命令错误，没有处理此命令方法。请检查idip接口请求命令：Cmdid";
		return status;
	}

	@Override
	public int getCmd() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getRetrunCmd() {
		// TODO Auto-generated method stub
		return 0;
	}

}

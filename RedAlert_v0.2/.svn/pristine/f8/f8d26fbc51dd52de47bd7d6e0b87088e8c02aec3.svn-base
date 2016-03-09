package com.youxigu.wolf.net;

import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestTimeoutCloseHandler implements
		KeepAliveRequestTimeoutHandler {
	private static Logger logger = LoggerFactory
			.getLogger(RequestTimeoutCloseHandler.class);

	/**
	 * 超时几次关闭连接
	 */
	private int timeoutNum = 3;

	public void setTimeoutNum(int timeoutNum) {
		this.timeoutNum = timeoutNum;
	}

	public RequestTimeoutCloseHandler() {
	}

	public RequestTimeoutCloseHandler(int timeoutNum) {
		this.timeoutNum = timeoutNum;
	}

	public void keepAliveRequestTimedOut(KeepAliveFilter filter,
			IoSession session) throws Exception {
		Integer num = (Integer) session
				.getAttribute(KeepAliveFilter.TIMEOUT_NUM);
		if (num == null || num >= timeoutNum) {
			if (logger.isWarnEnabled()) {
				logger.warn("心跳超时:{}s，关闭socket连接:{}", filter
						.getRequestTimeout(), session);
			}
			
			Map<String, Object> params = new HashMap<String, Object>(3);
			params.put("cmd", 1);
			params.put("errCode", -9011);
			params
					.put("err","心跳无响应，服务器关闭连接");
			session.write(params);
			session.close(false);
			//session.close(true);
		}else{
			if (logger.isWarnEnabled()) {
				logger.warn("{}心跳超时{}次",session,num);
			}
		}
	}
}

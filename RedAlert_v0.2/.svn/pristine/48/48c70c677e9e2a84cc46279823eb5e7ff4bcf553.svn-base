package com.youxigu.wolf.net;

import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.youxigu.dynasty2.chat.proto.CommonHead.ResponseHead;
import com.youxigu.dynasty2.core.flex.amf.IAMF3Action;
/**
 * 支持protobuf or map心跳超时处理
 * @author Dagangzi
 *
 */
public class RequestTimeoutCloseProtoHandler implements
		KeepAliveRequestTimeoutHandler {
	private static Logger logger = LoggerFactory
			.getLogger(RequestTimeoutCloseProtoHandler.class);

	/**
	 * 超时几次关闭连接
	 */
	private int timeoutNum = 3;

	public void setTimeoutNum(int timeoutNum) {
		this.timeoutNum = timeoutNum;
	}

	public RequestTimeoutCloseProtoHandler() {
	}

	public RequestTimeoutCloseProtoHandler(int timeoutNum) {
		this.timeoutNum = timeoutNum;
	}

	public void keepAliveRequestTimedOut(KeepAliveFilter filter,
			IoSession session) throws Exception {
		Integer num = (Integer) session
				.getAttribute(KeepAliveFilter.TIMEOUT_NUM);
		if (num == null || num >= timeoutNum) {
			if (logger.isWarnEnabled()) {
				logger.warn(num+"次心跳超时:{}s，关闭socket连接:{}", filter
						.getRequestTimeout(), session);
			}
			
			Object retu = null;
			if(session.containsAttribute(Response.GMTOOL_KEY)) {
				Map<String, Object> params = new HashMap<String, Object>(3);
				params.put("cmd", 1);
				params.put("errCode", -9011);
				params
						.put("err","心跳无响应，服务器关闭连接");
				retu = params;
			}else {
				//断开连接前发一个消息给前端
				ResponseHead.Builder badRequest = ResponseHead.newBuilder();
				badRequest.setCmd(IAMF3Action.CMD_DEFAULT);
				badRequest.setErrCode(IAMF3Action.CMD_KEEPALIVE_TIMEOUT);
				badRequest.setErr("心跳无响应，服务器关闭连接");
				badRequest.setRequestCmd(KeepAliveMessageFactoryProtoImpl.BEAT_REQ);
				retu = badRequest.build();
			}
			
			session.write(retu);
			session.close(false);
			//session.close(true);
		}else{
			if (logger.isWarnEnabled()) {
				logger.warn("{}心跳超时{}次",session,num);
			}
		}
	}
}

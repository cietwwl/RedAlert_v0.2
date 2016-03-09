package com.youxigu.wolf.net;

import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;

/**
 * 心跳消息
 * 
 * @author Administrator
 * 
 */
public class KeepAliveMessageFactoryImpl implements KeepAliveMessageFactory {
	public static final int BEAT_REQ = 100000;
	public static final int BEAT_RES = 100001;
	private static final String key = "cmd";

	private boolean notSendRequest;// 不发送请求，只接受心跳请求
	private Map<String, Object> req = new HashMap<String, Object>();
	private Map<String, Object> res = new HashMap<String, Object>();

	public KeepAliveMessageFactoryImpl() {
		req.put(key, BEAT_REQ);
		res.put(key, BEAT_RES);
	}

	public KeepAliveMessageFactoryImpl(boolean notSendRequest) {
		req.put(key, BEAT_REQ);
		res.put(key, BEAT_RES);
		this.notSendRequest = notSendRequest;
	}

	@Override
	public Object getRequest(IoSession session) {
		if (notSendRequest) {
			return null;
		} else if (!session.containsAttribute(Response.CREDIT)) {
			// 不可信的连接,不心跳
			return null;
		}
		return req;
	}

	@Override
	public Object getResponse(IoSession session, Object request) {
		return res;
	}

	@Override
	public boolean isRequest(IoSession session, Object message) {
		if (message instanceof Map) {
			Map msg = (Map) message;
			Object obj = msg.get(key);
			if (obj != null && obj instanceof Integer) {
				if (((Integer) obj).intValue() == BEAT_REQ) {
					// System.out.println("发送心跳响应：。。。。。。。。"+session.getRemoteAddress());
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean isResponse(IoSession session, Object message) {
		if (message instanceof Map) {
			Map msg = (Map) message;
			Object obj = msg.get(key);
			if (obj != null && obj instanceof Integer) {
				if (((Integer) obj).intValue() == BEAT_RES) {
					// System.out.println("收到心跳响应：。。。。。。。。"+session.getRemoteAddress());
					return true;
				}
			}
		}
		return false;
	}

}

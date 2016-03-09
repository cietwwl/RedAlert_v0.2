package com.youxigu.wolf.net;

import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;

import com.youxigu.dynasty2.chat.proto.CommonHead.KeepAliveRequest;
import com.youxigu.dynasty2.chat.proto.CommonHead.KeepAliveResponse;

/**
 * 支持protobuf or map 心跳消息
 * 
 * @author Administrator
 * 
 */
public class KeepAliveMessageFactoryProtoImpl implements KeepAliveMessageFactory {
	public static final int BEAT_REQ = 100000;
	public static final int BEAT_RES = 100001;
	private static final String key = "cmd";

	private boolean notSendRequest;// 不发送请求，只接受心跳请求
	private KeepAliveRequest req = null;
	private KeepAliveResponse res = null;
	
	private Map<String, Object> req2 = new HashMap<String, Object>();
	private Map<String, Object> res2 = new HashMap<String, Object>();
	
	/**
	 * 心跳请求和响应的消息定义
	 */
	private void buildKeepAliveMessage() {
		//请求定义
		KeepAliveRequest.Builder reqBr = KeepAliveRequest.newBuilder();
		reqBr.setCmd(BEAT_REQ);
		this.req = reqBr.build();
		
		//响应定义
		KeepAliveResponse.Builder resBr = KeepAliveResponse.newBuilder();
		resBr.setCmd(BEAT_RES);
		this.res = resBr.build();
	}

	public KeepAliveMessageFactoryProtoImpl() {
		this.buildKeepAliveMessage();
		
		req2.put(key, BEAT_REQ);
		res2.put(key, BEAT_RES);
	}

	public KeepAliveMessageFactoryProtoImpl(boolean notSendRequest) {
		this.buildKeepAliveMessage();
		
		req2.put(key, BEAT_REQ);
		res2.put(key, BEAT_RES);
		
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
		
		//GM 后台和gmtool 心跳请求内容
		int serverType = -1;
		Object obj = session.getAttribute(NodeSessionMgr.SERVER_TYPE_KEY);
		if(obj != null) {
			serverType = (Integer)obj;
		}
		if(serverType == NodeSessionMgr.SERVER_TYPE_GM || session.containsAttribute(Response.GMTOOL_KEY)) {
			return req2;
		}
		return req;
	}

	@Override
	public Object getResponse(IoSession session, Object request) {
		if (request instanceof Map) {
			return res2;
		}
		return res;
	}

	@Override
	public boolean isRequest(IoSession session, Object message) {
		if (message instanceof KeepAliveRequest) {
			return true;
		}else if (message instanceof Map) {
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
		if (message instanceof KeepAliveResponse) {
			return true;
		}else if (message instanceof Map) {
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

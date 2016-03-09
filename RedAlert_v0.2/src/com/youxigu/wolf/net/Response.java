package com.youxigu.wolf.net;

import java.net.InetSocketAddress;

import org.apache.mina.core.session.IoSession;

/**
 * IoSession的包装类
 * @author Administrator
 *
 */
public final class Response {
	public static String CREDIT = "credit";// session 是否已经login
	
	public static String GMTOOL_KEY = "gmtool";//这个标志用在flax编写的gmtool工具建立的iosession，心跳的时候要按这个表示区分protobuf和map
	//public static String KEY="RESPONSE";
	// public static final String SESSION_TYPE_KEY = "SESSION_TYPE"; //
	// session是Node,还是flex客户端的
	// public static final int SESSION_TYPE_USER = 0;
	// public static final int SESSION_TYPE_GAMESERVER = 1;
	// public static final int SESSION_TYPE_OTHERSERVER = 2;
	private IoSession session;

	public Response(){
		
	}
	public Response(IoSession session) {
		this.session = session;
	}

	public void write(Object message) {
		// if (message != null) {
		if (this.session == null) {
			throw new RuntimeException("请先建立连接");
		}
		// if (!session.isConnected() || session.isClosing()) {
		// throw new RuntimeException("连接已经关闭，请重新建立连接");
		// }

		this.session.write(message);
		// }
	}

	public Object get(Object key) {
		return session.getAttribute(key);
	}

	public Object put(Object key, Object value) {
		return session.setAttribute(key, value);
	}
	
	public Object remove(Object key) {
		return session.removeAttribute(key);
	}
	

	public IoSession getSession() {
		return this.session;
	}

	/**
	 * 判断session是否有效
	 * 
	 * 登陆后会在session中保存
	 * 
	 * @return
	 */
	public boolean validateSession() {
		UserSession us = (UserSession) session
				.getAttribute(UserSession.KEY_USERSESSION);
		if (us != null)
			return true;
		else
			return false;
	}

	/*
	 * 判断是nodeserver还是客户端连接
	 */
	public boolean isNodeServerSession() {
		// 标志为gameserver
		Integer flag = (Integer) session
				.getAttribute(NodeSessionMgr.SERVER_TYPE_KEY);
		if (flag != null) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isGameNodeServerSession() {
		// 标志为gameserver
		Integer flag = (Integer) session
				.getAttribute(NodeSessionMgr.SERVER_TYPE_KEY);
		if (flag != null && flag.intValue() == NodeSessionMgr.SERVER_TYPE_NODE) {
			return true;
		} else {
			return false;
		}
	}
	
	public String getRemoteIp() {
		return ((InetSocketAddress) session.getRemoteAddress()).getAddress()
				.getHostAddress();
	}
	public void setSession(IoSession session) {
		this.session = session;
	}
	
}

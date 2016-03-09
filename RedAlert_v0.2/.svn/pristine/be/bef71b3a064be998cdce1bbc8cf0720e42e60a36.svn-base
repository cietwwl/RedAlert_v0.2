package com.youxigu.wolf.net;

/**
 * 通常是客户端使用
 * 自动重新连接server
 * 
 * @author Administrator
 * 
 */
public class Reconnect implements ISessionListener {

	/**
	 * 发现连接关闭，自动进行重连
	 */
	public void close(Response response) {
		SessionDaemon.startReconnect((WolfClient) response.getSession()
				.getAttribute("wolfClient"));
	}

	@Override
	public void open(Response response) {

	}

}

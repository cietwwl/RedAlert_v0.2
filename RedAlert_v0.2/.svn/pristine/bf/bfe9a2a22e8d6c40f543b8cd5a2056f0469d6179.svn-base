package com.youxigu.dynasty2.common.service;

import java.util.Map;

import org.apache.mina.core.session.IoSession;

public interface IServerIdSessionCach {
	/**
	 * 注册到世界服
	 * @param address
	 * @param areaId
	 */
	void registerSession(String address, String areaId);

	/**
	 * 取得世界服所有的注册服
	 * @return
	 */
	Map<String, String> getServerIdSessionNameMap();

	/**
	 * 取得IOsession
	 * @param serverId
	 * @return
	 */
	IoSession getSessionByServerId(String serverId);

}

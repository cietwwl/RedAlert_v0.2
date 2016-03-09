package com.youxigu.dynasty2.common.service.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.session.IoSession;

import com.youxigu.dynasty2.common.service.IServerIdSessionCach;
import com.youxigu.dynasty2.core.wolf.WolfClientService;
import com.youxigu.dynasty2.openapi.Constant;
import com.youxigu.wolf.net.NodeSessionMgr;
import com.youxigu.wolf.net.WolfClient;

/**
 * 世界服客户端
 * @author Dagangzi
 *
 */
public class AllServerClientService extends WolfClientService implements IServerIdSessionCach {

	public static final String ALLSERVER_NODE_KEY = "client_all";
	//世界服记录-大区id和网路地址映射
	protected Map<String, String> serverIdSessionNameMap = new ConcurrentHashMap<String, String>();

	@Override
	public void registerSession(String address, String areaIds) {
		IoSession session = NodeSessionMgr.getIoSession(address);
		if (session == null) {
			log.error("注册的session不存在:{}", address);
		} else {
			// sessionNameServerIdMap.put(address, areaId);
			String[] array = areaIds.trim().split(",");
			if (array != null && array.length > 0) {
				for (String _areaId : array) {
					serverIdSessionNameMap.put(_areaId, address);

					log.info("有all server client注册到全区服务器,{}={}", _areaId, address);
				}
			}

			// log.info("当前已经注册的跨服战server:");
			// for (Map.Entry<String, String> entry : serverIdSessionNameMap
			// .entrySet()) {
			// log.info("{}={}", entry.getKey(), entry.getValue());
			// }
			// log.info("sessionManager已经注册的跨服战server:");
			// for (IoSession tmp : NodeSessionMgr.getAllSessions()) {
			// Object obj = tmp
			// .getAttribute(interServerClientService.INTER_WAR_NODE_KEY);
			// if (obj != null) {
			// log.info("{}={}", obj, tmp.getRemoteAddress().toString());
			// }
			// }
		}
	}

	@Override
	public Object registerNode(Map<String, Object> params) {
		String serverTypeTmp = System.getProperty(NodeSessionMgr.SERVER_TYPE_KEY);
		int serverType = 0;
		if (serverTypeTmp != null) {
			serverType = Integer.parseInt(serverTypeTmp);
		}
		if (serverType == NodeSessionMgr.SERVER_TYPE_MAIN) {
			// 表示大区Id--用于socket回调某个服的service
			// 用分大区id映射
			String[] array = Constant.ALL_AREA_ID.trim().split(",");
			if (array != null && array.length > 0) {
				for (String _areaId : array) {
					params.put(_areaId, _areaId);
				}
			}
		}

		String addressKey = (String) super.registerNode(params);

		WolfClient wolfClient = getWolfClient();
		String localAddress = wolfClient.getLocalAddress();
		if (!localAddress.equals(addressKey)) {
			localAddress = addressKey;
		}

		if (serverType == NodeSessionMgr.SERVER_TYPE_MAIN) {
			sendTask(void.class, "activityServerClient", "registerSession", new Object[] { localAddress,
					Constant.ALL_AREA_ID });
			log.info("注册allServer client:{}={}", Constant.ALL_AREA_ID, localAddress);

		}

		return addressKey;
	}

	@Override
	public Map<String, String> getServerIdSessionNameMap() {
		return serverIdSessionNameMap;
	}

	@Override
	public IoSession getSessionByServerId(String serverId) {
		Map<String, String> serverIdSessionNameMap = this.getServerIdSessionNameMap();
		String address = serverIdSessionNameMap.get(serverId);
		IoSession session = null;
		if (address != null) {
			session = NodeSessionMgr.getIoSession(address);
		}

		if (session == null) {
			for (IoSession tmp : NodeSessionMgr.getAllSessions()) {
				Object obj = tmp.getAttribute(serverId);
				if (obj != null) {
					if (serverId.equals(obj)) {
						session = tmp;
						// address = tmp.getRemoteAddress().toString();
						// serverIdSessionNameMap.put(serverId, address);
						break;
					}
				}
			}
		}
		if (session == null) {
			log.error("没有找到server={}对应的session连接", serverId);
			log.info("sessionManager已经注册的全服战server:");
			for (IoSession tmp : NodeSessionMgr.getAllSessions()) {
				Object obj = tmp.getAttribute(serverId);
				if (obj != null) {
					log.info("{}={}", obj, tmp.getRemoteAddress().toString());
				}
			}
		}
		return session;
	}

}

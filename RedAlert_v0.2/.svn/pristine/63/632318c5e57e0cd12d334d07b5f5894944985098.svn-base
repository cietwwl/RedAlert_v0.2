package com.youxigu.dynasty2.common.service.impl;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.mina.core.session.IoSession;
import org.codehaus.jackson.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.ibatis.sqlmap.engine.cache.memcached.broadcast.IBroadcastProducer;
import com.manu.util.Util;
import com.youxigu.dynasty2.chat.ChatAction;
import com.youxigu.dynasty2.common.dao.ISysParaDao;
import com.youxigu.dynasty2.common.domain.Enumer;
import com.youxigu.dynasty2.common.domain.ServerInfo;
import com.youxigu.dynasty2.common.domain.SysPara;
import com.youxigu.dynasty2.common.service.ICommonService;
import com.youxigu.dynasty2.common.service.ILogOutService;
import com.youxigu.dynasty2.common.service.OpenServerListener;
import com.youxigu.dynasty2.core.JSONUtil;
import com.youxigu.dynasty2.openapi.Constant;
import com.youxigu.dynasty2.user.domain.UserChat;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.dynasty2.util.CompressUtils;
import com.youxigu.wolf.net.AsyncWolfTask;
import com.youxigu.wolf.net.ISessionListener;
import com.youxigu.wolf.net.NodeSessionMgr;
import com.youxigu.wolf.net.Response;
import com.youxigu.wolf.net.UserSession;

public class CommonService implements ICommonService, ApplicationContextAware,
		ISessionListener {
	public static final Logger log = LoggerFactory
			.getLogger(CommonService.class);

	// 编码方式
	private static final String CONTENT_CHARSET = "UTF-8";

	// 连接超时时间
	private static final int CONNECTION_TIMEOUT = 3000;

	// 读数据超时时间
	private static final int READ_DATA_TIMEOUT = 3000;

	private Map<String, Enumer> enumCache = new HashMap<String, Enumer>();
	private Map<String, List<Enumer>> enumGroupCache = new HashMap<String, List<Enumer>>();

	private Map<String, SysPara> sysParaCache = new ConcurrentHashMap<String, SysPara>();

	private Map<Integer, int[]> weightMaps = new HashMap<Integer, int[]>();
	private Map<Integer, int[]> valueMaps = new HashMap<Integer, int[]>();

	private String omsUrl = "http://192.168.3.81:8023/gameoms/";
	/**
	 * 默认的serverinfo
	 */
	private ServerInfo serverInfo = null;

	/**
	 * 合并服务器后一个区会有多个区Id与区名称，这里保存不同的区名称，用于显示
	 */
	private Map<String, ServerInfo> serverInfoMaps = new ConcurrentHashMap<String, ServerInfo>();

	private ISysParaDao sysParaDao = null;

	private IBroadcastProducer broadcastMgr;

	private List<OpenServerListener> openServerListeners;

	private ILogOutService logOutService;

	public void setLogOutService(ILogOutService logOutService) {
		this.logOutService = logOutService;
	}

	public void setOmsUrl(String omsUrl) {
		this.omsUrl = omsUrl;
	}

	public void setBroadcastMgr(IBroadcastProducer broadcastMgr) {
		this.broadcastMgr = broadcastMgr;
	}

	public void setSysParaDao(ISysParaDao sysParaDao) {
		this.sysParaDao = sysParaDao;
	}

	public void init() {
		log.info("加载系统参数.........");

		// 服务器开服设置表
		List<ServerInfo> serverInfos = sysParaDao.getServerInfos();
		if (serverInfos == null || serverInfos.size() == 0) {
			serverInfo = new ServerInfo(Constant.AREA_ID, new Timestamp(
					System.currentTimeMillis()));
			sysParaDao.insertServerInfo(serverInfo);
			serverInfoMaps.put(serverInfo.getServerId(), serverInfo);
		} else {
			StringBuffer allAreaIds = new StringBuffer();// 所有分大区id
			for (ServerInfo s : serverInfos) {
				String serverId = s.getServerId();
				serverInfoMaps.put(serverId, s);
				if (Constant.AREA_ID.equals(serverId)) {
					serverInfo = s;
				}
				if (allAreaIds.length() > 0) {
					allAreaIds.append(",");
				}
				allAreaIds.append(serverId);
			}
			Constant.ALL_AREA_ID = allAreaIds.toString();

			if (serverInfo == null) {
				throw new BaseException("serverinfo 中没有本区默认的serverId");
			}
		}

		// 配置系统变量
		List<SysPara> sysParaList = sysParaDao.getAllSysPara();
		for (SysPara sysPara : sysParaList) {
			sysParaCache.put(sysPara.getParaId(), sysPara);
		}

		// 运营更新的系统参数
		List<SysPara> sysParaList_gm = sysParaDao.getAllSysPara_gm();
		for (SysPara sysPara : sysParaList_gm) {
			SysPara stand = sysParaCache.get(sysPara.getParaId());
			if (stand == null) {
				log.error("运营活动参数中存在异常的系统参数:{}", sysPara.getParaId());
				sysParaCache.put(sysPara.getParaId(), sysPara);
				continue;
			}
			boolean same = true;
			if (stand.getParaValue() == null) {
				if (sysPara.getParaValue() != null) {
					same = false;
				}
			} else {
				if (!stand.getParaValue().equals(sysPara.getParaValue())) {
					same = false;
				}
			}
			if (!same) {
				log.warn("运营活动参数与默认系统参数不一致,采用运营参数:{}={}", sysPara.getParaId(),
						sysPara.getParaValue());
			} else {
				log.info("运营活动参数与默认系统参数一致:{}={},删除运营参数", sysPara.getParaId(),
						sysPara.getParaValue());
				sysParaDao.deleteSysPara_gm(sysPara.getParaId());
			}
			sysParaCache.put(sysPara.getParaId(), sysPara);
		}

		// 系统常量加载
		initEnumers();

		// 系统权重参数加载
		initWeightValues();

		log.info("加载系统参数 完成..........");

	}

	/*
	 * 系统权重参数加载
	 */
	public void initWeightValues() {
		List<Map<String, Object>> confs = sysParaDao.getWeightValueConf();

		for (Map<String, Object> conf : confs) {
			int[] weights = new int[11];
			int[] values = new int[10];
			Integer key = (Integer) conf.get("id");
			int sumWeight = 0;
			for (int i = 0; i < 10; i++) {
				weights[i] = (Integer) conf.get("weight" + i);
				if (weights[i] < 0) {
					throw new BaseException("权重配置不应该是负数");
				}
				sumWeight = sumWeight + weights[i];
				values[i] = (Integer) conf.get("value" + i);
			}
			if (sumWeight <= 0) {
				throw new BaseException("权重配数错误，权重总数<=0");
			}
			weights[10] = sumWeight;

			weightMaps.put(key, weights);
			valueMaps.put(key, values);
		}

	}

	/**
	 * 系统常量加载
	 */
	public void initEnumers() {
		List<Enumer> enumers = sysParaDao.listAllEnumer();
		for (Enumer enumer : enumers) {
			enumCache.put(enumer.getEnumId().trim(), enumer);
			List<Enumer> enumerList = enumGroupCache.get(enumer.getEnumGroup()
					.trim());
			if (enumerList == null) {
				enumerList = new ArrayList<Enumer>();
				enumGroupCache.put(enumer.getEnumGroup(), enumerList);
			}
			enumerList.add(enumer);
		}
	}

	@Override
	public String getSysParaValue(String paraId) {
		return getSysParaValue(paraId, null);
	}

	@Override
	public String getSysParaValue(String paraId, String defaultValue) {
		SysPara para = sysParaCache.get(paraId);
		if (para == null) {
			para = sysParaDao.getSysParaById_gm(paraId);
			if (para == null) {
				para = sysParaDao.getSysParaById(paraId);
			}
			if (para != null) {
				sysParaCache.put(paraId, para);
				return para.getParaValue();
			}
		} else {
			return para.getParaValue();
		}
		return defaultValue;
	}

	public int getSysParaIntValue(String paraId, int defaultValue) {
		SysPara para = sysParaCache.get(paraId);
		if (para == null) {
			para = sysParaDao.getSysParaById_gm(paraId);
			if (para == null) {
				para = sysParaDao.getSysParaById(paraId);
			}
			if (para != null) {
				sysParaCache.put(paraId, para);
				return para.getIntValue();
			}
		} else {
			return para.getIntValue();
		}
		return defaultValue;
	}

	public Timestamp getSysParaTimestampValue(String paraId) {
		SysPara para = sysParaCache.get(paraId);
		if (para == null) {
			para = sysParaDao.getSysParaById_gm(paraId);
			if (para == null) {
				para = sysParaDao.getSysParaById(paraId);
			}
			if (para != null) {
				sysParaCache.put(paraId, para);
				return para.getTimestampValue();
			}
		} else {
			return para.getTimestampValue();
		}
		return null;
	}

	@Override
	public void updateSysPara(String paraId, String value) {
		SysPara para = sysParaCache.get(paraId);
		if (para != null) {
			try {
				para.setParaValue(value);
				sysParaDao.insertSysPara_gm(para);
				// 不修改策划配置的参数
				// sysParaDao.updateSysPara(para);
				if (broadcastMgr != null) {
					broadcastMgr.sendNotification(new AsyncWolfTask(
							"commonService", "resetSysPara", new Object[] {
									paraId, value }));
				}
			} catch (Exception e) {
				sysParaCache.remove(paraId);
				throw new BaseException(e);
			}
		} else {
			throw new BaseException("系统参数不存在:" + paraId);
		}

	}

	@Override
	public void resetSysPara(String paraId, String value) {
		SysPara para = sysParaCache.get(paraId);
		if (para != null) {
			para.setParaValue(value);
			if (log.isDebugEnabled()) {
				log.debug("更新系统参数:paraId={},value={}", paraId, value);
			}
		}

	}

	public Enumer getEnumById(String enumId) {
		return enumCache.get(enumId);
	}

	public int getEnumIntValueById(String enumId, int defaultValue) {
		Enumer enumer = enumCache.get(enumId);
		if (enumer == null) {
			// log.warn("Enumer配置错误：不存在 " + enumId);
			return defaultValue;
		}
		return enumer.getIntValue();
	}

	public List<Enumer> getEnumsByGroup(String enumGroup) {
		return enumGroupCache.get(enumGroup);
	}

	@Override
	public int getRandomWeightValueById(int id) {
		int[] weights = weightMaps.get(id);
		int[] values = valueMaps.get(id);
		if (weights == null) {
			throw new BaseException("指定的权重配置ID不存在：" + id);
		}
		int percent = Util.randInt(weights[10]) + 1;

		int i = 0;
		percent = percent - weights[i];
		while (percent > 0) {// weights[i]是策划配数，不可能是负数
			i++;
			percent = percent - weights[i];
		}
		return values[i];
	}

	@Override
	public ServerInfo getServerInfo() {
		return serverInfo;
	}

	/**
	 * 取得所有运营端配置的参数
	 * 
	 * @return
	 */
	@Override
	public List<SysPara> getSysParaGMs() {
		return sysParaDao.getAllSysPara_gm();
	}

	@Override
	public void setServerInfoTime(long time) {
		ServerInfo serverInfo = this.getServerInfo();
		serverInfo.setDttm(new Timestamp(time));
		sysParaDao.updateServerInfo(serverInfo);
		if (broadcastMgr != null) {
			broadcastMgr.sendNotification(new AsyncWolfTask("commonService",
					"reloadServerInfoTime", new Object[] { time }));
		}
		for (OpenServerListener listener : openServerListeners) {
			listener.notice(serverInfo);
		}
		log.info("服务器开服时间被修改...." + serverInfo.getDttm());
	}

	public void reloadServerInfoTime(long time) {
		ServerInfo serverInfo = this.getServerInfo();
		serverInfo.setDttm(new Timestamp(time));
		for (OpenServerListener listener : openServerListeners) {
			listener.notice(serverInfo);
		}
		log.info("服务器开服时间被修改...." + serverInfo.getDttm());
	}

	/**
	 * 从运营管理系统抓取大区名称与跨服战IP:port
	 */
	@Override
	public void syncServerInfo() {
		log.info("执行同步服务器信息Job.....");
		if (omsUrl != null && omsUrl.length() > 0) {
			if (serverInfoMaps != null && serverInfoMaps.size() > 0) {
				StringBuilder sb = new StringBuilder(omsUrl);
				sb.append("?serverIds=");
				for (String serverId : serverInfoMaps.keySet()) {
					sb.append(serverId).append(",");
				}

				boolean update = false;
				try {
					String result = httpGet(sb.toString());

					JsonNode jsonObj = JSONUtil.getJsonNode(result);

					int ret = jsonObj.path("ret").getIntValue();
					if (ret != 0) {
						log.error("同步服务器数据出错,错误码:" + ret + ","
								+ jsonObj.path("msg").getTextValue());
						// throw new BaseException("获取帐户余额失败:" + ret);
					} else {
						JsonNode node = jsonObj.path("servers");
						Iterator<JsonNode> lit = node.getElements();
						while (lit.hasNext()) {
							JsonNode one = lit.next();
							String serverId = one.path("id").getTextValue();
							String serverName = one.path("name").getTextValue();
							String interServer2 = one.path("interServer")
									.getTextValue();

							ServerInfo serverInfo = serverInfoMaps
									.get(serverId);
							if (serverInfo == null) {
								continue;
							}
							if (!serverName.equals(serverInfo.getServerId())) {
								serverInfo.setServerName(serverName);
								update = true;
							}
							if (!interServer2.equals(serverInfo
									.getInterServer2())) {
								serverInfo.setInterServer2(interServer2);
								update = true;
							}

							if (update) {
								sysParaDao.updateServerInfo(serverInfo);
							}
						}
					}

					if (update && broadcastMgr != null) {
						broadcastMgr.sendNotification(new AsyncWolfTask(
								"commonService", "reloadServerInfo",
								(Object[]) null));

					}

				} catch (Exception e) {
					log.error("同步服务器数据出错", e);
				}
			}

		}
		log.info("执行同步服务器信息Job end.....");

	}

	private String httpGet(String url) {
		HttpClient httpClient = new HttpClient();

		// 设置建立连接超时时间
		httpClient.getHttpConnectionManager().getParams()
				.setConnectionTimeout(CONNECTION_TIMEOUT);

		// 设置读数据超时时间
		httpClient.getHttpConnectionManager().getParams()
				.setSoTimeout(READ_DATA_TIMEOUT);

		GetMethod getMethod = new GetMethod(url);

		// 设置User-Agent
		getMethod.setRequestHeader("User-Agent", "Java MDK Client");

		// 设置编码
		getMethod.getParams().setParameter(
				HttpMethodParams.HTTP_CONTENT_CHARSET, CONTENT_CHARSET);

		// 使用系统提供的默认的恢复策略
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());

		try {

			int statusCode = httpClient.executeMethod(getMethod);

			if (statusCode != HttpStatus.SC_OK) {
				throw new BaseException("Request [" + url + "] failed:"
						+ getMethod.getStatusLine());
			}
			// 读取内容
			String result = getMethod.getResponseBodyAsString();
			if (result != null && result.length() > 0) {
				if (log.isDebugEnabled()) {
					log.debug("return={},url={}", result, url);
				}
			}
			return result;

		} catch (HttpException e) {
			log.error("HttpException", e);
			// 发生致命的异常，可能是协议不对或者返回的内容有问题
			throw new BaseException("Request [" + url + "] failed:"
					+ e.getMessage());
		} catch (IOException e) {
			log.error("IOException", e);
			// 发生网络异常
			throw new BaseException("Request [" + url + "] failed:"
					+ e.getMessage());
		} finally {
			// 释放链接
			getMethod.releaseConnection();
		}
	}

	@Override
	public void reloadServerInfo() {
		// 重新加载数据库中的serverinfo
		List<ServerInfo> serverInfos = sysParaDao.getServerInfos();
		if (serverInfos != null) {
			for (ServerInfo s : serverInfos) {
				String serverId = s.getServerId();
				serverInfoMaps.put(serverId, s);
				if (Constant.AREA_ID.equals(serverId)) {
					serverInfo = s;
				}
			}
		}

	}

	@Override
	public void setApplicationContext(ApplicationContext ctx)
			throws BeansException {
		if (openServerListeners == null) {
			openServerListeners = new ArrayList<OpenServerListener>();

			while (ctx != null) {
				Map<String, OpenServerListener> maps = ctx
						.getBeansOfType(OpenServerListener.class);
				if (maps.values() != null && maps.values().size() > 0) {
					openServerListeners.addAll(maps.values());
				}
				ctx = ctx.getParent();

			}
		}

	}

	@Override
	public void open(Response response) {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unchecked")
	@Override
	public void close(Response response) {
		String val = System.getProperty(NodeSessionMgr.SERVER_TYPE_KEY);
		if (!(NodeSessionMgr.SERVER_TYPE_MAIN + "").equals(val)) {
			return;
		}
		// 退出时保留快捷聊天记录、频道设置
		IoSession ioSession = response.getSession();
		UserSession us = (UserSession) ioSession
				.getAttribute(UserSession.KEY_USERSESSION);
		if (us != null && us.getUserId() != 0) {
			UserChat userChat = null;
			byte[] hisRecord = null;
			String channelConfig = ChatAction.CHANNEL_CONFIG;
			Object obj = us.removeAttribute("HISRECORD");
			boolean update = false;

			if (obj != null) {
				hisRecord = CompressUtils
						.serializeAndCompress((List<String>) obj);
				update = true;
			}

			obj = us.removeAttribute("CHANNEL_CONFIG");
			if (obj != null) {
				channelConfig = (String) obj;
				update = true;
			}

			byte[] recentTimeFriend = null;
			obj = us.removeAttribute(UserSession.KEY_RECENT_TIME_FRIEND);
			if (obj != null) {
				recentTimeFriend = CompressUtils.serializeAndCompress(obj);
				update = true;
			}
			int statu = 0;
			obj = us.removeAttribute(UserSession.KEY_CHAT_MSG);
			if (obj != null) {
				statu = Integer.valueOf(obj + "");
				update = true;
			}

			if (update) {
				userChat = new UserChat(us.getUserId(), channelConfig,
						hisRecord, recentTimeFriend, statu);
				logOutService.insertUserChat(userChat);
			}
		}

	}
}

package com.youxigu.dynasty2.openapi.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qq.opensns.datacollector.DatacollectorClient;
import com.qq.opensns.datacollector.LogType;
import com.youxigu.dynasty2.core.flex.ActionDefine;
import com.youxigu.dynasty2.core.flex.amf.IProtoReportService;
import com.youxigu.dynasty2.openapi.Constant;
import com.youxigu.dynasty2.user.domain.Account;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.wolf.net.UserSession;

public class ProtoReportService implements IProtoReportService {
	public static final Logger log = LoggerFactory
			.getLogger(ProtoReportService.class);
	private String appName = "appoperlog"; // 必须是这个固定值
	/**
	 * 游戏应用自 身的版本号（游戏身的版本号）
	 */
	private String appVersion = "1.0";
	/**
	 * 应用（游戏）中开发方自身 协议版本号
	 */
	private String msgVersion = "1.0";

	/**
	 * 腾讯侧的接口版本号
	 */
	private String version = "1.6.6";

	private int start = 1;

	private DatacollectorClient client;

	public void setVersion(String version) {
		this.version = version;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public void setMsgVersion(String msgVersion) {
		this.msgVersion = msgVersion;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public void init() {
		if (Constant.isTestEnv()) {
			start = 0;
		}
		if (start != 1) {
			log.warn("禁止协议上报服务");
			return;
		}
		try {
			client = DatacollectorClient.init(appName);
			log.info("启动协议上报完毕....");
		} catch (Exception e) {
			log.error("启动协议上报服务失败", e);
			start = 0;
		}
	}

	@Override
	public void report(ActionDefine ad, UserSession session, boolean fail,
			Map<String, Object> params) {
		if (start == 0) {
			return;
		}
		String data = null;
		try {
			StringBuilder sb = makeMagHeader(ProtoActionID.ACTIONID_RPGPROTORP,
					session);
			sb.append("&RID=").append(session.getUserId());
			sb.append("&PID=").append(ad.getCmd());
			sb.append("&FID=").append(ad.getBean().getClass().getSimpleName())
					.append(".").append(ad.getMethodName());
			sb.append("&PTP=").append(ad.getAuto());
			sb.append("&RST=").append(fail ? "3" : "0");
			data = sb.toString();
			if (log.isDebugEnabled()) {
				log.debug("协议上传：{}", data);
			}

			client.writeBaseLog(LogType.LT_SECDATA, data, false);
		} catch (Exception e) {
			log.error("协议上传：{}", data);
			log.error("协议上传失败", e);
		}
	}

	private StringBuilder makeMagHeader(String actionId, UserSession session) {
		StringBuilder sb = new StringBuilder(1024);
		sb.append("APPV=").append(appVersion);
		sb.append("&MSGV=").append(msgVersion);
		sb.append("&VER=").append(version);
		sb.append("&APPID=").append(Constant.APP_ID_QQ);
		sb.append("&OID=").append(session.getOpenid());
		sb.append("&WID=").append(session.getAreaId());
		sb.append("&UIP=").append(session.getUserip());
		sb.append("&OKY=").append(session.getOpenkey());
		sb.append("&SIP=");
		sb.append("&MTM=").append(System.currentTimeMillis());
		sb.append("&DOM=").append(Constant.getPFIntValue(session.getPf()));
		sb.append("&MLV=0");
		sb.append("&AID=").append(actionId);
		return sb;
	}

	@Override
	public void login(UserSession session, Account account, User user,
			Map<String, Object> params) {
		if (start == 0) {
			return;
		}

		String data = null;
		try {
			StringBuilder sb = makeMagHeader(ProtoActionID.ACTIONID_LOGIN,
					session);
			sb.append("&ACT=").append(account.getCreateDttm().getTime());

			if (user == null) {
				sb.append("&PAY=");
				sb.append("&RID=0");
				sb.append("&RNA=0");
				sb.append("&RLV=0");
			} else {
				sb.append("&PAY=").append(user.getConsumeCash() == 0 ? 0 : 1);
				sb.append("&RID=").append(user.getUserId());
				sb.append("&RNA=").append(client.encode(user.getUserName()));
				sb.append("&RLV=").append(user.getUsrLv());
				sb.append("&EXP=").append(user.getHonor());

			}
			sb.append("&RTN=0");
			// sb.append("&RTI=");
			// sb.append("&RTP=")
			sb.append("&GTN=1");
			sb.append("&GTI=点券");
			if (user == null) {
				sb.append("&GOD=0");
			} else {
				sb.append("&GOD=").append(user.getPayPoint());
			}
			sb.append("&CTN=0");
			// sb.append("&CTI=");
			// sb.append("&CAS=")
			if (user == null) {
				sb.append("&RCT=0");
			} else {
				sb.append("&RCT=").append(user.getCreateDate().getTime());
			}
			data = sb.toString();
			if (log.isDebugEnabled()) {
				log.debug("协议上传：{}", data);
			}

			client.writeBaseLog(LogType.LT_SECDATA, data, false);
		} catch (Exception e) {
			log.error("协议上传：{}", data);
			log.error("协议上传失败", e);
		}
	}

}

package com.youxigu.dynasty2.openapi.service.impl;

import java.util.Date;
import java.util.Map;

import com.youxigu.dynasty2.core.flex.ActionDefine;
import com.youxigu.dynasty2.core.flex.amf.IProtoReportService;
import com.youxigu.dynasty2.log.AbsLogLineBuffer;
import com.youxigu.dynasty2.log.ILogService;
import com.youxigu.dynasty2.log.LogTypeConst;
import com.youxigu.dynasty2.openapi.Constant;
import com.youxigu.dynasty2.user.domain.Account;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.wolf.net.UserSession;

/**
 * 文件名 TlogSecReportService.java
 * 
 * 描 述 tlog 安全日志 全去走tlog线程吧，
 * 
 * 时 间 2014-12-1
 * 
 * 作 者 huhaiquan
 * 
 * 版 本 v1.4
 */
public class TlogSecReportService implements IProtoReportService {
	private ILogService tlogService;

	// <struct name="SecOperationFlow" filter="1" version="1" desc="操作流水上报日志">
	// <entry name="GameSvrId" type="string" size="25" desc="登录的游戏服务端编号" />
	// <entry name="dtEventTime" type="datetime" desc="游戏事件的时间, 格式 YYYY-MM-DD
	// HH:MM:SS" />
	// <entry name="GameAppID" type="string" size="32" desc="游戏APPID" />
	// <entry name="OpenID" type="string" size="64" desc="用户OPENID号" />
	// <entry name="PlatID" desc="ios 0 /android 1" type="int"
	// defaultvalue="0"/>
	// <entry name="AreaID" desc="微信 0 /手Q 1" type="int" defaultvalue="0"/>
	// <entry name="ZoneID" desc="小区号id" type="int" defaultvalue="0"/>
	// <entry name="BattleID" type="uint"
	// desc="操作事件号（针对玩家个人记录，登录后从0开始累积，每操作一次+1，下线后清空）" />
	// <!--iBattleID的值，等于SecRoundStartFlow的dtEventTime字段的整型，例如 2013-12-26
	// 22:31:18
	// 的iBattleID为 1388068278-->
	// <entry name="ClientStartTime" type="datetime" desc="客户端本地时间，格式 YYYY-MM-DD
	// HH:MM:SS" />
	// <entry name="ClientVersion" type="string" size="64" desc="客户端版本号"/>
	// <entry name="UserName" type="string" size="64"
	// desc="玩家昵称，如果昵称中带有符号|，则记录时去掉|，比如 张|三 记为 张三"/>
	// <entry name="SvrUserMoney1" type="int" desc="游戏币数量" />
	// <entry name="SvrUserMoney2" type="int" desc="钻石数量" />
	// <!--操作上报数据-->
	// <!--（svr每响应1次客户端操作，就打印一条操作流水上报日志，记录下这次操作的OperationID）-->
	// <!--（如果是客户端的自动挂，操作行为也要上报）-->
	// <entry name="OperationID" type="int"
	// desc="操作类型id（如果是客户端的自动挂，操作行为也要上报），eg：101" />
	// <entry name="OperationDesc" type="string" size="64"
	// desc="操作类型英文简单描述，eg：action.product" />
	// <entry name="OperationType" type="int"
	// desc="操作来源，0为玩家操作，1为系统自动操作，2为客户端心跳包"
	// />
	// <entry name="OperationResult" type="int" desc="操作结果，0为成功，1为失败" />
	// </struct>

	@Override
	public void login(UserSession session, Account account, User user,
			Map<String, Object> params) {
//		session.getOperateCount().set(0);
//		AbsLogLineBuffer lb = getTlogHead(session);
//		lb.append(0);
//		int cash = 0;
//		if (user != null) {
//			cash = user.getCash();
//		}
//		lb.append(cash).append(10001).append("login").append(0).append(0);
//		tlogService.log(lb);
	}

	@Override
	public void report(ActionDefine action, UserSession session, boolean fail,
			Map<String, Object> params) {
//		AbsLogLineBuffer lb = getTlogHead(session);
//		lb.append(0);
//		lb.append(0).append(action.getCmd()).append("0").append(0).append(
//				fail ? 1 : 0);
//		tlogService.log(lb);
	}

	public void setTlogService(ILogService tlogService) {
		this.tlogService = tlogService;
	}

	public final static AbsLogLineBuffer getTlogHead(UserSession us) {
		Date now = new Date();
		String openId = null;
		String pf = null;
		String ip = null;
		String via = null;
		String areaId = null;
		// String appid = null;
		String head = "";
		int dType = 0;
		int oper = 0;
		String cv = "";
		String userName = "";
		if (us != null) {
			userName = us.getUserName();
			oper = us.getOperateCount().incrementAndGet();
			cv = us.getMobileClient().vClientVersion;
			openId = us.getOpenid();
			pf = us.getPfEx();
			ip = us.getUserip();
			via = us.getVia();
			areaId = us.getAreaId();
			if (us.getdType() == Constant.DEVIDE_TYPE_IOS) {
				dType = 0;
			} else {
				dType = 1;
			}
			if (us.isVistor()) {
				head = "G_";
			}
		}
		// 微信 0 /手Q 1
		int ptype = 0;
		if (Constant.PLATFORM_TYPE == 1) {
			ptype = 1;
		}

		// <entry name="GameSvrId" type="string" size="25" desc="登录的游戏服务端编号" />
		// <entry name="dtEventTime" type="datetime" desc="游戏事件的时间, 格式
		// YYYY-MM-DD
		// HH:MM:SS" />
		// <entry name="GameAppID" type="string" size="32" desc="游戏APPID" />
		// <entry name="OpenID" type="string" size="64" desc="用户OPENID号" />
		// <entry name="PlatID" desc="ios 0 /android 1" type="int"
		// defaultvalue="0"/>
		// <entry name="AreaID" desc="微信 0 /手Q 1" type="int" defaultvalue="0"/>
		// <entry name="ZoneID" desc="小区号id" type="int" defaultvalue="0"/>
		// <entry name="BattleID" type="uint"
		// desc="操作事件号（针对玩家个人记录，登录后从0开始累积，每操作一次+1，下线后清空）" />
		// <!--iBattleID的值，等于SecRoundStartFlow的dtEventTime字段的整型，例如 2013-12-26
		// 22:31:18
		// 的iBattleID为 1388068278-->
		// <entry name="ClientStartTime" type="datetime" desc="客户端本地时间，格式
		// YYYY-MM-DD
		// HH:MM:SS" />
		// <entry name="ClientVersion" type="string" size="64" desc="客户端版本号"/>
		// <entry name="UserName" type="string"
		return AbsLogLineBuffer.getBuffer(areaId, AbsLogLineBuffer.TYPE_TLOG,
				LogTypeConst.TLOG_TYPE_SecOperationFlow)
				.append(Constant.SVR_IP).append(now).appendLegacy(
						head + Constant.getAppId()).append(openId)
				.append(dType).append(ptype).append(Constant.AREA_ID).append(
						oper).append("0").append(cv).appendLegacy(userName);
	}
}

package com.youxigu.wolf.net;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.manu.util.UtilDate;
import com.youxigu.dynasty2.log.AbsLogLineBuffer;
import com.youxigu.dynasty2.log.ILogService;
import com.youxigu.dynasty2.log.LogTypeConst;
import com.youxigu.dynasty2.log.TLogTable;
import com.youxigu.dynasty2.openapi.Constant;
import com.youxigu.dynasty2.user.service.IUserService;

public class OnLineUserReportService {
	public static final Logger log = LoggerFactory
			.getLogger(OnLineUserReportService.class);
	private static boolean isRun = false;
	private static int count = 0;
	private static int maxUserNum;
	private static String worldId = "1";
	private static String channelId = "65535";
	private static DataSource dataSource;
	private ILogService logService;
	private ILogService tlogService;
	private IUserService userService;

	private int prevOnline;// 上次上报的在线数量
	private int prevReg;// 上次上报的注册数量
	private Map<String, Object> logMap = new HashMap<String, Object>();

	public void setTlogService(ILogService tlogService) {
		this.tlogService = tlogService;
	}

	public void setLogService(ILogService logService) {
		this.logService = logService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public void setWorldId(String worldId) {
		OnLineUserReportService.worldId = worldId;
	}

	public void setChannelId(String channelId) {
		OnLineUserReportService.channelId = channelId;
	}

	public void setDataSource(DataSource dataSource) {
		OnLineUserReportService.dataSource = dataSource;
	}

	public void start() {
		// dataSource = (DataSource) ServiceLocator.getSpringBean("dataSource");
		Thread t = new Thread(new Worker(),
				"OnLineUserReportService.onlineReport");
		OnLineUserReportService.isRun = true;
		OnLineUserReportService.count = 1;
		t.start();

	}

	public void stop() {
		OnLineUserReportService.isRun = false;
		synchronized (this) {
			this.notifyAll();
		}
	}

	/**
	 * 上报当前在线人数到腾讯远程数据库
	 * <p>
	 * 字段名 类型 键 描述 dtStatTime datetime PRI 统计时间 iWorldId tinyint(4) unsigned PRI
	 * World 服务器编号 或 Cluster ID 255 iChannelId smallint(6) unsigned PRI
	 * 频道ID，ZoneId， 没有的请填写65535 iUserNum int(11) unsigned 用户数
	 * 
	 * @param nowOnlineNum
	 * @throws Exception
	 * @author FengRui 2010-04-23
	 */
	private static void remoteSend2TencentOnlineNum(int nowOnlineNum, Date date) {
		Connection _conn = null;
		Statement _stmt = null;
		try {
			_conn = dataSource.getConnection();
			_stmt = _conn.createStatement();

			String dtStatTime = UtilDate.datetime2Text(date);
			String sql = "INSERT INTO tbrealonline(dtStatTime, iWorldId, iChannelId, iUserNum) values('"
					+ dtStatTime
					+ "',"
					+ worldId
					+ ", "
					+ channelId
					+ ", "
					+ nowOnlineNum
					+ ") "
					+ "ON DUPLICATE KEY UPDATE iUserNum="
					+ nowOnlineNum;
			_stmt.executeUpdate(sql);
		} catch (SQLException e) {
			log.error("在线人数上报错误", e);
			// e.printStackTrace();
		} finally {
			if (_stmt != null) {
				try {
					_stmt.close();
				} catch (Exception e) {
				}
			}
			if (_conn != null) {
				try {
					_conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static Date getDateTimeMinute5() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.SECOND, 0);
		int min = cal.get(Calendar.MINUTE);
		min = min % 5 == 0 ? min : (min / 5) * 5;
		cal.set(Calendar.MINUTE, min);
		return cal.getTime();
	}

	class Worker implements Runnable {
		public void run() {

			while (isRun) {
				try {
					//在线人数
					int curr = OnlineUserSessionManager
							.getCurrentOnlineUserNum();
					
					int num_andriod = 0;
					int num_ios = 0;

					int prev_num_andriod = 0;
					int prev_num_ios = 0;
					if (Constant.isAndroid()) {
						num_andriod = curr;
						prev_num_andriod = prevOnline;
					} else {
						num_ios = curr;
						prev_num_ios = prevOnline;
					}
					int nowSecond = (int) (System.currentTimeMillis() / 1000);

					// public static final String SQL_INSERT_onlinecnt =
					// "insert into tb_qxsy_onlinecnt
					// (gameappid,timekey,gsid,zoneid,onlinecntios,onlinecntandroid,dttm)
					// values(?,?,?,?,?,?,now())";
					tlogService.logDB(new Object[] {
							LogTypeConst.SQL_INSERT_onlinecnt,
							Constant.getAppId(), nowSecond, Constant.SVR_IP,
							Constant.AREA_ID, num_ios, num_andriod, num_ios,
							prev_num_andriod });
					prevOnline = num_andriod + num_ios;
					//
					// logMap.put("t", value)
					// tlogService.log(obj)
					// if (log.isDebugEnabled()) {
					// log.debug("当前在线人数:" + curr);
					// }
					//
					count++;
					if (count % 5 == 0) {
						//*******************mylog格式************************
						//<struct  name="PlayerOnline"  version="1" desc="(必填)玩家在线人数，每5分钟记录一条日志">
						//		<entry  name="num"		type="int"						defaultvalue="0"	desc="(必填)在线人数"/>
						//		<entry  name="reg"		type="int"						defaultvalue="0"	desc="(必填)5分钟注册数"/>
						//		<entry  name="login"		type="int"						defaultvalue="0"	desc="(必填)5分钟之内登陆数"/>
						//	    <entry  name="dtEventTime"		type="datetime"					desc="(必填) 格式 YYYY-MM-DD HH:MM:30   MM为5的整倍数  SS为30" />
						//		<entry  name="PlatID"			type="int"						defaultvalue="0"	desc="(必填)ios 0 /android 1"/>
						//	    <entry  name="GameSvrId"			type="string"		size="25"	desc="(必填)登录的游戏服务器编号" />
						//	    <entry  name="LoginChannel"		type="int"						defaultvalue="0"		desc="(必填)登录渠道"/>
						//	  </struct>
						
						//新增注册
						int userNum = userService.getCountUsers();
						int newReg = 0;
						if(prevReg > 0) {
							newReg = userNum - prevReg;
						}
						prevReg = userNum;
						
						//新增登陆人数
						int newLoging = OnlineUserSessionManager.getCountLoginnum4Fivemin();
						
						Date now = getDateTimeMinute5();
						logService.log(AbsLogLineBuffer.getBuffer(
								Constant.AREA_ID,
								TLogTable.TLOG_PLAYERONLINE.getName())
								.append(curr).append(newReg).append(newLoging)
								.append(now)
								.append(Constant.DEVIDE_TYPE)
								.append(Constant.SVR_IP)
								.append(0)
								);
						count = 0;
					}
					// remoteSend2TencentOnlineNum(curr, now);
					// count = 0;
					//						
					//

					// if (log.isDebugEnabled()) {
					// // 打印每个server上的人数
					// NodeSessionMgr.showUsersOnGameNode();
					// }
					// }
					// <entry name="dtEventTime" type="datetime" desc="(必填) 格式
					// YYYY-MM-DD HH:MM:SS" />
					// <entry name="vGameIP" type="string" size="32"
					// desc="(必填)服务器IP" />
					tlogService.log(AbsLogLineBuffer.getBuffer("",
							AbsLogLineBuffer.TYPE_TLOG,
							LogTypeConst.TLOG_TYPE_GameSvrState).append(
							new Date()).append(Constant.SVR_IP));
					try {
						synchronized (OnLineUserReportService.this) {
							OnLineUserReportService.this.wait(60000);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	}
}

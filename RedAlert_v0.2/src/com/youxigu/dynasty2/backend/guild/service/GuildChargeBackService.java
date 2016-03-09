package com.youxigu.dynasty2.backend.guild.service;

import java.util.Collection;
import java.util.Iterator;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.youxigu.wolf.net.AsyncWolfTask;
import com.youxigu.wolf.net.NodeSessionMgr;

public class GuildChargeBackService {
	public static final Logger log = LoggerFactory
			.getLogger(GuildChargeBackService.class);
	private boolean shutdown;

	public void startChargeBack() {
		if (!shutdown) {
			Collection<IoSession> sessions = NodeSessionMgr.getNodeSessions();// 初始化客户端tomcat链接
			if (sessions == null || sessions.size() == 0) {
				log.error("没有可用的nodeServer,本日联盟维护费用Job放弃......");
				return;
			}
			log.info("联盟维护费扣费分配Job,begin........");
			Iterator<IoSession> lit = sessions.iterator();
			for (int i = 1; i <= 7; i++) {
				AsyncWolfTask appBaseTask = new AsyncWolfTask("guildService",
						"dailyUpdateGuild", new Object[] { i });
				if (!lit.hasNext()) {
					lit = sessions.iterator();
				}

				if (!NodeSessionMgr.sendMessage(lit.next(), appBaseTask)) {
					log.error("error........");
				}
			}
			log.info("联盟维护费扣费分配Job,end........");
		}
	}

	public void destory() {
		shutdown = true;
	}

	public void waitConn(int second) {
		try {
			Thread.sleep(second * 1000);// 延迟second秒加载
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

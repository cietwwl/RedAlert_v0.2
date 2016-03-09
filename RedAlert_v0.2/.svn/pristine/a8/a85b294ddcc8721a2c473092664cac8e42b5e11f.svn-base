package com.youxigu.dynasty2.core.wolf;

import java.util.Date;
import java.util.Map;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.wolf.net.AsyncWolfTask;
import com.youxigu.wolf.net.NodeSessionMgr;
import com.youxigu.wolf.net.SyncWolfTask;
import com.youxigu.wolf.net.WolfClient;
import com.youxigu.wolf.node.core.NodeRegTask;
import com.youxigu.wolf.node.core.NodeUnRegTask;
import com.youxigu.wolf.node.job.Job;

/**
 * 
 * copy from qixiong1's wolfClientService
 * 
 * @author Administrator
 * 
 */
public class WolfClientService implements IWolfClientService {
	public static final Logger log = LoggerFactory
			.getLogger(WolfClientService.class);

	public static final String JOB_SERVICE = "wolf_jobService";

	private WolfClient wolfClient;

	private String adminUser;
	private String adminPwd;

	private boolean start = false;

	// private ScheduledThreadPoolExecutor exec;

	// 这里可能不用设置是否重新连接,Reconnect+SessionDaemon处理了断线重新连接的问题
	// private boolean autoReconnect = false;
	// private int period = 60;

	// public void setAutoReconnect(boolean autoReconnect) {
	// this.autoReconnect = autoReconnect;
	// }

	// public void setPeriod(int period) {
	// this.period = period;
	// }
	public void setAdminUser(String adminUser) {
		this.adminUser = adminUser;
	}

	public void setAdminPwd(String adminPwd) {
		this.adminPwd = adminPwd;
	}

	public void setWolfClient(WolfClient wolfClient) {
		this.wolfClient = wolfClient;
		if (wolfClient!=null && wolfClient.isConnected())
			start = true;		
	}

	public void init() {
		if (start)
			return;
		if (wolfClient == null) {
			log.error("wolf client is null");
			return;
		}

		wolfClient.start();
		if (wolfClient.isConnected()){
			start = true;
			log.info("wolf client start success .......");
		}else{
			log.info("wolf client start failed .......");
		}
		// if (autoReconnect) {
		// try {
		// final Method method = wolfClient.getClass().getDeclaredMethod(
		// "reconnect", (Class<?>) null);
		// if (method != null) {
		// exec = new ScheduledThreadPoolExecutor(1);
		// exec.scheduleAtFixedRate(new Runnable() {// 每隔一段时间就触发异常
		// @Override
		// public void run() {
		//
		// try {
		// method.invoke(wolfClient,
		// (Object[]) null);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		//
		// }
		// }, 10000L, period * 1000L, TimeUnit.MILLISECONDS);
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		//
		// }
		
		// if (wolfClient.isConnected()) {
		// log.debug("register node to server .......");
		// registerNode(null);
		// }
	}

	public void destory() {

		try {

			// if (exec != null) {
			// exec.shutdown();
			// }
			if (wolfClient.isConnected()) {
				// unRegisterNode(null);
				// Thread.sleep(100);
				wolfClient.stop();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		start = false;

	}

	@Override
	public Object asynSendTask(Object task) {
		return wolfClient.asynSendTask(task);
	}

	@Override
	public <T> T sendTask(Class<T> resultType, String serviceName,
			String methodName, Object... params) {
		try {
			return wolfClient.sendTask(resultType, serviceName, methodName,
					params);
		} catch (Exception e) {
			log.error("send task error", e);
			throw new BaseException(e.getMessage());
		}
	}
	
	@Override
	public <T> T sendTaskByServerId(IoSession session, Class<T> resultType, String serviceName, String methodName,
			Object[] params) {
		if (session != null) {
			SyncWolfTask task = new SyncWolfTask();
			task.setParams(params);
			task.setMethodName(methodName);
			task.setServiceName(serviceName);
			try {
				return NodeSessionMgr.sendTask(session, resultType, task);
			} catch (Exception e) {
				log.error("sendTask error", e);
				throw new BaseException(e.toString());
			}
		} else {
			throw new BaseException("无法连接大区:");
		}

	}

	@Override
	public boolean deleteJob(String groupName, String jobIdInGroup, boolean asyn) {
		if (asyn) {
			AsyncWolfTask jobTask = new AsyncWolfTask(JOB_SERVICE, "deleteJob",
					new Object[] { groupName, jobIdInGroup });

			asynSendTask(jobTask);
		} else {
			return sendTask(Boolean.class, JOB_SERVICE, "deleteJob", groupName,
					jobIdInGroup);
		}
		return true;

	}

	public boolean deleteJob(String groupName, String jobIdInGroup) {
		return deleteJob(groupName, jobIdInGroup, true);
	}

	/**
	 * TODO:这里没有受事务控制，可能会有点问题
	 */
	@Override
	public void startJob(Job job, boolean asyn) {
		if (Job.DISPATCH_AUTO != job.getDispatch()) {// job.setDispatch(Job.DISPATCH_AUTO)常量字符串用==判断，
			job.setDispatch(wolfClient.getLocalAddress());
		}
		if (asyn) {
			AsyncWolfTask jobTask = new AsyncWolfTask(JOB_SERVICE, "startJob",
					new Object[] { job });

			asynSendTask(jobTask);
		} else {
			boolean success = sendTask(Boolean.class, JOB_SERVICE, "startJob",
					job);
			if (!success) {
				throw new BaseException("启动Job失败");
			}
		}
		if (log.isDebugEnabled()) {
			String runDate = null;
			if (job.getJobExcuteTime() > 0) {
				runDate = new Date(job.getJobExcuteTime()).toString();
			} else {
				runDate = job.getRepeatRunRule();
			}
			log.debug("安排了一个计划任务：{},{}开始运行", job.getKey(), runDate);
		}

	}

	public void startJob(Job job) {
		startJob(job, true);
	}

	@Override
	public boolean hasJob(String groupName, String jobIdInGroup) {
		return sendTask(Boolean.class, JOB_SERVICE, "hasJob",
				new StringBuilder(groupName).append("_").append(jobIdInGroup)
						.toString());
	}

	@Override
	public Object registerNode(Map<String, Object> params) {
		NodeRegTask mainTask = new NodeRegTask();
		params.put("usr", adminUser);
		params.put("pwd", adminPwd);

		mainTask.setParams(new Object[] { params });

		return wolfClient.login(mainTask);

	}

	@Override
	public void unRegisterNode(String nodeName) {
		try {
			NodeUnRegTask mainTask = new NodeUnRegTask();
			wolfClient.sendTask(Object.class, mainTask);
		} catch (Exception e) {
			e.printStackTrace();
			throw new  RuntimeException(e);
		}

	}

	public WolfClient getWolfClient() {
		return wolfClient;
	}

	public boolean isConnected() {
		return start && wolfClient != null && wolfClient.isConnected();
	}

}

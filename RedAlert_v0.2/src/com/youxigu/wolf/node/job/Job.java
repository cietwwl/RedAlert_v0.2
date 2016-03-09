package com.youxigu.wolf.node.job;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.youxigu.wolf.net.MethodUtil;
import com.youxigu.wolf.net.NodeSessionMgr;
import com.youxigu.wolf.net.SyncWolfTask;

public class Job implements Serializable, Runnable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -686048090689558359L;

	public static final Logger log = LoggerFactory.getLogger(Job.class);

	private static final int waitTime = 3600000;
	public static final String DEFAULT_GROUP_NAME = "DEFAULT";
	public static final String DISPATCH_AUTO = "AUTO";// 随机在任何一个game Server上执行
	public static final String DISPATCH_ALL = "ALL";// 在所有gameServer上都执行：这个是为了动态修改一些策划数据
	public static final int JOB_RAM = 0;
	public static final int JOB_DB = 1;

	public static final int JOB_RUN_ON_NODE = 0;
	public static final int JOB_RUN_ON_SERVER = 1;

	public static final int JOB_RUN_ON_START = 1;

	private int jobId;// 主键

	private String jobGroupName;// job组名

	private String jobIdInGroup;// job组内Id

	private long jobExcuteTime;// job执行时间,对基准时间的偏移值

	private String serviceName;// spring服务名/job执行的类名
	private String methodName;// job执行的方法名

	private Object[] jobParams = null; // 数组中的数据必须实现Serializable接口
	private byte[] jobPara = null;// job执行时参数

	private int jobType = JOB_DB;//

	private int runOnServer = JOB_RUN_ON_NODE; // =1表示在server上运行

	private int runOnStart = 0; // =1表示在将job加入job队列时，立即运行一次
	/**
	 * 分发到不同机器执行的标识，数据加载时调用,通常是发送job的socket
	 * address(InetSocketAddress.toString())
	 * 
	 * runOnServer =0时有效
	 */
	private String dispatch;

	/**
	 * 需要多次执行的job,
	 * 
	 * 采用CronExpression
	 */
	private String repeatRunRule;// 
	private String errorDesc;
	private transient CronExpression cronExpression;
	private transient boolean isJobRun = false;
	private transient boolean errorJob;
	// 上次运行时间，防止重复运行
	private transient Date lastRunDate;
	/**
	 * 
	 */
	private transient String key = null;

	/**
	 * job出现异常重新执行次数
	 */
	private transient int retryCount;

	public boolean isErrorJob() {
		return errorJob;
	}

	public void setErrorJob(boolean errorJob) {
		this.errorJob = errorJob;
	}

	public CronExpression getCronExpression() {
		if (cronExpression == null) {
			if (repeatRunRule != null && !"".equals(repeatRunRule)) {
				try {
					cronExpression = new CronExpression(repeatRunRule);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("job CronExpression is error");
					repeatRunRule = null;
				}
			}
		}
		return cronExpression;
	}

	public long getNextTime() {

		if (runOnStart >= JOB_RUN_ON_START) {// //立即启动
			int nextTime = runOnStart; // 启动时间为runOnStart表示的毫秒数
			runOnStart = 0;
			return nextTime;
		}
		CronExpression exp = getCronExpression();
		if (exp != null) {
			if (lastRunDate == null) {
				lastRunDate = new Date();
			} else {
				if (lastRunDate.getTime() < System.currentTimeMillis()) {
					lastRunDate = new Date();
				}
			}
			Date date = exp.getNextValidTimeAfter(lastRunDate);
			if (date != null) {
				long nextTime = date.getTime() - System.currentTimeMillis();
				lastRunDate = date;
				return nextTime;
			}
		}
		return -1;

	}

	public int getRunOnStart() {
		return runOnStart;
	}

	public void setRunOnStart(int runOnStart) {
		this.runOnStart = runOnStart;
	}

	public String getRepeatRunRule() {
		return repeatRunRule;
	}

	public void setRepeatRunRule(String repeatRunRule) {
		this.repeatRunRule = repeatRunRule;
		this.cronExpression = null;
	}

	public String getServiceName() {
		return serviceName;
	}

	public int getRunOnServer() {
		return runOnServer;
	}

	public void setRunOnServer(int runOnServer) {
		this.runOnServer = runOnServer;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public int getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	public Object[] getJobParams() {
		if (jobParams == null) {
			if (jobPara != null) {
				ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
						jobPara);
				try {
					ObjectInputStream inputStream = new ObjectInputStream(
							byteArrayInputStream);
					jobParams = (Object[]) inputStream.readObject();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return jobParams;
	}

	public void setJobParams(Object[] jobParams) {
		this.jobParams = jobParams;
	}

	/**
	 * ibatis调用
	 * 
	 * @return
	 */
	public byte[] getJobPara() {

		if (jobPara == null) {
			if (this.jobParams != null) {
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				try {
					ObjectOutputStream objectOutputStream;
					objectOutputStream = new ObjectOutputStream(
							byteArrayOutputStream);
					objectOutputStream.writeObject(jobParams);
				} catch (IOException e) {
					e.printStackTrace();
				}
				this.jobPara = byteArrayOutputStream.toByteArray();

			}
		}
		return jobPara;
	}

	/**
	 * ibatis调用
	 * 
	 * @param jobPara
	 */
	public void setJobPara(byte[] jobPara) {
		this.jobPara = jobPara;
	}

	public Job() {

	}

	public Job(String groupName, String groupId, Date excuteDate, int jobType,
			String serviceName, String methodName, Object[] jobParams) {
		this.jobGroupName = groupName;
		this.jobIdInGroup = groupId;
		this.jobExcuteTime = excuteDate.getTime();
		this.jobType = jobType;
		this.serviceName = serviceName;
		this.methodName = methodName;
		this.jobParams = jobParams;
	}

	public int getJobId() {
		return jobId;
	}

	public void setJobId(int jobId) {
		this.jobId = jobId;
	}

	public String getJobGroupName() {
		if (jobGroupName == null)
			jobGroupName = DEFAULT_GROUP_NAME;
		return jobGroupName;
	}

	public void setJobGroupName(String jobGroupName) {
		this.jobGroupName = jobGroupName;
		this.key = null;
	}

	public String getJobIdInGroup() {
		if (jobIdInGroup == null)
			jobIdInGroup = String.valueOf(jobId);
		return jobIdInGroup;
	}

	public void setJobIdInGroup(String jobIdInGroup) {
		this.jobIdInGroup = jobIdInGroup;
		this.key = null;
	}

	public long getJobExcuteTime() {
		return jobExcuteTime;
	}

	public void setJobExcuteTime(long jobExcuteTime) {
		this.jobExcuteTime = jobExcuteTime;
	}

	public int getJobType() {
		return jobType;
	}

	public void setJobType(int jobType) {
		this.jobType = jobType;
	}

	public String getDispatch() {
		return dispatch;
	}

	public void setDispatch(String dispatch) {
		this.dispatch = dispatch;
	}

	public boolean isJobRun() {
		return isJobRun;
	}

	public void setJobRun(boolean isJobRun) {
		this.isJobRun = isJobRun;
	}

	public String getKey() {
		if (this.key == null) {
			this.key = new StringBuilder(this.getJobGroupName()).append("_")
					.append(this.getJobIdInGroup()).toString();
		}
		return this.key;
	}

	public String getErrorDesc() {
		return errorDesc;
	}

	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}

	@Override
	public void run() {
		if (this.isJobRun) {
			return;
		}
		this.isJobRun = true;
		if (log.isDebugEnabled()) {
			log.debug("执行job:{}", this.key);
		}
		if (runOnServer == 1) {
			// 在server上运行
			try {
				MethodUtil.call(this.getServiceName(), this.getMethodName(),
						this.getJobParams());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} else {

			// long userId = 0;
			// if (this.getDispatch() != null && !"".equals(this.getDispatch()))
			// {
			// try {
			// Long.valueOf(this.getDispatch());
			// } catch (Exception e) {
			// }
			// }

			String nodeName = this.getDispatch();

			long time = 0;
			while (!NodeSessionMgr.hasConn() && time < waitTime) {
				log.warn("没有可用的gameServer ,任务{}.{}无法执行，等待30秒后重试........",
						jobGroupName, jobIdInGroup);
				try {
					Thread.sleep(30000L);
				} catch (Exception e) {
				}
				time = time + 30000L;
			}
			if (this.jobType == JOB_DB) {// 同步执行
				//DBJOB 由于要执行后删除数据库中保存的Job,这里要用同步，否则可能导致Job丢失掉
				SyncWolfTask task = new SyncWolfTask();
				task.setServiceName(this.getServiceName());
				task.setMethodName(this.getMethodName());
				task.setParams(this.getJobParams());
				try {
					NodeSessionMgr.sendTask(nodeName, Void.class, task);
				} catch (Exception e) {
					e.printStackTrace();
					this.errorDesc = e.getMessage();
					throw new RuntimeException(e);
				}
				this.errorDesc = null;
			} else {
				String sendRes = null;
				try {
					sendRes = NodeSessionMgr.sendMessage2Node(nodeName, this
							.getServiceName(), this.getMethodName(), this
							.getJobParams());
				} catch (Exception e) {
					// e.printStackTrace();
					this.errorDesc = e.getMessage();
					throw new RuntimeException(e);
				}
				if (sendRes == null) {
					this.errorDesc = "can't run job:" + this.getKey();
					throw new RuntimeException(this.errorDesc);
				} else {
					this.errorDesc = null;
				}

			}

		}
	}
}

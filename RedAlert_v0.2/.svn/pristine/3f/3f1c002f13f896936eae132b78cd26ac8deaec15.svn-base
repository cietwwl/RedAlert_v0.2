package com.youxigu.wolf.node.job.service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.youxigu.wolf.net.NamingThreadFactory;
import com.youxigu.wolf.node.job.Job;
import com.youxigu.wolf.node.job.dao.IJobDao;

public class JobService implements IJobService {
	public static final Logger log = LoggerFactory.getLogger(JobService.class);

	private static final String THREAD_NAME_PREFIX = "DYNA_JOB";
	/**
	 * 缓存job与FutureTask,
	 * 
	 *TODO:这里处理的不好，浪费内存
	 */
	private ConcurrentHashMap<String, FutureTask<Boolean>> tasks = new ConcurrentHashMap<String, FutureTask<Boolean>>();
	private ConcurrentHashMap<FutureTask<Boolean>, Job> jobs = new ConcurrentHashMap<FutureTask<Boolean>, Job>();
	/**
	 * 
	 */
	private ScheduledThreadPoolExecutor exec = null;

	/**
	 * * 处理器个数*2+1个执行线程的线程池
	 */
	private int poolSize = Runtime.getRuntime().availableProcessors() * 8 + 1;

	/**
	 * job执行失败时重复执行次数
	 */
	private int retryCount = 3;

	/**
	 * job执行失败时重复执行时间间隔
	 */
	private int retryTimePeriod = 180;// second

	private IJobDao jobDao;

	private boolean start = false;

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	public void setRetryTimePeriod(int retryTimePeriod) {
		this.retryTimePeriod = retryTimePeriod;
	}

	public void setPoolSize(int poolSize) {
		this.poolSize = poolSize;
	}

	public void setJobDao(IJobDao jobDao) {
		this.jobDao = jobDao;
	}

	public void start() {
		if (start)
			return;
		// 等待gameserver连接,否则job可能没有可运行的客户端
		//
		// while (ClientConnection.clientConnectionMap.size() <= 0) {
		// try {
		// Thread.sleep(10000);// 等待10秒
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// }
		initPool();
		start = true;
		SimpleTaskQueueService.getInstance().init();
		log.info("job server 初始化完成..... ");

	}

	private void initPool() {
		exec = new ScheduledThreadPoolExecutor(poolSize,
				new NamingThreadFactory(THREAD_NAME_PREFIX)) {
			protected void afterExecute(Runnable r, Throwable t) {
				// log.debug("runnable:{}", r);
				// 这里不要抛出异常，否则线程池可能停止
				try {
					FutureTask<Boolean> task = (FutureTask<Boolean>) r;
					try {
						task.get();
					} catch (Throwable e) {
						t = e;
					}
					if (t != null) {
						t = getRootCause(t);
					}
					Job job = jobs.remove(task);
					if (job != null) {
						String key = job.getKey();
						tasks.remove(key);
						if (t != null || job.getErrorDesc() != null) {
							log.error("任务运行异常:", t);
							if (job.isErrorJob()) {
								jobDao.insertErrorJob(job);
								jobDao.deleteJob(job.getJobId());
								return;
							}
							// IOException 是没有gameserver,出错重新执行 ,
							if (t instanceof IOException
									&& job.getRetryCount() < retryCount) {
								// retryTimePeriod秒后重新执行
								log.error("任务运行失败,job={}将在{}秒后重新运行", key,
										retryTimePeriod);
								job.setJobExcuteTime(System.currentTimeMillis()
										+ retryTimePeriod * 1000);
								job.setRetryCount(job.getRetryCount() + 1);
								job.setJobRun(false);
								startJob(job);
							} else {
								String errorDesc = null;
								if (t != null) {
									errorDesc = t.getMessage();
								} else {
									errorDesc = job.getErrorDesc();
								}

								if (job.getJobType() == Job.JOB_DB) {
									log
											.error(
													"任务运行失败，不再运行,下次启动服务器会重新尝试执行:,job={},error={}",
													key, errorDesc);

									if (errorDesc != null) {
										if (errorDesc.length() > 100)
											errorDesc = errorDesc.substring(0,
													100);
									} else {
										errorDesc = "error";
									}
									job.setErrorDesc(errorDesc);
									jobDao.updateJob(job);
									// jobDao.deleteJob(job.getJobId());
									// jobDao.insertErrorJob(job);
								} else {
									log.error("任务运行失败，不再运行:,job={},error={}",
											key, errorDesc);
								}
							}
						} else {
							if (log.isDebugEnabled()) {
								log.debug("run job success,job={}", key);
							}
							if (job.getCronExpression() != null) {
								// 定期运行的重复任务
								job.setRetryCount(0);
								job.setJobRun(false);
								startJob(job);
							} else {
								if (job.getJobType() == Job.JOB_DB
										&& jobDao != null && job.getJobId() > 0)
									jobDao.deleteJob(job.getJobId());
							}
						}
					}

				} catch (Exception e) {
					log.error("job run exception:", e);

				}

			}
		};
		exec
				.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());

		// exec.setRejectedExecutionHandler(ThreadPoolExecutor.CallerRunsPolicy)

	}

	public void loadSerialJob() {
		if (jobDao != null) {// 持久化job
			long now = System.currentTimeMillis();
			int timeoutJobNum = 0;
			List<Job> list = jobDao.getJobList();
			log.info("数据库中待执行的Job共有{}个", list.size());
			for (Job job : list) {

				try {
					if (job.getErrorDesc() != null) {
						job.setErrorJob(true);
					}
					if (job.getCronExpression() == null) {
						// 超时任务立即执行
						if (job.getJobExcuteTime() - now <= 0) {
							timeoutJobNum++;
						}
					}

					this.startJob(job);
				} catch (Exception e) {
					int jobId = job.getJobId();
					log.error("start job error:{}", jobId);
					if (job.isErrorJob()) {
						jobDao.insertErrorJob(job);
						jobDao.deleteJob(job.getJobId());
					} else {
						// TODO:这里直接删除不太好
						String errorDesc = e.getMessage();
						if (errorDesc != null) {
							errorDesc = errorDesc.substring(0, 100);
						} else {
							errorDesc = "error";
						}
						job.setErrorDesc(errorDesc);
						jobDao.updateJob(job);
					}
				}
			}

			log.info("数据库中待执行的超时Job共有{}个", timeoutJobNum);

		}
	}

	public void destory() {
		log.info("jobservice 停止");
		exec.shutdownNow();
		start = false;

	}

	@Override
	public boolean startJob(Job job) {
		if (!start)
			this.start();

		if (job == null)
			return false;
		String key = job.getKey();
		if (tasks.containsKey(key)) {
			// 删除旧job
			deleteJob(job.getJobGroupName(), job.getJobIdInGroup());
			if (log.isInfoEnabled()) {
				log.info("更新job:{}", key);
			}
			// log.error("job key is error ,not run:" + job.getKey());
			// return false;
		}
		if (job.getJobType() == Job.JOB_DB && jobDao != null
				&& job.getJobId() <= 0) {
			jobDao.insertJob(job);
		}
		long delay = 0;
		if (job.getCronExpression() != null && job.getRetryCount() == 0) {
			delay = job.getNextTime();
			if (delay == -1) {
				log.error("定时任务已经过了运行期限,删除");
				if (job.getJobType() == Job.JOB_DB && jobDao != null
						&& job.getJobId() > 0)
					jobDao.deleteJob(job.getJobId());
				return false;
			}
		} else {
			// 超时任务立即执行
			delay = job.getJobExcuteTime() - System.currentTimeMillis();
		}

		if (log.isDebugEnabled()) {
			log.debug("schedule job:{},run at {}毫秒之后 ", key, delay);
		}
		FutureTask<Boolean> futureTask = (FutureTask<Boolean>) exec.schedule(
				job, delay, TimeUnit.MILLISECONDS);
		tasks.put(key, futureTask);
		jobs.put(futureTask, job);
		return true;
	}

	@Override
	public boolean deleteJob(String groupName, String groupId) {
		String key = new StringBuilder(groupName).append("_").append(groupId)
				.toString();
		FutureTask<Boolean> futureTask = tasks.remove(key);
		if (futureTask != null) {
			if (log.isDebugEnabled())
				log.debug("delete schedule job:{}", key);
			exec.remove(futureTask);
			Job job = jobs.remove(futureTask);
			if (job != null) {
				if (job.isJobRun()) {
					if (log.isDebugEnabled()) {
						log.debug("job is already run:{}", key);
					}
				} else {
					job.setJobRun(true);// 禁止再次运行
					job.setRepeatRunRule(null);// TODO:防止并发

				}
				if (job.getJobType() == Job.JOB_DB && jobDao != null
						&& job.getJobId() > 0) {
					jobDao.deleteJob(job.getJobId());
				}
			}
			return true;
		}
		return false;
	}

	public static void main(String[] args) {
		JobService jobServiuce = new JobService();
		jobServiuce.start();
		jobServiuce.setPoolSize(6);
		for (int i = 0; i < 100; i++) {
			Job job = new Job();
			job.setJobIdInGroup(i + "");
			job.setJobExcuteTime(1 * 1000);
			jobServiuce.startJob(job);
		}
		// jobServiuce.destory();
	}

	public boolean isStart() {
		return start;
	}

	@Override
	public boolean hasJob(String jobKey) {
		return tasks.containsKey(jobKey);
	}

	private Throwable getRootCause(Throwable t) {
		while (t.getCause() != null) {
			t = t.getCause();
		}
		return t;
	}
}

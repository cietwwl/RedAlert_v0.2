package com.youxigu.dynasty2.mail.service.impl;

import com.youxigu.dynasty2.chat.ChatChannelManager;
import com.youxigu.dynasty2.chat.client.IChatClientService;
import com.youxigu.dynasty2.chat.proto.CommonHead;
import com.youxigu.dynasty2.core.flex.amf.IAMF3Action;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.dynasty2.mail.domain.AsyncCmdWolfTask;
import com.youxigu.wolf.net.MethodUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MailTaskDispatcher {
    public static final Logger log = LoggerFactory
            .getLogger(MailTaskDispatcher.class);
    // 执行线程
    private Worker[] workers;
    private int threadNum = 3;
    private static final String MAIL_SERVER_ACTION = "mailServerAction";
    private IChatClientService messageService;

    public void setMessageService(IChatClientService messageService) {
        this.messageService = messageService;
    }

    //================= node远程调用方法区 ====================

    public void sendMail(long sendUserId, List<Long> receiveUserIds, String title, String comment){
        AsyncCmdWolfTask task = new AsyncCmdWolfTask();
        task.setServiceName(MAIL_SERVER_ACTION);
        task.setMethodName("sendMail");
        task.setParams(new Object[]{sendUserId, receiveUserIds, title, comment});

        //本方法其实不需要限制执行在哪个线程上
        getWorker(sendUserId).put(task);
    }

    public void setMailRead(long userId, int msgId){
        AsyncCmdWolfTask task = new AsyncCmdWolfTask();
        task.setServiceName(MAIL_SERVER_ACTION);
        task.setMethodName("setMailRead");
        task.setParams(new Object[]{userId,msgId});

        getWorker(userId).put(task);
    }

    public void extractMailAppendix(long receiveUserId, int messageId, int pos) {
        AsyncCmdWolfTask task = new AsyncCmdWolfTask();
        task.setServiceName(MAIL_SERVER_ACTION);
        task.setMethodName("extractMailAppendix");
        task.setParams(new Object[]{receiveUserId, messageId, pos});

        getWorker(receiveUserId).put(task);
    }

    public void batchExtractMailAppendix(long userId, int type, List<Integer> msgIds){
        AsyncCmdWolfTask task = new AsyncCmdWolfTask();
        task.setServiceName(MAIL_SERVER_ACTION);
        task.setMethodName("batchExtractMailAppendix");
        task.setParams(new Object[]{userId,type,msgIds});

        getWorker(userId).put(task);
    }

    public void deleteMessages(long userId, List<Integer> msgIds){
        AsyncCmdWolfTask task = new AsyncCmdWolfTask();
        task.setServiceName(MAIL_SERVER_ACTION);
        task.setMethodName("deleteMessages");
        task.setParams(new Object[]{userId, msgIds});

        getWorker(userId).put(task);
    }

    public void deleteAllMessages(long userId){
        AsyncCmdWolfTask task = new AsyncCmdWolfTask();
        task.setServiceName(MAIL_SERVER_ACTION);
        task.setMethodName("deleteAllMessages");
        task.setParams(new Object[]{userId});

        getWorker(userId).put(task);
    }

    //================= node远程调用方法区结束 =================

    /**
	 * 初始化方法
	 */
	public void init() {
		log.info("初始化邮件后台服务,默认启动{}个线程", threadNum);
		// 初始化线程
		workers = new Worker[threadNum];
		for (int i = 0; i < threadNum; i++) {
			workers[i] = new Worker("MAIL_TASK_" + i);
			workers[i].start();
			log.info("线程{}启动", workers[i].getName());
		}
	}

	/**
	 * 销毁方法
	 */
	public void destory() {
		log.info("关闭邮件后台服务{}个线程", threadNum);
		try {
			if (workers != null) {
				for (int i = 0; i < threadNum; i++) {
					if (workers[i] != null) {
						workers[i].shutDown();
					}
				}
			}
		} catch (Exception e) {
			log.error("邮件后台服务线程关闭异常", e);
		}
	}

    private Worker getWorker(long userId){
        Worker worker = workers[((int) (userId % threadNum))];
        if(worker == null){
            throw new BaseException("MailBackService后台线程初始化错误");
        }
        return worker;
    }

    /**
	 * 线程
	 *
	 * @author Dagangzi
	 *
	 */
	public class Worker extends Thread {
		private BlockingQueue<AsyncCmdWolfTask> queue;// 阻塞队列
		private boolean start = true;

		public Worker(String name) {
			this.setDaemon(true);
			this.setName(name);
			queue = new LinkedBlockingQueue<AsyncCmdWolfTask>();
		}

		public BlockingQueue<AsyncCmdWolfTask> getQueue() {
			return queue;
		}

		public void setQueue(BlockingQueue<AsyncCmdWolfTask> queue) {
			this.queue = queue;
		}

		public void put(AsyncCmdWolfTask action) {
			try {
				queue.put(action);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void shutDown() {
			start = false;
			try {
                AsyncCmdWolfTask task = new AsyncCmdWolfTask();
                task.setMethodName("");
				queue.put(new AsyncCmdWolfTask());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

        private void execTask(AsyncCmdWolfTask task) {
            if (task == null) {
                return;
            }

            long now = System.currentTimeMillis(); // 开始时间

            String serviceName = task.getServiceName();
            String methodName = task.getMethodName();
            Object[] params = task.getParams();
            int cmd = task.getCmd();
            long userId = task.getUserId();

            Object result = null;
            try {
                if (log.isDebugEnabled()) {
                    log.debug("邮件处理服务线程收到Task：{}", task);
                }
                result = MethodUtil.call(serviceName, methodName, params);
            } catch (Throwable e) {
                String errDesc = null;
                int errCode = IAMF3Action.CMD_SYSTEM_ERROR;

                if (e instanceof InvocationTargetException) {
                    e = ((InvocationTargetException) e).getTargetException();
                } else if (e.getCause() != null) {
                    e = e.getCause();
                }

                if (e instanceof BaseException) {
                    // 业务异常
                    if (log.isDebugEnabled()) {
                        log.debug("请求异常：", e);
                    } else {
                        log.error("method {} exec error. ", methodName, e);
                    }
                    BaseException tmp = (BaseException) e;
                    errCode = tmp.getErrCode();
                    errDesc = tmp.getErrMsg();
                    if (errDesc == null) {
                        Throwable e1 = e.getCause();
                        if (e1 instanceof TimeoutException) {
                            errDesc = "超时，请重试!";
                        } else {
                            errDesc = "系统异常，稍后再试";
                        }
                    }
                } else {
                    log.error("请求异常：" + methodName, e);
                    if (e instanceof TimeoutException) {
                        errDesc = "超时，请重试!";
                    } else {
                        errDesc = "系统异常，稍后再试";
                    }
                }

                CommonHead.ResponseHead.Builder badRequest = CommonHead.ResponseHead.newBuilder();
                badRequest.setCmd(IAMF3Action.CMD_DEFAULT);
                badRequest.setErrCode(errCode);
                badRequest.setErr(errDesc);
                badRequest.setRequestCmd(cmd);
                result = badRequest.build();
            } finally {
                if (result != null) {
                    messageService.sendMessage(0, userId, ChatChannelManager.CHANNEL_SYSTEM, "", result);
                }
                if (log.isWarnEnabled()) {
				long lag = System.currentTimeMillis() - now;
				if (lag > 50) {
					log.warn("命令:{}处理时间过长:{}", cmd, lag);
				}
			}

            }
        }

        @Override
        public void run() {
            while (start) {
                try {
                    AsyncCmdWolfTask task = queue.poll(30, TimeUnit.SECONDS);
                    if (task != null) {
                        if (task.getMethodName().equals("")) {
                            break;
                        }
                        execTask(task);
                    }
                } catch (Exception e) {
                    log.error("邮件处理线程发生异常", e);
                } finally {

                }
            }
            log.info("thread {} 终止运行.", this.getName());
		}
	}
}

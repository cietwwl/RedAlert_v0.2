package com.youxigu.dynasty2.core.flex.amf;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.mina.core.session.IoEvent;
import org.apache.mina.core.session.IoEventType;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.executor.IoEventQueueHandler;
import org.apache.mina.filter.executor.OrderedThreadPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.youxigu.wolf.net.UserSession;

/**
 * 约束请求队列的长度:针对OrderedThreadPoolExecutor
 * 
 * @author Administrator
 * 
 */
@Deprecated
public class ReceivedRequestMaxQueueSizeHandler2 implements IoEventQueueHandler {
	private static Logger log = LoggerFactory
			.getLogger(ReceivedRequestMaxQueueSizeHandler2.class);
	private static final String KEY = "_QUEUE_SIZE";
	private static final String KEY_REJECT = "_QUEUE_REJECT";
	private static final String ERROR = "系统繁忙，稍后再试";
	private int maxQueueSize = 0; // 单个session的队列长度

	public void setMaxQueueSize(int maxQueueSize) {
		this.maxQueueSize = maxQueueSize;
	}

	public ReceivedRequestMaxQueueSizeHandler2() {

	}

	public ReceivedRequestMaxQueueSizeHandler2(int maxQueueSize) {
		this.maxQueueSize = maxQueueSize;
	}

	@Override
	public boolean accept(Object source, IoEvent event) {
		// 这里判断接受请求queue的大小。防止queue过大
		if (maxQueueSize > 0 && event.getType() == IoEventType.MESSAGE_RECEIVED
				&& source instanceof OrderedThreadPoolExecutor) {
			if (event.getParameter() instanceof Map) {
				IoSession session = event.getSession();
				AtomicInteger counter = (AtomicInteger) session
						.getAttribute(KEY);
				if (counter != null) {
					int currQueueSize = counter.get();
					if (currQueueSize > maxQueueSize) {
						Map<String, Object> data = (Map) event.getParameter();
						Integer cmd = (Integer) data
								.remove(IAMF3Action.ACTION_CMD_KEY);
						data.clear();
						data.put(IAMF3Action.ACTION_CMD_KEY, cmd);
						data.put(IAMF3Action.ACTION_ERROR_CODE_KEY,
								IAMF3Action.CMD_SYSTEM_ERROR);
						data.put(IAMF3Action.ACTION_ERROR_KEY, ERROR);
						session.write(data);
						System.out.println("请求数量:{}大于预设的队列数量");
						if (log.isErrorEnabled()) {
							log.error("请求数量:{}大于预设的队列数量:{}，拒绝：", maxQueueSize,
									currQueueSize);
							UserSession us = (UserSession) session
									.getAttribute(UserSession.KEY_USERSESSION);
							if (us != null) {
								log.error("请求大于预设的队列数量的accName={}", us
										.getAccName());
							}

						}
						return false;
					}

				}
			}
		}
		return true;
	}

	@Override
	public void offered(Object source, IoEvent event) {

		IoSession session = event.getSession();
		AtomicInteger counter = (AtomicInteger) session.getAttribute(KEY);
		if (counter == null) {
			counter = new AtomicInteger(1);
			session.setAttribute(KEY, counter);
		} else {
			counter.incrementAndGet();
		}
		System.out.println("========+++"+counter.get());
	}

	@Override
	public void polled(Object source, IoEvent event) {
		IoSession session = event.getSession();
		AtomicInteger counter = (AtomicInteger) session.getAttribute(KEY);
		if (counter != null) {
			counter.decrementAndGet();
			System.out.println("========----"+counter.get());
		}
	}

}

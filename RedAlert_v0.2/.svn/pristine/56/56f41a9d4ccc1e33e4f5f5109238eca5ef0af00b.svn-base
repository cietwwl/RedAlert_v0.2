package com.youxigu.dynasty2.core.flex.amf;

import java.util.Map;

import org.apache.mina.core.session.IoEvent;
import org.apache.mina.core.session.IoEventType;
import org.apache.mina.filter.executor.IoEventQueueHandler;
import org.apache.mina.filter.executor.UnorderedThreadPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 约束请求队列的长度:针对UnorderedThreadPoolExecutor
 * @author Administrator
 *
 */
public class ReceivedRequestMaxQueueSizeHandler implements IoEventQueueHandler {
	private static Logger log = LoggerFactory
			.getLogger(ReceivedRequestMaxQueueSizeHandler.class);

	private static final String ERROR="系统繁忙，稍后再试..";
	private int maxQueueSize = 0;
	
	public void setMaxQueueSize(int maxQueueSize) {
		this.maxQueueSize = maxQueueSize;
	}

	public ReceivedRequestMaxQueueSizeHandler(){
		
	}
	public ReceivedRequestMaxQueueSizeHandler(int maxQueueSize) {
		this.maxQueueSize = maxQueueSize;
	}

	@Override
	public boolean accept(Object source, IoEvent event) {
		// 这里判断接受请求queue的大小。防止queue过大
		if (maxQueueSize > 0 && event.getType()==IoEventType.MESSAGE_RECEIVED && source instanceof UnorderedThreadPoolExecutor) {
			if (event.getParameter() instanceof Map) {
				UnorderedThreadPoolExecutor executor = (UnorderedThreadPoolExecutor) source;
				int currSize = executor.getQueue().size();
				if (currSize > maxQueueSize) {
					Map<String,Object> data = (Map)event.getParameter(); 
					Integer cmd =  (Integer)data.remove(IAMF3Action.ACTION_CMD_KEY);
					data.clear();
					data.put(IAMF3Action.ACTION_CMD_KEY, cmd);
					data.put(IAMF3Action.ACTION_ERROR_CODE_KEY, IAMF3Action.CMD_SYSTEM_ERROR);
					data.put(IAMF3Action.ACTION_ERROR_KEY, ERROR);
					event.getSession().write(data);
					if (log.isDebugEnabled()){
						log.warn("请求数量:{}大于预设的队列数量:{}，拒绝：",maxQueueSize,currSize);
					}
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public void offered(Object source, IoEvent event) {
	
	}

	@Override
	public void polled(Object source, IoEvent event) {
	}

}

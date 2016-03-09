package com.youxigu.wolf.net;



import org.apache.mina.core.session.IoEvent;
import org.apache.mina.filter.executor.IoEventQueueHandler;
import org.apache.mina.filter.executor.UnorderedThreadPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LoggingIoEventQueueHandler implements IoEventQueueHandler{
	private static Logger log = LoggerFactory.getLogger(LoggingIoEventQueueHandler.class);
	
	private String handlerName;
	public LoggingIoEventQueueHandler(String handlerName){
		this.handlerName=handlerName;
	}
	@Override
	public boolean accept(Object source, IoEvent event) {
		//这里可以判断queue的大小。防止queue过大
		return true;
	}

	@Override
	public void offered(Object source, IoEvent event) {
		if ( log.isDebugEnabled() && (source instanceof UnorderedThreadPoolExecutor)){
			UnorderedThreadPoolExecutor executor = (UnorderedThreadPoolExecutor)source;	
			
			log.debug("{} queue size:{},running threadNum={}",new Object[]{handlerName,executor.getQueue().size(),executor.getActiveCount()});
		}
		
	}

	@Override
	public void polled(Object source, IoEvent event) {
	}

}

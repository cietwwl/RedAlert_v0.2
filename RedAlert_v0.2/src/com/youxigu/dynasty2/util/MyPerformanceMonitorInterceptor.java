package com.youxigu.dynasty2.util;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.springframework.aop.interceptor.AbstractMonitoringInterceptor;
import org.springframework.util.StopWatch;

public class MyPerformanceMonitorInterceptor extends AbstractMonitoringInterceptor{
	private int logTime = 20;

	public MyPerformanceMonitorInterceptor() {
	}

	public MyPerformanceMonitorInterceptor(boolean useDynamicLogger) {
		setUseDynamicLogger(useDynamicLogger);
	}

	public void setLogTime(int logTime) {
		this.logTime = logTime;
	}

	protected Object invokeUnderTrace(MethodInvocation invocation, Log logger)
			throws Throwable {
		String name = createInvocationTraceName(invocation);
		StopWatch stopWatch = new StopWatch(name);
		stopWatch.start(name);
		long total;
		try {
			return invocation.proceed();
		} finally {
			stopWatch.stop();
			total = stopWatch.getTotalTimeMillis();
			if (total >= this.logTime){
				logger.trace(stopWatch.shortSummary());
			}
		}
	}
}

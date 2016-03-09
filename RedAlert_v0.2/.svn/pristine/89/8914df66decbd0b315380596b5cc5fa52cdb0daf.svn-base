package com.youxigu.dynasty2.log;



public interface ILogService {
	public void setOpenFlag(int open,boolean broadcast);
	
	/**
	 * tlog & mylog
	 * @param obj
	 */
	public void log(AbsLogLineBuffer obj);
	
	/**
	 * 腾讯有一部分需要直接入腾讯的经分库
	 * @param params
	 */
	public void logDB(Object[] params);
}

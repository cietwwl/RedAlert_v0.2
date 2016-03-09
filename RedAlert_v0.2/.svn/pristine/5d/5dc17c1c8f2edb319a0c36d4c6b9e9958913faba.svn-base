package com.youxigu.dynasty2.log;

import java.util.Date;

/**
 * 抽象类，
 * 
 * @author Administrator
 * 
 */
public abstract class AbsLogLineBuffer {

	public static int TYPE_TLOG = 1;
	public static int TYPE_MYLOG = 2;

	public static AbsLogLineBuffer getBuffer(String areaId,int type, String logType) {
		if (type == TYPE_TLOG) {
			return new LogLineBuffer(areaId,logType);
		} else {
			return new LogLineBufferDemo(areaId,logType);
		}
	}

	public static AbsLogLineBuffer getBuffer(String areaId,String logType) {
		return new LogLineBufferDemo(areaId,logType);
	}

	public abstract AbsLogLineBuffer append(Object obj);

	public abstract AbsLogLineBuffer append(Object... objs);

	public abstract AbsLogLineBuffer appendLegacy(String str);

	public abstract AbsLogLineBuffer append(String str);

	public abstract AbsLogLineBuffer append(boolean b);

	public abstract AbsLogLineBuffer append(int i);

	public abstract AbsLogLineBuffer append(long lng);

	public abstract AbsLogLineBuffer append(float f);

	public abstract AbsLogLineBuffer append(double d);

	public abstract AbsLogLineBuffer append(Date date);

	public abstract Object end();

	public abstract String getAreaId();

	public abstract int getCls();
}

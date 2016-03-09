package com.youxigu.dynasty2.log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * 没必要的，只是为了没有tlog的时候临时替代品
 * 
 * @author Administrator
 * 
 */
public class LogLineBufferDemo extends AbsLogLineBuffer {

	// public static String TYPE_CREATEUSER = "ADDUSER";

	private List<Object> datas = new ArrayList<Object>(10);
	private String areaId = null;

	public LogLineBufferDemo(String areaId, String logType) {
		this.areaId = areaId;
		datas.add(logType);
	}

	public AbsLogLineBuffer append(Object obj) {
		datas.add(obj);
		return this;
	}

	public AbsLogLineBuffer append(Object... objs) {
		for (Object obj : objs) {
			append(obj);
		}
		return this;
	}

	public AbsLogLineBuffer appendLegacy(String str) {
		datas.add(str);
		return this;
	}

	public AbsLogLineBuffer append(String str) {
		datas.add(str);
		return this;
	}

	public AbsLogLineBuffer append(boolean b) {
		datas.add(b);
		return this;
	}

	public AbsLogLineBuffer append(int i) {
		datas.add(i);
		return this;
	}

	public AbsLogLineBuffer append(long lng) {
		datas.add(lng);
		return this;
	}

	public AbsLogLineBuffer append(float f) {
		datas.add(f);
		return this;
	}

	public AbsLogLineBuffer append(double d) {
		datas.add(d);
		return this;
	}

	public AbsLogLineBuffer append(Date date) {
		datas.add(date);
		return this;
	}

	public Object end() {
		return datas;
	}

	public String getAreaId() {
		return areaId;
	}

	public int getCls() {
		return 0;
	}

}

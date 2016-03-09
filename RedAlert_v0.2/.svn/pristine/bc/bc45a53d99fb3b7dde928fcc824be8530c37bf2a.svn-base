package com.youxigu.dynasty2.log;

import java.util.Date;
import java.util.regex.Pattern;

import com.youxigu.dynasty2.util.DateUtils;

/**
 * LogLineBuffer 数据结构<br>
 * 用"|" 分隔的多个字符信息，用"\n"结尾<br>
 * 第一个总是logType,表示log的类型<br>
 * 最后一个总是eventGroupId，一个事务范围内的所有log,总是使用相同的eventGroupId,但是不保证不同事务的eventGroupId不同
 * (有多个server导致的)<br>
 * 中间是多个业务逻辑log信息<br>
 * 
 * eventGroupId 会自动追加到结尾，不用考虑<br>
 * 
 * "logtype|filed1|....|filedn|eventGroupId\n"<br>
 * 
 * @author Administrator
 * 
 */
public class LogLineBuffer extends AbsLogLineBuffer {

	private static final String FILED_SPLIT = "|";
	private static final String LINE_SPLIT = "\n";
	private static Pattern pattern1 = Pattern.compile("\\|");
	private static Pattern pattern2 = Pattern.compile("\n");
	// private static Pattern pattern3 = Pattern.compile("#cut#");

	private StringBuilder sb = null;

	public LogLineBuffer(String areaId, String logType) {
		sb = new StringBuilder(logType.length() + 128);
		sb.append(logType);

	}

	public AbsLogLineBuffer append(Object obj) {
		if (obj == null) {
			sb.append(FILED_SPLIT);
		} else {
			if (obj instanceof Date) {
				append((Date) obj);
			} else {
				append(String.valueOf(obj));
			}
		}
		return this;
	}

	public AbsLogLineBuffer append(Object... objs) {
		for (Object obj : objs) {
			append(obj);
		}
		return this;
	}

	public AbsLogLineBuffer appendLegacy(String str) {
		sb.append(FILED_SPLIT);
		if (str != null) {
			sb.append(str);
		}

		return this;
	}

	public AbsLogLineBuffer append(String str) {
		sb.append(FILED_SPLIT);
		if (str != null && str.length() > 0) {
			str = pattern1.matcher(str).replaceAll("@");
			str = pattern2.matcher(str).replaceAll("@");
			// str = pattern3.matcher(str).replaceAll("\\}");
			sb.append(str);
		}
		return this;
	}

	public AbsLogLineBuffer append(boolean b) {
		sb.append(FILED_SPLIT).append(b);
		return this;
	}

	public AbsLogLineBuffer append(int i) {
		sb.append(FILED_SPLIT).append(i);
		return this;
	}

	public AbsLogLineBuffer append(long lng) {
		sb.append(FILED_SPLIT).append(lng);
		return this;
	}

	public AbsLogLineBuffer append(float f) {
		sb.append(FILED_SPLIT).append(f);
		return this;
	}

	public AbsLogLineBuffer append(double d) {
		sb.append(FILED_SPLIT).append(d);
		return this;
	}

	public AbsLogLineBuffer append(Date date) {
		sb.append(FILED_SPLIT).append(DateUtils.datetime2Text(date));
		return this;
	}

	public Object end() {
		sb.append(LINE_SPLIT);
		return sb;
	}

	public int getCls() {
		return 0;
	}

	protected StringBuilder getBuffer() {
		return sb;
	}

	public String toString() {
		return sb.toString();
	}

	@Override
	public String getAreaId() {
		// TODO Auto-generated method stub
		return null;
	}
}

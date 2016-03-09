package com.youxigu.dynasty2.activity.proto;

import java.io.Serializable;

/**
 * 活动前端展现类
 * 
 * @author fengfeng
 *
 */
public abstract class ActivityView implements Serializable {
	private static final long serialVersionUID = 496879996703240201L;
	/** 活动id */
	private long actId;
	/** 活动名字 */
	private String actName;
	/** 活动类型 */
	private int type;
	/** 活动开始时间毫秒 */
	private long startTime;
	/** 活动结束时间毫秒 */
	private long endTime;
	/** 还有多少时间结束。。单位秒 */
	private long end;

	public long getActId() {
		return actId;
	}

	public void setActId(long actId) {
		this.actId = actId;
	}

	public String getActName() {
		return actName;
	}

	public void setActName(String actName) {
		this.actName = actName;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public long getEnd() {
		return end;
	}

	public void setEnd(long end) {
		this.end = end;
	}

}

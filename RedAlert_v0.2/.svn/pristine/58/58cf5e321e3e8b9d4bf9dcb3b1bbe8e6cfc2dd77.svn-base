package com.youxigu.dynasty2.tips.domain;

import java.sql.Timestamp;

import com.manu.util.UtilDate;
import com.youxigu.dynasty2.develop.proto.DevelopMsg;

/**
 * 各种产生持续一定时间效果的道具的前台显示buff
 * @author Administrator
 *
 */
public class BuffTip implements java.io.Serializable {
    private static final long serialVersionUID = 7415730446105560584L;

	public static final String BUFFTIP_LOCKER_PREFIX="BUFFTIP_";
	
	private int id;
	private long userId;
    private int buffId;
    private int effId;  //buff对应的effectDefine的id
    private Timestamp startTime;
    private Timestamp endTime;
    private String buffName;

    public BuffTip(){

    }

   public DevelopMsg.BuffTipInfo toMsg() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        int countdown = endTime == null ? 0 : (int) UtilDate.secondDistance(endTime, now);
        countdown = countdown > 0 ? countdown : 0;
        int totalSeconds = (int) UtilDate.secondDistance(endTime, startTime);
        totalSeconds = totalSeconds > 0 ? totalSeconds : 0;

        DevelopMsg.BuffTipInfo.Builder builder = DevelopMsg.BuffTipInfo.newBuilder();
        builder.setBuffId(buffId);
        builder.setBuffType(buffName);
        builder.setBuffTotalTime(totalSeconds);
        builder.setBuffCountdownTime(countdown);
        return builder.build();
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public int getEffId() {
        return effId;
    }

    public void setEffId(int effId) {
        this.effId = effId;
    }

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getBuffName() {
		return buffName;
	}
	public void setBuffName(String buffName) {
		this.buffName = buffName;
	}
	public Timestamp getEndTime() {
		return endTime;
	}
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

    public int getBuffId() {
        return buffId;
    }

    public void setBuffId(int buffId) {
        this.buffId = buffId;
    }
}

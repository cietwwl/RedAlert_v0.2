package com.youxigu.dynasty2.develop.domain;

import com.manu.util.UtilDate;
import com.youxigu.dynasty2.develop.proto.DevelopMsg;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 
 * @author Administrator
 * 
 */
public class UserTechnology implements Serializable {
    private static final long serialVersionUID = -4202608537314511268L;
	private int id;// 主键
	private long userId;// 玩家id
	private int entId;// 科技id
	private int level;// 科技级别
	private Timestamp beginDttm;//升级起始时间
    private Timestamp endDttm;//升级到期时间

	public UserTechnology(){}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getEntId() {
		return entId;
	}

	public void setEntId(int entId) {
		this.entId = entId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Timestamp getEndDttm() {
		return endDttm;
	}

	public void setEndDttm(Timestamp endDttm) {
		this.endDttm = endDttm;
	}

    public Timestamp getBeginDttm() {
        return beginDttm;
    }

    public void setBeginDttm(Timestamp beginDttm) {
        this.beginDttm = beginDttm;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("techId=").append(id);
        sb.append(",userId=").append(userId);
        sb.append(",entId=").append(entId);
        sb.append(",level=").append(level);
        sb.append(",beginDttm=").append(beginDttm);
        sb.append(",entDttm=").append(endDttm);
        return sb.toString();
    }

    public DevelopMsg.UserTech toMsg(int upgradeTime){
        Timestamp now = new Timestamp(System.currentTimeMillis());
        //已经开始建造的时间，单位为秒。不在建的为0。
        int beginTime = beginDttm == null ? 0 : (int) UtilDate.secondDistance(now, beginDttm);
        //还剩余的倒计时时间，单位为秒。不在建的为0。
        int countdown = endDttm == null ? 0 : (int) UtilDate.secondDistance(endDttm, now);

        DevelopMsg.UserTech.Builder builder = DevelopMsg.UserTech.newBuilder();
        builder.setTechId(id).setEntId(entId).setLevel(level).setBeginTime(beginTime)
                .setCountdown(countdown).setUpgradeTime(upgradeTime);
        return builder.build();

    }
}

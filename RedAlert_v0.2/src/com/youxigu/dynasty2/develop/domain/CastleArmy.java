package com.youxigu.dynasty2.develop.domain;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 玩家生产的未分配的兵种数量
 * 
 * @author fengfeng
 * 
 */
public class CastleArmy implements Serializable {
	private static final long serialVersionUID = -4585912165322666518L;
	private long casId;// 城池id
	private int num;// 已经生产好的兵数,未分配给英雄
	private int buildNum;// 生产的兵数
	private Timestamp refreshCD; // 生产结束时间
	private int status;// 状态记录 1表示自动补兵

	public CastleArmy() {
		super();
	}

	public CastleArmy(long casId, int num, int buildNum, Timestamp refreshCD) {
		super();
		this.casId = casId;
		this.num = num;
		this.buildNum = buildNum;
		this.refreshCD = refreshCD;
		this.status = 0;
	}

	public long getCasId() {
		return casId;
	}

	public void setCasId(long casId) {
		this.casId = casId;
	}

	public Timestamp getRefreshCD() {
		return refreshCD;
	}

	/**
	 * 获取开始生产零件的时间
	 * 
	 * @return
	 */
	public long getRefreshCDTime() {
		if (refreshCD == null) {
			return 0;
		}
		return refreshCD.getTime();
	}

	public void setRefreshCD(Timestamp refreshCD) {
		this.refreshCD = refreshCD;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		if (num < 0) {
			this.num = 0;
		} else {
			this.num = num;
		}

	}

	/**
	 * 判断是否有在建造的
	 * 
	 * @return
	 */
	public boolean hasBuildIng() {
		return this.buildNum > 0;
	}

	public int getBuildNum() {
		return buildNum;
	}

	public void setBuildNum(int buildNum) {
		this.buildNum = buildNum;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}

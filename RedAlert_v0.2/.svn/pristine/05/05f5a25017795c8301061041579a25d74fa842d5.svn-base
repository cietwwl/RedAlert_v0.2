package com.youxigu.dynasty2.develop.domain;

import java.io.Serializable;
import java.sql.Timestamp;

/*
 * 玩家城池数据在这里定
 */
public class Castle implements Serializable {
    private static final long serialVersionUID = -1177031452555118392L;
	public static final byte STATUS_NORMAL = 0;// 正常
	public static final byte STATUS_OVER = 1;// 流亡
	
    private long casId;// 城市id
    private long userId; // 关联的userId
    private Timestamp calcuDttm; // 每天0点刷新一次城池的日期，用来隔天刷新一些数据
    private Timestamp quarCalcuDttm; // 最后一次15分钟刷新城池信息时间
    private Timestamp changeCountryDttm;// 变换国家时间
    private int stateId;    //所属国家Id
    private int autoBuild;// 是否自动建造 0：不自动 1：自动
    private int posX;   //X坐标
    private int posY;   //Y坐标
    private int status;//状态
    private String casName;//城池名
    
    private String icon;//城堡图标  null表示是默认图标
	private Timestamp iconEndTime;//图标有效期

    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public long getCasId() {
        return casId;
    }

    public void setCasId(long casId) {
        this.casId = casId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Timestamp getCalcuDttm() {
        return calcuDttm;
    }

    public void setCalcuDttm(Timestamp calcuDttm) {
        this.calcuDttm = calcuDttm;
    }

    public Timestamp getQuarCalcuDttm() {
        return quarCalcuDttm;
    }

    public void setQuarCalcuDttm(Timestamp quarCalcuDttm) {
        this.quarCalcuDttm = quarCalcuDttm;
    }

    public Timestamp getChangeCountryDttm() {
        return changeCountryDttm;
    }

    public void setChangeCountryDttm(Timestamp changeCountryDttm) {
        this.changeCountryDttm = changeCountryDttm;
    }

    public int getAutoBuild() {
        return autoBuild;
    }

    public void setAutoBuild(int autoBuild) {
        this.autoBuild = autoBuild;
    }

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCasName() {
		return casName;
	}

	public void setCasName(String casName) {
		this.casName = casName;
	}
	
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Timestamp getIconEndTime() {
		return iconEndTime;
	}

	public void setIconEndTime(Timestamp iconEndTime) {
		this.iconEndTime = iconEndTime;
	}

	/**
	 * 判断是否已经分配的建城点
	 * @return
	 */
	public boolean hasPoint(){
		return (posX>0)&&(posY>0);
	}
}

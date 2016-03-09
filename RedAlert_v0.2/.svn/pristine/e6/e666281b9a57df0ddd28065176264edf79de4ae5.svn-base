package com.youxigu.dynasty2.develop.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import com.manu.util.UtilDate;
import com.youxigu.dynasty2.develop.proto.DevelopMsg;

/**
 * 城池建筑
 *
 * @author Administrator
 */
public class CastleBuilding implements Serializable {
    private static final long serialVersionUID = -6242367771742121719L;
    private int casBuiId;// 主键
    private long casId;// 城池id
    private int buiEntId;// 建筑实体id Building的主键
    private int level;// 建筑级别
    private int builderIndex;  //占用的建筑队列序号
    private Timestamp beginDttm;//建筑开始时间
    private Timestamp endDttm;//建筑到期时间
    private int autoBuild;//0:不自动  1自动

    public int getCasBuiId() {
        return casBuiId;
    }

    public void setCasBuiId(int casBuiId) {
        this.casBuiId = casBuiId;
    }

    public long getCasId() {
        return casId;
    }

    public void setCasId(long casId) {
        this.casId = casId;
    }

    public int getBuiEntId() {
        return buiEntId;
    }

    public void setBuiEntId(int buiEntId) {
        this.buiEntId = buiEntId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getBuilderIndex() {
        return builderIndex;
    }

    public void setBuilderIndex(int builderIndex) {
        this.builderIndex = builderIndex;
    }

    public Timestamp getBeginDttm() {
        return beginDttm;
    }

    public void setBeginDttm(Timestamp beginDttm) {
        this.beginDttm = beginDttm;
    }

    public Timestamp getEndDttm() {
        return endDttm;
    }

    public void setEndDttm(Timestamp endDttm) {
        this.endDttm = endDttm;
    }

    public int getAutoBuild() {
        return autoBuild;
    }

    public void setAutoBuild(int autoBuild) {
        this.autoBuild = autoBuild;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("casBuiId=").append(casBuiId);
        sb.append(",casId=").append(casId);
        sb.append(",buiEntId=").append(buiEntId);
        sb.append(",level=").append(level);
        sb.append(",builderIndex=").append(builderIndex);
        sb.append(",beginDttm=").append(beginDttm);
        sb.append(",entDttm=").append(endDttm);
        sb.append(",autoBuild=").append(autoBuild);
        return sb.toString();
    }

    public DevelopMsg.CastleBuilding toMsg(int upgradeTime, DevelopMsg.ConditionStatus condition) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        //已经开始建造的时间，单位为秒。不在建的为0。
        int beginTime = beginDttm == null ? 0 : (int) UtilDate.secondDistance(now, beginDttm);
        //还剩余的倒计时时间，单位为秒。不在建的为0。
        int countdown = endDttm == null ? 0 : (int) UtilDate.secondDistance(endDttm, now);

        DevelopMsg.CastleBuilding.Builder builder = DevelopMsg.CastleBuilding.newBuilder();
        builder.setBuildingId(casBuiId).setEntId(buiEntId).setLevel(level).setBuilder(builderIndex).setAutoBuild
                (autoBuild).setBeginTime(beginTime).setCountdown(countdown).setUpgradeTime(upgradeTime)
                .setSatisfyUpgradeCondition(condition);

        return builder.build();
    }
}

package com.youxigu.dynasty2.develop.domain;

import java.sql.Timestamp;

import com.manu.util.UtilDate;
import com.youxigu.dynasty2.develop.proto.DevelopMsg;
import com.youxigu.dynasty2.util.BaseException;

/**
 * 城池建筑工
 *
 * @author Administrator
 */
public class CastleBuilder implements java.io.Serializable {
    private static final long serialVersionUID = 4051653900731974410L;
    public static final int BUILDER_IDLE = 0;//空闲
    public static final int BUILDER_WORKING = 1;//忙碌
    public static final int BUILDER_NOTOPEN = 2;//未开放

    public final static int BUILDER_INDEX_NONE = 0;
    public final static int BUILDER_INDEX_FIRST = 1;
    public final static int BUILDER_INDEX_SECOND = 2;
    public final static int BUILDER_INDEX_THIRD = 3;

    private int id;//pk
    private long userId;
    private int builderIndex;
    private long casId;//当前正在建的建筑所属的城池Id
    private int buiId;//当前正在建的CastleBuilding的Id，空闲时为0
    private Timestamp beginDttm;//开始建造时间
    private Timestamp endDttm;//建造完成时间
    private int status; //建筑队列状态

    public int getBuilderIndex() {
        return builderIndex;
    }

    public void setBuilderIndex(int builderIndex) {
        this.builderIndex = builderIndex;
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

    public long getCasId() {
        return casId;
    }

    public void setCasId(long casId) {
        this.casId = casId;
    }

    public int getBuiId() {
        return buiId;
    }

    public void setBuiId(int buiId) {
        this.buiId = buiId;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public DevelopMsg.BuilderInfo toMsg() {
        //还剩余的倒计时时间，单位为秒。空闲的为0。
        Timestamp now = new Timestamp(System.currentTimeMillis());
        int countdown = endDttm == null ? 0 : (int) UtilDate.secondDistance(endDttm, now);

        DevelopMsg.BuilderInfo.BuilderStatus builderStatus = getBuilderStatus(status);

        DevelopMsg.BuilderInfo.Builder builder = DevelopMsg.BuilderInfo.newBuilder();
        builder.setBuildingId(buiId).setBuilderIndex(builderIndex).setCountdown(countdown)
                .setBuilderStatus(builderStatus);

        return builder.build();

    }

    /**
     * 将建造队列状态转化为Protobuf中定义的格式
     */
    private static DevelopMsg.BuilderInfo.BuilderStatus getBuilderStatus(int status) {
        DevelopMsg.BuilderInfo.BuilderStatus builderStatus;
        if (status == BUILDER_IDLE) {
            builderStatus = DevelopMsg.BuilderInfo.BuilderStatus.IDLE;
        } else if (status == BUILDER_WORKING) {
            builderStatus = DevelopMsg.BuilderInfo.BuilderStatus.WORKING;
        } else if (status == BUILDER_NOTOPEN) {
            builderStatus = DevelopMsg.BuilderInfo.BuilderStatus.NOTOPEN;
        } else {
            throw new BaseException("建筑队列状态错误");
        }
        return builderStatus;
    }
}

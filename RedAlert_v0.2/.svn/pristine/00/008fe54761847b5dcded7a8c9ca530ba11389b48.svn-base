package com.youxigu.dynasty2.develop.domain;

import java.io.Serializable;

import com.google.protobuf.Message;
import com.youxigu.dynasty2.chat.EventMessage;
import com.youxigu.dynasty2.chat.ISendMessage;
import com.youxigu.dynasty2.chat.proto.CommonHead;
import com.youxigu.dynasty2.common.AppConstants;
import com.youxigu.dynasty2.develop.proto.DevelopMsg;
import com.youxigu.dynasty2.util.BaseException;

public class CastleBuildingMessage implements ISendMessage, Serializable {
    private static final long serialVersionUID = 8833573911667195473L;
    private CastleBuilding castleBuilding;
    private int buildTimeInSeconds;
    private DevelopMsg.ConditionStatus conditionStatus;

    public CastleBuildingMessage(CastleBuilding castleBuilding, int buildTimeInSeconds,
                                 int conditionStatus) {
        this.castleBuilding = castleBuilding;
        this.buildTimeInSeconds = buildTimeInSeconds;
        if(conditionStatus == AppConstants.UPGRADE_SATISFIED){
            this.conditionStatus = DevelopMsg.ConditionStatus.SATISFIED;
        }
        else if(conditionStatus == AppConstants.UPGRADE_UNSATISFIED){
            this.conditionStatus = DevelopMsg.ConditionStatus.UNSATISFIED;
        }
        else{
            throw new BaseException("是否满足升级条件值错误。");
        }
    }

    public int getBuildTimeInSeconds() {
        return buildTimeInSeconds;
    }

    public void setBuildTimeInSeconds(int buildTimeInSeconds) {
        this.buildTimeInSeconds = buildTimeInSeconds;
    }

    public DevelopMsg.ConditionStatus getConditionStatus() {
        return conditionStatus;
    }

    public void setConditionStatus(DevelopMsg.ConditionStatus conditionStatus) {
        this.conditionStatus = conditionStatus;
    }

    public CastleBuilding getCastleBuilding() {
        return castleBuilding;
    }

    public void setCastleBuilding(CastleBuilding castleBuilding) {
        this.castleBuilding = castleBuilding;
    }

    @Override
    public Message build() {
        DevelopMsg.CastleBuilderSendEvent.Builder sEvent = DevelopMsg.CastleBuilderSendEvent.newBuilder();
        CommonHead.ResponseHead.Builder headBr = CommonHead.ResponseHead.newBuilder();
        headBr.setCmd(EventMessage.TYPE_CASTLE_BUILDING_CHANGED);
        headBr.setRequestCmd(EventMessage.TYPE_CASTLE_BUILDING_CHANGED);
        sEvent.setResponseHead(headBr.build());

        sEvent.setCastleBuilding(castleBuilding.toMsg(buildTimeInSeconds, conditionStatus));
        return sEvent.build();
    }
}

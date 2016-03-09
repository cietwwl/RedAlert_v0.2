package com.youxigu.dynasty2.develop.domain;

import com.google.protobuf.Message;
import com.youxigu.dynasty2.chat.EventMessage;
import com.youxigu.dynasty2.chat.ISendMessage;
import com.youxigu.dynasty2.chat.proto.CommonHead;
import com.youxigu.dynasty2.develop.proto.DevelopMsg;

import java.io.Serializable;

public class UserTechMessage implements ISendMessage, Serializable {
    private static final long serialVersionUID = 143153989013853417L;
    private UserTechnology userTech;
    private int upgradeTimeInSeconds;

    public UserTechMessage(UserTechnology userTech, int upgradeTimeInSeconds){
        this.userTech = userTech;
        this.upgradeTimeInSeconds = upgradeTimeInSeconds;
    }

    @Override
    public Message build() {
        DevelopMsg.UserTechSendEvent.Builder sEvent = DevelopMsg.UserTechSendEvent.newBuilder();
        CommonHead.ResponseHead.Builder headBr = CommonHead.ResponseHead.newBuilder();
        headBr.setCmd(EventMessage.TYPE_USER_TECH_CHANGED);
        headBr.setRequestCmd(EventMessage.TYPE_USER_TECH_CHANGED);
        sEvent.setResponseHead(headBr.build());

        sEvent.setUserTech(userTech.toMsg(upgradeTimeInSeconds));
        return sEvent.build();
    }
}

package com.youxigu.dynasty2.tips.domain;

import java.io.Serializable;

import com.google.protobuf.Message;
import com.youxigu.dynasty2.chat.EventMessage;
import com.youxigu.dynasty2.chat.ISendMessage;
import com.youxigu.dynasty2.chat.proto.CommonHead;
import com.youxigu.dynasty2.develop.proto.DevelopMsg;

public class BuffTipMessage implements ISendMessage, Serializable{
    private static final long serialVersionUID = -1576305745574410684L;
    private BuffTip buffTip;

    public BuffTipMessage(){}

    public BuffTipMessage(BuffTip buffTip){
        this.buffTip = buffTip;
    }

    @Override
    public Message build() {
        DevelopMsg.BuffTipSendEvent.Builder event = DevelopMsg.BuffTipSendEvent.newBuilder();
        CommonHead.ResponseHead.Builder headBr = CommonHead.ResponseHead.newBuilder();
        headBr.setCmd(EventMessage.TYPE_BUFFTIP_CHANGED);
        headBr.setRequestCmd(EventMessage.TYPE_BUFFTIP_CHANGED);
        event.setResponseHead(headBr.build());

        event.setBuffInfo(buffTip.toMsg());

        return event.build();
    }

    public BuffTip getBuffTip() {
        return buffTip;
    }

    public void setBuffTip(BuffTip buffTip) {
        this.buffTip = buffTip;
    }
}

package com.youxigu.dynasty2.armyout.proto;

import java.io.Serializable;

import com.google.protobuf.Message;
import com.youxigu.dynasty2.armyout.proto.MilitarySituationMsg.PBMiSi;
import com.youxigu.dynasty2.chat.ISendMessage;
/**
 * 推送情报信息
 * @author a
 *
 */
public class SendMiSiMsg implements Serializable ,ISendMessage{
	private final PBMiSi pbMiSi;
	public SendMiSiMsg(PBMiSi pbMiSi){
		this.pbMiSi = pbMiSi;
	}
	
	@Override
	public Message build() {
		return pbMiSi;
	}

}

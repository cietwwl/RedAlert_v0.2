package com.youxigu.dynasty2.hero.proto;

import java.io.Serializable;

import com.google.protobuf.Message;
import com.youxigu.dynasty2.chat.ISendMessage;
import com.youxigu.dynasty2.chat.proto.CommonHead.ResponseHead;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Response33130Define;

public class SendEquipCardAndDebrisMsg implements ISendMessage, Serializable {
	private static final long serialVersionUID = 1235627863054196980L;
	private EquipCardAndDebris info = null;

	public SendEquipCardAndDebrisMsg(EquipCardAndDebris info) {
		this.info = info;
	}

	@Override
	public Message build() {
		Response33130Define.Builder res = Response33130Define.newBuilder();
		ResponseHead.Builder headBr = ResponseHead.newBuilder();
		headBr.setCmd(33130);
		headBr.setRequestCmd(33130);
		res.setResponseHead(headBr.build());
		res.setInfo(info.toMsg());
		return res.build();
	}

}

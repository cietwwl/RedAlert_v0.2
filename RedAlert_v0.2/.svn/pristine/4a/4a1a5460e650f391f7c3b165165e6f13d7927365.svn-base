package com.youxigu.dynasty2.hero.proto;

import java.io.Serializable;

import com.google.protobuf.Message;
import com.youxigu.dynasty2.chat.ISendMessage;
import com.youxigu.dynasty2.chat.proto.CommonHead.ResponseHead;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Response33110Define;

public class SendHeroCardAndDebrisMsg implements ISendMessage, Serializable {
	private static final long serialVersionUID = 1235627863054196980L;
	private HeroCardAndDebris info = null;

	public SendHeroCardAndDebrisMsg(HeroCardAndDebris info) {
		this.info = info;
	}

	@Override
	public Message build() {
		Response33110Define.Builder res = Response33110Define.newBuilder();
		ResponseHead.Builder headBr = ResponseHead.newBuilder();
		headBr.setCmd(33110);
		headBr.setRequestCmd(33110);
		res.setResponseHead(headBr.build());

		com.youxigu.dynasty2.hero.proto.HeroMsg.HeroCardAndDebris.Builder b = com.youxigu.dynasty2.hero.proto.HeroMsg.HeroCardAndDebris
				.newBuilder();
		b.setCardNum(info.getCardNum());
		b.setDebrisNum(info.getDebrisNum());
		b.setHeroId(info.getHeroId());
		b.setSysHeroId(info.getSysHeroId());
		res.setCards(b.build());
		return res.build();
	}

}

package com.youxigu.dynasty2.hero.proto;

import java.io.Serializable;

import com.google.protobuf.Message;
import com.youxigu.dynasty2.chat.ISendMessage;
import com.youxigu.dynasty2.chat.proto.CommonHead.ResponseHead;
import com.youxigu.dynasty2.hero.domain.Hero;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Response31024Define;

/**
 * 此消息是服务器主动推送给客户端的武将信息
 * 
 * @author fengfeng
 *
 */
public class HeroFlushView implements ISendMessage, Serializable {
	private static final long serialVersionUID = 1L;
	private Hero info;

	public HeroFlushView(Hero info) {
		super();
		this.info = info;
	}

	@Override
	public Message build() {
		Response31024Define.Builder res = Response31024Define.newBuilder();
		res.setHero(info.toView());
		ResponseHead.Builder hd = ResponseHead.newBuilder();
		hd.setCmd(31024);
		hd.setRequestCmd(31024);
		res.setResponseHead(hd.build());
		return res.build();
	}

}

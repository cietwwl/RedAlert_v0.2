package com.youxigu.dynasty2.hero.proto;

import java.io.Serializable;

import com.google.protobuf.Message;
import com.youxigu.dynasty2.chat.ISendMessage;
import com.youxigu.dynasty2.chat.proto.CommonHead.ResponseHead;
import com.youxigu.dynasty2.hero.domain.Hero;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Response31034Define;

/**
 * 此消息是服务器主动推送给客户端的刷新武将的属性信息
 * 
 * @author fengfeng
 *
 */
public class HeroAttrFlushView implements ISendMessage, Serializable {
	private static final long serialVersionUID = 1L;
	private Hero info;

	public HeroAttrFlushView(Hero info) {
		super();
		this.info = info;
	}

	@Override
	public Message build() {
		Response31034Define.Builder res = Response31034Define.newBuilder();
		res.setHeroAttr(info.getAttrInfo());
		res.setHeroId(info.getHeroId());

		ResponseHead.Builder hd = ResponseHead.newBuilder();
		hd.setCmd(31034);
		hd.setRequestCmd(31034);
		res.setResponseHead(hd.build());
		return res.build();
	}

}

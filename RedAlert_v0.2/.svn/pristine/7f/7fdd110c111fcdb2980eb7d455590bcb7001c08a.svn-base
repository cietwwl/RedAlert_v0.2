package com.youxigu.dynasty2.hero.domain;

import java.io.Serializable;

import com.google.protobuf.Message;
import com.youxigu.dynasty2.chat.ISendMessage;
import com.youxigu.dynasty2.hero.proto.HeroMsg.HeroInfo;

/**
 * 此消息是服务器主动推送给客户端的武将信息
 * 
 * @author fengfeng
 *
 */
public class HeroView implements ISendMessage, Serializable {
	private static final long serialVersionUID = 1L;
	private HeroInfo info;

	public HeroView(HeroInfo info) {
		super();
		this.info = info;
	}

	@Override
	public Message build() {
		return info;
	}

}

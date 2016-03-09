package com.youxigu.dynasty2.hero.proto;

import java.io.Serializable;

import com.google.protobuf.Message;
import com.youxigu.dynasty2.chat.ISendMessage;
import com.youxigu.dynasty2.chat.proto.CommonHead.ItemInfo;
import com.youxigu.dynasty2.chat.proto.CommonHead.ResponseHead;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Response33106Define;

/**
 * 武将卡推送消息
 * 
 * @author fengfeng
 *
 */
public class HeroCardMsg implements ISendMessage, Serializable {
	private static final long serialVersionUID = 875199370232280623L;
	private int entId;
	private int num;

	public HeroCardMsg(int entId, int num) {
		super();
		this.entId = entId;
		this.num = num;
	}

	public int getEntId() {
		return entId;
	}

	public void setEntId(int entId) {
		this.entId = entId;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	@Override
	public Message build() {
		Response33106Define.Builder res = Response33106Define.newBuilder();
		ItemInfo.Builder info = ItemInfo.newBuilder();
		info.setEntId(entId);
		info.setNum(num);
		res.setInfo(info);
		
		ResponseHead.Builder hd = ResponseHead.newBuilder();
		hd.setCmd(33106);
		hd.setRequestCmd(33106);
		res.setResponseHead(hd.build());
		return res.build();
	}

}

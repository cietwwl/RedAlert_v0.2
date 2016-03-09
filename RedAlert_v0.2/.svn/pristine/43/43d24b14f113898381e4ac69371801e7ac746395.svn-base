package com.youxigu.dynasty2.mail.proto;

import java.io.Serializable;

import com.google.protobuf.Message;
import com.youxigu.dynasty2.chat.ISendMessage;
import com.youxigu.dynasty2.chat.proto.CommonHead.ResponseHead;
import com.youxigu.dynasty2.mail.proto.MailMsg.SendMailEvent;

/**
 * 新邮件消息
 * @author Dagangzi
 *
 */
public class SendMailMsg implements ISendMessage, Serializable {
	private int cmd;
	private int type;// 消息种类： 0：普通消息 1：系统消息 10 pvp战报 11 pve战报

	public SendMailMsg(int cmd, int type) {
		super();
		this.cmd = cmd;
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public Message build() {
		SendMailEvent.Builder res = SendMailEvent.newBuilder();
		
		ResponseHead.Builder headBr = ResponseHead.newBuilder();
		headBr.setCmd(this.cmd);
		headBr.setRequestCmd(this.cmd);
		res.setResponseHead(headBr.build());
		
		res.setType(this.type);
		return res.build();
	}

}

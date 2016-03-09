package com.youxigu.dynasty2.activity.proto;

import java.io.Serializable;

import com.google.protobuf.Message;
import com.youxigu.dynasty2.chat.ISendMessage;
import com.youxigu.dynasty2.util.BaseException;

public class SendActivityRewardMsg implements ISendMessage, Serializable {
	private static final long serialVersionUID = -5341966172720609953L;
	private long actId;
	private boolean open = false;

	public SendActivityRewardMsg(long actId) {
		super();
		this.actId = actId;
		this.open = true;
	}

	public SendActivityRewardMsg(long actId, boolean open) {
		super();
		this.actId = actId;
		this.open = open;
	}

	@Override
	public Message build() {
		System.out.println("SendActivityRewardMsg.build() // TODO");
		// throw new BaseException("// TODO");
		return null;
	}

	public long getActId() {
		return actId;
	}

	public void setActId(long actId) {
		this.actId = actId;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

}

package com.youxigu.dynasty2.treasury.domain;

import com.youxigu.dynasty2.chat.IResponseMessage;

/**
 * 掉落物品封装消息
 * 
 * @author fengfeng
 *
 */
public class DropPackItemInfo implements IResponseMessage {
	private int entId;
	private int num;

	public DropPackItemInfo(int entId, int num) {
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

}

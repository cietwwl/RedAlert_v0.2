package com.youxigu.dynasty2.log.imp;

/**
 * 文件名    LogHpAct.java
 *
 * 描  述    体力描述
 *
 * 时  间    2015-2-4
 *
 * 作  者    huhaiquan
 *
 * 版  本    v1.8
 */
public enum LogHpAct {
	FRIEND_SEND(1,"游戏内好友赠送"),
	FRIEND_Platform_SEND(2,"平台好友赠送"),
	GM_ADD(3,"GM加"),
	Risk_ADD(4,"冒险掉落"),
	Mission_ADD(5,"任务加"),
	Online_ADD(6,"在线领取"),
	Quarters_ADD(7,"每15分钟恢复"),
	Zhanyi_DES(8,"战役扣除"),
	USER_ITEM_ADD(9,"使用物品增加"),
	RISK_COST(10,"冒险扣除体力"),
	GM_DEL(11,"GM消耗")
	;
	public final int vuale;
	
	
	public final String desc;

	private LogHpAct(int vuale,  String desc) {
		this.vuale = vuale;
		this.desc = desc;
	}
	
}

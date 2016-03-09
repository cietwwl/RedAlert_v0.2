package com.youxigu.dynasty2.log.imp;

/**
 * 文件名 LogHeroAct.java
 *
 * 描 述 体力描述
 *
 * 时 间 2015-2-4
 *
 * 作 者 huhaiquan
 *
 * 版 本 v1.8
 */
public enum LogHeroAct {

	Fire_Hero(1, "解雇武将"), //
	Level_Hero(2, "升级"), //
	Strong_Hero(3, "进阶"), //
	Qianghua_Hero(4, "强化"), //
	Pub_Hero_ADD(10, "酒馆抽将"), //
	Hecheng_Hero_ADD(11, "合成"), //
	Mystic_Hero_Add(12, "神秘商城购买"), //
	Drop_Hero_ADD(13, "掉落包"), //
	GM_Hero_ADD(14, "GM增加武将"), //
	Mission_Hero_ADD(15, "任务奖励"), //
	Official_Hero_ADD(16, "官职商店"), //
	Init_Hero_ADD(17, "初始武将"), //
	MYSTICSHOP_HERO_ADD(18, "神秘商城购买"), //
	;
	public final int vuale;

	public final String desc;

	private LogHeroAct(int vuale, String desc) {
		this.vuale = vuale;
		this.desc = desc;
	}

}

package com.youxigu.dynasty2.hero.proto;

import java.util.ArrayList;
import java.util.List;

import com.youxigu.dynasty2.chat.IResponseMessage;
import com.youxigu.dynasty2.hero.enums.HeroDrawType;

/**
 * 武将抽奖相应传递消息
 * 
 * @author fengfeng
 *
 */
public class HeroDrawInfo implements IResponseMessage {
	private int luck;
	private boolean ten;
	private HeroDrawType type;
	private long cd;
	private int num;
	private List<HeroDrawItemInfo> drops = new ArrayList<HeroDrawItemInfo>();

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public List<HeroDrawItemInfo> getDrops() {
		return drops;
	}

	public void setDrops(List<HeroDrawItemInfo> drops) {
		this.drops = drops;
	}

	public int getLuck() {
		return luck;
	}

	public void setLuck(int luck) {
		this.luck = luck;
	}

	public boolean isTen() {
		return ten;
	}

	public void setTen(boolean ten) {
		this.ten = ten;
	}

	public HeroDrawType getType() {
		return type;
	}

	public void setType(HeroDrawType type) {
		this.type = type;
	}

	public long getCd() {
		return cd;
	}

	public void setCd(long cd) {
		this.cd = cd;
	}

}

package com.youxigu.dynasty2.hero.proto;

import com.youxigu.dynasty2.chat.IResponseMessage;
import com.youxigu.dynasty2.treasury.domain.DropPackItemInfo;

/**
 * 武将抽奖相应传递消息
 * 
 * @author fengfeng
 *
 */
public class HeroDrawItemInfo implements IResponseMessage {
	private long heroId;
	private int type;
	private DropPackItemInfo drops = null;

	public long getHeroId() {
		return heroId;
	}

	public void setHeroId(long heroId) {
		this.heroId = heroId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public DropPackItemInfo getDrops() {
		return drops;
	}

	public void setDrops(DropPackItemInfo drops) {
		this.drops = drops;
	}

}

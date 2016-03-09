package com.youxigu.dynasty2.hero.domain;

import com.youxigu.dynasty2.treasury.domain.Treasury;

/**
 * 装备的套装..主要是自动穿装备 临时使用
 * 
 * @author fengfeng
 *
 */
public class EquipSuitInfo implements Comparable<EquipSuitInfo> {
	/** 装备的得分。。其实就是战力 */
	private int bestPoint;
	private Treasury treasury = null;

	public EquipSuitInfo(int bestPoint, Treasury treasury) {
		this.bestPoint = bestPoint;
		this.treasury = treasury;
	}

	public int getBestPoint() {
		return bestPoint;
	}

	public void setBestPoint(int bestPoint) {
		this.bestPoint = bestPoint;
	}

	public Treasury getTreasury() {
		return treasury;
	}

	public void setTreasury(Treasury treasury) {
		this.treasury = treasury;
	}

	@Override
	public int compareTo(EquipSuitInfo o) {
		return o.getBestPoint() - this.getBestPoint();
	}
}

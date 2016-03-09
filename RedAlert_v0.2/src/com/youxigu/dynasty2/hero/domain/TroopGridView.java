package com.youxigu.dynasty2.hero.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 军团里面的格子信息
 * 
 * @author fengfeng
 *
 */
public class TroopGridView implements Serializable {
	private static final long serialVersionUID = -2364170038896182012L;
	private HeroView heroView;
	private List<Long> equipId = new ArrayList<Long>();

	public void setHeroView(HeroView heroView) {
		this.heroView = heroView;
	}

	public void setEquipId(List<Long> equipId) {
		this.equipId = equipId;
	}

	public HeroView getHeroView() {
		return heroView;
	}

	public List<Long> getEquipId() {
		return equipId;
	}

}

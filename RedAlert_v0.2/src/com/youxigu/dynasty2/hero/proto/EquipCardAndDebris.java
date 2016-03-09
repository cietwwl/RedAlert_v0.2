package com.youxigu.dynasty2.hero.proto;

import java.io.Serializable;

public class EquipCardAndDebris implements Serializable {
	private static final long serialVersionUID = 1L;
	private int entId;
	private int num;

	public EquipCardAndDebris(int entId, int num) {
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

	public com.youxigu.dynasty2.hero.proto.HeroMsg.EquipCardAndDebris toMsg() {
		com.youxigu.dynasty2.hero.proto.HeroMsg.EquipCardAndDebris.Builder ifo = com.youxigu.dynasty2.hero.proto.HeroMsg.EquipCardAndDebris
				.newBuilder();
		ifo.setEntId(entId);
		ifo.setNum(num);
		return ifo.build();

	}

}

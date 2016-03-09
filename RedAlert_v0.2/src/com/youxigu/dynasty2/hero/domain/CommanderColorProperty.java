package com.youxigu.dynasty2.hero.domain;

import java.io.Serializable;

/**
 * 指挥官品质属性
 * 
 * @author Dagangzi
 * @date 2016年1月19日
 */
public class CommanderColorProperty implements Serializable {
	private int color; // 评价 即 武将星级,武将颜色
	private int growing; // 评价 即 武将星级,武将颜色
	private int magicAttack;// 法术攻击
	private int physicalAttack;// 物理攻击
	private int magicDefend;// 法术防御
	private int physicalDefend;// 物理防御
	private int initHp;// 生命
	private int initMorale;// 士气
	private int initHit;// 命中
	private int initDodge;// 闪避
	private int critDec;// 免爆-决定坦克被攻击时的暴击率
	private int critAdd;// 暴击-决定坦克攻击时的暴击率
	private int critDamageAdd;// 暴击伤害-决定坦克攻击时的暴击伤害。（暂定为1.5倍，后期投放属性）
	private int critDamageDec;// 爆伤减免-决定坦克被攻击时的暴击伤害。（暂定为1.5倍，后期投放属性）

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getGrowing() {
		return growing;
	}

	public void setGrowing(int growing) {
		this.growing = growing;
	}

	public int getMagicAttack() {
		return magicAttack;
	}

	public void setMagicAttack(int magicAttack) {
		this.magicAttack = magicAttack;
	}

	public int getPhysicalAttack() {
		return physicalAttack;
	}

	public void setPhysicalAttack(int physicalAttack) {
		this.physicalAttack = physicalAttack;
	}

	public int getMagicDefend() {
		return magicDefend;
	}

	public void setMagicDefend(int magicDefend) {
		this.magicDefend = magicDefend;
	}

	public int getPhysicalDefend() {
		return physicalDefend;
	}

	public void setPhysicalDefend(int physicalDefend) {
		this.physicalDefend = physicalDefend;
	}

	public int getInitHp() {
		return initHp;
	}

	public void setInitHp(int initHp) {
		this.initHp = initHp;
	}

	public int getInitMorale() {
		return initMorale;
	}

	public void setInitMorale(int initMorale) {
		this.initMorale = initMorale;
	}

	public int getInitHit() {
		return initHit;
	}

	public void setInitHit(int initHit) {
		this.initHit = initHit;
	}

	public int getInitDodge() {
		return initDodge;
	}

	public void setInitDodge(int initDodge) {
		this.initDodge = initDodge;
	}

	public int getCritDec() {
		return critDec;
	}

	public void setCritDec(int critDec) {
		this.critDec = critDec;
	}

	public int getCritAdd() {
		return critAdd;
	}

	public void setCritAdd(int critAdd) {
		this.critAdd = critAdd;
	}

	public int getCritDamageAdd() {
		return critDamageAdd;
	}

	public void setCritDamageAdd(int critDamageAdd) {
		this.critDamageAdd = critDamageAdd;
	}

	public int getCritDamageDec() {
		return critDamageDec;
	}

	public void setCritDamageDec(int critDamageDec) {
		this.critDamageDec = critDamageDec;
	}

}

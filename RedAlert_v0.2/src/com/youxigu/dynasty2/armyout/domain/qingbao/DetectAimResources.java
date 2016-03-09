package com.youxigu.dynasty2.armyout.domain.qingbao;

import java.io.Serializable;

/**
 * 侦查目标资源
 * @author a
 *
 */
public class DetectAimResources implements Serializable{
	private static final long serialVersionUID = -2146863263491526137L;
	private int money; // 金钱
	private int oil; // 油
	private int axis; // 轴
	private int spareParts; // 零件
	private int iron; // 钢铁
	private int refugee; // 难民
	
	
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public int getOil() {
		return oil;
	}
	public void setOil(int oil) {
		this.oil = oil;
	}
	public int getAxis() {
		return axis;
	}
	public void setAxis(int axis) {
		this.axis = axis;
	}
	public int getSpareParts() {
		return spareParts;
	}
	public void setSpareParts(int spareParts) {
		this.spareParts = spareParts;
	}
	public int getIron() {
		return iron;
	}
	public void setIron(int iron) {
		this.iron = iron;
	}
	public int getRefugee() {
		return refugee;
	}
	public void setRefugee(int refugee) {
		this.refugee = refugee;
	}
	
	
	
}

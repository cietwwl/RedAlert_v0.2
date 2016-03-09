package com.youxigu.dynasty2.entity.domain;

public class DropPackItem implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4426549639695092454L;

	private int id;// pk

	private int entId;

	private int dropEntId; // 掉落的entityId,可能是一个子掉落包

	private int dropPercent;// 掉落百万分率

	private int minValue;// 经验数量 或 道具数量 最小值

	private int maxValue2;// 经验数量 或 道具数量 最大值

	private int weight;// 权重 -1 没有权重 直接掉落

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getEntId() {
		return entId;
	}

	public void setEntId(int entId) {
		this.entId = entId;
	}

	public int getDropEntId() {
		return dropEntId;
	}

	public void setDropEntId(int dropEntId) {
		this.dropEntId = dropEntId;
	}

	public int getDropPercent() {
		return dropPercent;
	}

	public void setDropPercent(int dropPercent) {
		this.dropPercent = dropPercent;
	}

	public int getMinValue() {
		return minValue;
	}

	public void setMinValue(int minValue) {
		this.minValue = minValue;
	}

	public int getMaxValue2() {
		return maxValue2;
	}

	public void setMaxValue2(int maxValue2) {
		this.maxValue2 = maxValue2;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

}

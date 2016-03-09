package com.youxigu.dynasty2.entity.domain;

public class DroppedEntity implements java.io.Serializable {
	private static final long serialVersionUID = -6434109114415176750L;
	private int entId;
	private int num;

	public DroppedEntity() {

	}

	public DroppedEntity(int entId, int num) {
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

	public void addNum(int num) {
		this.num = this.num + num;
	}

	public boolean equals(Object obj) {
		if (obj instanceof DroppedEntity) {
			if (((DroppedEntity) obj).getEntId() == this.entId) {
				return true;
			}
		}
		return false;

	}
}

package com.youxigu.dynasty2.treasury.domain;

import java.io.Serializable;
import java.util.List;

import com.youxigu.dynasty2.chat.proto.CommonHead;

/**
 * 运营礼包
 * @author Dagangzi
 *
 */
public class OnlinePack implements Serializable {
	// 掉落包属性
	private int entId;
	private String name;
	private String desc;
	private int color;
	private String icon;
	private int sumAble;// 可否堆叠 0:否 1:可以 下同
	private int throwAble;// 可否丢弃
	private int useAble;// 可否使用 0:（使用和批量使用都不可以） 1：（使用和批量使用都可以） 2：不能批量使用
	private int type;// 类型 6:装备 7:宝物
	private int childType;// 子类型 0:无 默认
	private List<DroppackInfo> items;//掉落内容
	private int stackNum;// 可堆叠数量上限
	private int exchangeId;// 兑换物品表的id

	public int getEntId() {
		return entId;
	}

	public void setEntId(int entId) {
		this.entId = entId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getSumAble() {
		return sumAble;
	}

	public void setSumAble(int sumAble) {
		this.sumAble = sumAble;
	}

	public int getThrowAble() {
		return throwAble;
	}

	public void setThrowAble(int throwAble) {
		this.throwAble = throwAble;
	}

	public int getUseAble() {
		return useAble;
	}

	public void setUseAble(int useAble) {
		this.useAble = useAble;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getChildType() {
		return childType;
	}

	public void setChildType(int childType) {
		this.childType = childType;
	}

	public List<DroppackInfo> getItems() {
		return items;
	}

	public void setItems(List<DroppackInfo> items) {
		this.items = items;
	}

	public int getStackNum() {
		return stackNum;
	}

	public void setStackNum(int stackNum) {
		this.stackNum = stackNum;
	}

	public int getExchangeId() {
		return exchangeId;
	}

	public void setExchangeId(int exchangeId) {
		this.exchangeId = exchangeId;
	}

	/**
	 * 掉落内容描述
	 * 
	 * @author Dagangzi
	 */
	public static class DroppackInfo implements java.io.Serializable {
		private int entId;
		private String name;
		private int minNum;
		private int maxNum;

		public DroppackInfo() {
		}

		public DroppackInfo(int entId, String name, int minNum, int maxNum) {
			this.entId = entId;
			this.name = name;
			this.minNum = minNum;
			this.maxNum = maxNum;
		}

		public int getEntId() {
			return entId;
		}

		public void setEntId(int entId) {
			this.entId = entId;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getMinNum() {
			return minNum;
		}

		public void setMinNum(int minNum) {
			this.minNum = minNum;
		}

		public int getMaxNum() {
			return maxNum;
		}

		public void setMaxNum(int maxNum) {
			this.maxNum = maxNum;
		}

		public CommonHead.DroppackInfo build() {
			CommonHead.DroppackInfo.Builder dEvent = CommonHead.DroppackInfo.newBuilder();
			dEvent.setEntId(this.entId);
			dEvent.setName(this.name);
			dEvent.setMinNum(this.minNum);
			dEvent.setMaxNum(this.maxNum);
			return dEvent.build();
		}
	}

	public CommonHead.OnlinePack build() {
		// 必须包含responseHead
		CommonHead.OnlinePack.Builder tEvent = CommonHead.OnlinePack.newBuilder();
		tEvent.setEntId(this.entId);
		tEvent.setName(this.name);
		tEvent.setDesc(this.desc);
		tEvent.setColor(this.color);
		tEvent.setIcon(this.icon);
		tEvent.setSumAble(this.sumAble);
		tEvent.setThrowAble(this.throwAble);
		tEvent.setUseAble(this.useAble);
		tEvent.setType(this.type);
		tEvent.setChildType(this.childType);
		tEvent.setStackNum(this.stackNum);
		tEvent.setExchangeId(this.exchangeId);
		if (this.items != null && this.items.size() > 0) {
			for (DroppackInfo droppackInfo : this.items) {
				tEvent.addItems(droppackInfo.build());
			}
		}
		return tEvent.build();
	}
}

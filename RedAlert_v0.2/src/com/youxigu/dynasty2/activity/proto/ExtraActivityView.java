package com.youxigu.dynasty2.activity.proto;

public class ExtraActivityView extends ActivityView {
	private static final long serialVersionUID = 1846744132628551415L;
	private int effectId;
	private String actDesc;

	public ExtraActivityView(int effectId, String actDesc) {
		super();
		this.effectId = effectId;
		this.actDesc = actDesc;
	}

	public int getEffectId() {
		return effectId;
	}

	public void setEffectId(int effectId) {
		this.effectId = effectId;
	}

	public String getActDesc() {
		return actDesc;
	}

	public void setActDesc(String actDesc) {
		this.actDesc = actDesc;
	}

}

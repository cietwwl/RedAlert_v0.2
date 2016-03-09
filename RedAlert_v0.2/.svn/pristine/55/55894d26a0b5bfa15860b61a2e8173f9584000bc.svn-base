package com.youxigu.dynasty2.hero.proto;

import com.youxigu.dynasty2.util.EffectValue;

/**
 * 构造给客户端发送的属性效果
 * 
 * @author fengfeng
 *
 */
public class EffectValueMsg {
	private String key;
	private EffectValue value;
	private boolean isPercent = false;

	public EffectValueMsg(String key, EffectValue value, boolean isPercent) {
		super();
		this.key = key;
		this.value = value;
		this.isPercent = isPercent;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getAbsValue() {
		return value.getAbsValue();
	}

	public int getPerValue() {
		return value.getPerValue();
	}

	public boolean isPercent() {
		return isPercent;
	}

	public void setPercent(boolean isPercent) {
		this.isPercent = isPercent;
	}

}

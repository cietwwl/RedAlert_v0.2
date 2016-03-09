package com.youxigu.dynasty2.util;

public class EffectValue {
	private int absValue;
	private int perValue;

	public EffectValue() {

	}

	public EffectValue(int abs, int percent) {
		this.absValue = abs;
		this.perValue = percent;
		check();
	}

	public int getAbsValue() {
		return absValue;
	}

	public void setAbsValue(int absValue) {
		this.absValue = absValue;
	}

	public int getPerValue() {
		return perValue;
	}

	public void setPerValue(int perValue) {
		this.perValue = perValue;
	}

	public double getDoublePerValue() {
		return 1.0d * perValue / 100d;
	}

	public boolean isValid() {
		return absValue != 0 || perValue != 0;
	}

	public void addPercentValue(int value) {
		this.perValue = this.perValue + value;
	}

	public void addAbsValue(int value) {
		this.absValue = this.absValue + value;
	}

	public void add(EffectValue value) {
		this.perValue = this.perValue + value.getPerValue();
		this.absValue = this.absValue + value.getAbsValue();
	}

	public void add(int abs, int percent) {
		this.perValue = this.perValue + percent;
		this.absValue = this.absValue + abs;
	}

	public void check() {
		if (perValue > 1000) {
			throw new BaseException("设置的千分比错误" + perValue);
		}
	}
}

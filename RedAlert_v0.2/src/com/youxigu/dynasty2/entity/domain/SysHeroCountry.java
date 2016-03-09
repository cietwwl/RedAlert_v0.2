package com.youxigu.dynasty2.entity.domain;

/**
 * 系统武将所属的国家类型
 * 
 * @author fengfeng
 *
 */
public class SysHeroCountry {
	private int id;
	private String countryName;
	private String icon;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

}

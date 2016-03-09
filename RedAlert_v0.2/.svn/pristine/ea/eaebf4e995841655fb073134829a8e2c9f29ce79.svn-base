package com.youxigu.dynasty2.map.domain;

import com.youxigu.dynasty2.chat.proto.CommonHead.SexType;
import com.youxigu.dynasty2.util.BaseException;

/**
 * 国家下面配置的可选人物,创建角色用
 * 
 * @author fengfeng
 *
 */
public class CountryCharacter {
	private int id;
	private int countryId;
	private int sex;// 性别
	private String icon;// 图像

	private Country country;
	private SexType sexType;

	public CountryCharacter() {
		super();
	}

	public void init(Country country) {
		this.country = country;
		this.sexType = SexType.valueOf(sex);
		if (sexType == null) {
			throw new BaseException("性别配置错误,只能是0或者1," + sex);
		}

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Country getCountry() {
		return country;
	}

	public SexType getSexType() {
		return sexType;
	}

}

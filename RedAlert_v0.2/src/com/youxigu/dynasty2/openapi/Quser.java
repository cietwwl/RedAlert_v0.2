package com.youxigu.dynasty2.openapi;

/**
 * @author zhaiyong QQ用户信息 调用腾讯平台获取用户信息接口v3/user/get_info， 返回的JSON对象对应的QQ用户信息类
 */

public class Quser implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4334329122999919730L;

	private String ret;
	private String msg;
	private String is_lost;
	private String nickname;
	private String gender;
	private String country;
	private String province;
	private String city;
	private String figureurl;
	private String openid;

	private String is_vip;
	private String is_super_vip;	
	private String is_year_vip;
	private String qq_level;
	private String qq_vip_level;
	private String qplus_level;

	private String is_yellow_vip;
	private String is_yellow_year_vip;
	private String yellow_vip_level;
	private String is_yellow_high_vip;
	private String is_red_vip;
	private String is_red_year_vip;
	private String red_vip_level;
	private String is_high_red;

	private String is_blue_vip;
	private String is_blue_year_vip;
	private String blue_vip_level;
	private String is_high_blue;

	private String pf3366_level;
	private String pf3366_level_name;
	private String pf3366_grow_level;
	private String pf3366_grow_value;
	// QQgame平台特有
	private String uin;
	private String pf;
	private String seq;
	private String is_super_blue_vip;

	private String picture40;
	private String picture100;

	/**
	 * @return the uin
	 */
	public String getUin() {
		return uin;
	}

	/**
	 * @param uin
	 *            the uin to set
	 */
	public void setUin(String uin) {
		this.uin = uin;
	}

	/**
	 * @return the pf
	 */
	public String getPf() {
		return pf;
	}

	/**
	 * @param pf
	 *            the pf to set
	 */
	public void setPf(String pf) {
		this.pf = pf;
	}

	/**
	 * @return the is_vip
	 */
	public String getIs_vip() {
		return is_vip;
	}

	/**
	 * @param is_vip
	 *            the is_vip to set
	 */
	public void setIs_vip(String is_vip) {
		this.is_vip = is_vip;
	}

	/**
	 * @return the is_year_vip
	 */
	public String getIs_year_vip() {
		return is_year_vip;
	}

	/**
	 * @param is_year_vip
	 *            the is_year_vip to set
	 */
	public void setIs_year_vip(String is_year_vip) {
		this.is_year_vip = is_year_vip;
	}

	public void setBlue_vip_level(String blue_vip_level) {
		this.blue_vip_level = blue_vip_level;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getFigureurl() {
		return figureurl;
	}

	public void setFigureurl(String figureurl) {
		this.figureurl = figureurl;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getIs_blue_vip() {
		return is_blue_vip;
	}

	public void setIs_blue_vip(String is_blue_vip) {
		this.is_blue_vip = is_blue_vip;
	}

	public String getIs_blue_year_vip() {
		return is_blue_year_vip;
	}

	public void setIs_blue_year_vip(String is_blue_year_vip) {
		this.is_blue_year_vip = is_blue_year_vip;
	}

	public String getIs_lost() {
		return is_lost;
	}

	public void setIs_lost(String is_lost) {
		this.is_lost = is_lost;
	}

	public String getIs_yellow_vip() {
		return is_yellow_vip;
	}

	public void setIs_yellow_vip(String is_yellow_vip) {
		this.is_yellow_vip = is_yellow_vip;
	}

	public String getIs_yellow_year_vip() {
		return is_yellow_year_vip;
	}

	public void setIs_yellow_year_vip(String is_yellow_year_vip) {
		this.is_yellow_year_vip = is_yellow_year_vip;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPf3366_grow_level() {
		return pf3366_grow_level;
	}

	public void setPf3366_grow_level(String pf3366_grow_level) {
		this.pf3366_grow_level = pf3366_grow_level;
	}

	public String getPf3366_grow_value() {
		return pf3366_grow_value;
	}

	public void setPf3366_grow_value(String pf3366_grow_value) {
		this.pf3366_grow_value = pf3366_grow_value;
	}

	public String getPf3366_level() {
		return pf3366_level;
	}

	public void setPf3366_level(String pf3366_level) {
		this.pf3366_level = pf3366_level;
	}

	public String getPf3366_level_name() {
		return pf3366_level_name;
	}

	public void setPf3366_level_name(String pf3366_level_name) {
		this.pf3366_level_name = pf3366_level_name;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getRet() {
		return ret;
	}

	public void setRet(String ret) {
		this.ret = ret;
	}

	public String getYellow_vip_level() {
		return yellow_vip_level;
	}

	public void setYellow_vip_level(String yellow_vip_level) {
		this.yellow_vip_level = yellow_vip_level;
	}

	/**
	 * @return the openid
	 */
	public String getOpenid() {
		return openid;
	}

	/**
	 * @param openid
	 *            the openid to set
	 */
	public void setOpenid(String openid) {
		this.openid = openid;
	}

	/**
	 * @return the qq_level
	 */
	public String getQq_level() {
		return qq_level;
	}

	/**
	 * @param qq_level
	 *            the qq_level to set
	 */
	public void setQq_level(String qq_level) {
		this.qq_level = qq_level;
	}

	/**
	 * @return the qq_vip_level
	 */
	public String getQq_vip_level() {
		return qq_vip_level;
	}

	/**
	 * @param qq_vip_level
	 *            the qq_vip_level to set
	 */
	public void setQq_vip_level(String qq_vip_level) {
		this.qq_vip_level = qq_vip_level;
	}

	/**
	 * @return the qplus_level
	 */
	public String getQplus_level() {
		return qplus_level;
	}

	/**
	 * @param qplus_level
	 *            the qplus_level to set
	 */
	public void setQplus_level(String qplus_level) {
		this.qplus_level = qplus_level;
	}

	/**
	 * @return the blue_vip_level
	 */
	public String getBlue_vip_level() {
		return blue_vip_level;
	}

	/**
	 * @return the is_yellow_high_vip
	 */
	public String getIs_yellow_high_vip() {
		return is_yellow_high_vip;
	}

	/**
	 * @param is_yellow_high_vip
	 *            the is_yellow_high_vip to set
	 */
	public void setIs_yellow_high_vip(String is_yellow_high_vip) {
		this.is_yellow_high_vip = is_yellow_high_vip;
	}

	/**
	 * @return the is_high_blue
	 */
	public String getIs_high_blue() {
		return is_high_blue;
	}

	/**
	 * @param is_high_blue
	 *            the is_high_blue to set
	 */
	public void setIs_high_blue(String is_high_blue) {
		this.is_high_blue = is_high_blue;
	}

	/**
	 * @return the seq
	 */
	public String getSeq() {
		return seq;
	}

	/**
	 * @param seq
	 *            the seq to set
	 */
	public void setSeq(String seq) {
		this.seq = seq;
	}

	/**
	 * @return the is_super_blue_vip
	 */
	public String getIs_super_blue_vip() {
		return is_super_blue_vip;
	}

	/**
	 * @param is_super_blue_vip
	 *            the is_super_blue_vip to set
	 */
	public void setIs_super_blue_vip(String is_super_blue_vip) {
		this.is_super_blue_vip = is_super_blue_vip;
	}

	public String getPicture40() {
		return picture40;
	}

	public void setPicture40(String picture40) {
		this.picture40 = picture40;
	}

	public String getPicture100() {
		return picture100;
	}

	public void setPicture100(String picture100) {
		this.picture100 = picture100;
	}

	public String getIs_red_vip() {
		return is_red_vip;
	}

	public void setIs_red_vip(String isRedVip) {
		is_red_vip = isRedVip;
	}

	public String getIs_red_year_vip() {
		return is_red_year_vip;
	}

	public void setIs_red_year_vip(String isRedYearVip) {
		is_red_year_vip = isRedYearVip;
	}

	public String getRed_vip_level() {
		return red_vip_level;
	}

	public void setRed_vip_level(String redVipLevel) {
		red_vip_level = redVipLevel;
	}

	public String getIs_high_red() {
		return is_high_red;
	}

	public void setIs_high_red(String isHighRed) {
		is_high_red = isHighRed;
	}

	public String getIs_super_vip() {
		return is_super_vip;
	}

	public void setIs_super_vip(String isSuperVip) {
		is_super_vip = isSuperVip;
	}

}

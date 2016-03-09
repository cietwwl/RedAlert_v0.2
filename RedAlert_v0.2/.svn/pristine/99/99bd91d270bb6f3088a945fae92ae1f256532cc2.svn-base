package com.youxigu.dynasty2.openapi.domain;

/**
 * 文件名    QQFriendsVipInfo.java
 *
 * 描  述    qq会员基本信息
 *
 * 时  间    2014-9-5
 *
 * 作  者    huhaiquan
 *
 * 版  本   v0.4  
 */
public class QQFriendsVipInfo extends BaseMessage {
//	truct QQFriendsVipInfo {
//	    1   optional     string          openid;          //好友openid
//	    2   optional     int             is_qq_vip;       //是否为QQ会员（0：不是； 1：是）
//	    3   optional     int             qq_vip_level;    //QQ会员等级（如果是QQ会员才返回此字段）
//	    4   optional     int             is_qq_year_vip;  //是否为年费QQ会员（0：不是； 1：是）
//	};

	private String openId;
	
	private  boolean isQQVip;
	
	private int qqVipLevel;
	
	private boolean isQQYearVip;
	
	

	public QQFriendsVipInfo(String openId, int is_qq_vip, int qqVipLevel,
			int is_qq_year_vip) {
		this.openId = openId;
		this.isQQVip = is_qq_vip==1;
		this.qqVipLevel = qqVipLevel;
		this.isQQYearVip = is_qq_year_vip==1;
	}

	/**
	 * @return the openId
	 */
	public String getOpenId() {
		return openId;
	}

	/**
	 * @return the isQQVip
	 */
	public boolean isQQVip() {
		return isQQVip;
	}

	/**
	 * @return the qqVipLevel
	 */
	public int getQqVipLevel() {
		return qqVipLevel;
	}

	/**
	 * @return the isQQYearVip
	 */
	public boolean isQQYearVip() {
		return isQQYearVip;
	}
	
	
}

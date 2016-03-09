package com.youxigu.dynasty2.openapi.domain;

/**
 * 文件名    QQScoreReport.java
 *
 * 描  述    qq得分上报信息
 *
 * 时  间    2014-9-5
 *
 * 作  者    huhaiquan
 *
 * 版  本   v0.4  
 */
public class QQScoreReport {
	public static final int TYPE_LEVEL = 1;
	public static final int TYPE_MONEY = 2;
	public static final int TYPE_SCORE = 3;
	public static final int TYPE_EXP = 4;
	public static final int TYPE_HST_SCORE = 5;
	public static final int TYPE_PRE_WEEK_FINAL_RANK = 6;
	public static final int TYPE_CHALLENGE_SCORE = 7;
//	{
//	    0   optional     int             type;    
//	    1   optional     string          data;    
//	    2   optional     string          expires; 
//	    3   optional     int             bcover;  
//	};
//	type:1:LEVEL（等级），2:MONEY（金钱）, 3:SCORE（得分）, 4:EXP（经验）, 5:HST_SCORE(历史最高分)，6:PRE_WEEK_FINAL_RANK(上周数据结算排名,注意结算数据应该在下次结算前过期，否则拉取到过期数据)， 7：CHALLENGE_SCORE（pk流水数据，登录时不报，每一局都报） 传对应数字,一一对应，千万不要传错 
//	data:成就值
//	expireds:超时时间，unix时间戳，单位s，表示哪个时间点数据过期，0时标识永不超时，不传递则默认为0
//	bcover:1表示覆盖上报，本次上报会覆盖以前的数据，不传递或者传递其它值表示增量上报，只会记录比上一次更高的数据

	private int type;
	private String data;
	
	private String expireds;
	
	private int bcover;

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the data
	 */
	public String getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(String data) {
		this.data = data;
	}

	/**
	 * @return the expireds
	 */
	public String getExpireds() {
		return expireds;
	}

	/**
	 * @param expireds the expireds to set
	 */
	public void setExpireds(String expireds) {
		this.expireds = expireds;
	}

	/**
	 * @return the bcover
	 */
	public int getBcover() {
		return bcover;
	}

	/**
	 * @param bcover the bcover to set
	 */
	public void setBcover(int bcover) {
		this.bcover = bcover;
	}
	
	
}

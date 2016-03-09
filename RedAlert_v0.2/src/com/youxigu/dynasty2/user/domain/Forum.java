package com.youxigu.dynasty2.user.domain;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 文件名    Forum.java
 *
 * 描  述    论坛
 *
 * 时  间    2014-12-12
 *
 * 作  者    huhaiquan
 *
 * 版  本    v1.5
 */
public class Forum implements Serializable{
	/**
	 * 唯一id，自增
	 */
	private int forumId;
	/**
	 * //1，天子公告，2，上周热帖，3，本周热帖
	 */
	private int forumType;
	/**
	 * 标题
	 */
	private String title;
	
	/**
	 * 内容
	 */
	private String forumContext;
	
	/**
	 * 时间
	 */
	private Timestamp lastTime;
	private int countryId;
	private String tianZiName="";
	
	
	

	public int getForumId() {
		return forumId;
	}

	public void setForumId(int forumId) {
		this.forumId = forumId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getForumContext() {
		return forumContext;
	}

	public void setForumContext(String forumContext) {
		this.forumContext = forumContext;
	}

	public Timestamp getLastTime() {
		return lastTime;
	}

	public void setLastTime(Timestamp lastTime) {
		this.lastTime = lastTime;
	}


	public int getForumType() {
		return forumType;
	}

	public void setForumType(int forumType) {
		this.forumType = forumType;
	}

	public String getTianZiName() {
		return tianZiName;
	}

	public void setTianZiName(String tianZiName) {
		this.tianZiName = tianZiName;
	}

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}
	
	

}

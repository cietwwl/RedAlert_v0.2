package com.youxigu.dynasty2.rank.domain;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 打塔排行
 * @author Dagangzi
 *
 */
public class TowerRank implements Serializable {
    private static final long serialVersionUID = -8288289976501243833L;
	private int rankId;
	private long userId;//君主id
	private String userName;//君主名
	private int userLv;//君主等级
	private int countryId;//国家
	private long guildId;
	private String guildName;
	private int topStageId;//最高挑战记录
	private Timestamp topDttm;//最高挑战记录对应时间
	public int getRankId() {
		return rankId;
	}
	public void setRankId(int rankId) {
		this.rankId = rankId;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getUserLv() {
		return userLv;
	}
	public void setUserLv(int userLv) {
		this.userLv = userLv;
	}
	public int getCountryId() {
		return countryId;
	}
	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}
	public int getTopStageId() {
		return topStageId;
	}
	public void setTopStageId(int topStageId) {
		this.topStageId = topStageId;
	}
	public Timestamp getTopDttm() {
		return topDttm;
	}
	public void setTopDttm(Timestamp topDttm) {
		this.topDttm = topDttm;
	}
	public long getGuildId() {
		return guildId;
	}
	public void setGuildId(long guildId) {
		this.guildId = guildId;
	}
	public String getGuildName() {
		return guildName;
	}
	public void setGuildName(String guildName) {
		this.guildName = guildName;
	}
	
}

package com.youxigu.dynasty2.tower.domain;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 用户打塔记录
 */
public class TowerUser implements Serializable {
    private static final long serialVersionUID = -1557336208754102926L;
	// 加入塔内状态
	public static final int JOIN_STATUS_FREE = 0;// 未进入作战试验中心
	public static final int JOIN_STATUS_JOIN = 1;// 已进入作战试验中心

	// 战斗状态
	public static final int COMBAT_STATUS_FREE = 0;// 空闲状态
	public static final int COMBAT_STATUS_JOIN = 1;// 战斗中，

	private long userId;// PK
	private int stageId;// 当前阵关ID
    private int joinStatus;// 0:不在嗒内 1：在嗒内
	private int reliveTimes;// 可重生次数
	private int freeJoinTime;// 免费进入的次数
	private int itemJoinTime;// 道具进入的次数
	private Timestamp lastJoinDttm;// 上一次进入打塔活动的时间

	private int topStageId;// 最高挑战记录
	private Timestamp topDttm;// 最高挑战记录对应时间
	private int score;// 上一场得分
	private int winItemId;// 道具奖励
	private String seasonWinItemId;// 本轮获得的道具奖励 stageId-itemId,
	private String combatId;// 最后一场战斗的ID

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getStageId() {
        return stageId;
    }

    public void setStageId(int stageId) {
        this.stageId = stageId;
    }

    public int getJoinStatus() {
        return joinStatus;
    }

    public void setJoinStatus(int joinStatus) {
        this.joinStatus = joinStatus;
    }

    public int getReliveTimes() {
        return reliveTimes;
    }

    public void setReliveTimes(int reliveTimes) {
        this.reliveTimes = reliveTimes;
    }

    public int getFreeJoinTime() {
        return freeJoinTime;
    }

    public void setFreeJoinTime(int freeJoinTime) {
        this.freeJoinTime = freeJoinTime;
    }

    public int getItemJoinTime() {
        return itemJoinTime;
    }

    public void setItemJoinTime(int itemJoinTime) {
        this.itemJoinTime = itemJoinTime;
    }

    public Timestamp getLastJoinDttm() {
        return lastJoinDttm;
    }

    public void setLastJoinDttm(Timestamp lastJoinDttm) {
        this.lastJoinDttm = lastJoinDttm;
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getWinItemId() {
        return winItemId;
    }

    public void setWinItemId(int winItemId) {
        this.winItemId = winItemId;
    }

    public String getSeasonWinItemId() {
        return seasonWinItemId;
    }

    public void setSeasonWinItemId(String seasonWinItemId) {
        this.seasonWinItemId = seasonWinItemId;
    }

    public String getCombatId() {
        return combatId;
    }

    public void setCombatId(String combatId) {
        this.combatId = combatId;
    }

}

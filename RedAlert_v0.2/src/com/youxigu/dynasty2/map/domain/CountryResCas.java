package com.youxigu.dynasty2.map.domain;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 占领的国家资源点
 */
public class CountryResCas implements Serializable {
    private int mapCellId;
    private int resCasId;
    private long currentCountryId;  //当前所属国家，为0表示还没有国家占领
    private String currentCountryName;
    private long currentGuildId;    //当前占领联盟Id
    private String currentGuildName;
    private long firstGuildId;  //首次占领联盟Id
    private String firstGuildName;
    private Timestamp occupyTime; //占领时间点
    private int casHp; //资源点耐久值
    //todo：增加npc防守列表存储方案

    public int getMapCellId() {
        return mapCellId;
    }

    public void setMapCellId(int mapCellId) {
        this.mapCellId = mapCellId;
    }

    public int getResCasId() {
        return resCasId;
    }

    public void setResCasId(int resCasId) {
        this.resCasId = resCasId;
    }

    public long getCurrentCountryId() {
        return currentCountryId;
    }

    public void setCurrentCountryId(long currentCountryId) {
        this.currentCountryId = currentCountryId;
    }

    public String getCurrentCountryName() {
        return currentCountryName;
    }

    public void setCurrentCountryName(String currentCountryName) {
        this.currentCountryName = currentCountryName;
    }

    public long getCurrentGuildId() {
        return currentGuildId;
    }

    public void setCurrentGuildId(long currentGuildId) {
        this.currentGuildId = currentGuildId;
    }

    public String getCurrentGuildName() {
        return currentGuildName;
    }

    public void setCurrentGuildName(String currentGuildName) {
        this.currentGuildName = currentGuildName;
    }

    public long getFirstGuildId() {
        return firstGuildId;
    }

    public void setFirstGuildId(long firstGuildId) {
        this.firstGuildId = firstGuildId;
    }

    public String getFirstGuildName() {
        return firstGuildName;
    }

    public void setFirstGuildName(String firstGuildName) {
        this.firstGuildName = firstGuildName;
    }

    public Timestamp getOccupyTime() {
        return occupyTime;
    }

    public void setOccupyTime(Timestamp occupyTime) {
        this.occupyTime = occupyTime;
    }

    public int getCasHp() {
        return casHp;
    }

    public void setCasHp(int casHp) {
        this.casHp = casHp;
    }
}

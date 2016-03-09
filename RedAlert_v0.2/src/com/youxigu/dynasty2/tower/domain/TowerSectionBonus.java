package com.youxigu.dynasty2.tower.domain;

/**
 * 作战试验中心楼层区间保底奖励定义。清剿时，根据用户当前所处的楼层在哪个区间，给用户发放保底奖励
 */
public class TowerSectionBonus {
    private int id;//主键
    private int startStage;//开始楼层，包含。如值为1，包含1层
    private int endStage;//结束楼层，包含。如值为9，包含9层
    private int dropEntId;//掉落包entId

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStartStage() {
        return startStage;
    }

    public void setStartStage(int startStage) {
        this.startStage = startStage;
    }

    public int getEndStage() {
        return endStage;
    }

    public void setEndStage(int endStage) {
        this.endStage = endStage;
    }

    public int getDropEntId() {
        return dropEntId;
    }

    public void setDropEntId(int dropEntId) {
        this.dropEntId = dropEntId;
    }
}

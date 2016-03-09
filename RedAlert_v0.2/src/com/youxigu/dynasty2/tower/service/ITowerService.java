package com.youxigu.dynasty2.tower.service;

import com.youxigu.dynasty2.npc.domain.NPCDefine;
import com.youxigu.dynasty2.tower.domain.Tower;
import com.youxigu.dynasty2.tower.domain.TowerUser;
import com.youxigu.dynasty2.user.domain.User;

import java.util.List;
import java.util.Map;

public interface ITowerService {

    TowerUser doGetAndUpdateTowerUser(long userId);

    //list内容：
    //0：towerUser
    //1：Map<Integer, Integer> 扫荡获得的物品。key为entId，value为num
    Map<Integer, Integer> doEnter(User user);

    void doExit(long userId);

    // 打塔次数
    int getTotalFreeTime(long userId);

    // 打塔经验加成
    int getHeroExpEffect(long userId);

    int getTotalItemTime(long userId);

    //	@Override
    TowerUser doCombat(long userId, NPCDefine npc,
                       Map<String, Object> params);

    int getNextKeyStageBonusId(int stageId);

    void validateUser(User user);

    int getReliveUpperLimit();

    Tower getTower(int stageId);
}

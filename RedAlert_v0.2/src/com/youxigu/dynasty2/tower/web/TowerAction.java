package com.youxigu.dynasty2.tower.web;

import com.youxigu.dynasty2.chat.proto.CommonHead;
import com.youxigu.dynasty2.combat.domain.Combat;
import com.youxigu.dynasty2.combat.service.ICombatService;
import com.youxigu.dynasty2.core.flex.amf.BaseAction;
import com.youxigu.dynasty2.entity.domain.DroppedEntity;
import com.youxigu.dynasty2.hero.service.ITroopService;
import com.youxigu.dynasty2.npc.domain.NPCDefine;
import com.youxigu.dynasty2.rank.domain.TowerRank;
import com.youxigu.dynasty2.tower.domain.Tower;
import com.youxigu.dynasty2.tower.domain.TowerUser;
import com.youxigu.dynasty2.tower.proto.TowerMsg.*;
import com.youxigu.dynasty2.tower.service.ITowerService;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.user.service.IUserService;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.wolf.net.Response;
import com.youxigu.wolf.net.UserSession;

import java.util.*;

public class TowerAction extends BaseAction{
    private static final int RANK_ITEM_COUNT = 3;
    private ITowerService towerService;
	private IUserService userService;
    //	private IRankService rankService;

	public void setTowerService(ITowerService towerService) {
		this.towerService = towerService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

//	public void setRankService(IRankService rankService) {
//		this.rankService = rankService;
//	}

    /**
     * 获取重楼信息-52001
     * @param params
     * @param context
     * @return
     */
    public Object getTowerInfo(Object params, Response context) {
		UserSession session = getUserSession(context);

        long userId = session.getUserId();
		User user = userService.getUserById(userId);

		towerService.validateUser(user);

		TowerUser towerUser = towerService.doGetAndUpdateTowerUser(user.getUserId());
        int freeJoinLimit = towerService.getTotalFreeTime(userId);
        int itemJoinLimit = towerService.getTotalItemTime(userId);

        //todo: 加入排行功能后修改
        List<TowerRank> rankList = new ArrayList<TowerRank>();
        int myOrder = 0;

        Response52001Define.Builder response = Response52001Define.newBuilder();
        response.setResponseHead(getResponseHead(52001));
        response.setMyMaxStage(towerUser.getTopStageId());
        response.setMyOrder(myOrder);
        response.setFreeJoinLimit(freeJoinLimit);
        response.setItemJoinLimit(itemJoinLimit);
        response.setFreeJoinTimes(towerUser.getFreeJoinTime());
        response.setItemJoinTimes(towerUser.getItemJoinTime());
        response.setCurrentStatus(towerUser.getJoinStatus());

        for (int i = 0; i < RANK_ITEM_COUNT; i++) {
            if (i < rankList.size()) {
                TowerRankingItem.Builder trib = TowerRankingItem.newBuilder();
                TowerRank rank = rankList.get(i);
                trib.setUserId(rank.getUserId());
                trib.setOrder(i);
                trib.setUserName(rank.getUserName());
                trib.setMaxStage(rank.getTopStageId());
                response.addRanking(trib);
            }
        }

        return response.build();
    }

    /**
     * 进入重楼-52003
     * @param params
     * @param context
     * @return
     */
    public Object enter(Object params, Response context) {
		UserSession session = getUserSession(context);

        long userId = session.getUserId();
        User user = userService.getUserById(userId);

		towerService.validateUser(user);

        Map<Integer, Integer> items = towerService.doEnter(user);
        TowerUser towerUser = towerService.doGetAndUpdateTowerUser(userId);

        int stageId = towerUser.getStageId();
        Tower tower = towerService.getTower(stageId);
        if(tower == null){
            throw new BaseException("作战实验室定义错误");
        }

        NPCDefine npc = tower.getNpc();
        if(npc == null){
            throw new BaseException("作战实验室未定义防守方");
        }

        Response52003Define.Builder response = Response52003Define.newBuilder();
        response.setResponseHead(getResponseHead(52003));
        response.setNpcId(npc.getNpcId());
        response.setNpcName(npc.getNpcName());
        response.setStage(stageId);
        response.setFirstBonusId(tower.getFirstBonusId());
        response.setNextKeyStageBonusId(towerService.getNextKeyStageBonusId(stageId));
        response.setReliveLimit(towerService.getReliveUpperLimit());
        response.setReliveTimes(towerUser.getReliveTimes());
        if (items.size() > 0) {
            for (Map.Entry<Integer, Integer> item : items.entrySet()) {
                CommonHead.ItemInfo.Builder iib = CommonHead.ItemInfo.newBuilder();
                iib.setEntId(item.getKey());
                iib.setNum(item.getValue());
                response.addWipeOutBonus(iib);
            }
        }

        return response.build();
    }

     /**
     * 挑战-52005
     * @param params
     * @param context
     * @return
     */
    public Object combat(Object params, Response context) {
		UserSession session = getUserSession(context);

        long userId = session.getUserId();
        User user = userService.getUserById(userId);

		towerService.validateUser(user);

        Map<String, Object> ctx = new HashMap<String, Object>();
        TowerUser towerUser = towerService.doCombat(userId, null, ctx);

        Collection<DroppedEntity> bonuses = (Collection<DroppedEntity>) ctx.get("bonus");
        Combat combat = (Combat)ctx.get("combat");
        if(combat == null){
            throw new BaseException("战斗数据错误");
        }

        Response52005Define.Builder response = Response52005Define.newBuilder();
        response.setResponseHead(getResponseHead(52005));
        response.setStageId(towerUser.getStageId());
        response.setScore(towerUser.getScore());
        response.setCombat(combat.toCombat());
        if(bonuses != null && bonuses.size()>0){
            for (DroppedEntity de : bonuses){
                CommonHead.ItemInfo.Builder iib = CommonHead.ItemInfo.newBuilder();
                iib.setEntId(de.getEntId());
                iib.setNum(de.getNum());
                response.addBonuses(iib);
            }
        }

        return response.build();
    }

     /**
     * 退出重楼-52007
     * @param params
     * @param context
     * @return
     */
    public Object exit(Object params, Response context) {
		UserSession session = getUserSession(context);

        towerService.doExit(session.getUserId());

        Response52007Define.Builder response = Response52007Define.newBuilder();
        response.setResponseHead(getResponseHead(52007));
        return response.build();
    }

}

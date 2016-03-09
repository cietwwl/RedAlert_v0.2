package com.youxigu.dynasty2.develop.web;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.youxigu.dynasty2.chat.proto.CommonHead;
import com.youxigu.dynasty2.common.AppConstants;
import com.youxigu.dynasty2.common.service.ICommonService;
import com.youxigu.dynasty2.core.flex.amf.BaseAction;
import com.youxigu.dynasty2.develop.domain.Castle;
import com.youxigu.dynasty2.develop.domain.CastleBuilder;
import com.youxigu.dynasty2.develop.domain.CastleBuilding;
import com.youxigu.dynasty2.develop.proto.DevelopMsg;
import com.youxigu.dynasty2.develop.service.ICastleService;
import com.youxigu.dynasty2.entity.domain.enumer.ItemType;
import com.youxigu.dynasty2.entity.domain.enumer.SpeedUpItemType;
import com.youxigu.dynasty2.entity.service.IEntityService;
import com.youxigu.dynasty2.log.ILogService;
import com.youxigu.dynasty2.tips.domain.BuffTip;
import com.youxigu.dynasty2.tips.service.IBuffTipService;
import com.youxigu.dynasty2.treasury.domain.Treasury;
import com.youxigu.dynasty2.treasury.service.ITreasuryService;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.user.service.IUserService;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.dynasty2.vip.service.IVipService;
import com.youxigu.wolf.net.Response;
import com.youxigu.wolf.net.UserSession;

/**
 * 客户端内政数据的通信类
 *
 * @author Dagangzi
 */
public class CastleAction extends BaseAction {
    public static final Logger log = LoggerFactory.getLogger(CastleAction.class);

    private ICastleService castleService;
    private IUserService userService;
    private IEntityService entityService;
    private ICommonService commonService;
    private IVipService vipService;
    private ILogService tlogService;
    private ITreasuryService treasuryService;
    private IBuffTipService buffTipService;

    public void setTreasuryService(ITreasuryService treasuryService) {
        this.treasuryService = treasuryService;
    }

    public void setCommonService(ICommonService commonService) {
        this.commonService = commonService;
    }

    public void setVipService(IVipService vipService) {
        this.vipService = vipService;
    }

    public void setTlogService(ILogService tlogService) {
        this.tlogService = tlogService;
    }

    public void setCastleService(ICastleService castleService) {
        this.castleService = castleService;
    }

    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    public void setEntityService(IEntityService entityService) {
        this.entityService = entityService;
    }

    public void setBuffTipService(IBuffTipService buffTipService) {
        this.buffTipService = buffTipService;
    }

    /**
     * 获取城内建筑列表-20001
     *
     * @param msg
     * @param context
     * @return
     */
    public Object getCastleBuildings(Object msg, Response context) {
        DevelopMsg.Request20001Define req = (DevelopMsg.Request20001Define) msg;
        // 输出
        UserSession us = getUserSession(context);
        long userId = us.getUserId();

        Castle castle = castleService.getCastleByUserId(userId);
        if (castle == null) {
            throw new BaseException("用户城市不存在");
        }
        long casId = castle.getCasId();
//        if (req.getCasId() != casId) {
//            throw new BaseException("用户城市错误");
//        }

        List<CastleBuilding> buildings = castleService.doGetCastleBuildingsByCasId(casId);
        DevelopMsg.Response20001Define.Builder res = DevelopMsg.Response20001Define.newBuilder();

        //获取每个建筑当前是否满足下一等级升级条件，及下一等级升级时间（含效果加成），一并返回
        for (CastleBuilding castleBuilding : buildings) {
            DevelopMsg.CastleBuilding dsb = getCastleBuildingMsg(casId, castleBuilding);
            res.addCasBuildings(dsb);
        }

        List<CastleBuilder> builders = castleService.getCastleBuilders(userId);
        for (CastleBuilder builder : builders) {
            DevelopMsg.BuilderInfo bi = builder.toMsg();
            res.addAllBuilderInfo(bi);
        }

        res.setResponseHead(getResponseHead(req.getCmd()));
        return res.build();
    }

    /**
     * 升级建筑-20003
     *
     * @param msg
     * @param context
     * @return
     */
    public Object upgradeBuilding(Object msg, Response context) {
        DevelopMsg.Request20003Define req = (DevelopMsg.Request20003Define) msg;
        // 输出
        UserSession us = getUserSession(context);
        long userId = us.getUserId();

        Castle castle = castleService.getCastleByUserId(userId);
        if (castle == null) {
            throw new BaseException("用户城市不存在");
        }
        long casId = castle.getCasId();
//        if (req.getCasId() != casId) {
//            throw new BaseException("用户城市错误");
//        }

        long castleBuildingId = req.getBuildingId();
        int builderIndex = req.getBuilder();

        CastleBuilding cb = castleService.doUpgradeCastleBuilding(castle, builderIndex, castleBuildingId, false);

        DevelopMsg.Response20003Define.Builder res = DevelopMsg.Response20003Define.newBuilder();
        res.setCasId(casId);

        DevelopMsg.CastleBuilding dsb = getCastleBuildingMsg(casId, cb);
        res.setCasBuilding(dsb);

        List<CastleBuilder> builders = castleService.getCastleBuilders(userId);
        for (CastleBuilder builder : builders) {
            DevelopMsg.BuilderInfo bi = builder.toMsg();
            res.addAllBuilderInfo(bi);
        }

        res.setResponseHead(getResponseHead(req.getCmd()));
        return res.build();
    }

    private DevelopMsg.CastleBuilding getCastleBuildingMsg(long casId, CastleBuilding cb) {
        long castleBuildingId = cb.getCasBuiId();
        int upgradeSatisfied = castleService.validateBuildingUpgradeCondition(casId, castleBuildingId);
        int upgradeTime = castleService.getBuildingNextLevelUpgradeTime(casId, castleBuildingId);
        DevelopMsg.ConditionStatus condition = DevelopMsg.ConditionStatus.UNSATISFIED;
        if (upgradeSatisfied == AppConstants.UPGRADE_SATISFIED) {
            condition = DevelopMsg.ConditionStatus.SATISFIED;
        } else if (upgradeSatisfied == AppConstants.UPGRADE_UNSATISFIED) {
            condition = DevelopMsg.ConditionStatus.UNSATISFIED;
        }
        return cb.toMsg(upgradeTime, condition);
    }

    /**
     * 取消在建建筑-20005
     *
     * @param msg
     * @param context
     * @return
     */
    public Object cancelBuilding(Object msg, Response context) {
        DevelopMsg.Request20005Define req = (DevelopMsg.Request20005Define) msg;
        UserSession us = getUserSession(context);
        long userId = us.getUserId();

        Castle castle = castleService.getCastleByUserId(userId);
        if (castle == null) {
            throw new BaseException("用户城市不存在");
        }
        long casId = castle.getCasId();
//        if (req.getCasId() != casId) {
//            throw new BaseException("用户城市错误");
//        }

        long castleBuildingId = req.getBuildingId();

        CastleBuilding cb = castleService.doCancelBuilding(castle, castleBuildingId);

        DevelopMsg.Response20005Define.Builder res = DevelopMsg.Response20005Define.newBuilder();
        res.setCasId(casId);

        DevelopMsg.CastleBuilding dsb = getCastleBuildingMsg(casId, cb);
        res.setCasBuilding(dsb);

        List<CastleBuilder> builders = castleService.getCastleBuilders(userId);
        for (CastleBuilder builder : builders) {
            DevelopMsg.BuilderInfo bi = builder.toMsg();
            res.addAllBuilderInfo(bi);
        }

        res.setResponseHead(getResponseHead(req.getCmd()));
        return res.build();
    }

    /**
     * 获取用户当前所有的建筑加速道具
     * @param msg
     * @param context
     * @return
     */
    public Object getBuildingSpeedupItems(Object msg, Response context) {
        DevelopMsg.Request20009Define req = (DevelopMsg.Request20009Define) msg;

        UserSession us = getUserSession(context);
        long userId = us.getUserId();

        List<Treasury> buildingSpeedupItems = treasuryService.getUserItemByItemTypeAndChildType(
                userId, ItemType.ITEM_TYPE_SPEED_UP.getType(),
                SpeedUpItemType.BUILDING_SPEED_UP_TYPE.getType());
        List<Treasury> generalSpeedupItems = treasuryService.getUserItemByItemTypeAndChildType(
                userId, ItemType.ITEM_TYPE_SPEED_UP.getType(),
                SpeedUpItemType.TIMER_SPEED_UP_TYPE.getType());

        DevelopMsg.Response20009Define.Builder res = DevelopMsg.Response20009Define.newBuilder();
        res.setResponseHead(getResponseHead(req.getCmd()));

        for (Treasury treasury : buildingSpeedupItems) {
            CommonHead.ItemInfo.Builder ii = CommonHead.ItemInfo.newBuilder();
            ii.setEntId(treasury.getEntId());
            ii.setNum(treasury.getItemCount());
            res.addItems(ii);
        }
        for (Treasury treasury : generalSpeedupItems) {
            CommonHead.ItemInfo.Builder ii = CommonHead.ItemInfo.newBuilder();
            ii.setEntId(treasury.getEntId());
            ii.setNum(treasury.getItemCount());
            res.addItems(ii);
        }

        return res.build();
    }

    /**
     * 加速建筑升级-20011
     *
     * @param msg
     * @param context
     * @return
     */
    public Object speedupCastleBuilding(Object msg, Response context) {
        DevelopMsg.Request20011Define req = (DevelopMsg.Request20011Define)msg;
        long casId = req.getCasId();
        long casBuildingId = req.getBuildingId();
        int speedUpType = req.getSpeedUpType().getNumber();
        int entId = req.hasEntId() ? req.getEntId() : 0;
        int num = req.hasNum() ? req.getNum() : 0;

        if (num < 0) {
            throw new BaseException("道具数量错误。");
        }

        UserSession us = super.getUserSession(context);
        long userId = us.getUserId();
        Castle castle = castleService.getCastleByUserId(userId);
        if (castle == null) {
            throw new BaseException("用户城市不存在");
        }
//        if (req.getCasId() != castle.getCasId()) {
//            throw new BaseException("用户城市错误");
//        }

        if (req.getSpeedUpType() == DevelopMsg.Request20011Define.SpeedUpType.ITEM) {
            if ((!req.hasNum()) || (!req.hasEntId())) {
                throw new BaseException("道具加速必须指定使用的道具和数量");
            }
        }

        CastleBuilding cb = castleService.doSpeedupUpgradeCastleBuilding(us
                .getUserId(), casId, casBuildingId, speedUpType, entId, num);

        DevelopMsg.Response20011Define.Builder res = DevelopMsg.Response20011Define.newBuilder();
        res.setCasId(casId);

        DevelopMsg.CastleBuilding dsb = getCastleBuildingMsg(casId, cb);
        res.setCasBuilding(dsb);

        List<CastleBuilder> builders = castleService.getCastleBuilders(userId);
        for (CastleBuilder builder : builders) {
            DevelopMsg.BuilderInfo bi = builder.toMsg();
            res.addAllBuilderInfo(bi);
        }

        res.setResponseHead(getResponseHead(req.getCmd()));
        return res.build();
    }

    /**
     * 获取建筑队列信息-20013
     *
     * @param msg
     * @param context
     * @return
     */
    public Object getAllBuilderInfo(Object msg, Response context) {
        DevelopMsg.Request20013Define req = (DevelopMsg.Request20013Define) msg;
        // 输出
        UserSession us = getUserSession(context);
        long userId = us.getUserId();

        Castle castle = castleService.getCastleByUserId(userId);
        if (castle == null) {
            throw new BaseException("用户城市不存在");
        }
        long casId = castle.getCasId();
//        if (req.getCasId() != casId) {
//            throw new BaseException("用户城市错误");
//        }

        DevelopMsg.Response20013Define.Builder res = DevelopMsg.Response20013Define.newBuilder();
        res.setResponseHead(getResponseHead(req.getCmd()));

        List<CastleBuilder> builders = castleService.getCastleBuilders(userId);
        for (CastleBuilder builder : builders) {
            DevelopMsg.BuilderInfo bi = builder.toMsg();
            res.addAllBuilderInfo(bi);
        }
        return res.build();
    }

    /**
     * 开启自动建造-20015
     *
     * @param msg
     * @param context
     * @return
     */
    public Object openAutoBuild(Object msg, Response context) {
        UserSession us = getUserSession(context);

        //todo: VIP功能完成后开启以下代码
//        int vipLv = vipService.getVipLvByUserId(us.getUserId());
//        int vipOpenLv = commonService.getSysParaIntValue(AppConstants.AUTO_BUILD_OPEN_VIP_LEVEL, 4);
//        if (vipLv < vipOpenLv) {
//            throw new BaseException("自动建造需要VIP等级" + vipOpenLv + "级开启。");
//        }

        DevelopMsg.Request20015Define req = (DevelopMsg.Request20015Define)msg;
        List<Long> castleBuildingIds = req.getBuildingIdsList();
        if(castleBuildingIds == null){
            castleBuildingIds = new ArrayList<Long>();
        }
        long casId = us.getMainCasId();
        List<CastleBuilding> cbs = castleService.doAutoUpgradeCastleBuilding(casId, castleBuildingIds);

        DevelopMsg.Response20015Define.Builder res = DevelopMsg.Response20015Define.newBuilder();
        res.setResponseHead(getResponseHead(req.getCmd()));

        for(CastleBuilding cb:cbs){
            res.addCasBuildingList(getCastleBuildingMsg(casId, cb));
        }

        List<CastleBuilder> builders = castleService.getCastleBuilders(us.getUserId());
        for (CastleBuilder builder : builders) {
            DevelopMsg.BuilderInfo bi = builder.toMsg();
            res.addAllBuilderInfo(bi);
        }
       return res.build();
    }

    /**
     * 获取城池Buff信息 20031
     * @param msg
     * @param context
     * @return
     */
    public Object getBuffTips(Object msg, Response context) {
        DevelopMsg.Request20031Define req = (DevelopMsg.Request20031Define) msg;
        long casId = req.getCastleId();

        UserSession us = getUserSession(context);
        long userId = us.getUserId();
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new BaseException("指挥官不存在。");
        }
        if (user.getMainCastleId() != casId) {
            throw new BaseException("错误的城池ID。");
        }

        DevelopMsg.Response20031Define.Builder res = DevelopMsg.Response20031Define.newBuilder();
        res.setResponseHead(getResponseHead(req.getCmd()));
        res.setCastleId(casId);

        List<BuffTip> tips = buffTipService.deleteAndGetBuffTipsByUserId(userId);
        if (tips != null) {
            for (BuffTip buffTip : tips) {
                //如果需要禁言功能，解除以下代码的注释
//                if (BuffDefine.FORBIDTALK.equals(buffTip.getBuffType())) {
//                    //加入到禁言名单
//                    long now = System.currentTimeMillis();
//                    messageService.disableChat(userId, (buffTip.getEndTime().getTime() - now) / 1000, false);
//                }

                res.addBuffInfoList(buffTip.toMsg());
            }
        }

        return res.build();
    }

    /**
     * 激活Buff 20033
     * @param msg
     * @param context
     * @return
     */
    public Object activateBuff(Object msg, Response context) {
        DevelopMsg.Request20033Define req = (DevelopMsg.Request20033Define) msg;
        long casId = req.getCastleId();

        UserSession us = getUserSession(context);
        long userId = us.getUserId();
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new BaseException("指挥官不存在。");
        }
        if (user.getMainCastleId() != casId) {
            throw new BaseException("错误的城池ID。");
        }

        int buffId = req.getBuffId();
        //todo: 商城功能完成之后，修改此处获取useCash字段的功能
        int useCash = 0;
//        int useCash = req.getUseCash();

        castleService.doActivateBuff(casId, buffId, useCash, us.getPfEx());

        DevelopMsg.Response20033Define.Builder res = DevelopMsg.Response20033Define.newBuilder();
        res.setResponseHead(getResponseHead(req.getCmd()));
        res.setCastleId(casId);

        List<BuffTip> tips = buffTipService.deleteAndGetBuffTipsByUserId(userId);
        if (tips != null) {
            for (BuffTip buffTip : tips) {
                res.addBuffInfoList(buffTip.toMsg());
            }
        }

        return res.build();
    }

    /**
     * 取消Buff 20035
     * @param msg
     * @param context
     * @return
     */
    public Object cancelBuff(Object msg, Response context) {
        DevelopMsg.Request20035Define req = (DevelopMsg.Request20035Define) msg;
        long casId = req.getCastleId();

        UserSession us = getUserSession(context);
        long userId = us.getUserId();
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new BaseException("指挥官不存在。");
        }
        if (user.getMainCastleId() != casId) {
            throw new BaseException("错误的城池ID。");
        }

        int buffId = req.getBuffId();

        castleService.doCancelBuff(userId, casId, buffId);

        DevelopMsg.Response20035Define.Builder res = DevelopMsg.Response20035Define.newBuilder();
        res.setResponseHead(getResponseHead(req.getCmd()));
        res.setCastleId(casId);

        List<BuffTip> tips = buffTipService.deleteAndGetBuffTipsByUserId(userId);
        if (tips != null) {
            for (BuffTip buffTip : tips) {
                res.addBuffInfoList(buffTip.toMsg());
            }
        }

        return res.build();
    }

    /**
     * 钻石购买资源 20037
     * @param msg
     * @param context
     * @return
     */
    public Object buyResource(Object msg, Response context) {
        DevelopMsg.Request20037Define req = (DevelopMsg.Request20037Define) msg;
        int resEntId = req.getResEntId();
        int cashNum = req.getCashNum();

        UserSession us = getUserSession(context);
        long userId = us.getUserId();

        castleService.doBuyResource(userId, resEntId, cashNum);

        DevelopMsg.Response20037Define.Builder res = DevelopMsg.Response20037Define.newBuilder();
        res.setResponseHead(getResponseHead(req.getCmd()));
        return res.build();
    }

    public static class CastleBuilderComparator implements Comparator<CastleBuilder> {
        @Override
        public int compare(CastleBuilder o1, CastleBuilder o2) {
            throw new BaseException("Not implemented.");
        }
    }

}

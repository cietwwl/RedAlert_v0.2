package com.youxigu.dynasty2.develop.web;

import com.youxigu.dynasty2.common.AppConstants;
import com.youxigu.dynasty2.common.service.ICommonService;
import com.youxigu.dynasty2.core.flex.amf.BaseAction;
import com.youxigu.dynasty2.develop.domain.Castle;
import com.youxigu.dynasty2.develop.domain.UserTechnology;
import com.youxigu.dynasty2.develop.proto.DevelopMsg;
import com.youxigu.dynasty2.develop.service.ICastleService;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.wolf.net.Response;
import com.youxigu.wolf.net.UserSession;

import java.util.*;

/**
 * 研发中心接口
 *
 * @author Dagangzi
 */
public class RdCenterAction extends BaseAction {
    private ICastleService castleService;
    private ICommonService commonService;

    public void setCommonService(ICommonService commonService) {
        this.commonService = commonService;
    }

    public void setCastleService(ICastleService castleService) {
        this.castleService = castleService;
    }

    /**
     * @return 研发中心功能开关
     */
    private boolean isOpenCastleAcade() {
        int opened = commonService.getSysParaIntValue(
                AppConstants.SYS_TECHNOLOGY_OPEN_STATUS,
                AppConstants.SYS_OPNE_STATUS_DEFAULTVALUE);
        return opened == 1;
    }

    /**
     * 获取已解锁科技-20041
     *
     * @param msg
     * @param context
     * @return
     */

    public Object getTechnology(Object msg, Response context) {
        if (!isOpenCastleAcade()) {
            throw new BaseException("研发中心功能暂未开放");
        }
        DevelopMsg.Request20041Define req = (DevelopMsg.Request20041Define) msg;
        // 输出
        UserSession us = getUserSession(context);
        long userId = us.getUserId();
        Castle castle = castleService.getCastleByUserId(userId);
        if (castle == null) {
            throw new BaseException("用户城市不存在");
        }

        List<UserTechnology> uts = castleService.doRefreshUserTechnologys(userId, castle.getCasId());

        DevelopMsg.Response20041Define.Builder res = DevelopMsg.Response20041Define.newBuilder();
        res.setResponseHead(getResponseHead(req.getCmd()));

        for (UserTechnology ut : uts) {
            int nextTimeSpan = castleService.getTechNextLevelUpgradeTime(castle.getCasId(),
                    userId, ut.getEntId());
            res.addUserTechList(ut.toMsg(nextTimeSpan));
        }

        return res.build();
    }

    /**
     * 升级科技- 20043
     *
     * @param msg
     * @param context
     * @return
     */
    public Object upgradeTechnology(Object msg, Response context) {
        if (!isOpenCastleAcade()) {
            throw new BaseException("研发中心功能暂未开放");
        }
        DevelopMsg.Request20043Define req = (DevelopMsg.Request20043Define) msg;
        // 输出
        UserSession us = getUserSession(context);
        long userId = us.getUserId();
        Castle castle = castleService.getCastleByUserId(userId);
        if (castle == null) {
            throw new BaseException("用户城市不存在");
        }
        int techEntId = req.getTechEntId();

        UserTechnology ut = castleService.doUpgradeUserTechnology(us
                .getMainCasId(), techEntId);

        DevelopMsg.Response20043Define.Builder res = DevelopMsg.Response20043Define.newBuilder();
        res.setResponseHead(getResponseHead(req.getCmd()));

        int nextTimeSpan = castleService.getTechNextLevelUpgradeTime(castle.getCasId(),
                userId, ut.getEntId());
        res.setUserTech(ut.toMsg(nextTimeSpan));

        return res.build();
    }

    /**
     * 加速科技升级-20045
     *
     * @param msg
     * @param context
     * @return
     */
    public Object fastUpgradeTechnology(Object msg, Response context) {
        if (!isOpenCastleAcade()) {
            throw new BaseException("研发中心功能暂未开放");
        }
        DevelopMsg.Request20045Define req = (DevelopMsg.Request20045Define) msg;
        // 输出
        UserSession us = getUserSession(context);
        long userId = us.getUserId();
        Castle castle = castleService.getCastleByUserId(userId);
        if (castle == null) {
            throw new BaseException("用户城市不存在");
        }
        int techEntId = req.getTechEntId();
        int speedUpType = req.getSpeedUpType();
        int itemEntId = req.getEntId();
        int num = req.getNum();

        UserTechnology ut = castleService.doFastUpgradeUserTechnology(castle.getCasId(),
                techEntId, speedUpType, itemEntId, num);

        DevelopMsg.Response20045Define.Builder res = DevelopMsg.Response20045Define.newBuilder();
        res.setResponseHead(getResponseHead(req.getCmd()));

        int nextTimeSpan = castleService.getTechNextLevelUpgradeTime(castle.getCasId(),
                userId, ut.getEntId());
        res.setUserTech(ut.toMsg(nextTimeSpan));

        return res.build();

    }

    /**
     * 取消科技升级-20047
     *
     * @param msg
     * @param context
     * @return
     */
    public Object cancelUpgradeTechnology(Object msg, Response context) {
        if (!isOpenCastleAcade()) {
            throw new BaseException("研发中心功能暂未开放");
        }
        DevelopMsg.Request20047Define req = (DevelopMsg.Request20047Define) msg;
        // 输出
        UserSession us = getUserSession(context);
        long userId = us.getUserId();
        Castle castle = castleService.getCastleByUserId(userId);
        if (castle == null) {
            throw new BaseException("用户城市不存在");
        }
        int techEntId = req.getTechEntId();

        UserTechnology ut = castleService.doCancelUpgradeUserTechnology(us
                .getMainCasId(), techEntId);

        DevelopMsg.Response20047Define.Builder res = DevelopMsg.Response20047Define.newBuilder();
        res.setResponseHead(getResponseHead(req.getCmd()));

        int nextTimeSpan = castleService.getTechNextLevelUpgradeTime(castle.getCasId(),
                userId, ut.getEntId());
        res.setUserTech(ut.toMsg(nextTimeSpan));

        return res.build();
    }
}

package com.youxigu.dynasty2.entity.service.validator.impl;

import java.util.List;
import java.util.Map;

import com.youxigu.dynasty2.develop.domain.Castle;
import com.youxigu.dynasty2.develop.domain.CastleBuilding;
import com.youxigu.dynasty2.develop.service.ICastleService;
import com.youxigu.dynasty2.entity.domain.Building;
import com.youxigu.dynasty2.entity.domain.EntityLimit;
import com.youxigu.dynasty2.entity.service.EntityLimitResult;
import com.youxigu.dynasty2.util.BaseException;

/**
 * 依赖建筑等级约束校验器
 */
public class BuildingLimitValidator extends DefaultEntityLimitValidator {
    private ICastleService castleService;

    public void setCastleService(ICastleService castleService) {
        this.castleService = castleService;
    }

    @Override
    public EntityLimitResult validate(EntityLimit limit, Map<String, Object> context) {
        if (limit.getNeedEntity() == null)// 没有依赖的条件
            return null;

        EntityLimitResult result = new EntityLimitResult(limit);

        Building building = (Building) limit.getNeedEntity();// 初始化依赖的建筑

        Castle castle = (Castle) context.get("castle");
        if (castle == null) {
            Object tmp = context.get("casId");
            if (tmp != null) {
                long casId = (Long) tmp;
                castle = castleService.getCastleById(casId);
                context.put("castle", castle);
            } else {
                throw new BaseException("param casIdnot found.");
            }
        }

        List<CastleBuilding> casBuis = null;// 初始化城内建筑
        casBuis = castleService.doGetCastleBuildingsByCasId(castle.getCasId());

        int maxLv = getMaxBuildingLevel(casBuis, building.getEntId());
        result.setActualNum(limit.getLeastNum());
        result.setActualLevel(maxLv);
        result.setNeedEntName(building.getBuiName());
        result.setNeedEntTypeDesc("建筑");
        if (log.isDebugEnabled()) {
            log.debug("需要[" + result.getNeedEntName() + "]等级=[" + result.getNeedLevel() + "]");
        }
        return result;
    }

    /**
     * 城内指定建筑类型的当前最大等级
     *
     * @param buildings 城内建筑
     * @param entId     指定建筑类型
     * @return
     */
    private int getMaxBuildingLevel(List<CastleBuilding> buildings, int entId) {
        int max = 0;
        if (buildings == null)
            return max;
        for (CastleBuilding building : buildings) {
            if (building.getBuiEntId() == entId) {
                if (building.getLevel() > max) {
                    max = building.getLevel();
                }
            }
        }
        return max;
    }
}

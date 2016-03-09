package com.youxigu.dynasty2.entity.service.validator.impl;

import java.util.Map;

import com.youxigu.dynasty2.core.flex.amf.IAMF3Action;
import com.youxigu.dynasty2.develop.domain.Castle;
import com.youxigu.dynasty2.develop.domain.CastleResource;
import com.youxigu.dynasty2.develop.service.ICastleResService;
import com.youxigu.dynasty2.develop.service.ICastleService;
import com.youxigu.dynasty2.entity.domain.EntityConsume;
import com.youxigu.dynasty2.entity.domain.Resource;
import com.youxigu.dynasty2.entity.service.EntityConsumeResult;
import com.youxigu.dynasty2.util.BaseException;

/**
 * 需要消耗的铁矿
 * 
 */
public class ResourceIronConsumeValidator extends
		DefaultEntityConsumeValidator {
    private ICastleService castleService;
    private ICastleResService castleResService;

    public void setCastleService(ICastleService castleService) {
        this.castleService = castleService;
    }

    public void setCastleResService(ICastleResService castleResService) {
        this.castleResService = castleResService;
    }

    public EntityConsumeResult validate(EntityConsume consume,
                                        Map<String, Object> context) {
        EntityConsumeResult result = new EntityConsumeResult(consume);
        Resource resource = (Resource) consume.getNeedEntity();

        // 初始化Castle
        Castle castle = (Castle) context.get("castle");
        if (castle==null){
            Object tmp = context.get("casId");
            if (tmp != null) {
                castle = castleService.getCastleById((Long)tmp);
            } else {
                throw new BaseException("param casId or userId not found.");
            }
            context.put("castle", castle);
        }

        //获取该城池对应的资源
        CastleResource casRes = castleResService.getCastleResourceById(castle.getCasId());

        //设置EntityConsumeResult并返回
        result.setNeedEntName(resource.getResName());
        result.setActualNum((int)casRes.getIronNum());
        return result;
    }

    public EntityConsumeResult validateAndUpdate(EntityConsume consume,
                                                 Map<String, Object> context) {
        EntityConsumeResult result = new EntityConsumeResult(consume);
        Resource resource = (Resource) consume.getNeedEntity();

        int num = 1;
        double factor = 1.0;

        Object tmp = context.get("num");
        if (tmp != null)
            num = (Integer) tmp;

        tmp = context.get("factor");
        if (tmp != null)
            factor = (Double) tmp;

        // 初始化主城
        Castle castle = (Castle) context.get("castle");
        if (castle==null){
            Object casId = context.get("casId");
            if (casId != null) {
                castle = castleService.getCastleById((Long)casId);
            } else {
                throw new BaseException("param casId or userId not found.");
            }
            context.put("castle", castle);
        }

        CastleResource casRes = castleResService.lockCasRes(castle.getCasId());

        int consumeNum = (int) (consume.getNeedEntNum() * num / factor);
        long realNum = casRes.getIronNum();
        result.setActualNum((int)realNum);
        if (log.isDebugEnabled()) {
            log.debug("消耗{}={}", resource.getResName(), consumeNum);
        }

        if (consumeNum > 0) {
            if (realNum - consumeNum < 0) {
                throw new BaseException(IAMF3Action.ERROR_MONEY, resource.getResName() + "不足，需要"
                        + consumeNum + "，当前拥有" + realNum);
            }
        }

        casRes.setIronNum(realNum - consumeNum);
        castleResService.updateCastleResource(casRes,castle.getUserId(), true);
        return result;
    }
}

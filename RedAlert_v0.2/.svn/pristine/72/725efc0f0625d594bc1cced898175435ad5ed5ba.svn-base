package com.youxigu.dynasty2.entity.service.validator.impl;

import com.youxigu.dynasty2.develop.domain.Castle;
import com.youxigu.dynasty2.develop.domain.UserTechnology;
import com.youxigu.dynasty2.develop.service.ICastleService;
import com.youxigu.dynasty2.entity.domain.EntityLimit;
import com.youxigu.dynasty2.entity.domain.Technology;
import com.youxigu.dynasty2.entity.service.EntityLimitResult;
import com.youxigu.dynasty2.util.BaseException;

import java.util.Map;

/**
 * 科技研发条件约束
 * 
 * @author Dagangzi
 * 
 */
public class TechnologyLimitValidator extends DefaultEntityLimitValidator {
	private ICastleService castleService;

	// private IUserService userService;

	public void setCastleService(ICastleService castleService) {
		this.castleService = castleService;
	}

	@Override
	public EntityLimitResult validate(EntityLimit limit,
			Map<String, Object> context) {

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

		if (limit.getNeedEntity() == null)// 没有条件约束
			return null;

		EntityLimitResult result = new EntityLimitResult(limit);

		Technology technology = (Technology) limit.getNeedEntity();

		result.setNeedEntName(technology.getTechName());
		result.setNeedEntTypeDesc("科技");
		
		
		UserTechnology ut = castleService.getUserTechnologyByEntId(castle.getUserId(), technology.getEntId());
		if (ut!=null){
			result.setActualLevel(ut.getLevel());			
		}
		
		if (log.isDebugEnabled()) {
			log.debug("需要[" + result.getNeedEntName() + "]等级=["
					+ result.getNeedLevel() + "]");
		}
		return result;
	}

}

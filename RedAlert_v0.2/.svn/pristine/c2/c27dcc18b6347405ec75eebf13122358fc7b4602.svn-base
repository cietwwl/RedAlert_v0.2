package com.youxigu.dynasty2.entity.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.youxigu.dynasty2.entity.domain.EntityLimit;
import com.youxigu.dynasty2.entity.service.EntityLimitResult;
import com.youxigu.dynasty2.entity.service.EntityLimitResultSet;
import com.youxigu.dynasty2.entity.service.IEntityLimitChecker;

public class DefaultEntityLimitChecker implements IEntityLimitChecker {
//	private ICastleService castleService;
//
//	// private IUserService userService;
//
//	public void setCastleService(ICastleService castleService) {
//		this.castleService = castleService;
//	}
//
//	public void init() {
//		if (castleService == null) {
//			throw new BaseException(
//					"DefaultEntityConsumeChecker must set castleService property");
//		}
//	}

	@Override
	public EntityLimitResultSet check(List<EntityLimit> limits,
			Map<String, Object> context) {

		if (limits == null || limits.size() == 0)
			return null;

//		Castle castle = (Castle) context.get("castle");
//		if (castle == null) {
//			long casId = 0;
//			Object tmp = context.get("casId");
//			if (tmp != null) {
//				casId = (Long) tmp;
//			}
//			if (casId > 0) {
//				castle = castleService.getCastleById(casId);
//				context.put("castle", castle);
//			}
//		}
//		if (castle != null) {
//			Castle mainCastle = null;
//			if (castle.getParentCasId() == 0) {
//				mainCastle = castle;
//			} else {
//				mainCastle = castleService.getCastleById(castle
//						.getParentCasId());
//			}
//			context.put("mainCastle", mainCastle);
//		}

		List<EntityLimitResult> results = new ArrayList<EntityLimitResult>();
		EntityLimitResultSet rs = new EntityLimitResultSet();
		rs.setResults(results);

		for (EntityLimit limit : limits) {

			EntityLimitResult result = limit.getValitor().validate(limit,
					context);
			if (result != null) {
				results.add(result);
				if (!result.isMatch())
					rs.setMatch(false);
			}
		}
		return rs;
	}

}

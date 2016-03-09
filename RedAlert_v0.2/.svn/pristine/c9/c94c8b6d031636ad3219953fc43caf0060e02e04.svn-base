package com.youxigu.dynasty2.entity.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.youxigu.dynasty2.entity.domain.EntityConsume;
import com.youxigu.dynasty2.entity.service.EntityConsumeResult;
import com.youxigu.dynasty2.entity.service.EntityConsumeResultSet;
import com.youxigu.dynasty2.entity.service.IEntityConsumeChecker;

/**
 * 实体消耗检查器实现类
 * 
 */
public class DefaultEntityConsumeChecker implements IEntityConsumeChecker {

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

	// 校验实体消耗，不更新
	public EntityConsumeResultSet check(List<EntityConsume> consumes,
			Map<String, Object> context) {

		return process(consumes, context, false);
	}

	// 校验实体消耗，并更新
	public EntityConsumeResultSet checkAndUpdate(List<EntityConsume> consumes,
			Map<String, Object> context) {
		return process(consumes, context, true);
	}

	private EntityConsumeResultSet process(List<EntityConsume> consumes,
			Map<String, Object> context, boolean update) {
		EntityConsumeResultSet rs = new EntityConsumeResultSet();
		if (consumes == null || consumes.size() == 0)// 没有需要消耗的物资
			return rs;
//		// ///////////处理城池参数
//		Castle castle = (Castle) context.get("castle");
//		long mainCasId = 0;
//		if (castle == null) {
//			long casId = 0;
//			Object tmp = context.get("casId");
//			if (tmp != null) {
//				casId = (Long) tmp;
//				castle = castleService.getCastleById(casId);
//				context.put("castle", castle);
//			}
//		}
//		if (castle != null) {
//			if (castle.getParentCasId() != 0) {
//				mainCasId = castle.getParentCasId();
//			} else {
//				mainCasId = castle.getCasId();
//			}
//			context.put("mainCasId", mainCasId);
//			context.put("userId", castle.getUserId());
//		} else {
//			User user = (User) context.get("user");
//			if (user != null) {
//				context.put("mainCasId", user.getMainCastleId());
//				context.put("userId", user.getUserId());
//			}
//		}

		List<EntityConsumeResult> results = new ArrayList<EntityConsumeResult>();

		rs.setResults(results);
		for (EntityConsume consume : consumes) {
			EntityConsumeResult result = null;
			if (update) {
				result = consume.getValidator().validateAndUpdate(consume,
						context);
			} else {
				result = consume.getValidator().validate(consume, context);
			}
			if (result != null) {
				results.add(result);
				if (!result.isMatch())
					rs.setMatch(false);
			}

		}
		return rs;
	}
}

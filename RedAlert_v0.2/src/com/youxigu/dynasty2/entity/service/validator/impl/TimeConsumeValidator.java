package com.youxigu.dynasty2.entity.service.validator.impl;

import java.util.Map;

import com.youxigu.dynasty2.entity.domain.EntityConsume;
import com.youxigu.dynasty2.entity.service.EntityConsumeResult;

public class TimeConsumeValidator extends DefaultEntityConsumeValidator {
	
//	private ICastleEffectService castleEffectService;
//	
//	public void setCastleEffectService(ICastleEffectService castleEffectService) {
//		this.castleEffectService = castleEffectService;
//	}

	@Override
	public EntityConsumeResult validate(EntityConsume consume,
			Map<String, Object> context) {

		return validateAndUpdate(consume, context);
	}

	@Override
	public EntityConsumeResult validateAndUpdate(EntityConsume consume,
			Map<String, Object> context) {
		EntityConsumeResult result = new EntityConsumeResult(consume);

		int num = 1;
		Object tmp = context.get("num");
		if (tmp != null)
			num = (Integer) tmp;
		// 时间永远是正数
		int lvTime = consume.getNeedEntNum() * Math.abs(num);
		//建筑，科技，城防的时间这里还是返回原始时间，实际消耗的时间再外部队列出计算
		result.setActualNum(lvTime);
		return result;
	}

}

package com.youxigu.dynasty2.entity.service.script;

import java.util.Map;

import com.youxigu.dynasty2.entity.domain.Entity;
import com.youxigu.dynasty2.entity.domain.effect.EffectDefine;
import com.youxigu.dynasty2.entity.service.IEffectRender;
import com.youxigu.dynasty2.log.imp.LogHpAct;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.user.service.IUserService;
import com.youxigu.dynasty2.util.BaseException;

/**
 * 增加体力 effTypeId: RISK_HP_POINT
 * 
 * @author Dagangzi
 * 
 */
public class RiskHpPointRender implements IEffectRender {

	private IUserService userService;

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	@Override
	public Map<String, Object> render(Entity entity, EffectDefine effectDefine,
			Map<String, Object> context) {
		User usr = (User) context.get("user");
		int num = 1;
		Object tmp = context.get("num");
		if (tmp != null)
			num = (Integer) tmp;
		// if (num > 1) {
		// throw new BaseException("该类道具不允许批量使用");
		// }
		int curHp = usr.getHpPoint();
		int hpPointLimit = userService.getHpPointMaxB();
		if (curHp >= hpPointLimit) {
			throw new BaseException("体力已满，不需补充");
		}

		int addValue = (effectDefine.getPara2() + (int) (hpPointLimit
				* effectDefine.getPara1() / 100d))
				* num;
		userService.doAddHpPoint(usr, addValue, LogHpAct.USER_ITEM_ADD);
		// userDailyActivityService.addUserDailyActivity(usr.getUserId(),
		// DailyActivity.ACT_RESUME_HP, (byte) 1);
		return null;
	}

}

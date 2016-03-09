package com.youxigu.dynasty2.entity.service.script;

import com.youxigu.dynasty2.entity.domain.Entity;
import com.youxigu.dynasty2.entity.domain.effect.EffectDefine;
import com.youxigu.dynasty2.entity.service.IEffectRender;
import com.youxigu.dynasty2.log.imp.LogCashAct;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.user.service.IUserService;

import java.util.Map;

/**
 * 加元宝
 * @author Dagangzi
 *
 */
public class AddCashRender implements IEffectRender {
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

		int addValue = effectDefine.getPara2() * num;
		userService.addGiftCash(usr.getUserId(), addValue, LogCashAct.ITEM_ACTION);
		return null;
	}
}

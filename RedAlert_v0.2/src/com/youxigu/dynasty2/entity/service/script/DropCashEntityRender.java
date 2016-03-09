package com.youxigu.dynasty2.entity.service.script;

import com.manu.core.ServiceLocator;
import com.youxigu.dynasty2.entity.domain.Entity;
import com.youxigu.dynasty2.entity.service.IEntityEffectRender;
import com.youxigu.dynasty2.log.imp.LogCashAct;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.user.service.IUserService;
import com.youxigu.dynasty2.util.BaseException;

import java.util.Map;

/**
 * 掉落元宝 TODO 这个方法应该在正式上线前关掉
 * 
 * @author Dagangzi
 * 
 */
public class DropCashEntityRender implements IEntityEffectRender {
	@Override
	public Map<String, Object> render(Entity entity, Map<String, Object> context) {
		User user = (User) context.get("user");
        IUserService userService = (IUserService) ServiceLocator.getSpringBean("userService");
        if (user == null) {
			Object userId = context.get("userId");
			if (userId != null) {
				user = userService.getUserById((Long) userId);
				if (user == null) {
					throw new BaseException("缺少userId或者user参数");
				}
			}
		}
		user = userService.lockGetUser(user);// 加锁
		int num = 0;
		Object tmp = context.get("num");
		if (tmp != null) {
			num = (Integer) tmp;
		}
		if (num <= 0) {
			throw new BaseException("执行掉落元宝缺少参数num");
		}
		Object action = context.get("iAction");
		if (action==null||!(action instanceof LogCashAct)){
			action = LogCashAct.DROP_PACK_ACTION;
		}
		userService.addGiftCash(user, num, (LogCashAct)action);

		return null;
	}
}

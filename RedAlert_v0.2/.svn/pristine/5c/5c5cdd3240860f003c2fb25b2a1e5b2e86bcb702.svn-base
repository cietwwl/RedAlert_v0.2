package com.youxigu.dynasty2.entity.service.script;

import java.util.Map;

import com.youxigu.dynasty2.entity.domain.Entity;
import com.youxigu.dynasty2.entity.domain.effect.EffectDefine;
import com.youxigu.dynasty2.entity.service.IEffectRender;
import com.youxigu.dynasty2.log.imp.LogActiveAct;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.user.service.IUserService;

/*******************************************************************************
 * 增加行动点 effTypeId:USER_ACTION_POINT
 * 
 * @author zouhe
 * 
 */
public class ActionPoint implements IEffectRender {

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

		int maxActPoint = userService.getActPointMaxB();
		// TODO 绝对值 + 上限值*百分比
		int addValue = (effectDefine.getPara2() + (int) (maxActPoint
				* effectDefine.getPara1() / 100d))
				* num;

		long userId = usr.getUserId();
		userService.doAddCurActPoint(userId, addValue,
				LogActiveAct.USE_ITEM_ADD);
		return null;
	}

}

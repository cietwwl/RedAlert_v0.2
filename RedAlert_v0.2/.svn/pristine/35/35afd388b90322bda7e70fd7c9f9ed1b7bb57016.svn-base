package com.youxigu.dynasty2.entity.service.script;

import java.util.Map;

import com.youxigu.dynasty2.chat.ChatChannelManager;
import com.youxigu.dynasty2.chat.client.IChatClientService;
import com.youxigu.dynasty2.develop.domain.CastleResource;
import com.youxigu.dynasty2.develop.service.ICastleResService;
import com.youxigu.dynasty2.entity.domain.Entity;
import com.youxigu.dynasty2.entity.domain.effect.EffectDefine;
import com.youxigu.dynasty2.entity.service.IEffectRender;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.util.BaseException;

/**
 * 增加油矿
 * @author Dagangzi
 *
 */
public class OilRender implements IEffectRender {
	private IChatClientService messageService;
	private ICastleResService castleResService;

	public void setMessageService(IChatClientService messageService) {
		this.messageService = messageService;
	}

	public void setCastleResService(ICastleResService castleResService) {
		this.castleResService = castleResService;
	}

	@Override
	public Map<String, Object> render(Entity entity, EffectDefine effectDefine, Map<String, Object> context) {
		User user = (User) context.get("user");
		long userId = user.getUserId();
		int num = 1;
		Object tmp = context.get("num");
		if (tmp != null)
			num = (Integer) tmp;

		int addNum = effectDefine.getPara2() * num;

		if (num < 0 || num > Integer.MAX_VALUE) {
			throw new BaseException("使用的资源不能大于21亿");
		}

		CastleResource castleRes = castleResService.lockCasRes(user.getMainCastleId());
		long total = castleRes.getOilNum() + addNum;
		if (total < 0 || total > Integer.MAX_VALUE) {
			throw new BaseException("资源已经达到上限");
		}
		castleRes.setOilNum(total);
		castleResService.updateCastleResource(castleRes, userId, true);// 更新数据

		StringBuffer sb = new StringBuffer();
		if (addNum > 0) {
			sb.append("增加");
		} else {
			sb.append("减少");
		}
		sb.append("油矿" + addNum).append("。");
		messageService.sendMessage(0, userId, ChatChannelManager.CHANNEL_SYSTEM, null, sb.toString());

		return null;
	}
}

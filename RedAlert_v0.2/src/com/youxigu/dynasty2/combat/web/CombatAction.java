package com.youxigu.dynasty2.combat.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.youxigu.dynasty2.chat.proto.CommonHead.ResponseHead;
import com.youxigu.dynasty2.combat.proto.CombatMsg;
import com.youxigu.dynasty2.combat.proto.CombatMsg.Request70101Define;
import com.youxigu.dynasty2.combat.proto.CombatMsg.Response70101Define;
import com.youxigu.dynasty2.combat.service.ICombatService;
import com.youxigu.dynasty2.core.flex.amf.BaseAction;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.wolf.net.Response;

/**
 * 战斗模块与前台接口
 * 
 * @author Administrator
 * 
 */
public class CombatAction extends BaseAction {
	public static final Logger log = LoggerFactory.getLogger(CombatAction.class);
	private ICombatService combatService;

	public void setCombatService(ICombatService combatService) {
		this.combatService = combatService;
	}

	/**
	 * 获取战斗信息 70101
	 * @param params
	 * @param context
	 * @return
	 */
	public Object getCombat(Object params, Response context) {
		Request70101Define request = (Request70101Define) params;
		String combatId = request.getCombatId();
		boolean db = request.hasFromDB();
		if (log.isDebugEnabled()) {
			System.out.println("战斗ID:" + combatId);
		}
		CombatMsg.Combat tmp = null;
		if (combatId != null && !"".equals(combatId)) {
			tmp = combatService.getCombatPf(combatId, db);
		}

		// 前台自行判断是否有战斗
		if (tmp == null) {
			throw new BaseException("战斗不存在");
		}

		Response70101Define.Builder response = Response70101Define.newBuilder();
		response.setCombat(tmp);
		//消息头
		ResponseHead.Builder headBr = ResponseHead.newBuilder();
		headBr.setCmd(70102);
		headBr.setRequestCmd(request.getCmd());
		response.setResponseHead(headBr.build());
		return response.build();
	}
}

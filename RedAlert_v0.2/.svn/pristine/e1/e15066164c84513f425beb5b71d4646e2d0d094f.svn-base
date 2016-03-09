package com.youxigu.dynasty2.user.web;

import java.util.List;
import java.util.Map;

import com.youxigu.dynasty2.chat.proto.CommonHead;
import com.youxigu.dynasty2.core.flex.amf.BaseAction;
import com.youxigu.dynasty2.entity.domain.DroppedEntity;
import com.youxigu.dynasty2.user.proto.UserMsg;
import com.youxigu.dynasty2.user.proto.UserMsg.Request10036Define;
import com.youxigu.dynasty2.user.proto.UserMsg.Request10038Define;
import com.youxigu.dynasty2.user.proto.UserMsg.Request10040Define;
import com.youxigu.dynasty2.user.proto.UserMsg.Response10036Define;
import com.youxigu.dynasty2.user.proto.UserMsg.Response10038Define;
import com.youxigu.dynasty2.user.proto.UserMsg.Response10040Define;
import com.youxigu.dynasty2.user.service.IUserTitleService;
import com.youxigu.wolf.net.Response;
import com.youxigu.wolf.net.UserSession;

/**
 * 君主军衔协议
 * 
 * @author Dagangzi
 * @date 2016年1月21日
 */
public class UserTitleAction extends BaseAction {
	private IUserTitleService userTitleService;

	public void setUserTitleService(IUserTitleService userTitleService) {
		this.userTitleService = userTitleService;
	}

	/**
	 * 领取勋章-10036
	 * 
	 * @param obj
	 * @param context
	 * @return
	 */
	public Object clickMedal(Object obj, Response context) {
		UserSession us = super.getUserSession(context);
		Request10036Define req = (Request10036Define) obj;
		int awardId = req.getAwardId();
		Map<String, Object> result = userTitleService
				.doActiveMedal(us.getUserId(), awardId);

		Response10036Define.Builder res = Response10036Define.newBuilder();
		res.setResponseHead(super.getResponseHead(req.getCmd()));
		if (result.containsKey("color")) {
			res.setColor((Integer) result.get("color"));
		}

		if (result.containsKey("upTitleId")) {
			res.setUpTitleId((Integer) result.get("upTitleId"));
		}

		if (result.containsKey("awardId")) {
			res.setAwardId((Integer) result.get("awardId"));
		}

		if (result.containsKey("upItemAward")) {
			Object tmp = result.get("upItemAward");
			if (tmp != null) {
				List<DroppedEntity> dropList = (List<DroppedEntity>) tmp;
				if (dropList != null && dropList.size() > 0) {
					for (DroppedEntity de : dropList) {
						CommonHead.ItemInfo.Builder itemInfo = CommonHead.ItemInfo
								.newBuilder();
						itemInfo.setEntId(de.getEntId());
						itemInfo.setNum(de.getNum());
						res.addItems(itemInfo.build());
					}
				}
			}
		}

		result = userTitleService.showTitleInfo(us.getUserId(), 0);
		if (result.containsKey("junGong")) {
			res.setJunGong((Integer) result.get("junGong"));
		}

		if (result.containsKey("list")) {
			Object tmp = result.get("list");
			if (tmp != null) {
				List<Map<String, Object>> list = (List<Map<String, Object>>) tmp;
				if (list != null && list.size() > 0) {
					for (Map<String, Object> map : list) {
						UserMsg.TitleStatus.Builder tsB = UserMsg.TitleStatus
								.newBuilder();
						tsB.setAwardId((Integer) map.get("awardId"));
						tsB.setStatus((Integer) map.get("status"));
						res.addList(tsB.build());
					}
				}
			}
		}
		return res.build();
	}

	/**
	 * 取得军衔详情-10038
	 * 
	 * @param obj
	 * @param context
	 * @return
	 */
	public Object showTitleInfo(Object obj, Response context) {
		UserSession us = super.getUserSession(context);
		int titleId = 0;
		Request10038Define req = (Request10038Define) obj;
		if (req.hasTitleId()) {
			titleId = req.getTitleId();
		}
		Map<String, Object> result = userTitleService
				.showTitleInfo(us.getUserId(), titleId);

		Response10038Define.Builder res = Response10038Define.newBuilder();
		res.setResponseHead(super.getResponseHead(req.getCmd()));
		if (result.containsKey("junGong")) {
			res.setJunGong((Integer) result.get("junGong"));
		}

		if (result.containsKey("list")) {
			Object tmp = result.get("list");
			if (tmp != null) {
				List<Map<String, Object>> list = (List<Map<String, Object>>) tmp;
				if (list != null && list.size() > 0) {
					for (Map<String, Object> map : list) {
						UserMsg.TitleStatus.Builder tsB = UserMsg.TitleStatus
								.newBuilder();
						tsB.setAwardId((Integer) map.get("awardId"));
						tsB.setStatus((Integer) map.get("status"));
						res.addList(tsB.build());
					}
				}
			}
		}
		return res.build();
	}

	/**
	 * 取当前军衔-10040
	 * 
	 * @param obj
	 * @param context
	 * @return
	 */
	public Object getRedPoint(Object obj, Response context) {
		UserSession us = super.getUserSession(context);
		Request10040Define req = (Request10040Define) obj;
		Map<String, Object> result = userTitleService
				.getRedPoint(us.getUserId());

		Response10040Define.Builder res = Response10040Define.newBuilder();
		res.setResponseHead(super.getResponseHead(req.getCmd()));
		if (result.containsKey("isRed")) {
			res.setRed((Boolean) result.get("isRed"));
		}
		if (result.containsKey("titleId")) {
			res.setTitleId((Integer) result.get("titleId"));
		}
		return res.build();
	}
}

package com.youxigu.dynasty2.activity.web;

import java.util.List;
import java.util.Map;

import com.youxigu.dynasty2.activity.proto.ActivityMsg.MysticShopInfo;
import com.youxigu.dynasty2.activity.proto.ActivityMsg.Request11021Define;
import com.youxigu.dynasty2.activity.proto.ActivityMsg.Request11023Define;
import com.youxigu.dynasty2.activity.proto.ActivityMsg.Request11025Define;
import com.youxigu.dynasty2.activity.proto.ActivityMsg.Response11022Define;
import com.youxigu.dynasty2.activity.proto.ActivityMsg.Response11024Define;
import com.youxigu.dynasty2.activity.proto.ActivityMsg.Response11026Define;
import com.youxigu.dynasty2.activity.proto.ActivityMsg.UserMysticShop;
import com.youxigu.dynasty2.activity.service.IMysticShopService;
import com.youxigu.dynasty2.common.AppConstants;
import com.youxigu.dynasty2.common.service.ICommonService;
import com.youxigu.dynasty2.core.flex.amf.BaseAction;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.dynasty2.util.MathUtils;
import com.youxigu.wolf.net.Response;
import com.youxigu.wolf.net.UserSession;

@SuppressWarnings("unchecked")
public class MysticShopAction extends BaseAction {

	private IMysticShopService mysticShopService;
	private ICommonService commonService;

	/**
	 * 11021打开神秘商店面板
	 * 
	 * @param params
	 * @param context
	 * @return
	 */

	public Object getShopItemView(Object obj, Response context) {
		isOpen();
		Request11021Define req = (Request11021Define) obj;
		UserSession us = super.getUserSession(context);
		int shopId = req.getShopId();
		Map<String, Object> params = mysticShopService.doGetShopItemView(
				us.getUserId(), shopId);
		Response11022Define.Builder res = Response11022Define.newBuilder();
		res.setResponseHead(super.getResponseHead(req.getCmd()));

		MysticShopInfo.Builder info = toShopInfo(shopId, params);
		res.setShopInfo(info.build());
		return res.build();

	}

	private MysticShopInfo.Builder toShopInfo(int shopId,
			Map<String, Object> params) {
		MysticShopInfo.Builder info = MysticShopInfo.newBuilder();

		info.setCash(MathUtils.getInt(params.remove("cash")));
		info.setShopId(shopId);
		info.setFreeNum(MathUtils.getInt(params.remove("freeNum")));
		info.setNextTime(MathUtils.getLong(params.remove("nextTime")));
		info.setItemCnt(MathUtils.getInt(params.remove("itemCnt")));
		info.setBuyNum(MathUtils.getInt(params.remove("buyNum")));
		info.setItemNum(MathUtils.getInt(params.remove("itemNum")));

		List<Map<String, Object>> dataList = (List<Map<String, Object>>) params
				.remove("shopItems");
		for (Map<String, Object> mp : dataList) {
			UserMysticShop.Builder ums = UserMysticShop.newBuilder();
			ums.setShopItemId(MathUtils.getInt(mp.remove("shopItemId")));
			ums.setStatus(MathUtils.getInt(mp.remove("status")));
			info.addShopItems(ums.build());
		}
		return info;
	}

	/**
	 * 11023刷新神秘商店
	 * 
	 * @param params
	 * @param context
	 * @return
	 */
	public Object freashShopIems(Object obj, Response context) {
		isOpen();
		Request11023Define req = (Request11023Define) obj;
		UserSession us = super.getUserSession(context);
		int shopId = req.getShopId();
		Map<String, Object> params = mysticShopService.doFreashShopIems(
				us.getUserId(), shopId);

		Response11024Define.Builder res = Response11024Define.newBuilder();
		res.setResponseHead(super.getResponseHead(req.getCmd()));
		MysticShopInfo.Builder info = toShopInfo(shopId, params);
		res.setShopInfo(info.build());
		return res.build();
	}

	/**
	 * 11025购买神秘商店道具
	 * 
	 * @param params
	 * @param context
	 * @return
	 */
	public Object buyShopIem(Object obj, Response context) {
		isOpen();
		Request11025Define req = (Request11025Define) obj;

		UserSession us = super.getUserSession(context);
		int pos = req.getPos();
		int shopId = req.getShopId();
		mysticShopService.doBuyShopIem(us.getUserId(), shopId, pos);
		Response11026Define.Builder res = Response11026Define.newBuilder();
		res.setResponseHead(super.getResponseHead(req.getCmd()));
		res.setPos(pos);
		res.setShopId(shopId);
		return res.build();

	}

	private void isOpen() {
		if (1 != commonService.getSysParaIntValue(
				AppConstants.MYSTICSHOP_OPEN_STATUS, 1)) {
			throw new BaseException("功能未开放");
		}
	}

	public void setMysticShopService(IMysticShopService mysticShopService) {
		this.mysticShopService = mysticShopService;
	}

	public void setCommonService(ICommonService commonService) {
		this.commonService = commonService;
	}

}

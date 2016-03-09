package com.youxigu.dynasty2.treasury.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.youxigu.dynasty2.chat.proto.CommonHead.ItemInfo;
import com.youxigu.dynasty2.common.service.ICommonService;
import com.youxigu.dynasty2.core.flex.amf.BaseAction;
import com.youxigu.dynasty2.entity.domain.DroppedEntity;
import com.youxigu.dynasty2.treasury.domain.Treasury;
import com.youxigu.dynasty2.treasury.domain.TreasuryMessage;
import com.youxigu.dynasty2.treasury.proto.TreasuryMsg.Request61001Define;
import com.youxigu.dynasty2.treasury.proto.TreasuryMsg.Request61003Define;
import com.youxigu.dynasty2.treasury.proto.TreasuryMsg.Request61005Define;
import com.youxigu.dynasty2.treasury.proto.TreasuryMsg.Request61007Define;
import com.youxigu.dynasty2.treasury.proto.TreasuryMsg.Response61001Define;
import com.youxigu.dynasty2.treasury.proto.TreasuryMsg.Response61003Define;
import com.youxigu.dynasty2.treasury.proto.TreasuryMsg.Response61005Define;
import com.youxigu.dynasty2.treasury.proto.TreasuryMsg.Response61007Define;
import com.youxigu.dynasty2.treasury.service.ITreasuryService;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.user.service.IUserService;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.dynasty2.util.MathUtils;
import com.youxigu.wolf.net.Response;
import com.youxigu.wolf.net.UserSession;

public class TreasuryAction extends BaseAction {
	private ITreasuryService treasuryService;
	private IUserService userService;
	private ICommonService commonService;

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public void setTreasuryService(ITreasuryService treasuryService) {
		this.treasuryService = treasuryService;
	}

	public void setCommonService(ICommonService commonService) {
		this.commonService = commonService;
	}

	/**
	 * [前台缓存用]显示背包道具列表 -61001
	 */
	public Object loadTreasuryViewByUserId(Object params, Response context) {
		UserSession session = getUserSession(context);
		//传入值
		Request61001Define request = (Request61001Define) params;

		long userId = session.getUserId();
		List<TreasuryMessage> result = new ArrayList<TreasuryMessage>();

		List<Treasury> tList = treasuryService.getTreasurysByUserId(userId);
		if (tList != null && tList.size() > 0) {
			for (Treasury t : tList) {
				result.add(t.getView());
			}
		}

		//返回值
		Response61001Define.Builder response = Response61001Define.newBuilder();
		response.setResponseHead(super.getResponseHead(request.getCmd()));
		for (TreasuryMessage tm : result) {
			response.addTreasuryList(tm.convetMessage());
		}
		return response.build();
	}

	/**
	 * 丢弃道具 61003
	 * @return
	 */
	public Object delItem(Object params, Response context) {
		UserSession session = getUserSession(context);

		//传入值
		Request61003Define request = (Request61003Define) params;
		long userId = session.getUserId();
		long id = request.getId();
		int num = request.getItemCount();

		treasuryService.deleteTreasuryByDiscard(userId, id, num);

		//返回值
		Response61003Define.Builder response = Response61003Define.newBuilder();
		response.setResponseHead(super.getResponseHead(request.getCmd()));

		return response.build();
	}

	/**
	 * 使用道具61005
	 * @param params
	 * @param context
	 * @return
	 */
	public Object useItem(Object obj, Response context) {
		UserSession session = getUserSession(context);

		//传入值
		Request61005Define request = (Request61005Define) obj;

		long userId = session.getUserId();
        Object tmpNum = 0;
        if(request.hasItemCount()) {
            tmpNum = request.getItemCount();
        }
		int num = 1;
		if (tmpNum != null) {
			num = (Integer) tmpNum;
		}
        Object tmp = null;
        if(request.hasId()) {
            tmp = request.getId();
        }

		//返回值
		Response61005Define.Builder response = Response61005Define.newBuilder();
		response.setResponseHead(super.getResponseHead(request.getCmd()));

		Map<String, Object> params = new HashMap<String, Object>();
		if (tmp != null) {
			long id = MathUtils.getLong(tmp);
			treasuryService.doUseItem(userId, id, num, session.getPfEx(), params);
			response.setId(id);
		} else {// 按道具ID使用道具
			int itemId = request.getItemId();
			treasuryService.doUseItem(userId, itemId, num, session.getPfEx(), params);
			response.setItemId(itemId);
		}
		response.setItemCount(num);

		//掉落包时，掉落详情
		if (params.containsKey("items")) {
			Map<Integer, DroppedEntity> dropList = (Map<Integer, DroppedEntity>) params.get("items");
			if (dropList != null && dropList.size() > 0) {
				Iterator<Map.Entry<Integer, DroppedEntity>> itl = dropList.entrySet().iterator();
				while (itl.hasNext()) {
					Map.Entry<Integer, DroppedEntity> ent = itl.next();
					DroppedEntity droppedEntity = ent.getValue();
					ItemInfo.Builder itemInfo = ItemInfo.newBuilder();
					itemInfo.setEntId(droppedEntity.getEntId());
					itemInfo.setNum(droppedEntity.getNum());
					response.addItems(itemInfo.build());
				}
			}
		}
		return response.build();
	}

	/**
	 * 61007 查看道具： 用在聊天频道显示某个人发送的道具信息
	 * 
	 * @param params
	 * @param context
	 * @return
	 */
	public Object getTreasury(Object params, Response context) {
		//传入值
		Request61007Define request = (Request61007Define) params;
		long userId = 0;
		String userName = request.getUName();
		if (userName != null && !"".equals(userName)) {
			User user = userService.getUserByName(userName);
			if (user != null) {
				userId = user.getUserId();
			}
		} else {
			userId = request.getUId();
		}

		long treasuryId = request.getTId();
		if (userId == 0 || treasuryId == 0) {
			throw new BaseException("参数错误，道具不存在");
		}
		Treasury treasury = treasuryService.getTreasuryById(userId, treasuryId);

		//返回值
		Response61007Define.Builder response = Response61007Define.newBuilder();
		response.setResponseHead(super.getResponseHead(request.getCmd()));
		if (treasury != null) {
			response.setTreasuryEvent(treasury.getOtherUserView().convetMessage());
		} else {
			throw new BaseException("道具不存在");
		}
		return response.build();
	}

}

package com.youxigu.dynasty2.map.web;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import com.youxigu.dynasty2.chat.proto.CommonHead.Point;
import com.youxigu.dynasty2.common.AppConstants;
import com.youxigu.dynasty2.common.service.ICommonService;
import com.youxigu.dynasty2.common.service.ISensitiveWordService;
import com.youxigu.dynasty2.core.flex.amf.BaseAction;
import com.youxigu.dynasty2.map.domain.CollectPoint;
import com.youxigu.dynasty2.map.proto.MapMsg.CollectPointInfo;
import com.youxigu.dynasty2.map.proto.MapMsg.CollectType;
import com.youxigu.dynasty2.map.proto.MapMsg.Request90011Define;
import com.youxigu.dynasty2.map.proto.MapMsg.Request90013Define;
import com.youxigu.dynasty2.map.proto.MapMsg.Request90015Define;
import com.youxigu.dynasty2.map.proto.MapMsg.Request90017Define;
import com.youxigu.dynasty2.map.proto.MapMsg.Request90030Define;
import com.youxigu.dynasty2.map.proto.MapMsg.Request90032Define;
import com.youxigu.dynasty2.map.proto.MapMsg.Response90011Define;
import com.youxigu.dynasty2.map.proto.MapMsg.Response90013Define;
import com.youxigu.dynasty2.map.proto.MapMsg.Response90015Define;
import com.youxigu.dynasty2.map.proto.MapMsg.Response90017Define;
import com.youxigu.dynasty2.map.proto.MapMsg.Response90030Define;
import com.youxigu.dynasty2.map.proto.MapMsg.Response90032Define;
import com.youxigu.dynasty2.map.service.ICollectPointService;
import com.youxigu.dynasty2.map.service.IMapService;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.user.service.IUserService;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.dynasty2.util.StringUtils;
import com.youxigu.wolf.net.Response;
import com.youxigu.wolf.net.UserSession;

/**
 * 主要是一些世界地图 界面 格子的操作协议
 * 
 * @author fengfeng
 *
 */
public class MapViewAction extends BaseAction {
	private Pattern reg = Pattern.compile("[a-zA-Z0-9_\u4e00-\u9fa5]{1,16}");
	private ICollectPointService collectPointService = null;
	private ICommonService commonService = null;
	private ISensitiveWordService sensitiveWordService;
	private IMapService mapService;
	private IUserService userService;

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public void setMapService(IMapService mapService) {
		this.mapService = mapService;
	}

	public void setCommonService(ICommonService commonService) {
		this.commonService = commonService;
	}

	public void setSensitiveWordService(
			ISensitiveWordService sensitiveWordService) {
		this.sensitiveWordService = sensitiveWordService;
	}

	/**
	 * 90011获取收藏列表
	 * 
	 * @param obj
	 * @param context
	 * @return
	 */
	public Object collectPointList(Object obj, Response context) {
		Request90011Define req = (Request90011Define) obj;
		UserSession us = super.getUserSession(context);
		List<CollectPoint> ps = collectPointService.getAllCollectPoints(us
				.getUserId());
		Response90011Define.Builder res = Response90011Define.newBuilder();
		if (ps != null && !ps.isEmpty()) {
			for (CollectPoint p : ps) {
				CollectPointInfo.Builder cp = buildCollectPoint(p);
				res.addCollInfos(cp.build());
			}
		}
		res.setResponseHead(super.getResponseHead(req.getCmd()));
		return res.build();
	}

	private CollectPointInfo.Builder buildCollectPoint(CollectPoint p) {
		CollectPointInfo.Builder cp = CollectPointInfo.newBuilder();
		cp.setCollectType(CollectType.valueOf(p.getCollectType()));
		cp.setId(p.getId());
		cp.setName(p.getName());

		Point.Builder pt = Point.newBuilder();
		pt.setPosX(p.getPosX());
		pt.setPosY(p.getPosY());
		cp.setPon(pt.build());
		return cp;
	}

	private String checkName(String name) {
		name = name.trim();
		if (name.length() == 0) {
			throw new BaseException("收藏名字不能为空");
		}
		if (name.length() > 12) {
			throw new BaseException("收藏名字必须为1-12个字符");
		}
		if (!StringUtils.match(name, reg)) {
			throw new BaseException("收藏名字包含非法字符或空格，请重新输入");
		}
		if (sensitiveWordService.match(name)) {
			throw new BaseException("收藏名字包含非法词语,请重新输入");
		}
		return name;
	}

	/**
	 * 90013添加收藏
	 * 
	 * @param obj
	 * @param context
	 * @return
	 */
	public Object addCollectPoint(Object obj, Response context) {
		Request90013Define req = (Request90013Define) obj;
		UserSession us = super.getUserSession(context);
		CollectPoint p = new CollectPoint();

		int x = req.getCollInfo().getPon().getPosX();
		int y = req.getCollInfo().getPon().getPosY();
		String name = req.getCollInfo().getName();
		if (!req.getCollInfo().hasCollectType()) {
			throw new BaseException("为设置收藏分类");
		}
		// 判断名字是否合法
		name = checkName(name);
		// 判断坐标是否合法
		if (x < 0 || x > IMapService.MAP_MAX_WIDTH || y < 0
				|| y > IMapService.MAP_MAX_HIGH) {
			throw new BaseException("坐标超出范围");
		}

		// 判断条数上限
		int max = commonService.getSysParaIntValue(
				AppConstants.COLLECT_POINT_MAX, 100);
		if (collectPointService.getCollectPointCount(us.getUserId()) >= max) {
			new BaseException("最多只能收藏" + max + "个坐标点");
		}
		p.setCollectType(req.getCollInfo().getCollectType().getNumber());
		p.setName(name);
		p.setPosX(x);
		p.setPosY(y);
		p.setUserId(us.getUserId());
		p.setCreateTime(new Timestamp(System.currentTimeMillis()));

		collectPointService.doSaveCollectPoint(p);

		Response90013Define.Builder res = Response90013Define.newBuilder();
		CollectPointInfo.Builder cp = buildCollectPoint(p);
		res.setCollInfo(cp.build());
		res.setResponseHead(super.getResponseHead(req.getCmd()));
		return res.build();
	}

	/**
	 * 90015 修改收藏
	 * 
	 * @param obj
	 * @param context
	 * @return
	 */
	public Object editCollectPoint(Object obj, Response context) {
		Request90015Define req = (Request90015Define) obj;
		UserSession us = super.getUserSession(context);
		String name = req.getCollInfo().getName();
		if (!req.getCollInfo().hasCollectType()) {
			throw new BaseException("为设置收藏分类");
		}

		// 判断名字是否合法
		name = checkName(name);

		CollectPoint cp = collectPointService.getCollectPoint(us.getUserId(),
				req.getCollInfo().getId());
		if (cp == null) {
			throw new BaseException("修改的点不存在");
		}
		cp.setCollectType(req.getCollInfo().getCollectType().getNumber());
		cp.setName(name);
		collectPointService.doUpdateCollectPoint(cp);

		Response90015Define.Builder res = Response90015Define.newBuilder();
		CollectPointInfo.Builder p = buildCollectPoint(cp);
		res.setCollInfo(p.build());
		res.setResponseHead(super.getResponseHead(req.getCmd()));
		return res.build();
	}

	/**
	 * 90017 删除收藏
	 * 
	 * @param obj
	 * @param context
	 * @return
	 */
	public Object delCollectPoint(Object obj, Response context) {
		Request90017Define req = (Request90017Define) obj;
		UserSession us = super.getUserSession(context);
		collectPointService.doDeleteCollectPoint(us.getUserId(), req.getId());
		Response90017Define.Builder res = Response90017Define.newBuilder();
		res.setId(req.getId());
		res.setResponseHead(super.getResponseHead(req.getCmd()));
		return res.build();
	}

	/**
	 * 90030获取随机迁城列表
	 * 
	 * @param obj
	 * @param context
	 * @return
	 */
	public Object randomCityStates(Object obj, Response context) {
		Request90030Define req = (Request90030Define) obj;
		UserSession us = super.getUserSession(context);

		User user = userService.getUserById(us.getUserId());
		Set<Integer> rt = mapService.getAllOpenStates(user.getCountryId());
		Response90030Define.Builder res = Response90030Define.newBuilder();
		res.setResponseHead(super.getResponseHead(req.getCmd()));
		if (rt != null && !rt.isEmpty()) {
			for (int r : rt) {
				res.addStateIds(r);
			}
		}
		return res.build();
	}

	/**
	 * 90032随机迁城
	 * 
	 * @param obj
	 * @param context
	 * @return
	 */
	public Object randomCity(Object obj, Response context) {
		Request90032Define req = (Request90032Define) obj;
		// UserSession us = super.getUserSession(context);
		// collectPointService.doDeleteCollectPoint(us.getUserId(),
		// req.getId());
		Response90032Define.Builder res = Response90032Define.newBuilder();
		// res.setId(req.getId());
		res.setResponseHead(super.getResponseHead(req.getCmd()));
		return res.build();
	}

	/**
	 * 90034 定点迁城
	 * 
	 * @param obj
	 * @param context
	 * @return
	 */
	public Object setCityPoint(Object obj, Response context) {
		Request90032Define req = (Request90032Define) obj;
		// UserSession us = super.getUserSession(context);
		// collectPointService.doDeleteCollectPoint(us.getUserId(),
		// req.getId());
		Response90032Define.Builder res = Response90032Define.newBuilder();
		// res.setId(req.getId());
		res.setResponseHead(super.getResponseHead(req.getCmd()));
		return res.build();
	}

}

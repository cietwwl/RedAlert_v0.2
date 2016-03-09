package com.youxigu.dynasty2.user.web;

import java.util.List;
import java.util.Map;

import com.youxigu.dynasty2.core.flex.amf.BaseAction;
import com.youxigu.dynasty2.user.domain.UserAchieve;
import com.youxigu.dynasty2.user.proto.UserMsg;
import com.youxigu.dynasty2.user.proto.UserMsg.Request10034Define;
import com.youxigu.dynasty2.user.proto.UserMsg.Response10034Define;
import com.youxigu.dynasty2.user.service.IUserAchieveService;
import com.youxigu.wolf.net.Response;
import com.youxigu.wolf.net.UserSession;

/**
 * 君主成就协议
 * 
 * @author Dagangzi
 * @date 2016年1月19日
 */
public class UserAchieveAction extends BaseAction {
	private IUserAchieveService userAchieveService;

	public void setUserAchieveService(IUserAchieveService userAchieveService) {
		this.userAchieveService = userAchieveService;
	}

	/**
	 * 10034取得成就界面请求
	 * type=-1 总览 显示所有已完成的
	 * type>0 按类别显示全部的完成进度
	 * @param obj
	 * @param context
	 * @return
	 */
	public Object getUserAchieveByType(Object obj, Response context) {
		Request10034Define req = (Request10034Define) obj;
		int type = req.getType();
		UserSession us = super.getUserSession(context);
		Map<String, Object> params = userAchieveService
				.getUserAchieveByType(us.getUserId(), type);
		int total = (Integer)params.get("total");
		int finishNum = (Integer)params.get("finishNum");
		int junGong = (Integer)params.get("junGong");
		// 成就列表
		List<UserAchieve.AchieveInfo> infos = (List<UserAchieve.AchieveInfo>)params.get("infos");
		
		Response10034Define.Builder res = Response10034Define.newBuilder();
		res.setResponseHead(super.getResponseHead(req.getCmd()));
		res.setTotal(total);
		res.setFinishNum(finishNum);
		res.setJunGong(junGong);
		if (infos != null && infos.size() > 0) {
			for (UserAchieve.AchieveInfo info : infos) {
				UserMsg.AchieveInfo.Builder achieveInfo = UserMsg.AchieveInfo
						.newBuilder();
				achieveInfo.setAchieveId(info.getAchieveId());
				achieveInfo.setEntId(info.getEntId());
				achieveInfo.setEntNum(info.getEntNum());
				achieveInfo.setFinish(info.isFinish());
				achieveInfo.setType(info.getType());
				res.addInfos(achieveInfo.build());
			}
		}
		return res.build();
	}
}

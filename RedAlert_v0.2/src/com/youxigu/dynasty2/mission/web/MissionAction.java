package com.youxigu.dynasty2.mission.web;

import java.util.List;

import com.youxigu.dynasty2.core.flex.amf.BaseAction;
import com.youxigu.dynasty2.mission.domain.Mission;
import com.youxigu.dynasty2.mission.domain.UserMission;
import com.youxigu.dynasty2.mission.domain.WorldMission;
import com.youxigu.dynasty2.mission.proto.MissionMsg;
import com.youxigu.dynasty2.mission.proto.MissionMsg.Request80001Define;
import com.youxigu.dynasty2.mission.proto.MissionMsg.Request80003Define;
import com.youxigu.dynasty2.mission.proto.MissionMsg.Request80005Define;
import com.youxigu.dynasty2.mission.proto.MissionMsg.Request80007Define;
import com.youxigu.dynasty2.mission.proto.MissionMsg.Request80009Define;
import com.youxigu.dynasty2.mission.proto.MissionMsg.Response80001Define;
import com.youxigu.dynasty2.mission.proto.MissionMsg.Response80003Define;
import com.youxigu.dynasty2.mission.proto.MissionMsg.Response80005Define;
import com.youxigu.dynasty2.mission.proto.MissionMsg.Response80007Define;
import com.youxigu.dynasty2.mission.proto.MissionMsg.Response80009Define;
import com.youxigu.dynasty2.mission.proto.UserMissionView;
import com.youxigu.dynasty2.mission.service.IMissionService;
import com.youxigu.dynasty2.mission.service.IWorldMissionClientService;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.user.service.IUserService;
import com.youxigu.dynasty2.util.PagerResult;
import com.youxigu.wolf.net.Response;
import com.youxigu.wolf.net.UserSession;

/**
 * 任务协议
 * 
 * @author Dagangzi
 * @date 2016年1月8日
 */
public class MissionAction extends BaseAction {
	private IMissionService missionService;
	private IUserService userService;
	private IWorldMissionClientService worldMissionClientService;

	public void setMissionService(IMissionService missionService) {
		this.missionService = missionService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public void setWorldMissionClientService(
			IWorldMissionClientService worldMissionClientService) {
		this.worldMissionClientService = worldMissionClientService;
	}

	/**
	 * 显示君主任务列表-80001
	 */
	public Object showUserMissionViewList(Object params, Response context) {
		// 传入值
		Request80001Define request = (Request80001Define) params;

		String missionType = request.getMissionType();
		/*
		 * if (missionType == null || missionType.equals("")) { throw new
		 * BaseException("任务类型不能为空。"); }
		 */

		// 返回值
		Response80001Define.Builder response = Response80001Define.newBuilder();
		response.setResponseHead(super.getResponseHead(request.getCmd()));

		// 常规任务
		UserSession session = getUserSession(context);
		User user = userService.getUserById(session.getUserId());
		List<UserMissionView> list = missionService
				.doRefreshAndGetUserMissionByUserId(user, missionType);
		for (UserMissionView view : list) {
			response.addUserMissionList(view.convetMessage());
		}
		return response.build();
	}

	/**
	 * 提交君主任务-80003
	 * 
	 * @param params
	 * @param context
	 * @return
	 */
	public Object commitUserMission(Object params, Response context) {
		// 传入值
		Request80003Define request = (Request80003Define) params;
		UserSession us = getUserSession(context);
		User user = userService.getUserById(us.getUserId());

		int userMissionId = request.getId();
		Response80003Define.Builder response = Response80003Define.newBuilder();
		response.setResponseHead(super.getResponseHead(request.getCmd()));

		if (userMissionId > 0) {
			// Object obj = params.remove("costCash");// 是否花费元宝完成
			boolean isConsume = true;// 是否检查任务消耗
			// if (obj != null) {
			// isConsume = false;
			// }
			UserMission um = missionService.doCommitMission(user, userMissionId,
					isConsume);
			response.setMissionId(um.getMissionId());
		}

		// 普通任务未领奖的数量
		int mainNum = missionService.getFinishUserMissionNum(us.getUserId(),
				Mission.MISSION_TYPE_MAIN);
		response.setMainNum(mainNum);
		return response.build();
	}

	/**
	 * 分页显示世界任务列表-80005
	 * 
	 * @param params
	 * @param context
	 * @return
	 */
	public Object showWorldMissionViewList(Object params, Response context) {
		Request80005Define request = (Request80005Define) params;
		int pageNo = request.getPageNo();
		UserSession us = getUserSession(context);

		// 分页数据
		PagerResult pagerResult = worldMissionClientService
				.getUserMissionByUserId(us.getUserId(), pageNo);
		MissionMsg.PagerMissionMsg.Builder pagerMsg = MissionMsg.PagerMissionMsg
				.newBuilder();
		pagerMsg.setPageNo(pagerResult.getPageNo());
		pagerMsg.setPageSize(pagerResult.getPageSize());
		pagerMsg.setTotal(pagerResult.getTotal());
		pagerMsg.setTotalPages(pagerResult.getTotalPage());
		List<UserMissionView> data = (List<UserMissionView>) pagerResult
				.getDatas();
		if (data != null && data.size() > 0) {
			for (UserMissionView view : data) {
				pagerMsg.addDatas(view.convetMessage());
			}
		}

		Response80005Define.Builder response = Response80005Define.newBuilder();
		response.setResponseHead(super.getResponseHead(request.getCmd()));
		response.setPagerResult(pagerMsg.build());
		return response.build();
	}

	/**
	 * 提交世界任务-80007
	 * 
	 * @param params
	 * @param context
	 * @return
	 */
	public Object commitWorldMission(Object params, Response context) {
		UserSession us = getUserSession(context);

		Request80007Define request = (Request80007Define) params;
		int missionId = request.getMissionId();
		WorldMission worldMission = worldMissionClientService
				.doAward(us.getUserId(), missionId);

		// 刷新小红点
		int[] nums = worldMissionClientService
				.getFinishWorldMissionNum(us.getUserId());
		// 世界任务完成数
		int finishNum = nums[0];
		// 世界任务领取数
		int awardedNum = nums[1];

		Response80007Define.Builder response = Response80007Define.newBuilder();
		response.setResponseHead(super.getResponseHead(request.getCmd()));
		response.setMissionId(worldMission.getMissionId());
		response.setFinishNum(finishNum);
		response.setAwardedNum(awardedNum);
		return response.build();
	}

	/**
	 * 任务小红点 未领奖的君主任务数，未领奖的世间任务数量-80009
	 * 
	 * @param params
	 * @param context
	 * @return
	 */
	public Object showMissionRedPoint(Object params, Response context) {
		Request80009Define request = (Request80009Define) params;
		UserSession us = getUserSession(context);

		// 普通任务未领奖的数量
		int mainNum = missionService.getFinishUserMissionNum(us.getUserId(),
				Mission.MISSION_TYPE_MAIN);

		int[] nums = worldMissionClientService
				.getFinishWorldMissionNum(us.getUserId());

		// 世界任务完成数
		int finishNum = nums[0];
		// 世界任务领取数
		int awardedNum = nums[1];

		Response80009Define.Builder response = Response80009Define.newBuilder();
		response.setResponseHead(super.getResponseHead(request.getCmd()));
		response.setMainNum(mainNum);
		response.setFinishNum(finishNum);
		response.setAwardedNum(awardedNum);
		return response.build();
	}

	// /**
	// * 标记任务已读-80004
	// *
	// * @param params
	// * @param context
	// * @return
	// */
	// public Object setMissionRead(Map<String, Object> params, Response
	// context) {
	// UserSession session = getUserSession(context);
	//
	// int userMissionId = MathUtils.getInt(params.remove("id"));
	// if (userMissionId > 0) {
	// UserMission um = missionService.doMissionRead(session.getUserId(),
	// userMissionId);
	// }
	// return params;
	// }
}

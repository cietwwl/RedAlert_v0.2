package com.youxigu.dynasty2.mission.service.impl;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.manu.core.ServiceLocator;
import com.manu.util.UtilDate;
import com.youxigu.dynasty2.chat.ChatChannelManager;
import com.youxigu.dynasty2.chat.EventMessage;
import com.youxigu.dynasty2.chat.client.IChatClientService;
import com.youxigu.dynasty2.log.ILogService;
import com.youxigu.dynasty2.log.LogBeanFactory;
import com.youxigu.dynasty2.mission.dao.IWorldMissionDao;
import com.youxigu.dynasty2.mission.domain.Mission;
import com.youxigu.dynasty2.mission.domain.MissionLimit;
import com.youxigu.dynasty2.mission.domain.UserMission;
import com.youxigu.dynasty2.mission.domain.UserWorldMission;
import com.youxigu.dynasty2.mission.domain.WorldMission;
import com.youxigu.dynasty2.mission.proto.FinishCountBroadMsg;
import com.youxigu.dynasty2.mission.proto.UserMissionView;
import com.youxigu.dynasty2.mission.service.IMissionService;
import com.youxigu.dynasty2.mission.service.IWorldMissionService;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.dynasty2.util.PagerResult;

import edu.emory.mathcs.backport.java.util.Collections;

public class WorldMissionService implements IWorldMissionService {
	public static final Logger log = LoggerFactory
			.getLogger(WorldMissionService.class);
	// 条件类型-未完成的任务
	private static Map<String, List<WorldMission>> OTCTYPES = new HashMap<String, List<WorldMission>>();
	// 世界任务列表
	private static List<WorldMission> WORLD_MISSIONS = new ArrayList<WorldMission>();
	private static int WORLD_MISSIONS_FINISHNUM = 0;// 成功完成的个数

	/**
	 * 状态由大到小
	 */
	public static final StatusComparator statusComparator = new StatusComparator();
	public static class StatusComparator implements Comparator<WorldMission> {
		@Override
		public int compare(WorldMission o1, WorldMission o2) {
			// 状态由大到小,相同时-完成时间由大到小
			// 多条件比较都可以用这个方式
			int status = (int) (o2.getStatus() - o1.getStatus());
			if (status != 0) {
				return status;
			} else {
				return (int) (o2.getCompleteLimitTime().getTime()
						- o1.getCompleteLimitTime().getTime());
			}
		}
	}

	// 执行线程
	private Worker[] workers;
	private int threadNum = 3;

	private IWorldMissionDao worldMissionDao;
	private IMissionService missionService;
	protected IChatClientService messageService;
	private ILogService logService;

	public void setWorldMissionDao(IWorldMissionDao worldMissionDao) {
		this.worldMissionDao = worldMissionDao;
	}

	public void setMissionService(IMissionService missionService) {
		this.missionService = missionService;
	}

	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}

	public void setMessageService(IChatClientService messageService) {
		this.messageService = messageService;
	}

	public void setLogService(ILogService logService) {
		this.logService = logService;
	}

	/**
	 * 初始化方法
	 */
	public void init() {
		log.info("初始化世界任务,默认启动{}个线程", threadNum);
		// 初始化线程
		workers = new Worker[threadNum];
		for (int i = 0; i < threadNum; i++) {
			workers[i] = new Worker("WORLD_MISSION_" + i);
			workers[i].start();
			log.info("线程{}启动", workers[i].getName());
		}

		// 需要补充的任务
		Set<Integer> missionIds = new HashSet<Integer>();
		// 根任务
		Map<Integer, Mission> rootMap = missionService.getRootMission();
		missionIds.addAll(rootMap.keySet());

		// 刷新就任务的状态和检查新任务
		List<WorldMission> list = worldMissionDao.getAllWorldMission();
		Timestamp now = UtilDate.nowTimestamp();
		if (list != null && list.size() > 0) {
			WORLD_MISSIONS.addAll(list);
			for (WorldMission worldMission : list) {
				// 已有的任务删掉
				missionIds.remove(worldMission.getMissionId());

				// 检查任务是否过期
				if (worldMission.isExpire() && worldMission
						.getStatus() != WorldMission.STATUS_FAIL) {
					worldMission.setCompleteLimitTime(now);
					worldMission.setStatus(WorldMission.STATUS_FAIL);
					worldMissionDao.updateWorldMission(worldMission);

					// 过期日志
					logService.log(LogBeanFactory.buildWorldMissionLog(
							worldMission.getMissionId(),
							worldMission.getMission().getMissionName(),
							worldMission.getStatus()));
				}

				// 绑定任务
				Mission mission = missionService
						.getMissionById(worldMission.getMissionId());
				worldMission.setMission(mission);

				// 已结束的任务添加子任务
				if (worldMission.getStatus() != WorldMission.STATUS_NEW) {
					List<Mission> childList = mission.getChildMissions();
					if (childList != null && childList.size() > 0) {
						for (Mission m : childList) {
							missionIds.add(m.getMissionId());
						}
					}
				}
			}
		}

		// 初始化新世界任务
		if (missionIds.size() > 0) {
			for (Integer missionId : missionIds) {
				Mission mission = missionService.getMissionById(missionId);
				if (mission.getMissionType() != null && mission
						.getMissionType().equals(Mission.MISSION_TYPE_WORLD)) {
					WorldMission wm = this.initWorldMission(mission, now);
					if (wm != null) {
						WORLD_MISSIONS.add(wm);
					}
				}
			}
		}

		// 初始化未完成任务所需的完成条件
		if (WORLD_MISSIONS != null && WORLD_MISSIONS.size() > 0) {
			for (WorldMission worldMission : WORLD_MISSIONS) {
				if (worldMission.getStatus() == WorldMission.STATUS_COMPLETE) {
					WORLD_MISSIONS_FINISHNUM = WORLD_MISSIONS_FINISHNUM + 1;
				}

				if (worldMission.getStatus() != WorldMission.STATUS_NEW) {
					continue;
				}

				Mission mission = worldMission.getMission();
				// 绑定线程
				worldMission.setWorker(
						workers[(int) (mission.getMissionId() % threadNum)]);

				// 按完成条件分队
				this.loadOtcType(worldMission, mission);
			}
		}

		// 状态有大到小排序
		if (WORLD_MISSIONS.size() > 1) {
			Collections.sort(WORLD_MISSIONS, statusComparator);
		}
		log.info("目前正在进行的世界任务有{}个,已经完成{}个", WORLD_MISSIONS.size(),
				WORLD_MISSIONS_FINISHNUM);
	}

	/**
	 * 销毁方法
	 */
	public void destory() {
		log.info("关闭世界任务{}个线程", threadNum);
		try {
			if (workers != null) {
				for (int i = 0; i < threadNum; i++) {
					if (workers[i] != null) {
						workers[i].shutDown();
					}
				}
			}
		} catch (Exception e) {
			log.error("WORLD_MISSION_线程关闭异常", e);
		}
		if (OTCTYPES != null) {
			OTCTYPES.clear();
		}
	}

	/**
	 * new任务
	 * 
	 * @param mission
	 * @param now
	 * @return
	 */
	private WorldMission initWorldMission(Mission mission, Timestamp now) {
		WorldMission worldMission = worldMissionDao
				.getWorldMission(mission.getMissionId());
		if (worldMission == null) {
			worldMission = new WorldMission();
			// 绑定任务
			worldMission.setMission(mission);
			worldMission.setMissionId(mission.getMissionId());
			worldMission.setFactor(1);
			worldMission.setStatus(UserMission.STATUS_NEW);// 表示没完成
			worldMission.setCompleteLimitTime(now);
			if (mission.getTimeLimit() > 0) {
				worldMission.setEndDttm(
						UtilDate.moveSecond(now, mission.getTimeLimit()));
			}
			worldMissionDao.insertWorldMission(worldMission);

			// 特殊事件初始化日志
			logService.log(LogBeanFactory.buildWorldMissionLog(
					mission.getMissionId(), mission.getMissionName(),
					worldMission.getStatus()));

			return worldMission;
		}
		return null;
	}

	/**
	 * 初始化完成条件映射
	 * 
	 * @param m
	 */
	private void loadOtcType(WorldMission wm, Mission m) {
		Map<String, List<MissionLimit>> limitMaps = m.getLimitMaps();
		if (limitMaps != null && limitMaps.size() > 0) {
			Iterator<String> itl = limitMaps.keySet().iterator();
			while (itl.hasNext()) {
				String key = itl.next();
				List<WorldMission> tmpList = OTCTYPES.get(key);
				if (tmpList == null) {
					tmpList = new ArrayList<WorldMission>();
					OTCTYPES.put(key, tmpList);
				}
				tmpList.add(wm);
			}
		}
	}

	@Override
	public void doGmtoolCreateMission(int missionId) {
		Mission mission = missionService.getMissionById(missionId);
		WorldMission wm = this.initWorldMission(mission,
				UtilDate.nowTimestamp());
		if (wm != null) {
			if (wm != null) {
				WORLD_MISSIONS.add(wm);
			}

			// 按完成条件分队
			this.loadOtcType(wm, mission);

			// 状态有大到小排序
			if (WORLD_MISSIONS.size() > 1) {
				Collections.sort(WORLD_MISSIONS, statusComparator);
			}
		}
	}

	/**
	 * 线程
	 * 
	 * @author Dagangzi
	 * 
	 */
	public class Worker extends Thread {
		private BlockingQueue<NotifyInfo> queue;// 阻塞队列
		private boolean start = true;

		public Worker(String name) {
			this.setDaemon(true);
			this.setName(name);
			queue = new LinkedBlockingQueue<NotifyInfo>();
		}

		public BlockingQueue<NotifyInfo> getQueue() {
			return queue;
		}

		public void setQueue(BlockingQueue<NotifyInfo> queue) {
			this.queue = queue;
		}

		public void put(NotifyInfo action) {
			try {
				queue.put(action);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void shutDown() {
			start = false;
			try {
				queue.put(new NotifyInfo());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			while (start) {
				try {
					NotifyInfo notifyInfo = queue.poll(30, TimeUnit.SECONDS);
					if (notifyInfo != null) {
						if (notifyInfo.getWorldMission() == null) {
							// 线程结束
							break;
						}
						IWorldMissionService wolf_worldMissionService = (IWorldMissionService) ServiceLocator
								.getSpringBean("wolf_worldMissionService");
						try {
							wolf_worldMissionService
									.doNotifyMissionModule(notifyInfo);
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				} catch (Exception e) {
					log.error("WORLD_MISSION线程发生异常", e);
				} finally {

				}
			}
			log.info("thread {} 终止运行.", this.getName());
		}
	}

	/**
	 * 任务通知的完成条件
	 * 
	 * @author Dagangzi
	 * @date 2016年1月11日
	 */
	public class NotifyInfo implements Serializable {
		private String otcType;
		private int entId;
		private int num;
		private WorldMission worldMission;

		public NotifyInfo() {
		}

		public NotifyInfo(String otcType, int entId, int num,
				WorldMission worldMission) {
			this.otcType = otcType;
			this.entId = entId;
			this.num = num;
			this.worldMission = worldMission;
		}

		public String getOtcType() {
			return otcType;
		}

		public void setOtcType(String otcType) {
			this.otcType = otcType;
		}

		public int getEntId() {
			return entId;
		}

		public void setEntId(int entId) {
			this.entId = entId;
		}

		public int getNum() {
			return num;
		}

		public void setNum(int num) {
			this.num = num;
		}

		public WorldMission getWorldMission() {
			return worldMission;
		}

		public void setWorldMission(WorldMission worldMission) {
			this.worldMission = worldMission;
		}
	}

	@Override
	public PagerResult getUserMissionByUserId(long userId,
			int pageNo) {
		PagerResult result = new PagerResult();
		int pageSize = result.getPageSize();
		int begin = pageNo * pageSize;
		int count = WORLD_MISSIONS.size();
		if (begin >= count) {
			// begin = 0;
			// result.setPageNo(0);
			throw new BaseException("没有更多的数据");
		}

		int total = 0;
		List<UserMissionView> list = new ArrayList<UserMissionView>();
		for (int i = begin; i < count; i++) {
			WorldMission worldMission = WORLD_MISSIONS.get(i);
			UserMissionView view = worldMission.getView(-1);
			// 查询君主领奖记录
			if (worldMission != null) {
				if (worldMission.getStatus() == WorldMission.STATUS_COMPLETE) {
					UserWorldMission userWorldMission = worldMissionDao
							.getUserWorldMission(userId,
									worldMission.getMissionId());
					if (userWorldMission != null) {
						view.setStatus(WorldMission.STATUS_COMMIT);
					} else {
						view.setStatus(WorldMission.STATUS_COMPLETE);
					}
				} else if (worldMission
						.getStatus() == WorldMission.STATUS_FAIL) {
					view.setStatus(WorldMission.STATUS_FAIL);
				} else {
					view.setStatus(WorldMission.STATUS_NEW);
				}
			} else {
				log.error("世界任务数据异常");
			}


			list.add(view);

			total = total + 1;
			if (total >= pageSize) {
				break;
			}
		}

		result.setPageNo(pageNo);
		result.setPageSize(pageSize);
		result.setTotal(count);
		result.setDatas(list);
		return result;
	}

	@Override
	public void asynNotifyMissionModule(String otcType, int entId, int num) {
		List<WorldMission> list = OTCTYPES.get(otcType);
		if (list == null || list.size() == 0) {
			// 没有这个类型的任务完成条件
			return;
		}

		for (WorldMission worldMission : list) {
			if (worldMission.getStatus() != WorldMission.STATUS_NEW) {
				continue;
			}
			worldMission.getWorker()
					.put(new NotifyInfo(otcType, entId, num, worldMission));
		}
	}

	@Override
	public void doNotifyMissionModule(NotifyInfo notifyInfo) {
		if (notifyInfo == null || notifyInfo.getWorldMission() == null) {
			return;
		}
		WorldMission parentWm = notifyInfo.getWorldMission();
		if (parentWm.getStatus() != WorldMission.STATUS_NEW) {
			return;
		}

		Mission parentMission = parentWm.getMission();
		if (parentMission == null) {
			return;
		}

		String otcType = notifyInfo.getOtcType();
		List<MissionLimit> limits = parentMission.getLimitsByOctType(otcType);
		if (limits == null) {
			return;
		}

		boolean changed = false;
		Timestamp now = UtilDate.nowTimestamp();
		if (parentWm.isExpire()) {
			parentWm.setStatus(WorldMission.STATUS_FAIL);
			changed = true;
		} else {
			Iterator<MissionLimit> lit = limits.iterator();
			while (lit.hasNext()) {
				MissionLimit limit = lit.next();
				int entId = notifyInfo.getEntId();
				int num = notifyInfo.getNum();
				if (entId == limit.getEntId()) {
					boolean chg = limit.getLimitChecker().check(null, parentWm,
							parentMission, limit, entId, num);
					if (chg) {
						changed = true;
					}
				}
			}
			if (this.missionIsFinished(null, parentWm, parentMission)) {
				parentWm.setStatus(UserMission.STATUS_COMPLETE);
				WORLD_MISSIONS_FINISHNUM = WORLD_MISSIONS_FINISHNUM + 1;
			}
		}

		// 刷新新任务
		if (changed) {
			parentWm.setCompleteLimitTime(now);
			worldMissionDao.updateWorldMission(parentWm);

			if (parentWm.getStatus() != WorldMission.STATUS_NEW) {
				List<Mission> childList = parentMission.getChildMissions();
				if (childList != null && childList.size() > 0) {
					for (Mission m : childList) {
						// 初始化任务
						WorldMission wm = this.initWorldMission(m, now);
						if (wm != null) {
							WORLD_MISSIONS.add(wm);

							// 按完成条件分队
							this.loadOtcType(wm, m);
						}
					}
				}

				// 状态有大到小排序
				if (WORLD_MISSIONS.size() > 1) {
					Collections.sort(WORLD_MISSIONS, statusComparator);
				}

				try {
					// 广播世界任务完成数
					int eventType = EventMessage.TYPE_WORLD_MISSIONS_FINISHNUM;
					messageService.sendBroadMessage(
							ChatChannelManager.CHANNEL_WORLD, null, eventType,
							new FinishCountBroadMsg(WORLD_MISSIONS_FINISHNUM,
									eventType));
				} catch (Exception e) {
					// TODO: handle exception
				}
			}

			logService.log(LogBeanFactory.buildWorldMissionLog(
					parentMission.getMissionId(),
					parentMission.getMissionName(), parentWm.getStatus()));
		}

	}

	/**
	 * 任务是否完成
	 * 
	 * @param uMission
	 * @param mission
	 * @return
	 */
	private boolean missionIsFinished(User user, WorldMission uMission,
			Mission mission) {

		Iterator<Map.Entry<MissionLimit, Integer>> lit = mission
				.getLimitIndexs().entrySet().iterator();
		while (lit.hasNext()) {
			Map.Entry<MissionLimit, Integer> entry = lit.next();
			MissionLimit limit = entry.getKey();
			int index = entry.getValue();
			if (limit.getLimitChecker().getLimitEntNum(user, uMission, mission,
					limit) > uMission.getNum(index)) {
				return false;
			}
		}

		return true;
	}

}

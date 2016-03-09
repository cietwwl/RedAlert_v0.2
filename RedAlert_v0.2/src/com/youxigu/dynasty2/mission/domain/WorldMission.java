package com.youxigu.dynasty2.mission.domain;

import java.sql.Timestamp;

import com.youxigu.dynasty2.mission.proto.UserMissionView;
import com.youxigu.dynasty2.mission.service.impl.WorldMissionService;

/**
 * 
 * @author Dagangzi
 * @date 2016年1月8日
 */
public class WorldMission extends UserMission {
	public static final int STATUS_NEW = 0;// 未完成
	public static final int STATUS_COMPLETE = 1; // 已经完成没提交
	public static final int STATUS_COMMIT = 2;// 已经完成提交过
	public static final int STATUS_FAIL = 3;// 失败

	/**
	 * 任务结束时间,启动时计算
	 */
	private Timestamp endDttm;

	/**
	 * 对应的任务
	 */
	private transient Mission mission;

	/**
	 * 对应的线程
	 */
	private transient WorldMissionService.Worker worker;

	/**
	 * 判断当前任务是否已经完成
	 * 
	 * @return
	 */
	public boolean hasComplete() {
		if (this.status == STATUS_COMPLETE)
			return true;
		if (this.status == STATUS_COMMIT)
			return true;
		if (this.status == STATUS_FAIL)
			return true;
		return false;
	}

	public Timestamp getEndDttm() {
		return endDttm;
	}

	public void setEndDttm(Timestamp endDttm) {
		this.endDttm = endDttm;
	}

	/**
	 * 是否过期
	 * 
	 * @return
	 */
	public boolean isExpire() {
		if (this.status == WorldMission.STATUS_NEW) {
			if (this.getEndDttm() != null && this.getEndDttm().getTime()
					- System.currentTimeMillis() < 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 剩余的秒
	 * 
	 * @return
	 */
	public long getRemainTime() {
		if (this.getEndDttm() == null) {
			return 0L;
		}
		return Math.max(0L,
				(this.getEndDttm().getTime() - System.currentTimeMillis())
						/ 1000L);
	}

	public Mission getMission() {
		return mission;
	}

	public void setMission(Mission mission) {
		this.mission = mission;
	}

	public WorldMissionService.Worker getWorker() {
		return worker;
	}

	public void setWorker(WorldMissionService.Worker worker) {
		this.worker = worker;
	}

	public UserMissionView getView(int eventType) {
		UserMissionView view = new UserMissionView(missionId, status, num1,
				num2, num3, num4, num5, completeNum, read0,
				this.getRemainTime(), eventType);
		return view;
	}
}

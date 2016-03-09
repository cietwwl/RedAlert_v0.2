package com.youxigu.dynasty2.activity.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibatis.sqlmap.engine.cache.memcached.broadcast.IBroadcastProducer;
import com.youxigu.dynasty2.activity.dao.IActivityDao;
import com.youxigu.dynasty2.activity.domain.BaseReward;
import com.youxigu.dynasty2.activity.domain.OperateActivity;
import com.youxigu.dynasty2.activity.enums.ActivityEffectType;
import com.youxigu.dynasty2.activity.enums.ActivityType;
import com.youxigu.dynasty2.activity.proto.ActivityView;
import com.youxigu.dynasty2.activity.proto.SendActivityRewardMsg;
import com.youxigu.dynasty2.activity.service.IGMOperateActivityService;
import com.youxigu.dynasty2.activity.service.IOperateActivityProcessor;
import com.youxigu.dynasty2.activity.service.IOperateActivityService;
import com.youxigu.dynasty2.activity.service.impl.ExtraProcessor.ExtraReward;
import com.youxigu.dynasty2.chat.ChatChannelManager;
import com.youxigu.dynasty2.chat.ChatMessage;
import com.youxigu.dynasty2.chat.EventMessage;
import com.youxigu.dynasty2.chat.client.IChatClientService;
import com.youxigu.dynasty2.core.wolf.IWolfClientService;
import com.youxigu.dynasty2.mail.service.IMailMessageService;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.dynasty2.util.EffectValue;
import com.youxigu.wolf.net.AsyncWolfTask;
import com.youxigu.wolf.node.job.Job;

/**
 * 文件名 OperateActivityService.java
 * 
 * 描 述 运营活动管理总服务
 * 
 * 时 间 2014-4-9
 * 
 * 作 者 huhaiquan
 * 
 * 版 本 v1.2
 */
public class OperateActivityService implements IOperateActivityService,
		IGMOperateActivityService {
	public static final Logger log = LoggerFactory
			.getLogger(OperateActivityService.class);

	private IActivityDao activityDao;
	private IBroadcastProducer broadcastMgr;

	private IChatClientService messageService;

	private IWolfClientService jobServerClient;

	protected IMailMessageService mailMessageService;
	/**
	 * 礼包配数:修改成并发map，在查询还修改中存在并发
	 */
	private Map<Long, OperateActivity> activityMap = new ConcurrentHashMap<Long, OperateActivity>();
	private Map<Integer, IOperateActivityProcessor> processors = new HashMap<Integer, IOperateActivityProcessor>();

	public void init() {
		List<OperateActivity> list = activityDao.getOperateActivitys();
		for (OperateActivity operateActivity : list) {
			IOperateActivityProcessor processor = processors
					.get(operateActivity.getType());
			BaseReward reward = processor.parseReward(
					operateActivity.getRewardContext(), operateActivity);
			operateActivity.setReward(reward);
			activityMap.put(operateActivity.getActId(), operateActivity);
		}
	}

	@Override
	public ActivityView doGetActById(long userId, long actId,
			Map<String, Object> params) {
		OperateActivity operateActivity = activityMap.get(actId);
		if (operateActivity == null) {
			throw new BaseException("活动已过期");
		}
		long cur = System.currentTimeMillis();
		if (cur < operateActivity.getStartTime().getTime()
				|| cur > operateActivity.getMaxTime().getTime()) {
			throw new BaseException("活动未开启");
		}
		IOperateActivityProcessor processor = processors.get(operateActivity
				.getType());
		return processor.doGetActById(userId, operateActivity);
	}

	@Override
	public ActivityView doReward(long userId, long actId,
			Map<String, Object> params) {
		OperateActivity operateActivity = activityMap.get(actId);
		if (operateActivity == null) {
			throw new BaseException("活动已过期");
		}
		long cur = System.currentTimeMillis();
		if (cur < operateActivity.getStartTime().getTime()
				|| cur > operateActivity.getMaxTime().getTime()) {
			throw new BaseException("活动未开启");
		}
		IOperateActivityProcessor processor = processors.get(operateActivity
				.getType());
		return processor.doReward(userId, operateActivity);
	}

	@Override
	public Map<String, Object> getActivitys(long userId) {
		Map<String, Object> activitys = new HashMap<String, Object>(4);
		Map<Long, OperateActivity> activityMap = this.activityMap;
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>(
				activitys.size());
		long cur = System.currentTimeMillis();
		for (Map.Entry<Long, OperateActivity> entry : activityMap.entrySet()) {
			if (entry.getValue().getStartTime().getTime() < cur
					&& cur < entry.getValue().getMaxTime().getTime()) {
				Map<String, Object> data = new HashMap<String, Object>(4);
				data.put("actId", entry.getValue().getActId());
				data.put("actName", entry.getValue().getActName());
				data.put("type", entry.getValue().getType());
				IOperateActivityProcessor processor = processors.get(entry
						.getValue().getType());
				if (processor.hasReward(userId, entry.getValue())) {
					data.put("isT", 1);
				} else {
					data.put("isT", 0);
				}
				dataList.add(data);
			}
		}

		activitys.put("actList", dataList);
		return activitys;
	}

	@Override
	public void addActivity(OperateActivity activity) {
		IOperateActivityProcessor processor = processors
				.get(activity.getType());
		BaseReward reward = processor.parseReward(activity.getRewardContext(),
				activity);
		activity.setReward(reward);
		activityDao.createOperateActivity(activity);
		activityMap.put(activity.getActId(), activity);
		this.broadcastActivity(activity.getActId());
		this.startJob(activity);

	}

	@Override
	public void deleteActivity(OperateActivity activity) {
		activityMap.remove(activity.getActId());
		activityDao.deleteOperateActivity(activity.getActId());
		this.broadcastActivity(activity.getActId());
		this.delJob(activity);
		this.sendEventMessage(false, activity.getActId());

	}

	@Override
	public void updateActivity(OperateActivity activity) {
		IOperateActivityProcessor processor = processors
				.get(activity.getType());
		BaseReward reward = processor.parseReward(activity.getRewardContext(),
				activity);
		activity.setReward(reward);
		activityDao.updateOperateActivity(activity);
		activityMap.put(activity.getActId(), activity);
		this.broadcastActivity(activity.getActId());
		this.delJob(activity);
		this.startJob(activity);

	}

	@Override
	public void broadcastActivity(long actId) {

		if (broadcastMgr != null) {
			broadcastMgr.sendNotification(new AsyncWolfTask(
					"operateActivityService", "reloadRechargeActivity",
					new Object[] { actId }));
		}
	}

	public void reloadRechargeActivity(long actId) {
		OperateActivity act = activityDao.getOperateActivity(actId);
		if (act == null) {
			activityMap.remove(actId);
		} else {
			IOperateActivityProcessor processor = processors.get(act.getType());
			BaseReward reward = processor.parseReward(act.getRewardContext(),
					act);
			act.setReward(reward);
			activityMap.put(actId, act);
		}
	}

	@Override
	public void doStartActivitys() {
		for (OperateActivity act : activityMap.values()) {
			this.startJob(act);
		}
	}

	private void delJob(OperateActivity act) {
		jobServerClient.deleteJob(
				"JOB_OPERATE_ACTIVITY_OPEN_" + act.getActId(),
				"JOB_OPERATE_ACTIVITY");
		if (act.isAutoReward()) {
			jobServerClient.deleteJob(
					"JOB_OPERATE_ACTIVITY_REWARD_" + act.getActId(),
					"JOB_OPERATE_ACTIVITY");
		}
	}

	private void startJob(OperateActivity act) {
		long cur = System.currentTimeMillis();
		if (cur > act.getMaxTime().getTime()) {// 如果已经过期，不需要冲启动job
			if (!act.isPublicAutoReward() && act.isAutoReward()) {
				autoRewardActivity(act);
			}
			return;
		}
		// 活动未开启
		if (cur < act.getStartTime().getTime()) {// 开启通知job
			Job job = new Job();
			job.setJobGroupName("JOB_OPERATE_ACTIVITY_OPEN_" + act.getActId());
			job.setJobIdInGroup("JOB_OPERATE_ACTIVITY");
			job.setJobExcuteTime(act.getStartTime().getTime());
			job.setJobParams(new Object[] { 1, act.getActId() });
			job.setJobType(Job.JOB_RAM);
			job.setServiceName("operateActivityService");
			job.setMethodName("sendEventMessage");
			// 异步启动
			jobServerClient.startJob(job, true);
			log.info("活动启动时间:{},活动名称:{}", act.getStartTime(), act.getActName());
		}
		if (cur < act.getMaxTime().getTime()) {// 开启通知job
			Job job = new Job();
			job.setJobGroupName("JOB_OPERATE_ACTIVITY_CLOSE_" + act.getActId());
			job.setJobIdInGroup("JOB_OPERATE_ACTIVITY");
			job.setJobExcuteTime(act.getMaxTime().getTime());
			job.setJobParams(new Object[] { 0, act.getActId() });
			job.setJobType(Job.JOB_RAM);
			job.setServiceName("operateActivityService");
			job.setMethodName("sendEventMessage");
			// 异步启动
			jobServerClient.startJob(job, true);
			log.info("活动启动时间:{},活动名称:{}", act.getStartTime(), act.getActName());
		}
		if (cur < act.getEndTime().getTime() && act.isAutoReward()) {
			autoRewardActivity(act);
		}
		if (act.getStartTime().getTime() < cur
				&& cur < act.getEndTime().getTime()) {
			this.sendEventMessage(true, act.getActId());
		}
	}

	/**
	 * 构建自动发奖的调度任务
	 * 
	 * @param act
	 */
	private void autoRewardActivity(OperateActivity act) {
		Job job = new Job();
		job.setJobGroupName("JOB_OPERATE_ACTIVITY_REWARD_" + act.getActId());
		job.setJobIdInGroup("JOB_OPERATE_ACTIVITY");
		job.setJobExcuteTime(act.getEndTime().getTime());
		job.setJobParams(new Object[] { act.getActId() });
		job.setJobType(Job.JOB_RAM);
		job.setServiceName("operateActivityService");
		job.setMethodName("doAutoReward");
		// 异步启动
		jobServerClient.startJob(job, true);
		log.info("活动自动领奖时间时间:{},活动名称:{}", act.getEndTime(), act.getActName());
	}

	@Override
	public void sendEventMessage(boolean open, long actId) {
		messageService.sendEventMessage(0,
				EventMessage.TYPE_MUTI_RECHARGE_ACTIVITY,
				new SendActivityRewardMsg(actId, open));
		// 好鸡肋啊 咋加只有 限时活动 才发送这个，如果再有发送，就需要加接口了，先临时加到这
		OperateActivity operateActivity = activityMap.get(actId);
		sendTimerShop(operateActivity, open);

	}

	private boolean sendTimerShop(OperateActivity operateActivity, boolean open) {
		if (operateActivity != null && operateActivity.getType() == 8) {
			ExtraReward rwd = (ExtraReward) operateActivity.getReward();
			if (rwd.effectId == ActivityEffectType.TIMER_SHOP.getEffectId()) {
				messageService.sendEventMessage(0,
						EventMessage.TYPE_TIMER_SHOP_START_MSG,
						new SendActivityRewardMsg(operateActivity.getActId(),
								open));
				if (open) {
					ChatMessage chatMessage = new ChatMessage(0, 0,
							ChatChannelManager.CHANNEL_NOTICE, "system",
							"城里出现了一位神秘商人，他带的货物有限，快去看看吧！",
							ChatMessage.PRIORITY_MIN);

					messageService.sendMessage(chatMessage);
				}

				return true;
			}
		}
		return false;
	}

	@Override
	public void doAutoReward(long actId) {
		OperateActivity act = this.getOperateActivity(actId);
		if (act != null && act.isAutoReward()) {
			if (act.isPublicAutoReward()) {
				log.error("自动领奖已经发放。。。活动id:{},status:{}", actId,
						act.getStatus());
				return;
			}
			try {
				IOperateActivityProcessor processor = processors.get(act
						.getType());
				processor.doAutoReward(act);
				// 自动发奖成功标记下..
				act.updatePublicAutoReward();
				activityDao.updateOperateActivity(act);
			} catch (RuntimeException e) {
				log.error("自动领奖出错。。。活动id:{}", actId);
				log.error("自动领奖出错", e);
			}
		}
	}

	@Override
	public OperateActivity getOperateActivity(long actId) {
		return activityMap.get(actId);
	}

	public void setActivityDao(IActivityDao activityDao) {
		this.activityDao = activityDao;
	}

	public void setBroadcastMgr(IBroadcastProducer broadcastMgr) {
		this.broadcastMgr = broadcastMgr;
	}

	public void setMessageService(IChatClientService messageService) {
		this.messageService = messageService;
	}

	public void setJobServerClient(IWolfClientService jobServerClient) {
		this.jobServerClient = jobServerClient;
	}

	public void setMailMessageService(IMailMessageService mailMessageService) {
		this.mailMessageService = mailMessageService;
	}

	public void setProcessors(Map<Integer, IOperateActivityProcessor> processors) {
		this.processors = processors;
	}

	@Override
	public void addCashNotify(long userId) {
		this.sendMessage(userId, IOperateActivityProcessor.NOTICE_1);

	}

	@Override
	public void consumeNotify(long userId) {
		sendMessage(userId, IOperateActivityProcessor.NOTICE_2);

	}

	@Override
	public void loginNotify(long userId) {
		sendMessage(userId, IOperateActivityProcessor.NOTICE_3);
	}

	private void sendMessage(long userId, int type) {
		long cur = System.currentTimeMillis();
		boolean has = false;
		for (Map.Entry<Long, OperateActivity> entry : activityMap.entrySet()) {
			if (entry.getValue().getStartTime().getTime() < cur
					&& cur < entry.getValue().getMaxTime().getTime()) {
				IOperateActivityProcessor processor = processors.get(entry
						.getValue().getType());
				// if 是充值或者是消费，
				boolean isSend = (type & processor.getNoticeType()) != 0;
				if (isSend && processor.hasReward(userId, entry.getValue())) {
					messageService.sendEventMessage(userId,
							EventMessage.TYPE_ACTIVITY_REWARD_MSG,
							new SendActivityRewardMsg(entry.getKey()));
				}

				if (entry.getValue().getType() == 8) {
					ExtraReward rwd = (ExtraReward) entry.getValue()
							.getReward();
					if (rwd.effectId == ActivityEffectType.TIMER_SHOP
							.getEffectId()) {
						has = true;
					}
				}

			}
		}
		// 如果登陆的时候没有限时商城， 发送没有活动
		if (IOperateActivityProcessor.NOTICE_3 == type) {
			messageService.sendEventMessage(userId,
					EventMessage.TYPE_TIMER_SHOP_START_MSG,
					new SendActivityRewardMsg(0, has));
		}
	}

	@Override
	public EffectValue getEffectValue(ActivityEffectType effect) {
		long cur = System.currentTimeMillis();
		EffectValue effectValue = new EffectValue();
		for (Map.Entry<Long, OperateActivity> entry : activityMap.entrySet()) {
			boolean isTrue = ActivityType.EXTRA_ACTIVITY_TYPE.equals(entry
					.getValue().getActivityType())
					&& entry.getValue().getStartTime().getTime() < cur
					&& cur < entry.getValue().getMaxTime().getTime();
			if (isTrue) {
				ExtraReward reward = (ExtraReward) entry.getValue().getReward();
				if (reward.effectId == effect.getEffectId()) {
					if (reward.isPercent) {
						effectValue.setPerValue(effectValue.getAbsValue()
								+ reward.effectVal);
					} else {
						effectValue.setAbsValue(effectValue.getAbsValue()
								+ reward.effectVal);
					}
				}
			}
		}
		return effectValue;
	}

	/**
	 * 获得加成后的效果值
	 * 
	 * @param base
	 *            加成前
	 * @param effect
	 *            应该计算的效果
	 * @return
	 */
	public int getExpByActivityEffect(int base, ActivityEffectType effect) {
		EffectValue val = this.getEffectValue(effect);
		int temp = base;
		if (val.getAbsValue() != 0 || val.getPerValue() != 0) {
			temp = (int) ((base + val.getAbsValue())
					* (100 + val.getPerValue()) / 100f);
		}

		return temp;
	}

}

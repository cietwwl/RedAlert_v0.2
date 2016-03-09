package com.youxigu.dynasty2.activity.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibatis.sqlmap.engine.cache.memcached.broadcast.IBroadcastProducer;
import com.manu.util.UtilDate;
import com.youxigu.dynasty2.activity.dao.IActivityDao;
import com.youxigu.dynasty2.activity.domain.Activity;
import com.youxigu.dynasty2.activity.domain.AwardActivity;
import com.youxigu.dynasty2.activity.service.IActivityService;
import com.youxigu.dynasty2.common.service.ICommonService;
import com.youxigu.dynasty2.develop.domain.Castle;
import com.youxigu.dynasty2.develop.service.ICastleService;
import com.youxigu.dynasty2.openapi.Constant;
import com.youxigu.dynasty2.user.dao.IAccountDao;
import com.youxigu.dynasty2.user.dao.IUserDao;
import com.youxigu.dynasty2.user.domain.Account;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.util.DateUtils;
import com.youxigu.dynasty2.util.EffectValue;
import com.youxigu.wolf.net.AsyncWolfTask;

import edu.emory.mathcs.backport.java.util.Collections;

public class ActivityService implements IActivityService {
	public static final Logger log = LoggerFactory
			.getLogger(ActivityService.class);

	/**
	 * 缓存所有的运营活动
	 * 
	 */
	private static ActivityComparator activityCompartor = new ActivityComparator();
	private Map<Integer, Activity> activityMaps = new ConcurrentHashMap<Integer, Activity>();

	// /这个缓存可能线程不安全，有时候遍历会出错
	private List<Activity> activitys = null;
	private IActivityDao activityDao;
	private IUserDao userDao;
	private IAccountDao accountDao;
	private ICastleService castleService;
	private ICommonService commonService;
	private IBroadcastProducer broadcastMgr;

	/**
	 * 奖励活动缓存
	 */
	private Map<Integer, AwardActivity> awardActivityMaps = new ConcurrentHashMap<Integer, AwardActivity>();

	public void setCommonService(ICommonService commonService) {
		this.commonService = commonService;
	}

	public void setCastleService(ICastleService castleService) {
		this.castleService = castleService;
	}

	public void setBroadcastMgr(IBroadcastProducer broadcastMgr) {
		this.broadcastMgr = broadcastMgr;
	}

	public void setActivityDao(IActivityDao activityDao) {
		this.activityDao = activityDao;
	}

	public void setUserDao(IUserDao userDao) {
		this.userDao = userDao;
	}

	public void setAccountDao(IAccountDao accountDao) {
		this.accountDao = accountDao;
	}

	public void init() {
		log.info("加载运营活动.......");
		activitys = activityDao.getActivitys();
		Collections.sort(activitys, activityCompartor);
		for (Activity a : activitys) {
			a.init();
			activityMaps.put(a.getActId(), a);
		}

		List<AwardActivity> aas = activityDao.getAwardActivitys();
		for (AwardActivity a : aas) {
			awardActivityMaps.put(a.getId(), a);
		}
		log.info("加载运营活动完毕.......");
	}

	//
	// @Override
	// public List<Activity> getActivitys(Date date) {
	// Calendar cal = Calendar.getInstance();
	// cal.setTime(date);
	// return getActivitys(cal);
	// }

	// @Override
	// public List<Activity> getActivitys(Calendar cal){
	// if (activityMaps.size() > 0) {
	// List<Activity> validActivitys = new ArrayList<Activity>();
	// Iterator<Activity> lit = activityMaps.values().iterator();
	// while (lit.hasNext()) {
	// Activity a = lit.next();
	// if(checkActivity(a,cal,0)){
	// validActivitys.add(a);
	// }
	// }
	// return validActivitys;
	// }
	// return null;
	//
	// }
	/**
	 * 检查此活动在当前时间是否是开放的
	 * 
	 * @param a
	 * @param cal
	 *            活动开放的条件：<br>
	 *            1、活动是开启状态<br>
	 *            2、日期条件介于【开始日期时间】和【结束日期时间】之间，格式：yyyy-MM-dd HH:mm:ss<br>
	 *            3、时间条件介于【开始时间】和【结束时间】之间，格式：HH:mm:ss <br>
	 *            4、星期条件介于【开始星期】和【结束星期】之间，格式：1-7 其中 7 是表示星期天<br>
	 *            5、特权 6、平台 7、君主等级 8、城池等级
	 * @return true=开放 false=不开放
	 */
	private boolean checkActivity(Activity a, Calendar cal, long userId) {
		boolean open = false;
		if (a.getStatus() == Activity.STATUS_OPEN) {
			Calendar startDttm = Calendar.getInstance();
			startDttm.clear();
			startDttm.setTime(a.getStartDttm());
			Calendar endDttm = Calendar.getInstance();
			endDttm.clear();
			endDttm.setTime(a.getEndDttm());
			if (cal.after(startDttm) && cal.before(endDttm)) {// 满足日期条件
				Timestamp timeStart = UtilDate.moveSecond(
						DateUtils.getNowZeroDttm(), a.getTimeStart());
				Timestamp endStart = UtilDate.moveSecond(
						DateUtils.getNowZeroDttm(), a.getTimeEnd());
				startDttm.clear();
				startDttm.setTime(timeStart);
				endDttm.clear();
				endDttm.setTime(endStart);
				if (cal.after(startDttm) && cal.before(endDttm)) {// 满足时间条件
					int week = cal.get(Calendar.DAY_OF_WEEK) - 1;// 当前是周几
					if (week == 0) {
						week = 7;
					}
					// GM管理系统保存按西方日里周日-周一（1-7）来表示的 ,操作人员是按照周一 - 周日=1-7来理解的
					int startWeek = a.getWeekStart();
					int endWeek = a.getWeekEnd();
					if (startWeek == 1) {
						startWeek = 7;
					} else {
						startWeek--;
					}
					if (endWeek == 1) {
						endWeek = 7;
					} else {
						endWeek--;
					}
					if (week >= startWeek && week <= endWeek) {
						if (userId > 0) {
							User user = userDao.getUserById(userId);
							Account account = accountDao.getAccountByAccId(user
									.getAccId());

							// 特权里每项是或的关系，其它条件之前的关系是由a.getRelation()控制

							// 特权
							open = checkPrivilege(account, a, a.getRelation());

							// 平台
							boolean pf = checkChannel(account, a);

							// 用户等级
							boolean usrLv = checkConditionAnd(a.getUsrLv(),
									user.getUsrLv());

							// 城池等级
							boolean castLv = false;
							Castle c = castleService.getCastleById(user
									.getMainCastleId());
							if (c != null) {
								// TODO 删除了代码
								// castLv = checkConditionAnd(a.getCasteLv(),
								// c.getCasLv());
								castLv = checkConditionAnd(a.getCasteLv(),
										user.getUsrLv());
							}

							boolean tmp = false;
							if (a.getRelation() == Activity.RELATION_AND) {// 且
								tmp = usrLv && castLv && pf;
							} else {
								tmp = usrLv || castLv || pf;
							}
							open = tmp && open;
						} else {
							open = true;
						}
					}
				}
			}
		}
		return open;
	}

	/**
	 * 检查平台
	 * 
	 * @param account
	 * @param a
	 * @return
	 */
	private boolean checkChannel(Account account, Activity a) {
		boolean open = false;
		if (StringUtils.isEmpty(a.getChannel())) {// 对所有平台开放
			open = true;
		} else {
			String pf = Constant.getPfEx(account.getPf());
			if (pf != null) {
				open = a.getChannel().contains(pf);
			}
		}
		return open;
	}

	/**
	 * 检查特权 参数relation 暂时没用
	 */
	private boolean checkPrivilege(Account account, Activity a, byte relation) {
		if (!a.isCheckPrivilege()) {
			return true;
		}
		// 否则 该活动必定设置了一种特权

		boolean q_y = false;
		// 黄钻
		if ((account.getQqCurrFlag() & Account.QQ_YELLOW) == Account.QQ_YELLOW) {
			q_y = checkConditionOr(a.getQqYellowLv(), account.getQqYellowLv());
		}

		// 年费黄钻
		if ((account.getQqCurrFlag() & Account.QQ_YELLOW) == Account.QQ_YELLOW) {
			if ((account.getQqCurrFlag() & Account.QQ_YELLOW_YEAR) == Account.QQ_YELLOW_YEAR) {
				q_y = q_y
						&& checkConditionOr(a.getQqYellowLvYear(),
								account.getQqYellowLv());
			}
		}
		// 超级黄钻
		if ((account.getQqCurrFlag() & Account.QQ_YELLOW_HIGH) == Account.QQ_YELLOW_HIGH) {
			q_y = q_y
					&& checkConditionOr(a.getQqYellowLvHigh(),
							account.getQqYellowLv());
		}
		boolean q_b = false;
		// 蓝钻
		if ((account.getQqCurrFlag() & Account.QQ_BLUE) == Account.QQ_BLUE) {
			q_b = checkConditionOr(a.getQqBlueLv(), account.getQqBlueLv());
		}
		// 年费蓝钻
		if ((account.getQqCurrFlag() & Account.QQ_BLUE) == Account.QQ_BLUE) {
			if ((account.getQqCurrFlag() & Account.QQ_BLUE_YEAR) == Account.QQ_BLUE_YEAR) {
				q_b = checkConditionOr(a.getQqBlueLvYear(),
						account.getQqBlueLv());
			}
		}

		if ((account.getQqCurrFlag() & Account.QQ_BLUE_HIGH) == Account.QQ_BLUE_HIGH) {
			q_y = q_y
					&& checkConditionOr(a.getQqBlueLvHigh(),
							account.getQqBlueLv());
		}

		boolean q_pl = false;
		if ((account.getQqCurrFlag() & Account.QQ_PLUS) == Account.QQ_PLUS) {
			q_pl = checkConditionOr(a.getQqPlusLv(), account.getQqPlusLv());
		}
		boolean q_l = checkConditionOr(a.getQqLv(), account.getQqLv());

		boolean q_v = false;
		// QQ会员
		if ((account.getQqCurrFlag() & Account.QQ_VIP) == Account.QQ_VIP) {
			q_v = checkConditionOr(a.getQqVipLv(), account.getQqVipLv());
		}
		// QQ年费会员
		if ((account.getQqCurrFlag() & Account.QQ_VIP) == Account.QQ_VIP) {
			if ((account.getQqCurrFlag() & Account.QQ_VIP_YEAR) == Account.QQ_VIP_YEAR) {
				q_v = q_v
						&& checkConditionOr(a.getQqVipLvYear(),
								account.getQqVipLv());
			}
		}
		boolean q_3 = false;
		if ((account.getQqCurrFlag() & Account.QQ_3366) == Account.QQ_3366) {
			q_3 = checkConditionOr(a.getQq3366Lv(), account.getQq3366Lv());
		}
		boolean q_r = false;
		if ((account.getQqCurrFlag() & Account.QQ_RED) == Account.QQ_RED) {
			q_r = checkConditionOr(a.getQqRedLv(), account.getQqRedLv());
		}
		boolean q_g = false;
		if ((account.getQqCurrFlag() & Account.QQ_GREEN) == Account.QQ_GREEN) {
			q_g = checkConditionOr(a.getQqGreenLv(), account.getQqGreenLv());
		}
		boolean q_pi = false;
		// 粉 钻
		if ((account.getQqCurrFlag() & Account.QQ_PINK) == Account.QQ_PINK) {
			q_pi = checkConditionOr(a.getQqPinkLv(), account.getQqPinkLv());
		}
		// 年费粉钻
		if ((account.getQqCurrFlag() & Account.QQ_PINK) == Account.QQ_PINK) {
			if ((account.getQqCurrFlag() & Account.QQ_PINK_YEAR) == Account.QQ_PINK_YEAR) {
				q_pi = q_pi
						&& checkConditionOr(a.getQqPinkLvYear(),
								account.getQqPinkLv());
			}
		}
		boolean q_s = false;
		// 超级QQ
		if ((account.getQqCurrFlag() & Account.QQ_SUPER) == Account.QQ_SUPER) {
			q_s = checkConditionOr(a.getQqSuperLv(), account.getQqSuperLv());
		}

		// if(a.getRelation()== Activity.RELATION_AND){//且
		// return q_y && q_b && q_pl && q_l && q_v && q_3 && q_r && q_g && q_pi
		// && q_s;
		// }else{
		return q_y || q_b || q_pl || q_l || q_v || q_3 || q_r || q_g || q_pi
				|| q_s;
		// }
	}

	public boolean checkConditionOr(String actLv, int accountLv) {
		boolean open = false;
		if (actLv == null || actLv.length() == 0) {
			return false;
		}
		if (accountLv == 0) {
			return false;
		}
		String[] lv = StringUtils.split(actLv, ",");
		int min = 1;
		int max = Integer.MAX_VALUE;
		if (lv.length >= 2) {
			try {
				min = Integer.valueOf(lv[0]);
			} catch (Exception e) {
			}
			try {
				max = Integer.valueOf(lv[1]);
			} catch (Exception e) {
			}

		}

		if (accountLv >= min && accountLv <= max) {
			open = true;
		}
		return open;
	}

	public boolean checkConditionAnd(String actLv, int accountLv) {
		boolean open = false;
		if (actLv == null || actLv.length() == 0) {
			return true;
		}
		if (accountLv == 0) {
			return false;
		}
		String[] lv = StringUtils.split(actLv, ",");
		int min = 1;
		int max = Integer.MAX_VALUE;
		if (lv.length >= 2) {
			try {
				min = Integer.valueOf(lv[0]);
			} catch (Exception e) {
			}
			try {
				max = Integer.valueOf(lv[1]);
			} catch (Exception e) {
			}

		}

		if (accountLv >= min && accountLv <= max) {
			open = true;
		}
		return open;
	}

	@Override
	public List<Map<String, Object>> getActivityMaps(Calendar cal, long userId,
			String pf) {
		List<Map<String, Object>> validActivityMaps = new ArrayList<Map<String, Object>>();
		Iterator<Activity> lit = activitys.iterator();

		long beginTime = cal.getTimeInMillis();// 今天的0点
		long endTime = beginTime + 24L * 3600 * 1000 - 1000L;// 今天的23:59:59点
		int currWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (currWeekDay == 0) {
			currWeekDay = 7;
		}

		while (lit.hasNext()) {
			Activity a = lit.next();
			if (a.getStatus() != Activity.STATUS_OPEN) {
				continue;
			}

			// GM管理系统保存按西方日里周日-周一（1-7）来表示的 ,操作人员是按照周一 - 周日=1-7来理解的
			int startWeek = a.getWeekStart();
			int endWeek = a.getWeekEnd();
			if (startWeek == 1) {
				startWeek = 7;
			} else {
				startWeek--;
			}
			if (endWeek == 1) {
				endWeek = 7;
			} else {
				endWeek--;
			}
			if (currWeekDay < startWeek || currWeekDay > endWeek) {
				continue;
			}

			long begin = 0;
			long end = Long.MAX_VALUE;
			Timestamp tmp = a.getStartDttm();
			if (tmp != null) {
				begin = tmp.getTime();
			}
			tmp = a.getEndDttm();
			if (tmp != null) {
				end = tmp.getTime();
			}
			if ((endTime > begin) && beginTime < end) {
				// 判断平台
				String actPf = a.getChannel();
				if (actPf != null && actPf.length() != 0) {
					if (actPf.indexOf(pf) < 0) {
						continue;
					}
				}
				// 判断符合的Week_DAY

				// if(checkActivity( a, cal,userId)){
				Map<String, Object> map = new HashMap<String, Object>(4);
				map.put("actId", a.getActId());
				map.put("name", a.getName() == null ? "" : a.getName());
				map.put("desc",
						a.getDescription() == null ? "" : a.getDescription());
				map.put("url", a.getUrl() == null ? "" : a.getUrl());
				map.put("icon", a.getIcon() == null ? "" : a.getIcon());

				// 活动起点时间
				long timeStart = beginTime + a.getTimeStart() * 1000L;
				long timeEnd = beginTime + a.getTimeEnd() * 1000L;

				map.put("startDttm", timeStart / 1000);
				map.put("endDttm", timeEnd / 1000);
				// map.put("usrLv", a.getUsrLv());
				// map.put("casteLv", a.getCasteLv());
				// map.put("qqYellowLv", a.getQqYellowLv());
				// map.put("qqYellowLvYear", a.getQqYellowLvYear());
				// map.put("qqYellowLvHigh", a.getQqYellowLvHigh());
				// map.put("qqBlueLv", a.getQqBlueLv());
				// map.put("qqBlueLvYear", a.getQqBlueLvYear());
				// map.put("qqPlusLv", a.getQqPlusLv());
				// map.put("qqLv", a.getQqLv());
				// map.put("qqVipLvYear", a.getQqVipLvYear());
				// map.put("qqVipLv", a.getQqVipLv());
				// map.put("qq3366Lv", a.getQq3366Lv());
				// map.put("qqRedLv", a.getQqRedLv());
				// map.put("qqGreenLv", a.getQqGreenLv());
				// map.put("qqPinkLv", a.getQqPinkLv());
				// map.put("qqPinkLvYear", a.getQqPinkLvYear());
				// map.put("qqSuperLv", a.getQqSuperLv());
				// map.put("pf", a.getChannel()==null?"":a.getChannel());
				validActivityMaps.add(map);
			}
		}
		return validActivityMaps;

	}

	@Override
	public EffectValue getSumEffectValueByEffectType(long userId,
			String effTypeId) {
		int abs = 0;
		int percent = 0;
		Iterator<Activity> lit = activityMaps.values().iterator();
		long now = System.currentTimeMillis();
		while (lit.hasNext()) {
			Activity a = lit.next();
			Calendar cal = Calendar.getInstance();
			cal.clear();
			cal.setTimeInMillis(now);
			if (checkActivity(a, cal, userId)) {
				String[] effIdsPara = StringUtils.split(a.getEfId(), ";");
				for (String efIds : effIdsPara) {
					if (!StringUtils.isEmpty(efIds)) {
						String[] efId = StringUtils.split(efIds, ":");
						if (effTypeId.equals(efId[0])) {
							int maxValue = commonService.getSysParaIntValue(
									effTypeId, 9999);
							String[] param = StringUtils.split(efId[1], ",");
							int ab = Integer.valueOf(param[0]);
							int p = Integer.valueOf(param[1]);
							percent = percent + Math.min(p, maxValue);
							abs = abs + Math.min(ab, maxValue);
						}
					}
				}
			}
		}
		return new EffectValue(abs, percent);
	}

	@Override
	public int getSumEffectAbsValueByEffectType(long userId, String effTypeId) {
		int value = 0;
		Iterator<Activity> lit = activityMaps.values().iterator();
		long now = System.currentTimeMillis();
		while (lit.hasNext()) {
			Activity a = lit.next();
			Calendar cal = Calendar.getInstance();
			cal.clear();
			cal.setTimeInMillis(now);
			if (checkActivity(a, cal, userId)) {
				String[] effIdsPara = StringUtils.split(a.getEfId(), ";");
				for (String efIds : effIdsPara) {
					if (!StringUtils.isEmpty(efIds)) {
						String[] efId = StringUtils.split(efIds, ":");
						if (effTypeId.equals(efId[0])) {
							int maxValue = commonService.getSysParaIntValue(
									effTypeId, 9999);
							String[] param = StringUtils.split(efId[1], ",");
							int p = Integer.valueOf(param[0]);
							value = value + Math.min(p, maxValue);
						}
					}
				}
			}
		}

		return value;
	}

	@Override
	public int getSumEffectPercentValueByEffectType(long userId,
			String effTypeId) {
		int value = 0;
		Iterator<Activity> lit = activityMaps.values().iterator();
		long now = System.currentTimeMillis();
		while (lit.hasNext()) {
			Activity a = lit.next();
			Calendar cal = Calendar.getInstance();
			cal.clear();
			cal.setTimeInMillis(now);
			if (checkActivity(a, cal, userId)) {
				String[] effIdsPara = StringUtils.split(a.getEfId(), ";");
				for (String efIds : effIdsPara) {
					if (!StringUtils.isEmpty(efIds)) {
						String[] efId = StringUtils.split(efIds, ":");
						if (effTypeId.equals(efId[0])) {
							int maxValue = commonService.getSysParaIntValue(
									effTypeId, 9999);
							String[] param = StringUtils.split(efId[1], ",");
							int p = Integer.valueOf(param[1]);
							value = value + Math.min(p, maxValue);
						}
					}
				}
			}
		}
		return value;
	}

	@Override
	public void reloadActivity(int actId, boolean broadcast) {
		Activity act = activityDao.getActivity(actId);
		if (act != null) {
			Iterator<Activity> lit = activitys.iterator();
			while (lit.hasNext()) {
				Activity tmp = lit.next();
				if (tmp.getActId() == actId) {
					lit.remove();
					break;
				}
			}
			act.init();
			activitys.add(act);
			Collections.sort(activitys, activityCompartor);

			activityMaps.put(act.getActId(), act);

			if (broadcast && broadcastMgr != null) {
				broadcastMgr.sendNotification(new AsyncWolfTask(
						"activityService", "reloadActivity", new Object[] {
								actId, false }));
			}
		} else {
			log.warn("运营活动{}不存在.", actId);
		}
	}

	@Override
	public void createActivity(Activity activity) {
		activityDao.createActivity(activity);
		reloadActivity(activity.getActId(), true);
	}

	@Override
	public void updateActivity(Activity activity) {
		activityDao.updateActivity(activity);
		reloadActivity(activity.getActId(), true);
	}

	@Override
	public void deleteActivity(int actId, boolean broadCast) {
		activityDao.deleteActivity(actId);

		Iterator<Activity> lit = activitys.iterator();
		while (lit.hasNext()) {
			Activity tmp = lit.next();
			if (tmp.getActId() == actId) {
				lit.remove();
				break;
			}
		}
		activityMaps.remove(actId);

		if (broadCast && broadcastMgr != null) {
			broadcastMgr.sendNotification(new AsyncWolfTask("activityService",
					"deleteActivity", new Object[] { actId, false }));
		}
	}

	@Override
	public List<Activity> getActivitys() {
		return activityDao.getActivitys();
	}

	@Override
	public Activity getActivityById(int actId) {
		Activity act = activityDao.getActivity(actId);
		return act;
	}

	static class ActivityComparator implements Comparator<Activity> {

		@Override
		public int compare(Activity o1, Activity o2) {

			return o1.getActId() - o2.getActId();
		}

	}

	@Override
	public void createAwardActivity(AwardActivity activity) {
		activityDao.createAwardActivity(activity);
		reloadAwardActivity(activity.getId(), true);

	}

	@Override
	public void deleteAwardActivity(int actId, boolean broadCast) {
		activityDao.deleteAwardActivity(actId);

		awardActivityMaps.remove(actId);

		if (broadCast && broadcastMgr != null) {
			broadcastMgr.sendNotification(new AsyncWolfTask("activityService",
					"deleteAwardActivity", new Object[] { actId, false }));
		}

	}

	@Override
	public AwardActivity getAwardActivityById(int actId) {
		AwardActivity act = awardActivityMaps.get(actId);
		if (act == null) {
			act = activityDao.getAwardActivity(actId);
		}
		return act;
	}

	@Override
	public Collection<AwardActivity> getAwardActivitys() {
		return awardActivityMaps.values();
	}

	@Override
	public void reloadAwardActivity(int actId, boolean broadcast) {
		AwardActivity act = activityDao.getAwardActivity(actId);
		if (act != null) {
			awardActivityMaps.put(act.getId(), act);

			if (broadcast && broadcastMgr != null) {
				broadcastMgr.sendNotification(new AsyncWolfTask(
						"activityService", "reloadAwardActivity", new Object[] {
								actId, false }));
			}
		} else {
			log.warn("奖励活动{}不存在.", actId);
		}

	}

	@Override
	public void updateAwardActivity(AwardActivity activity) {

		activityDao.updateAwardActivity(activity);
		reloadAwardActivity(activity.getId(), true);
	}

	@Override
	public int hasAwardActivity() {
		int has = 0;
		Collection<AwardActivity> list = this.getAwardActivitys();
		int num = list == null ? 0 : list.size();
		if (num > 0) {
			for (AwardActivity a : list) {
				long now = System.currentTimeMillis();
				Timestamp startDttm = a.getStartDttm();
				Timestamp endDttm = a.getEndDttm();
				// System.out.println("startDttm="+startDttm);
				// System.out.println("endDttm="+endDttm);
				if (now >= startDttm.getTime() && now <= endDttm.getTime()) {
					has = has | a.getType();
				}
			}
		}
		return has;
	}
}
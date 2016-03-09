package com.youxigu.dynasty2.user.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibatis.sqlmap.engine.cache.memcached.MemcachedManager;
import com.manu.util.UtilDate;
import com.manu.util.UtilMisc;
import com.youxigu.dynasty2.chat.client.IChatClientService;
import com.youxigu.dynasty2.common.service.ICommonService;
import com.youxigu.dynasty2.log.ILogService;
import com.youxigu.dynasty2.log.LogBeanFactory;
import com.youxigu.dynasty2.log.imp.JunGongAct;
import com.youxigu.dynasty2.user.dao.IUserAchieveDao;
import com.youxigu.dynasty2.user.domain.Achieve;
import com.youxigu.dynasty2.user.domain.AchieveLimit;
import com.youxigu.dynasty2.user.domain.AchieveType;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.user.domain.UserAchieve;
import com.youxigu.dynasty2.user.service.IAchieveCompleteChecker;
import com.youxigu.dynasty2.user.service.IUserAchieveService;
import com.youxigu.dynasty2.user.service.IUserService;
import com.youxigu.dynasty2.util.BaseException;

/**
 * 君主成就service接口实现类
 * 
 * @author Dagangzi
 * 
 */
public class UserAchieveService implements IUserAchieveService {
	public static final Logger log = LoggerFactory
			.getLogger(UserAchieveService.class);
	private IUserAchieveDao userAchieveDao;
	private IChatClientService messageService;
	private ICommonService commonService;
	private IUserService userService;
	private ILogService logService;
	// private IInviteService inviteService;

	public Map<Integer, Achieve> achieveMap = new HashMap<Integer, Achieve>();// 成就id-成就
	public Map<Integer, List<Achieve>> entId2Achieve = new HashMap<Integer, List<Achieve>>();// entId-成就列表
	public Map<Integer, AchieveType> achieveTypeMap = new HashMap<Integer, AchieveType>();// 类型
	public Map<Integer, AchieveLimit> achieveLimitMap = new HashMap<Integer, AchieveLimit>();// 约束
	public static int TOTAL_ACHIEVE_NUM = 0;// 总成就个数
	/**
	 * 成就完成进度校验器
	 */
	private Map<String, IAchieveCompleteChecker> limitCheckers = new HashMap<String, IAchieveCompleteChecker>();

	/**
	 * type由小到大 如果相同 achieveId 由小到大
	 */
	public static final IDComparator idComparator = new IDComparator();

	public static class IDComparator
			implements Comparator<UserAchieve.AchieveInfo> {
		@Override
		public int compare(UserAchieve.AchieveInfo o1,
				UserAchieve.AchieveInfo o2) {
			int cr = 0;
			int a = (int) (o1.getType() - o2.getType());
			if (a != 0) {
				cr = (a > 0) ? 2 : -1;
			} else {
				a = o1.getAchieveId() - o2.getAchieveId();
				if (a != 0)
					cr = (a > 0) ? 1 : -2;
			}
			return cr;

		}
	}

	public void setUserAchieveDao(IUserAchieveDao userAchieveDao) {
		this.userAchieveDao = userAchieveDao;
	}

	public void setMessageService(IChatClientService messageService) {
		this.messageService = messageService;
	}

	public void setCommonService(ICommonService commonService) {
		this.commonService = commonService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public void setLimitCheckers(
			Map<String, IAchieveCompleteChecker> limitCheckers) {
		this.limitCheckers = limitCheckers;
	}

	public void setLogService(ILogService logService) {
		this.logService = logService;
	}

	// 初始化到内存
	private void initService() {// 初始化service
		log.info("加载成就数据");
		// 加载成就类型
		List<AchieveType> typeList = userAchieveDao.listAchieveType();
		if (typeList != null && typeList.size() > 0) {
			for (AchieveType achieveType : typeList) {
				TOTAL_ACHIEVE_NUM = TOTAL_ACHIEVE_NUM + achieveType.getNum();
				achieveTypeMap.put(achieveType.getType(), achieveType);
			}
		}

		List<AchieveLimit> limitList = userAchieveDao.listAchieveLimit();
		if (limitList != null && limitList.size() > 0) {
			for (AchieveLimit limit : limitList) {
				IAchieveCompleteChecker cheaker = limitCheckers
						.get(limit.getOctType());
				if (cheaker != null) {
					limit.setLimitChecker(cheaker);
				} else {
					log.warn("成就约束类型{}未定义", limit.getOctType());
				}
				achieveLimitMap.put(limit.getAchieveId(), limit);
			}
		}

		// 加载成就
		List<Achieve> achieveList = userAchieveDao.listAllAchieve();
		if (achieveList != null && achieveList.size() > 0) {
			for (Achieve achieve : achieveList) {
				AchieveType achieveType = achieveTypeMap.get(achieve.getType());
				if (achieveType == null) {
					log.error("成就类型不存在{}", achieve.getType());
					continue;
				}

				// if (achieve.getType() == AchieveType.TYPE_RES || (achieve
				// .getType() == AchieveType.TYPE_MILITARY
				// && (achieve
				// .getEntId() == AchieveType.TYPE_MILITARY_ENTID1
				// || achieve
				// .getEntId() == AchieveType.TYPE_MILITARY_ENTID3))) {
				// achieve.setCumulative(true);
				// }

				achieveType.addList(achieve);
				achieve.setAchieveType(achieveType);

				AchieveLimit achieveLimit = achieveLimitMap
						.get(achieve.getAchieveId());
				if (achieveLimit != null) {
					achieve.setAchieveLimit(achieveLimit);
				}

				achieveMap.put(achieve.getAchieveId(), achieve);

				// 按entId分类
				List<Achieve> entAchList = entId2Achieve
						.get(achieve.getEntId());
				if (entAchList == null) {
					entAchList = new ArrayList<Achieve>();
					entId2Achieve.put(achieve.getEntId(), entAchList);
				}
				entAchList.add(achieve);
			}
		}
	}

	@Override
	public Achieve getAchieveByAchieveId(int achieveId) {
		return achieveMap.get(achieveId);
	}

	@Override
	public List<Achieve> getAchieveListByType(int type) {
		return achieveTypeMap.get(type).getList();
	}

	@Override
	public List<Achieve> getAchieveListByEntId(int entId) {
		return entId2Achieve.get(entId);
	}

	@Override
	public Set<Integer> listAchieveTypes() {
		return achieveTypeMap.keySet();
	}

	/**
	 * 锁用户成就
	 * 
	 * @param userAchieve
	 * @return
	 */
	private void lockUserAchieve(long userId) {
		try {
			MemcachedManager.lock("UAchieve_" + userId);
		} catch (TimeoutException e) {
			throw new BaseException(e);
		}
	}

	@Override
	public void doNotifyAchieveModule(long userId, int type, int entId,
			int entNum) {
		lockUserAchieve(userId);
		List<Achieve> achieveList = this.getAchieveListByEntId(entId);
		if (achieveList == null) {
			log.warn("系统成就未配置。");
			return;
		}

		if (achieveList != null && achieveList.size() > 0) {
			// 增加的军功值
			int addJungong = 0;
			User user = userService.getUserById(userId);

			for (Achieve achieve : achieveList) {
				int achieveId = achieve.getAchieveId();
				int maxEntNum = achieve.getEntNum();
				entNum = achieve.cheakAchieveLimit(userId, entNum);
				if (entNum <= 0) {
					continue;
				}
				boolean isSingle = false;// 是否为独立成就
				UserAchieve userAchieve = null;
				if (achieve.getAchieveLimit() != null) {
					userAchieve = userAchieveDao.getUserAchieveByEntId(userId,
							achieve.getAchieveId());
					isSingle = true;
				} else {
					userAchieve = userAchieveDao.getUserAchieveByEntId(userId,
							entId);
				}

				if (userAchieve == null) {
					if (isSingle) {
						userAchieve = new UserAchieve(userId, type,
								achieve.getAchieveId(), entNum);
					} else {
						userAchieve = new UserAchieve(userId, type, entId,
								entNum);
					}
					if (maxEntNum <= userAchieve.getEntNum()) {
						userAchieve.setAchieveId(achieveId);
						userAchieve.setFinishDttm(UtilDate.nowTimestamp());
						addJungong = addJungong + achieve.getJungong();

						// 成就完成的日志
						logService.log(LogBeanFactory.buildAchieveLog(user,
								achieveId, achieve.getName(),
								achieve.getJungong()));
						if (log.isDebugEnabled()) {
							log.debug("成就：{}达成", achieve.getName());
						}
					}
					userAchieveDao.createUserAchieve(userAchieve);
				} else {
					if (isSingle) {
						if (userAchieve.getAchieveId() > 0) {
							// 独立成就完成
							continue;
						}
					} else {
						if (achieveId <= userAchieve.getAchieveId()) {
							// 同类中已经完成的
							continue;
						}
					}
					if (achieve.isCumulative()) {
						userAchieve.setEntNum(userAchieve.getEntNum() + entNum);
					} else {
						userAchieve.setEntNum(entNum);
					}
					if (maxEntNum <= userAchieve.getEntNum()
							&& achieveId > userAchieve.getAchieveId()) {
						userAchieve.setAchieveId(achieveId);
						userAchieve.setFinishDttm(UtilDate.nowTimestamp());
						addJungong = addJungong + achieve.getJungong();

						// 成就完成的日志
						logService.log(LogBeanFactory.buildAchieveLog(user,
								achieveId, achieve.getName(),
								achieve.getJungong()));
						if (log.isDebugEnabled()) {
							log.debug("成就：{}达成", achieve.getName());
						}
					}
					userAchieveDao.updateUserAchieve(userAchieve);
				}
			}

			if (addJungong > 0) {
				userService.doAddjunGong(userId, addJungong,
						JunGongAct.TYPE_ACHIEVE);
			}
		}
	}

	@Override
	public Map<String, Object> getUserAchieveByType(long userId, int type) {
		List<UserAchieve> userAchieveList = userAchieveDao
				.getUserAchieveByUserId(userId);
		List<UserAchieve.AchieveInfo> infos = new ArrayList<UserAchieve.AchieveInfo>();

		// 总成就个数
		int total = 0;
		// 完成的个数
		int finishNum = 0;
		// 按大类取
		if (type > 0) {
			AchieveType achieveType = achieveTypeMap.get(type);
			if (achieveType == null) {
				throw new BaseException("成就类型不存在");
			}
			total = achieveType.getNum();

			List<Achieve> syslist = achieveType.getList();
			if (syslist != null && syslist.size() > 0) {
				List<Achieve> tmp = new ArrayList<Achieve>();
				tmp.addAll(syslist);

				for (UserAchieve userAchieve : userAchieveList) {
					if (userAchieve.getType() != type) {
						continue;
					}

					if (userAchieve.getEntNum() <= 0) {
						continue;
					}

					int _achieveId = userAchieve.getAchieveId();
					int _entId = userAchieve.getEntId();
					int _entNum = userAchieve.getEntNum();
					List<Achieve> tmpList = this.getAchieveListByEntId(_entId);
					if (tmpList != null && tmpList.size() > 0) {
						// 同类成就有多个，公用一个进度
						for (Achieve _achieve : tmpList) {
							int achieveId = _achieve.getAchieveId();
							int entNum = _achieve.getEntNum();
							boolean finish = achieveId <= _achieveId;
							infos.add(new UserAchieve.AchieveInfo(achieveId,
									_entId, (finish ? entNum : _entNum), finish,
									type));
							tmp.remove(_achieve);

							if (finish) {
								finishNum = finishNum + 1;
							}
						}
					} else {
						// 一个成就独立使用一个进度
						Achieve _achieve = this.getAchieveByAchieveId(_entId);
						if (_achieve != null
								&& _achieve.getAchieveLimit() != null) {
							int achieveId = _achieve.getAchieveId();
							int entNum = _achieve.getEntNum();
							boolean finish = userAchieve
									.getFinishDttm() != null;
							infos.add(new UserAchieve.AchieveInfo(achieveId,
									_entId, (finish ? entNum : _entNum), finish,
									type));
							tmp.remove(_achieve);

							if (finish) {
								finishNum = finishNum + 1;
							}
						}
					}
				}

				if (tmp.size() > 0) {
					for (Achieve tmpAchieve : tmp) {
						infos.add(new UserAchieve.AchieveInfo(
								tmpAchieve.getAchieveId(),
								tmpAchieve.getEntId(), 0, false, type));
					}
				}
			}
		} else {
			// 总览
			if (userAchieveList != null && userAchieveList.size() > 0) {
				for (UserAchieve userAchieve : userAchieveList) {
					int _achieveId = userAchieve.getAchieveId();
					int _entId = userAchieve.getEntId();
					int _entNum = userAchieve.getEntNum();
					if (_achieveId <= 0) {
						// 只显示已完成的
						continue;
					}

					List<Achieve> tmpList = this.getAchieveListByEntId(_entId);
					if (tmpList != null && tmpList.size() > 0) {
						// 同类成就有多个，公用一个进度
						for (Achieve _achieve : tmpList) {
							int achieveId = _achieve.getAchieveId();
							int entNum = _achieve.getEntNum();
							boolean finish = achieveId <= _achieveId;
							if (finish) {
								infos.add(new UserAchieve.AchieveInfo(achieveId,
										_entId, (finish ? entNum : _entNum),
										finish, _achieve.getType()));
							}
						}
					} else {
						// 一个成就独立使用一个进度
						Achieve _achieve = this.getAchieveByAchieveId(_entId);
						if (_achieve != null
								&& _achieve.getAchieveLimit() != null) {
							int achieveId = _achieve.getAchieveId();
							int entNum = _achieve.getEntNum();
							boolean finish = userAchieve
									.getFinishDttm() != null;
							if (finish) {
								infos.add(new UserAchieve.AchieveInfo(achieveId,
										_entId, (finish ? entNum : _entNum),
										finish, _achieve.getType()));
							}
						}
					}
				}
			}
			total = TOTAL_ACHIEVE_NUM;
			finishNum = infos.size();
		}

		// id由小到大
		if (infos.size() > 1) {
			Collections.sort(infos, idComparator);
		}

		// 目前拥有的军功
		int junGong = userService.getUserById(userId).getJunGong();

		Map<String, Object> params = UtilMisc.toMap("infos", infos, "total",
				total, "finishNum", finishNum, "junGong", junGong);
		return params;
	}
}

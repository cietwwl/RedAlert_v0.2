package com.youxigu.dynasty2.activity.service.impl;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.youxigu.dynasty2.activity.dao.IActivityDao;
import com.youxigu.dynasty2.activity.domain.MysticShop;
import com.youxigu.dynasty2.activity.domain.MysticShopItem;
import com.youxigu.dynasty2.activity.domain.UserMysticShop;
import com.youxigu.dynasty2.activity.service.IMysticShopService;
import com.youxigu.dynasty2.common.AppConstants;
import com.youxigu.dynasty2.common.service.ICommonService;
import com.youxigu.dynasty2.develop.service.ICastleResService;
import com.youxigu.dynasty2.entity.domain.Entity;
import com.youxigu.dynasty2.entity.domain.SysHero;
import com.youxigu.dynasty2.entity.service.IEntityService;
import com.youxigu.dynasty2.hero.service.IHeroService;
import com.youxigu.dynasty2.log.imp.LogCashAct;
import com.youxigu.dynasty2.log.imp.LogHeroAct;
import com.youxigu.dynasty2.treasury.service.ITreasuryService;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.user.service.impl.UserService;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.dynasty2.util.DateUtils;
import com.youxigu.dynasty2.util.MathUtils;
import com.youxigu.dynasty2.vip.service.IVipService;

/**
 * 文件名 MysticShopService.java
 * 
 * 描 述 神秘商店接口
 */
public class MysticShopService implements IMysticShopService {
	private IActivityDao activityDao;
	private IVipService vipService;
	private UserService userService;
	private ITreasuryService treasuryService;
	private IEntityService entityService;
	private IHeroService heroService;
	private Map<Integer, MysticShop> mysticShopMap = new HashMap<Integer, MysticShop>();
	private Map<Integer, MysticShopItem> shopItemMap = new HashMap<Integer, MysticShopItem>();
	// private Map<Integer, List<MysticShopItem>> typeShopItemMap = new
	// HashMap<Integer, List<MysticShopItem>>();
	private Map<Integer, Map<Integer, List<MysticShopItem>>> typeShopItems = new HashMap<Integer, Map<Integer, List<MysticShopItem>>>();
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat format = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private ICastleResService castleResService = null;
	private ICommonService commonService = null;

	public String curTime(Date date) {
		return dateFormat.format(date);
	}

	/**
	 * yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 * @return
	 */
	public long parseToLong(String date) {
		try {
			return format.parse(date).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public void init() {
		List<MysticShop> mysticShopList = activityDao.getAllMysticShop();
		for (MysticShop mysticShop : mysticShopList) {
			mysticShop.init();
			mysticShopMap.put(mysticShop.getShopId(), mysticShop);
		}
		List<MysticShopItem> shopItemList = activityDao.getAllMysticShopItem();
		for (MysticShopItem shopItem : shopItemList) {
			shopItem.check();
			shopItemMap.put(shopItem.getShopItemId(), shopItem);
			// List<MysticShopItem> list =
			// typeShopItemMap.get(shopItem.getType());
			// if (list == null) {
			// list = new ArrayList<MysticShopItem>();
			// typeShopItemMap.put(shopItem.getType(), list);
			// }
			// list.add(shopItem);
			// 按照等级区间构建物品
			typeShopItems.get(shopItem.getType());
			Map<Integer, List<MysticShopItem>> lists = typeShopItems
					.get(shopItem.getType());
			if (lists == null) {
				lists = new HashMap<Integer, List<MysticShopItem>>();
				typeShopItems.put(shopItem.getType(), lists);
			}
			List<MysticShopItem> ls = lists.get(shopItem.getLevelId());
			if (ls == null) {
				ls = new ArrayList<MysticShopItem>();
				// 初始化区间段等级指向
				for (int l = shopItem.getMinLevle(); l <= shopItem
						.getMaxLevel(); l++) {
					int id = MysticShopItem.countLevelId(l);
					lists.put(id, ls);
				}
			}
			ls.add(shopItem);
		}
		// 校验数据
	}

	@Override
	public Map<String, Object> doBuyShopIem(long userId, int shopId, int pos) {
		UserMysticShop userMysticShop = getUserMysticShop(userId, shopId);
		List<int[]> arr = userMysticShop.getShopItems();
		if (pos > arr.size()) {
			throw new BaseException("索引位无效");
		}
		int[] itemInfo = arr.get(pos);
		if (itemInfo[1] == 1) {
			throw new BaseException("已经购买，不能重复购买");
		}
		MysticShopItem mysticShopItem = shopItemMap.get(itemInfo[0]);
		User user = userService.getUserById(userId);
		mysticShopItem.getShopCostType().costMoney(user, userService,
				mysticShopItem, castleResService, treasuryService,
				commonService);
		userMysticShop.buyItemByPos(pos);
		Entity entity = entityService.getEntity(mysticShopItem.getItemId());
		if (entity instanceof SysHero) {
			for (int i = 0; i < mysticShopItem.getItemNum(); i++) {
				heroService.doCreateAHero(user.getUserId(),
						mysticShopItem.getItemId(),
						LogHeroAct.MYSTICSHOP_HERO_ADD);
			}
		} else {
			treasuryService.doAddItemToTreasury(userId,
					mysticShopItem.getItemId(), mysticShopItem.getItemNum());
		}
		activityDao.updateUserMysticShop(userMysticShop);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("pos", pos);
		return data;
	}

	@Override
	public Map<String, Object> doFreashShopIems(long userId, int shopId) {
		User user = userService.getUserById(userId);

		UserMysticShop userMysticShop = getUserMysticShop(userId, shopId);
		// 1，扣除消耗，2刷新，3，返回数据
		MysticShop mysticShop = mysticShopMap.get(shopId);
		if (userMysticShop.hasFreeCount()) {
			userMysticShop.setFreeCount(userMysticShop.getFreeCount() - 1);
		} else {
			if (!userMysticShop.hasBuyCount()) {
				throw new BaseException("没有刷新次数了");
			}
			int cnt = treasuryService.getTreasuryCountByEntId(userId,
					mysticShop.getItemId());
			if (cnt > 0) {
				treasuryService.deleteItemFromTreasury(userId,
						mysticShop.getItemId(), 1, true,
						com.youxigu.dynasty2.log.imp.LogItemAct.LOGITEMACT_59);
			} else {
				userService.doConsumeCash(userId,
						this.freashCost(userMysticShop),
						LogCashAct.SHENMI_SHUAXIN_ACTION);
				userMysticShop.setCashCount(userMysticShop.getCashCount() + 1);
			}
			userMysticShop.setBuyCount(userMysticShop.getBuyCount() - 1);
		}
		this.coreFreash(userMysticShop, mysticShop, user.getUsrLv());
		userMysticShop.setLastTimes(new Timestamp(System.currentTimeMillis()));
		activityDao.updateUserMysticShop(userMysticShop);
		return doGetShopItemView(userId, shopId);
	}

	@Override
	public Map<String, Object> doGetShopItemView(long userId, int shopId) {
		UserMysticShop userMysticShop = getUserMysticShop(userId, shopId);
		MysticShop mysticShop = mysticShopMap.get(shopId);
		int itemCnt = treasuryService.getTreasuryCountByEntId(userId,
				mysticShop.getItemId());
		List<int[]> list = userMysticShop.getShopItems();
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>(
				list.size());
		for (int[] ii : list) {
			Map<String, Object> data = new HashMap<String, Object>(2);
			data.put("shopItemId", ii[0]);
			data.put("status", ii[1]);
			dataList.add(data);
		}
		Map<String, Object> data = new HashMap<String, Object>();

		data.put("shopItems", dataList);
		data.put("freeNum", userMysticShop.getFreeCount());
		data.put("buyNum", userMysticShop.getBuyCount());
		data.put("itemCnt", itemCnt);
		data.put("cash", freashCost(userMysticShop));
		data.put("nextTime", getNextTime(mysticShop));

		int itemId = commonService.getSysParaIntValue(
				AppConstants.HERO_CARD_DISCARD_ENTID, 0);
		int cnt = treasuryService.getTreasuryCountByEntId(userId, itemId);
		data.put("itemNum", cnt);
		return data;
	}

	// private void doFreashFreeCount(UserMysticShop userMysticShop) {
	// int max = this.getMaxFreeCount(userMysticShop.getUserId(),
	// userMysticShop.getShopId());
	// int leaveCnt = max - userMysticShop.getFreeCount();
	// // i如果已经达到最大次数，不回复
	// if (leaveCnt <= 0) {
	// return;
	// }
	// int cnt = getCnt(userMysticShop.getLastTimes(), leaveCnt,
	// userMysticShop.getShopId());
	// cnt = cnt > leaveCnt ? leaveCnt : cnt;
	//
	// if (cnt > 0) {
	// userMysticShop.setFreeCount(userMysticShop.getFreeCount() + cnt);
	// userMysticShop.setLastTimes(new Timestamp(System
	// .currentTimeMillis()));
	// activityDao.updateUserMysticShop(userMysticShop);
	// }
	//
	// }

	// private int getCnt(Timestamp lasTimestamp, int leaveCnt, int shopId) {
	// int cnt = 0;
	// MysticShop mysticShop = mysticShopMap.get(shopId);
	// long lastTime = lasTimestamp.getTime();
	// long curTime = System.currentTimeMillis();
	// String freashTime = mysticShop.getFreashTime();
	// List<Long> dateList = mysticShop.getDateDttm();
	// int dates = DateUtils.daysBetween(lasTimestamp,
	// new Timestamp(dateList.get(0)));
	// cnt += getCurCnt(dateList, lastTime, curTime, leaveCnt);
	// if (cnt >= leaveCnt) {
	// return cnt;
	// }
	// if (dates > 0) {// 当天恢复
	// // 多天恢复
	// cnt += (dates - 1) * dateList.size();
	// if (cnt >= leaveCnt) {
	// return cnt;
	// }
	// cnt += getLastLeaveCnt(lasTimestamp, freashTime);
	// }
	// return cnt;
	// }

	// 上次刷新当天剩余了多少次
	private int getLastLeaveCnt(Timestamp date, String freashTime) {
		int lastCurCnt = 0;
		String[] arr = freashTime.split(";");
		String last = this.curTime(date);
		// 12:00;18:00;21:00
		for (String dat : arr) {
			String dateS = last + " " + dat + ":00";
			long fresTime = this.parseToLong(dateS);
			if (date.getTime() >= fresTime) {
				++lastCurCnt;
			} else {
				break;
			}
		}
		return arr.length - lastCurCnt;
	}

	/**
	 * 操作今日可领取次数
	 * 
	 * @param dateList
	 * @param lastTime
	 * @param curTime
	 * @param leaveCnt
	 * @return
	 */
	private int getCurCnt(List<Long> dateList, long lastTime, long curTime,
			int leaveCnt) {
		int cnt = 0;
		for (long date : dateList) {
			// 如果时间戳在上次执行与当前时间之间，需要回复
			if (lastTime < date && date < curTime) {
				if (++cnt == leaveCnt) {
					break;
				}
			}
		}
		return cnt;
	}

	/**
	 * 获得花费数据 跟vip等级有关
	 * 
	 * @param userMysticShop
	 * @return
	 */
	private int freashCost(UserMysticShop userMysticShop) {
		MysticShop mysticShop = mysticShopMap.get(userMysticShop.getShopId());
		return mysticShop.cashCostByCount(userMysticShop.getCashCount());
	}

	private long getNextTime(MysticShop mysticShop) {
		long cur = System.currentTimeMillis();
		List<Long> dateList = mysticShop.getDateDttm();
		for (long date : dateList) {
			if (cur < date) {
				return date - cur;
			}
		}
		// 如果循环没有找到，一定是隔天
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_YEAR,
				calendar.get(Calendar.DAY_OF_YEAR) + 1);
		// 明天
		String sDate = dateFormat.format(calendar.getTime());
		String[] arr = mysticShop.getFreashTime().split(";");
		// 12:00;18:00;21:00
		sDate = sDate + " " + arr[0] + ":00";
		return parseToLong(sDate) - cur;
	}

	/**
	 * 根据vip等级获取玩家每天可以免费刷新的次数
	 * 
	 * @param userId
	 * @param shopId
	 * @return
	 */
	private int getMaxFreeCount(long userId, int shopId) {
		int curLv = vipService.getVipLvByUserId(userId);
		MysticShop mysticShop = mysticShopMap.get(shopId);
		return mysticShop.getFreeCountByLv(curLv);
	}

	/**
	 * 有偿刷新的最大次数
	 * 
	 * @param userId
	 * @param shopId
	 * @return
	 */
	private int getMaxBuyCount(long userId, int shopId) {
		int curLv = vipService.getVipLvByUserId(userId);
		MysticShop mysticShop = mysticShopMap.get(shopId);
		return mysticShop.getBuyCountByLv(curLv);
	}

	private UserMysticShop getUserMysticShop(long userId, int shopId) {
		User user = userService.getUserById(userId);
		UserMysticShop userMysticShop = activityDao.getUserMysticShop(userId,
				shopId);
		MysticShop mysticShop = mysticShopMap.get(shopId);
		if (mysticShop == null) {
			throw new BaseException("无效的商店id");
		}
		if (userMysticShop == null) {
			userMysticShop = new UserMysticShop();
			userMysticShop.setShopId(shopId);
			userMysticShop.setUserId(userId);
			// userMysticShop.setFreeCount(0);
			// userMysticShop.setBuyCount(0);
			userMysticShop.setFreeCount(getMaxFreeCount(userId, shopId));
			userMysticShop.setBuyCount(getMaxBuyCount(userId, shopId));
			userMysticShop.setLastTimes(new Timestamp(System
					.currentTimeMillis()));
			this.coreFreash(userMysticShop, mysticShop, user.getUsrLv());
			activityDao.insertUserMysticShop(userMysticShop);
		} else {
			long curTime = System.currentTimeMillis();
			List<Long> dateList = mysticShop.getDateDttm();
			int max = this.getMaxFreeCount(userMysticShop.getUserId(),
					userMysticShop.getShopId());
			// 剩余次数
			int leaveCnt = max - userMysticShop.getFreeCount();
			int cnt = getCurCnt(dateList, userMysticShop.getLastTimes()
					.getTime(), curTime, leaveCnt);
			boolean isUpdate = false;
			// 不同天操作
			if (!DateUtils.isSameDay(userMysticShop.getLastTimes())) {
				String freashTime = mysticShop.getFreashTime();
				userMysticShop.setCashCount(0);
				userMysticShop.setBuyShopIds("");
				userMysticShop.reset();
				int dates = DateUtils.daysBetween(
						userMysticShop.getLastTimes(),
						new Timestamp(dateList.get(0)));
				// 多天恢复
				if (cnt < leaveCnt) {
					cnt += (dates - 1) * dateList.size();
				}
				if (cnt < leaveCnt) {
					cnt += getLastLeaveCnt(userMysticShop.getLastTimes(),
							freashTime);
				}
				isUpdate = true;
			}
			cnt = cnt > leaveCnt ? leaveCnt : cnt;
			if (cnt > 0) {
				userMysticShop
						.setFreeCount(userMysticShop.getFreeCount() + cnt);
				isUpdate = true;
			}
			if (isUpdate) {
				userMysticShop.setLastTimes(new Timestamp(System
						.currentTimeMillis()));
				activityDao.updateUserMysticShop(userMysticShop);
			}

		}
		return userMysticShop;
	}

	/**
	 * 刷新神秘商店物品
	 * 
	 * @param userMysticShop
	 * @param mysticShop
	 */
	private void coreFreash(UserMysticShop userMysticShop,
			MysticShop mysticShop, int userLevel) {
		Map<Integer, List<MysticShopItem>> shops = typeShopItems.get(mysticShop
				.getType());
		int id = MysticShopItem.countLevelId(userLevel);
		List<MysticShopItem> shopItemList = shops.get(id);
		// List<MysticShopItem> shopItemList = typeShopItemMap.get(mysticShop
		// .getType());
		int[] arr = new int[shopItemList.size()];
		MysticShopItem shopItem;

		// 上次刷新不是今天，
		if (!DateUtils.isSameDay(userMysticShop.getLastTimes())) {
			userMysticShop.setBuyShopIds("");
			userMysticShop.reset();
		}
		Map<Integer, Integer> data = userMysticShop.getCountMap();
		int max = 0;
		for (int i = 0; i < shopItemList.size(); i++) {
			shopItem = shopItemList.get(i);
			Integer count = data.get(shopItem.getShopItemId());
			int weight = 0;
			if (count != null && count.intValue() >= shopItem.getWightNum()) {
				weight = shopItem.getWight2();
			} else {
				weight = shopItem.getWight1();
			}
			max += weight;
			arr[i] = weight;
		}
		arr = MathUtils.randomRepeatArray(arr, mysticShop.getNum(), max);
		StringBuilder conext = new StringBuilder();
		for (int shopItemId : arr) {
			conext.append(shopItemList.get(shopItemId).getShopItemId()).append(
					UserMysticShop.SPLIST_);
		}
		userMysticShop.setCurBuyStatus(0);
		userMysticShop.setCurShopIds(conext.substring(0, conext.length() - 1));
		userMysticShop.reset();
	}

	public void setActivityDao(IActivityDao activityDao) {
		this.activityDao = activityDao;
	}

	public void setVipService(IVipService vipService) {
		this.vipService = vipService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setTreasuryService(ITreasuryService treasuryService) {
		this.treasuryService = treasuryService;
	}

	public void setEntityService(IEntityService entityService) {
		this.entityService = entityService;
	}

	public void setHeroService(IHeroService heroService) {
		this.heroService = heroService;
	}

	public void setCastleResService(ICastleResService castleResService) {
		this.castleResService = castleResService;
	}

	public void setCommonService(ICommonService commonService) {
		this.commonService = commonService;
	}

}

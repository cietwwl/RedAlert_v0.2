package com.youxigu.dynasty2.treasury.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibatis.sqlmap.engine.cache.memcached.MemcachedManager;
import com.manu.util.UtilDate;
import com.youxigu.dynasty2.chat.ChatChannelManager;
import com.youxigu.dynasty2.chat.EventMessage;
import com.youxigu.dynasty2.chat.client.IChatClientService;
import com.youxigu.dynasty2.common.AppConstants;
import com.youxigu.dynasty2.common.domain.Enumer;
import com.youxigu.dynasty2.common.service.ICommonService;
import com.youxigu.dynasty2.entity.domain.DroppedEntity;
import com.youxigu.dynasty2.entity.domain.Entity;
import com.youxigu.dynasty2.entity.domain.Equip;
import com.youxigu.dynasty2.entity.domain.Item;
import com.youxigu.dynasty2.entity.domain.ItemExchange;
import com.youxigu.dynasty2.entity.domain.ItemProperty;
import com.youxigu.dynasty2.entity.domain.RandomItemProperty;
import com.youxigu.dynasty2.entity.service.IEntityService;
import com.youxigu.dynasty2.entity.service.IEquipService;
import com.youxigu.dynasty2.hero.domain.HeroEquipDebris;
import com.youxigu.dynasty2.hero.proto.EquipCardAndDebris;
import com.youxigu.dynasty2.hero.proto.SendEquipCardAndDebrisMsg;
import com.youxigu.dynasty2.hero.service.IHeroService;
import com.youxigu.dynasty2.log.AbsLogLineBuffer;
import com.youxigu.dynasty2.log.ILogService;
import com.youxigu.dynasty2.log.LogTypeConst;
import com.youxigu.dynasty2.log.MylogHeadUtil;
import com.youxigu.dynasty2.log.TLogTable;
import com.youxigu.dynasty2.log.TlogHeadUtil;
import com.youxigu.dynasty2.log.imp.LogItemAct;
import com.youxigu.dynasty2.mission.domain.Mission;
import com.youxigu.dynasty2.mission.service.IMissionService;
import com.youxigu.dynasty2.openapi.Constant;
import com.youxigu.dynasty2.treasury.dao.ITreasuryDao;
import com.youxigu.dynasty2.treasury.domain.Treasury;
import com.youxigu.dynasty2.treasury.service.ITreasuryFullListener;
import com.youxigu.dynasty2.treasury.service.ITreasuryService;
import com.youxigu.dynasty2.user.domain.Account;
import com.youxigu.dynasty2.user.domain.AchieveType;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.user.service.IAccountService;
import com.youxigu.dynasty2.user.service.IUserAchieveService;
import com.youxigu.dynasty2.user.service.IUserService;
import com.youxigu.dynasty2.user.service.impl.UserService;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.dynasty2.util.MathUtils;
import com.youxigu.wolf.net.OnlineUserSessionManager;
import com.youxigu.wolf.net.UserSession;

import edu.emory.mathcs.backport.java.util.Collections;

@SuppressWarnings("unchecked")
public class TreasuryService implements ITreasuryService {

	public static final Logger log = LoggerFactory
			.getLogger(TreasuryService.class);
	public static final TreasuryNumSort treasuryNumSort = new TreasuryNumSort();
	private ITreasuryDao treasuryDao;
	private IEntityService entityService;
	private IUserService userService;
	private IChatClientService messageService;
	private ICommonService commonService;
	private ITreasuryFullListener treasuryFullListener;
	private ILogService logService;
	private ILogService tlogService;
	private IAccountService accountService;
	private IEquipService equipService;
	private IHeroService heroService;
	private IMissionService missionService;
	private IUserAchieveService userAchieveService;

	public void setLogService(ILogService logService) {
		this.logService = logService;
	}

	public void setTlogService(ILogService tlogService) {
		this.tlogService = tlogService;
	}

	public void setTreasuryFullListener(
			ITreasuryFullListener treasuryFullListener) {
		this.treasuryFullListener = treasuryFullListener;
	}

	public void setCommonService(ICommonService commonService) {
		this.commonService = commonService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setMessageService(IChatClientService messageService) {
		this.messageService = messageService;
	}

	public void setTreasuryDao(ITreasuryDao treasuryDao) {

		this.treasuryDao = treasuryDao;
	}

	public void setEntityService(IEntityService entityService) {
		this.entityService = entityService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public void setAccountService(IAccountService accountService) {
		this.accountService = accountService;
	}

	@Override
	public Treasury getTreasuryById(long userId, long treasuryId) {
		return treasuryDao.getTreasuryById(userId, treasuryId);
	}

	public void setHeroService(IHeroService heroService) {
		this.heroService = heroService;
	}

	public void setMissionService(IMissionService missionService) {
		this.missionService = missionService;
	}

	public void setUserAchieveService(IUserAchieveService userAchieveService) {
		this.userAchieveService = userAchieveService;
	}

	/**
	 * 道具类型匹配
	 * 
	 * @param itemType
	 * @param itemTypes
	 * @return
	 */
	private boolean isInItemTypeCondition(int itemType, List<Integer> itemTypes) {
		if (itemTypes == null) {// 空表示没有过滤
			return true;
		}

		for (int type : itemTypes) {
			// 相对
			if (itemType == type) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 道具子类型匹配
	 * 
	 * @param childType
	 * @param childTypes
	 * @return
	 */
	private boolean isInChildTypeCondition(int childType,
			List<Integer> childTypes) {
		if (childTypes == null) {// 空表示没有过滤
			return true;
		}
		for (int type : childTypes) {

			if (type == childType) {
				return true;
			}
		}

		return false;
	}

	@Override
	public List<Treasury> getUserItemByItemTypeAndChildType(long userId,
			int itemType, int childType) {
		List<Treasury> resultList = new ArrayList<Treasury>();
		List<Treasury> userTreasuryList = getTreasurysByUserId(userId);
		if (itemType == -1) {
			return userTreasuryList;
		}
		if (userTreasuryList != null) {
			for (Treasury treasury : userTreasuryList) {
				if (treasury.getEntType() == itemType) {
					if (childType == -1 || childType == treasury.getChildType()) {
						resultList.add(treasury);
					}
				}

			}
		}
		return resultList;
	}

	/**
	 * 判断道具是在过滤条件中
	 * 
	 * @param treasury
	 * @param itemTypes
	 * @param childTypes
	 * @return
	 */
	@SuppressWarnings("unused")
	private boolean isInCondition(Treasury treasury, List<Integer> itemTypes,
			List<Integer> childTypes) {
		boolean itemTypeOk = isInItemTypeCondition(treasury.getEntType(),
				itemTypes);
		boolean childTypeOk = isInChildTypeCondition(treasury.getChildType(),
				childTypes);

		return itemTypeOk && childTypeOk;
	}

	@Override
	public boolean doAddItemToTreasury(long userId, int itemId, int itemNum) {
		return addItemToTreasury(userId, itemId, itemNum, 0, -1, false, true,
				null);
	}

	@Override
	public boolean addItemToTreasury(long userId, int itemId, int itemNum,
			int isGift, int isBand, boolean isThrowException, boolean toMail,
			LogItemAct addReason) {

		if (itemNum <= 0) {
			throw new BaseException("数字不对");
		}
		Item item = (Item) entityService.getEntity(itemId);
		if (item == null) {
			throw new BaseException("没有此道具");
		}
		// 系统不再区分绑定/非绑定
		isBand = item.getBandAble();
		// if (isBand < 0) {
		// isBand = item.getBandAble();
		// }
		// 系统不再区分赠品/非赠品
		isGift = 0;

		if (item.getNotJoinPack() > 0) {
			// 不进背包
			// 加武将图纸
			if (item.getItemType().isHeroCard()) {
				heroService.doAddHeroCardNum(userId, item, itemNum, addReason);
				return true;
			}

			// 加武将碎片
			if (item.getItemType().isHeroSoul()) {
				heroService.doAddHeroSoulNum(userId, item, itemNum, addReason);
				return true;
			}

			// 装备碎片
			if (item.getItemType().isEquipDebris()) {
				addHeroEquipDebris(userId, item, itemNum, addReason);
				return true;
			}

			// 装备图纸
			if (item.getItemType().isEquipCard()) {
				addHeroEquipCards(userId, item, itemNum, addReason);
				return true;
			}

			throw new BaseException("该道具需要做处理 entId = " + item.getEntId());
		}

		// if (item instanceof Gem ) {
		// isGift = 0; // 宝石都是非赠品，不管怎么得到的
		// }

		if (item.getSumAble() != 0) {// 可堆叠的Item
			return addSumableItemToTreasury(userId, item, itemNum, isGift,
					isBand, isThrowException, toMail, addReason);
		} else {
			return addUnSumableItemToTreasury(userId, item, itemNum, isGift,
					isBand, isThrowException, toMail, addReason, 0);
		}
	}

	private boolean addUnSumableItemToTreasury(long userId, Item item,
			int itemNum, int isGift, int isBand, boolean isThrowException,
			boolean toMail, LogItemAct addReason, int gemExp) {

		int itemId = item.getEntId();

		// List<Treasury> reslut = new ArrayList<Treasury>();
		// lock
		lockTreasury(userId);

		int errCode = 0;// -1 达到背包容量上限，-2超过单种道具的上限 -3不可堆叠的情况，部分超出背包容量上限
		int treasuryCount = 0;// // 背包道具总数(格子数)
		int existNum = 0;// 背包中的数量

		// 用来处理图鉴
		// boolean isEquip = (item instanceof Equip);
		// // 用来处理图鉴
		// boolean findEquip = false;

		List<Treasury> treasurys = this.getTreasurysByUserId(userId);
		for (Treasury t : treasurys) {
			if (!t.isEquipToHero()) {
				treasuryCount++;
				if (t.getEntId() == itemId) {
					existNum = existNum + t.getItemCount();
				}
			} /*
			 * else { if (isEquip && t.getEntId() == itemId) { findEquip = true;
			 * } }
			 */
		}

		int realNum = 0;// 实际加入背包的数量
		if (treasuryCount >= AppConstants.TREASURY_LIMIT) {
			if (isThrowException) {
				throw new BaseException("背包已满");
			}
			errCode = -1;// 达到上背包上限
			realNum = 0;
		} else {
			// 同一种类道具允许的最大数量
			int userHasMaxNum = item.getUserHasMaxNum() == 0 ? Integer.MAX_VALUE
					: item.getUserHasMaxNum();

			// 达到同类道具的上限,则减少数量
			if (existNum + itemNum > userHasMaxNum) {
				if (isThrowException) {
					throw new BaseException("背包已满");
				}
				errCode = -1;// 达到同类道具的上限

				realNum = userHasMaxNum - existNum;
				if (realNum < 0) {
					realNum = 0;
				}
			} else {
				realNum = itemNum;
			}

			if (realNum > 0) {

				// 增加的数量超过背包上限,则减小增加数量
				int tmp = AppConstants.TREASURY_LIMIT - treasuryCount;
				if (tmp < 0)
					tmp = 0;
				if (tmp < realNum) {
					if (isThrowException) {
						throw new BaseException("背包已满");
					}
					errCode = -2;// 超过上限，只增加了部分
					realNum = tmp;
				}

				// Date now = new Date();
				for (int i = 0; i < realNum; i++) {
					createTreasury(userId, item, 1, isGift, isBand, addReason,
							gemExp, itemId);
				}

			}
		}
		// // 紫色装备或者以上
		// if (isEquip && realNum > 0 && item.getColor() >= 4) {
		// if (!findEquip && existNum == 0) {
		// // 增加图鉴
		// userService.addUserIllustrations(userId, item);
		// }
		// }
		sendMessage(userId, item, itemNum, realNum, existNum, errCode, toMail,
				isGift);
		return itemNum != realNum;

	}

	@Override
	public Treasury doCreateEquipItemToTreasury(long userId, Equip equip,
			int itemNum, LogItemAct addReason) {
		if (equip == null) {
			throw new BaseException("装备物品为null");
		}
		if (itemNum < 1) {
			throw new BaseException("物品数量错误" + itemNum);
		}
		if (hasFull(userId, itemNum)) {
			throw new BaseException("背包已经满了");
		}
		return createTreasury(userId, equip, itemNum, 0, 0, addReason, 0,
				equip.getEntId());
	}

	private Treasury createTreasury(long userId, Item item, int itemNum,
			int isGift, int isBand, LogItemAct addReason, int gemExp, int itemId) {
		Treasury treasury = new Treasury();
		treasury.setUserId(userId);
		treasury.setEntId(itemId);
		treasury.setItemCount(itemNum);
		treasury.setEntType(item.getType());
		treasury.setEquip(0);
		treasury.setThrowAble(item.getThrowAble());
		treasury.setChildType(item.getChildType());
		treasury.setBand(isBand);
		treasury.setIsGift(isGift);
		treasury.setGemExp(gemExp);
		if (item.getTime() > 0) {
			treasury.setExistEndTime(UtilDate.moveSecond(item.getTime()));
		} else {
			treasury.setExistEndTime(UtilDate.NOTAVAIABLE);
		}
		if (item instanceof Equip) {
			Equip eq = (Equip) item;
			if (eq.isBuildEquip()) {
				// 随机属性
				RandomItemProperty rp = eq.randomBuildAttr();
				if (rp == null) {
					throw new BaseException("装备打造的属性为null");
				}
				int v = MathUtils.getRandomExMax(rp.getMinValue(),
						rp.getMaxValue());
				ItemProperty p = new ItemProperty(rp.getPropName(), v,
						rp.isAbs());
				// 随机特殊属性
				for (RandomItemProperty ipt : eq.getSpecialAttr()) {
					boolean b = MathUtils.randomProbability(rp
							.getSpecialWeightVal());
					if (!b) {
						continue;
					}
					int v1 = MathUtils.getRandomExMax(ipt.getMinValue(),
							ipt.getMaxValue());
					ItemProperty rp1 = new ItemProperty(ipt.getPropName(), v1,
							ipt.isAbs());
					treasury.addSpecialAttr(rp1);
				}
				treasury.setRandomProp(p.toAttrString());
			}

			// 搜集装备成就
			userAchieveService.doNotifyAchieveModule(userId,
					AchieveType.TYPE_MILITARY, AchieveType.TYPE_MILITARY_ENTID1,
					1);

			// X件Y品质Z级装备成就
			userAchieveService.doNotifyAchieveModule(userId,
					AchieveType.TYPE_MILITARY, AchieveType.TYPE_MILITARY_ENTID2,
					1);
		}
		treasuryDao.createTreasury(treasury);
		setItemTLog(LogTypeConst.TYPE_ADDITEM, userId, item, itemNum, addReason);
		// 发刷新道具的事件-entId
		this.sendFlushTreasuryEvent(treasury);

		// reslut.add(treasury);
		return treasury;
	}

	private boolean addSumableItemToTreasury(long userId, Item item,
			int itemNum, int isGift, int isBand, boolean isThrowException,
			boolean toMail, LogItemAct addReason) {
		int itemId = item.getEntId();
		// List<Treasury> reslut = new ArrayList<Treasury>();
		// lock
		lockTreasury(userId);
		// Date now = new Date();
		int errCode = 0;// -1 达到背包容量上限，-2超过单种道具的上限 -3不可堆叠的情况，部分超出背包容量上限

		List<Treasury> treasurys = this.getTreasurysByUserId(userId);
		int treasuryCount = 0;// treasurys.size();

		int realNum = 0;// 实际加入背包的数量
		int existNum = 0;// 背包中的数量

		List<Treasury> sumAbleTreasurys = new ArrayList<Treasury>(); // 背包中已经存在的，并且没有堆叠满的同种道具
		int canSumAbleNum = 0;// 没有堆叠满的背包格子可堆叠得数量

		// 同一种类道具允许的最大数量
		int userHasMaxNum = item.getUserHasMaxNum() == 0 ? Integer.MAX_VALUE
				: item.getUserHasMaxNum();

		int sumableLimit = item.getStackNum();
		for (Treasury tmp : treasurys) {
			if (!tmp.isEquipToHero()) {
				treasuryCount++;
				if (tmp.getEntId() == itemId) {
					int tmpCount = tmp.getItemCount();
					existNum = existNum + tmpCount;
					if (tmp.getIsGift() == isGift && tmp.getBand() == isBand) {
						tmpCount = sumableLimit - tmpCount;
						if (tmpCount > 0) {
							sumAbleTreasurys.add(tmp);
							canSumAbleNum = canSumAbleNum + tmpCount;
						}
					}

				}
			}
		}

		// 达到同类道具的上限,则减少数量
		if (existNum + itemNum > userHasMaxNum) {
			if (isThrowException) {
				throw new BaseException("背包已满");
			}
			errCode = -1;// 达到同类道具的上限
			realNum = userHasMaxNum - existNum;
			if (realNum < 0) {
				realNum = 0;
			}
		} else {
			realNum = itemNum;
		}

		// 计算是否放得下:还需要增加几个背包格子
		if (canSumAbleNum < realNum) {
			int tmp = realNum - canSumAbleNum;
			int addBag = tmp / sumableLimit;
			int mod = tmp % sumableLimit;
			if (mod != 0) {
				addBag++;
			}
			if ((addBag + treasuryCount) > AppConstants.TREASURY_LIMIT) {
				if (isThrowException) {
					throw new BaseException("背包已满");
				} else {
					errCode = -2;// 超过上限，只增加了部分
					// 实际可增加到背包中的数量
					realNum = (AppConstants.TREASURY_LIMIT - treasuryCount)
							* sumableLimit + canSumAbleNum;
				}
			}
		}
		if (realNum > 0) {
			int total = realNum;
			for (Treasury treasury : sumAbleTreasurys) {
				int oldNum = treasury.getItemCount();
				int remain = sumableLimit - oldNum;
				if (total > remain) {
					treasury.setItemCount(sumableLimit);
					total = total - remain;
				} else {
					treasury.setItemCount(oldNum + total);
					total = 0;

				}
				treasuryDao.updateTreasury(treasury);

				// 发刷新道具的事件-entId
				this.sendFlushTreasuryEvent(treasury);

				// reslut.add(treasury);
			}

			while (total > 0) {
				Treasury treasury = new Treasury();
				treasury.setUserId(userId);
				treasury.setEntId(itemId);
				if (total <= sumableLimit) {
					treasury.setItemCount(total);
					total = 0;
				} else {
					treasury.setItemCount(sumableLimit);
					total = total - sumableLimit;

				}
				treasury.setEntType(item.getType());
				treasury.setEquip(0);
				treasury.setThrowAble(item.getThrowAble());
				treasury.setChildType(item.getChildType());
				treasury.setBand(isBand);
				treasury.setIsGift(isGift);
				if (item.getTime() > 0) {
					treasury.setExistEndTime(UtilDate.moveSecond(item.getTime()));
				} else {
					treasury.setExistEndTime(UtilDate.NOTAVAIABLE);
				}
				if (item instanceof Equip) {
					// treasury.setHoleNum(((Equip) item).getCurrHoleNum());
				}
				treasuryDao.createTreasury(treasury);

				// 发刷新道具的事件-entId
				this.sendFlushTreasuryEvent(treasury);

				// reslut.add(treasury);
			}
		} else {
			realNum = 0;
		}
		sendMessage(userId, item, itemNum, realNum, existNum, errCode, toMail,
				isGift);
		setItemTLog(LogTypeConst.TYPE_ADDITEM, userId, item, itemNum, addReason);
		return itemNum != realNum;

	}

	@Override
	public void setItemTLog(String logType, long userId, Item item,
			int itemNum, LogItemAct addReason) {
		this.setItemTLog(logType, userId, item, itemNum, -1, addReason);
	}

	/**
	 * 记录道具tlog
	 */
	public void setItemTLog(String logType, long userId, Item item,
			int itemNum, int afterItemNum, LogItemAct addReason) {
		User user = userService.getUserById(userId);
		// Account account = accountService.getAccount(user.getAccId());

		Date now = new Date();
		String via = null;
		String areaId = null;
		String openId = null;
		// String pf = null;
		// String ip = null;
		UserSession us = OnlineUserSessionManager
				.getUserSessionByUserId(userId);

		if (us != null) {
			via = us.getVia();
			areaId = us.getAreaId();
			openId = us.getOpenid();
			// pf = us.getPfEx();
			// ip = us.getUserip();
		} else {
			Account account = accountService.getAccount(user.getAccId());
			via = account.getVia();
			areaId = account.getAreaId();
			openId = account.getAccName();
			// pf = Constant.getPfEx(account.getPf());
			// ip = account.getLoginIp();
		}

		// ****************************mylog数据格式************************
		// <struct name="ItemFlow" version="1" desc="(必填)道具流水表">
		// <entry name="GameSvrId" type="string" size="25" desc="(必填)登录的游戏服务器编号"
		// />
		// <entry name="dtEventTime" type="datetime"
		// desc="(必填)游戏事件的时间, 格式 YYYY-MM-DD HH:MM:SS" />
		// <entry name="vGameAppid" type="string" size="32" desc="(必填)游戏APPID"
		// />
		// <entry name="PlatID" type="int" defaultvalue="0"
		// desc="(必填)ios 0/android 1"/>
		// <entry name="ZoneID" type="int" defaultvalue="0"
		// desc="(必填)针对分区分服的游戏填写分区id，用来唯一标示一个区；非分区分服游戏请填写0"/>
		// <entry name="vopenid" type="string" size="64" desc="(必填)玩家" />
		// <entry name="Level" type="int" desc="(必填)玩家等级" />
		// <entry name="Sequence" type="int" desc="(必填)用于关联一次购买产生多条不同类型的货币日志" />
		// <entry name="iGoodsType" type="int" desc="(必填)道具类型" />
		// <entry name="iGoodsId" type="int" desc="(必填)道具ID" />
		// <entry name="Count" type="int" desc="(必填)数量" />
		// <entry name="AfterCount" type="int" desc="(必填)动作后的物品存量" />
		// <entry name="Reason" type="int" desc="(必填)道具流动一级原因" />
		// <entry name="SubReason" type="int" desc="(必填)道具流动二级原因" />
		// <entry name="iMoney" type="int"
		// desc="(必填)花费代币或金币购买道具情况下输出消耗的钱数量，否则填0" />
		// <entry name="iMoneyType" type="int"
		// desc="(必填)钱的类型MONEYTYPE,其它货币类型参考FAQ文档" />
		// <entry name="AddOrReduce" type="int" desc="(必填)增加 0/减少 1" />
		// <entry name="LoginChannel" type="int" defaultvalue="0"
		// desc="(必填)登录渠道"/>
		// <entry name="RoleId" type="string" size="64" desc="(必填)角色ID" />
		// </struct>

		// mylog 道具流水日志
		int count = 0;
		if (afterItemNum < 0) {
			count = this.getTreasuryCountByEntId(userId, item.getEntId());
		} else {
			count = afterItemNum;
		}
		logService.log(MylogHeadUtil
				.getMylogHead(TLogTable.TLOG_ITEMFLOW.getName(), user)
				.append(user.getUsrLv()).append(0).append(item.getType())
				.append(item.getEntId()).append(itemNum).append(count)
				.append(addReason == null ? 0 : addReason.vuale).append(0)
				.append(0).append(0)
				.append(logType.equals(LogTypeConst.TYPE_ADDITEM) ? 0 : 1)
				.append(MylogHeadUtil.getIntVia(via)).append(user.getUserId()));

		// ****************************tlog数据格式************************
		// <struct name="ItemFlow" version="1" desc="(必填)道具流水表">
		// <entry name="vGameSvrId" type="string" size="25"
		// desc="(必填)登录的游戏服务器编号" />
		// <entry name="dtEventTime" type="datetime"
		// desc="(必填)游戏事件的时间, 格式 YYYY-MM-DD HH:MM:SS" />
		// <entry name="vGameAppid" type="string" size="32" desc="(必填)游戏APPID"
		// />
		// <entry name="iPlatID" type="int" desc="(必填)ios 0/android 1"/>
		// <entry name="iZoneID" type="int"
		// desc="(必填)针对分区分服的游戏填写分区id，用来唯一标示一个区；非分区分服游戏请填写0"/>
		// <entry name="vopenid" type="string" size="64" desc="(必填)玩家" />
		// <entry name="iLevel" type="int" desc="(必填)玩家等级" />
		// <entry name="iSequence" type="int" desc="(必填)用于关联一次购买产生多条不同类型的货币日志"
		// />
		// <entry name="iGoodsType" type="int" desc="(必填)道具类型" />
		// <entry name="iGoodsId" type="int" desc="(必填)道具ID" />
		// <entry name="iCount" type="int" desc="(必填)数量" />
		// <entry name="iAfterCount" type="int" desc="(必填)动作后的物品存量" />
		// <entry name="iReason" type="int" desc="(必填)道具流动一级原因" />
		// <entry name="iSubReason" type="int" desc="(必填)道具流动二级原因" />
		// <entry name="iMoney" type="int"
		// desc="(必填)花费代币或金币购买道具情况下输出消耗的钱数量，否则填0" />
		// <entry name="iMoneyType" type="int"
		// desc="(必填)钱的类型MONEYTYPE,其它货币类型参考FAQ文档" />
		// <entry name="iAddOrReduce" type="int" desc="(必填)增加 0/减少 1" />
		// </struct>
		AbsLogLineBuffer buf = TlogHeadUtil.getTlogHead(
				LogTypeConst.TLOG_TYPE_ITEM, user);
		buf.append(user.getUsrLv()).append(0).append(item.getType())
				.append(item.getEntId()).append(itemNum);
		// int count = this.getTreasuryCountByEntId(userId, item.getEntId());
		buf.append(count).append(addReason == null ? 0 : addReason.vuale)
				.append(0).append(0).append(0)
				.append(logType.equals(LogTypeConst.TYPE_ADDITEM) ? 0 : 1);
		tlogService.log(buf);
	}

	/**
	 * 发送系统消息
	 * 
	 * @param userId
	 * @param item
	 * @param itemNum
	 * @param realNum
	 * @param existNum
	 * @param errCode
	 * @param toMail
	 * @param isGift
	 */
	private void sendMessage(long userId, Item item, int itemNum, int realNum,
			int existNum, int errCode, boolean toMail, int isGift) {
		StringBuilder message = new StringBuilder();
		if (errCode < 0) {
			int restNum = itemNum - realNum;
			if (realNum > 0) {
				// 部分获得道具
				message.append("您获得了道具").append(item.getChatName()).append("x")
						.append(itemNum);

				if (toMail) {
					treasuryFullListener.doTreasuryFull(userId,
							item.getEntId(), restNum, isGift == 1);
					message.append(",背包已满,发往邮箱x").append(restNum);

				} else {
					message.append(",背包已满,被丢弃x").append(restNum);

				}
			} else {// 这种情况没有吧？
				if (toMail) {
					treasuryFullListener.doTreasuryFull(userId,
							item.getEntId(), restNum, isGift == 1);
					message.append("背包已满,您获得的道具").append(item.getChatName())
							.append("x").append(restNum).append("已直接发往邮箱");
				} else {
					message.append("背包已满,您无法获得").append(item.getChatName())
							.append("x").append(restNum);

				}

			}

		} else {
			if (realNum > 0) {
				message.append("您获得了道具").append(item.getChatName()).append("x")
						.append(realNum);
			}
		}
		String msg = message.append("。").toString();

		messageService.sendMessage(0, userId,
				ChatChannelManager.CHANNEL_SYSTEM, null, msg);

		if (log.isDebugEnabled()) {
			log.debug(msg);
		}

		// User usr = userService.getUserById(userId);
		// missionService.notifyMissionModule(usr, Mission.QCT_TYPE_ITEM_NUM,
		// item
		// .getEntId(), realNum + existNum);
	}

	@Override
	public boolean hasTreasury(long userId, int itemId) {
		List<Treasury> treasurys = treasuryDao.getTreasurysByUserId(userId);
		for (Treasury treasury : treasurys) {
			if (treasury.getEntId() == itemId && !treasury.isEquipToHero()) {
				return true;

			}
		}
		return false;
	}

	/**
	 * 从国库中减去一定数量的某种物品,如果数量减为0，则删除
	 * 
	 * @param userId
	 * @param itemId
	 * @param itemNum
	 * @param isThrowException
	 * @param delReason
	 */
	@Override
	public void deleteItemFromTreasury(long userId, int itemId, int itemNum,
			boolean isThrowException, LogItemAct delReason) {
		if (itemNum <= 0) {
			return;
		}
		int delCount = itemNum;
		Item item = (Item) entityService.getEntity(itemId);
		if (item == null) {
			throw new BaseException("entId=" + itemId + " 的道具未定义");
		}

		// Date now = new Date();
		lockTreasury(userId);

		if (item.getNotJoinPack() > 0) {
			// 不是背包物品
			// 加武将图纸
			if (item.getItemType().isHeroCard()) {
				heroService.doDelHeroCardNum(userId, item, itemNum, delReason);
				return;
			}

			// 加武将碎片
			if (item.getItemType().isHeroSoul()) {
				heroService.doDelHeroSoulNum(userId, item, itemNum, delReason);
				return;
			}

			if (item.getItemType().isEquipDebris()) {
				addHeroEquipDebris(userId, item, -itemNum, delReason);
				return;
			}

			if (item.getItemType().isEquipCard()) {
				addHeroEquipCards(userId, item, -itemNum, delReason);
				return;
			}
			throw new BaseException("该道具需要做处理 entId = " + item.getEntId());
		}

		List<Treasury> treasurys = getNotEquippedTreasurysByItemId(userId,
				itemId);
		if (treasurys.size() > 1) {
			Collections.sort(treasurys, treasuryNumSort);
		}
		int total = 0;
		for (Treasury treasury : treasurys) {
			total = total + treasury.getItemCount();
		}

		// GM工具删除道具时 如果背包内道具不足 直接清空
		if (total < delCount && "FROMGM".equals(delReason)) {
			delCount = itemNum - total;
		}
		if (total < delCount) {
			if (isThrowException) {
				throw new BaseException("背包中没有足够的道具:" + item.getItemName());
			} else {
				return;
			}
		}
		// TODO:这里要判断，先删除赠品，
		Iterator<Treasury> lit = treasurys.iterator();
		while (lit.hasNext()) {
			Treasury t = lit.next();
			int itemCount = t.getItemCount();
			// if (t.getIsGift() == 1) {
			if (delCount >= itemCount) {
				delCount -= itemCount;
				treasuryDao.deleteTreasury(t);
				t.setItemCount(0);
				lit.remove();
				// 发刷新道具的事件-entId
				this.sendFlushTreasuryEvent(t);
			} else {
				t.setItemCount(itemCount - delCount);
				treasuryDao.updateTreasury(t);
				delCount = 0;
				// 发刷新道具的事件-entId
				this.sendFlushTreasuryEvent(t);
				break;
			}
			// }

		}
		// 再删除绑定
		// if (delCount > 0) {
		// lit = treasurys.iterator();
		// while (lit.hasNext()) {
		// Treasury t = lit.next();
		// if (t.getBand() == 1) {
		// if (delCount >= t.getItemCount()) {
		//
		// treasuryDao.deleteTreasury(t);
		// t.setItemCount(0);
		// lit.remove();
		// delCount -= t.getItemCount();
		// // 发刷新道具的事件-entId
		// this.sendFlushTreasuryEvent(t);
		//
		// } else {
		// t.setItemCount(t.getItemCount() - delCount);
		// treasuryDao.updateTreasury(t);
		// delCount = 0;
		// // 发刷新道具的事件-entId
		// this.sendFlushTreasuryEvent(t);
		//
		// break;
		// }
		// }
		// }
		// }
		// // 再删出普通
		// if (delCount > 0) {
		// lit = treasurys.iterator();
		// while (lit.hasNext()) {
		// Treasury t = lit.next();
		// if (delCount >= t.getItemCount()) {
		// treasuryDao.deleteTreasury(t);
		// t.setItemCount(0);
		// lit.remove();
		// delCount -= t.getItemCount();
		// // 发刷新道具的事件-entId
		// this.sendFlushTreasuryEvent(t);
		//
		// } else {
		// t.setItemCount(t.getItemCount() - delCount);
		// treasuryDao.updateTreasury(t);
		// delCount = 0;
		// // 发刷新道具的事件-entId
		// this.sendFlushTreasuryEvent(t);
		//
		// break;
		// }
		// }
		// }

		StringBuilder sb = new StringBuilder("您的道具").append(item.getChatName())
				.append("减少了").append(itemNum).append("个。");
		String msg = sb.toString();
		messageService.sendMessage(0, userId,
				ChatChannelManager.CHANNEL_SYSTEM, null, msg);
		if (log.isDebugEnabled()) {
			log.debug(msg);
		}

		setItemTLog(LogTypeConst.TYPE_DELITEM, userId, item, itemNum, delReason);

		// User usr = userService.getUserById(userId);
		// Map<String, Object> param = new HashMap<String, Object>();
		// param.put("otc", "QCT_Item");
		// missionService.notifyMissionModule(usr, param);//
		// 丢弃道具的时候要判断一下物品任务的完成情况
	}

	@Override
	public int deleteTreasury(long userId, long treasuryId, int itemNum,
			LogItemAct reason) {
		if (itemNum <= 0)
			return 0;

		lockTreasury(userId);

		Treasury treasury = treasuryDao.getTreasuryById(userId, treasuryId);
		if (treasury == null || treasury.getUserId() != userId)
			return 0;

		Item item = (Item) entityService.getEntity(treasury.getEntId());
		return deleteTreasury(treasury, item, itemNum, reason, true);
	}

	private int deleteTreasury(Treasury treasury, Item item, int itemNum,
			LogItemAct reason, boolean sendMessage) {
		if (item == null) {
			throw new BaseException("道具配置数据错误,该道具不存在配置数据");
		}
		if (treasury.isEquipToHero()) {
			throw new BaseException("该道具已经被装备");
		}

		if (item instanceof Equip) {
			// TODO:这里是否判断宝石
			if (treasury.hasGem()) {
				throw new BaseException("该道具镶嵌宝石，不可丢弃");
			}
		}
		int realDelNum = itemNum;
		int oldNum = treasury.getItemCount();
		int num = oldNum - itemNum;
		treasury.setItemCount(num);
		if (num > 0) {
			treasuryDao.updateTreasury(treasury);
		} else if (num == 0) {
			treasuryDao.deleteTreasury(treasury);
		} else {
			throw new BaseException("该道具数量不足。");
		}
		setItemTLog(LogTypeConst.TYPE_DELITEM, treasury.getUserId(), item,
				itemNum, reason);
		// 发刷新道具的事件-entId
		this.sendFlushTreasuryEvent(treasury);

		if (sendMessage) {
			StringBuilder sb = new StringBuilder("您的道具")
					.append(item.getChatName()).append("减少了")
					.append(realDelNum).append("个。");
			String msg = sb.toString();
			messageService.sendMessage(0, treasury.getUserId(),
					ChatChannelManager.CHANNEL_SYSTEM, null, msg);
			// if (log.isDebugEnabled()) {
			// log.debug(msg);
			// }
		}
		return realDelNum;
	}

	/**
	 * 丢弃道具,丢弃的时候要判断是否可丢弃
	 * 
	 * @param userId
	 * @param treasuryId
	 * @param itemNum
	 */
	@Override
	public void deleteTreasuryByDiscard(long userId, long treasuryId, int num) {
		if (num <= 0) {
			throw new BaseException("数量必须大于0");
		}
		lockTreasury(userId);

		Treasury treasury = treasuryDao.getTreasuryById(userId, treasuryId);
		if (treasury == null || treasury.getUserId() != userId)
			return;

		Item item = (Item) entityService.getEntity(treasury.getEntId());
		if (item.getThrowAble() != 1) {
			throw new BaseException("该道具不可丢弃");
		}
		if (treasury.isEquipToHero()) {
			throw new BaseException("该道具已经被装备，不可丢弃");
		}

		if (item instanceof Equip) {
			if (treasury.hasGem()) {
				throw new BaseException("该道具镶嵌宝石，不可丢弃");
			}
		}
		if (num <= 0 || num > treasury.getItemCount()) {
			num = treasury.getItemCount();
		}
		deleteTreasury(treasury, item, num, LogItemAct.LOGITEMACT_130, true);

	}

	/**
	 * 得到国库中某物品的数量
	 * 
	 * @param userId
	 * @param entId
	 * @return
	 */
	@Override
	public int getNotEquippedTreasuryCountByEntId(long userId, int entId) {

		List<Treasury> treasurys = treasuryDao.getTreasurysByUserId(userId);
		int result = 0;
		for (Treasury treasury : treasurys) {
			if (treasury.getEntId() == entId && !treasury.isEquipToHero()) {
				result += treasury.getItemCount();
			}
		}
		return result;

	}

	/**
	 * 获取全部背包道具
	 * 
	 * @param userId
	 * @return
	 */
	@Override
	public List<Treasury> getTreasurysByUserId(long userId) {
		return treasuryDao.getTreasurysByUserId(userId);
	}

	@Override
	public List<Treasury> getAllEquipByUserId(long userId) {
		List<Treasury> equipList = new ArrayList<Treasury>();
		List<Treasury> list = this.getTreasurysByUserId(userId);
		if (list != null && list.size() > 0) {
			for (Treasury treasury : list) {
				if (treasury.getItem().isEquip()) {
					equipList.add(treasury);
				}
			}
		}
		return equipList;
	}

	/**
	 * 获取未装备的全部背包道具
	 * 
	 * @param userId
	 * @return
	 */
	@Override
	public List<Treasury> getNotEquippedTreasurysByUserId(long userId) {
		List<Treasury> datas = new ArrayList<Treasury>();
		List<Treasury> all = treasuryDao.getTreasurysByUserId(userId);
		for (Treasury treasury : all) {
			if (!treasury.isEquipToHero()) {
				datas.add(treasury);
			}
		}
		return datas;
	}

	@Override
	public void lockTreasury(long userId) {
		try {
			MemcachedManager.lock(TreasuryLocker + userId);
		} catch (TimeoutException e) {
			throw new BaseException(e);
		}
	}

	/**
	 * 获取镶嵌宝石所要的铜币，按宝石等级走
	 * 
	 * @param gemLv
	 *            宝石等级
	 * @return int needCopper
	 */
	@SuppressWarnings("unused")
	private int getNeedCopperByGemLv(int gemLv) {
		final String key = AppConstants.ENUMER_GEM_ADD_COPPER + gemLv;
		Enumer enumer = commonService.getEnumById(key);
		int needCopper = 0;
		if (enumer != null) {
			needCopper = enumer.getIntValue();
		} else {
			log.warn(key + " is error. use gemLv * 1500");
			needCopper = gemLv * 1500;

		}
		return needCopper;
	}

	@SuppressWarnings("unused")
	private String joinIntArray(int[] datas, String join) {
		StringBuilder sb = new StringBuilder();
		boolean has = false;
		for (int i = 0; i < datas.length; i++) {
			if (datas[i] != 0) {
				has = true;
				break;
			}
		}
		if (has) {
			for (int i = 0; i < datas.length; i++) {
				if (datas[i] != 0) {
					sb.append(datas[i]);
				}
				if (i < datas.length)
					sb.append(join);
			}
			return sb.toString();
		} else {
			return null;
		}

	}

	/**
	 * 取得背包中所有的未装备的某一ItemId的数量
	 * 
	 * @param userId
	 * @param itemId
	 * @return
	 */
	private List<Treasury> getNotEquippedTreasurysByItemId(long userId,
			int itemId) {
		List<Treasury> treasurys = treasuryDao.getTreasurysByUserId(userId);
		List<Treasury> datas = new ArrayList<Treasury>();
		for (Treasury treasury : treasurys) {
			if (treasury.getEntId() == itemId && !treasury.isEquipToHero()) {
				datas.add(treasury);
			}
		}
		return datas;
	}

	@Override
	public void doUseItem(long userId, int itemId, int num, String pfFlag) {
		doUseItem(userId, itemId, num, pfFlag, null);

	}

	@Override
	public void doUseItem(long userId, int itemId, int num, String pfFlag,
			Map<String, Object> params) {
		if (num <= 0) {
			throw new BaseException("使用数量必须大于0");
		}
		lockTreasury(userId);
		List<Treasury> treasurys = this.getTreasurysByEntId(userId, itemId);
		int count = treasurys.size();
		if (treasurys == null || count == 0) {
			throw new BaseException("背包中没有该道具");
		}

		if (count == 1) {
			Treasury curr = treasurys.get(0);
			doUseItem(userId, curr.getId(), num, pfFlag, params);
		} else {

			Iterator<Treasury> lit = treasurys.iterator();
			while (num > 0 && lit.hasNext()) {
				Treasury t = lit.next();
				if (t.getIsGift() == 1) {// 先用赠品
					int useNum = doUseItem(userId, t.getId(), num, pfFlag,
							params);
					num = num - useNum;
					lit.remove();
				}
			}

			lit = treasurys.iterator();
			while (num > 0 && lit.hasNext()) {
				Treasury t = lit.next();
				if (t.getBand() == 1) {// 用绑定
					int useNum = doUseItem(userId, t.getId(), num, pfFlag,
							params);
					num = num - useNum;
					lit.remove();
				}
			}
			// 用普通
			lit = treasurys.iterator();
			while (num > 0 && lit.hasNext()) {
				Treasury t = lit.next();
				int useNum = doUseItem(userId, t.getId(), num, pfFlag, params);
				num = num - useNum;
				// lit.remove();
			}

		}
		// params.clear();
		// return curr;
	}

	@Override
	public int doUseItem(long userId, long treasuryId, int num) {
		return doUseItem(userId, treasuryId, num, null, null);
	}

	@Override
	public int doUseItem(long userId, long treasuryId, int num, String pfFlag,
			Map<String, Object> params) {
		if (num <= 0) {
			throw new BaseException("使用数量必须大于0");
		}

		// TODO:要不要限制一次使用的数量上限?
		lockTreasury(userId);
		User usr = userService.getUserById(userId);
		if (usr == null) {
			throw new BaseException("没有该玩家");
		}
		Treasury treasury = getTreasuryById(userId, treasuryId);
		if (treasury == null || treasury.getUserId() != userId) {
			throw new BaseException("没有该道具");
		}
		Item item = treasury.getItem();
		if (num > treasury.getItemCount()) {
			throw new BaseException("没有足够数量的" + item.getItemName());
		}
		if (item.getUseAble() <= 0) {
			throw new BaseException("该道具不能使用");
		}
		if (item.getUseAble() >= 2 && num > 1) {
			throw new BaseException("该道具不能批量使用");
		}
		if (num > 1 && item.getUseMaxNum() < num) {
			throw new BaseException("该道具批量使用的上限为" + item.getUseMaxNum());
		}
		int pf = item.getMissionId();// 道具上的平台特权标志
		if (pf != 0) {
			int currPf = Constant.getPFIntValue(pfFlag);
			if (pf != currPf) {
				throw new BaseException("必须从相应平台进入游戏才可以使用此道具");
			}
		}

		// Castle castle = castleService.getMainCastlesByUserId(userId);
		// if (castle == null) {
		// throw new BaseException("玩家没有主城");
		// }

		int realNum = this.deleteTreasury(userId, treasuryId, num,
				LogItemAct.LOGITEMACT_134);

		Map<String, Object> params1 = new HashMap<String, Object>(5);
		//
		// params1.put("castle", castle);
		params1.put("num", num);
		params1.put("user", usr);
		params1.put("treasury", treasury);
		params1.put("iAction", LogItemAct.LOGITEMACT_134);
		Map<String, Object> datas = entityService.doAction(item,
				Entity.ACTION_USE, params1);

		// ///////构造传给前台的数据
		Map<Integer, DroppedEntity> dropItems = (Map<Integer, DroppedEntity>) datas
				.get("items");
		if (dropItems != null && dropItems.size() > 0) {

			List<DroppedEntity> allDropItems = (List<DroppedEntity>) params
					.get("items");
			if (allDropItems != null) {
				for (DroppedEntity de : dropItems.values()) {
					boolean find = false;
					for (DroppedEntity old : allDropItems) {
						if (old.getEntId() == de.getEntId()) {
							old.setNum(old.getNum() + de.getNum());
							find = true;
						}
					}
					if (!find) {
						allDropItems.add(de);
					}
				}
			} else {
				allDropItems = new ArrayList<DroppedEntity>(dropItems.size());
				params.put("items", allDropItems);
				for (DroppedEntity de : dropItems.values()) {
					allDropItems.add(de);
				}
			}

		}

		// 批量使用时：只扣除补满所需消耗的道具数
		if (num > 1 && params1.containsKey("num")) {
			num = (Integer) params1.get("num");
		}

		// 使用道具,累计值
		missionService.notifyMissionModule(usr, Mission.QCT_TYPE_USE,
				treasury.getEntId(), realNum);

		// TODO 这样做的目的是为了把一些使用后的效果参数返给前台
		// params.remove("castle");
		// params.remove("num");
		// params.remove("user");
		// params.remove("treasury");
		// params.remove("action");
		// params.clear(); // 这里clear会不会出问题？
		// if (item.getType() == Item.ITEM_TYPE_HERO_CARD) {
		// params.putAll(datas);
		// }
		return realNum;

	}

	@Override
	public List<Treasury> getTreasurysByEntId(long userId, int entId) {
		return treasuryDao.getTreasurysByEntId(userId, entId);
	}

	@Override
	public int getTreasuryCountByEntId(long userId, int entId) {
		List<Treasury> temList = getTreasurysByEntId(userId, entId);
		int result = 0;
		for (Treasury t : temList) {
			result += t.getItemCount();
		}
		return result;
	}

	@Override
	public void sendFlushTreasuryEvent(Treasury treasury) {
		// event 刷新道具
		messageService.sendEventMessage(treasury.getUserId(),
				EventMessage.TYPE_FRESH_ITEM, treasury.getView());

	}

	public static class TreasuryNumSort implements Comparator<Treasury> {

		@Override
		public int compare(Treasury t1, Treasury t2) {
			int tmp = t1.getIsGift() - t2.getIsGift();
			if (tmp == 0) {
				tmp = t1.getBand() - t2.getBand();
				if (tmp == 0) {
					return t1.getItemCount() - t2.getItemCount();
				} else {
					return tmp;
				}
			} else {
				return tmp;
			}
		}

	}

	@Override
	public int getBagFreeCount(long userId) {
		List<Treasury> treasurys = this.getNotEquippedTreasurysByUserId(userId);
		int treasuryCount = treasurys.size();// 背包道具总数(格子数)
		return AppConstants.TREASURY_LIMIT - treasuryCount;
	}

	@Override
	public boolean hasFull(long userId) {
		return getBagFreeCount(userId) <= 0;
	}

	@Override
	public boolean hasFull(long userId, int num) {
		int count = getBagFreeCount(userId);
		return count < num;
	}

	/**
	 * 背包装备加锁
	 */
	@Override
	public void doEquipLock(long userId, long treasuryId, int isLock) {
		this.lockTreasury(userId);
		Treasury treasury = treasuryDao.getTreasuryById(userId, treasuryId);
		if (treasury != null) {
			treasury.setIsLock(isLock);
			treasuryDao.updateTreasury(treasury);
			// 发刷新道具的事件-entId
			this.sendFlushTreasuryEvent(treasury);
		} else {
			throw new BaseException("没有可以加锁的装备");
		}
	}

	@Override
	public void updateEquipToEquipped(Treasury treasury, long troopGridId) {
		lockTreasury(treasury.getUserId());
		treasury.setEquip(troopGridId);
		treasuryDao.updateTreasury(treasury);
		// 发刷新道具的事件-entId
		this.sendFlushTreasuryEvent(treasury);

	}

	@Override
	public Map<String, ItemProperty> getEquipAttr(Treasury treasury,
			int sysHeroId) {
		Equip equip = (Equip) entityService.getEntity(treasury.getEntId());
		// 不要直接修改，否则脏了缓存数据
		Map<String, ItemProperty> newPropertys = new HashMap<String, ItemProperty>();
		int lv = treasury.getLevel();// 强化等级
		if (equip.isFragmentEquip()) {
			// 装备的基础属性
			List<ItemProperty> basePropertys = equip.getProperties();
			for (ItemProperty p : basePropertys) {
				ItemProperty p2 = p.clone();
				p2.setValue(p2.getValue() + p2.getAddValue() * lv);
				newPropertys.put(p2.getPropName(), p2);
			}
		}

		if (equip.isBuildEquip()) {
			// 打造装备要加的属性 系数
			int p = commonService.getSysParaIntValue(
					AppConstants.HERO_EQUIP_BUILD_ADD_ATTR, 0);
			List<ItemProperty> list = ItemProperty.parseProperty(treasury
					.getRandomProp());
			for (ItemProperty tmp : list) {
				ItemProperty p1 = newPropertys.get(tmp.getPropName());
				if (p1 == null) {
					ItemProperty cl = tmp.clone();
					float v = cl.getValue() * p / 100f;
					cl.setValue(cl.getValue()
							+ (int) (v * equip.getBuildFactorF() * lv));
					newPropertys.put(tmp.getPropName(), cl);
				} else {
					float v = tmp.getValue() * p / 100f;
					int addV = tmp.getValue()
							+ (int) (v * equip.getBuildFactorF() * lv);
					if (tmp.isAbs()) {// 加绝对值
						if (p1.isAbs()) {// 装备本身是绝对值
							p1.setValue(p1.getValue() + addV);
						} else {
							log.error("装备属性是加百分比,专属属性是加绝对值，无法加成");
						}
					} else {// 加百分比
						if (p1.isAbs()) {
							p1.setValue((int) (p1.getValue() * (1 + addV / 100d)));
						} else {
							p1.setValue(p1.getValue() + addV);
						}
					}
				}
			}
		}
		// 加装备特殊属性
		List<ItemProperty> list = ItemProperty.parseProperty(treasury
				.getSpecialAttr());
		addAttr(newPropertys, list);

		// 装备的武将专属属性
		if (equip.hasSpecSysHeroId(sysHeroId)) {
			List<ItemProperty> specPropertys = equip.getSpecProperties();
			addAttr(newPropertys, specPropertys);
		}
		return newPropertys;
	}

	/**
	 * 计算属性累计
	 * 
	 * @param rtAttr
	 *            最终保留的属性
	 * @param attr
	 *            提供属性基础数据
	 */
	private void addAttr(Map<String, ItemProperty> rtAttr,
			List<ItemProperty> attr) {
		for (ItemProperty tmp : attr) {
			ItemProperty p = rtAttr.get(tmp.getPropName());
			if (p == null) {
				rtAttr.put(tmp.getPropName(), tmp.clone());
			} else {
				if (tmp.isAbs()) {// 加绝对值
					if (p.isAbs()) {// 装备本身是绝对值
						p.setValue(p.getValue() + tmp.getValue());
					} else {
						log.error("装备属性是加百分比,专属属性是加绝对值，无法加成");
					}
				} else {// 加百分比
					if (p.isAbs()) {
						p.setValue((int) (p.getValue() * (1 + tmp.getValue() / 100d)));
					} else {
						p.setValue(p.getValue() + tmp.getValue());
					}
				}
			}
		}
	}

	@Override
	public void updateTreasury(Treasury treasury) {
		treasuryDao.updateTreasury(treasury);
	}

	@Override
	public void lockEquipDebris(long userId) {
		try {
			MemcachedManager.lock(TREASURY_HERO_EQUIP_DEBRIS_LOCKER + userId);
		} catch (TimeoutException e) {
			throw new BaseException(e);
		}

	}

	@Override
	public HeroEquipDebris lockAndGetEquipDebris(long userId) {
		lockEquipDebris(userId);
		return getEquipDebris(userId);
	}

	@Override
	public HeroEquipDebris getEquipDebris(long userId) {
		HeroEquipDebris ed = treasuryDao.getHeroEquipDebris(userId);
		if (ed == null) {
			ed = new HeroEquipDebris(userId, "", "");
			treasuryDao.createHeroEquipDebris(ed);
		}
		return ed;
	}

	@Override
	public HeroEquipDebris addHeroEquipDebris(long userId, Item equipDebris,
			int num, LogItemAct reason) {
		if (!equipDebris.getItemType().isEquipDebris()) {
			throw new BaseException("装备碎片类型错误");
		}
		if (num == 0) {
			throw new BaseException("装备碎片数量错误");
		}
		int entId = equipDebris.getEntId();
		lockTreasury(userId);
		HeroEquipDebris ed = lockAndGetEquipDebris(userId);
		int newNum = 0;
		Map<Integer, Integer> map = ed.getDebrisMap();
		newNum = equipDebrisAndCard(entId, num, map, false);
		ed.setDebris(map);

		treasuryDao.updateHeroEquipDebris(ed);
		if (num > 0) {
			setItemTLog(LogTypeConst.TYPE_ADDITEM, userId, equipDebris, num,
					newNum, reason);
		} else {
			setItemTLog(LogTypeConst.TYPE_DELITEM, userId, equipDebris, num,
					newNum, reason);
		}
		sendEquipCardAndDebrisMsg(userId, new EquipCardAndDebris(entId, newNum));
		return ed;
	}

	private int equipDebrisAndCard(int entId, int num,
			Map<Integer, Integer> map, boolean card) {
		int newNum = 0;
		if (num < 0) {// 扣除
			Integer count = map.get(entId);
			if (count == null) {
				count = 0;
			}
			newNum = count + num;
			if (newNum < 0) {
				if (card) {
					throw new BaseException("装备图纸不够" + num);
				}
				throw new BaseException("装备碎片不够" + num);
			}
		} else {// 增加
			Integer count = map.get(entId);
			if (count == null) {
				count = 0;
			}
			newNum = count + num;
		}
		map.put(entId, newNum);
		return newNum;
	}

	private void sendEquipCardAndDebrisMsg(long userId, EquipCardAndDebris info) {
		messageService.sendEventMessage(userId,
				EventMessage.TYPE_EQUIP_DEBRIS_OR_CARD,
				new SendEquipCardAndDebrisMsg(info));
	}

	@Override
	public HeroEquipDebris addHeroEquipCards(long userId, Item equipCard,
			int num, LogItemAct reason) {
		if (!equipCard.getItemType().isEquipCard()) {
			throw new BaseException("装备图纸类型错误");
		}
		if (num == 0) {
			throw new BaseException("装备图纸类型错误");
		}
		int entId = equipCard.getEntId();
		lockTreasury(userId);
		HeroEquipDebris ed = lockAndGetEquipDebris(userId);
		int newNum = 0;
		Map<Integer, Integer> map = ed.getCardsMap();
		newNum = equipDebrisAndCard(entId, num, map, true);
		ed.setCards(map);
		treasuryDao.updateHeroEquipDebris(ed);
		if (num > 0) {
			setItemTLog(LogTypeConst.TYPE_ADDITEM, userId, equipCard, num,
					newNum, reason);
		} else {
			setItemTLog(LogTypeConst.TYPE_DELITEM, userId, equipCard, num,
					newNum, reason);
		}
		sendEquipCardAndDebrisMsg(userId, new EquipCardAndDebris(entId, newNum));
		return ed;
	}

	@Override
	public List<DroppedEntity> doExchangeItem(long userId, long tId, int num) {
		if (num < 0 || num > 10000) {
			throw new BaseException("单词最大10000个");
		}

		// 加锁
		this.lockTreasury(userId);

		Treasury treasury = this.getTreasuryById(userId, tId);
		if (treasury == null || treasury.getUserId() != userId) {
			throw new BaseException("道具不存在");
		}

		if (treasury.getIsLock() != 0) {
			throw new BaseException("道具已加锁");
		}
		Item item = (Item) entityService.getEntity(treasury.getEntId());
		return this.doExchangeItem(userId, item, num);
	}

	@Override
	public List<DroppedEntity> doExchangeItem(long userId, Item item, int num) {
		if (num < 0 || num > 10000) {
			throw new BaseException("单词最大10000个");
		}

		// 加锁
		this.lockTreasury(userId);

		ItemExchange itemExchange = item.getExchange();
		if (itemExchange == null) {
			throw new BaseException("该道具不能兑换");
		}

		if (itemExchange == null || itemExchange.getNeeds() == null
				|| itemExchange.getNeeds().length == 0
				|| itemExchange.getTos() == null
				|| itemExchange.getTos().length == 0) {
			throw new BaseException("该道具不能兑换");
		}

		// 消耗的道具和数量
		int[][] needs = itemExchange.getNeeds();

		// 产出的道具和数量
		int[][] tos = itemExchange.getTos();

		// 可兑换的最小次数
		int min = 0;
		for (int[] need : needs) {
			int _entId = need[0];
			int _num = need[1];

			// 当前可兑换的次数
			int curNum = this.getTreasuryCountByEntId(userId, _entId) / _num;
			if (min <= 0 || min > curNum) {
				min = curNum;
			}

			if (min <= 0) {
				Item _item = (Item) entityService.getEntity(_entId);
				throw new BaseException(_item.getItemName() + "数量不足");
			}
		}

		if (num > min) {
			num = min;
		}

		if (num <= 0) {
			throw new BaseException(item.getItemName() + "兑换条件不足");
		}

		for (int[] need : needs) {
			int _entId = need[0];
			int _itemNum = num * need[1];

			this.deleteItemFromTreasury(userId, _entId, _itemNum, true,
					LogItemAct.LOGITEMACT_147);
		}

		List<DroppedEntity> dropList = new ArrayList<DroppedEntity>(tos.length);
		for (int[] to : tos) {
			int _entId = to[0];
			int _itemNum = num * to[1];

			// 添加到背包
			this.addItemToTreasury(userId, _entId, _itemNum, 0, 1, false, true,
					LogItemAct.LOGITEMACT_147);

			dropList.add(new DroppedEntity(_entId, _itemNum));
		}
		return dropList;

	}
}
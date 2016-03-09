package com.youxigu.dynasty2.hero.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.youxigu.dynasty2.develop.service.ICastleResService;
import com.youxigu.dynasty2.entity.domain.Entity;
import com.youxigu.dynasty2.entity.domain.Item;
import com.youxigu.dynasty2.entity.domain.Resource;
import com.youxigu.dynasty2.entity.domain.SysHero;
import com.youxigu.dynasty2.entity.service.IEntityService;
import com.youxigu.dynasty2.hero.dao.IHeroDrawDao;
import com.youxigu.dynasty2.hero.domain.Hero;
import com.youxigu.dynasty2.hero.domain.HeroPub;
import com.youxigu.dynasty2.hero.domain.UserPubAttr;
import com.youxigu.dynasty2.hero.enums.HeroDrawType;
import com.youxigu.dynasty2.hero.proto.HeroDrawInfo;
import com.youxigu.dynasty2.hero.proto.HeroDrawItemInfo;
import com.youxigu.dynasty2.hero.service.IHeroDrawService;
import com.youxigu.dynasty2.hero.service.IHeroService;
import com.youxigu.dynasty2.log.imp.LogHeroAct;
import com.youxigu.dynasty2.log.imp.LogItemAct;
import com.youxigu.dynasty2.mission.domain.Mission;
import com.youxigu.dynasty2.mission.service.IMissionService;
import com.youxigu.dynasty2.treasury.domain.DropPackItemInfo;
import com.youxigu.dynasty2.treasury.service.ITreasuryService;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.user.service.IUserService;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.dynasty2.util.TimeUtils;
import com.youxigu.dynasty2.vip.domain.UserVip;
import com.youxigu.dynasty2.vip.service.IVipService;

public class HeroDrawService implements IHeroDrawService {
	private Map<HeroDrawType, Map<Short, HeroPub>> heroPubMaps = null;// 酒馆配置
	private IHeroDrawDao heroDrawDao;
	// private ICommonService commonService;
	private IVipService vipService;
	private IUserService userService;
	private ITreasuryService treasuryService;
	private IHeroService heroService;
	private IEntityService entityService = null;
	private ICastleResService castleResService;
	private IMissionService missionService;

	/**
	 * 初始化酒馆配置数据
	 */
	public void init() {
		heroPubMaps = new HashMap<HeroDrawType, Map<Short, HeroPub>>(3);
		List<HeroPub> heroPubs = heroDrawDao.getHeroPubs();
		for (HeroPub hp : heroPubs) {
			hp.check();
			Map<Short, HeroPub> subs = heroPubMaps.get(hp.getDrawType());
			if (subs == null) {
				subs = new HashMap<Short, HeroPub>(8);
				heroPubMaps.put(hp.getDrawType(), subs);
			}
			subs.put(hp.getVipLv(), hp);
		}
	}

	@Override
	public HeroPub getHeroPub(HeroDrawType type, short vipLv) {
		Map<Short, HeroPub> maps = heroPubMaps.get(type);
		if (maps != null) {
			return maps.get(vipLv);
		}
		return null;
	}

	@Override
	public Map<HeroDrawType, Map<Short, HeroPub>> getHeroPubMaps() {
		return heroPubMaps;
	}

	@Override
	public UserPubAttr getUserPubAttrById(long userId) {
		UserPubAttr result = this.heroDrawDao.getUserPubAttrById(userId);
		if (result == null) {
			result = new UserPubAttr(userId);
			// HeroDrawType type = HeroDrawType
			// .valueOf(AppConstants.PUB_REFRESH_MODE_ITEM);
			// HeroPub heroPub = getHeroPub(type, (short) 0);
			// result.setHireCDByType(
			// type,
			// new Timestamp(System.currentTimeMillis()
			// + heroPub.getFreePeriod() * 1000));
			int version = TimeUtils.getVersionOfToday(System
					.currentTimeMillis());
			result.setVersion(version);
			this.heroDrawDao.insertUserPubAttr(result);
		}
		refreshDailyVersion(result);
		return result;
	}

	/**
	 * 刷新每日版本数据
	 */
	private void refreshDailyVersion(UserPubAttr result) {
		int version = TimeUtils.getVersionOfToday(System.currentTimeMillis());// 获取当前时间的数据版本号
		if (result.getVersion() == version) {
			return;
		}
		// 刷新抽奖数据
		result.refreshDailyData();
		result.setVersion(version);
		this.heroDrawDao.updateUserPubAttr(result);
	}

	@Override
	public HeroDrawInfo doHireHero(long userId, HeroDrawType type) {
		HeroDrawInfo res = new HeroDrawInfo();
		res.setLuck(-1);
		res.setTen(false);
		res.setType(type);

		long now = System.currentTimeMillis();
		int vipLv = 0;
		UserVip userVip = vipService.getUserVip(userId);
		if (userVip != null) {
			if (!userVip.isExpire()) {
				vipLv = userVip.getVipLv();
			}
		}

		HeroPub heroPub = getHeroPub(type, (short) vipLv);
		User user = userService.lockGetUser(userId);
		// String action = heroPub.getPubName();
		heroService.lockHero(userId);
		UserPubAttr upa = this.getUserPubAttrById(userId);

		int num = 0; // 已经抽的次数
		Timestamp cd = null;

		num = upa.getHireNumByType(type);
		cd = upa.getHireCDByType(type);

		// 用来记录log
		int mode = type.getIndex();
		num++;

		int dropPackId = heroPub.getDropPackId();

		boolean free = false;
		int freePeriod = heroPub.getFreePeriod();
		if (freePeriod >= 0) {// 有免费配置
			if (upa.getFreetimes(type) < heroPub.getFreetimes()) {
				if (cd == null || cd.getTime() <= now) {// 在免费期间内
					free = true;
					cd = new Timestamp(now + freePeriod * 1000L);
					dropPackId = heroPub.getFreeDropPackId();
				}
			}

		}

		if (heroPub.isLuckNum(num)) {
			if (type == HeroDrawType.HERO_DRAW_TYPE3) {
				mode = 4;
			}
			// 是必紫抽取次数
			dropPackId = heroPub.getLuckDropPackId();
		}

		if (!free) {
			int cash = heroPub.getCash();
			int itemId = heroPub.getItem();
			boolean cost = false;
			// 是否扣道具
			if (itemId > 0) {// 使用道具
				treasuryService
						.deleteItemFromTreasury(userId, itemId,
								heroPub.getItemCount(), true,
								LogItemAct.LOGITEMACT_118);
				cost = true;
			}
			if (!cost && cash > 0) {
				if (user.getCash() < cash) {
					throw new BaseException("您的钻石不足,是否充值？");
				}
				userService.doConsumeCash(user, cash, heroPub.getLog());
				cost = true;
			}

			if (!cost) {
				throw new BaseException("配置数据错误");
			}

		}

		if (upa.isFirstDraw(type)) {
			dropPackId = heroPub.getFirstDropPackId();
			upa.setFirstDraw(type);
		}
		// 掉落
		List<DropPackItemInfo> dropItems = heroService.dropItem(user,
				dropPackId, 1, true, LogItemAct.LOGITEMACT_118);
		if (dropItems != null) {
			// StringBuilder sb = new StringBuilder(255);
			Iterator<DropPackItemInfo> lit = dropItems.iterator();
			while (lit.hasNext()) {
				DropPackItemInfo one = lit.next();
				Entity en = entityService.getEntity(one.getEntId());
				if (en instanceof Resource) {
					castleResService.doAddRes(user.getMainCastleId(),
							one.getEntId(), one.getNum(), true);
					continue;
				}
				HeroDrawItemInfo io = new HeroDrawItemInfo();
				io.setDrops(one);
				res.getDrops().add(io);

				int entId = one.getEntId();
				int entNum = one.getNum();
				SysHero sysHero = heroService.getSysHeroById(entId);
				heroService.tlog_hero_hire(user, mode, sysHero, 1, free);

				Hero src = heroService.getHeroBySysHeroId(userId, entId);

				// int heroType = 1; // ==1身上的武将,=2在野的武将,=3武将卡
				Hero hero = heroService.doCreateAHero(userId, entId,
						LogHeroAct.Pub_Hero_ADD);
				// if (hero == null) {
				// Hero h = heroService.getHeroBySysHeroId(userId, entId);
				// io.setHeroId(h.getHeroId());
				// heroType = 3;
				// } else {
				// io.setHeroId(hero.getHeroId());
				// }

				if (src == null || !src.isHero()) {
					// 新抽到的武将
					io.setType(2);
				} else {
					io.setType(3);
				}
				if(hero==null){
					Hero h = heroService.getHeroBySysHeroId(userId, entId);
					io.setHeroId(h.getHeroId());
				}else{
					io.setHeroId(hero.getHeroId());
				}
				

				if (entNum > 1) {
					// 剩下的数量直接生成卡
					heroService.doAddHeroCardNum(userId, hero,
							(Item) entityService.getEntity(sysHero
									.getHeroCardId()), entNum - 1,
							LogItemAct.LOGITEMACT_118);
				}
			}
		}

		// save
		upa.setHireNumByType(type, num);
		upa.setHireCDByType(type, cd);
		if (free) {
			upa.addFreetimes(type);// 增加免费抽奖次数
		}
		heroDrawDao.updateUserPubAttr(upa);

		res.setLuck(heroPub.getNextLuckNum(num + 1));
		res.setNum(num);
		if (cd != null) {
			long cdSec = (cd.getTime() - now) / 1000L + 1;
			if (cdSec > 0) {
				res.setCd(cdSec);
			}

		}

		// 通知酒馆抽武将N次,累计值
		missionService.notifyMissionModule(user, Mission.QCT_TYPE_RECORD, 0, 1);
		return res;
	}

	@Override
	public HeroDrawInfo doHireHero10(long userId, HeroDrawType type) {
		int batchCount = 10;// 抽奖10次
		HeroDrawInfo res = new HeroDrawInfo();
		res.setLuck(-1);
		res.setTen(true);
		res.setType(type);
		int vipLv = 0;
		UserVip userVip = vipService.getUserVip(userId);
		if (userVip != null) {
			if (!userVip.isExpire()) {
				vipLv = userVip.getVipLv();
			}
		}

		HeroPub heroPub = getHeroPub(type, (short) vipLv);
		// String action = heroPub.getPubName();
		User user = userService.lockGetUser(userId);

		boolean cost = false;// 标记抽奖扣除物品或元宝是否成功
		if (type == HeroDrawType.HERO_DRAW_TYPE3) {
			int cash = heroPub.getCash10();
			if (cash <= 0) {
				throw new BaseException("不支持10连抽");
			}
			userService.doConsumeCash(user, cash, heroPub.getLog10());
		} else {
			int cash = heroPub.getCash10();
			int itemId = heroPub.getItem();
			if (cash <= 0 && itemId <= 0) {
				throw new BaseException("配置数据错误 10连抽未配置消耗的元宝和物品");
			}
			// 判断道具是否够10连抽
			if (itemId > 0) {// 使用道具
				// 先取道具数量
				int itemNum = treasuryService
						.getNotEquippedTreasuryCountByEntId(userId, itemId);
				if (itemNum < heroPub.getItemCount10()) {
					throw new BaseException("10连抽道具不够");
				}
				treasuryService.deleteItemFromTreasury(userId, itemId,
						heroPub.getItemCount10(), true,
						LogItemAct.LOGITEMACT_118);
				cost = true;
			}
			if (!cost) {
				// 判断元宝抽奖是否够
				if (cash > 0) {
					if (user.getCash() < cash) {
						throw new BaseException("10连抽您的钻石不足,是否充值？");
					}
					userService.doConsumeCash(userId, cash, heroPub.getLog());
					cost = true;
				} else {
					throw new BaseException("配置数据错误 10连抽未配置消耗的元宝");
				}
			}

		}
		if (!cost) {
			throw new BaseException("扣除 10连抽消耗错误");
		}
		heroService.lockHero(userId);
		UserPubAttr upa = this.getUserPubAttrById(userId);

		int num = 0; // 已经抽的次数
		Timestamp cd = null;
		if (upa != null) {
			num = upa.getHireNumByType(type);
			cd = upa.getHireCDByType(type);
		}

		List<DropPackItemInfo> allDropItems = new ArrayList<DropPackItemInfo>();
		for (int i = 0; i < batchCount; i++) {
			num++;
			int dropPackId = 0;
			int mode = type.getIndex();
			if (heroPub.isLuckNum(num)) {
				mode = 4;
				dropPackId = heroPub.getLuckDropPackId();

			} else {
				dropPackId = heroPub.getDropPackId();
			}
			List<DropPackItemInfo> oneTime = heroService.dropItem(user,
					dropPackId, 1, true, LogItemAct.LOGITEMACT_118);
			allDropItems.addAll(oneTime);
			// tlog
			Iterator<DropPackItemInfo> lit = oneTime.iterator();
			while (lit.hasNext()) {
				DropPackItemInfo one = lit.next();
				int entId = one.getEntId();
				int entNum = one.getNum();
				Entity en = entityService.getEntity(entId);
				if (en instanceof Resource) {
					castleResService.doAddRes(user.getMainCastleId(), entId,
							entNum, true);
					continue;
				}
				SysHero sysHero = heroService.getSysHeroById(entId);
				heroService.tlog_hero_hire(user, mode, sysHero, entNum, false);
			}

		}
		// StringBuilder sb = new StringBuilder(255);
		Iterator<DropPackItemInfo> lit = allDropItems.iterator();
		while (lit.hasNext()) {
			DropPackItemInfo one = lit.next();
			HeroDrawItemInfo io = new HeroDrawItemInfo();
			io.setDrops(one);

			int entId = one.getEntId();
			int entNum = one.getNum();
			Entity en = entityService.getEntity(entId);
			if (en instanceof Resource) {
				continue;
			}

			res.getDrops().add(io);
			SysHero sysHero = heroService.getSysHeroById(entId);
			int heroType = 1; // ==1身上的武将,=2在野的武将,=3武将卡
			Hero hero = heroService.doCreateAHero(userId, entId,
					LogHeroAct.Pub_Hero_ADD);
			if (hero == null) {
				Hero h = heroService.getHeroBySysHeroId(userId, entId);
				io.setHeroId(h.getHeroId());
				heroType = 3;
			} else {
				io.setHeroId(hero.getHeroId());
			}
			io.setType(heroType);
			if (entNum > 1) {
				// 剩下的数量直接生成卡
				heroService
						.doAddHeroCardNum(userId, hero, (Item) entityService
								.getEntity(sysHero.getHeroCardId()),
								entNum - 1, LogItemAct.LOGITEMACT_118);
			}
		}
		// // save
		if (upa == null) {
			upa = new UserPubAttr(userId);
			upa.setHireNumByType(type, num);
			heroDrawDao.insertUserPubAttr(upa);
		} else {
			upa.setHireNumByType(type, num);
			heroDrawDao.updateUserPubAttr(upa);
		}
		res.setLuck(heroPub.getNextLuckNum(num + 1));
		res.setNum(num);
		if (cd != null) {
			long cdSec = (cd.getTime() - System.currentTimeMillis()) / 1000L + 1;
			if (cdSec > 0) {
				res.setCd(cdSec);
			}
		}

		// 通知酒馆抽武将N次,累计值
		missionService.notifyMissionModule(user, Mission.QCT_TYPE_RECORD, 0,
				batchCount);
		return res;
	}

	public void setHeroDrawDao(IHeroDrawDao heroDrawDao) {
		this.heroDrawDao = heroDrawDao;
	}

	public void setVipService(IVipService vipService) {
		this.vipService = vipService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public void setTreasuryService(ITreasuryService treasuryService) {
		this.treasuryService = treasuryService;
	}

	public void setHeroService(IHeroService heroService) {
		this.heroService = heroService;
	}

	public void setEntityService(IEntityService entityService) {
		this.entityService = entityService;
	}

	public void setCastleResService(ICastleResService castleResService) {
		this.castleResService = castleResService;
	}

	public void setMissionService(IMissionService missionService) {
		this.missionService = missionService;
	}

}

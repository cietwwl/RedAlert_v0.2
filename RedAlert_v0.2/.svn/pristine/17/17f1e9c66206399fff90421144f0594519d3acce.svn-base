package com.youxigu.dynasty2.hero.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.youxigu.dynasty2.common.AppConstants;
import com.youxigu.dynasty2.common.service.ICommonService;
import com.youxigu.dynasty2.develop.service.ICastleResService;
import com.youxigu.dynasty2.entity.domain.Entity;
import com.youxigu.dynasty2.entity.domain.Equip;
import com.youxigu.dynasty2.entity.domain.EquipLevelUpRule;
import com.youxigu.dynasty2.entity.domain.Item;
import com.youxigu.dynasty2.entity.domain.ItemProperty;
import com.youxigu.dynasty2.entity.domain.effect.EffectTypeDefine;
import com.youxigu.dynasty2.entity.domain.enumer.EquipPosion;
import com.youxigu.dynasty2.entity.service.IEntityService;
import com.youxigu.dynasty2.entity.service.IEquipService;
import com.youxigu.dynasty2.hero.domain.EquipSuitInfo;
import com.youxigu.dynasty2.hero.domain.Hero;
import com.youxigu.dynasty2.hero.domain.HeroEquipDebris;
import com.youxigu.dynasty2.hero.domain.HeroFate;
import com.youxigu.dynasty2.hero.domain.TroopGrid;
import com.youxigu.dynasty2.hero.proto.EquipInfoMsg;
import com.youxigu.dynasty2.hero.service.IHeroEquipService;
import com.youxigu.dynasty2.hero.service.IHeroService;
import com.youxigu.dynasty2.hero.service.ITroopGridService;
import com.youxigu.dynasty2.log.ILogService;
import com.youxigu.dynasty2.log.LogBeanFactory;
import com.youxigu.dynasty2.log.imp.LogCashAct;
import com.youxigu.dynasty2.log.imp.LogItemAct;
import com.youxigu.dynasty2.mission.domain.Mission;
import com.youxigu.dynasty2.mission.service.IMissionService;
import com.youxigu.dynasty2.treasury.domain.Treasury;
import com.youxigu.dynasty2.treasury.enums.SellType;
import com.youxigu.dynasty2.treasury.service.ITreasuryService;
import com.youxigu.dynasty2.user.domain.AchieveType;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.user.service.IUserAchieveService;
import com.youxigu.dynasty2.user.service.IUserService;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.dynasty2.util.EffectValue;
import com.youxigu.dynasty2.util.StringUtils;

import edu.emory.mathcs.backport.java.util.Collections;

public class HeroEquipService implements IHeroEquipService {
	private static final Logger log = LoggerFactory
			.getLogger(HeroEquipService.class);

	private IHeroService heroService = null;
	private ITreasuryService treasuryService = null;
	private IEntityService entityService = null;
	private IUserService userService = null;
	private IEquipService equipService = null;
	private ILogService logService = null;
	private ITroopGridService troopGridService = null;
	private ICommonService commonService;
	private ICastleResService castleResService;
	private IMissionService missionService;
	private IUserAchieveService userAchieveService;

	public void setCastleResService(ICastleResService castleResService) {
		this.castleResService = castleResService;
	}

	public void setMissionService(IMissionService missionService) {
		this.missionService = missionService;
	}

	public void setUserAchieveService(IUserAchieveService userAchieveService) {
		this.userAchieveService = userAchieveService;
	}

	public void check(User user) {
		int level = commonService.getSysParaIntValue(
				AppConstants.SYS_EQUIP_FACTORY_OPEN_STATUS, 1);
		if (user.getUsrLv() < level) {
			throw new BaseException("兵器工厂未开启");
		}
	}

	@Override
	public Hero doTakeEquip(long userId, long heroId, long treasuryId) {
		User user = userService.getUserById(userId);
		check(user);
		// 判定武将状态
		Hero hero = heroService.lockAndGetHero(userId, heroId);
		if (hero == null) {
			throw new BaseException("武将不存在。");
		}
		if (!hero.isInTroop()) {
			throw new BaseException("武将不在军团中");
		}
		if (!hero.idle()) {
			throw new BaseException("武将必须是空闲状态");
		}

		troopGridService.lockTroopGrid(userId);
		treasuryService.lockTreasury(userId);// 锁道具

		Treasury treasury = treasuryService.getTreasuryById(userId, treasuryId);
		if (treasury == null) {
			throw new BaseException("背包中不存在此道具");
		}

		if (!treasury.isEquip()) {
			throw new BaseException("此物品不是装备");
		}
		// 判定背包道具状态
		if (hero.getUserId() != treasury.getUserId()) {
			throw new BaseException("只能装备自己拥有的道具");
		}
		if (treasury.isEquipToHero()) {
			throw new BaseException("道具已装备");
		}

		EquipPosion ep = treasury.getEquipPosion();
		if (ep == null) {
			throw new BaseException("装备位置未知");
		}

		TroopGrid tg = hero.getTroopGrid();
		if (tg == null) {
			throw new BaseException("武将未配置在军团中");
		}

		long equTreasuryId = ep.getEqu(tg);// 身上对应装备槽里的装备
		if (equTreasuryId == treasuryId) {
			throw new BaseException("装备已在身上。");
		}
		// 脱掉身上的装备
		if (equTreasuryId > 0) {
			this.doTakeOffEquip(hero, equTreasuryId, ep, false, false);// 脱下身上装备
		}
		// 穿上新装备
		ep.setEqu(tg, treasuryId);
		troopGridService.updateTroopGrid(tg);
		// 表示装备已经被穿上
		treasuryService.updateEquipToEquipped(treasury, tg.getTroopGridId());
		// 处理装备情缘
		updateHeroEquipFate(userId, tg);
		heroService.updateHero(hero, true);

		// 任意武将身上装备达到N个 0/当前数
		missionService.notifyMissionModule(user, Mission.QCT_TYPE_HEROEQUIPNUM,
				0, tg.getEquipNum());

		// 成就
		if (hero.isCommander()) {
			userAchieveService.doNotifyAchieveModule(user.getUserId(),
					AchieveType.TYPE_MILITARY,
					AchieveType.TYPE_MILITARY_ENTID7, 1);
		}

		// 发提示消息
		if (log.isDebugEnabled()) {
			log.debug("武将换装备_userId[" + hero.getUserId() + "]_heroId["
					+ hero.getHeroId() + "]_treasuryId[" + treasury.getId()
					+ "]");
		}
		return hero;
	}

	/**
	 * 更新武将的装备情缘--穿单件装备的情况
	 * 
	 * @param hero
	 */
	private void updateHeroEquipFate(long userId, TroopGrid tg) {
		if (tg == null) {
			return;
		}
		// 获取武将身上的所有装备
		List<Long> eqs = tg.getEquip();
		Map<Integer, Treasury> eqFate = new HashMap<Integer, Treasury>();
		for (long l : eqs) {
			if (l <= 0) {
				continue;
			}
			Treasury tr = treasuryService.getTreasuryById(userId, l);
			eqFate.put(tr.getEntId(), tr);
		}
		if (eqFate.isEmpty()) {// 装备为null
			return;
		}
		// 计算每件装备的套装属性
		Map<Integer, Integer> fates = new HashMap<Integer, Integer>();
		for (Treasury t : eqFate.values()) {
			Equip eq = (Equip) t.getItem();
			List<HeroFate> heroFates = eq.getEquipFates();
			if (heroFates != null) {
				for (HeroFate fate : heroFates) {
					int v = fate.satisfyHeroFate(eqFate.keySet());
					if (v >= HeroFate.DEFAULT_EQUIP) {
						int id = fate.calculateHeroFateId(v);
						fates.put(id, id);
					}
				}
			}
		}

		StringBuilder sb = new StringBuilder();
		for (int id : fates.keySet()) {
			if (sb.length() > 0) {
				sb.append(",");
			}
			sb.append(id);
		}

		String fateStr = sb.toString();
		if (!StringUtils.isEmpty(fateStr)) {
			tg.setEquipFate(fateStr);
			troopGridService.updateTroopGrid(tg);
		}
	}

	public Hero doTakeOffEquip(Hero hero, long treasuryId, EquipPosion ep,
			boolean update, boolean resetEquipFate) {

		long userId = hero.getUserId();
		// long heroId = hero.getHeroId();
		if (ep == null) {
			throw new BaseException("卸下的装备不存在或装备位置错误");
		}
		Treasury treasury = treasuryService.getTreasuryById(userId, treasuryId);

		TroopGrid tg = hero.getTroopGrid();
		long equTreasuryId = ep.getEqu(tg);// 身上对应装备槽里的装备
		if (treasury != null) {
			if (treasury.getUserId() != userId || !treasury.isEquip()) {
				throw new BaseException("装备不存在。");
			}
		}

		if (equTreasuryId != treasuryId) {
			// / 外网出现bug:一个武将传了2个同样类型的装备。 没找到原因,.这里是修补出现bug,
			if (equTreasuryId != 0) {
				treasuryId = equTreasuryId;
			} else {
				treasuryId = -1;
			}
			// throw new BaseException("该装备没有穿在武将身上。");
		}
		// int total = 0;
		// List<Treasury> treList =
		// treasuryService.getTreasurysByUserId(userId);
		// if (treList != null) {
		// for (Treasury t : treList) {
		// long equipHeroId = t.getEquip();
		// if (equipHeroId == 0) {
		// total++;
		// } else {
		// // 外网出现bug:一个武将传了2个同样类型的装备。 没找到原因,.这里是修补出现bug,
		// if (equipHeroId == heroId && t.getEquipPosion() == ep
		// && t.getId() != treasuryId) {
		// log.error("同一类型装备武将身上有多个，去掉一个:{}.{}", userId, t.getId());
		// total++;
		// if (equTreasuryId == 0) {
		// treasuryId = t.getId();
		// equTreasuryId = treasuryId;
		// } else {
		// treasuryService.updateEquipToEquipped(t, 0);
		// }
		// }
		// }
		// }
		//
		// }
		if (treasuryService.hasFull(userId)) {
			throw new BaseException("背包容量不足,无法脱下武将装备！");
		}

		ep.setEqu(tg, 0);
		troopGridService.updateTroopGrid(tg);

		// 修改道具装备状态
		if (treasury != null) {
			treasuryService.updateEquipToEquipped(treasury, 0);
		}

		if (resetEquipFate) {
			updateHeroEquipFate(userId, tg);
		}

		if (update) {
			heroService.updateHero(hero, true);
		}
		return hero;
	}

	@Override
	public Hero doAutoTakeAllEquip(long userId, long heroId) {
		User user = userService.getUserById(userId);
		check(user);
		// 判定武将状态
		Hero hero = heroService.lockAndGetHero(userId, heroId);
		if (hero == null || hero.getUserId() != userId) {
			throw new BaseException("武将不存在。");
		}

		if (!hero.isHero()) {
			throw new BaseException("不是武将");
		}

		if (!hero.isInTroop()) {
			throw new BaseException("武将必须军团里面");
		}

		if (!hero.idle()) {
			throw new BaseException("武将必须是空闲状态");
		}
		troopGridService.lockTroopGrid(userId);
		treasuryService.lockTreasury(userId);// 锁道具
		// 用来计算装备情缘..装备穿完后 统一计算每个位置的装备情缘
		Map<Integer, Integer> equipedEntIdMaps = new HashMap<Integer, Integer>(
				6);
		Map<EquipPosion, List<EquipSuitInfo>> eps = getBestTreasury(hero);
		if (eps.isEmpty()) {
			// TODO 一键装备都没有不自动换装了
			return hero;
		}
		TroopGrid tg = hero.getTroopGrid();
		for (EquipPosion ep : EquipPosion.values()) {
			// 一个格子 一个格子的换装备.
			// 1已经装备则卸下来
			long equipId = ep.getEqu(tg);
			if (equipId > 0) {
				doTakeOffEquip(hero, equipId, ep, false, false);
			}
			List<EquipSuitInfo> e = eps.get(ep);
			if (e != null && !e.isEmpty()) {
				// 获取第一件装备..最好的
				EquipSuitInfo info = e.get(0);
				// 后面统一更新格子里面的数据
				ep.setEqu(tg, info.getTreasury().getId());
				treasuryService.updateEquipToEquipped(info.getTreasury(),
						tg.getTroopGridId());
				equipedEntIdMaps.put(info.getTreasury().getEntId(), info
						.getTreasury().getEntId());
			}
		}
		troopGridService.updateTroopGrid(tg);
		// 武将装备情缘
		updateHeroEquipFate(userId, tg);

		heroService.updateHero(hero, true);

		// 任意武将身上装备达到N个 0/当前数
		missionService.notifyMissionModule(user, Mission.QCT_TYPE_HEROEQUIPNUM,
				0, tg.getEquipNum());

		// 成就
		if (hero.isCommander()) {
			userAchieveService.doNotifyAchieveModule(user.getUserId(),
					AchieveType.TYPE_MILITARY,
					AchieveType.TYPE_MILITARY_ENTID7, 1);
		}

		return hero;
	}

	/**
	 * 获取已经穿戴在武将身上的装备和背包里面未穿戴的装备的一个战力排序
	 * 
	 * @param hero
	 * @return
	 */
	private Map<EquipPosion, List<EquipSuitInfo>> getBestTreasury(Hero hero) {
		// 全部道具
		List<Treasury> treasurys = treasuryService.getTreasurysByUserId(hero
				.getUserId());
		Map<Integer, Integer> eqFates = new HashMap<Integer, Integer>();
		// 选择出所有未穿戴的装备
		List<Treasury> ts = new ArrayList<Treasury>();
		for (Treasury t : treasurys) {// 此方法里面已经包含了武将穿戴的装备
			if (!(t.getItem() instanceof Equip)) {
				continue;
			}

			if (t.isEquipToHero() && t.getEquip() != hero.getTroopGridId()) {
				// 不是穿在自己身上的要过滤掉
				continue;
			}
			// 只要没穿戴的装备和自己身上的装备
			ts.add(t);
			eqFates.put(t.getEntId(), t.getEntId());
		}

		// for (EquipPosion ep : EquipPosion.values()) {
		// // 装备Id
		// TroopGrid tg = hero.getTroopGrid();
		// long equipId = ep.getEqu(tg);
		// if (equipId <= 0) {
		// continue;
		// }
		// Treasury t = treasuryService.getTreasuryById(hero.getUserId(),
		// equipId);
		// ts.add(t);
		//
		// eqFates.put(t.getEntId(), t.getEntId());
		// }

		Map<EquipPosion, List<EquipSuitInfo>> result = new HashMap<EquipPosion, List<EquipSuitInfo>>();
		for (Treasury t : ts) {
			Equip eq = (Equip) t.getItem();
			List<HeroFate> hf = eq.getEquipFates();
			// 这件装备加的套装属性
			Map<String, EffectValue> rev = new HashMap<String, EffectValue>();
			if (hf != null && !hf.isEmpty()) {
				for (HeroFate f : hf) {
					// int[] reqEntIds = f.getReqEntIds();
					// boolean match = true;
					// for (Integer reqEntId : reqEntIds) {
					// if (!eqFates.containsKey(reqEntId)) {
					// match = false;
					// break;
					// }
					// }
					int i = f.satisfyHeroFate(eqFates.keySet());
					if (i >= HeroFate.DEFAULT_EQUIP) {
						// 套装属性需要除以装备的数量
						Map<String, EffectValue> ev = f.getEquipFate(i);
						for (Entry<String, EffectValue> en : ev.entrySet()) {
							EffectValue src = en.getValue();
							EffectValue r = rev.get(en.getKey());
							if (r == null) {
								r = new EffectValue();
								rev.put(en.getKey(), r);
							}
							r.setAbsValue(r.getAbsValue() + src.getAbsValue()
									/ i);
							r.setPerValue(r.getPerValue() + src.getPerValue()
									/ i);
						}
					}
					// if (match) {
					// int s = reqEntIds.length;
					// // 套装属性需要除以装备的数量
					// Map<String, EffectValue> ev = f.getProperties();
					// for (Entry<String, EffectValue> en : ev.entrySet()) {
					// EffectValue src = en.getValue();
					// EffectValue r = rev.get(en.getKey());
					// if (r == null) {
					// r = new EffectValue();
					// rev.put(en.getKey(), r);
					// }
					// r.setAbsValue(r.getAbsValue() + src.getAbsValue()
					// / s);
					// r.setPerValue(r.getPerValue() + src.getPerValue()
					// / s);
					// }
					// }
				}
			}
			int p = getEquipPointByHero(hero, t, rev);
			EquipSuitInfo info = new EquipSuitInfo(p, t);
			List<EquipSuitInfo> eqs = result.get(t.getEquipPosion());
			if (eqs == null) {
				eqs = new ArrayList<EquipSuitInfo>();
				result.put(t.getEquipPosion(), eqs);
			}
			eqs.add(info);
		}
		// 排序所有装备
		for (List<EquipSuitInfo> l : result.values()) {
			Collections.sort(l);
		}
		return result;
	}

	/**
	 * 获取指定的属性,此方法只在自动换装备的时候调用
	 * 
	 * @param properties
	 * @param key
	 * @param isPercent
	 * @param equipFate
	 *            装备的套装属性
	 * @return
	 */
	private int getEquipAttr(Map<String, ItemProperty> properties, String key,
			boolean isPercent, Map<String, EffectValue> equipFate) {
		int abs = 0;
		int percent = 0;
		if (equipFate != null && !equipFate.isEmpty()) {
			EffectValue val = equipFate.get(key);
			if (val != null) {
				abs += val.getAbsValue();
				percent += val.getPerValue();
			}
		}

		ItemProperty val = properties.get(key);
		if (val != null) {
			abs += val.getAbsValue();
			percent += val.getPercentValue();
		}
		if (isPercent) {
			return percent;
		} else {
			double finalProp = (1 + percent / 1000d) + abs;
			return (int) finalProp;
		}
	}

	/**
	 * 计算装备战斗力
	 * 
	 * @param hero
	 * @param treasury
	 * @return
	 */
	private int getEquipPointByHero(Hero hero, Treasury treasury,
			Map<String, EffectValue> equipFate) {

		Map<String, ItemProperty> properties = treasuryService.getEquipAttr(
				treasury, hero.getSysHeroId());

		double tmp1 = getEquipAttr(properties,
				EffectTypeDefine.H_PHYSICALATTACK, false, equipFate)
				+ getEquipAttr(properties, EffectTypeDefine.H_MAGICATTACK,
						false, equipFate)
				+ getEquipAttr(properties, EffectTypeDefine.H_MAGICDEFEND,
						false, equipFate)
				+ getEquipAttr(properties, EffectTypeDefine.H_PHYSICALDEFEND,
						false, equipFate);
		double tmp2 = (getEquipAttr(properties, EffectTypeDefine.H_INIT_HP,
				false, equipFate)) / 5d;

		double tmp3 = (getEquipAttr(properties, EffectTypeDefine.H_Hit, false,
				equipFate) + getEquipAttr(properties, EffectTypeDefine.H_DODGE,
				false, equipFate)) * 5d;

		double tmp4 = getEquipAttr(properties, EffectTypeDefine.H_DAMAGE,
				false, equipFate)
				+ getEquipAttr(properties, EffectTypeDefine.H_SHIELD, false,
						equipFate);

		double tmp5 = 1.0d
				+ getEquipAttr(properties, EffectTypeDefine.H_CRITADD, false,
						equipFate)
				+ getEquipAttr(properties, EffectTypeDefine.H_CRITDEC, false,
						equipFate)
				+ getEquipAttr(properties, EffectTypeDefine.H_CRITDAMAGE,
						false, equipFate)
				+ getEquipAttr(properties, EffectTypeDefine.H_CRITDAMAGE_DEC,
						false, equipFate)
				+ getEquipAttr(properties, EffectTypeDefine.H_DAMAGE_PER, true,
						equipFate)
				+ getEquipAttr(properties, EffectTypeDefine.H_SHIELD_PER, true,
						equipFate);

		double tmp = Math.pow((((tmp1 + tmp2 + tmp3 + tmp4) + tmp5)), 0.5);
		return (int) tmp;

	}

	@Override
	public Hero doTakeOffEquip(long userId, long heroId, long treasuryId) {
		User user = userService.getUserById(userId);
		check(user);
		Hero hero = heroService.lockAndGetHero(userId, heroId);
		if (hero == null || hero.getUserId() != userId) {
			throw new BaseException("武将不存在。");
		}

		if (!hero.idle()) {
			throw new BaseException("武将必须是空闲状态");
		}

		if (!hero.isInTroop()) {
			throw new BaseException("武将不在军团里面");
		}

		Treasury treasury = treasuryService.getTreasuryById(userId, treasuryId);
		if (treasury == null) {
			throw new BaseException("物品不存在");
		}
		if (!treasury.isEquipToHero()) {
			throw new BaseException("物品未装备,不用卸载");
		}

		troopGridService.lockTroopGrid(userId);
		treasuryService.lockTreasury(userId);// 锁道具

		EquipPosion ep = treasury.getEquipPosion();
		return doTakeOffEquip(hero, treasuryId, ep, true, true);
	}

	@Override
	public Hero doTakeOffAllEquip(long userId, long heroId) {
		User user = userService.getUserById(userId);
		check(user);
		Hero hero = heroService.lockAndGetHero(userId, heroId);
		if (hero == null || hero.getUserId() != userId) {
			throw new BaseException("武将不存在。");
		}
		if (!hero.idle()) {
			throw new BaseException("武将必须是空闲状态");
		}

		if (!hero.isInTroop()) {
			throw new BaseException("武将不在军团里面");
		}
		troopGridService.lockTroopGrid(userId);
		treasuryService.lockTreasury(userId);// 锁道具

		TroopGrid tg = hero.getTroopGrid();
		for (EquipPosion p : EquipPosion.values()) {// 查看武将身上是否有装备
			long treasuryId = p.getEqu(tg);
			if (treasuryId > 0) {
				doTakeOffEquip(hero, treasuryId, p, false, false);
			}
		}
		tg.setEquipFate(null);
		troopGridService.updateTroopGrid(tg);

		heroService.updateHero(hero, true);
		return hero;
	}

	@Override
	public EquipInfoMsg doEquipLevelup(long userId, long treasuryId, int num) {
		User user = userService.getUserById(userId);
		check(user);
		treasuryService.lockTreasury(userId);
		// 要强化的装备
		Treasury treasury = treasuryService.getTreasuryById(userId, treasuryId);
		if (treasury == null || treasury.getUserId() != userId) {
			throw new BaseException("装备错误");
		}
		if (!treasury.isEquip()) {
			throw new BaseException("不是装备，不能强化");
		}
		int lv = commonService.getSysParaIntValue(
				AppConstants.HERO_EQUIP_LEVEL_UP, 2);
		int max = lv * user.getUsrLv();
		if (treasury.getLevel() >= max) {
			throw new BaseException("前强化等级已达上限，请提升指挥官等级！");
		}
		Entity tmp = entityService.getEntity(treasury.getEntId());
		Equip entity = (Equip) tmp;
		int consumeMoney = 0;// 消耗的金矿
		int oldLevel = treasury.getLevel();
		int nextLevel = oldLevel + 1;
		if (oldLevel + num > max) {
			num = max - oldLevel;
		}
		int uplv = oldLevel;
		boolean isThrow = (num == 1);
		for (int i = 0; i < num; i++) {
			EquipLevelUpRule rule = equipService.getEquipLevelUpRule(
					entity.getColor(), nextLevel);
			if (rule == null) {
				if (isThrow) {
					throw new BaseException("已经达到最大级别，装备不能继续强化");
				}
				break;
			}
			// 检查道具
			int entId = rule.getEntId();
			int entNum = rule.getEntNum();
			if (entId > 0 && entNum > 0) {
				treasuryService.deleteItemFromTreasury(userId, entId, entNum,
						true, LogItemAct.LOGITEMACT_20);
			}

			entId = rule.getEntId2();
			entNum = rule.getEntNum2();
			if (entId > 0 && entNum > 0) {
				treasuryService.deleteItemFromTreasury(userId, entId, entNum,
						true, LogItemAct.LOGITEMACT_20);
			}
			uplv += 1;
			// 累计消耗的金矿
			consumeMoney += rule.getLvUpGoldNum();
			this.equipLvUpTLog(userId, treasury.getId(), treasury.getEntId(),
					rule.getEntId(), rule.getEntNum(), rule.getEntId2(),
					rule.getEntNum2(), rule.getLvUpGoldNum());
		}
		castleResService.doDelRes(user.getMainCastleId(),
				AppConstants.ENT_RES_GOLD, consumeMoney, true, true);
		treasury.setLevel(uplv);

		// 装备属性变更
		if (oldLevel != treasury.getLevel()) {
			treasuryService.updateTreasury(treasury);
			// 更新武将装备效果
			updateHeroEffectByEquipPropChanged(treasury, treasury.getEquip());
			// 发刷新道具的事件-entId
			treasuryService.sendFlushTreasuryEvent(treasury);

			// 强化装备到N级,最大值： 0/当前等级
			missionService.notifyMissionModule(user, Mission.QCT_TYPE_EQUIPLV,
					0, treasury.getLevel());

			// X件Y品质Z级装备成就
			userAchieveService.doNotifyAchieveModule(userId,
					AchieveType.TYPE_MILITARY,
					AchieveType.TYPE_MILITARY_ENTID2, 1);
		}
		EquipInfoMsg msg = new EquipInfoMsg();
		StringBuffer retuMsg = new StringBuffer(255);
		retuMsg.append("【").append(entity.getItemName()).append("】强化");
		retuMsg.append(uplv + "次，");
		retuMsg.append("基础属性得到提升。");
		msg.setSucc(true);
		msg.setDesc(retuMsg.toString());

		// 强化装备N次，累计值
		missionService.notifyMissionModule(user, Mission.QCT_TYPE_EQUIPLVTIMES,
				0, num);
		return msg;
	}

	private void equipLvUpTLog(long userId, long treId, int itemId,
			int consumeItemId1, int num1, int consumeItemId2, int num2,
			int consumeMoney) {
		User user = userService.getUserById(userId);
		logService.log(LogBeanFactory.buildEquipLvUpLog(user, treId, itemId,
				consumeItemId1, num1, consumeItemId2, num2, consumeMoney));

	}

	/**
	 * 
	 * @param treasury
	 * @param heroId
	 */
	public void updateHeroEffectByEquipPropChanged(Treasury treasury,
			long troopGridId) {
		if (!treasury.isEquipToHero()) {
			return;
		}
		heroService.lockHero(treasury.getUserId());
		TroopGrid tg = troopGridService.getTroopGrid(treasury.getUserId(),
				troopGridId);
		Hero hero = heroService.getHeroByHeroId(treasury.getUserId(),
				tg.getHeroId());
		if (!hero.idle()) {
			return;
		}
		// 后台推刷新单个武将的事件
		heroService.sendFlushHeroEvent(hero);
	}

	@Override
	public void doEquipCompose(long userId, int entityId) {
		treasuryService.lockTreasury(userId);
		Entity equip = entityService.getEntity(entityId);
		if (equip == null) {
			throw new BaseException("道具定义不存在");
		}

		if (!(equip instanceof Equip)) {
			throw new BaseException("此道具不能合成");
		}
		Equip ep = (Equip) equip;
		if (!ep.isFragmentEquip()) {
			throw new BaseException("此道具不能合成装备");
		}
		// boolean enough = true;
		// for (Entry<Integer, Integer> en : ep.getFragmentItems().entrySet()) {
		// int ct = treasuryService.getTreasuryCountByEntId(userId,
		// en.getKey());
		// if (ct < en.getValue()) {
		// enough = false;
		// break;
		// }
		// }
		// if (!enough) {
		// throw new BaseException("合成消耗的道具不够");
		// }
		// 判断背包空间是否够
		if (treasuryService.hasFull(userId, 1)) {
			throw new BaseException("背包已经满,不能合成装备");
		}

		int item1 = 0;
		int num1 = 0;
		int item2 = 0;
		int num2 = 0;
		int item3 = 0;
		int num3 = 0;

		int index = 0;
		for (Entry<Integer, Integer> en : ep.getFragmentItems().entrySet()) {
			treasuryService.deleteItemFromTreasury(userId, en.getKey(),
					en.getValue(), true, LogItemAct.LOGITEMACT_9);

			switch (index) {
			case 0:
				item1 = en.getKey();
				num1 = en.getValue();
				break;
			case 1:
				item2 = en.getKey();
				num2 = en.getValue();
				break;
			case 2:
				item3 = en.getKey();
				num3 = en.getValue();
				break;
			default:
				break;
			}
			index++;
		}

		treasuryService.addItemToTreasury(userId, entityId, 1, 0, 1, false,
				true, LogItemAct.LOGITEMACT_10);
		User user = userService.getUserById(userId);
		logService.log(LogBeanFactory.buildEquipComposeLog(user, item1, num1,
				item2, num2, item3, num3, entityId, equip.getEntityName(), 0,
				0, 0));
	}

	/**
	 * 背包装备加锁
	 */
	@Override
	public void doEquipLock(long userId, long treasuryId, boolean isLock) {
		treasuryService.lockTreasury(userId);
		Treasury treasury = treasuryService.getTreasuryById(userId, treasuryId);
		if (treasury == null) {
			throw new BaseException("没有可以加锁的装备");
		}

		if (!treasury.isEquip()) {
			throw new BaseException("不是装备不能加锁");
		}
		if (treasury.isLock() == isLock) {
			String s = treasury.isLock() ? "已锁" : "未锁";
			throw new BaseException("装备" + s);
		}
		treasury.setLock(isLock);
		treasuryService.updateTreasury(treasury);
		// 发刷新道具的事件-entId
		treasuryService.sendFlushTreasuryEvent(treasury);
	}

	/**
	 * 装备回炉 <br />
	 */
	@Override
	public void doEquipRebirth(long userId, List<Long> treasuryIds) {
		Map<Integer, Integer> userdReturnItemMap = new HashMap<Integer, Integer>();
		treasuryService.lockTreasury(userId);

		User user = userService.getUserById(userId);
		// int lv = commonService.getSysParaIntValue(
		// AppConstants.HERO_EQUIP_EQUIP_REBIRTH,
		// AppConstants.SYS_OPNE_STATUS_DEFAULTVALUE);
		// if (user.getUsrLv() < lv) {
		// throw new BaseException("装备回炉功能未开放");
		// }
		// 需要返还的金矿
		int addMoney = 0;
		List<Treasury> treasurys = new ArrayList<Treasury>(treasuryIds.size());
		for (long treasuryId : treasuryIds) {
			Map<Integer, Integer> cmap = new HashMap<Integer, Integer>();
			Treasury treasury = treasuryService.getTreasuryById(userId,
					treasuryId);
			if (treasury == null || treasury.getUserId() != userId)
				throw new BaseException("无此装备");
			int maxLv = treasury.getLevel();
			if (maxLv == 0) {
				throw new BaseException("装备未强化，不能回炉");
			}
			if (treasury.isEquipToHero()) {
				throw new BaseException("装备已使用，不能回炉");
			}
			int equipEntId = treasury.getEntId();
			Entity tmp = entityService.getEntity(equipEntId);
			if (!(tmp instanceof Equip)) {
				throw new BaseException("不是装备，不能回炉");
			}
			Equip equip = (Equip) tmp;
			// 品质来 回炉一次消耗多少元宝
			int yb = commonService.getSysParaIntValue(
					AppConstants.HERO_EQUIP_EQUIP_REBIRTH_COST
							+ equip.getColor(), 100);
			if (user.getCash() < yb) {
				throw new BaseException("您的钻石不足,是否充值？");
			}

			userService.doConsumeCash(user, yb, LogCashAct.EQUIP_REBIRTH);
			// 装备回炉前的rule
			EquipLevelUpRule rule = equipService.getEquipLevelUpRule(
					equip.getColor(), treasury.getLevel());
			if (rule == null) {
				throw new BaseException("装备属性不正确，没有找到回炉规则");
			}
			// 计算累计消耗金矿
			addMoney += rule.getDestroyGoldNum();
			if (addMoney <= 0) {
				throw new BaseException("装备回收数组越界");
			}
			if ((rule.getDestroyEntId() > 0) && rule.getDestroyEntNum() > 0) {
				putDestoryItemToMap(cmap, rule.getDestroyEntId(),
						rule.getDestroyEntNum());
			}
			if ((rule.getDestroyEntId2() > 0) && rule.getDestroyEntNum2() > 0) {
				putDestoryItemToMap(cmap, rule.getDestroyEntId2(),
						rule.getDestroyEntNum2());
			}

			for (Entry<Integer, Integer> en : cmap.entrySet()) {
				Integer v = userdReturnItemMap.get(en.getKey());
				if (v == null) {
					v = 0;
				}
				userdReturnItemMap.put(en.getKey(), v + en.getValue());
			}
			// 增加道具到背包
			treasurys.add(treasury);

			logService.log(LogBeanFactory.buildEquipRebirthLog(user,
					equip.getEntId(), equip.getEntityName(),
					rule.getDestroyGoldNum(), cmap));
		}
		// TODO 更新金矿消耗
		if (addMoney != 0) {
			castleResService.doAddRes(user.getMainCastleId(),
					AppConstants.ENT_RES_GOLD, addMoney, true);
		}
		for (Treasury t : treasurys) {
			t.setLevel(0);
			treasuryService.updateTreasury(t);
			treasuryService.sendFlushTreasuryEvent(t);
		}
	}

	private void putDestoryItemToMap(Map<Integer, Integer> generatedItemMaps,
			int generatedItemId, int generatedItemNum) {
		if (generatedItemNum > 0) {
			Integer oldNum = generatedItemMaps.get(generatedItemId);
			if (oldNum == null) {
				generatedItemMaps.put(generatedItemId, generatedItemNum);
			} else {
				generatedItemMaps.put(generatedItemId, oldNum
						+ generatedItemNum);
			}
		}
	}

	/**
	 * 装备分解 <br />
	 */
	@Override
	public void doEquipDestroy(long userId, List<Long> treasuryIds) {
		treasuryService.lockTreasury(userId);
		User user = userService.getUserById(userId);
		// int lv = commonService.getSysParaIntValue(
		// AppConstants.HERO_EQUIP_EQUIP_DESTROY,
		// AppConstants.SYS_OPNE_STATUS_DEFAULTVALUE);
		// if (user.getUsrLv() < lv) {
		// throw new BaseException("装备分解功能未开放");
		// }
		// 返还的道具集合
		Map<Integer, Integer> generatedItemMaps = new HashMap<Integer, Integer>();
		int addMoney = 0;
		for (long treasuryId : treasuryIds) {
			Treasury treasury = treasuryService.getTreasuryById(userId,
					treasuryId);
			if (treasury == null || treasury.getUserId() != userId) {
				throw new BaseException("装备错误");
			}
			if (treasury.isEquipToHero()) {
				throw new BaseException("装备正在使用，不能分解");
			}
			if (treasury.isLock()) {
				throw new BaseException("装备已加锁，不能分解");
			}
			int equipEntId = treasury.getEntId();
			Entity tmp = entityService.getEntity(equipEntId);
			if (!(tmp instanceof Equip)) {
				throw new BaseException("不是装备，不能分解");
			}
			Equip equip = (Equip) tmp;
			// 物品本身分解道具
			if (equip.getEquipDebris() > 0) {
				Integer count = generatedItemMaps.get(equip.getEquipDebris());
				if (count == null) {
					count = 0;
				}
				generatedItemMaps.put(equip.getEquipDebris(),
						count + equip.getEquipDebrisCount());
			}

			// if (equip.isBuildEquip() && treasury.isEquipBuild()) {// 如果是打造装备
			// // 退还打造消耗的东西 返还通用道具和金币，对应属性道具不返还。
			// castleResService.doAddRes(user.getMainCastleId(),
			// AppConstants.ENT_RES_GOLD, equip.getGold(), true);
			// // 退还物品
			// Map<Integer, Integer> mp = equip.getBuildItems();
			// if (mp != null && !mp.isEmpty()) {
			// for (Entry<Integer, Integer> en : mp.entrySet()) {
			// Entity e = entityService.getEntity(en.getKey());
			// // 打造消耗的图纸不返还
			// Item it = (Item) e;
			// if (it.getItemType().isEquipCard()) {
			// continue;
			// }
			// treasuryService.doAddItemToTreasury(userId,
			// en.getKey(), en.getValue());
			// }
			// }
			//
			// }

			if (treasury.getLevel() <= 0) {// 没有强化过直接分解
				addMoney += equip.getGoldNum();
				treasuryService.deleteTreasury(treasury.getUserId(),
						treasury.getId(), treasury.getItemCount(),
						LogItemAct.LOGITEMACT_11);
				continue;
			}
			// 装备回炉前的rule
			EquipLevelUpRule rule = equipService.getEquipLevelUpRule(
					equip.getColor(), treasury.getLevel());
			if (rule == null) {
				throw new BaseException("装备属性不正确，没有找到回炉规则");
			}
			// 计算累计消耗金矿//分解自己获得金矿
			addMoney += (rule.getDestroyGoldNum() + equip.getGoldNum());
			if (addMoney <= 0) {
				throw new BaseException("装备回收数组越界");
			}

			if ((rule.getDestroyEntId() > 0) && rule.getDestroyEntNum() > 0) {
				putDestoryItemToMap(generatedItemMaps, rule.getDestroyEntId(),
						rule.getDestroyEntNum());
			}
			if ((rule.getDestroyEntId2() > 0) && rule.getDestroyEntNum2() > 0) {
				putDestoryItemToMap(generatedItemMaps, rule.getDestroyEntId2(),
						rule.getDestroyEntNum2());
			}
			treasuryService.deleteTreasury(treasury.getUserId(),
					treasury.getId(), treasury.getItemCount(),
					LogItemAct.LOGITEMACT_11);
			this.equipDestroyTLog(userId, treasuryId, treasury.getEntId(),
					treasury.getLevel(), rule.getDestroyEntId(),
					rule.getDestroyEntNum(), rule.getDestroyEntId2(),
					rule.getDestroyEntNum2(), rule.getDestroyGoldNum());
		}
		if (addMoney != 0) {
			castleResService.doAddRes(user.getMainCastleId(),
					AppConstants.ENT_RES_GOLD, addMoney, true);
		}

		// 增加碎片
		for (Entry<Integer, Integer> en : generatedItemMaps.entrySet()) {
			Entity e = entityService.getEntity(en.getKey());
			if (!(e instanceof Item)) {
				throw new BaseException("装备分解的物品错误");
			}
			treasuryService.addItemToTreasury(userId, en.getKey(),
					en.getValue(), 1, 0, true, true, LogItemAct.LOGITEMACT_12);
		}
	}

	private void equipDestroyTLog(long userId, long treId, int itemId,
			int equipLevel, int returnItemId1, int num1, int returnItemId2,
			int num2, int returnMoney) {
		User user = userService.getUserById(userId);
		logService.log(LogBeanFactory.buildEquipDestroyLog(user, treId, itemId,
				equipLevel, returnItemId1, num1, returnItemId2, num2,
				returnMoney));
	}

	@Override
	public Treasury doEquipBuild(long userId, int entId) {
		treasuryService.lockTreasury(userId);
		Entity ent = entityService.getEntity(entId);
		if (ent == null) {
			throw new BaseException("物品不存在");
		}
		if (!(ent instanceof Equip)) {
			throw new BaseException("物品不是装备");
		}

		Equip eq = (Equip) ent;
		if (!eq.isBuildEquip()) {
			throw new BaseException("不是打造装备类型");
		}
		// 判断是否已经打造了
		// if (ty.isEquipBuild()) {
		// throw new BaseException("装备已经打造过了");
		// }
		//
		// if (!ty.isEquipToHero()) {
		// throw new BaseException("装备未装备在武将上");
		// }

		int num = 1;
		// 判断背包空间是否够
		if (treasuryService.hasFull(userId, num)) {
			throw new BaseException("背包已经满,不能打造装备");
		}

		User u = userService.getUserById(userId);
		for (Entry<Integer, Integer> en : eq.getBuildItems().entrySet()) {
			Entity et = entityService.getEntity(en.getKey());
			boolean cast = false;
			if (et instanceof Item) {
				Item i = (Item) et;
				if (i.getItemType().isEquipCard()) {
					cast = true;
					treasuryService.addHeroEquipCards(userId, i,
							-en.getValue(), LogItemAct.LOGITEMACT_19);
				}
			}

			// if (et instanceof Resource) {
			// castleResService.doDelRes(u.getMainCastleId(), en.getKey(),
			// en.getValue(), true, true);
			// cast = true;
			// }

			if (cast) {
				continue;
			}
			// 扣除物品
			treasuryService.deleteItemFromTreasury(userId, en.getKey(),
					en.getValue(), true, LogItemAct.LOGITEMACT_17);
		}
		castleResService.doDelRes(u.getMainCastleId(),
				AppConstants.ENT_RES_GOLD, eq.getGold(), true, true);
		// 随机属性 放入背包内部自己来做。保证运营加入物品的时候会主动随机属性
		// 加入背包
		Treasury tr = treasuryService.doCreateEquipItemToTreasury(userId, eq,
				num, LogItemAct.LOGITEMACT_66);
		// treasuryService.addItemToTreasury(userId, entId, num, 1, 0, true,
		// true,
		// LogItemAct.LOGITEMACT_66);

		// 生产装备N次，累计值
		missionService
				.notifyMissionModule(u, Mission.QCT_TYPE_EQUIPBUILD, 0, 1);
		return tr;
	}

	@Override
	public Map<String, Object> equipCardAndDebris(long userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		HeroEquipDebris ed = treasuryService.getEquipDebris(userId);
		map.put("cards", ed.getCardsMap());
		map.put("debris", ed.getDebrisMap());
		return map;
	}

	@Override
	public void doSellItem(long userId, Map<Integer, Integer> items) {
		User user = userService.getUserById(userId);
		treasuryService.lockTreasury(userId);
		castleResService.lockCasRes(user.getMainCastleId());
		HeroEquipDebris ed = treasuryService.getEquipDebris(userId);
		Map<Integer, Integer> map = ed.getCardsMap();

		Map<Item, Integer> its = new HashMap<Item, Integer>();

		for (Entry<Integer, Integer> en : items.entrySet()) {
			if (en.getValue() <= 0) {
				throw new BaseException("卖出数量必须大于0");
			}

			Integer val = map.get(en.getKey());
			if (val == null || val < en.getValue()) {
				throw new BaseException("没有可用出售的物品" + en.getKey());
			}
			Entity et = entityService.getEntity(en.getKey());
			if (et == null) {
				throw new BaseException("找不到物品配置" + en.getKey());
			}
			Item item = (Item) et;

			if (item.getSellPrice() <= 0) {
				throw new BaseException("该道具不能出售");
			}

			if (!item.getItemType().isEquipCard()
					&& !item.getItemType().isEquipDebris()) {
				throw new BaseException("不是装备图纸或碎片");
			}

			SellType type = SellType.valueOf(item.getSellType());
			if (type == null) {
				throw new BaseException("出售的货币类型错误" + item.getEntId() + ","
						+ item.getSellType());
			}

			its.put(item, en.getValue());
		}

		for (Entry<Item, Integer> en : its.entrySet()) {
			SellType type = SellType.valueOf(en.getKey().getSellType());
			type.sellItem(user, en.getKey(), en.getValue(), castleResService,
					treasuryService);
		}
	}

	public void setHeroService(IHeroService heroService) {
		this.heroService = heroService;
	}

	public void setTreasuryService(ITreasuryService treasuryService) {
		this.treasuryService = treasuryService;
	}

	public void setEntityService(IEntityService entityService) {
		this.entityService = entityService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public void setEquipService(IEquipService equipService) {
		this.equipService = equipService;
	}

	public void setLogService(ILogService logService) {
		this.logService = logService;
	}

	public void setTroopGridService(ITroopGridService troopGridService) {
		this.troopGridService = troopGridService;
	}

	public void setCommonService(ICommonService commonService) {
		this.commonService = commonService;
	}

}

package com.youxigu.dynasty2.entity.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.youxigu.dynasty2.entity.domain.Equip;
import com.youxigu.dynasty2.entity.domain.EquipLevelUpRule;
import com.youxigu.dynasty2.entity.service.IEntityService;
import com.youxigu.dynasty2.entity.service.IEquipService;
import com.youxigu.dynasty2.hero.domain.HeroFate;
import com.youxigu.dynasty2.util.BaseException;

public class EquipEntityService extends AbstractEntityFactoryService<Equip>
		implements IEquipService {
	/** 装备强化规则 */
	private Map<Integer, EquipLevelUpRule> levelUpRules = null;
	private Map<Integer, Equip> entityCache = null;
	private Map<String, HeroFate> equipFateMaps = null;
	/** 每件装备对应那些套装 key装备id,val 套装集合 */
	private Map<Integer, List<HeroFate>> equipFates;

	public EquipEntityService() {
		super();
	}

	public void initService() {
		log.info("初始化载入装备强化/分解/洗练/回炉规则");
		if (entityCache != null) {
			return;
		}

		List<HeroFate> fates = entityDao
				.listHeroFatesByType(HeroFate.EQUIP_FATE);
		if (fates != null) {
			equipFateMaps = new HashMap<String, HeroFate>(fates.size());
			equipFates = new HashMap<Integer, List<HeroFate>>(fates.size());
			for (HeroFate fate : fates) {
				if (fate.getType() != HeroFate.EQUIP_FATE) {
					// 这里只加载装备情缘
					continue;
				}
				fate.parse();
				equipFateMaps.put(String.valueOf(fate.getFateId()), fate);
				for (int vl : fate.getReqEntIds()) {
					List<HeroFate> ls = equipFates.get(vl);
					if (ls == null) {
						ls = new ArrayList<HeroFate>();
						equipFates.put(vl, ls);
					}
					ls.add(fate);
				}
			}
		}

		entityCache = new ConcurrentHashMap<Integer, Equip>();
		List<Equip> list = entityDao.listEquips();
		if (list != null && list.size() > 0) {
			for (Equip entity : list) {
				entity.check();
				entityCache.put(entity.getEntId(), entity);
				entity.setEquipFates(equipFates.get(entity.getEntId()));
			}
		}

		this.levelUpRules = new HashMap<Integer, EquipLevelUpRule>();
		List<EquipLevelUpRule> levelUpRules = entityDao.getEquipLevelUpRules();
		// 校验强化数据是否正确
		Map<Integer, List<EquipLevelUpRule>> rs = new HashMap<Integer, List<EquipLevelUpRule>>();
		for (EquipLevelUpRule l : levelUpRules) {
			List<EquipLevelUpRule> ls = rs.get(l.getEquipColor());
			if (ls == null) {
				ls = new ArrayList<EquipLevelUpRule>();
				rs.put(l.getEquipColor(), ls);
			}
			ls.add(l);
			if (this.levelUpRules.containsKey(l.getId())) {
				throw new BaseException("装备强化数据异常.配置了相同的强化数据" + l.getId());
			}
			this.levelUpRules.put(l.getId(), l);
		}
		for (List<EquipLevelUpRule> ls : rs.values()) {
			Collections.sort(ls);
			// 计算数据的合法性
			EquipLevelUpRule pre = null;
			for (EquipLevelUpRule l : ls) {
				if (pre == null) {
					if ((l.getEntId() != l.getDestroyEntId() || l.getEntNum() != l
							.getDestroyEntNum())
							|| (l.getEntId2() != l.getDestroyEntId2() || l
									.getEntNum2() != l.getDestroyEntNum2())) {
						throw new BaseException("装备强化数据配置异常id=" + l.getId());
					}
					pre = l;
					continue;
				}
				int num1 = pre.getDestroyEntNum() + l.getEntNum();
				int num2 = pre.getDestroyEntNum2() + l.getEntNum2();
				int gold = pre.getDestroyGoldNum() + l.getLvUpGoldNum();
				if ((l.getEntId() != l.getDestroyEntId() || num1 != l
						.getDestroyEntNum())
						|| (l.getEntId2() != l.getDestroyEntId2() || num2 != l
								.getDestroyEntNum2())
						|| gold != l.getDestroyGoldNum()) {
					throw new BaseException("装备强化数据配置异常id=" + pre.getId()
							+ ",id2=" + l.getId());
				}
				pre = l;
			}
		}
	}

	@Override
	public Equip loadEntity(int entityId, String entityType,
			Map<String, Object> context) {
		Equip equip = entityCache.get(entityId);
		if (equip == null) {
			equip = entityDao.getEquipByEntId(entityId);
			if (equip != null) {
				entityCache.put(entityId, equip);
			}
		}
		if (equip != null) {
			equip.setEntType(entityType);
			equip.init();
		}
		return equip;
	}

	@Override
	public void createEntity(Equip entity, IEntityService entityService) {
		entityDao.createEquip(entity);
	}

	@Override
	public EquipLevelUpRule getEquipLevelUpRule(int color, int level) {
		int id = EquipLevelUpRule.getEquipId(color, level);
		return this.levelUpRules.get(id);
		// for (EquipLevelUpRule rule : levelUpRules) {
		// if (rule.getEquipColor() == color && rule.getLevel() == level)
		// return rule;
		// }
		// return null;
	}

	@Override
	public HeroFate getHeroFate(int fateId) {
		return equipFateMaps.get(String.valueOf(fateId));
	}

	@Override
	public HeroFate getEquipFateByProcessId(String fateId) {
		int f = Integer.valueOf(fateId);
		int id = HeroFate.resoveHeroFateId(f);
		return getHeroFate(id);
	}
}

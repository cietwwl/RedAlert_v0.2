package com.youxigu.dynasty2.entity.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.youxigu.dynasty2.combat.skill.CombatSkillEffect;
import com.youxigu.dynasty2.entity.domain.HeroSkill;
import com.youxigu.dynasty2.entity.domain.HeroSkillEffect;
import com.youxigu.dynasty2.entity.domain.HeroSkillEffectLimit;
import com.youxigu.dynasty2.entity.domain.HeroSkillLimit;
import com.youxigu.dynasty2.entity.service.IEntityService;

/**
 * 技能实体处理类
 * 
 * @author Dagangzi
 *
 */
public class HeroSkillEntityService extends AbstractEntityFactoryService<HeroSkill> {
	private Map<Integer, HeroSkill> entityCache = null;
	private Map<Integer, List<HeroSkillLimit>> skillLimitCache = null;
	private Map<Integer, List<HeroSkillEffectLimit>> skillEffLimitCache = null;
	private Map<Integer, List<HeroSkillEffect>> skillEffCache = null;
	private static AttackComparator priorityComparator = new AttackComparator();// 效果优先级

	/**
	 * 战力从高到低排序
	 * 
	 * @author Dagangzi
	 */
	static class AttackComparator implements Comparator<HeroSkillEffect> {
		@Override
		public int compare(HeroSkillEffect eff1, HeroSkillEffect eff2) {
			return eff2.getPriority() - eff1.getPriority();
		}
	}

	public void initService() {
		log.info("初始化武将技能");
		if (entityCache != null) {
			return;
		}
		entityCache = new ConcurrentHashMap<Integer, HeroSkill>();
		List<HeroSkill> list = entityDao.listHeroSkills();
		if (list != null && list.size() > 0) {
			for (HeroSkill entity : list) {
				entityCache.put(entity.getEntId(), entity);
			}
		}

		List<HeroSkillLimit> skillLimits = entityDao.listHeroSkillLimits();
		if (skillLimits != null && skillLimits.size() > 0) {
			skillLimitCache = new HashMap<Integer, List<HeroSkillLimit>>();
			for (HeroSkillLimit heroSkillLimit : skillLimits) {
				List<HeroSkillLimit> tmp = skillLimitCache.get(heroSkillLimit.getSkillId());
				if (tmp == null) {
					tmp = new ArrayList<HeroSkillLimit>();
					skillLimitCache.put(heroSkillLimit.getSkillId(), tmp);
				}
				tmp.add(heroSkillLimit);
			}
		}

		List<HeroSkillEffectLimit> skillEffLimits = entityDao.listHeroSkillEffectLimits();
		if (skillEffLimits != null && skillEffLimits.size() > 0) {
			skillEffLimitCache = new HashMap<Integer, List<HeroSkillEffectLimit>>();
			for (HeroSkillEffectLimit effLimit : skillEffLimits) {
				List<HeroSkillEffectLimit> tmp = skillEffLimitCache.get(effLimit.getEffId());
				if (tmp == null) {
					tmp = new ArrayList<HeroSkillEffectLimit>();
					skillEffLimitCache.put(effLimit.getEffId(), tmp);
				}
				tmp.add(effLimit);
			}
		}
		
		List<HeroSkillEffect> effList = entityDao.listHeroSkillEffects();
		if(effList != null && effList.size() >0){
			skillEffCache = new HashMap<Integer, List<HeroSkillEffect>>();
			for(HeroSkillEffect skillEff : effList){
				List<HeroSkillEffect> tmp= skillEffCache.get(skillEff.getSkillId());
				if (tmp == null) {
					tmp = new ArrayList<HeroSkillEffect>();
					skillEffCache.put(skillEff.getSkillId(), tmp);
				}
				tmp.add(skillEff);
			}
		}
	}

	@Override
	public HeroSkill loadEntity(int entityId, String entityType, Map<String, Object> context) {
		HeroSkill entity = entityCache.get(entityId);
		if (entity == null) {
			entity = entityDao.getHeroSkillByEntId(entityId);
		}
		if (entity != null) {
			entity.setEntType(entityType);
//			List<HeroSkillEffect> skillEffects = entityDao.getHeroSkillEffectsBySkillId(entityId);
			List<HeroSkillEffect> skillEffects = skillEffCache.get(entityId);
			if (skillEffects != null && skillEffects.size() > 0) {
				for (HeroSkillEffect heroSkillEffect : skillEffects) {
					// List<HeroSkillEffectLimit> skillEffectLimits = entityDao
					// .getHeroSkillEffectLimitByEffId(heroSkillEffect.getEffId());

					List<HeroSkillEffectLimit> skillEffectLimits = skillEffLimitCache.get(heroSkillEffect.getEffId());

					if (skillEffectLimits != null && skillEffectLimits.size() > 0) {
						heroSkillEffect.setSkillEffectLimits(skillEffectLimits);
					}

					// 初始化效果制定的syshero
					if (heroSkillEffect.getEffKey()
							.equals(CombatSkillEffect.EFF_TOGETHER_ATTACK)
							&& heroSkillEffect.getEntIds() == null) {
						String para2 = heroSkillEffect.getPara2();
						if (para2 != null && !para2.equals("")) {
							String[] strEntIds = para2.split(",");
							if (strEntIds != null && strEntIds.length > 0) {
								int[] entIds = new int[strEntIds.length];
								heroSkillEffect.setEntIds(entIds);
								int i = 0;
								for (String str : strEntIds) {
									int entId = Integer.parseInt(str);
									entIds[i] = entId;
									i = i + 1;
								}
							}
						}
					}
				}

				// 按效果优先级排序
				Collections.sort(skillEffects, priorityComparator);

				entity.setSkillEffects(skillEffects);
			}

			// List<HeroSkillLimit> skillLimits =
			// entityDao.getHeroSkillLimitBySkillId(entityId);
			List<HeroSkillLimit> skillLimits = skillLimitCache.get(entityId);
			if (skillLimits != null && skillLimits.size() > 0) {
				entity.setSkillLimits(skillLimits);
			}

		}
		return entity;
	}

	@Override
	public void afterLoad(HeroSkill entity, IEntityService entityService) {
		int nextSkillId = entity.getNextSkillId();
		if (nextSkillId > 0) {
			HeroSkill next = (HeroSkill) entityService.getEntity(nextSkillId);
			if (next == null) {
				log.error("武将技能的下级技能配置错误:{},{}", entity.getEntId(), nextSkillId);
			}
			entity.setNext(next);
			next.setPrev(entity);
		}
	}

}

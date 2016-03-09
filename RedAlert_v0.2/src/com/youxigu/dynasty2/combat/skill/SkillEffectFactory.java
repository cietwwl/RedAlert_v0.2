package com.youxigu.dynasty2.combat.skill;

import java.util.Map;

import com.youxigu.dynasty2.entity.domain.HeroSkillEffect;
import com.youxigu.dynasty2.entity.domain.HeroSkillEffectLimit;

/**
 * 效果渲染器的工厂类 加上了技能释放目标的工厂 加上了技能释放约束的工厂
 * @author Dagangzi
 *
 */
public class SkillEffectFactory {
	/**
	 * key = skilleffect id 或者skilleffect type
	 */
	private static Map<String, ISkillEffectRender> renders;

	/**
	 * key = skill id ,或者是目标类型 对某个Effect的施放对象的搜索器
	 */
	private static Map<Integer, ITargetSearcher> searchers;

	/**
	 * 技能释放约束 key = 发动条件的key值
	 */
	private static Map<String, ISkillFiredLimitValidator> limits;
	
	/**
	 * 效果约束
	 */
	private static Map<String, ISkillEffectFiredLimitValidator> effectlimits;

	public void setSearchers(Map<Integer, ITargetSearcher> searchers) {
		SkillEffectFactory.searchers = searchers;
	}

	public void setRenders(Map<String, ISkillEffectRender> renders) {
		SkillEffectFactory.renders = renders;
	}

	public void setLimits(Map<String, ISkillFiredLimitValidator> limits) {
		SkillEffectFactory.limits = limits;
	}

	public void setEffectlimits(Map<String, ISkillEffectFiredLimitValidator> effectlimits) {
		SkillEffectFactory.effectlimits = effectlimits;
	}

	public static ITargetSearcher getTargetSearcher(HeroSkillEffect effect) {
		return searchers.get((int)effect.getTarget());
	}
	public static ITargetSearcher getTargetSearcher(int target) {
		return searchers.get(target);
	}

	public static ITargetSearcher getOwnerSearcher(HeroSkillEffect effect) {
		return searchers.get((int) effect.getOwner());
	}

	public static ITargetSearcher getOwnerSearcher(int target) {
		return searchers.get(target);
	}

	public static ISkillEffectRender getSkillEffectRender(HeroSkillEffect effect) {
		ISkillEffectRender render = renders.get(effect.getEffKey());
		if (render == null) {
			render = renders.get(effect.getEffTypeStr());
		}
		return render;
	}

	public static ISkillFiredLimitValidator getSkillFiredLimitValidator(String limitType) {
		return limits.get(limitType);
	}
	
	public static ISkillEffectFiredLimitValidator getSkillEffectFiredLimitValidator(HeroSkillEffectLimit limit) {
		return effectlimits.get(limit.getLimitType());
	}
}

package com.youxigu.dynasty2.entity.service.script;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.youxigu.dynasty2.entity.domain.Entity;
import com.youxigu.dynasty2.entity.domain.Item;
import com.youxigu.dynasty2.entity.domain.effect.EffectDefine;
import com.youxigu.dynasty2.entity.service.IEffectRender;
import com.youxigu.dynasty2.entity.service.IEntityService;
import com.youxigu.dynasty2.hero.domain.Hero;
import com.youxigu.dynasty2.hero.service.IHeroService;
import com.youxigu.dynasty2.log.ILogService;
import com.youxigu.dynasty2.log.LogBeanFactory;
import com.youxigu.dynasty2.log.imp.LogItemAct;
import com.youxigu.dynasty2.treasury.domain.Treasury;
import com.youxigu.dynasty2.treasury.service.ITreasuryService;
import com.youxigu.dynasty2.user.domain.LvParaLimit;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.user.service.IUserService;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.dynasty2.util.MathUtils;

/**
 * 使用武将经验卡 加武将经验
 * 
 * @author fengfeng
 *
 */
public class HeroExpRender implements IEffectRender {
	private static final Logger log = LoggerFactory
			.getLogger(HeroExpRender.class);
	private IUserService userService;
	private ITreasuryService treasuryService;
	private IHeroService heroService;
	private IEntityService entityService = null;
	private ILogService logService;
	
	@Override
	public Map<String, Object> render(Entity entity, EffectDefine effectDefine,
			Map<String, Object> context) {
		if (!context.containsKey("user"))
			throw new BaseException("参数不对");
		User usr = (User) context.get("user");

		int num = 1;
		if (context.containsKey("num")) {
			num = MathUtils.getInt(context.get("num"));
			if (num <= 1) {
				num = 1;
			}
		}
		Object tmp = context.get("entId");
		if (tmp == null)
			throw new BaseException("使用的物品id不存在");
		int entId = MathUtils.getInt(tmp);

		if (!context.containsKey("heroId")) {
			throw new BaseException("参数错误");
		}
		long heroId = MathUtils.getLong(context.get("heroId"));
		Hero hero = heroService.lockAndGetHero(usr.getUserId(), heroId);
		if (hero == null) {
			throw new BaseException("武将不存在");
		}
		if (!hero.isInTroop()) {
			throw new BaseException("不在军团里面,不能吃经验道具");
		}
		if (!hero.idle()) {
			throw new BaseException("武将必须是空闲状态");
		}
		if (hero.isCommander()) {
			throw new BaseException("君主坦克不能升级");
		}

		Entity en = entityService.getEntity(entId);
		if (en == null || !(en instanceof Item)) {
			throw new BaseException("经验道具物品不存在");
		}

		if (!((Item) en).getItemType().isExpItem()) {
			throw new BaseException("使用的不是经验道具");
		}

		List<Treasury> trs = treasuryService.getTreasurysByEntId(
				usr.getUserId(), entId);
		int size = 0;
		for (Treasury t : trs) {
			size += t.getItemCount();
		}

		if (num > size) {
			throw new BaseException("使用的物品数量不够");
		}
		int perNum = effectDefine.getPara2();
		if (perNum <= 0) {
			throw new BaseException("效果配置错误,加的经验不能小于1");
		}
		LvParaLimit lvp = userService.getLvParaLimit(usr.getUsrLv());
		if (lvp != null) {
			int maxLv = lvp.getHeroLvLimit();
			if (hero.getLevel() >= maxLv) {
				throw new BaseException("武将等级不可超过君主等级，请提升君主等级。");
			}
		}

		int exp = perNum * num;
		// 删除背包物品
		treasuryService.deleteItemFromTreasury(usr.getUserId(), entId, num,
				false, LogItemAct.LOGITEMACT_2);
		int oldlevel = hero.getLevel();// 原先的等级
		int oldExp = hero.getExp();
		int newExp = oldExp + exp;
		if (newExp > IHeroService.MAX_EXP) {
			newExp = IHeroService.MAX_EXP;
		}
		hero.setExp(newExp);
		hero = heroService.doLevelUpHero(hero, usr);
		heroService.updateHero(hero, true);
		// 发提示消息
		StringBuilder sb = new StringBuilder(64);
		sb.append(hero.getHrefStr()).append("得到经验").append(exp);
		if (oldlevel != hero.getLevel()) {
			sb.append("，从").append(oldlevel).append("级升级到")
					.append(hero.getLevel()).append("级。");
		}
		String msg = sb.toString();
		// messageService.sendMessage(0, hero.getUserId(),
		// ChatChannelManager.CHANNEL_SYSTEM, null, msg);
		if (log.isDebugEnabled()) {
			log.debug(msg + "_userId[" + hero.getUserId() + "]_heroId["
					+ hero.getHeroId() + "]");
		}
		logService.log(LogBeanFactory.buildLvHeroLog(usr, entId, oldlevel,
				hero.getLevel(), num, hero.getHeroId(), 0));
		return null;
	}

	public void setLogService(ILogService logService) {
		this.logService = logService;
	}

	public void setTreasuryService(ITreasuryService treasuryService) {
		this.treasuryService = treasuryService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public void setHeroService(IHeroService heroService) {
		this.heroService = heroService;
	}

	public void setEntityService(IEntityService entityService) {
		this.entityService = entityService;
	}

}

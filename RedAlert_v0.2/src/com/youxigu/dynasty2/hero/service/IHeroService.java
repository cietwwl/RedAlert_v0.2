package com.youxigu.dynasty2.hero.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.youxigu.dynasty2.develop.domain.CastleArmy;
import com.youxigu.dynasty2.entity.domain.HeroSkill;
import com.youxigu.dynasty2.entity.domain.Item;
import com.youxigu.dynasty2.entity.domain.SysHero;
import com.youxigu.dynasty2.hero.domain.CommanderColorProperty;
import com.youxigu.dynasty2.hero.domain.Hero;
import com.youxigu.dynasty2.hero.enums.HeroIdle;
import com.youxigu.dynasty2.hero.proto.EffectValueMsg;
import com.youxigu.dynasty2.hero.proto.HeroCardAndDebris;
import com.youxigu.dynasty2.hero.proto.HeroMsg.ExpItem;
import com.youxigu.dynasty2.hero.proto.HeroMsg.HeroArmyInfo;
import com.youxigu.dynasty2.log.imp.LogHeroAct;
import com.youxigu.dynasty2.log.imp.LogItemAct;
import com.youxigu.dynasty2.treasury.domain.DropPackItemInfo;
import com.youxigu.dynasty2.user.domain.User;

/**
 * 武将操作service接口不包括(医馆, 修炼馆, 酒馆)操作
 * 
 * @author Dagangzi
 * 
 */
public interface IHeroService {
	public static final int MAX_EXP = 200000000;// 每次增加经验后的最大经验数

	/**
	 * 锁
	 * 
	 * @param userId
	 * @param heroId
	 * @return
	 */
	Hero lockAndGetHero(long userId, long heroId);

	/**
	 * 加武将经验(需要外部加锁)
	 * 
	 * @param hero
	 * @param exp
	 * @param isUserOpt
	 * @return
	 */
	int doChangeHeroExp(Hero hero, int exp, boolean isUserOpt);

	/**
	 * 加武将经验(需要外部加锁)
	 * 
	 * @param hero
	 * @param exp
	 * @param isUserOpt
	 * @param alreadyAddEff
	 * @return
	 */
	int doChangeHeroExp(Hero hero, int exp, boolean isUserOpt,
			boolean alreadyAddEff);

	/**
	 * 实际获得的经验
	 * 
	 * @param exp
	 * @param userId
	 * @param casId
	 * @param expType
	 *            默认null
	 * @return
	 */
	int getActualExp(int exp, long userId, long casId, String expType);

	/**
	 * 取得一个系统武将
	 * 
	 * @param sysHeroId
	 * @return
	 */
	SysHero getSysHeroById(int sysHeroId);

	/**
	 * 按指定的武将id取武将 必须是武将。。如果是武将碎片是抛异常的
	 * 
	 * @param heroId
	 * @return
	 */
	Hero getHeroByHeroId(long userId, long heroId);

	/**
	 * 获取武将卡对应的信息
	 * 
	 * @param userId
	 * @param heroId
	 * @return
	 */
	Hero getHeroCardByHeroId(long userId, long heroId);

	/**
	 * 取得技能
	 * 
	 * @param heroSkillId
	 * @return
	 */
	public HeroSkill getHeroSkill(int heroSkillId);

	/**
	 * 根据策划配置的系统武将id获取武将
	 * 
	 * @param userId
	 * @param sysHeroId
	 * @return
	 */
	public Hero getHeroBySysHeroId(long userId, int sysHeroId);

	/**
	 * 获取所有可以用的武将 包括上阵的和未上阵
	 * 
	 * @param userId
	 * @return
	 */
	public List<Hero> getUserHeroList(long userId);

	/**
	 * 锁定这个武将的缓存
	 * 
	 * @param hero
	 */
	public void lockHero(long userId);

	/**
	 * 创建武将
	 * 
	 * @param userId
	 * @param sysHeroId
	 * @param isCommander
	 * @param reason
	 * @return
	 */
	public Hero doCreateAHero(long userId, int sysHeroId, boolean isCommander,
			LogHeroAct reason);

	public Hero doCreateAHero(long userId, int sysHeroId, LogHeroAct reason);

	/**
	 * 执行掉落包
	 * 
	 * @param user
	 * @param dropEntId
	 * @param num
	 * @param notUpdate
	 * @param action
	 * @return
	 */
	public List<DropPackItemInfo> dropItem(User user, int dropEntId, int num,
			boolean notUpdate, LogItemAct action);

	/**
	 * 写日志
	 * 
	 * @param user
	 * @param mode
	 * @param sysHero
	 * @param num
	 * @param free
	 */
	public void tlog_hero_hire(User user, int mode, SysHero sysHero, int num,
			boolean free);

	/**
	 * 更新武将
	 * 
	 * @param hero
	 * @param sendMessage
	 *            true表示同时消息通知客户端刷新武将信息
	 */
	public void updateHero(Hero hero, boolean sendMessage);

	/**
	 * 推送刷新武将信息
	 * 
	 * @param hero
	 */
	public void sendFlushHeroEvent(Hero hero);

	/**
	 * 推送刷新武将属性信息
	 * 
	 * @param hero
	 */
	public void sendFlushHeroAttrEvent(Hero hero);

	/**
	 * 更新武将的行动状态。。他是跟随军团状态走了
	 * 
	 * @param hero
	 * @param actionStatus
	 * @param freeDttm
	 * @return
	 */
	public Hero updateHeroActionStatus(Hero hero, HeroIdle actionStatus,
			Timestamp freeDttm);

	/**
	 * 武将强化
	 * 
	 * @param userId
	 * @param heroId
	 * @param num
	 * @return
	 */
	public boolean doHeroStrength(long userId, long heroId);

	/**
	 * 进阶
	 * 
	 * @param userId
	 * @param mainCasId
	 * @param heroId
	 */
	void doRelifeHero(long userId, long mainCasId, long heroId);

	/**
	 * 武将（重生）
	 * 
	 * @param userId
	 * @param heroId
	 * @param retire
	 *            true表示退役 false是重生
	 */
	@Deprecated
	void doHeroRebirth(long userId, List<Long> heroIds, boolean retire);

	/**
	 * 武将（重生） 重构的方法。。。
	 * 
	 * @param userId
	 * @param heroId
	 * @param retire
	 *            true表示退役 false是重生
	 */
	void doHeroRebirth2(long userId, List<Long> heroIds, boolean retire);

	/**
	 * 武将升级使用经验道具之后刷新的方法
	 * 
	 * @param hero
	 * @param user
	 * @return
	 */
	public Hero doLevelUpHero(Hero hero, User user);

	/**
	 * 使用经验道具
	 * 
	 * @param userId
	 * @param heroId
	 * @param entId
	 * @param num
	 */
	void doHeroLevelUp(long userId, long heroId, List<ExpItem> exps);

	/**
	 * 碎片兑换坦克
	 * 
	 * @param userId
	 * @param soulId
	 */
	Map<String, Object> doHeroSoulComposite(long userId, long heroId);

	/**
	 * 分解坦克图纸
	 * 
	 * @param userId
	 * @param cardIds
	 * @param params
	 */
	void doHeroCardDiscard(long userId, List<Long> heroIds);

	/**
	 * 获取所有的坦克图纸和碎片信息
	 * 
	 * @param userId
	 * @return
	 */
	List<HeroCardAndDebris> doHeroCardAndDebris(long userId);

	/**
	 * 增加 图纸-信物
	 * 
	 * @param userId
	 * @param entId
	 * @param num
	 */
	void doAddHeroCardNum(long userId, int entId, int num, LogItemAct reason);

	void doAddHeroCardNum(long userId, Item item, int num, LogItemAct reason);

	void doAddHeroCardNum(long userId, Hero hero, Item item, int num,
			LogItemAct reason);

	/**
	 * 删除 图纸-信物
	 * 
	 * @param userId
	 * @param hero
	 * @param num
	 * @param reason
	 */
	void doDelHeroCardNum(long userId, Hero hero, int num, LogItemAct reason);

	void doDelHeroCardNum(long userId, Item item, int num, LogItemAct reason);

	/**
	 * 增加武将碎片
	 * 
	 * @param userId
	 * @param entId
	 * @param num
	 */
	void doAddHeroSoulNum(long userId, int entId, int num, LogItemAct reason);

	void doAddHeroSoulNum(long userId, Item item, int num, LogItemAct reason);

	void doAddHeroSoulNum(long userId, Hero hero, Item item, int num,
			LogItemAct reason);

	/**
	 * 删除武将碎片
	 * 
	 * @param userId
	 * @param hero
	 * @param num
	 * @param reason
	 */
	void doDelHeroSoulNum(long userId, Hero hero, int num, LogItemAct reason);

	void doDelHeroSoulNum(long userId, Item item, int num, LogItemAct reason);

	/**
	 * 减少累计的坦克图纸数量
	 * 
	 * @param userId
	 * @param hero
	 * @param num
	 * @param reason
	 */
	void doDelSumHeroCardNum(long userId, Hero hero, int num, LogItemAct reason);

	/**
	 * 增加累计的坦克图纸数量
	 * 
	 * @param userId
	 * @param hero
	 * @param num
	 * @param reason
	 */
	void doAddSumHeroCardNum(long userId, Hero hero, int num, LogItemAct reason);

	/**
	 * gm修改武将等级。。测试用
	 * 
	 * @param userId
	 * @param heroId
	 * @param level
	 */
	// void doGmUppdateHeroLevel(long userId, long heroId, int level);

	/**
	 * 武将带的总兵数也就是血量或零件
	 * 
	 * @param userId
	 * @return
	 */
	public int getHeroArmyNum(long userId);

	/**
	 * 坦克维修
	 * 
	 * @return
	 */
	CastleArmy doChangeHeroArmyNum(long userId, long casId,
			List<HeroArmyInfo> armys, int status);
	
	/**
	 * 自动补满武将兵力
	 * 
	 * @param userId
	 * @param heros
	 * @return true 补满，false未补满
	 */
	public boolean doFillUpHeroArmy(User user, Hero[] heros);

	/**
	 * 坦克一键维修
	 * 
	 * @return
	 */
	CastleArmy doAutoChangeHeroArmyNum(long userId, long casId, int status);

	/**
	 * 获取武将的所有属性效果
	 * 
	 * @param userId
	 * @param heroId
	 * @return
	 */
	List<EffectValueMsg> getHeroEffectValue(long userId, long heroId);

	/**
	 * 武将从军团里面下来了,方法内部没有加锁<br>
	 * 1.设置武将套装未null<br>
	 * 2.设置装备套装为null<br>
	 * 3.把玩家身上的坦克连接也卸下来<br>
	 * 
	 * @param hero
	 */
	void doHeroDownTroop(Hero hero);

	/**
	 * 判断是否为君主坦克
	 * 
	 * @param heroId
	 * @return
	 */
	boolean isMainHero(long userId, long heroId);

	/**
	 * 判断是否为君主坦克
	 * 
	 * @param heroId
	 * @return
	 */
	boolean isMainHero(long userId, Hero hero);

	/**
	 * 更新君主坦克信息
	 * 
	 * @param user
	 */
	void doUpdateMainHero(User user);

	/**
	 * 按照指挥官的品质取基础属性
	 * 
	 * @param color
	 * @return
	 */
	CommanderColorProperty getCommanderColorProperty(long userId);
	CommanderColorProperty getCommanderColorProperty(int color);
}
package com.youxigu.dynasty2.hero.service;

import java.sql.Timestamp;
import java.util.List;

import com.youxigu.dynasty2.hero.domain.Troop;
import com.youxigu.dynasty2.hero.domain.TroopGridView;
import com.youxigu.dynasty2.hero.enums.TroopIdle;
import com.youxigu.dynasty2.hero.enums.TroopType;
import com.youxigu.dynasty2.hero.proto.HeroMsg.TroopInfo;
import com.youxigu.dynasty2.hero.proto.HeroUpTroop;

/**
 * 军团service
 * 
 * @author Administrator
 *
 */
public interface ITroopService {

	/** 最多上阵的武将人数 */
	final static int MAX_HERO = 6;

	/**
	 * 更新军团,同时刷新下武将情缘
	 * 
	 * @param troop
	 */
	void doUpdateTroopAndFate(Troop troop);

	/**
	 * 武将上阵，1.处理未上阵的武将上阵 2.已上阵的武将移动到其他的军团里面的空闲格子上面去
	 * 
	 * @param userId
	 * @param troopId
	 * @param heroId
	 * @param troopGridId
	 * @return
	 */
	public HeroUpTroop doUpTroop(long userId, long troopId, long heroId,
			long troopGridId);

	/**
	 * 交换坦克。。必须是两个位置上面都有坦克
	 * 
	 * @param userId
	 * @param troopGridId1
	 * @param troopGridId2
	 * @return
	 */
	public HeroUpTroop doSwapTroopGrid(long userId, long troopId1,
			long troopGridId1, long troopId2, long troopGridId2);

	/**
	 * 同一个军团里面的武将交换格子,一定要检查传入的参数 是不是属于这个军团
	 * 
	 * @param userId
	 * @param troopId1
	 * @param troopGridId1
	 * @param troopGridId2
	 * @return
	 */
	public HeroUpTroop doSwapSameTroopGrid(long userId, long troopId,
			long troopGridId1, long troopGridId2);

	/**
	 * 获取格子里面的信息
	 * 
	 * @param userId
	 * @param troopGridId
	 */
	public TroopGridView getTroopGrid(long userId, long troopGridId);

	/**
	 * 创建军团..主要是新手第一次进入游戏调用
	 * 
	 * @param userId
	 * @param fpos
	 *            6个位置。表示6个格子
	 * @param mainHeroId
	 * @param first
	 *            是否为第一次创建 true表示第一次创建设置为主力军军团
	 * @return
	 */
	Troop doCreateTroop(long userId, long heroId, boolean first);

	/**
	 * 更新军团
	 * 
	 * @param troop
	 */
	void doUpdateTroop(Troop troop);

	/**
	 * 角色升级解锁军团
	 * 
	 * @param userId
	 * @return
	 */
	void doCreateTroopByLevelUp(long userId);

	/**
	 * 校验军团是否空闲，同时锁军团状态
	 * 
	 * @param userId
	 * @param troopId
	 * @param status
	 * @param freeDttm
	 */
	void doCheakIdleTroopStatus(long userId, long troopId, TroopIdle status,
			Timestamp freeDttm);

	/**
	 * 更新军团和武将的状态
	 * 
	 * @param troop
	 * @param status
	 * @param freeDttm
	 *            预计恢复成空闲状态的时间
	 */
	void updateTroopStatus(long userId, long troopId, TroopIdle status,
			Timestamp freeDttm);

	/**
	 * 军团和武将的状态。。军团里面的武将必须保持和军团的状态一致
	 * 需要外部加锁
	 * @param troop
	 * @param status
	 * @param freeDttm
	 */
	void updateTroopStatus(Troop troop, TroopIdle status, Timestamp freeDttm);

	/**
	 * 检查并更新troop状态
	 * 
	 * @param userId
	 */
	List<Troop> doRefreshTroop(long userId);

	/**
	 * 获取用户的所有军团,,此方法获取的坦克是安装indexs 从小到大 排序的(sql里面写了排序规则)
	 * 
	 * @param userId
	 * @return
	 */
	List<Troop> getTroopsByUserId(long userId);

	/**
	 * 或者指定类型军团
	 * 
	 * @param userId
	 * @param type
	 * @return
	 */
	Troop getTroopByType(long userId, TroopType type);

	/**
	 * 获取主力军团
	 * 
	 * @param userId
	 * @return
	 */
	Troop getMainTroop(long userId);

	/**
	 * 获取君主坦克id
	 * 
	 * @param userId
	 * @return
	 */
	long getMainHero(long userId);

	/**
	 * 获取当前用户已经拥有的军团数量
	 * 
	 * @param userId
	 * @return
	 */
	int getTroopsNum(long userId);

	/**
	 * 获取某一个军团
	 * 
	 * @param troopId
	 * @return
	 */
	Troop getTroopById(long userId, long troopId);

	// /**
	// * 随机取一个军团
	// *
	// * @param userId
	// * @param troopId
	// * @return
	// */
	// Troop getTroopRandom(long userId, long troopId);

	// long getRiskTroop(long userId);

	/**
	 * 得到军团战略站战斗力
	 * 
	 * @param troop
	 * @return
	 */
	int getTroopCombatPower(Troop troop);

	int getTroopCombatPower(Troop troop, boolean fullArmy);

	/**
	 * 军团锁
	 * 
	 * @param troop
	 * @return
	 */
	void lockTroop(long userId);

	/**
	 * 给前台的Troop信息
	 */
	TroopInfo getTroopMap(Troop troop, long userId);

	/**
	 * 设置队长
	 * 
	 * @param userId
	 * @param heroId
	 */
	public void doSetTeamLeader(long userId, long troopId, long heroId);

	/**
	 * 自动选取队长
	 */
	public void doAutoSetTeamLeader(long userId, long troopId);

	/**
	 * 保存军团里面的格子
	 * 
	 * @param userId
	 * @param troopId
	 */
	public Troop doSaveTroopGrid(long userId, long troopId,
			List<Long> troopGrids);
}

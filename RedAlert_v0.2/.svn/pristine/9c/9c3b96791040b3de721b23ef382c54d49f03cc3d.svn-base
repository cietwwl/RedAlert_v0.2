package com.youxigu.dynasty2.develop.service;

import com.youxigu.dynasty2.develop.domain.CastleArmy;

/**
 * 维修工厂
 * 
 * @author fengfeng
 *
 */
public interface ICastleArmyService {
	/**
	 * 加锁维修工厂
	 * 
	 * @param casId
	 */
	void lockCastleArmy(long casId);

	/**
	 * 加锁维修工厂返回维修工厂对象
	 * 
	 * @param casId
	 */
	CastleArmy lockGetCastleArmy(long casId);

	/**
	 * 刷新零件生产
	 * 
	 * @param casId
	 * @return
	 */
	CastleArmy doRefreshAndGetCastleArmy(long casId);

	/**
	 * 得到玩家拥有的空闲兵力
	 * 
	 * @param casId
	 * @return
	 */
	CastleArmy getCastleArmy(long casId);

	/**
	 * 招募兵
	 * 
	 * @param casId
	 * @param recruitType
	 *            招募类型
	 * @param num
	 */
	CastleArmy doCreateArmy(long userId, long casId, int num);

	/**
	 * 更新城池兵力：需要外部自己加锁
	 */
	void doUpdateCasArmy(CastleArmy casArmy);

	/**
	 * 生产零件的上限
	 * 
	 * @param casId
	 * @return
	 */
	int getCastleArmyLimit(long casId);

	/**
	 * 高级零件兑换成普通零件
	 * 
	 * @param userId
	 * @param casId
	 * @param num
	 */
	CastleArmy doExchangeItem(long userId, long casId);

	/**
	 * 加速生产零件
	 * 
	 * @param userId
	 * @param casId
	 * @param diamond
	 * @param entId
	 * @param num
	 * @return
	 */
	CastleArmy doSpeedUp(long userId, long casId, boolean diamond, int entId,
			int num);

	/**
	 * 更新剩余的零件数量
	 * 
	 * @param casId
	 * @param num
	 * @return
	 */
	CastleArmy doUpdateCasArmyNum(long casId, int num);
}

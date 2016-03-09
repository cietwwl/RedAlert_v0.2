package com.youxigu.dynasty2.armyout.service;

import java.util.List;

import com.youxigu.dynasty2.armyout.domain.Armyout;
import com.youxigu.dynasty2.armyout.domain.Strategy;
import com.youxigu.dynasty2.armyout.domain.UserArmyout;
import com.youxigu.dynasty2.map.domain.MapCell;
import com.youxigu.dynasty2.user.domain.User;

/**
 * 出征
 * 
 * @author LK
 * @date 2016年2月25日
 */
public interface IArmyoutService {
	/**
	 * 加载出征数据
	 */
	void loadArmyout();

	/**
	 * 取得策略
	 * 
	 * @param outType
	 * @return
	 */
	Strategy getStrategy(int outType);

	/**
	 * 锁出征，相当于锁坐标，不需要重复锁标了
	 * 
	 * @param armyout
	 */
	Armyout lockArmyout(Armyout armyout);

	void lockArmyout(List<Integer> cellIds);

	/**
	 * 出征和坐标绑定
	 * 
	 * @param cellIds
	 * @param armyout
	 */
	void addArmyoutToCell(List<Integer> cellIds, Armyout armyout);

	/**
	 * 出征加入到执行队列
	 * 
	 * @param armyout
	 *            出征数据副本
	 * @param strategy
	 * @param fromMapCell
	 *            出发地坐标副本
	 * @param toMapCell
	 *            目的地坐标副本
	 * @param fromUser
	 * @param toUser
	 */
	void doArmtoutJoinExeQueue(Armyout armyout, Strategy strategy,
			MapCell fromMapCell, MapCell toMapCell, User fromUser);

	/**
	 * 清除坐标下的出征
	 * 
	 * @param cellIds
	 * @param armyout
	 */
	void removeArmyoutFromCell(List<Integer> cellIds, Armyout armyout);

	/**
	 * 取得内部存储的原始Armyout, TODO 仅供特殊情况需要取得原始Armyout时使用，一般不应调用这个方法 -专为事务提交时同步缓存使用
	 * 
	 * @param armyoutId
	 * @return
	 */
	Armyout getArmyoutCache(long armyoutId);

	/**
	 * 添加armyout缓存-专为事务提交时同步缓存使用
	 * 
	 * @param armyout
	 */
	void addArmyoutCache(Armyout armyout);

	/**
	 * 删除armyout缓存-专为事务提交时同步缓存使用
	 * 
	 * @param armyout
	 */
	void removeArmyoutCache(Armyout armyout);

	/**
	 * 取得只读armyout
	 * 
	 * @param armyoutId
	 * @return
	 */
	Armyout getArmyoutForRead(long armyoutId);

	/**
	 * 取得只写armyout
	 * 
	 * @param armyoutId
	 * @return
	 */
	Armyout getArmyoutForWrite(long armyoutId);

	/**
	 * 创建armyout
	 * 
	 * @param armyout
	 */
	void createArmyOut(Armyout armyout);

	void updateArmyout(Armyout armyout);

	void deleteArmyOut(Armyout armyout);

	/**
	 * 取得每日最大的PVP次数
	 * 
	 * @return
	 */
	int getMaxPvpTimes();

	/**
	 * 取得出征队列
	 * 
	 * @param userId
	 * @return
	 */
	UserArmyout lockUserArmyout(long userId);

	/**
	 * 更新出征队列
	 * 
	 * @param userArmyout
	 */
	void updateUserArmyout(UserArmyout userArmyout);

	/**
	 * 校验出征队列
	 * 
	 * @param userId
	 * @param strategy
	 * @return
	 */
	UserArmyout doCheakUserArmyout(long userId, Strategy strategy);

	/**
	 * 加入出征队列
	 * 
	 * @param armyout
	 * @param strategy
	 */
	void doJoinUserArmyout(Armyout armyout, Strategy strategy);

	/**
	 * 加入出征队列
	 * 需要外部加锁
	 * @param userArmyout
	 * @param armyout
	 * @param strategy
	 */
	void doJoinUserArmyout(UserArmyout userArmyout, Armyout armyout,
			Strategy strategy);

	/**
	 * 释放出征队列
	 * 
	 * @param armyout
	 */
	void doExitUserArmyout(Armyout armyout);

	/**
	 * 两点间距离
	 * 
	 * @param fromX
	 * @param fromY
	 * @param toX
	 * @param toY
	 * @return
	 */
	double getDistanceBetweenPoints(int fromX, int fromY, int toX, int toY);

	/**
	 * 侦查时间(秒)
	 * 
	 * @param user
	 * @param fromCell
	 * @param toCell
	 * @return
	 */
	double getSpyTimeBetweenPoints(User user, MapCell fromCell, MapCell toCell);

	/**
	 * 总的基础时间(秒)
	 * 
	 * @param user
	 * @param fromCell
	 * @param toCell
	 * @return
	 */
	double getBaseTimeBetweenPoints(User user, MapCell fromCell,
			MapCell toCell);

	/**
	 * 基础资源消耗
	 * 
	 * @param strategy
	 * @param distance
	 * @return
	 */
	double getBaseResConsume(Strategy strategy, double distance);

	double getBaseResConsume(Strategy strategy, MapCell fromCell,
			MapCell toCell);
}
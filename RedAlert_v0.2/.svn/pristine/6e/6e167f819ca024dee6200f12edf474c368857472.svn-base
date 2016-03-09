package com.youxigu.dynasty2.develop.service;

import java.util.List;

import com.youxigu.dynasty2.develop.domain.CastleEffect;
import com.youxigu.dynasty2.develop.domain.CastleResource;
import com.youxigu.dynasty2.user.domain.User;

/**
 * 操作资源的接口
 * @author Dagangzi
 *
 */
public interface ICastleResService {
	/**
	 * 加锁
	 * @param casId
	 * @return
	 */
	CastleResource lockCasRes(long casId);
	
	/**
	 * 更新资源-外部加锁-不推送刷新资源
	 * @param castleResource
	 */
	void updateCastleResource(CastleResource castleResource);
	
	/**
	 * 更新资源-外部加锁
	 * @param castleResource
	 * @param sendEvent
	 */
	void updateCastleResource(CastleResource castleResource, long userId, boolean sendEvent);
	
	/**
	 * 获取城池资源
	 * @param casId
	 * @return
	 */
	CastleResource getCastleResourceById(long casId);

	/**
	 * 增加某一类型的资源,GM接口
	 * @param casId
	 * @param num
	 * @param type
	 *  0=各种资源, 1=金矿,2=铁矿,3=油矿,4=铀矿
	 * @return
	 */
	CastleResource doGmtoolAddResource(long casId, int num, int type);

    /**
     * 根据资源的entityId增加资源
     * @param casId
     * @param entId 资源的entityId
     * @param resNum 增加的数量，不能小于0
     * @param sendFlushRes 是否向前台推送
     */
    void doAddRes(long casId, int entId, long resNum, boolean sendFlushRes);
    
    /**
     * 根据资源的entityId增加资源
     * @param casId
     * @param entIds
     * @param resNums
     * @param sendFlushRes
     */
    void doAddRes(long casId, int[] entIds, long[] resNums, boolean sendFlushRes);
    
    /**
     * 根据资源的entityId消耗资源
     * @param casId
     * @param entId 资源的entityId
     * @param resNum 增加的数量，不能小于0
     * @param sendFlushRes 是否向前台推送
     * @return 是否满足消耗  false 不满足
     */
    boolean doDelRes(long casId, int entId, long resNum, boolean sendFlushRes, boolean throwException);
    
    /**
     * 根据资源的entityId消耗资源
     * @param casId
     * @param entIds 实体数组
     * @param resNums 增加的数量数组，不能小于0
     * @param sendFlushRes 是否满足消耗  false 不满足
     */
    boolean doDelRes(long casId, int[] entIds, long[] resNums, boolean sendFlushRes, boolean throwException);

    /**
	 * 取得金矿的生产上限
	 * @param casId
	 * @return
	 */
	int getGoldProduceLimit(long casId);	
	int getGoldProduceLimit(long casId,List<CastleEffect> effects);
	
	/**
	 * 取得铁矿的生产上限
	 * @param casId
	 * @return
	 */
	int getIronProduceLimit(long casId);	
	int getIronProduceLimit(long casId,List<CastleEffect> effects);
	
	/**
	 * 取得有油矿的生产上限
	 * @param casId
	 * @return
	 */
	int getOilProduceLimit(long casId);	
	int getOilProduceLimit(long casId,List<CastleEffect> effects);
	
	/**
	 * 取得铀矿的生产上限
	 * @param casId
	 * @return
	 */
	int getUraniumProduceLimit(long casId);	
	int getUraniumProduceLimit(long casId,List<CastleEffect> effects);
	
	/**
	 * 得到每15分钟的金矿产量
	 * @param user
	 * @return
	 */
	int getGoldProducePerQuarter(User user);
	int getGoldProducePerQuarter(User user,List<CastleEffect> effects);
	int getGoldProducePerQuarter(long userId,long casId);
	int getGoldProducePerQuarter(long userId,long casId,List<CastleEffect> effects);
	
	/**
	 * 得到每15分钟的铁矿产量
	 * @param user
	 * @return
	 */
	int getIronProducePerQuarter(User user);
	int getIronProducePerQuarter(User user,List<CastleEffect> effects);
	int getIronProducePerQuarter(long userId,long casId);
	int getIronProducePerQuarter(long userId,long casId,List<CastleEffect> effects);
	
	/**
	 * 得到每15分钟的油矿产量
	 * @param user
	 * @return
	 */
	int getOilProducePerQuarter(User user);
	int getOilProducePerQuarter(User user,List<CastleEffect> effects);
	int getOilProducePerQuarter(long userId,long casId);
	int getOilProducePerQuarter(long userId,long casId,List<CastleEffect> effects);
	
	/**
	 * 得到每15分钟的铀矿产量
	 * @param user
	 * @return
	 */
	int getUraniumProducePerQuarter(User user);
	int getUraniumProducePerQuarter(User user,List<CastleEffect> effects);
	int getUraniumProducePerQuarter(long userId,long casId);
	int getUraniumProducePerQuarter(long userId,long casId,List<CastleEffect> effects);
	
	/**
	 * 获得主基地的金矿容量
	 * @param casId
	 * @return
	 */
	int getGoldCapacity(long casId);
	int getGoldCapacity(long casId,List<CastleEffect> effects);
	
	/**
	 * 获得主基地的铁矿容量
	 * @param casId
	 * @return
	 */
	int getIronCapacity(long casId);
	int getIronCapacity(long casId,List<CastleEffect> effects);
	
	/**
	 * 获得主基地的油矿容量
	 * @param casId
	 * @return
	 */
	int getOilCapacity(long casId);
	int getOilCapacity(long casId,List<CastleEffect> effects);
	
	/**
	 * 获得主基地的铀矿容量
	 * @param casId
	 * @return
	 */
	int getUraniumCapacity(long casId);
	int getUraniumCapacity(long casId,List<CastleEffect> effects);
	
	/**
	 * 获得仓库金矿被掠夺上限
	 * @param casId
	 * @return
	 */
	int getGoldRobLimit(long casId);
	int getGoldRobLimit(long casId,List<CastleEffect> effects);
	
	/**
	 * 获得仓库铁矿被掠夺上限
	 * @param casId
	 * @return
	 */
	int getIronRobLimit(long casId);
	int getIronRobLimit(long casId,List<CastleEffect> effects);
	
	/**
	 * 获得仓库油矿被掠夺上限
	 * @param casId
	 * @return
	 */
	int getOilRobLimit(long casId);
	int getOilRobLimit(long casId,List<CastleEffect> effects);
	
	/**
	 * 获得仓库铀矿被掠夺上限
	 * @param casId
	 * @return
	 */
	int getUraniumRobLimit(long casId);
	int getUraniumRobLimit(long casId,List<CastleEffect> effects);
	
	/**
	 * 收获金矿
	 * @param casId
	 * @param type
	 * @return
	 */
	CastleResource doGainGold(long casId,long userId);
	
	/**
	 * 收获铁矿
	 * @param casId
	 * @param userId
	 * @return
	 */
	CastleResource doGainIron(long casId,long userId);
	
	/**
	 * 收获油矿
	 * @param casId
	 * @param userId
	 * @return
	 */
	CastleResource doGainOil(long casId,long userId);
	
	/**
	 * 收获铀矿
	 * @param casId
	 * @param userId
	 * @return
	 */
	CastleResource doGainUranium(long casId,long userId);
}

package com.youxigu.dynasty2.develop.service;

import java.util.List;

import com.youxigu.dynasty2.develop.domain.Castle;
import com.youxigu.dynasty2.develop.domain.CastleBuilder;
import com.youxigu.dynasty2.develop.domain.CastleBuilding;
import com.youxigu.dynasty2.develop.domain.UserTechnology;

/*
 * 城池service接口定义
 */
public interface ICastleService {
    /**
     * 根据国家/郡Id，创建一个castle,作为user的主城
     *
     * @param userId
     * @param stateId
     * @return
     */
    Castle createCastle(long userId, int stateId);

    /**
     * 获取城市当前的所有建筑。未加锁
     *
     * @param casId
     * @return
     */
    List<CastleBuilding> doGetCastleBuildingsByCasId(long casId);

    /**
     * 使用当前用户建筑队列中builderIndex指定的队列，对castleBuildingId指定的建筑进行升级
     * @param castle 主城
     * @param builderIndex 建筑队列序号。第1、第2、第3队列的值分别为1、2、3。
     * @param castleBuildingId 建筑对应的Id（非entity Id）
     * @return 正在升级的建筑对象
     */
    CastleBuilding doUpgradeCastleBuilding(Castle castle, int builderIndex,
                                           long castleBuildingId, boolean isAuto);

    /**
     * 启动建筑的升级，不管是否满足限制条件。供GM工具使用
     * @param casId
     * @param builderIndex
     * @param castleBuildingId
     * @return
     */
    CastleBuilding doUpgradeCastleBuildingWithoutLimit(long casId, int builderIndex,
                                                       long castleBuildingId);

    /**
     * 取消建筑升级
     * @param castle
     * @param castleBuildingId
     */
    CastleBuilding doCancelBuilding(Castle castle, long castleBuildingId);

    /**
     * 加速建筑建造
     * @param userId 用户id
     * @param casId 城池id
     * @param casBuiId 待加速的建筑的主键
     * @param speedUpType 加速类型：免费、钻石、道具
     * @param entId 加速道具ID
     * @param num 加速道具数量
     * @return
     */
    CastleBuilding doSpeedupUpgradeCastleBuilding(long userId, long casId, long casBuiId,
                                                  int speedUpType,
                                                  int entId, int num);

    /**
     * 获取用户的建筑队列，如需加锁，需调用方自行考虑
     *
     * @param userId
     * @return
     */
    List<CastleBuilder> getCastleBuilders(long userId);

    /**
     * 自动建造。如果casBuiIds为空，调用本函数时应该传入一个空List集合，表示取消自动建造。
     * @param casId
     * @param casBuiIds 自动建造的建筑Id列表。如果客户端选中自动建造的建筑为0，则应该传入size为0的空List集合
     * @return
     */
    List<CastleBuilding> doAutoUpgradeCastleBuilding(long casId, List<Long> casBuiIds);

    /**
     * 关闭自动建造
     * @param casId
     * @return
     */
    Castle doCancelAutoBuild(long casId);

    /**
     * 升级建筑完成
     *
     * @param casId
     * @param casBuiId
     * @param level
     */
    void doEndUpgradeCastleBuilding(long casId, int casBuiId, int level);

    /**
     * 获取该建筑下一次升级时需要消耗的时间（单位秒；含各种加成） 以建筑当前的等级为准。如果建筑正在升级，仍沿用当前等级计算。
     *
     * @param casId
     * @param castleBuildingId
     * @return 升级时间（单位秒）
     */
    int getBuildingNextLevelUpgradeTime(long casId, long castleBuildingId);

    /**
     * 验证castleBuildingId对应的建筑是否满足升级条件
     *
     * @param casId
     * @param castleBuildingId
     * @return 是否满足。true：满足；false：不满足
     */
    int validateBuildingUpgradeCondition(long casId, long castleBuildingId);

    //==================Castle 获取接口===================

    /**
     * 根据casId获取用户主城，未加锁
     * @param casId
     * @return
     */
    Castle getCastleById(long casId);

    /**
     * 创建当前满足开放条件，但在城池中还未创建过的建筑
     * @param casId
     * @return
     */
    List<CastleBuilding> createOpenedCastleBuildings(long casId);

    /**
     * 根据指定的建筑实体id创建建筑，不考虑建筑的约束条件。供GM工具使用
     * @param casId
     * @param buildingIds
     * @return
     */
    List<CastleBuilding> createCastleBuildingsWithoutLimit(long casId, List<Integer> buildingIds);

    /**
     * 根据userId获取用户的主城，未加锁
     *
     * @param userId
     * @return
     */
    Castle getCastleByUserId(long userId);

    /**
     * 获取Castle并加锁。在事物中需要更改Castle时需要调用
     * @param casId
     * @return
     */
    Castle lockAndGetCastle(long casId);

    /**
     * 锁定主城
     * @param castle
     * @return
     */
    Castle lockMainCastle(Castle castle);
    
    
    void doUpdateCastle(Castle castle);

    //==================CastleBuilding 获取接口===================

    /**
     * 根据castleBuildingId获取对应的建筑对象。未加锁
     * @param casId
     * @param castleBuildingId
     * @return
     */
    CastleBuilding getCastBuildingById(long casId, long castleBuildingId);

    /**
     * 根据castleBuildingId对CastleBuilding加锁
     * @param casId
     * @param castleBuildingId
     */
    CastleBuilding lockAndGetCastleBuildingById(long casId, long castleBuildingId);

	/**
	 * 获取制定类型的最大等级建筑
	 * 
	 * @param casId
	 * @param entId
	 * @return
	 */
	CastleBuilding getMaxLevelCastBuildingByEntId(long casId, int entId);

    /**
     * 根据casId获取城池的所有已开放建筑。已加锁
     * @param casId
     * @return
     */
    List<CastleBuilding> lockAndGetCastleBuildingsByCasId(long casId);

    //==================CastleBuilder 获取接口===================

    /**
     * 锁定并获取用户的建筑队列。
     * @param userId
     * @return
     */
    List<CastleBuilder> lockAndGetCastleBuilders(long userId);

    /**
     * 根据用户状态检查是否有新的建筑队列需要开放
     * 第二队列根据君主等级开放，第三队列根据VIP等级开放
     * 用户等级和VIP等级变化时需要调用此函数
     * @param userId
     */
    void doCheckBuilderOpenStatus(long userId);

    /**
     * 激活主基地Buff
     * @param casId
     * @param buffId
     * @param useCash
     */
    void doActivateBuff(long casId, int buffId, int useCash, String pxEx);

    /**
     * 取消Buff
     * @param userId
     * @param casId
     * @param buffId
     */
    void doCancelBuff(long userId, long casId, int buffId);

    /**
     * 钻石购买资源
     * @param userId
     * @param resEntId 资源实体的entId
     * @param cashNum 钻石数量，必须为正值
     */
    void doBuyResource(long userId, int resEntId, int cashNum);


    /**********************************************
     * 玩家科技
     **********************************************/

    /**
     * 取得玩家科技列表
     *
     * @param userId
     * @return
     */
    List<UserTechnology> getUserTechnologysByUserId(long userId);

    List<UserTechnology> doRefreshUserTechnologys(long userId, long casId);

    /**
     * 根据科技实体编号取得用户科技
     *
     * @param userId
     * @param entId
     * @return
     */
    UserTechnology getUserTechnologyByEntId(long userId, int entId);

    UserTechnology lockTech(UserTechnology userTech);

    UserTechnology lockAndGetTech(long userId, long techId);

    /**
     *
     * @param userId
     * @param id
     * @return
     */
    UserTechnology getUserTechnologyById(long userId, long id);

    /**
     * 升级科技
     *
     * @param casId
     * @param entId
     * @return
     */
    UserTechnology doUpgradeUserTechnology(long casId, int entId);

    /**
     * job完成科技升级
     *
     * @param casId
     * @param utId
     * @param level
     */
    void doEndUpgradeUserTechnology(long casId, int utId, int level);

    /**
     * 取消科技升级
     *  @param casId
     * @param entId
     */
    UserTechnology doCancelUpgradeUserTechnology(long casId, int entId);

    UserTechnology doFastUpgradeUserTechnology(long casId, int techEntId, int speedUpType, int itemEntId, int itemNum);

    int getTechNextLevelUpgradeTime(long casId, long userId, int techEntId);
}

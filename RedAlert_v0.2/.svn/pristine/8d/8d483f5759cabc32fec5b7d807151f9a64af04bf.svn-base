package com.youxigu.dynasty2.develop.dao;

import java.util.List;

import com.youxigu.dynasty2.develop.domain.Castle;
import com.youxigu.dynasty2.develop.domain.CastleBuilder;
import com.youxigu.dynasty2.develop.domain.CastleBuilding;
import com.youxigu.dynasty2.develop.domain.UserTechnology;

/**
 * 城池对象的DAO接口
 */
public interface ICastleDao {
	// ==========================Castle========================

	/**
	 * 建造城池
	 *
	 * @param castle
	 */
	void createCastle(Castle castle);

	/**
	 * 更新城池
	 *
	 * @param castle
	 */
	void updateCastle(Castle castle);

	/**
	 * 取得指定君主的所有城池
	 *
	 * @param userId
	 *            君主id
	 * @return 城池列表
	 */
	Castle getCastleByUserId(long userId);

	/**
	 * 取得指定城池id的城池
	 *
	 * @param casId
	 *            城池id
	 * @return 城池对象
	 */
	Castle getCastleById(long casId);

	// ==========================CastleBuilding========================

	/**
	 * 新建建筑
	 *
	 * @param cb
	 */
	void createCastleBuilding(CastleBuilding cb);

	/**
	 * 更新建筑(升级/降级)
	 *
	 * @param cb
	 */
	void updateCastleBuilding(CastleBuilding cb);

	/**
	 * 删除建筑
	 *
	 * @param cb
	 */
	void deleteCastleBuilding(CastleBuilding cb);

    /**
     * 根据主键获取建筑
     *
     * @param casBuildingId
     * @return
     */
    CastleBuilding getCastBuildingById(long casId, long casBuildingId);

	/**
	 * 根据城池ID获取城池内所有建筑
	 *
	 * @param casId
	 * @return
	 */
	List<CastleBuilding> getCastBuildingsByCasId(long casId);

	// ==========================CastleBuilder=========================

	/**
	 * 获得城池建筑队列
	 *
	 * @param userId
	 * @return
	 */
	List<CastleBuilder> getCastleBuilders(long userId);

	/**
	 * 创建建筑队列
	 *
	 * @param casBuilder
	 */
	void createCastleBuilder(CastleBuilder casBuilder);

	/**
	 * 删除建筑队列
	 *
	 * @param casBuilder
	 */
	void deleteCastleBuilder(CastleBuilder casBuilder);

    /**
     * 修改建筑队列
     *
     * @param casBuilder
     */
    void updateCastleBuilder(CastleBuilder casBuilder);

    // ==========================Technology=========================

	/**
	 * 创建玩家科技
	 */
	void createUserTechnology(UserTechnology ut);

	/**
	 * 更新玩家科技（升级）
	 *
	 * @param ut
	 */
	void updateUserTechnology(UserTechnology ut);

	/**
	 * 取得玩家科技列表
	 *
	 * @param userId
	 * @return
	 */
	List<UserTechnology> getUserTechnologysByUserId(long userId);

	/**
	 *
	 * @param id
	 * @return
	 */

	UserTechnology getUserTechnologyById(long userId, long id);


	/**
	 * 根据科技实体编号取得用户科技
	 * @param userId
	 * @param entId
	 * @return
	 */
	@Deprecated
    UserTechnology getUserTechnologyByEntId(long userId, int entId);
}

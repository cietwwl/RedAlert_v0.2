package com.youxigu.dynasty2.entity.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.youxigu.dynasty2.entity.domain.Entity;
import com.youxigu.dynasty2.entity.domain.EntityConsume;
import com.youxigu.dynasty2.entity.domain.EntityLimit;
import com.youxigu.dynasty2.entity.domain.effect.EffectTypeDefine;

/**
 * 实体service的接口定义
 * 
 */

public interface IEntityService {

	/**
	 * 获取实体定义
	 * 
	 * @param entId
	 * @return
	 */

	public Entity getEntity(int entId);
	
	/**
	 * 重新加载实体,GM在修改了实体属性后，使用GM工具调用
	 * 
	 * @param entId
	 * @return
	 */
	public Entity reloadEntity(int entId);
	
	public void reloadEntitys(int[] entIds,boolean broadcast);
	public void reloadEntitys(String entIds,boolean broadcast);	

	/**
	 * 或取某一类型的所有实体
	 * 
	 * @param entType
	 * @return
	 */
	public List<Entity> getEntityByEntTypes(String entType);

	/**
	 * 根据实体类型，子类型获取子类型的所有实体
	 * @param entType
	 * @param subType
	 * @return
	 */
	public List<Entity> getEntityByTypes(String entType,String subType);
	
	/**
	 * 获取所有效果类型
	 * @return
	 */
	public List<EffectTypeDefine>  getAllEffectTypes();
	
	/**
	 * 得到全部实体
	 * @return
	 */
	public Collection<Entity> getAllEntitys();
	
	/**
	 * 得到全部实体的类型
	 * @return
	 */
	public Collection<String> getAllEntityTypes();
	/**
	 * 
	 * 根据实体消耗定义及当前城池的资源数得到比较结果 ，该方法为前台界面显示用
	 * 
	 * @param consumes
	 * @param casId
	 * @return
	 */
	public EntityConsumeResultSet getEntityConsumeCheckResult(
			List<EntityConsume> consumes, long casId);

	/**
	 * 根据实体消耗定义及当前城池的资源数得到比较结果，该方法为前台界面显示用
	 * 
	 * @param consumes
	 * @param casId
	 * @param num
	 *            : 倍数，consumes是一个实体的消耗,如果是多个实体，则用num来表示数量 。默认是1 <br>
	 * 
	 * @param factor
	 *            除数，对于取消，需要返回消耗，默认是1.0
	 * @return
	 */
	public EntityConsumeResultSet getEntityConsumeCheckResult(
			List<EntityConsume> consumes, long casId, int num, double factor);

	/**
	 * 检查并更新消耗的资源，任何一项资源不符合要求，抛出异常
	 * 
	 * @param consumes
	 * @param casId
	 * @return
	 */
	public EntityConsumeResultSet updateByEntityConsume(
			List<EntityConsume> consumes, long casId);

	public EntityConsumeResultSet updateByEntityConsume(
			List<EntityConsume> consumes, long casId, int num, double factor);

	public EntityConsumeResultSet updateByEntityConsume(
			List<EntityConsume> consumes, Map<String,Object> context);

	/**
	 * 检查实体约束
	 * 
	 * 返回 EntityLimitResultSet <br>
	 * 该类的 isMatch()==true表示符合约束条件 ,否则，不符合约束条件，<br>
	 * isMatch()==false，可以用该类的 getExceptionString()返回格式化的异常信息.
	 * 
	 * @param limits
	 * @param casId
	 * @return
	 */
	public EntityLimitResultSet checkLimit(List<EntityLimit> limits, long casId);

	
	public EntityLimitResultSet checkLimit(List<EntityLimit> limits, Map<String, Object> context);
	
	
	/**
	 * 
	 * TODO:参数应该怎样才合适？
	 * 
	 * @param entity
	 * @param action
	 *            实体执行的动作 Entity.ACTION_LEVEL_UP
	 *            ,Entity.ACTION_LEVEL_DOWN........
	 * @param params
	 *            不同的IEntityEffectRender实现会需要不同的参数 : casId ,userId,
	 *            heroId,num,level,prevLevel......
	 */
	Map<String, Object> doAction(Entity entity, int action,
			Map<String, Object> params);
	Map<String, Object> doAction(int entId, int action,
			Map<String, Object> params);

	/**
	 * 执行实体操作 建筑建造，升级，降级，拆除。。。。。。。。
	 * 
	 */
	// public void doEntityAction(int entId, Map<String, Object> params);
	
	/**
	 * 创建实体，导入数据库实体使用
	 */
	void createEntity(Entity entity);
	void createEffectTypeDefine(EffectTypeDefine effectTypeDefine);	
}

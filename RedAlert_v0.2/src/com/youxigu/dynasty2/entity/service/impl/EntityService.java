package com.youxigu.dynasty2.entity.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibatis.sqlmap.engine.cache.memcached.broadcast.IBroadcastProducer;
import com.youxigu.dynasty2.entity.dao.IEntityDao;
import com.youxigu.dynasty2.entity.domain.Entity;
import com.youxigu.dynasty2.entity.domain.EntityConsume;
import com.youxigu.dynasty2.entity.domain.EntityLimit;
import com.youxigu.dynasty2.entity.domain.effect.EffectDefine;
import com.youxigu.dynasty2.entity.domain.effect.EffectTypeDefine;
import com.youxigu.dynasty2.entity.service.EntityConsumeResultSet;
import com.youxigu.dynasty2.entity.service.EntityLimitResultSet;
import com.youxigu.dynasty2.entity.service.IEffectRender;
import com.youxigu.dynasty2.entity.service.IEntityConsumeChecker;
import com.youxigu.dynasty2.entity.service.IEntityConsumeValidator;
import com.youxigu.dynasty2.entity.service.IEntityEffectRender;
import com.youxigu.dynasty2.entity.service.IEntityFactoryService;
import com.youxigu.dynasty2.entity.service.IEntityLimitChecker;
import com.youxigu.dynasty2.entity.service.IEntityLimitValidator;
import com.youxigu.dynasty2.entity.service.IEntityService;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.dynasty2.util.StringUtils;
import com.youxigu.wolf.net.AsyncWolfTask;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * 实体service的接口的实现类
 * 
 */
@SuppressWarnings("rawtypes")
public class EntityService implements IEntityService {

	public static final Logger log = LoggerFactory
			.getLogger(EntityService.class);

	private IEntityDao entityDao;
	/**
	 * 资源消耗检查器
	 */
	private IEntityConsumeChecker consumeChecker;

	/**
	 * 实体限制检查器
	 */
	private IEntityLimitChecker limitChecker;
	/**
	 * 实体构造器 factory的初始化在配置文件中完成，存储结构举例：BUILDING-BuildingEntityFactoryService
	 */
	private Map<String, IEntityFactoryService> factorys;

	// Map<String,Long> testMap = new HashMap<String,Long>();
	/**
	 * 实体消耗校验器的初始化在配置文件中完成，存储结构举例：金钱消耗(20100001)-ResourceMoneyConsumeValidator
	 * 
	 * 按entity类型或者实体ID来配置
	 */
	private Map<String, IEntityConsumeValidator> consumeValidators = new HashMap<String, IEntityConsumeValidator>();

	/**
	 * 实体约束校验器的初始化在配置文件中完成，存储结构举例：BUILDING-BuildingLimitValidator
	 * 
	 * 按entity类型或者实体ID来配置
	 */
	private Map<String, IEntityLimitValidator> limitValidators = new HashMap<String, IEntityLimitValidator>();
	/**
	 * 实体缓存,由于GM可能线上实时修改并加载数据到entityCache,因此，这里用ConcurrentHashMap;
	 */
	private Map<Integer, Entity> entityCache = null;

	private Map<String, List<Entity>> entityCacheByType = new ConcurrentHashMap<String, List<Entity>>();

	/*
	 * 实体效果类型缓存
	 */
	private Map<String, EffectTypeDefine> entityEffectTypeCache = new HashMap<String, EffectTypeDefine>();

	/**
	 * 
	 * 根据实体类型/ID，缓存的实体效果渲染器，主要是building,科技使用
	 */
	private Map<String, IEntityEffectRender> entityEffectRenders = new HashMap<String, IEntityEffectRender>();

	/**
	 * 
	 * 单个effect的渲染器，主要是Item使用
	 * ，如果entityEffectRenders中有该entity的效果render,则不使用单个effect的渲染器
	 * 
	 * 可以定义在EffectDefine中的serviceName中 或者在配置文件中配置：按effectId配置，或者按effectType配置
	 */
	private Map<String, IEffectRender> effectRenders = new HashMap<String, IEffectRender>();

	private List<EffectTypeDefine> effectTypes = null;

	private IBroadcastProducer broadcastMgr;

	public void setBroadcastMgr(IBroadcastProducer broadcastMgr) {
		this.broadcastMgr = broadcastMgr;
	}

	public void setEffectRenders(Map<String, IEffectRender> effectRenders) {
		this.effectRenders = effectRenders;
	}

	public void setLimitChecker(IEntityLimitChecker limitChecker) {
		this.limitChecker = limitChecker;
	}

	public void setConsumeChecker(IEntityConsumeChecker consumeChecker) {
		this.consumeChecker = consumeChecker;
	}

	public void setEntityDao(IEntityDao entityDao) {
		this.entityDao = entityDao;
	}

	public void setConsumeValidators(
			Map<String, IEntityConsumeValidator> consumeValidators) {
		this.consumeValidators = consumeValidators;
	}

	public void setLimitValidators(
			Map<String, IEntityLimitValidator> limitValidators) {
		this.limitValidators = limitValidators;
	}

	public void setFactorys(Map<String, IEntityFactoryService> factorys) {
		this.factorys = factorys;
	}

	public void setEntityEffectRenders(
			Map<String, IEntityEffectRender> entityEffectRenders) {
		this.entityEffectRenders = entityEffectRenders;
	}

	// bean实例化后要执行的初始化方法
	public void init() {

		if (consumeChecker == null) {
			throw new BaseException("must set consumeChecker  property");
		}

		if (limitChecker == null) {
			throw new BaseException("must set limitChecker  property");
		}
		if (factorys == null || factorys.size() == 0) {
			throw new BaseException("must set factorys to create entity");
		}
		log.info("加载实体定义.........");

		entityCache = new ConcurrentHashMap<Integer, Entity>();

		// 取得所有的实体效果类型，缓存
		effectTypes = getAllEffectTypes();
		if (effectTypes != null) {
			for (EffectTypeDefine type : effectTypes) {
				entityEffectTypeCache.put(type.getEffTypeId(), type);
			}
		} else {
			log.warn("EffectType not found");
		}

		// 取得实体表中的所有实体-每个实体被封装到了map中
		List<Map<String, Object>> entitys = entityDao.getEntitys();
		if (entitys == null)
			return;

		Map<String, Object> context = new HashMap<String, Object>();
		context.put("effectTypes", entityEffectTypeCache);
		context.put("effectRenders", effectRenders);
		context.put("entityCacheByType", entityCacheByType);

		for (Map<String, Object> entityMap : entitys) {
			int entityId = (Integer) entityMap.get("entId");// 实体id
			String entityType = (String) entityMap.get("entType");// 实体类型

			_loadEntity(entityId, entityType, context);
		}

		// 检查并设置Consume需要的entity
		Iterator<Entity> lit = entityCache.values().iterator();
		while (lit.hasNext()) {// 遍历所有实体
			Entity entity = lit.next();// 取出相应的实体
			_afterLoadEntity(entity);

		}

		// 排序
		Iterator<List<Entity>> lit1 = entityCacheByType.values().iterator();
		while (lit1.hasNext()) {
			Collections.sort(lit1.next());
		}

		// Map<String, List<Entity>> entityCacheByType

		log.info("加载实体定义数据完成.........");
	}

	@SuppressWarnings("unchecked")
	private Entity _loadEntity(int entityId, String entityType,
			Map<String, Object> context) {

		// 相应的实体类型交给对应的实体service处理
		IEntityFactoryService factory = this
				.getEntityFactoryService(entityType);
		if (factory == null) {
			log.warn("can not find entity:{} 's factory,maybe error", entityId);
		} else {
			// 执行相应的XXEntityFactoryService父类AbstractEntityFactoryService中的getEntity方法
			// 完善实体定义增加容量-消耗-约束
			// long time = System.nanoTime();
			Entity entity = factory.getEntity(entityId, entityType, context);

			if (entity != null) {
				entityCache.put(entityId, entity);
				addToTypeCache(entity);
			} else {
				log.warn("can not find entity:{} 's define,maybe error",
						entityId);
			}
			return entity;

		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private void _afterLoadEntity(Entity entity) {
		// 设置实体渲染器
		IEntityEffectRender render = entityEffectRenders.get(String
				.valueOf(entity.getEntId()));
		if (render == null) {
			render = entityEffectRenders.get(entity.getEntType());
		}
		if (render != null) {
			entity.setRender(render);
		}

		List<EntityConsume> consumes = entity.getConsumes();// 该实体的全部消耗
		if (consumes != null) {
			Iterator<EntityConsume> lit1 = consumes.iterator();
			while (lit1.hasNext()) {// 遍历每个实体的所有消耗
				EntityConsume consume = lit1.next();// 取到一个消耗
				Entity need = entityCache.get(consume.getNeedEntId());// 取到这个消耗需要的实体
				if (need != null) {// 能够找到所需的实体
					consume.setNeedEntity(need);
					IEntityConsumeValidator v = getConsumeValidator(need);
					if (v == null) {
						lit1.remove();
						log.warn(
								"EntityConsume's comsume validator not found,entId={},needEntId={}",
								consume.getEntId(), consume.getNeedEntId());

					} else {
						consume.setValidator(v);
					}
				} else {// 找不到从消耗集中过滤掉
					lit1.remove();
					log.warn(
							"EntityConsume's needentId not found,entId={},needEntId={}",
							consume.getEntId(), consume.getNeedEntId());
				}
			}
		}

		// 检查并设置limit约束所需的entity
		List<EntityLimit> limits = entity.getLimits();// 该实体的全部约束
		if (limits != null) {
			Iterator<EntityLimit> lit1 = limits.iterator();
			while (lit1.hasNext()) {// 遍历每个实体的所有约束
				EntityLimit limit = lit1.next();// 取到一个约束
				Entity need = entityCache.get(limit.getNeedEntId());// 取到这个约束需要的实体
				if (need != null) {// 能够找到所需的实体
					limit.setNeedEntity(need);
					IEntityLimitValidator v = getLimitValidator(need);
					if (v == null) {
						lit1.remove();
						log.warn(
								"EntityConsume's limit validator not found,entId={},needEntId={}",
								limit.getEntId(), limit.getNeedEntId());

					} else {
						limit.setValitor(v);
					}
				} else {// 找不到从约束集中过滤掉
					lit1.remove();
					log.warn(
							"EntityLimit's needentId not found,entId={},needEntId={}",
							limit.getEntId(), limit.getNeedEntId());
				}
			}
		}

		IEntityFactoryService factory = this.getEntityFactoryService(entity
				.getEntType());
		if (factory != null) {

			factory.afterLoad(entity, this);
		}

	}

	/**
	 * 取相应的实体消耗验证器
	 * 
	 * @param entity
	 * @return
	 */
	private IEntityConsumeValidator getConsumeValidator(Entity entity) {
		IEntityConsumeValidator v = consumeValidators.get(String.valueOf(entity
				.getEntId()));
		if (v == null) {
			v = consumeValidators.get(entity.getEntType());
		}
		if (v == null) {// 用默认的检查器
			v = consumeValidators.get("*");
		}
		return v;

	}

	/**
	 * 取相应的实体约束验证器
	 * 
	 * @param entity
	 * @return
	 */
	private IEntityLimitValidator getLimitValidator(Entity entity) {
		IEntityLimitValidator v = limitValidators.get(String.valueOf(entity
				.getEntId()));
		if (v == null) {
			v = limitValidators.get(entity.getEntType());
		}
		if (v == null) {// 用默认的检查器
			v = limitValidators.get("*");
		}
		return v;

	}

	// 取得对应XXXEntityFactoryService
	private IEntityFactoryService getEntityFactoryService(String entityType) {
		return factorys.get(entityType);
	}

	// 保存为缓存 存储结构举例： 建筑-所有建筑类型
	private void addToTypeCache(Entity entity) {
		if (entity.getEntType() == null) {
			System.out.println();
		}
		List<Entity> entityes = entityCacheByType.get(entity.getEntType());
		if (entityes == null) {
			entityes = new ArrayList<Entity>();

			entityCacheByType.put(entity.getEntType(), entityes);
		}
		entityes.add(entity);
	}

	/**
	 * 
	 */
	@Override
	public Entity getEntity(int entId) {
		if (entityCache == null) {
			init();
		}
		Entity entity = entityCache.get(entId);
		if (entity == null && entId != 0) {
			// 动态加载,由于很少会出现动态加载，这里用synchronized来处理
			synchronized (this) {
				entity = reloadEntity(entId);
			}
		}
		return entity;
	}

	@Override
	public void reloadEntitys(String entIds, boolean broadcast) {
		String[] tmps = StringUtils.split(entIds, ",");
		int[] ids = new int[tmps.length];
		for (int i = 0; i < tmps.length; i++) {
			ids[i] = Integer.parseInt(tmps[i]);
		}
		reloadEntitys(ids, broadcast);
	}

	@Override
	public void reloadEntitys(int[] entIds, boolean broadcast) {
		for (int entId : entIds) {
			reloadEntity(entId);
		}

		if (broadcast && broadcastMgr != null) {
			broadcastMgr.sendNotification(new AsyncWolfTask("entityService",
					"reloadEntitys", new Object[] { entIds, false }));
		}
	}

	@Override
	public Entity reloadEntity(int entId) {
		Entity entity = null;
		Map<String, Object> entityMap = entityDao.getEntityById(entId);
		if (entityMap != null) {

			int entityId = (Integer) entityMap.get("entId");// 实体id

			if (log.isDebugEnabled()) {
				log.debug("重新加载实体:{}", entityId);
			}

			String entityType = (String) entityMap.get("entType");// 实体类型

			Map<String, Object> context = new HashMap<String, Object>();
			context.put("effectTypes", entityEffectTypeCache);
			context.put("effectRenders", effectRenders);
			context.put("entityCacheByType", entityCacheByType);

			entity = _loadEntity(entityId, entityType, context);

			if (entity != null) {
				_afterLoadEntity(entity);
			}
		}
		return entity;
	}

	public List<Entity> getEntityByEntTypes(String entType) {
		return entityCacheByType.get(entType);
	}

	@Override
	public Collection<Entity> getAllEntitys() {
		// TODO Auto-generated method stub
		return entityCache.values();
	}

	@Override
	public Collection<String> getAllEntityTypes() {
		// TODO Auto-generated method stub
		return entityCacheByType.keySet();
	}

	@Override
	public EntityConsumeResultSet getEntityConsumeCheckResult(
			List<EntityConsume> consumes, long casId) {
		return getEntityConsumeCheckResult(consumes, casId, 1, 1.0);
	}

	@Override
	public EntityConsumeResultSet getEntityConsumeCheckResult(
			List<EntityConsume> consumes, long casId, int num, double factor) {

		Map<String, Object> context = new HashMap<String, Object>();
		context.put("casId", casId);
		context.put("num", num);
		context.put("factor", factor);
		return consumeChecker.check(consumes, context);
	}

	@Override
	public EntityConsumeResultSet updateByEntityConsume(
			List<EntityConsume> consumes, long casId) {
		// TODO Auto-generated method stub
		return updateByEntityConsume(consumes, casId, 1, 1.0);
	}

	@Override
	public EntityConsumeResultSet updateByEntityConsume(
			List<EntityConsume> consumes, long casId, int num, double factor) {
		Map<String, Object> context = new HashMap<String, Object>();
		context.put("casId", casId);
		context.put("num", num);
		context.put("factor", factor);
		return consumeChecker.checkAndUpdate(consumes, context);

	}

	@Override
	public EntityConsumeResultSet updateByEntityConsume(
			List<EntityConsume> consumes, Map<String, Object> context) {
		if (!context.containsKey("num")) {
			context.put("num", 1);
		}
		if (!context.containsKey("factor")) {
			context.put("factor", 1.0d);
		}

		return consumeChecker.checkAndUpdate(consumes, context);
	}

	@Override
	public EntityLimitResultSet checkLimit(List<EntityLimit> limits, long casId) {
		Map<String, Object> context = new HashMap<String, Object>();
		context.put("casId", casId);
		return limitChecker.check(limits, context);
	}

	public EntityLimitResultSet checkLimit(List<EntityLimit> limits,
			Map<String, Object> context) {
		return limitChecker.check(limits, context);
	}

	@Override
 	public Map<String, Object> doAction(int entId, int action,
			Map<String, Object> params) {
        Entity entity = getEntity(entId);
        if(entity == null){
            throw new BaseException("错误的entId。");
        }
        return doAction(entity, action, params);
    }

	/**
	 * params : level,castle,mainCastle user.........
	 */
	@Override
	public Map<String, Object> doAction(Entity entity, int action,
			Map<String, Object> params) {
		params.put("action", action);
		// 默认处理，只处理effect
		if (entity.getRender() != null) {
			return entity.getRender().render(entity, params);
		} else {
			Map<String, Object> result = new HashMap<String, Object>();
			List<EffectDefine> effects = null;
			Object tmp = params.get("level");
			if (tmp != null) {
				effects = entity.getEffects(((Integer) tmp).intValue());
			} else {
				effects = entity.getEffects();
			}

			if (effects != null && effects.size() > 0) {
				for (EffectDefine effect : effects) {
					IEffectRender effectRender = effect.getRender();
					if (effectRender != null) {
						Map<String, Object> one = effectRender.render(entity,
								effect, params);
						if (one != null)
							result.putAll(one);
					}

				}
			}
			return result;
		}
		// }

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Entity> getEntityByTypes(String entType, String subType) {
		IEntityFactoryService factory = this.getEntityFactoryService(entType);
		if (factory != null) {
			return factory.getEntityByTypes(subType, this);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void createEntity(Entity entity) {
		// TODO Auto-generated method stub
		IEntityFactoryService factory = this.getEntityFactoryService(entity
				.getEntType());
		if (factory == null) {
			log.error("没有找到实体工厂:entId={},entType={}", entity.getEntId(),
					entity.getEntType());
		}
		factory.createEntity(entity, this);

		this.reloadEntitys(new int[] { entity.getEntId() }, true);
	}

	public void createEffectTypeDefine(EffectTypeDefine effectTypeDefine) {
		entityDao.deleteEffectTypeDefine(effectTypeDefine.getEffTypeId());
		entityDao.createEffectTypeDefine(effectTypeDefine);
	}

	@Override
	public List<EffectTypeDefine> getAllEffectTypes() {
		return entityDao.getAllEffectTypes();
	}

	/**
	 * 判断缓存里面是否有某个物品
	 * 
	 * @param id
	 * @return
	 */
	public boolean hasEntity(int entId) {
		return entityCache.containsKey(entId);
	}

	/**
	 * 获取缓存的Entity
	 * 
	 * @param entId
	 * @return
	 */
	public Entity getCacheEntity(int entId) {
		return entityCache.get(entId);
	}

}

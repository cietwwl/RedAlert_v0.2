package com.youxigu.dynasty2.entity.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.manu.core.ServiceLocator;
import com.youxigu.dynasty2.entity.domain.Entity;
import com.youxigu.dynasty2.entity.domain.Item;
import com.youxigu.dynasty2.entity.domain.ItemExchange;
import com.youxigu.dynasty2.entity.service.IEntityFactoryService;
import com.youxigu.dynasty2.entity.service.IEntityService;
import com.youxigu.dynasty2.entity.service.IItemService;

/**
 * 道具实体的实现类 继承抽象类AbstractEntityFactoryService，完成对方法loadEntity的具体实现
 * 
 */
@SuppressWarnings("rawtypes")
public class ItemEntityService extends AbstractEntityFactoryService<Item> implements IItemService {

	/**
	 * Item分成多个子类,这里是子类的加载器
	 */
	private Map<String, IEntityFactoryService> factorys;
	/**
	 * 铁匠铺卖的道具列表
	 */
	protected static List<Item> npcSellItems = new ArrayList<Item>();

	private Map<Integer, Item> entityCache = null;
	private Map<Integer, ItemExchange> exchangeCache = null;//背包物品兑换

	public void initService() {
		log.info("初始化道具");
		if (entityCache != null) {
			return;
		}
		entityCache = new ConcurrentHashMap<Integer, Item>();
		List<Item> list = entityDao.listItems();
		if (list != null && list.size() > 0) {
			for (Item entity : list) {
				entity.check();
				entityCache.put(entity.getEntId(), entity);
			}
		}

		//兑换物品配置
		List<ItemExchange> exList = entityDao.listItemExchanges();
		if (exList != null && exList.size() > 0) {
			exchangeCache = new HashMap<Integer, ItemExchange>(exList.size());
			for (ItemExchange itemExchange : exList) {
				String[] needStr = itemExchange.getNeedEntIds().trim().split(";");
				if (needStr != null && needStr.length > 0) {
					int[][] needs = new int[needStr.length][2];
					for (int i = 0; i < needStr.length; i++) {
						String[] entStr = needStr[i].trim().split(",");
						int entId = Integer.parseInt(entStr[0]);
						int num = Integer.parseInt(entStr[1]);
						needs[i][0] = entId;
						needs[i][1] = num;
					}
					itemExchange.setNeeds(needs);
				}

				String[] toStr = itemExchange.getToEntIds().trim().split(";");
				if (toStr != null && toStr.length > 0) {
					int[][] tos = new int[toStr.length][2];
					for (int i = 0; i < toStr.length; i++) {
						String[] entStr = toStr[i].trim().split(",");
						int entId = Integer.parseInt(entStr[0]);
						int num = Integer.parseInt(entStr[1]);
						tos[i][0] = entId;
						tos[i][1] = num;
					}
					itemExchange.setTos(tos);
				}
				exchangeCache.put(itemExchange.getExchangeId(), itemExchange);
			}
		}
	}

	public void setFactorys(Map<String, IEntityFactoryService> factorys) {
		this.factorys = factorys;
	}

	// 按照指定的实体id，在item表中取一个道具
	@SuppressWarnings("unchecked")
	@Override
	public Item loadEntity(int entityId, String entityType, Map<String, Object> context) {

		Item entity = null;
		if (factorys != null) {
			IEntityFactoryService factory = factorys.get(entityType);
			if (factory != null) {
				entity = (Item) factory.getEntity(entityId, entityType, context);

				if (entity != null) {
					// 子类也要放到父类的分类中
					Map<String, List<Entity>> entityCacheByType = (Map<String, List<Entity>>) context
							.get("entityCacheByType");
					List<Entity> entityes = entityCacheByType.get(Entity.TYPE_ITEM);
					if (entityes == null) {
						entityes = new ArrayList<Entity>();
						entityCacheByType.put(Entity.TYPE_ITEM, entityes);
					}
					entityes.add(entity);
				}
			}
		}
		if (entity == null) {
			entity = entityCache.get(entityId);
			if (entity == null) {
				entity = entityDao.getItemByEntId(entityId);
			}
			if (entity != null) {
				entity.setEntType(entityType);
			}
		}

		if (entity != null) {
			// if (entity.getNpcSellLevel() > 0) {
			// npcSellItems.add(entity);
			// }
		}

		return entity;

	}

	@SuppressWarnings("unchecked")
	@Override
	public void afterLoad(Item entity, IEntityService entityService) {
		IEntityFactoryService factory = factorys.get(entity.getEntType());
		if (factory != null) {
			factory.afterLoad(entity, entityService);
		}
		
		//兑换配置
		if(entity.getExchangeId() >0) {
			entity.setExchange(exchangeCache.get(entity.getExchangeId()));
		}
	}

	@Override
	public List<Item> getAllNpcSellItems() {
		return npcSellItems;
	}

	@Override
	public List<Item> getNpcSellItemsByNpcLevel(int level) {
		List<Item> datas = new ArrayList<Item>();
		for (Item item : npcSellItems) {
			// if (item.getNpcSellLevel() <= level) {
			// datas.add(item);
			// }
		}
		return datas;
	}

	@Override
	public List<Item> getItemsByCodition(Map conds) {
		IEntityService entityService = (IEntityService) ServiceLocator.getSpringBean("entityService");

		List<Entity> all = entityService.getEntityByEntTypes(Entity.TYPE_ITEM);
		// type childType minLevel maxLevel minColor maxColor
		int type = -1;
		int childType = -1;
		int minLevel = -1;
		int maxLevel = -1;
		int minColor = -1;
		int maxColor = -1;

		Object tmp = conds.get("type");
		if (tmp != null)
			type = (Integer) tmp;

		tmp = conds.get("childType");
		if (tmp != null)
			childType = (Integer) tmp;

		tmp = conds.get("minLevel");
		if (tmp != null)
			minLevel = (Integer) tmp;

		tmp = conds.get("maxLevel");
		if (tmp != null)
			maxLevel = (Integer) tmp;

		tmp = conds.get("minColor");
		if (tmp != null)
			minColor = (Integer) tmp;

		tmp = conds.get("maxColor");
		if (tmp != null)
			maxColor = (Integer) tmp;

		List<Item> subs = new ArrayList<Item>();

		for (Entity e : all) {
			Item item = (Item) e;
			if (childType != -1) {
				if (item.getType() != type) {
					continue;
				}
			}
			if (minLevel != -1) {
				if (item.getLevel() < minLevel) {
					continue;
				}
			}

			if (maxLevel != -1) {
				if (item.getLevel() > maxLevel) {
					continue;
				}
			}

			if (minColor != -1) {
				if (item.getColor() < minColor) {
					continue;
				}
			}

			if (maxColor != -1) {
				if (item.getColor() > maxColor) {
					continue;
				}
			}
			subs.add(item);

		}
		return subs;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void createEntity(Item entity, IEntityService entityService) {
		super.createEntity(entity, entityService);
		// entityDao.deleteItem(entity.getEntId());
		entityDao.createItem(entity);

		if (factorys != null) {
			IEntityFactoryService factory = factorys.get(entity.getEntType());
			if (factory != null) {
				factory.createEntity(entity, entityService);
			}
		}
	}
}
package com.youxigu.dynasty2.entity.service.script;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.manu.util.Util;
import com.youxigu.dynasty2.develop.service.ICastleResService;
import com.youxigu.dynasty2.entity.domain.BoxEntity;
import com.youxigu.dynasty2.entity.domain.DropPackEntity;
import com.youxigu.dynasty2.entity.domain.DropPackItem;
import com.youxigu.dynasty2.entity.domain.DroppedEntity;
import com.youxigu.dynasty2.entity.domain.Entity;
import com.youxigu.dynasty2.entity.domain.EntityConsume;
import com.youxigu.dynasty2.entity.domain.Item;
import com.youxigu.dynasty2.entity.domain.Party;
import com.youxigu.dynasty2.entity.domain.Resource;
import com.youxigu.dynasty2.entity.domain.SysHero;
import com.youxigu.dynasty2.entity.service.IEntityEffectRender;
import com.youxigu.dynasty2.entity.service.IEntityService;
import com.youxigu.dynasty2.hero.service.IHeroService;
import com.youxigu.dynasty2.log.imp.LogHeroAct;
import com.youxigu.dynasty2.log.imp.LogItemAct;
import com.youxigu.dynasty2.treasury.domain.Treasury;
import com.youxigu.dynasty2.treasury.service.ITreasuryService;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.user.service.IUserService;
import com.youxigu.dynasty2.util.BaseException;

/**
 * 执行掉落包的掉落效果
 * 
 * @author Administrator
 * 
 */
public class DropPackEntityRender implements IEntityEffectRender {

	public static final Logger log = LoggerFactory
			.getLogger(DropPackEntityRender.class);

	private int maxPercent = 10000001;
	private IUserService userService;
	private IEntityService entityService;
	private ITreasuryService treasuryService;
	private IHeroService heroService;
	private ICastleResService castleResService;

	public void setTreasuryService(ITreasuryService treasuryService) {
		this.treasuryService = treasuryService;
	}

	public void setEntityService(IEntityService entityService) {
		this.entityService = entityService;
	}

	public void setMaxPercent(int maxPercent) {
		this.maxPercent = maxPercent;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public void setHeroService(IHeroService heroService) {
		this.heroService = heroService;
	}

	public void setCastleResService(ICastleResService castleResService) {
		this.castleResService = castleResService;
	}

	@Override
	public Map<String, Object> render(Entity entity, Map<String, Object> context) {
		Map<String, Object> map = new HashMap<String, Object>();
		// DropPackEntity dropPack = (DropPackEntity) entity;
		User user = (User) context.get("user");
		if (user == null) {
			Object userId = context.get("userId");
			if (userId != null) {
				user = userService.getUserById((Long) userId);
				if (user == null) {
					throw new BaseException("缺少userId或者user参数");
				}
			}
		}
		Object action = context.get("iAction");
		LogItemAct act = com.youxigu.dynasty2.log.imp.LogItemAct.LOGITEMACT_35;
		if (action != null && action instanceof LogItemAct) {
			act = (LogItemAct) action;
		}

		// 使用的数量
		int useNum = 1;
		Object tmp1 = context.get("num");
		if (tmp1 != null) {
			useNum = (Integer) tmp1;
		}

		// 处理消耗：掉落包使用可能要消耗别的东西才能使用，比如金箱子使用会消耗金钥匙
		List<EntityConsume> consumes = entity.getConsumes();
		if (consumes != null && consumes.size() > 0) {
			Map<String, Object> params1 = new HashMap<String, Object>(5);
			// 更新资源消耗
			params1.put("num", useNum);
			params1.put("user", user);
			params1.put("iAction", action);
			entityService.updateByEntityConsume(consumes, params1);

		}

		// /要过滤掉的掉落包中的entId
		@SuppressWarnings("unchecked")
		Map<Integer, Object> excludes = (Map<Integer, Object>) context
				.get("excludes");

		Map<Integer, DroppedEntity> result = excuteDropPack(entity, user,
				excludes);

		for (int i = 1; i < useNum; i++) {
			Map<Integer, DroppedEntity> tmp = excuteDropPack(entity, user,
					excludes);

			for (DroppedEntity entity1 : tmp.values()) {
				int entId = entity1.getEntId();
				DroppedEntity old = result.get(entId);
				if (old != null) {
					old.setNum(old.getNum() + entity1.getNum());
				} else {
					result.put(entId, entity1);
				}
			}
		}

		if (!context.containsKey("notUpdate")) {// notUpdate
			// ：实体不直接更新数据，返回给外部程序自行处理
			Treasury treasury = (Treasury) context.get("treasury");
			int isGift = treasury != null ? treasury.getIsGift() : 1;

			// 对于非道具类的item;如经验等，直接只用
			Iterator<DroppedEntity> lit = result.values().iterator();
			while (lit.hasNext()) {
				DroppedEntity item = lit.next();
				// if (useNum > 1) {
				// item.setNum(item.getNum() * useNum);
				// }
				Entity tmp = entityService.getEntity(item.getEntId());
				if (tmp instanceof Item) {
					// 添加到背包
					treasuryService.addItemToTreasury(user.getUserId(),
							item.getEntId(), item.getNum(), isGift, -1, false,
							true, act);
				} else if (tmp instanceof SysHero) {
					//掉落武将
					for (int i = 0; i < item.getNum(); i++) {
						heroService.doCreateAHero(user.getUserId(), tmp
								.getEntId(),LogHeroAct.Drop_Hero_ADD);
					}
				} else if (tmp instanceof Resource) {
					//掉落资源
					int entId = tmp.getEntId();
					int num = item.getNum();
					if (num < 0 || num > Integer.MAX_VALUE) {
						throw new BaseException("使用的资源不能大于21亿");
					}
					if (num != 0) {
						castleResService.doAddRes(user.getMainCastleId(), entId, num, true);
					}
				}else if (tmp instanceof Party) {
					// 直接使用,通常是party类的：武将经验，武将功勋，君主经验，点券等等
					context.put("num", item.getNum());
					Map<String, Object> usedItem = entityService.doAction(tmp,
							Entity.ACTION_USE, context);
					if (usedItem != null) {
						map.putAll(usedItem);
					}
				} else {
					throw new BaseException("目前不支持该种物品掉落");
				}
			}
		}

		map.put("items", result);
		return map;

	}

	private Map<Integer, DroppedEntity> excuteDropPack(Entity dropPack,
			User user, Map<Integer, Object> excludes) {
		List<DropPackItem> items = null;
		if (dropPack instanceof DropPackEntity) {
			items = ((DropPackEntity) dropPack).getItems();
		} else if (dropPack instanceof BoxEntity) {
			items = ((BoxEntity) dropPack).getItems();
		}

		Map<Integer, DroppedEntity> droppedItems = new HashMap<Integer, DroppedEntity>();
		// List<DropPackItem> droppedItems = new ArrayList<DropPackItem>();
		List<DropPackItem> weightItems = new ArrayList<DropPackItem>();
		int sumWeight = 0;

		if (items == null) {
			throw new BaseException("掉落包内容不存在。");
		}

		for (DropPackItem item : items) {
			int entId = item.getEntId();
			if (excludes != null && excludes.containsKey(item.getDropEntId())) {
				continue;
			}
			if (item.getMaxValue2() <= 0 || item.getMaxValue2() <= 0) {
				continue;
			}
			if (item.getWeight() <= 0) {// 没有权重，直接算掉率
				if (item.getDropPercent() >= Util.randInt(maxPercent)) {// 符合掉率
					Entity tmp = entityService.getEntity(item.getDropEntId());
					if (tmp == null) {
						log.error("掉落包:{}配置错误，掉落的实体不存在：{}", entId,
								item.getDropEntId());
						continue;
					}
					int num = getRandomNum(item.getMaxValue2(),
							item.getMinValue());
					if (num > 0) {
						if (tmp instanceof DropPackEntity
								&& ((DropPackEntity) tmp).getChildType() != Item.ITEM_TYPE_BOX_DROP) {// 掉落的是子掉落包
							Map<Integer, DroppedEntity> subResult = excuteDropPack(
									(DropPackEntity) tmp, user, null);
							if (subResult != null && subResult.size() > 0) {

								Iterator<DroppedEntity> lit = subResult
										.values().iterator();
								while (lit.hasNext()) {
									DroppedEntity sub = lit.next();
									addToDroppedItemMap(sub, num, droppedItems);
								}
							}
						} else {
							DroppedEntity droppedEntity = new DroppedEntity(
									item.getDropEntId(), num);
							addToDroppedItemMap(droppedEntity, 1, droppedItems);
						}
					}
				}
			} else {// 计算总权重值，排序掉落权重
				sumWeight += item.getWeight();
				weightItems.add(item);
			}
		}

		if (weightItems.size() > 0) {// 处理权重掉落
			// n选1
			DropPackItem item = getDropPack(weightItems, sumWeight);
			if (item != null) {
				Entity tmp = entityService.getEntity(item.getDropEntId());
				if (tmp == null) {
					log.error("掉落包:{}配置错误，掉落的实体不存在：{}", item.getEntId(),
							item.getDropEntId());

				} else {
					int num = getRandomNum(item.getMaxValue2(),
							item.getMinValue());
					if (num > 0) {
						if (tmp instanceof DropPackEntity
								&& ((DropPackEntity) tmp).getChildType() != Item.ITEM_TYPE_BOX_DROP) {// 掉落的是子掉落包
							Map<Integer, DroppedEntity> subResult = excuteDropPack(
									(DropPackEntity) tmp, user, null);

							if (subResult != null && subResult.size() > 0) {
								Iterator<DroppedEntity> lit = subResult
										.values().iterator();
								while (lit.hasNext()) {
									DroppedEntity sub = lit.next();
									addToDroppedItemMap(sub, num, droppedItems);
								}
							}
						} else {
							DroppedEntity droppedEntity = new DroppedEntity(
									item.getDropEntId(), num);
							addToDroppedItemMap(droppedEntity, 1, droppedItems);
						}
					}
				}

			}
		}
		return droppedItems;
	}

	private void addToDroppedItemMap(DroppedEntity entity, int num,
			Map<Integer, DroppedEntity> droppedItems) {

		DroppedEntity droppedEntity = droppedItems.get(entity.getEntId());
		if (droppedEntity == null) {
			entity.setNum(entity.getNum() * num);
			droppedItems.put(entity.getEntId(), entity);
		} else {
			droppedEntity
					.setNum(droppedEntity.getNum() + entity.getNum() * num);
		}
	}

	private int getRandomNum(int max, int min) {
		return max > min ? Util.randInt(max - min + 1) + min : min;
	}

	private DropPackItem getDropPack(List<DropPackItem> weigthList,
			int sumWeight) {
		int currWeight = Util.randInt(sumWeight) + 1;// 随机得出一个总权重范围内的权重
		int stepWeight = 0;
		for (int i = 0; i < weigthList.size(); i++) {
			// 遍历，得到符合随机权重的
			DropPackItem temp = weigthList.get(i);
			stepWeight += temp.getWeight();
			if (currWeight <= stepWeight) {
				return temp;
			}
		}
		return null;
	}

}

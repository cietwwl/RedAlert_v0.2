package com.youxigu.dynasty2.activity.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.youxigu.dynasty2.activity.dao.IActivityDao;
import com.youxigu.dynasty2.activity.domain.OperateActivity;
import com.youxigu.dynasty2.activity.domain.UserOperateActivity;
import com.youxigu.dynasty2.activity.proto.ActivityView;
import com.youxigu.dynasty2.activity.proto.DefaultActivityView;
import com.youxigu.dynasty2.activity.service.IOperateActivityProcessor;
import com.youxigu.dynasty2.activity.service.IOperateActivityService;
import com.youxigu.dynasty2.chat.client.IChatClientService;
import com.youxigu.dynasty2.entity.domain.DropPackEntity;
import com.youxigu.dynasty2.entity.domain.DroppedEntity;
import com.youxigu.dynasty2.entity.domain.Entity;
import com.youxigu.dynasty2.entity.domain.Item;
import com.youxigu.dynasty2.entity.service.impl.EntityService;
import com.youxigu.dynasty2.log.imp.LogItemAct;
import com.youxigu.dynasty2.treasury.service.impl.TreasuryService;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.user.service.IUserService;
import com.youxigu.dynasty2.util.BaseException;

public abstract class AOperateActivityProcessor implements
		IOperateActivityProcessor {
	protected IUserService userService;
	protected IActivityDao activityDao;
	protected IOperateActivityService operateActivityService;
	protected EntityService entityService;
	protected TreasuryService treasuryService;
	protected IChatClientService messageService;
	protected static Calendar calendar = Calendar.getInstance();

	// private DateFormat df = new SimpleDateFormat("yyyy年MM月dd日HH时");
	// private String format(Date date) {
	// return df.format(date);
	// }

	@Override
	public ActivityView doGetActById(long userId, OperateActivity activity) {
		ActivityView act = doGetActByIdImp(userId, activity);
		if (act == null) {
			act = new DefaultActivityView();
		}
		act.setActId(activity.getActId());
		act.setType(activity.getType());
		act.setActName(activity.getActName());
		act.setEndTime(activity.getEndTime().getTime());
		act.setStartTime(activity.getStartTime().getTime());
		long cur = System.currentTimeMillis();
		long leaveTime = activity.getEndTime().getTime() - cur;
		if (leaveTime < 0) {
			act.setEnd(0);
		} else {
			act.setEnd(leaveTime / 1000L);
		}
		return act;
	}

	protected abstract ActivityView doGetActByIdImp(long userId,
			OperateActivity activity);

	@Override
	public ActivityView doReward(long userId, OperateActivity activity) {
		if (activity.isAutoReward()) {
			throw new BaseException("不能手动领奖");
		}
		doRewardImp(userId, activity);
		return doGetActById(userId, activity);
	}

	@Override
	public void doAutoReward(OperateActivity activity) {
		if (!activity.isAutoReward()) {
			throw new BaseException("不能自动发奖");
		}
		doAutoRewardImp(activity);
	}

	protected abstract void doRewardImp(long userId, OperateActivity activity);

	protected abstract void doAutoRewardImp(OperateActivity activity);

	protected void toItemMaps(int itemId, int num, Map<String, Object> itemMap) {
		// Entity entity = entityService.getEntity(itemId);
		// if (entity != null) {
		// if (entity instanceof Item) {
		// itemMap.putAll(((Item) entity).toMap());
		// }
		// }
	}

	@SuppressWarnings("unchecked")
	protected List<Map<String, Object>> dropItem(long userId, int dropEntId,
			int num, LogItemAct reason, boolean isOpen) {
		List<Map<String, Object>> dropItemDatas = null;
		if (dropEntId != 0) {
			Entity entity = entityService.getEntity(dropEntId);
			User user = userService.getUserById(userId);
			if (entity != null) {
				if (isOpen && (entity instanceof DropPackEntity)
				// && ((DropPackEntity) entity).getChildType() !=
				// Item.ITEM_TYPE_BOX_DROP) {
				) {
					Map<String, Object> params1 = new HashMap<String, Object>();
					params1.put("user", user);
					params1.put("iAction", reason);
					if (num > 1) {
						params1.put("num", num);
					}
					Map<String, Object> dropItems = entityService.doAction(
							entity, Entity.ACTION_USE, params1);
					Map<Integer, DroppedEntity> datas = (Map<Integer, DroppedEntity>) dropItems
							.get("items");
					dropItemDatas = new ArrayList<Map<String, Object>>(
							dropItems.size());

					Iterator<DroppedEntity> lit = datas.values().iterator();
					Item broadCastItem = null;
					// DroppedEntity broadDroppedEntity = null;
					while (lit.hasNext()) {
						DroppedEntity dropEntity = lit.next();
						Map<String, Object> map = new HashMap<String, Object>(2);
						map.put("itemId", dropEntity.getEntId());
						map.put("num", dropEntity.getNum());
						dropItemDatas.add(map);
						Entity e = entityService.getEntity(dropEntity
								.getEntId());
						if (e instanceof Item) {
							Item item = (Item) e;
							int color = item.getColor();
							if (color > -1) {
								if (broadCastItem == null) {
									broadCastItem = item;
									// broadDroppedEntity = dropEntity;
								} else {
									if (color > broadCastItem.getColor()) {
										broadCastItem = item;
										// broadDroppedEntity = dropEntity;
									}
								}
							}
						}
					}
				} else {
					treasuryService.addItemToTreasury(userId, dropEntId, num,
							1, -1, false, true, reason);
					// throw new BaseException("充值礼包奖励必须是可直接打开的掉落包");
				}
			} else {
				throw new BaseException("充值掉落包没配置！");
			}
		}
		return dropItemDatas;
	}

	protected String getStatus(UserOperateActivity operateActivity, long actId) {
		String status = "";
		if (operateActivity != null) {
			// 如果玩家操作的活动不是 玩家身上
			if (operateActivity.getActId() != actId) {
				OperateActivity activity = operateActivityService
						.getOperateActivity(operateActivity.getActId());
				// 如果活动还没有结束，禁止操作
				if (activity != null
						&& System.currentTimeMillis() < activity.getMaxTime()
								.getTime()) {
					throw new BaseException("当前活动还没有结束，不能进行下一个的操作");
				}
				// 活动已经结束，重置当前状态
				status = "";
			} else {
				status = operateActivity.getStatus() == null ? ""
						: operateActivity.getStatus();
			}

		}
		return status;
	}

	protected static long getMiddleOfNight(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date.getTime());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTimeInMillis();
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public void setActivityDao(IActivityDao activityDao) {
		this.activityDao = activityDao;
	}

	public void setOperateActivityService(
			IOperateActivityService operateActivityService) {
		this.operateActivityService = operateActivityService;
	}

	public void setEntityService(EntityService entityService) {
		this.entityService = entityService;
	}

	public void setTreasuryService(TreasuryService treasuryService) {
		this.treasuryService = treasuryService;
	}

	public void setMessageService(IChatClientService messageService) {
		this.messageService = messageService;
	}

}

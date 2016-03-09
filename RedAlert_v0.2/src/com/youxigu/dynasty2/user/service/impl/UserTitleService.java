package com.youxigu.dynasty2.user.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.youxigu.dynasty2.chat.EventMessage;
import com.youxigu.dynasty2.chat.client.IChatClientService;
import com.youxigu.dynasty2.core.event.Event;
import com.youxigu.dynasty2.core.event.EventDispatcher;
import com.youxigu.dynasty2.core.event.EventTypeConstants;
import com.youxigu.dynasty2.core.event.IEventListener;
import com.youxigu.dynasty2.entity.domain.DropPackEntity;
import com.youxigu.dynasty2.entity.domain.DroppedEntity;
import com.youxigu.dynasty2.entity.domain.Entity;
import com.youxigu.dynasty2.entity.domain.Item;
import com.youxigu.dynasty2.entity.domain.ItemProperty;
import com.youxigu.dynasty2.entity.service.IEntityService;
import com.youxigu.dynasty2.hero.domain.Hero;
import com.youxigu.dynasty2.log.ILogService;
import com.youxigu.dynasty2.log.LogBeanFactory;
import com.youxigu.dynasty2.log.imp.JunGongAct;
import com.youxigu.dynasty2.log.imp.LogItemAct;
import com.youxigu.dynasty2.treasury.service.ITreasuryService;
import com.youxigu.dynasty2.user.dao.IUserTitleDao;
import com.youxigu.dynasty2.user.domain.AchieveType;
import com.youxigu.dynasty2.user.domain.Title;
import com.youxigu.dynasty2.user.domain.TitleAward;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.user.proto.UserTitleEvent;
import com.youxigu.dynasty2.user.service.IUserAchieveService;
import com.youxigu.dynasty2.user.service.IUserService;
import com.youxigu.dynasty2.user.service.IUserTitleService;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.dynasty2.util.EffectValue;

public class UserTitleService implements IUserTitleService, IEventListener {
	public static final Logger log = LoggerFactory
			.getLogger(UserTitleService.class);
	private IUserTitleDao userTitleDao;
	private IUserService userService;
	private IEntityService entityService;
	private ITreasuryService treasuryService;
	private IChatClientService messageService;
	private IUserAchieveService userAchieveService;
	private ILogService logService;

	public Map<Integer, Title> titleMap = new HashMap<Integer, Title>();// 军衔id-军衔
	public Map<Integer, TitleAward> titleAwardMap = new HashMap<Integer, TitleAward>();// 奖励id-军衔奖励

	public void setUserTitleDao(IUserTitleDao userTitleDao) {
		this.userTitleDao = userTitleDao;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public void setEntityService(IEntityService entityService) {
		this.entityService = entityService;
	}

	public void setTreasuryService(ITreasuryService treasuryService) {
		this.treasuryService = treasuryService;
	}

	public void setMessageService(IChatClientService messageService) {
		this.messageService = messageService;
	}

	public void setUserAchieveService(IUserAchieveService userAchieveService) {
		this.userAchieveService = userAchieveService;
	}

	public void setLogService(ILogService logService) {
		this.logService = logService;
	}

	/**
	 * 初始化
	 */
	public void initService() {
		log.info("加载军衔配置");

		// 注册事件
		EventDispatcher.registerListener(
				EventTypeConstants.EVT_USER_JUNGONG_ADD, this);

		List<TitleAward> titleAwards = userTitleDao.listTitleAwards();
		if (titleAwards != null && titleAwards.size() > 0) {
			for (TitleAward titleAward : titleAwards) {
				titleAwardMap.put(titleAward.getAwardId(), titleAward);
			}
		}

		List<Title> titles = userTitleDao.listTitles();
		if (titles != null && titles.size() > 0) {
			for (Title title : titles) {
				// 设置军衔父子关系
				if (title.getParentId() > 0) {
					Title parent = titleMap.get(title.getParentId());
					if (parent != null) {
						parent.setChild(title);
						title.setParent(parent);
					}
				}
				int[] awards = new int[5];
				awards[0] = title.getAward1();
				awards[1] = title.getAward2();
				awards[2] = title.getAward3();
				awards[3] = title.getAward4();
				awards[4] = title.getAward5();
				title.setAwards(awards);

				// 初始化军衔勋章父子关系
				TitleAward award1 = null;
				TitleAward award2 = null;
				TitleAward award3 = null;
				TitleAward award4 = null;
				TitleAward award5 = null;
				if (title.getAward1() > 0) {
					award1 = titleAwardMap.get(title.getAward1());
				}

				if (title.getAward2() > 0) {
					award2 = titleAwardMap.get(title.getAward2());
				}

				if (title.getAward3() > 0) {
					award3 = titleAwardMap.get(title.getAward3());
				}

				if (title.getAward4() > 0) {
					award4 = titleAwardMap.get(title.getAward4());
				}

				if (title.getAward5() > 0) {
					award5 = titleAwardMap.get(title.getAward5());
				}

				if (award1 != null && award2 != null && award3 != null
						&& award4 != null && award5 != null) {
					award1.setChildAward(award2);
					award2.setParentAward(award1);
					award2.setChildAward(award3);
					award3.setParentAward(award2);
					award3.setChildAward(award4);
					award4.setParentAward(award3);
					award4.setChildAward(award5);
					award5.setParentAward(award4);

					// 初始化勋章所属的军衔
					award1.setParent(title);
					award2.setParent(title);
					award3.setParent(title);
					award4.setParent(title);
					award5.setParent(title);

					// 解析勋章属性加成
					List<ItemProperty> list = new ArrayList<ItemProperty>();
					if (award1.getAwardType()
							.equals(TitleAward.AWARDTYPE_HERO_PRO)) {
						List<ItemProperty> tmp = ItemProperty
								.parseProperty(award1.getAwardDetail());
						if (tmp != null && tmp.size() > 0) {
							list.addAll(tmp);

							// 为了计算效果时，可以吧没有激活的删掉
							award1.setHeroProp(tmp);
						}
					}

					if (award2.getAwardType()
							.equals(TitleAward.AWARDTYPE_HERO_PRO)) {
						List<ItemProperty> tmp = ItemProperty
								.parseProperty(award2.getAwardDetail());
						if (tmp != null && tmp.size() > 0) {
							list.addAll(tmp);
							award2.setHeroProp(tmp);
						}
					}

					if (award3.getAwardType()
							.equals(TitleAward.AWARDTYPE_HERO_PRO)) {
						List<ItemProperty> tmp = ItemProperty
								.parseProperty(award3.getAwardDetail());
						if (tmp != null && tmp.size() > 0) {
							list.addAll(tmp);
							award3.setHeroProp(tmp);
						}
					}

					if (award4.getAwardType()
							.equals(TitleAward.AWARDTYPE_HERO_PRO)) {
						List<ItemProperty> tmp = ItemProperty
								.parseProperty(award4.getAwardDetail());
						if (tmp != null && tmp.size() > 0) {
							list.addAll(tmp);
							award4.setHeroProp(tmp);
						}
					}

					if (award5.getAwardType()
							.equals(TitleAward.AWARDTYPE_HERO_PRO)) {
						List<ItemProperty> tmp = ItemProperty
								.parseProperty(award5.getAwardDetail());
						if (tmp != null && tmp.size() > 0) {
							list.addAll(tmp);
							award5.setHeroProp(tmp);
						}
					}

					// 初始化当前等级军衔的属性加成
					if (list != null && list.size() > 0) {
						for (ItemProperty itemProperty : list) {
							if (itemProperty.abs) {
								title.addAbsBonus(itemProperty.getPropName(),
										itemProperty.getValue());
							} else {
								title.addPercentBonus(
										itemProperty.getPropName(),
										itemProperty.getValue());
							}
						}
					}
				}

				titleMap.put(title.getTitleId(), title);
			}
		}

		// 合并1~n等级军衔的属性加成
		if (titleMap != null && titleMap.size() > 0) {
			Iterator<Map.Entry<Integer, Title>> itl = titleMap.entrySet()
					.iterator();
			while (itl.hasNext()) {
				Title t = itl.next().getValue();
				Title pt = t.getParent();
				while (pt != null) {
					// 累计绝对值加成
					Map<String, Integer> absBonus = pt.getAbsBonus();
					if (absBonus != null && absBonus.size() > 0) {
						Iterator<Map.Entry<String, Integer>> subItl = absBonus
								.entrySet().iterator();
						while (subItl.hasNext()) {
							Map.Entry<String, Integer> ent = subItl.next();
							String key = ent.getKey();
							int bonus = ent.getValue();
							t.addAbsBonus(key, bonus);
						}
					}

					// 累计百分比加成
					Map<String, Integer> percentBonus = pt.getPercentBonus();
					if (percentBonus != null && percentBonus.size() > 0) {
						Iterator<Map.Entry<String, Integer>> subItl = percentBonus
								.entrySet().iterator();
						while (subItl.hasNext()) {
							Map.Entry<String, Integer> ent = subItl.next();
							String key = ent.getKey();
							int bonus = ent.getValue();
							t.addPercentBonus(key, bonus);
						}
					}

					pt = pt.getParent();
				}
			}
		}
	}

	@Override
	public Title getTitle(int titleId) {
		return titleMap.get(titleId);
	}

	@Override
	public TitleAward getTitleAward(int awardId) {
		return titleAwardMap.get(awardId);
	}

	@Override
	public Map<String, Object> doActiveMedal(long userId, int awardId) {
		// 加锁
		User user = userService.lockGetUser(userId);
		Title curTitle = this.getTitle(user.getTitle());
		int junGong = user.getJunGong();
		if (curTitle == null) {
			throw new BaseException("军衔不存在");
		}

		if (awardId <= 0) {
			awardId = curTitle.getAward1();
		}
		TitleAward titleAward = getTitleAward(awardId);
		if (titleAward == null || titleAward.getParent()
				.getTitleId() != curTitle.getTitleId()) {
			throw new BaseException("勋章不存在");
		}

		if (user.getTitleAwardId() == awardId) {
			throw new BaseException("勋章已经领取");
		}

		if (titleAward.getParentAward() != null && titleAward.getParentAward()
				.getAwardId() != user.getTitleAwardId()) {
			throw new BaseException("前置勋章尚未领取");
		}

		int consumeJunGong = titleAward.getJunGong();
		if (consumeJunGong > 0) {
			if (consumeJunGong > junGong) {
				throw new BaseException("军功不足，无法领取");
			}
			// 消耗军功
			userService.doCostjunGong(userId, consumeJunGong,
					JunGongAct.TYPE_XUNZHANG);
		}

		Map<String, Object> result = new HashMap<String, Object>(3);

		if (titleAward.getAwardType().equals(TitleAward.AWARDTYPE_COLOR)) {
			// 提升品质
			int color = Integer.parseInt(titleAward.getAwardDetail().trim());
			if (user.getColor() < color) {
				user.setColor(color);
				result.put("color", color);
			}
		} else if (titleAward.getAwardType()
				.equals(TitleAward.AWARDTYPE_DROPITEM)) {
			// 掉落包奖励
			int dorpId = Integer.parseInt(titleAward.getAwardDetail().trim());
			this.dealDrop(user, dorpId, 1, LogItemAct.LOGITEMACT_160);
		} else if (titleAward.getAwardType()
				.equals(TitleAward.AWARDTYPE_HERO_PRO)) {
		}
		user.setTitleAwardId(awardId);
		result.put("awardId", awardId);

		// 军衔是否升级
		if (titleAward.getChildAward() == null) {
			// 没有后续军衔了，可以升级了
			Title nextTitle = curTitle.getChild();
			if (nextTitle != null) {
				user.setTitle(nextTitle.getTitleId());
				user.setTitleAwardId(0);
				result.put("upTitleId", nextTitle.getTitleId());

				// 成就
				userAchieveService.doNotifyAchieveModule(user.getUserId(),
						AchieveType.TYPE_MILITARY,
						AchieveType.TYPE_MILITARY_ENTID6,
						nextTitle.getTitleId());
			}

			// 自动发放晋升奖励
			if (curTitle.getEntId() > 0) {
				List<DroppedEntity> dropList = this.dealDrop(user,
						curTitle.getEntId(), 1, LogItemAct.LOGITEMACT_160);
				result.put("upItemAward", dropList);
			}
		}
		userService.doUpdateUser(user);

		// 军衔日志
		logService.log(LogBeanFactory.buildTitleLog(user, curTitle.getTitleId(),
				titleAward.getAwardId(), titleAward.getJunGong(),
				titleAward.getAwardType(), titleAward.getAwardDetail(),
				user.getTitle()));
		return result;
	}

	/**
	 * 处理勋章和晋升奖励
	 * 
	 * @param user
	 * @param dorpId
	 * @param num
	 * @param logItemAct
	 * @return
	 */
	private List<DroppedEntity> dealDrop(User user, int dorpId, int num,
			LogItemAct logItemAct) {
		List<DroppedEntity> dropList = new ArrayList<DroppedEntity>();
		Entity entity = entityService.getEntity(dorpId);
		if (entity != null) {
			if (entity instanceof DropPackEntity && ((DropPackEntity) entity)
					.getChildType() != Item.ITEM_TYPE_BOX_DROP) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("user", user);
				map.put("iAction", logItemAct);
				map.put("num", num);// TODO 这个地方是否需要合并次数
				Map<String, Object> dropItems = entityService.doAction(entity,
						Entity.ACTION_USE, map);
				if (dropItems != null && dropItems.size() > 0) {
					Map<Integer, DroppedEntity> datas = (Map<Integer, DroppedEntity>) dropItems
							.get("items");
					if (datas != null) {
						Collection<DroppedEntity> dropedEntitys = datas
								.values();
						if (dropedEntitys != null && dropedEntitys.size() > 0) {
							Iterator<DroppedEntity> itl = dropedEntitys
									.iterator();
							while (itl.hasNext()) {
								DroppedEntity drop = itl.next();
								dropList.add(drop);
							}
						}
					}
				}

			} else {
				// 直接掉道具
				treasuryService.addItemToTreasury(user.getUserId(), dorpId, 1,
						1, -1, false, true, logItemAct);
				dropList.add(new DroppedEntity(dorpId, 1));
			}
		} else {
			throw new BaseException("掉落包不存在");
		}
		return dropList;
	}

	@Override
	public Map<String, Object> showTitleInfo(long userId, int titleId) {
		User user = userService.getUserById(userId);
		if (titleId <= 0) {
			titleId = user.getTitle();
		}
		Title title = this.getTitle(titleId);
		if (title == null) {
			throw new BaseException("军衔不存在");
		}

		// 下一个开启的勋章
		TitleAward nextAward = null;
		if (user.getTitleAwardId() == 0) {
			nextAward = this.getTitleAward(title.getAward1());
		} else {
			nextAward = this.getTitleAward(user.getTitleAwardId())
					.getChildAward();
		}
		int nextId = 0;
		if (nextAward != null) {
			nextId = nextAward.getAwardId();
		}

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		int[] awards = title.getAwards();
		for (int awardId : awards) {
			Map<String, Object> map = new HashMap<String, Object>(2);
			map.put("awardId", awardId);
			int status = 4;// 高级别的-默认锁定
			if (titleId < user.getTitle()) {
				// 低级别的军衔
				status = 1;// 已经领取了
			} else if (titleId == user.getTitle()) {
				// 当前军衔
				if (awardId <= user.getTitleAwardId()) {
					status = 1;// 已经领取了
				} else {
					if (nextId == awardId) {
						if (nextAward != null
								&& nextAward.getJunGong() > user.getJunGong()) {
							status = 3;// 条件不满足
						} else {
							status = 2;// 可领取
						}
					}
				}
			}
			map.put("status", status);
			list.add(map);
		}

		Map<String, Object> result = new HashMap<String, Object>(2);
		result.put("list", list);
		result.put("junGong", user.getJunGong());
		return result;
	}

	@Override
	public Map<String, EffectValue> getTitleEffectValue(Hero hero) {
		Map<String, EffectValue> effect = new HashMap<String, EffectValue>();
		if (hero.isCommander()) {
			User user = userService.getUserById(hero.getUserId());
			if (user != null && user.getTitle() > 0) {
				Title title = this.getTitle(user.getTitle());
				// 已经领取的勋章id
				int awardId = user.getTitleAwardId();
				if (title != null) {
					// 绝对值加成
					Map<String, Integer> absBonus = title.getAbsBonus();
					if (absBonus != null && absBonus.size() > 0) {
						Iterator<Map.Entry<String, Integer>> subItl = absBonus
								.entrySet().iterator();
						while (subItl.hasNext()) {
							Map.Entry<String, Integer> ent = subItl.next();
							String key = ent.getKey();
							int bonus = ent.getValue();
							EffectValue effectValue = effect.get(key);
							if (effectValue == null) {
								effectValue = new EffectValue(0, 0);
								effect.put(key, effectValue);
							}
							effectValue.setAbsValue(
									effectValue.getAbsValue() + bonus);
						}
					}

					// 累计千分比加成
					Map<String, Integer> percentBonus = title.getPercentBonus();
					if (percentBonus != null && percentBonus.size() > 0) {
						Iterator<Map.Entry<String, Integer>> subItl = percentBonus
								.entrySet().iterator();
						while (subItl.hasNext()) {
							Map.Entry<String, Integer> ent = subItl.next();
							String key = ent.getKey();
							int bonus = ent.getValue();
							EffectValue effectValue = effect.get(key);
							if (effectValue == null) {
								effectValue = new EffectValue(0, 0);
								effect.put(key, effectValue);
							}
							effectValue.setPerValue(
									effectValue.getPerValue() + bonus);
						}
					}

					// 去掉没有激活的勋章属性
					TitleAward childAward = null;
					if (awardId <= 0) {
						childAward = this.getTitleAward(title.getAward1());
					} else {
						childAward = this.getTitleAward(awardId)
								.getChildAward();
					}
					while (childAward != null) {
						List<ItemProperty> heroProp = childAward.getHeroProp();
						if (heroProp != null && heroProp.size() > 0) {
							for (ItemProperty itemProperty : heroProp) {
								EffectValue effectValue = effect
										.get(itemProperty.getPropName());
								if (effectValue != null) {
									if (itemProperty.abs) {
										effectValue.setAbsValue(Math.max(0,
												(effectValue.getAbsValue()
														- itemProperty
																.getAbsValue())));
										effectValue.setPerValue(Math.max(0,
												(effectValue.getPerValue()
														- itemProperty
																.getPercentValue())));
									}
								}
							}
						}
						childAward = childAward.getChildAward();
					}
				}
			}
		}
		return effect;
	}

	@Override
	public void doEvent(Event event) {
		if (event.getEventType() == EventTypeConstants.EVT_USER_JUNGONG_ADD) {
			Map<String, Object> params = (Map<String, Object>) event
					.getParams();
			User user = (User) params.get("user");
			boolean isRed = this.cheakRedPoint(user);
			if (isRed) {
				// TODO 推送小红点
				int eventId = EventMessage.TYPE_TITLE_REDPOINT;
				messageService.sendEventMessage(user.getUserId(), eventId,
						new UserTitleEvent(eventId, user.getTitle()));
			}
		}
	}

	@Override
	public Map<String, Object> getRedPoint(long userId) {
		User user = userService.getUserById(userId);
		boolean isRed = this.cheakRedPoint(user);
		Map<String, Object> result = new HashMap<String, Object>(2);
		result.put("isRed", isRed);
		result.put("titleId", user.getTitle());
		return result;
	}

	/**
	 * 检查是否有小红点
	 * 
	 * @param user
	 * @return
	 */
	private boolean cheakRedPoint(User user) {
		int junGong = user.getJunGong();
		Title title = this.getTitle(user.getTitle());

		boolean isRed = false;
		if (title != null) {
			TitleAward nextAward = null;
			if (user.getTitleAwardId() == 0) {
				nextAward = this.getTitleAward(title.getAward1());
			} else {
				nextAward = this.getTitleAward(user.getTitleAwardId())
						.getChildAward();
			}

			if (nextAward != null && nextAward.getJunGong() <= junGong) {
				isRed = true;
			}
		}
		return isRed;
	}
}

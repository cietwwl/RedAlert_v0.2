package com.youxigu.dynasty2.treasury.domain;

import java.io.Serializable;
import java.util.List;

import com.google.protobuf.Message;
import com.manu.core.ServiceLocator;
import com.youxigu.dynasty2.chat.EventMessage;
import com.youxigu.dynasty2.chat.ISendMessage;
import com.youxigu.dynasty2.chat.proto.CommonHead.AttrProperty;
import com.youxigu.dynasty2.chat.proto.CommonHead.EquipInfo;
import com.youxigu.dynasty2.chat.proto.CommonHead.ResponseHead;
import com.youxigu.dynasty2.common.AppConstants;
import com.youxigu.dynasty2.entity.domain.Entity;
import com.youxigu.dynasty2.entity.domain.Item;
import com.youxigu.dynasty2.entity.domain.ItemProperty;
import com.youxigu.dynasty2.entity.service.IEntityService;
import com.youxigu.dynasty2.treasury.proto.TreasuryMsg;

/**
 * 背包单个道具缓存内容
 * 
 * @author Dagangzi
 *
 */
public class TreasuryMessage implements ISendMessage, Serializable {
	private static final long serialVersionUID = 2556139651495702519L;
	private long id;
	private long userId;
	private int entId;
	private int itemCount;
	private long equip;
	private int level;
	private int isLock;
	private OnlinePack onlinePack;// 如果是运营个礼包有该值
	private String randomProp;// 目前保存的是装备打造出来的属性
	private String specialAttr = "";// 里面保存的是不会变的属性

	public TreasuryMessage() {
	}

	public TreasuryMessage(Treasury treasury, boolean owner) {
		this.id = treasury.getId();
		this.userId = treasury.getUserId();
		this.entId = treasury.getEntId();
		this.itemCount = treasury.getItemCount();
		this.equip = treasury.getEquip();
		this.level = treasury.getLevel();
		this.isLock = treasury.getIsLock();
		if (entId >= AppConstants.ENT_DYNAMIC_ID_MIN
				&& entId <= AppConstants.ENT_DYNAMIC_ID_MAX) {
			this.onlinePack = treasury.getItem().toOnlinePack();
		}
		this.randomProp = treasury.getRandomProp();
		this.specialAttr = treasury.getSpecialAttr();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getEntId() {
		return entId;
	}

	public void setEntId(int entId) {
		this.entId = entId;
	}

	public int getItemCount() {
		return itemCount;
	}

	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}

	public long getEquip() {
		return equip;
	}

	public void setEquip(long equip) {
		this.equip = equip;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getIsLock() {
		return isLock;
	}

	public void setIsLock(int isLock) {
		this.isLock = isLock;
	}

	public OnlinePack getOnlinePack() {
		return onlinePack;
	}

	public void setOnlinePack(OnlinePack onlinePack) {
		this.onlinePack = onlinePack;
	}

	public String getRandomProp() {
		return randomProp;
	}

	public void setRandomProp(String randomProp) {
		this.randomProp = randomProp;
	}

	public String getSpecialAttr() {
		return specialAttr;
	}

	public void setSpecialAttr(String specialAttr) {
		this.specialAttr = specialAttr;
	}

	/**
	 * 转换protobuf
	 * 
	 * @return
	 */
	public com.youxigu.dynasty2.treasury.proto.TreasuryMsg.TreasuryEvent convetMessage() {
		TreasuryMsg.TreasuryEvent.Builder tEvent = TreasuryMsg.TreasuryEvent
				.newBuilder();
		tEvent.setId(this.id);
		tEvent.setUserId(this.userId);
		tEvent.setEntId(this.entId);
		tEvent.setItemCount(this.itemCount);
		tEvent.setEquip(this.equip);
		tEvent.setLevel(this.level);
		tEvent.setIsLock(this.isLock);
		tEvent.setIsNew(false);

		if (this.getOnlinePack() != null) {
			tEvent.setOnlinePack(this.onlinePack.build());
		}
		IEntityService entityService = (IEntityService) ServiceLocator
				.getSpringBean("entityService");
		Entity en = entityService.getEntity(entId);
		if (en instanceof Item) {
			Item it = (Item) en;
			if (it.isEquip()) {
				// 加装备相关 的属性
				EquipInfo.Builder eqinfo = EquipInfo.newBuilder();

				eqinfo.setTreasuryId(this.getId());
				// 打造的属性
				attr(eqinfo, getRandomProp(), true);

				// 特殊属性
				attr(eqinfo, getSpecialAttr(), false);

				tEvent.setEquipInfo(eqinfo.build());
			}
		}
		return tEvent.build();
	}

	private void attr(EquipInfo.Builder eqinfo, String attr, boolean build) {
		List<ItemProperty> list = ItemProperty.parseProperty(attr);
		for (ItemProperty p : list) {
			AttrProperty.Builder ap = AttrProperty.newBuilder();
			ap.setAbs(p.isAbs());
			ap.setAddValue(p.getAddValue());
			ap.setPropName(p.getPropName());
			ap.setValue(p.getValue());
			if (build) {
				eqinfo.addBuildAttr(ap.build());
			} else {
				eqinfo.addSpecialAttr(ap.build());
			}

		}
	}

	@Override
	public Message build() {
		// 必须包含responseHead
		TreasuryMsg.SendTreasuryEvent.Builder sEvent = TreasuryMsg.SendTreasuryEvent
				.newBuilder();
		ResponseHead.Builder headBr = ResponseHead.newBuilder();
		headBr.setCmd(EventMessage.TYPE_FRESH_ITEM);
		headBr.setRequestCmd(EventMessage.TYPE_FRESH_ITEM);
		sEvent.setResponseHead(headBr.build());

		// 物品信息
		sEvent.setTreasuryEvent(this.convetMessage());
		return sEvent.build();
	}
}

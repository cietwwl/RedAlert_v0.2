package com.youxigu.dynasty2.entity.service.script;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.youxigu.dynasty2.chat.EventMessage;
import com.youxigu.dynasty2.chat.client.IChatClientService;
import com.youxigu.dynasty2.develop.domain.Castle;
import com.youxigu.dynasty2.develop.domain.CastleEffect;
import com.youxigu.dynasty2.develop.service.ICastleEffectService;
import com.youxigu.dynasty2.develop.service.ICastleService;
import com.youxigu.dynasty2.entity.domain.Entity;
import com.youxigu.dynasty2.entity.domain.effect.EffectDefine;
import com.youxigu.dynasty2.entity.domain.effect.EffectTypeDefine;
import com.youxigu.dynasty2.entity.service.IEntityEffectRender;
import com.youxigu.dynasty2.map.proto.CastleIconMsg;

/**
 * 城堡图标效果 此类效果不叠加只替换，旧道具的效果要全部删除， 若新增道具，需要配entId-render的对应关系
 * 
 * @author Administrator
 *
 */
public class CastleIconRender implements IEntityEffectRender {
	private ICastleService castleService;
	private IChatClientService messageService;
	private ICastleEffectService castleEffectService;

	public void setCastleService(ICastleService castleService) {
		this.castleService = castleService;
	}

	public void setMessageService(IChatClientService messageService) {
		this.messageService = messageService;
	}

	public void setCastleEffectService(ICastleEffectService castleEffectService) {
		this.castleEffectService = castleEffectService;
	}

	@Override
	public Map<String, Object> render(Entity entity, Map<String, Object> context) {
		Castle castle = (Castle) context.get("castle");
		long casId = castle.getCasId();
		// long userId = castle.getUserId();
		long now = System.currentTimeMillis();
		// 删除旧道具的效果
		castleEffectService.lockCastleEffect(casId);
		// this.dealOldEff(userId, casId);
		// 其他增加新效果
		List<EffectDefine> eds = entity.getEffects();
		for (EffectDefine ed : eds) {
			Timestamp endTime = new Timestamp(now + ed.getPara3() * 1000L);
			if (ed.getEffTypeId().equals(EffectTypeDefine.EFFECT_CASTLE_ICON)) {
				// 处理城池外观效果
				castle = castleService.lockAndGetCastle(casId);
				castle.setIcon(ed.getIconPath());
				castle.setIconEndTime(endTime);
				castleService.doUpdateCastle(castle);
				// //保存实体id
				// userAttrService.saveUserAttr(userId,
				// AppConstants.USERATTR_EXCHANGE_CAS_FACE, entity.getEntId());
				//
				// // 添加buf
				// if (ed.getBuffName() != null && !"".equals(ed.getBuffName()))
				// {
				// buffTipService.addBuffTip(castle.getUserId(),
				// ed.getBuffName(), castle.getIconEndTime());
				// }

				// 推刷新大地图城池外观
				messageService.sendEventMessage(castle.getUserId(),
						EventMessage.TYPE_CASTLE_ICON,
						new CastleIconMsg(ed.getIconPath(),true));
			} else {
				CastleEffect ce = new CastleEffect();
				ce.setCasId(casId);
				ce.setEntId(entity.getEntId());
				ce.setEffTypeId(ed.getEffTypeId());
				ce.setExpireDttm(endTime);
				ce.setPerValue(ed.getPara1());
				ce.setAbsValue(ed.getPara2());
				castleEffectService.createCastleEffect(ce);
			}
		}

		return null;
	}

	// /**
	// * 取得所有城堡生成的旧效果
	// * @param casId
	// * @return
	// */
	// private void dealOldEff(long userId, long casId) {
	// int entId = userAttrService.getIntUserAttr(userId,
	// AppConstants.USERATTR_EXCHANGE_CAS_FACE);
	// if(entId >0) {
	// //删效果
	// List<CastleEffect> effs =
	// castleEffectService.getCastleEffectByCasId(casId);
	// if(effs != null && effs.size() >0) {
	// for(CastleEffect ce : effs) {
	// if(ce.getEntId() != entId) {
	// continue;
	// }
	// castleEffectService.deleteCastleEffect(ce);
	// }
	// }
	//
	// //删buffTip
	// Entity entity = entityService.getEntity(entId);
	// List<EffectDefine> effects = entity.getEffects();
	// String buffName = "";
	// if(effects != null && effects.size() >0) {
	// for(EffectDefine ed : effects) {
	// if(!ed.getEffTypeId().equals(EffectTypeDefine.EFFECT_CASTLE_ICON)) {
	// continue;
	// }
	// buffName = ed.getBuffName();
	// buffTipService.deleteBuffTipByBuffName(userId, buffName);
	// break;
	// }
	// }
	// }
	// }

}

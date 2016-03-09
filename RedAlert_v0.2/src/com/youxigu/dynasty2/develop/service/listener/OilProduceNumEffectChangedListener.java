package com.youxigu.dynasty2.develop.service.listener;

import com.youxigu.dynasty2.develop.domain.Castle;
import com.youxigu.dynasty2.develop.domain.CastleEffect;
import com.youxigu.dynasty2.develop.service.ICastleResService;
import com.youxigu.dynasty2.develop.service.ICastleService;
import com.youxigu.dynasty2.develop.service.IFlushCastleService;
import com.youxigu.dynasty2.entity.service.IEffectChangedListener;

/**
 * 油矿每15分钟产量(资源建筑效果)
 * @author Dagangzi
 *
 */
public class OilProduceNumEffectChangedListener implements IEffectChangedListener {
	private ICastleResService castleResService;
	private IFlushCastleService flushCastleService;
	private ICastleService castleService;

	public void setCastleResService(ICastleResService castleResService) {
		this.castleResService = castleResService;
	}

	public void setFlushCastleService(IFlushCastleService flushCastleService) {
		this.flushCastleService = flushCastleService;
	}

	public void setCastleService(ICastleService castleService) {
		this.castleService = castleService;
	}

	@Override
	public void effectChanged(Object effect) {
		CastleEffect ce = (CastleEffect)effect;
		Castle castle = castleService.getCastleById(ce.getCasId());
		
		int oilProcNum = castleResService.getOilProducePerQuarter(castle.getUserId(), ce.getCasId());
		flushCastleService.sendFlushCasEffEvent(castle.getUserId(), "oilProcNum", oilProcNum);
	}
}

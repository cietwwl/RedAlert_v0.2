package com.youxigu.dynasty2.tips.service.impl;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.ibatis.sqlmap.engine.cache.memcached.MemcachedManager;
import com.youxigu.dynasty2.chat.EventMessage;
import com.youxigu.dynasty2.chat.client.IChatClientService;
import com.youxigu.dynasty2.tips.dao.IBuffTipDao;
import com.youxigu.dynasty2.tips.domain.BuffDefine;
import com.youxigu.dynasty2.tips.domain.BuffTip;
import com.youxigu.dynasty2.tips.domain.BuffTipMessage;
import com.youxigu.dynasty2.tips.service.IBuffTipService;
import com.youxigu.dynasty2.util.BaseException;

public class BuffTipService implements IBuffTipService {

	private IBuffTipDao buffTipDao;
	private IChatClientService messageService;
	private Map<Integer, BuffDefine> buffDefines = new HashMap<Integer, BuffDefine>();

	
	public void setMessageService(IChatClientService messageService) {
		this.messageService = messageService;
	}

	public void setBuffTipDao(IBuffTipDao buffTipDao) {
		this.buffTipDao = buffTipDao;
	}

	public void init() {
		List<BuffDefine> defines = buffTipDao.getBuffDefines();
		for (BuffDefine bi : defines) {
			buffDefines.put(bi.getBuffId(), bi);
		}
	}

	@Override
	public BuffTip addBuffTip(long userId, int effId, String buffName, Timestamp startTime, Timestamp endTime, int
            buffId) {
		long now = System.currentTimeMillis();
		lockBuffTip(userId);
		List<BuffTip> buffTips = buffTipDao.getBuffTipsByUserId(userId);
		BuffTip buffTip = null;
		if (buffTips != null) {
			for (BuffTip tip : buffTips) {
				if (tip.getBuffName().equals(buffName)) {
					buffTip = tip;
				} else {
					Timestamp dttm = tip.getEndTime();
					if (dttm != null && dttm.getTime() < now) {
						deleteBuffTip(tip);
					}
				}
			}
		}
		if (buffTip != null) {
            buffTip.setEffId(effId);
            buffTip.setBuffId(buffId);
            buffTip.setStartTime(startTime);
			buffTip.setEndTime(endTime);
			buffTipDao.updateBuffTip(buffTip);
		} else {
			buffTip = new BuffTip();
            buffTip.setBuffId(buffId);
            buffTip.setUserId(userId);
            buffTip.setEffId(effId);
            buffTip.setBuffName(buffName);
            buffTip.setStartTime(startTime);
			buffTip.setEndTime(endTime);
			buffTipDao.createBuffTip(buffTip);
		}

		messageService.sendEventMessage(buffTip.getUserId(),
				EventMessage.TYPE_BUFFTIP_CHANGED, new BuffTipMessage(buffTip));
		return buffTip;
	}

	// @Override
	private boolean deleteBuffTip(BuffTip buffTip) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        buffTip.setEndTime(now);

        messageService.sendEventMessage(buffTip.getUserId(),
                EventMessage.TYPE_BUFFTIP_CHANGED, new BuffTipMessage(buffTip));

        buffTipDao.deleteBuffTip(buffTip);

        return true;
	}

	@Override
	public List<BuffTip> getBuffTipsByUserId(long userId) {
		return buffTipDao.getBuffTipsByUserId(userId);
	}

	private void lockBuffTip(long userId) {
		try {
			MemcachedManager.lock(BuffTip.BUFFTIP_LOCKER_PREFIX + userId);
		} catch (TimeoutException e) {
			throw new BaseException(e);
		}
	}

	@Override
	public List<BuffTip> deleteAndGetBuffTipsByUserId(long userId) {
		long now = System.currentTimeMillis();
		lockBuffTip(userId);
		List<BuffTip> buffTips = buffTipDao.getBuffTipsByUserId(userId);
		if (buffTips != null) {
			Iterator<BuffTip> lit = buffTips.iterator();
			while (lit.hasNext()) {
				BuffTip tip = lit.next();
				Timestamp dttm = tip.getEndTime();
				if (dttm != null && dttm.getTime() < now) {
					deleteBuffTip(tip);
					lit.remove();
				}
			}
		}
		return buffTips;
	}

	@Override
	public void deleteBuffTipByBuffName(long userId, String buffName) {
		lockBuffTip(userId);
		List<BuffTip> buffTips = buffTipDao.getBuffTipsByUserId(userId);
		if (buffTips != null) {
            for (BuffTip tip : buffTips) {
                if (tip.getBuffName().equals(buffName)) {
                    deleteBuffTip(tip);
                }
            }
		}
	}

	@Override
	public BuffDefine getBuffDefine(int buffId) {
		return buffDefines.get(buffId);
	}
}

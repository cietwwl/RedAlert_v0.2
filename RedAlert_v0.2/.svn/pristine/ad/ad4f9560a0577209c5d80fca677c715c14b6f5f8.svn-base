package com.youxigu.dynasty2.map.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.youxigu.dynasty2.map.dao.ICollectPointDao;
import com.youxigu.dynasty2.map.domain.CollectPoint;
import com.youxigu.dynasty2.map.enums.CollectType;
import com.youxigu.dynasty2.map.service.ICollectPointService;

public class CollectPointService implements ICollectPointService {
	private ICollectPointDao collectPointDao;

	@Override
	public List<CollectPoint> getAllCollectPoints(long userId) {
		return collectPointDao.getAllCollectPoints(userId);
	}

	@Override
	public List<CollectPoint> getAllCollectPointsByType(long userId,
			CollectType type) {
		List<CollectPoint> cs = getAllCollectPoints(userId);
		if (cs == null || cs.isEmpty()) {
			return cs;
		}
		List<CollectPoint> rt = new ArrayList<CollectPoint>();
		for (CollectPoint c : cs) {
			if (type.equals(c.getType())) {
				rt.add(c);
			}
		}
		return rt;
	}

	@Override
	public int getCollectPointCount(long userId) {
		List<CollectPoint> rt = getAllCollectPoints(userId);
		if (rt == null) {
			return 0;
		}
		return rt.size();
	}

	@Override
	public boolean doSaveCollectPoint(CollectPoint p) {
		collectPointDao.insertCollectPoint(p);
		return true;
	}

	@Override
	public boolean doUpdateCollectPoint(CollectPoint p) {
		collectPointDao.updateCollectPoint(p);
		return true;
	}

	@Override
	public boolean doDeleteCollectPoint(long userId, long id) {
		CollectPoint p = new CollectPoint();
		p.setUserId(userId);
		p.setId(id);
		collectPointDao.deleteCollectPoint(p);
		return true;
	}

	@Override
	public CollectPoint getCollectPoint(long userId, long id) {
		return collectPointDao.getCollectPoint(userId, id);
	}

}

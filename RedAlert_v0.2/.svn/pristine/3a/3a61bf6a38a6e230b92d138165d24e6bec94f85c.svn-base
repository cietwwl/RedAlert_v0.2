package com.youxigu.dynasty2.hero.service.impl;

import java.util.List;

import com.youxigu.dynasty2.hero.dao.ITroopGridDao;
import com.youxigu.dynasty2.hero.domain.TroopGrid;
import com.youxigu.dynasty2.hero.service.IHeroService;
import com.youxigu.dynasty2.hero.service.ITroopGridService;
import com.youxigu.dynasty2.hero.service.ITroopService;
import com.youxigu.dynasty2.user.service.IUserService;

public class TroopGridService implements ITroopGridService {
	private ITroopGridDao troopGridDao = null;
	private ITroopService troopService = null;
	private IHeroService heroService = null;
	private IUserService userService = null;

	@Override
	public void lockTroopGrid(long userId) {
		troopService.lockTroop(userId);
	}

	@Override
	public void doCreateTroopGrid(TroopGrid grid) {
		troopGridDao.createTroopGrid(grid);
	}

	@Override
	public TroopGrid getTroopGrid(long userId, long troopGridId) {
		return troopGridDao.getTroopGridById(userId, troopGridId);
	}

	@Override
	public List<TroopGrid> getTroopGridByUserId(long userId) {
		// TODO Auto-generated method stub
		return troopGridDao.getTroopGridsByUserId(userId);
	}

	@Override
	public void updateTroopGrid(TroopGrid grid) {
		troopGridDao.updateTroopGrid(grid);
	}

	public void setTroopGridDao(ITroopGridDao troopGridDao) {
		this.troopGridDao = troopGridDao;
	}

	public void setTroopService(ITroopService troopService) {
		this.troopService = troopService;
	}

	public void setHeroService(IHeroService heroService) {
		this.heroService = heroService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
}

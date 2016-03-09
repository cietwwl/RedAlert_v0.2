package com.youxigu.dynasty2.user.service.impl.achieve;

import java.util.List;

import com.youxigu.dynasty2.entity.domain.Item;
import com.youxigu.dynasty2.hero.domain.Troop;
import com.youxigu.dynasty2.hero.domain.TroopGrid;
import com.youxigu.dynasty2.hero.service.ITroopService;
import com.youxigu.dynasty2.treasury.domain.Treasury;
import com.youxigu.dynasty2.treasury.service.ITreasuryService;
import com.youxigu.dynasty2.user.domain.Achieve;
import com.youxigu.dynasty2.user.domain.AchieveLimit;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.user.service.IAchieveCompleteChecker;
import com.youxigu.dynasty2.user.service.IUserService;

/**
 * commanderEquipColorNum 指挥官坦克装备X件Y品质装备
 * color para1 品质
 * num para2 数量
 * 
 * @author Dagangzi
 * @date 2016年1月12日
 */
public class CommanderEquipColorNumAchieveChecker implements IAchieveCompleteChecker {
	private ITroopService troopService;
	private IUserService userService;
	private ITreasuryService treasuryService;

	public void setTroopService(ITroopService troopService) {
		this.troopService = troopService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public void setTreasuryService(ITreasuryService treasuryService) {
		this.treasuryService = treasuryService;
	}

	@Override
	public int check(long userId, Achieve achieve, AchieveLimit achieveLimit,
			int entNum) {
		int color = achieveLimit.getPara1();
		int num = achieveLimit.getPara2();
		entNum = 0;
		User user = userService.getUserById(userId);
		Troop troop = troopService.getTroopById(userId, user.getTroopId());
		if (troop != null) {
			TroopGrid troopGrid = troop.getMainTroopGrid();
			if (troopGrid != null) {
				List<Long> ids = troopGrid.getEquip();
				if (ids != null && ids.size() > 0) {
					for (long treasuryId : ids) {
						Treasury treasury = treasuryService
								.getTreasuryById(userId, treasuryId);
						if (treasury != null) {
							Item item = treasury.getItem();
							if (item.getColor() == color) {
								entNum = entNum + 1;
							}
						}
					}
				}
			}
		}
		return Math.min(entNum, num);
	}

}

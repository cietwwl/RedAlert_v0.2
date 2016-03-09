package com.youxigu.dynasty2.user.service.impl.achieve;

import java.util.List;

import com.youxigu.dynasty2.entity.domain.Item;
import com.youxigu.dynasty2.treasury.domain.Treasury;
import com.youxigu.dynasty2.treasury.service.ITreasuryService;
import com.youxigu.dynasty2.user.domain.Achieve;
import com.youxigu.dynasty2.user.domain.AchieveLimit;
import com.youxigu.dynasty2.user.service.IAchieveCompleteChecker;

/**
 * equipLvColorNum X件Y品质Z级装备 
 * color para1 品质 
 * equipLv para2 等级
 * num para3 个数
 * 
 * @author Dagangzi
 * @date 2016年1月12日
 */
public class EquipLvColorNumAchieveChecker implements IAchieveCompleteChecker {
	private ITreasuryService treasuryService;

	public void setTreasuryService(ITreasuryService treasuryService) {
		this.treasuryService = treasuryService;
	}

	@Override
	public int check(long userId, Achieve achieve, AchieveLimit achieveLimit,
			int entNum) {
		int color = achieveLimit.getPara1();
		int equipLv = achieveLimit.getPara2();
		int num = achieveLimit.getPara3();
		entNum = 0;
		List<Treasury> list = treasuryService.getAllEquipByUserId(userId);
		if (list != null && list.size() > 0) {
			for (Treasury treasury : list) {
				Item item = treasury.getItem();
				if (item.getColor() == color
						&& treasury.getLevel() >= equipLv) {
					entNum = entNum + 1;
				}
			}
		}
		return Math.min(entNum, num);
	}

}

package com.youxigu.dynasty2.tips.service;

import java.sql.Timestamp;
import java.util.List;

import com.youxigu.dynasty2.tips.domain.BuffDefine;
import com.youxigu.dynasty2.tips.domain.BuffTip;

public interface IBuffTipService {

	BuffDefine getBuffDefine(int buffId);
	
	BuffTip addBuffTip(long userId, int effId, String buffType, Timestamp startTime, Timestamp endTime, int buffId);
	
	//void deleteBuffTip(BuffTip buffTip);
	
	void deleteBuffTipByBuffName(long userId, String buffName);
	
	List<BuffTip> getBuffTipsByUserId(long userId);
	
	List<BuffTip> deleteAndGetBuffTipsByUserId(long userId);
}

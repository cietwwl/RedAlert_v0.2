package com.youxigu.dynasty2.tips.dao;

import java.util.List;

import com.youxigu.dynasty2.tips.domain.BuffDefine;
import com.youxigu.dynasty2.tips.domain.BuffTip;

public interface IBuffTipDao {
	List<BuffDefine> getBuffDefines();
	
	void createBuffTip(BuffTip buffTip);
	
	void updateBuffTip(BuffTip buffTip);
	
	void deleteBuffTip(BuffTip buffTip);
	
	List<BuffTip> getBuffTipsByUserId(long userId);
}

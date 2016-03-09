package com.youxigu.dynasty2.user.service;

import java.util.Map;

import com.youxigu.dynasty2.hero.domain.Hero;
import com.youxigu.dynasty2.user.domain.Title;
import com.youxigu.dynasty2.user.domain.TitleAward;
import com.youxigu.dynasty2.util.EffectValue;

/**
 * 君主军衔service
 * 
 * @author Dagangzi
 * @date 2016年1月19日
 */
public interface IUserTitleService {
	/**
	 * 取军衔
	 * 
	 * @param titleId
	 * @return
	 */
	Title getTitle(int titleId);

	/**
	 * 取得勋章奖励
	 * 
	 * @param awardId
	 * @return
	 */
	TitleAward getTitleAward(int awardId);

	/**
	 * 激活勋章
	 * 
	 * @param userId
	 * @param awardId
	 * @return
	 */
	Map<String, Object> doActiveMedal(long userId, int awardId);

	/**
	 * 取得军衔详情
	 * 
	 * @param userId
	 * @param titleId
	 * @return
	 */
	Map<String, Object> showTitleInfo(long userId, int titleId);
	
	/**
	 * 取指挥官军衔属性加成
	 * 
	 * @param hero
	 * @return
	 */
	Map<String, EffectValue> getTitleEffectValue(Hero hero);

	/**
	 * 返回可以领取勋章的军衔id
	 * 
	 * @param user
	 * @return
	 */
	Map<String, Object> getRedPoint(long userId);
}

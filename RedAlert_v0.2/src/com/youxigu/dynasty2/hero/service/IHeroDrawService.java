package com.youxigu.dynasty2.hero.service;

import java.util.Map;

import com.youxigu.dynasty2.hero.domain.HeroPub;
import com.youxigu.dynasty2.hero.domain.UserPubAttr;
import com.youxigu.dynasty2.hero.enums.HeroDrawType;
import com.youxigu.dynasty2.hero.proto.HeroDrawInfo;

/**
 * 武将酒馆，抽取武将
 * 
 * @author fengfeng
 *
 */
public interface IHeroDrawService {
	/**
	 * 取得抽将配置
	 * 
	 * @param type
	 * @param vipLv
	 * @return
	 */
	HeroPub getHeroPub(HeroDrawType type, short vipLv);

	/**
	 * 取所有的抽将配置
	 * 
	 * @return
	 */
	Map<HeroDrawType, Map<Short, HeroPub>> getHeroPubMaps();

	/**
	 * 酒馆抽将
	 * 
	 * @param userId
	 * @param type
	 * @param discard
	 *            , true=分解蓝色，绿色武将
	 * @return
	 */

	HeroDrawInfo doHireHero(long userId, HeroDrawType type);

	/**
	 * 10连抽
	 * 
	 * @param userId
	 * @param type
	 * @param discard
	 *            , true=分解蓝色，绿色武将
	 * @return
	 */
	HeroDrawInfo doHireHero10(long userId, HeroDrawType type);

	public UserPubAttr getUserPubAttrById(long userId);

}

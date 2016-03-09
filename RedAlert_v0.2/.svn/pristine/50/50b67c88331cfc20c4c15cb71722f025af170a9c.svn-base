package com.youxigu.dynasty2.user.service;

import java.util.List;

import com.youxigu.dynasty2.entity.domain.Entity;
import com.youxigu.dynasty2.entity.domain.effect.EffectDefine;
import com.youxigu.dynasty2.user.domain.UserEffect;
import com.youxigu.dynasty2.util.EffectValue;
/**
 * 主要针对buff，科技，影响全体武将的效果
 * @author Dagangzi
 *
 */
public interface IUserEffectService {
	/**
	 * 
	 * @param userId
	 * @return
	 */
	List<UserEffect> getUserEffectByUserId(long userId);

	/**
	 * 
	 * @param userId
	 * @param effTypeId
	 * @return
	 */
	List<UserEffect> getUserEffectByEffTypeId(long userId, String effTypeId);
	
	/**
	 * 取特定类型的城市效果:包括过期的
	 * @param userId
	 * @param effTypeId
	 * @return
	 */
	List<UserEffect> getUserEffectByEffTypeIdWithTimeout(long userId,String effTypeId);

	/**
	 * 取得用户的某个实体ID+效果类型的当前效果
	 * 
	 * @param userId
	 * @param effectId
	 * @return
	 */
	UserEffect getUserEffectByEffTypeId(long userId, int entId, String effTypeId);

	/**
	 * 取得用户的某一类型效果的和
	 * 
	 * @param userId
	 * @param effTypeId
	 * @return
	 */
	EffectValue getSumUserEffectValueByEffectType(long userId, String effTypeId);

	/**
	 * 创建/更新用户效果
	 * 
	 * 更新：找到原有的具有相同entId的效果，直接更新成新的效果，不累加
	 * 
	 * @param casId
	 * @param effect
	 */
	@Deprecated
	void addUserEffect(long userId, Entity entity, EffectDefine effect);

	void createUserEffect(UserEffect ue);

	void updateUserEffect(UserEffect ue);
	
	void removeTimeoutUserEffect(long userId);
	
	void lockUserEffect(long userId);

}

package com.youxigu.dynasty2.entity.service.validator.impl;

import java.util.Map;

import com.youxigu.dynasty2.develop.domain.Castle;
import com.youxigu.dynasty2.develop.service.ICastleService;
import com.youxigu.dynasty2.entity.domain.EntityLimit;
import com.youxigu.dynasty2.entity.domain.Party;
import com.youxigu.dynasty2.entity.service.EntityLimitResult;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.user.service.IUserService;
import com.youxigu.dynasty2.util.BaseException;

/**
 * 君主等级约束
 * @author Administrator
 *
 */
public class UserLevelLimitValidator extends DefaultEntityLimitValidator {

	private IUserService userService;
	private ICastleService castleService;
	
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
	public void setCastleService(ICastleService castleService) {
		this.castleService = castleService;
	}
	@Override
	public EntityLimitResult validate(EntityLimit limit,
                                      Map<String, Object> context) {

		if (limit.getNeedEntity() == null)// 没有条件约束
			return null;
		
		User user = (User) context.get("user");
		if (user == null) {
			Object tmp = context.get("userId");
			if (tmp != null) {
				long userId = (Long) tmp;
				user = userService.getUserById(userId);
				context.put("user", user);
			} else {
				Castle castle = (Castle) context.get("castle");
				if (castle==null){
					tmp = context.get("casId");
					if (tmp != null) {
						castle = castleService.getCastleById((Long)tmp);
						user = userService.getUserById(castle.getUserId());
					}
				}else{
					user = userService.getUserById(castle.getUserId());
				}
				
			}
		}
		if(user==null){
			throw new BaseException("param user found.");
		}

		Party party = (Party) limit.getNeedEntity();
		
		EntityLimitResult result = new EntityLimitResult(limit);

		result.setNeedEntName(party.getPartyName());
		result.setNeedEntTypeDesc(null);
		
		result.setActualLevel(user.getUsrLv());			
		
		if (log.isDebugEnabled()) {
			log.debug("需要[" + result.getNeedEntName() + "]等级[" + result.getNeedLevel() + "]");
		}
		return result;
	}
}

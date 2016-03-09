package com.youxigu.dynasty2.gmtool.dao;

import java.sql.Timestamp;
import java.util.List;

import com.youxigu.dynasty2.gmtool.domain.OnlineUser;
import com.youxigu.dynasty2.gmtool.domain.UserCashBonus;

public interface IGmToolDao {
	/**
	 * @author ninglong
	 *         <p>
	 *         得到某一时间段里的在线人数
	 *         </p>
	 * @param start
	 * @param end
	 * @return
	 */
	List<OnlineUser> getOnlineUser(Timestamp start, Timestamp end);

	/**
	 * 得到内部员工元宝福利发放列表
	 * 
	 * @return
	 */
	List<UserCashBonus> getAllUserCashBonus();

	/**
	 * 增加一个内部员工福利
	 * 
	 * @param ucb
	 */
	void createUserCashBonus(UserCashBonus ucb);

	/**
	 * 修改内部员工福利
	 * 
	 * @param ucb
	 */
	void updateUserCashBonus(UserCashBonus ucb);

	UserCashBonus getUserCashBonus(String openId);
}

package com.youxigu.dynasty2.activity.dao;

import java.util.List;

import com.youxigu.dynasty2.activity.domain.Activity;
import com.youxigu.dynasty2.activity.domain.AwardActivity;
import com.youxigu.dynasty2.activity.domain.MysticShop;
import com.youxigu.dynasty2.activity.domain.MysticShopItem;
import com.youxigu.dynasty2.activity.domain.OperateActivity;
import com.youxigu.dynasty2.activity.domain.UserMysticShop;
import com.youxigu.dynasty2.activity.domain.UserOperateActivity;

/**
 * 运营活动DAO接口
 * 
 * @author Administrator
 * 
 */
public interface IActivityDao {

	// =========================神秘商店相关配置 ==================================
	/**
	 * 获得商店配数
	 * 
	 * @return
	 */
	List<MysticShop> getAllMysticShop();

	/**
	 * 获得商店道具配数
	 * 
	 * @return
	 */
	List<MysticShopItem> getAllMysticShopItem();

	/**
	 * 插入
	 * 
	 * @param userMysticShop
	 */
	void insertUserMysticShop(UserMysticShop userMysticShop);

	/**
	 * 查询
	 * 
	 * @param userId
	 * @return
	 */
	UserMysticShop getUserMysticShop(long userId, int shopId);

	void updateUserMysticShop(UserMysticShop userMysticShop);

	// --------------------------

	void createOperateActivity(OperateActivity act);

	void deleteOperateActivity(long actId);

	void updateOperateActivity(OperateActivity act);

	OperateActivity getOperateActivity(long actId);

	List<OperateActivity> getOperateActivitys();

	void createUserOperateActivity(UserOperateActivity act);

	void updateUserOperateActivity(UserOperateActivity act);

	UserOperateActivity getUserOperateActivity(long userId, int type);

	void createActivity(Activity act);

	void deleteActivity(int actId);

	void updateActivity(Activity act);

	Activity getActivity(int actId);

	List<Activity> getActivitys();

	// //////////奖励活动
	void createAwardActivity(AwardActivity act);

	void updateAwardActivity(AwardActivity act);

	void deleteAwardActivity(int actId);

	AwardActivity getAwardActivity(int actId);

	List<AwardActivity> getAwardActivitys();

}

package com.youxigu.dynasty2.openapi.service;

import java.util.List;
import java.util.Map;

public interface ITecentPayService {
	/**
	 * 获取用户游戏币余额
	 * 
	 * @param platformType
	 * @param openid
	 * @param openkey
	 * @param pay_token
	 * @param pf
	 * @param pkkey
	 * @param zoneid
	 * @return
	 * 
	 *         balance 游戏币个数（包含了赠送游戏币） gen_balance 赠送游戏币个数 first_save
	 *         是否满足首次充值，1：满足，0：不满足 save_amt 累计充值金额
	 */
	public Map<String, Object> getBalances(int platformType, String openid,
			String openkey, String pay_token, String pf, String pfkey,
			String zoneid);

	/**
	 * 获取用户游戏币余额
	 * 
	 * @param platformType
	 * @param openid
	 * @param openkey
	 * @param pay_token
	 * @param pf
	 * @param pfkey
	 * @param zoneid
	 * @return
	 */
	public int getBalance(int platformType, String openid, String openkey,
			String pay_token, String pf, String pfkey, String zoneid);

	/**
	 * 扣除游戏币接口
	 * 
	 * @param platformType
	 * @param openid
	 * @param openkey
	 * @param pay_token
	 * @param pf
	 * @param pfkey
	 * @param zoneid
	 * @param amt
	 * @return
	 */
	public Map<String, Object> pay(int platformType, String openid,
			String openkey, String pay_token, String pf, String pfkey,
			String zoneid, String amt);

	/**
	 * 取消支付
	 * 
	 * @param platformType
	 * @param openid
	 * @param openkey
	 * @param pay_token
	 * @param pf
	 * @param pfkey
	 * @param zoneid
	 * @param amt
	 * @param billno
	 */
	public void cancelPay(int platformType, String openid, String openkey,
			String pay_token, String pf, String pfkey, String zoneid,
			String amt, String billno);

	/**
	 * 赠送游戏币
	 * 
	 * @param platformType
	 * @param openid
	 * @param openkey
	 * @param pay_token
	 * @param pf
	 * @param pfkey
	 * @param zoneid
	 * @param discountid
	 * @param giftid
	 * @param amt
	 * @return
	 */
	public int present(int platformType, String openid, String openkey,
			String pay_token, String pf, String pfkey, String zoneid,
			String discountid, String giftid, String amt);

	/**
	 * 查询订阅物品接口
	 * 
	 * @param platformType
	 * @param openid
	 * @param openkey
	 * @param pay_token
	 * @param pf
	 * @param pfkey
	 * @param zoneid
	 * @param discountid
	 * @param giftid
	 * @param amt
	 * @return
	 */


	public List<Map<String, Object>> getSubscribe(int platformType,
			String openid, String openkey, String pay_token, String pf,
			String pfkey, String zoneid);
	
	/**
	 * 转帐
	 * @param platformType
	 * @param openid
	 * @param openkey
	 * @param pf
	 * @param dstzoneid
	 * @param payer_session_id
	 * @param payer
	 * @param srczoneid
	 * @param amt
	 * @param pfkey
	 * @param appremark
	 * @return
	 */
	public List<Map<String, Object>> transfer(int platformType, String openid,
			String openkey, String pf, String dstzoneid,
			String payer_session_id, String payer, String srczoneid,
			String amt, String pfkey, String appremark);	
}

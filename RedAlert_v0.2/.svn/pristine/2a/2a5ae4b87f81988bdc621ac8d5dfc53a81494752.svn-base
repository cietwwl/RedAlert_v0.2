package com.youxigu.dynasty2.openapi.service;

import java.util.Map;

import com.youxigu.wolf.net.UserSession;

/**
 * 腾讯联运支付接口
 * 
 * @author Administrator
 * 
 */
public interface ITecentUnionPayService {

	/**
	 * 生成支付接口服务端签名
	 * 
	 * "p_appid",		        //平台appid
"p_openid",			    //平台openid
"g_cid",			        //渠道ID
"server_id",	            //区服ID，没有填”0”
"server_name",	        //区服名称， 默认”1区”
"pay_order",	            //游戏支付订单号 
"pay_time",	            //支付时间,格式 yyyy-MM-dd HH:mm:ss
"pay_fee",	            //支付总额（为0为不定值）
"pay_model",	            //充值模式 1 一级货币（目前固定为1，货币由平台托管）2道具 3 行为
"pay_ratio",	            //一级货币兑换比例，一级货币和现金的比例，比如1块钱买10个钻石，这个值为10；
"user_vip",	            //用户是否会员，默认””
"user_lv",                //用户等级  默认””
"user_party",             //工会 默认””
"user_race",               //种族 默认””
"user_rolename",           //角色名，默认””
"user_roleid",              //角色id默认””
"product_code",	        //产品ID（目前为一级货币ID）
"product_name",	        //产品名（目前为一级货币名）
"product_count",	        //产品数量, 数值型字符串，默认为 ”0” （目前为一级货币数量）
"product_price",	        //充值单价（原价） 数值型字符串，默认为 ”0”
"gift_product_count",	    //本次充值赠送数量（目前为赠送的一级货币数量，不计算在用户充值总数量里）
"c_extinfo",	            //游戏透传信息,默认”” 

	 * @param params
	 * @return
	 */
	String getPaySig(Map<String, String> params);

	String getOrderId(String areaId, long userId);

	/**
	 * 生成签名
	 * 
	 * @param params
	 * @return
	 */
	String makeSig(Map<String, String> params);

	/**
	 * 支付
	 * p_appid	String	应用在外发联运平台唯一id
p_openid	String	普通用户唯一平台标识
g_cid	String	渠道ID
p_accesstoken	String	平台授权凭证access_token
server_id	String	区服ID
pay_order	String	游戏消费订单号【如果游戏没有可为空】
pay_model	String 	//消费模式 1虚拟币（非一级货币）、2道具 3 行为
pay_time	String	购买时间（YYYY-MM-DD hh:mm:ss）
pay_product_code	String	支付产品（一般指游戏内一级货币，和支付的ID保持一致）ID
pay_product_name	String	支付产品（一般指游戏内一级货币）名
pay_product_count	String	支付产品（一般指游戏内一级货币）数量
user_vip	String	用户是否会员，默认””
user_lv	String	用户等级默认”” 
user_party	String	user_工会 ，默认””
user_race	String	种族 ，默认””
user_rolename	String	角色名，默认””
user_roleid	String	角色id，默认””
buy_product_code	String	购买产品ID
buy_product_name	String	购买产品名
buy_product_count	String	购买产品数量
buy_product_price	String 	购买产品单价
user_ip	String 	用户客户端IP
p_device_info	String	上报数据（其中包含用户上传信息,这个数据是登录时候平台客户端传回的）
extinfo	String	游戏透传字段，平台原样返回，支持游戏异步处理，不要超过32个字符
sig	String	校验码(签名)

	 * @param params
	 */
	//Map<String, Object> pay(Map<String, String> params);
	Map<String, Object> pay(UserSession us,int cash);

	/**
	 * 赠送
	 * p_appid	String	应用在外发联运平台唯一id
p_openid	String	普通用户唯一平台标识
g_cid	String	渠道ID
server_id	String	区服ID,默认写0，和支付接口保持一致
pay_order	String	游戏赠送订单号【如果游戏没有可为空】
send_time	String	赠送时间
discountid	String	营销活动ID
user_vip	String	用户是否会员，默认””
user_lv	String	用户等级默认”” 
user_party	String	user_工会 ，默认””
user_race	String	种族 ，默认””
user_rolename	String	角色名，默认””
user_roleid	String	角色id，默认””
gift_serial	String	赠送ID或者序列（礼包的唯一ID，防止重复赠送）
gift_product_code	String	赠送产品（指游戏内一级货币，和支付的ID保持一致）ID
gift_product_name	String	赠送产品（指游戏内一级货币）名
gift_product_count	String	赠送产品（指游戏内一级货币）数量
user_ip	String 	用户客户端IP
p_device_info	String	上报数据（其中包含用户上传信息,这个数据是登录时候平台客户端传回的）
extinfo	String	游戏透传字段，平台原样返回，支持游戏异步处理，不要超过32个字符
sig	String	校验码(签名)
p_accesstoken	String	平台授权凭证access_token

	 * 
	 * @param params
	 */
	//Map<String, Object> present(Map<String, String> params);
	Map<String, Object> present(UserSession us,int cash);

}

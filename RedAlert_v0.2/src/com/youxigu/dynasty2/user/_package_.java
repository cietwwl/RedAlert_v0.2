package com.youxigu.dynasty2.user;
/**
 * 用户模块文档类
 *
 */
public class _package_ {
	/**
	 * 用户相关功能
	 * 鼓励写注释-接口的头注释-（实体对象的头注释-字段注释）-关键行-初始变量-if条件判断处
	 * 获取指定条件的玩家。。主要做账号清理工作
	 * SELECT b.* FROM account as a, user as b where    
	 * 	(TO_DAYS(NOW()) - TO_DAYS(a.lastLoginDttm)) >= 10000   
	 * 	and b.cashTotal >0 and b.usrLv < 10  
	 * 	and a.accid = b.accid ;
	 * 1.转国功能没做 
	 */
}
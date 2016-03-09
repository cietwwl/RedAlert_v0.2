package com.youxigu.dynasty2.asyncdb;
/**
 * 异步数据库处理
 *
 */
public class _package_ {
	/**
	 * 目前只能进行update的异步处理
	 * 
	 * TODO：
	 * insert:由于需要数据库的自动主键，目前无法做异步
	 * delete:由于缓存的机制是缓存中若没有，则查询数据库。进行异步delete,就会将删除的数据重新加入缓存.
	 * 			若要做异步delete,必须保证缓存没有LRU
	 * 
	 * 异步update是修改了ibatis后实现的。
	 * 
	 * 不需要编程处理，只要修改ibatis的sql配置文件。
	 * 
	 * 1.在xml的<update>中加上属性 asyncUpdate="1"
	 * 	<update id="updateAccount" parameterClass="account" asyncUpdate="1"> 。。。。。</update>
	 *
	 * 2.还有一个总的开关
	 *   <sqlMapConfig>
	 *      <settings
	 *         。。。。。。。
	 *         asyncUpdate="true"
	 *         />
	 *   asyncUpdate="true"表示使用异步更新，1中的配置才会生效
	 *   asyncUpdate="false"禁止所有的异步更新
	 *   
	 *            
	 */
}
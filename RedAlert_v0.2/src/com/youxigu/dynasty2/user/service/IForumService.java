package com.youxigu.dynasty2.user.service;

import java.util.List;
import java.util.Map;

/**
 * 文件名    IForumService.java
 *
 * 描  述    论坛管理器
 *
 * 时  间    2014-12-12
 *
 * 作  者    huhaiquan
 *
 * 版  本    v1.5
 */
public interface IForumService {
	//1，天子公告，2，上周热帖，3，本周热帖
	int TYPE_TIANZI =1;
	int TYPE_PRE = 2;
	int TYPE_NEXT = 3;
	
	/**
	 * 天子发送论坛内容
	 * 
	 * @param userId
	 * @param title
	 * @param context
	 * @return
	 */
	public Map<String,Object> doSendForum(long userId, String title, String context);
	
	/**
	 * 获得指定类型的论坛
	 * 
	 * @param type 1，天子历史公告，2，上周热帖，3，本周热帖
	 * @param startNo
	 * @param pageNum
	 * @return
	 */
	public Map<String,Object> getForumContextList(long userId, int type, int startNo, int pageNum);

	
	/**
	 * 获得天子当日公告
	 * 
	 * @param userId
	 * @return
	 */
	public Map<String,Object> getCurForum(long userId);
	
	/**
	 * 根据论坛id获得论坛具体
	 * 
	 * @param forumId
	 * @return
	 */
	public Map<String,Object> getContextById(int forumId);
	
	/**
	 * GM同步论坛内容
	 * 
	 * @param type 2,上周热帖，3，本周热帖
 	 * @param data
	 */
	public void doGmSynForumContext(int type, List<Map<String, Object>> dataList);
	
}

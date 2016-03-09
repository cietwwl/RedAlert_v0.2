package com.youxigu.dynasty2.user.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibatis.sqlmap.engine.cache.memcached.broadcast.IBroadcastProducer;
import com.youxigu.dynasty2.common.service.ISensitiveWordService;
import com.youxigu.dynasty2.user.dao.IUserDao;
import com.youxigu.dynasty2.user.domain.Forum;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.user.service.IForumService;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.dynasty2.util.DateUtils;
import com.youxigu.wolf.net.AsyncWolfTask;

/**
 * 文件名 ForumService.java
 * 
 * 描 述
 * 
 * 时 间 2014-12-12
 * 
 * 作 者 huhaiquan
 * 
 * 版 本 v1.5
 */
public class ForumService implements IForumService {
	private IUserDao userDao;
	private UserService userService;
	private ISensitiveWordService sensitiveWordService;
//	private IOfficialService officialService;
	private IBroadcastProducer broadcastMgr;
	private SortForum sortForum = new SortForum();
	
	

	/**
	 * 天子帖子
	 */
	private Map<Integer, List<Forum>> tianziForumMap = new HashMap<Integer, List<Forum>>(8);
	/**
	 * 上周热帖
	 */
	private List<Forum> preForumList = new ArrayList<Forum>(0);
	/**
	 * 本周热帖
	 */
	private List<Forum> nextForumList= new ArrayList<Forum>(0);
	private Map<Integer,Forum> forumMap = new HashMap<Integer, Forum>();

	public void init() {

		// 天子帖子
		Map<Integer, List<Forum>> tianziForumMap = new HashMap<Integer, List<Forum>>();
		// 上周热帖
		List<Forum> preForumList = new ArrayList<Forum>();
		// 本周热帖
		List<Forum> nextForumList = new ArrayList<Forum>();
		List<Forum> forumList = userDao.getForumList();
		for (Forum forum : forumList) {
			forumMap.put(forum.getForumId(), forum);
			switch (forum.getForumType()) {
			case 1:
				List<Forum> list = tianziForumMap.get(forum.getCountryId());
				if (list == null) {
					list = new ArrayList<Forum>();
					tianziForumMap.put(forum.getCountryId(), list);
				}
				list.add(forum);
				break;
			case 2:
				preForumList.add(forum);
				break;
			case 3:
				nextForumList.add(forum);
			}

		}
		// 统一按时间排序
		Collections.sort(preForumList, sortForum);
		Collections.sort(nextForumList, sortForum);
		for (List<Forum> fList : tianziForumMap.values()) {
			Collections.sort(fList, sortForum);
		}
		this.nextForumList = nextForumList;
		this.preForumList = preForumList;
		this.tianziForumMap = tianziForumMap;

	}

	@Override
	public void doGmSynForumContext(int type, List<Map<String, Object>> dataList) {
		List<Forum> forumL = new ArrayList<Forum>(dataList.size());
		userDao.deleteForum(type);
		//解析数据
		for(Map<String,Object> data : dataList){
			Forum forum = new Forum();
			String title = (String)data.get("title");
			String context = (String)data.get("context");
			String time = (String)data.get("time");
			forum.setForumContext(context);
			forum.setTitle(title);
			forum.setForumType(type);
			forum.setLastTime(Timestamp.valueOf(time));
			userDao.insertForum(forum);
			forumL.add(forum);
			forumMap.put(forum.getForumId(), forum);
		}
		//替换成新数据
		List<Forum> forumList = null;
		if(type==2){
			forumList = this.preForumList;
			this.preForumList = forumL;
		}else if(type==3){
			forumList = this.nextForumList;
			this.nextForumList = forumL;
			
		}
		Object[] arr = new Object[forumList.size()];
		//清除老数据
		int i=0;
		for(Forum forum : forumList){
			forumMap.remove(forum.getForumId());
			arr[i]=forum.getForumId();
			++i;
		}
		broadcastOther(type, arr);
		
	}

	/**
	 * 3) 标题输入规则  没有输入时的初始状态显示系统字：点击输入，最多10字  文字走系统屏蔽字规则，可以打出** 
	 * 输入超过10字后的内容不显示，最多显示10个字  每日官职刷新后，保存上一次的内容为历史公告，并恢复到初始状态
	 * 
	 * 4) 正文输入规则  没有输入时的初始状态显示系统字：点击输入正文，最多输入500字  文字走系统屏蔽字规则，可以打出** 
	 * 输入超过500字之后的内容不显示，最多显示500个字  每日官职刷新后，保存上一次的内容为历史公告，并恢复到初始状态
	 * 
	 * 
	 * (non-Javadoc)
	 * 
	 * @see IForumService#doSendForum(long,
	 *      String, String)
	 */
	@Override
	public Map<String, Object> doSendForum(long userId, String title,
			String context) {
//		if(title==null||title.equals("")){
//			throw new BaseException("标题不能为空");
//		}
//		if(context==null||context.equals("")){
//			throw new BaseException("正文不能为空");
//		}
//		if (title.length() > 10) {
//			throw new BaseException("标题不能超过10个字");
//		}
//		if (context.length() > 500) {
//			throw new BaseException("内容不能超过500个字");
//		}
//
//
//		title = sensitiveWordService.replace(title, "**");
//		context = sensitiveWordService.replace(context, "**");
//		UserOfficial userOffical = officialService.getUserOfficial(userId);
//		if (userOffical == null || !userOffical.isTianzi()) {
//			throw new BaseException("您当前不是本国的王，不能发布公告");
//		}
//		Calendar cal = Calendar.getInstance();
//		int curHour = cal.get(Calendar.HOUR_OF_DAY);
//		if(curHour<9||curHour>23){
//			throw new BaseException("百战千军中成为本国的王每日9-23点可发布");
//		}
//		User user = userService.getUserById(userId);
//		List<Forum> tianziList = tianziForumMap.get(user.getCountryId());
//		boolean isNew = true;
//		Forum forum = null;
//		if (tianziList != null && tianziList.size() > 0) {
//			forum = tianziList.get(0);
//			// 如果是一天
//			if (DateUtils.isSameDay(forum.getLastTime())) {
//				isNew = false;
//				forum.setTitle(title);
//				forum.setForumContext(context);
//				forum.setLastTime(new Timestamp(System.currentTimeMillis()));
//				userDao.updateForum(forum);
//			}
//		}
//		if (isNew) {
//			forum = new Forum();
//			forum.setForumContext(context);
//			forum.setForumType(TYPE_TIANZI);
//			forum.setLastTime(new Timestamp(System.currentTimeMillis()));
//			forum.setTianZiName(user.getUserName());
//			forum.setTitle(title);
//			forum.setCountryId(user.getCountryId());
//			userDao.insertForum(forum);
//			if (tianziList == null) {
//				tianziList = new ArrayList<Forum>();
//				tianziForumMap.put(user.getCountryId(), tianziList);
//			}
//			tianziList.add(0, forum);
//			forumMap.put(forum.getForumId(), forum);
//		}
//		Object[] arr = {forum.getForumId()};
//		this.broadcastOther(TYPE_TIANZI, arr);
		return null;
	}
	

	public Map<String, Object> getForumContextList(long userId, int type,
			int startNo, int pageNum) {
		List<Forum> forumList;
		int index = 0;
		switch (type) {
		case 1:
			User user = userService.getUserById(userId);
			forumList = tianziForumMap.get(user.getCountryId());
			if (forumList != null && forumList.size() > 0
					&& DateUtils.isSameDay(forumList.get(0).getLastTime())) {
				index = 1;
			}
			break;
		case 2:
			forumList = preForumList;
			break;
		case 3:
			forumList = nextForumList;
			break;
		default:
			throw new BaseException("类型无效");
		}
		
		return getForums(forumList, index, startNo, pageNum);
	}


	private Map<String, Object> getForums(List<Forum> forumList,
			int index, int pageNo, int pageSize) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		int total = forumList != null ? forumList.size() : 0;
		
		int pageSum = 1;
		if (total > 0) {
			pageSum = total / pageSize + (total % pageSize == 0 ? 0 : 1);
			if (pageNo > pageSum) {
				pageNo = pageSum;
			}
			int begin = (pageNo - 1) * pageSize + index;
			int end = begin + pageSize;
			if (end >= total) {
				end = total;
			}
			for (int start = begin; start < end; start++) {
				Forum forum = forumList.get(start);
				Map<String,Object> data = new HashMap<String, Object>(8);
				data.put("title", forum.getTitle());
				data.put("time", forum.getLastTime().getTime()/1000);
				data.put("tianziName", forum.getTianZiName());
				data.put("forumId", forum.getForumId());
				dataList.add(data);
			}
		}
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("dataList", dataList);
		dataMap.put("pageSum", pageSum);
		return dataMap;

	}

	@Override
	public Map<String, Object> getContextById(int forumId) {
		Forum forum = forumMap.get(forumId);
		if(forum==null){
			throw new BaseException("该帖子已经过期");
		}
		Map<String,Object> data = new HashMap<String, Object>(2);
		data.put("context", forum.getForumContext());
		data.put("title", forum.getTitle());
		data.put("tianziName", forum.getTianZiName());
		return data;
	}

	@Override
	public Map<String, Object> getCurForum(long userId) {
		User user = userService.getUserById(userId);
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		List<Forum> forumList = tianziForumMap.get(user.getCountryId());
		if(forumList!=null&&forumList.size()>0){
			Forum forum = forumList.get(0);
			if(DateUtils.isSameDay(forum.getLastTime())){
				Map<String,Object> data = new HashMap<String, Object>(8);
				data.put("title", forum.getTitle());
				data.put("time", forum.getLastTime().getTime()/1000);
				data.put("tianziName", forum.getTianZiName());
				data.put("forumId", forum.getForumId());
				dataList.add(data);
			}
			
		}
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("dataList", dataList);
		return dataMap;
	}
	
	
		public void broadcastOther(int type,Object[] ids) {

		if (broadcastMgr != null) {
			broadcastMgr.sendNotification(new AsyncWolfTask(
					"forumService", "reloadForum",
					new Object[]{type,ids}));
		}
	}

	public void reloadForum(int type,Object[] ids) {
		if (type==1) {
			//天子发言
			Forum forum = userDao.getForumById((Integer)ids[0]);
			List<Forum> forumList = tianziForumMap.get(forum.getCountryId());
			boolean isReplase = false;
			if(null!=forumList&&forumList.size()>0){
				Forum temp = forumList.get(0);
				//如果有老的，替换
				if (DateUtils.isSameDay(temp.getLastTime())) {
					forumList.set(0, forum);
					isReplase = true;
				}
			}
			//如果没有替换，则添加
			if(!isReplase){
				if (forumList == null) {
					forumList = new ArrayList<Forum>();
					tianziForumMap.put(forum.getForumId(), forumList);
				}
				forumList.add(0, forum);
			}
			forumMap.put(forum.getForumId(), forum);
		}else{
			//取出新增帖子
			List<Forum> forumList = userDao.getForumListByType(type);
			//存新贴子
			for (int i = 0; i < forumList.size(); i++) {
				this.forumMap.put(forumList.get(i).getForumId(),forumList.get(i));
			}
			//排序
			Collections.sort(forumList, sortForum);
			if(type==2){
				this.preForumList = forumList;
			}else if(type==3){
				this.nextForumList = forumList;
			}
			//删除老贴子
			for (int i = 0; i < ids.length; i++) {
				this.forumMap.remove(ids[i]);
			}
		}
	}

	class SortForum implements Comparator<Forum> {

		@Override
		public int compare(Forum o1, Forum o2) {
			return (int) (o2.getLastTime().getTime() - o1.getLastTime()
					.getTime());
		}

	}


	public void setUserDao(IUserDao userDao) {
		this.userDao = userDao;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setSensitiveWordService(ISensitiveWordService sensitiveWordService) {
		this.sensitiveWordService = sensitiveWordService;
	}

//	public void setOfficialService(IOfficialService officialService) {
//		this.officialService = officialService;
//	}

	public void setBroadcastMgr(IBroadcastProducer broadcastMgr) {
		this.broadcastMgr = broadcastMgr;
	}

}

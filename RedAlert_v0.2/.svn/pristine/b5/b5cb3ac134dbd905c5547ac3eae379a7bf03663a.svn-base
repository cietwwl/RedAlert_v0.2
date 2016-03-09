package com.youxigu.dynasty2.chat;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 频道类
 * 
 * @author Phoeboo
 * @version 1.0   2011.01.19
 */
public class ChatChannel {

	private String 	channelType;			// 频道类别
	private String 	channelId;				// 频道id
	private String 	entName;				// 频道名:英文
	private String 	cnName;					// 频道名:中文
	//private String 	color;					// 字颜色
	//private String 	joinBeanName;			// 加入频道bean
	//private int 	canMask;				// 频道是否可屏蔽   0:没限制  1:有限制
	//private int 	sendLimit;				// 在频道发送消息限制 0:没限制  1:有限制
	//private int 	joinLimit;				// 加入限制   0:没限制  1:有限制
	//private int 	maxUsers;				// 频道最大用户限制,<=0不限制
	//private boolean removeUserWhenClose=true;	//socket断开是否删除该频道的用户,默认为true
	private Map<String, ChatUser> userList  =  new ConcurrentHashMap<String,ChatUser>();		// 频道用户列表
	
	
	public ChatChannel( String channelType, String channelId ) {
		
		this.channelType = channelType;
		this.channelId   = channelId;
	}


	//玩家注册到本频道
	public void addUser( ChatUser chatUser ) {
		
		if ( chatUser == null ) return;
		userList.put( chatUser.getUserName(), chatUser );
	}

	//玩家注销本频道
	public void removeUser( ChatUser chatUser ) {
		
		if ( chatUser == null ) return;
		userList.remove( chatUser.getUserName( ) );
	}
	public void removeAllUser() {
		userList.clear();
	}


	public String getChannelType() {
		return channelType;
	}


	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}


	public String getChannelId() {
		return channelId;
	}


	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}


	public String getEntName() {
		return entName;
	}


	public void setEntName(String entName) {
		this.entName = entName;
	}


	public String getCnName() {
		return cnName;
	}


	public void setCnName(String cnName) {
		this.cnName = cnName;
	}

//
//	public String getColor() {
//		return color;
//	}
//
//
//	public void setColor(String color) {
//		this.color = color;
//	}
//
//
//	public String getJoinBeanName() {
//		return joinBeanName;
//	}
//
//
//	public void setJoinBeanName(String joinBeanName) {
//		this.joinBeanName = joinBeanName;
//	}
//
//
//	public int getCanMask() {
//		return canMask;
//	}
//
//
//	public void setCanMask(int canMask) {
//		this.canMask = canMask;
//	}
//
//
//	public int getSendLimit() {
//		return sendLimit;
//	}
//
//
//	public void setSendLimit(int sendLimit) {
//		this.sendLimit = sendLimit;
//	}
//
//
//	public int getJoinLimit() {
//		return joinLimit;
//	}
//
//
//	public void setJoinLimit(int joinLimit) {
//		this.joinLimit = joinLimit;
//	}


	public Map<String, ChatUser> getUserList() {
		return userList;
	}

	public void putAllUser(Map<String, ChatUser> users){
		userList.putAll(users);
	}

	public void setUserList( Map<String, ChatUser> userList ) {
		this.userList = userList;
	}

//
//	public int getMaxUsers() {
//		return maxUsers;
//	}
//
//
//	public void setMaxUsers(int maxUsers) {
//		this.maxUsers = maxUsers;
//	}
//
//	public boolean isfull(){
//		return maxUsers>0 &&  userList.size()> maxUsers;
//	}
//
//
//	public boolean isRemoveUserWhenClose() {
//		return removeUserWhenClose;
//	}
//
//
//	public void setRemoveUserWhenClose(boolean removeUserWhenClose) {
//		this.removeUserWhenClose = removeUserWhenClose;
//	}

	public int getCurrentUserNum(){
		return userList.size(); 
	}
}

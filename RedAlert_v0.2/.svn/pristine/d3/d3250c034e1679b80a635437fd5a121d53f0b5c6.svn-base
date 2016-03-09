package com.youxigu.dynasty2.map.domain.action;

import com.youxigu.dynasty2.user.domain.User;

/**
 * 用户操作行为
 * 这个父类主要用来处理异常消息
 * @author LK
 * @date 2016年2月5日
 */
public class UserOperAction extends TimeAction {
	private User user;

	public UserOperAction(User user, long time, int cmd) {
		this.user = user;
		super.time = time;
		super.cmd = cmd;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}

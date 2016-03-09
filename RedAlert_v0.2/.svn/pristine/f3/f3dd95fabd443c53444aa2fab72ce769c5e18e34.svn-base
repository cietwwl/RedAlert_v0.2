package com.youxigu.dynasty2.user.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.ibatis.sqlmap.engine.cache.memcached.broadcast.IBroadcastProducer;
import com.youxigu.dynasty2.chat.EventMessage;
import com.youxigu.dynasty2.chat.client.IChatClientService;
import com.youxigu.dynasty2.common.AppConstants;
import com.youxigu.dynasty2.common.service.ICommonService;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.user.service.IOperPasswordService;
import com.youxigu.dynasty2.user.service.IUserService;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.dynasty2.util.MD5;
import com.youxigu.dynasty2.util.StringUtils;
import com.youxigu.dynasty2.vip.service.IVipService;
import com.youxigu.wolf.net.AsyncWolfTask;
import com.youxigu.wolf.net.OnlineUserSessionManager;
import com.youxigu.wolf.net.UserSession;

public class OperPasswordService implements IOperPasswordService {
	private Pattern passwordReg = Pattern.compile("^[A-Za-z0-9]+$");
	
	private IUserService userService;
	private IVipService vipService;
	private ICommonService commonService;
	private IChatClientService messageService;
	private IBroadcastProducer broadcastMgr;

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public void setVipService(IVipService vipService) {
		this.vipService = vipService;
	}

	public void setCommonService(ICommonService commonService) {
		this.commonService = commonService;
	}

	public void setMessageService(IChatClientService messageService) {
		this.messageService = messageService;
	}

	public void setBroadcastMgr(IBroadcastProducer broadcastMgr) {
		this.broadcastMgr = broadcastMgr;
	}

	@Override
	public void doSetPassword(UserSession us, String newPwd) {
		long userId = us.getUserId();
		User user = userService.lockGetUser(userId);

		int vipLv = vipService.getVipLvByUserId(userId);
		if (vipLv < commonService.getSysParaIntValue(
				AppConstants.SYS_PASSWORD_OPEN_VIPLV, 8)) {
			throw new BaseException("贵族"
					+ vipLv
					+ "级才可设置二级密码");
		}

		if (newPwd.equals("") || newPwd.length() < 6) {
			throw new BaseException("密码太短");
		}

		if (newPwd.length() > 8) {
			throw new BaseException("长度不能超过8位");
		}

		if (!StringUtils.match(newPwd, passwordReg)) {
			throw new BaseException("只可输入数字或字母");
		}

		String[] array = user.parsePassword();
		if (!array[0].equals("")) {
			throw new BaseException("您已设置了密码");
		}
		array[0] = MD5.getMD5(newPwd).toLowerCase();

		user.setPassword(array[0] + "," + array[1] + "," + array[2] + ","
				+ array[3]);
		userService.doUpdateUser(user);
		us.setOldPwd(array[0]);
		us.setNewPwd("");
	}

	@Override
	public Map<String, Object> doUpdatePassword(UserSession us, String oldPwd,
			String newPwd) {
		long userId = us.getUserId();
		User user = userService.lockGetUser(userId);

		String msg = "";
		int vipLv = vipService.getVipLvByUserId(userId);
		if (vipLv < commonService.getSysParaIntValue(
				AppConstants.SYS_PASSWORD_OPEN_VIPLV, 8)) {
			msg = "贵族"
					+ vipLv
					+ "级才可设置二级密码";
		}

		if (newPwd.equals("") || newPwd.length() < 6) {
			msg = "密码太短";
		}

		if (newPwd.length() > 8) {
			msg = "长度不能超过8位";
		}

		if (!StringUtils.match(newPwd, passwordReg)) {
			msg = "只可输入数字或字母";
		}

		// 修改密码
		String[] array = user.parsePassword();
		if (oldPwd.equals("")
				|| !array[0].equals(MD5.getMD5(oldPwd).toLowerCase())) {
			msg = "旧密码错误";
		}

		int num = Integer.parseInt(array[2]);
		if (num <= 0) {
			throw new BaseException("今天操作次数已用完，请明天再试");
		}

		array[2] = Math.max((num - 1), 0) + "";
		if (msg.equals("")) {
			array[0] = MD5.getMD5(newPwd).toLowerCase();
			us.setOldPwd(array[0]);
			us.setNewPwd("");
		}

		user.setPassword(array[0] + "," + array[1] + "," + array[2] + ","
				+ array[3]);
		userService.doUpdateUser(user);

		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("msg", msg);
		params.put("updateNum", Integer.parseInt(array[2]));
		return params;

	}

	@Override
	public Map<String, Object> doDelPassword(UserSession us, String oldPwd) {
		long userId = us.getUserId();
		User user = userService.lockGetUser(userId);
		String msg = "";
		// int vipLv = vipService.getVipLvByUserId(userId);
		// if(vipLv <
		// commonService.getSysParaIntValue(AppConstants.SYS_PASSWORD_OPEN_VIPLV,
		// 8)) {
		// msg =
		// com.youxigu.dynasty2.i18n.MarkupMessages.getString("UserService_32")+vipLv+com.youxigu.dynasty2.i18n.MarkupMessages.getString("UserService_101");
		// }

		String[] array = user.parsePassword();

		if (!array[0].equals(MD5.getMD5(oldPwd).toLowerCase())) {
			msg = "旧密码错误";
		}

		int num = Integer.parseInt(array[1]);
		if (num <= 0) {
			throw new BaseException("今天操作次数已用完，请明天再试");
		}
		array[1] = Math.max((num - 1), 0) + "";
		if (msg.equals("")) {
			array[0] = "";
			us.setOldPwd("");
			us.setNewPwd("");
		}

		user.setPassword(array[0] + "," + array[1] + "," + array[2] + ","
				+ array[3]);
		userService.doUpdateUser(user);

		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("msg", msg);
		params.put("delNum", Integer.parseInt(array[1]));
		return params;
	}

	@Override
	public boolean doGMDelPassword(long userId) {
		try {
			User user = userService.lockGetUser(userId);
			String[] array = user.parsePassword();
			user.setPassword("" + "," + array[1] + "," + array[2] + ","
					+ array[3]);
			userService.doUpdateUser(user);

			UserSession us = OnlineUserSessionManager
					.getUserSessionByUserId(userId);
			if (us != null) {
				us.setOldPwd("");
				us.setNewPwd("");
			}
			// 推消息给前台，删除二级密码成功
			messageService.sendEventMessage(user.getUserId(),
					EventMessage.TYPE_FIRST_PASSWOED_DEL, null);

			broadcastMgr.sendNotification(new AsyncWolfTask("userService",
					"doBroadcastDelPassword", new Object[] { userId }));
			return true;
		} catch (Exception e) {

		}
		return false;
	}

	@Override
	public void doBroadcastDelPassword(long userId) {
		try {
			User user = userService.lockGetUser(userId);
			String[] array = user.parsePassword();
			if (!array[0].equals("")) {
				user.setPassword("" + "," + array[1] + "," + array[2] + ","
						+ array[3]);
				userService.doUpdateUser(user);
			}

			UserSession us = OnlineUserSessionManager
					.getUserSessionByUserId(userId);
			if (us != null) {
				us.setOldPwd("");
				us.setNewPwd("");
			}
		} catch (Exception e) {

		}
	}

	@Override
	public void doCheakPassword(UserSession us, String oldPwd) {
		userService.lockGetUser(us.getUserId());
		if (us.getOldPwd() != null && !us.getOldPwd().equals("")) {
			if (us.getOldPwd().equals(MD5.getMD5(oldPwd).toLowerCase())) {
				us.setNewPwd(MD5.getMD5(oldPwd).toLowerCase());
			} else {
				throw new BaseException("密码错误");
			}
		} else {
			throw new BaseException("未设置密码");
		}
	}

	@Override
	public Map<String, Object> getPasswordInfo(long userId,
			Map<String, Object> params) {
		User user = userService.getUserById(userId);
		String[] array = user.parsePassword();
		params.put("delNum", Integer.parseInt(array[1]));
		params.put("updateNum", Integer.parseInt(array[2]));
		return params;
	}

}

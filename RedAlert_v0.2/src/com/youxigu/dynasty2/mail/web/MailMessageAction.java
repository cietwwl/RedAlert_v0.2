package com.youxigu.dynasty2.mail.web;

import com.google.protobuf.InvalidProtocolBufferException;
import com.manu.core.ServiceLocator;
import com.youxigu.dynasty2.common.AppConstants;
import com.youxigu.dynasty2.common.service.ICommonService;
import com.youxigu.dynasty2.core.flex.amf.BaseAction;
import com.youxigu.dynasty2.core.wolf.IWolfClientService;
import com.youxigu.dynasty2.entity.domain.Item;
import com.youxigu.dynasty2.entity.service.IEntityService;
import com.youxigu.dynasty2.mail.domain.MailMessage;
import com.youxigu.dynasty2.mail.proto.MailMsg;
import com.youxigu.dynasty2.mail.proto.MailMsg.*;
import com.youxigu.dynasty2.mail.service.IMailMessageService;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.user.service.IUserService;
import com.youxigu.dynasty2.user.service.impl.UserService;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.dynasty2.util.DateUtils;
import com.youxigu.dynasty2.mail.domain.AsyncCmdWolfTask;
import com.youxigu.wolf.net.Response;
import com.youxigu.wolf.net.UserSession;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

//        +-------------------------+     +-----------------------------------------------------+
//        |                         |     |                                                     |
//        |        NodeServer       |     |                       JobServer                     |
//        |                         |     |                                                     |
//        |  +--------------------  |     |   +-------------------+      +------------------+   |
//        |  |                   |  |     |   |                   |      |                  |   |
//        |  | MailMessageAction | +--------> | MailTaskDispacher | +--> | MailServerAction |   |
//        |  |                   |  |     |   |                   |      |                  |   |
//        |  +-------------------+  |     |   +-------------------+      +------------------+   |
//        +-------------------------+     |       (Wolf Thread)           (Mail Task Thread)    |
//                                        |                                                     |
//                                        +-----------------------------------------------------+

/**
 * 邮件协议
 * @author Dagangzi
 *
 */
public class MailMessageAction extends BaseAction {

    public static final String MAIL_TASK_DISPATCHER = "mailTaskDispatcher";
    private IMailMessageService mailMessageService;
	private ICommonService commonService;
	private IEntityService entityService;
	private IUserService userService;
    private IWolfClientService jobServerClient;

	public void setCommonService(ICommonService commonService) {
		this.commonService = commonService;
	}

	public void setMailMessageService(IMailMessageService mailMessageService) {
		this.mailMessageService = mailMessageService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public void setEntityService(IEntityService entityService) {
		this.entityService = entityService;
	}

    public void setJobServerClient(IWolfClientService jobServerClient) {
        this.jobServerClient = jobServerClient;
    }

	/**
	 * 用户发邮件-11001
	 * 
	 * @param params
     * @param context
	 */
	public Object sendMail(Object params, Response context) {
		if (!isOpenMail()) {
			throw new BaseException("邮件功能暂未开放");
		}
		UserService userService = (UserService) ServiceLocator.getSpringBean("userService");
		UserSession session = getUserSession(context);

		Request11001Define request = (Request11001Define) params;
		String title = request.getTitle();
		String comment = request.getContent();

		List<String> userNames = request.getUserNamesList();

		if (userNames == null || userNames.size() == 0) {
			throw new BaseException("没有收件人");
		}
		if (userNames.size() > 5) {
			throw new BaseException("收件人不能超过5个");
		}
		List<Long> uids = new LinkedList<Long>();
		// StringBuffer sendFailUaer = new StringBuffer();// 发送失败的玩家
		for (Object tmp : userNames) {
			String toUserName = (String) tmp;
			if (toUserName == null || toUserName.trim().equals(""))
				continue;

			User toUsr = userService.getUserByName(toUserName);
			if (toUsr == null) {
				throw new BaseException("收件人不存在");
				// if (sendFailUaer.toString().length() > 0) {
				// sendFailUaer.append(",");
				// }
				// sendFailUaer.append(toUserName);
				// continue;
			}
			// boolean isBlack = friendService.isBlack(toUsr.getUserId(),
			// session
			// .getUserId());
			// if (isBlack) {
			// throw new BaseException(toUserName + " 拒绝接受您的邮件.");
			// }

			long toUserId = toUsr.getUserId();
			uids.add(toUserId);

		}

        AsyncCmdWolfTask task = new AsyncCmdWolfTask();
        task.setCmd(11001);
        task.setUserId(session.getUserId());
        task.setServiceName(MAIL_TASK_DISPATCHER);
        task.setMethodName("sendMail");
        task.setParams(new Object[]{session.getUserId(), uids, title, comment});
        jobServerClient.asynSendTask(task);
        return null;
	}

	private boolean isOpenMail() {
		int opened = commonService.getSysParaIntValue(AppConstants.SYS_MAIL_OPEN_STATUS,
				AppConstants.SYS_OPNE_STATUS_DEFAULTVALUE);
		return opened == 1;
	}

	/**
	 * 用户获取自己的邮件-11003
	 * 
	 * @param params
     * @param context
	 * @return
	 */
	public Object getUserMails(Object params, Response context) {
		// TODO:这里应该只向前台传邮件的名称，不传内容，另外加一个显示邮件内容的接口
		Request11003Define request = (Request11003Define) params;
		if (!isOpenMail()) {
			throw new BaseException("邮件功能暂未开放");
		}
		UserSession session = getUserSession(context);
		int type = MailMessage.TYPE_NORMAL;
		if (request.hasType()) {
			type = request.getType();
		}

		int status = -1;
		if (request.hasStatus()) {
			status = request.getStatus();
		}
		int pageNo = 0;// 当前页
		if (request.hasPageNo()) {
			pageNo = request.getPageNo();
		}
		int pageSize = 13;
		if (request.hasPageSize()) {
			pageSize = request.getPageSize();
		}

		//返回值
		MailMsg.Response11003Define.Builder response = MailMsg.Response11003Define.newBuilder();
		response.setResponseHead(getResponseHead(request.getCmd()));

		//本页的邮件列表
		Map<String, Object> map = mailMessageService.getUserMessages(session.getUserId(), (byte) type, (byte) status,
				pageNo, pageSize);
		List<MailMessage> pagedMessages = (List<MailMessage>) map.get("pagedMessages");
		if (pagedMessages.size() > 0) {
			for (MailMessage mail : pagedMessages) {
				response.addDatas(this.buildMailMessage(mail));
			}
		}

		response.setPageNo((Integer) map.get("pageNo"));
		response.setPageSize((Integer) map.get("pageSize"));
		response.setPages((Integer) map.get("pages"));
		response.setTotal((Integer) map.get("count"));
		response.setType(type);

		MailMsg.MailCount.Builder mailCount = MailMsg.MailCount.newBuilder();
		mailCount.setNewNormal((Integer) map.get("unRead_normal"));
		mailCount.setNewSystem((Integer) map.get("unRead_sys"));
		mailCount.setNewPVP((Integer) map.get("unRead_pvp"));
		response.setMailCount(mailCount.build());
        response.setServerTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		return response.build();
	}

	/**
	 * 标记邮件已读-11005
	 * 
	 * @param params
	 * @param context
	 * @return
	 */
	public Object setMailRead(Object params, Response context) {
		if (!isOpenMail()) {
			throw new BaseException("邮件功能暂未开放");
		}
		Request11005Define request = (Request11005Define) params;
		UserSession session = getUserSession(context);
		int msgId = request.getMsgId();

        AsyncCmdWolfTask task = new AsyncCmdWolfTask();
        task.setCmd(11005);
        task.setUserId(session.getUserId());
        task.setServiceName(MAIL_TASK_DISPATCHER);
        task.setMethodName("setMailRead");
        task.setParams(new Object[]{session.getUserId(),msgId});
        jobServerClient.asynSendTask(task);
        return null;
	}

	//
	// /**
	// * 获取邮件详细信息
	// *
	// * @param messageId
	// * @return
	// */
	// public MailMessage getMailMessage(int messageId) {
	// MailMessage message =
	// mailMessageService.getUserMessage(super.getUserIdFromSess(), messageId);
	//
	// if (message.getStatus() == MailMessage.STATUS_UNREAD) {
	// message.setStatus(MailMessage.STATUS_READ);
	// mailMessageService.updateMessage2Read(super.getUserIdFromSess(),
	// messageId);
	// }
	//
	// return message;
	//
	// }
	//
	/**
	 * 获取用户邮件附件-11007
	 * 
	 * @param params
     * @param context
	 */
	public Object getMailAppendix(Object params, Response context) {
		if (!isOpenMail()) {
			throw new BaseException("邮件功能暂未开放");
		}

		Request11007Define request = (Request11007Define) params;
		UserSession session = getUserSession(context);
		int messageId = request.getMsgId();
		int pos = 0;
		if (request.hasPos()) {
			pos = request.getPos();
		}

        AsyncCmdWolfTask task = new AsyncCmdWolfTask();
        task.setCmd(11007);
        task.setUserId(session.getUserId());
        task.setServiceName(MAIL_TASK_DISPATCHER);
        task.setMethodName("extractMailAppendix");
        task.setParams(new Object[]{session.getUserId(), messageId, pos});
        jobServerClient.asynSendTask(task);
        return null;
	}

	/**
	 * 删除邮件-11009
	 * 
	 * @param params
     * @param context
	 */
	public Object deleteMessages(Object params, Response context) {
		if (!isOpenMail()) {
			throw new BaseException("邮件功能暂未开放");
		}
		Request11009Define request = (Request11009Define) params;
		UserSession session = getUserSession(context);
		List<Integer> msgIds = request.getMsgIdsList();

        AsyncCmdWolfTask task = new AsyncCmdWolfTask();
        task.setCmd(11009);
        task.setUserId(session.getUserId());
        task.setServiceName(MAIL_TASK_DISPATCHER);
        task.setMethodName("deleteMessages");
        task.setParams(new Object[]{session.getUserId(), msgIds});
        jobServerClient.asynSendTask(task);
        return null;
	}

    /**
	 * 批量获取用户邮件附件-11011
	 *  @param params
     * @param context
     */
	public Object batchExtractMailAppendix(Object params, Response context) {
		if (!isOpenMail()) {
			throw new BaseException("邮件功能暂未开放");
		}

		Request11011Define request = (Request11011Define) params;
		UserSession session = getUserSession(context);
        int type = request.getType();
        List<Integer> msgIds = request.getMsgIdList();

        AsyncCmdWolfTask task = new AsyncCmdWolfTask();
        task.setCmd(11011);
        task.setUserId(session.getUserId());
        task.setServiceName(MAIL_TASK_DISPATCHER);
        task.setMethodName("batchExtractMailAppendix");
        task.setParams(new Object[]{session.getUserId(), type, msgIds});
        jobServerClient.asynSendTask(task);
        return null;
	}


    /**
     * 删除用户所有邮件（无附件邮件）-11013
     * @param params
     * @param context
     */
    public Object deleteAllMessages(Object params, Response context) {
 		if (!isOpenMail()) {
			throw new BaseException("邮件功能暂未开放");
		}
		MailMsg.Request11013Define request = (MailMsg.Request11013Define) params;
		UserSession session = getUserSession(context);
        long userId = session.getUserId();

        AsyncCmdWolfTask task = new AsyncCmdWolfTask();
        task.setCmd(11013);
        task.setUserId(session.getUserId());
        task.setServiceName(MAIL_TASK_DISPATCHER);
        task.setMethodName("deleteAllMessages");
        task.setParams(new Object[]{userId});
        jobServerClient.asynSendTask(task);
        return null;
   }

	/**
	 * 构造protobuf for MailMessage
	 * @param message
	 * @return
	 */
	private MailMsg.MailMessage buildMailMessage(MailMessage message) {
		byte type = message.getMessageType();
		long senderId = message.getSendUserId();
		//单封邮件的信息
		MailMsg.MailMessage.Builder mailMessage = MailMsg.MailMessage.newBuilder();
		mailMessage.setMsgId(message.getMessageId());
		mailMessage.setType(type);
		mailMessage.setSenderId(senderId);
		if (senderId > 0) {
			User user = userService.getUserById(senderId);
			if (user != null) {
				mailMessage.setSenderName(user.getUserName());
			}
		}
		Timestamp dttm = message.getSendDttm();
		if (dttm != null) {
			mailMessage.setSendDttm(DateUtils.datetime2Text(dttm));
		}
		mailMessage.setTitle(message.getTitle());
		mailMessage.setContent(message.getComment());
		mailMessage.setStatus(message.getStatus());
		if (message.getItemNum0() > 0) {
			mailMessage.addAppendixDetail(this.buildAppendixDetail(Math.abs(message.getEntityId0()),
					message.getItemNum0(), message.getStatus0ByPos(0)));
		}

		if (message.getItemNum1() > 0) {
			mailMessage.addAppendixDetail(this.buildAppendixDetail(Math.abs(message.getEntityId1()),
					message.getItemNum1(), message.getStatus0ByPos(1)));

		}

		if (message.getItemNum2() > 0) {
			mailMessage.addAppendixDetail(this.buildAppendixDetail(Math.abs(message.getEntityId2()),
					message.getItemNum2(), message.getStatus0ByPos(2)));
		}

		if (message.getItemNum3() > 0) {
			mailMessage.addAppendixDetail(this.buildAppendixDetail(Math.abs(message.getEntityId3()),
					message.getItemNum3(), message.getStatus0ByPos(3)));
		}

		if (message.getItemNum4() > 0) {
			mailMessage.addAppendixDetail(this.buildAppendixDetail(Math.abs(message.getEntityId4()),
					message.getItemNum4(), message.getStatus0ByPos(4)));

		}

		if (message.getItemNum5() > 0) {
			mailMessage.addAppendixDetail(this.buildAppendixDetail(Math.abs(message.getEntityId5()),
					message.getItemNum5(), message.getStatus0ByPos(5)));

		}

		//protobuf 反序列化后的战报
		if (message.getMap() != null) {
			MailMsg.CombatMail combatMail = null;
			try {
				if (message.getMap() != null) {
					combatMail = MailMsg.CombatMail.parseFrom(message.getMap());
				}
			} catch (InvalidProtocolBufferException e) {
				//				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (combatMail != null) {
				mailMessage.setCombatDetail(combatMail);
			}
		}
		return mailMessage.build();
	}

	/**
	 * 构造protobuf for AppendixDetail
	 * @param itemId
	 * @param num
	 * @param status0
	 * @return
	 */
	private MailMsg.AppendixDetail buildAppendixDetail(int itemId, int num, int status0) {
		MailMsg.AppendixDetail.Builder appendixDetail = MailMsg.AppendixDetail.newBuilder();
		appendixDetail.setItemId(itemId);
		appendixDetail.setNum(num);
		appendixDetail.setStatus(status0 == MailMessage.APPENDIX_NONE ? 2 : status0);

		//可能是运营礼包
		Item item = (Item) entityService.getEntity(itemId);
		if (item != null && item.toOnlinePack() != null) {
			appendixDetail.setOnlinePack(item.toOnlinePack().build());
		}
		return appendixDetail.build();
	}

	/**
	 * 构造protobuf for MailCount
	 * @param userId
	 * @return
	 */
	private MailMsg.MailCount buildMailCount(long userId) {

		// 获取统计计数
		// int[] mailCount = getMailMessageCount(userId);
		List<MailMessage> messages = mailMessageService.listMessages(userId);

		int unRead_normal = 0;
		int unRead_sys = 0;
		int unRead_pvp = 0;

		for (MailMessage message : messages) {
			byte type = message.getMessageType();
			byte stat = message.getStatus();

			if (stat == MailMessage.STATUS_UNREAD) {
				// 统计各个类型邮件的未读数量
				if (type == MailMessage.TYPE_NORMAL) {
					unRead_normal++;
				} else if (type == MailMessage.TYPE_SYSTEM) {
					unRead_sys++;
				} else if (type == MailMessage.TYPE_SYSTEM_PVP) {
					unRead_pvp++;
				}
			}

		}

		MailMsg.MailCount.Builder mailCount = MailMsg.MailCount.newBuilder();
		mailCount.setNewNormal(unRead_normal);
		mailCount.setNewSystem(unRead_sys);
		mailCount.setNewPVP(unRead_pvp);

		return mailCount.build();
	}
}

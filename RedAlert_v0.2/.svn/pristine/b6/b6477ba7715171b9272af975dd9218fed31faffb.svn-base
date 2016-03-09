package com.youxigu.dynasty2.mail.web;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.youxigu.dynasty2.chat.proto.CommonHead.*;
import com.youxigu.dynasty2.entity.domain.Item;
import com.youxigu.dynasty2.entity.service.IEntityService;
import com.youxigu.dynasty2.mail.domain.MailMessage;
import com.youxigu.dynasty2.mail.proto.MailMsg;
import com.youxigu.dynasty2.mail.proto.MailMsg.*;
import com.youxigu.dynasty2.mail.service.IMailMessageService;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.user.service.IUserService;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.dynasty2.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MailServerAction {
    private static final Logger log = LoggerFactory.getLogger(MailServerAction.class);
    private IMailMessageService mailMessageService;
	private IEntityService entityService;
	private IUserService userService;

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public void setEntityService(IEntityService entityService) {
		this.entityService = entityService;
	}

    public void setMailMessageService(IMailMessageService mailMessageService) {
        this.mailMessageService = mailMessageService;
    }

	private static ResponseHead getResponseHead(int requestCmd) {
		//返回值
		ResponseHead.Builder headBr = ResponseHead.newBuilder();
		headBr.setCmd(requestCmd +1);
		headBr.setRequestCmd(requestCmd);
		return headBr.build();
	}

    public Message sendMail(long sendUserId, List<Long> receiveUserIds, String title, String comment){
        if (receiveUserIds != null && receiveUserIds.size() > 0) {
			MailMessage msg = new MailMessage();
			msg.setSendUserId(sendUserId);
			msg.setReceiveUserId(0);//createSimpleMessages方法中会修改此字段值
			msg.setTitle(title);
			msg.setComment(comment);
			msg.setMessageType(MailMessage.TYPE_NORMAL);
			msg.setSendDttm(new Timestamp(System.currentTimeMillis()));
			mailMessageService.createSimpleMessages(msg, receiveUserIds);
		}

		//返回值
		Response11001Define.Builder response = Response11001Define.newBuilder();
		response.setResponseHead(getResponseHead(11001));
        return response.build();
    }

    public Message setMailRead(long userId, int msgId){
		mailMessageService.updateMessage2Read(userId, msgId);

		//返回值
		Response11005Define.Builder response = Response11005Define.newBuilder();
		response.setResponseHead(getResponseHead(11005));
		response.setMailCount(buildMailCount(userId));

        return response.build();
    }

    public Message extractMailAppendix(long receiveUserId, int messageId, int pos){
		MailMessage message = null;
		if (pos >= 1) {
            //把pos值从proto协议中的1~6，改为内部使用的0~5
            pos -= 1;
			message = mailMessageService.updateMessage2fetchAppendix(receiveUserId,
                    messageId, pos, new HashMap<Integer, Integer>());
		} else {
			mailMessageService.updateMessage2fetchAppendix(receiveUserId, messageId,
                    new HashMap<Integer, Integer>());
		}

		//返回值
		Response11007Define.Builder response = Response11007Define.newBuilder();
		response.setResponseHead(getResponseHead(11007));
		if (message != null) {
			response.setMail(buildMailMessage(message));
		}

        return response.build();
    }

    public Message batchExtractMailAppendix(long userId, int type, List<Integer> msgIds){
        Map<Integer, Integer> itemMap = new HashMap<Integer, Integer>();
        String errDesc = null;
        try {
            mailMessageService.extractAppendix(userId, type, msgIds, itemMap);
        }catch (Throwable e){
            if(log.isErrorEnabled()){
                log.error("批量提取邮件附件出现异常", e);
            }
            if(e instanceof BaseException){
                errDesc = ((BaseException)e).getErrMsg();
            }
            else{
                errDesc = "系统异常，稍后再试";
            }
        }

        //返回值
        Response11011Define.Builder response = Response11011Define.newBuilder();
        response.setResponseHead(getResponseHead(11011));
        response.setMailCount(buildMailCount(userId));
        for(Map.Entry<Integer, Integer> entry : itemMap.entrySet()){
            ItemInfo.Builder iib = ItemInfo.newBuilder();
            iib.setEntId(entry.getKey());
            iib.setNum(entry.getValue());
            response.addItems(iib.build());
        }
        if(errDesc != null){
            response.setErrDesc(errDesc);
        }

        return response.build();
    }

    public Message deleteMessages(long userId, List<Integer> msgIds) {
		if (msgIds != null && msgIds.size() > 0) {
			int[] ids = new int[msgIds.size()];
			for (int i = 0; i < msgIds.size(); i++) {
				ids[i] = msgIds.get(i);
			}
			mailMessageService.deleteMessage(userId, ids);
		}

		//返回值
		Response11009Define.Builder response = Response11009Define.newBuilder();
		response.setResponseHead(getResponseHead(11009));
		response.setMailCount(buildMailCount(userId));
        return response.build();
    }

    public Message deleteAllMessages(long userId){
        List<MailMessage> msgs = mailMessageService.getUserAllMessages(userId);
        if (msgs != null && msgs.size() > 0) {
            for (MailMessage message : msgs) {
                if (message != null && message.getReceiveUserId() == userId) {
                    if (message.getStatus0() == 0) {
                        mailMessageService.deleteMessage(message);
                    }
                }
            }
        }

		//返回值
		MailMsg.Response11013Define.Builder response = MailMsg.Response11013Define.newBuilder();
		response.setResponseHead(getResponseHead(11013));
		response.setMailCount(buildMailCount(userId));
        return response.build();
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
	private MailCount buildMailCount(long userId) {

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

		MailCount.Builder mailCount = MailCount.newBuilder();
		mailCount.setNewNormal(unRead_normal);
		mailCount.setNewSystem(unRead_sys);
		mailCount.setNewPVP(unRead_pvp);

		return mailCount.build();
	}
}

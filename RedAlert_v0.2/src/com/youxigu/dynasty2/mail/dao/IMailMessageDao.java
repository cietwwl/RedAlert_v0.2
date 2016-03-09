package com.youxigu.dynasty2.mail.dao;

import java.util.List;

import com.youxigu.dynasty2.mail.domain.MailMessage;

/**
 * 站内邮件（消息）Dao
 * 
 * @author Administrator
 * 
 */
public interface IMailMessageDao {

	/**
	 * 创建邮件
	 * 
	 * @param message
	 */
	void createMessage(MailMessage message);

	/**
	 * 
	 * @param receiveUserId
	 * @param messageType
	 *            消息类型 ：0 普通 >=1系统
	 * @param status
	 *            0：未读 1已读
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */


	
	/**
	 * 
	 * @param receiveUserId

	 * @param status
	 *            0：未读 1已读
	 * @return
	 */
	 List<MailMessage> getUserMessages(long receiveUserId);
	/**
	 * 根据消息Id得到消息
	 * 
	 * @param receiveUserId
	 * @param messageId
	 * @return
	 */
	MailMessage getUserMessage(long receiveUserId, int messageId);

//	public List<MailCount> getMailMessageCount(long userId);
	
	/**
	 * 更新消息:读的时间/状态
	 * 
	 * @param message
	 */
	void updateMessage2Read(MailMessage message);

	/**
	 * 更新消息：附件标志为已经获取
	 * 
	 * @param receiveUserId
	 * @param messageId
	 */
	int updateMessage2fetchAppendix(MailMessage message);

	/**
	 * 删除消息：目前是将消息状态置为2
	 * 
	 * @param receiveUserId
	 * @param messageId
	 */
	void deleteMessage(MailMessage message);

	/**
	 * 删除指定天数以前的没有附件的邮件以及带附件的邮件
	 * 
	 * @param day
	 */
	public void deleteMessage(int day,int appendixDay);
	

	
}

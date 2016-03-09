package com.youxigu.dynasty2.mail.service;

import java.util.List;
import java.util.Map;

import com.youxigu.dynasty2.combat.domain.Combat;
import com.youxigu.dynasty2.mail.domain.MailMessage;

/**
 * 
 *站内邮件功能接口
 */
public interface IMailMessageService {

	/**
	 * 发送邮件消息
	 * 
	 * @param message
	 */
	void createSimpleMessage(MailMessage message);

	//void createSimpleMessage( long fromUserId,long toUserId,String title,String context, byte type);

    /**
     * 发送战报
     * @param toUserId
     * @param title
     * @param comment
     * @param combat
     */
    void createPvpMessage(long toUserId, String title, String comment, Combat combat);

    /**
	 * 发送系统邮件消息，通常是系统邮件使用
	 * 
	 * @param message
	 */
	void createSystemMessage(MailMessage message);

	void createSystemMessage(long toUserId, String title, String context, byte type);

	//void createSystemMessage(MailMessage message, List<Long> userIds);

	/**
	 * 发送系统邮件消息，使用模板格式化邮件消息，通常是系统邮件使用
	 * 
	 * @param message
	 */
	void createSystemMessageWithTemplate(MailMessage message, String templateName);

	void createSystemMessageWithTemplate(MailMessage message, Map<String, Object> context, String templateName);

	void createSystemMessageWithTemplate(long toUserId, String title, String context, String templateName, byte type);

	void createSystemMessageWithTemplate(MailMessage message, List<Long> userIds, String templateName);

	void createSystemMessageWithTemplate(long toUserId, String title, Map<String, Object> context, String templateName,
			byte type);

	/**
	 * 根据模板得到邮件内容
	 * @param context
	 * @param templateName
	 * @return
	 */
	String getContextByTemplate(Map<String, Object> context, String templateName);

	/**
	 * 发送消息给一组用户(联盟/战场等)
	 * 
	 * @param message
	 * @param userIds
	 */
	void createSimpleMessages(MailMessage message, List<Long> userIds);

    /**
     * 获取用户所有邮件
     * @param receiveUserId
     * @return
     */
    List<MailMessage> getUserAllMessages(long receiveUserId);

    /**
	 * 
	 * @param receiveUserId
	 * @param messageType
	 *            消息类型 ：0 普通 10系统
	 * @param status
	 *            0：未读 1已读
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	Map<String,Object> getUserMessages(long receiveUserId, byte messageType, byte status, int pageNo, int pageSize);

	/**
	 * 根据消息Id得到消息
	 * 
	 * @param receiveUserId
	 * @param messageId
	 * @return
	 */
	MailMessage getUserMessage(long receiveUserId, int messageId);

	/**
	 * 更新消息:读的时间/状态
	 * 
	 * @param receiveUserId
	 */
	void updateMessage2Read(long receiveUserId, int messageId);

	/**
     * 提取邮件附件中指定格子的道具
     * @param receiveUserId
     * @param messageId
     * @param pos 格子id，0~5
     * @param appendices 提取到的道具entId及数量
     */
	MailMessage updateMessage2fetchAppendix(long receiveUserId, int messageId, int pos, Map<Integer, Integer> appendices);

	/**
	 * 提取邮件附件的全部道具
     * @param receiveUserId
     * @param messageId
     * @param appendices 提取到的道具entId及数量
     */
	MailMessage updateMessage2fetchAppendix(long receiveUserId, int messageId, Map<Integer, Integer> appendices);

    /**
     * 提取指定邮件附件的全部道具
     * @param receiveUserId
     * @param message
     * @param appendices 提取到的道具entId及数量
     * @param throwException 如果邮件无附件可提取，是否抛出异常，true表示抛出
     * @return
     */
    MailMessage updateMessage2fetchAppendix(long receiveUserId, MailMessage message, Map<Integer, Integer> appendices, boolean throwException);

    /**
     * 批量提取邮件附件。
     * 每封邮件的提取是一个事物，单个邮件提取出错时，不影响前序已经提取的邮件附件
     * @param receiveUserId
     * @param type 0:提取msgIds指定的邮件  1:提取用户全部邮件
     * @param msgIds type为0时有效，指定待提取邮件的Id
     * @param appendices 提取到的道具entId及数量
     */
    void extractAppendix(long receiveUserId, int type, List<Integer> msgIds, Map<Integer, Integer> appendices);

	/**
	 * 删除消息
	 *
	 * @param receiveUserId
	 * @param messageId
	 */
	void deleteMessage(long receiveUserId, int messageId);

    void deleteMessage(MailMessage message);

    void deleteMessage(long receiveUserId, int[] messageIds);

    /**
	 * 系统任务 清理过期的邮件
	 */
	void cleanMessage();
	
	/**
	 * 取得收件人的所有邮件
	 * @param receiveUserId
	 * @return
	 */
	List<MailMessage> listMessages(long receiveUserId);

    /**
     * 创建邮件，仅供内部测试使用。（GmTool）
     * @param sendUserId
     * @param receiveUserId
     * @param sendTime
     * @param readTime
     * @param comment
     * @param title
     * @param status
     * @param mailType
     * @param appendix
     */
    void createMailInternal(long sendUserId, long receiveUserId, String sendTime,
                            String readTime, String comment, String title,
                            byte status, byte mailType, String appendix);
}

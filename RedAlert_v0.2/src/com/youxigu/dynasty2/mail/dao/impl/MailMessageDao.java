package com.youxigu.dynasty2.mail.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibatis.sqlmap.engine.config.ShardingConfig;
import com.ibatis.sqlmap.engine.impl.SqlMapClientImpl;
import com.manu.core.base.BaseDao;
import com.youxigu.dynasty2.mail.dao.IMailMessageDao;
import com.youxigu.dynasty2.mail.domain.MailMessage;

@SuppressWarnings("unchecked")
public class MailMessageDao extends BaseDao implements IMailMessageDao {

	@Override
	public void createMessage(MailMessage message) {
		this.getSqlMapClientTemplate().insert("insertMessage", message);

	}

	@Override
	public MailMessage getUserMessage(long receiveUserId, int messageId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("receiveUserId", receiveUserId);
		params.put("messageId", messageId);
		return (MailMessage) this.getSqlMapClientTemplate().queryForObject(
				"getMessageById", params);
	}

	@Override
	public List<MailMessage> getUserMessages(long receiveUserId) {
		return this.getSqlMapClientTemplate().queryForList("getUserMessages",
				receiveUserId);
	}

	@Override
	public void updateMessage2Read(MailMessage message) {
		this.getSqlMapClientTemplate().update("updateMessage", message);
	}

	@Override
	public int updateMessage2fetchAppendix(MailMessage message) {
		// return this.getSqlMapClientTemplate().update(
		// "updateMessage2fetchAppendix", message);
		return this.getSqlMapClientTemplate().update("updateMessage", message);
	}

	@Override
	public void deleteMessage(MailMessage message) {
		// Map<String, Object> params = new HashMap<String, Object>();
		// params.put("receiveUserId", receiveUserId);
		// params.put("messageId", messageId);
		// params.put("status", MailMessage.STATUS_DELETE);
		this.getSqlMapClientTemplate().delete("deleteMessage", message);

	}

	@Override
	public void deleteMessage(int day, int appendixDay) {
		// 删除大于指定天数的邮件
		int shardNum = 1;
		SqlMapClientImpl client = (SqlMapClientImpl) this.getSqlMapClient();
		ShardingConfig conf = client.delegate.getShardingConfig("message");
		if (conf != null) {
			if (conf.getProperties() != null) {
				shardNum = Integer.parseInt(conf.getProperties().getProperty(
						"shardingNum", "1"));
			}
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("day", day);
		for (int i = 0; i < shardNum; i++) {
			params.put("shardNum", i);
			this.getSqlMapClientTemplate().delete("deleteOverDueMessage",
					params);
		}

		params.put("day", appendixDay);
		for (int i = 0; i < shardNum; i++) {
			params.put("shardNum", i);
			this.getSqlMapClientTemplate().delete(
					"deleteOverDueAppendixMessage", params);
		}

		// TODO: 删除每个用户大于100条的邮件

	}

}

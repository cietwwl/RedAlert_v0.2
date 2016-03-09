package com.youxigu.dynasty2.treasury.service.impl;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.shardbatis.spring.jdbc.transaction.DefaultTransactionListener;
import org.springframework.transaction.support.DefaultTransactionStatus;

import com.manu.core.ServiceLocator;
import com.youxigu.dynasty2.common.AppConstants;
import com.youxigu.dynasty2.mail.domain.MailMessage;
import com.youxigu.dynasty2.mail.service.IMailMessageService;
import com.youxigu.dynasty2.treasury.service.ITreasuryFullListener;

/**
 * 背包满了加到邮件
 * 
 * @author Administrator
 * 
 */
public class TreasuryFullListener extends DefaultTransactionListener implements
		ITreasuryFullListener {
	protected IMailMessageService mailMessageService;

	private static ThreadLocal<Map<Long, FullItems>> allItems = new ThreadLocal<Map<Long, FullItems>>();

	public void setMailMessageService(IMailMessageService mailMessageService) {
		this.mailMessageService = mailMessageService;
	}

	@Override
	public void doTreasuryFull(long userId, int itemId, int num, boolean isGift) {

		Map<Long, FullItems> userItems = allItems.get();
		if (userItems == null) {
			userItems = new HashMap<Long, FullItems>();
			allItems.set(userItems);
		}
		FullItems fullItems = userItems.get(userId);
		if (fullItems == null) {
			fullItems = new FullItems();
			userItems.put(userId, fullItems);
		}
		fullItems.addItem(itemId, num, isGift);

	}

	public void doCommitBefore(DefaultTransactionStatus status) {
		if (mailMessageService == null) {
			mailMessageService = (IMailMessageService) ServiceLocator
					.getSpringBean("mailMessageService");
		}

		try {
			if (mailMessageService == null) {
				return;
			}
			Map<Long, FullItems> items = allItems.get();
			if (items != null) {
				Iterator<Map.Entry<Long, FullItems>> lit = items.entrySet()
						.iterator();
				while (lit.hasNext()) {
					Map.Entry<Long, FullItems> entry = lit.next();
					long userId = entry.getKey();
					FullItems fullItems = entry.getValue();
					if (fullItems.gifts != null) {
						addItemToMail(userId, fullItems.gifts.values(),
								(byte) 1);
					}
					if (fullItems.notGifts != null) {
						addItemToMail(userId, fullItems.notGifts.values(),
								(byte) 0);
					}
				}
			}

		} finally {
			allItems.remove();
		}
	}

	private void addItemToMail(long userId, Collection<FullItem> items,
			byte isGift) {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		int i = 0;
		MailMessage mail = null;

		for (FullItem item : items) {
			int index = i % 6;
			if (mail == null) {
				mail = new MailMessage();
				mail.setReceiveUserId(userId);
				mail
						.setComment("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;背包已满，请到附件中提取剩余的物品。若背包空间不足领取附件，请点击单个附件领取。");
				mail.setTitle(AppConstants.FONT_COLOR_WHITE + "背包已满</color>");
				mail.setMessageType(MailMessage.TYPE_SYSTEM);
				mail.setSendDttm(now);
				mail.setIsGift(isGift);
			}
			mail.setItem(index, item.itemId, item.num);
			mail.setStatus0ByPos(index, MailMessage.APPENDIX_HAVE);
			if (index == 5) {
				mailMessageService.createSystemMessage(mail);
				mail = null;
			}
			i++;
		}
		// 最后一个
		if (mail != null) {
			mailMessageService.createSystemMessage(mail);
		}
	}

	public void doRollbackAfter(DefaultTransactionStatus status) {
		allItems.remove();
	}

	static class FullItems {
		public Map<Integer, FullItem> gifts = null;
		public Map<Integer, FullItem> notGifts = null;
		public int itemId;
		public int num;

		public void addItem(int itemId, int num, boolean isGift) {
			// Map<Integer, FullItem> items = isGift ? gifts : notGifts;
			if (isGift) {
				if (gifts == null) {
					gifts = new HashMap<Integer, FullItem>();
					gifts.put(itemId, new FullItem(itemId, num));
				} else {
					FullItem fullItem = gifts.get(itemId);
					if (fullItem == null) {
						gifts.put(itemId, new FullItem(itemId, num));
					} else {
						fullItem.num = fullItem.num + num;
					}
				}
			} else {
				if (notGifts == null) {
					notGifts = new HashMap<Integer, FullItem>();
					notGifts.put(itemId, new FullItem(itemId, num));
				} else {
					FullItem fullItem = notGifts.get(itemId);
					if (fullItem == null) {
						notGifts.put(itemId, new FullItem(itemId, num));
					} else {
						fullItem.num = fullItem.num + num;
					}
				}
			}

		}
	}

	static class FullItem {
		public int itemId;
		public int num;

		public FullItem(int itemId, int num) {
			this.itemId = itemId;
			this.num = num;
		}

	}
}

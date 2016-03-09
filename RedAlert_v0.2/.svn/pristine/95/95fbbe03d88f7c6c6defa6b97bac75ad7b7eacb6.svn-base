package com.youxigu.dynasty2.mail.domain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Timestamp;
import java.util.Map;

import com.youxigu.dynasty2.util.BaseException;

/**
 * 站内邮件实体的定义 数据量大做分表处理
 */
public class MailMessage implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	// 邮件类型
	public static final byte TYPE_NORMAL = 0;// 普通邮件
	public static final byte TYPE_SYSTEM = 10;// 系统邮件
	public static final byte TYPE_SYSTEM_PVP = 11;// pvp战报

	// 邮件状态
	public static final byte STATUS_UNREAD = 0;
	public static final byte STATUS_READ = 1;
	public static final byte STATUS_DELETE = 2;

	public static final byte APPENDIX_NONE = 0;// 没有附件,或者已经提取
	public static final byte APPENDIX_HAVE = 1;// 有附件
	// public static final byte APPENDIX_GET = 3;// 有附件，已经提取

	private int messageId;// 主键
	private long sendUserId;// 发送方id
	private long receiveUserId;// 接收方id
	private Timestamp sendDttm;// 发送日期、时间
	private Timestamp readDttm;// 读取日期、时间
	private String comment;// 消息内容
	private String title;// 消息标题
	private byte status;// 消息状态： 0：未读的消息 1：读了的消息 2 删除的消息
	private byte messageType;// 消息种类： 0：普通消息 11：系统消息 11 pvp战报
	private byte isGift;// 道具是否是礼品
	private int entityId0;// 附件道具id >0，有附件道具，else 没有
	private int itemNum0;// 附件数量
	private int entityId1;// 附件道具id >0，有附件道具，else 没有
	private int itemNum1;// 附件数量
	private int entityId2;// 附件道具id >0，有附件道具，else 没有
	private int itemNum2;// 附件数量
	private int entityId3;// 附件道具id >0，有附件道具，else 没有
	private int itemNum3;// 附件数量
	private int entityId4;// 附件道具id >0，有附件道具，else 没有
	private int itemNum4;// 附件数量
	private int entityId5;// 附件道具id >0，有附件道具，else 没有
	private int itemNum5;// 附件数量
	private byte status0;// 附件状态
	private byte[] map;// 二进制数据 ,战报的描述，messageType=10/11 有效。

	// private transient String sendUserName;// 发送方Name

	// private transient Map mapData; // map二进制数据转换成的map对象
	public MailMessage() {
	}

	public MailMessage(MailMessage clone) {
		this.sendUserId = clone.sendUserId;// 发送方id
		this.receiveUserId = clone.receiveUserId;// 接收方id
		this.sendDttm = clone.sendDttm;// 发送日期、时间
		this.readDttm = clone.readDttm;// 读取日期、时间
		this.comment = clone.comment;// 消息内容
		this.title = clone.title;// 消息标题
		this.status = clone.status;// 消息状态： 0：未读的消息 1：读了的消息 2 删除的消息
		this.messageType = clone.messageType;// 消息种类： 0：普通消息 1：系统消息 10 pvp战报 11
												// pve战报
		this.isGift = clone.isGift;// 道具是否是礼品
		this.entityId0 = clone.entityId0;// 附件道具id >0，有附件道具，else 没有
		this.itemNum0 = clone.itemNum0;// 附件数量
		this.entityId1 = clone.entityId1;// 附件道具id >0，有附件道具，else 没有
		this.itemNum1 = clone.itemNum1;// 附件数量
		this.entityId2 = clone.entityId2;// 附件道具id >0，有附件道具，else 没有
		this.itemNum2 = clone.itemNum2;// 附件数量
		this.entityId3 = clone.entityId3;// 附件道具id >0，有附件道具，else 没有
		this.itemNum3 = clone.itemNum3;// 附件数量
		this.entityId4 = clone.entityId4;// 附件道具id >0，有附件道具，else 没有
		this.itemNum4 = clone.itemNum4;// 附件数量
		this.entityId5 = clone.entityId5;// 附件道具id >0，有附件道具，else 没有
		this.itemNum5 = clone.itemNum5;// 附件数量
		this.status0 = clone.status0;// 附件状态
		this.map = clone.map;// 二进制数据 ,战报的描述，messageType=10/11 有效。
	}

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public long getSendUserId() {
		return sendUserId;
	}

	public void setSendUserId(long sendUserId) {
		this.sendUserId = sendUserId;
	}

	public long getReceiveUserId() {
		return receiveUserId;
	}

	public void setReceiveUserId(long receiveUserId) {
		this.receiveUserId = receiveUserId;
	}

	public Timestamp getSendDttm() {
		return sendDttm;
	}

	public void setSendDttm(Timestamp sendDttm) {
		this.sendDttm = sendDttm;
	}

	public Timestamp getReadDttm() {
		return readDttm;
	}

	public void setReadDttm(Timestamp readDttm) {
		this.readDttm = readDttm;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public byte getMessageType() {
		return messageType;
	}

	public void setMessageType(byte messageType) {
		this.messageType = messageType;
	}

	public int getEntityId0() {
		return entityId0;
	}

	public void setEntityId0(int entityId0) {
		this.entityId0 = entityId0;
	}

	public int getItemNum0() {
		return itemNum0;
	}

	public void setItemNum0(int itemNum0) {
		this.itemNum0 = itemNum0;
	}

	public int getEntityId1() {
		return entityId1;
	}

	public void setEntityId1(int entityId1) {
		this.entityId1 = entityId1;
	}

	public int getItemNum1() {
		return itemNum1;
	}

	public void setItemNum1(int itemNum1) {
		this.itemNum1 = itemNum1;
	}

	public int getEntityId2() {
		return entityId2;
	}

	public void setEntityId2(int entityId2) {
		this.entityId2 = entityId2;
	}

	public int getItemNum2() {
		return itemNum2;
	}

	public void setItemNum2(int itemNum2) {
		this.itemNum2 = itemNum2;
	}

	public int getEntityId3() {
		return entityId3;
	}

	public void setEntityId3(int entityId3) {
		this.entityId3 = entityId3;
	}

	public int getItemNum3() {
		return itemNum3;
	}

	public void setItemNum3(int itemNum3) {
		this.itemNum3 = itemNum3;
	}

	public int getEntityId4() {
		return entityId4;
	}

	public void setEntityId4(int entityId4) {
		this.entityId4 = entityId4;
	}

	public int getItemNum4() {
		return itemNum4;
	}

	public void setItemNum4(int itemNum4) {
		this.itemNum4 = itemNum4;
	}

	public int getEntityId5() {
		return entityId5;
	}

	public void setEntityId5(int entityId5) {
		this.entityId5 = entityId5;
	}

	public int getItemNum5() {
		return itemNum5;
	}

	public void setItemNum5(int itemNum5) {
		this.itemNum5 = itemNum5;
	}

	public byte getStatus0() {
		return status0;
	}

	public void setStatus0(byte status0) {
		this.status0 = status0;
	}

	public byte[] getMap() {
		// if (this.map == null) {
		// if (this.mapData != null) {
		// ByteArrayOutputStream byteArrayOutputStream = new
		// ByteArrayOutputStream();
		// try {
		// ObjectOutputStream objectOutputStream;
		// objectOutputStream = new ObjectOutputStream(
		// byteArrayOutputStream);
		// objectOutputStream.writeObject(mapData);
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// this.map = byteArrayOutputStream.toByteArray();
		// }
		// }
		return this.map;

	}

	// public Map getMapData(){
	// if (this.map!=null){
	// return fromBytes(this.map);
	// }else{
	// return null;
	// }
	// }
	public void setMap(byte[] map) {
		this.map = map;
	}

	@SuppressWarnings("rawtypes")
	public static byte[] toBytes(Map obj) {
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		ObjectOutputStream os = null;
		try {

			os = new ObjectOutputStream(bs);
			os.writeObject(obj);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				os.close();
			} catch (Exception e) {
			}
		}
		return bs.toByteArray();
	}

	@SuppressWarnings("rawtypes")
	public static Map fromBytes(byte[] bytes) {
		Map retu = null;
		ObjectInputStream ois = null;
		try {

			ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(bis);
			retu = (Map) ois.readObject();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ois.close();
			} catch (Exception e) {
			}
		}
		return retu;
	}

	// public void setMapData(Map mapData) {
	// this.mapData = mapData;
	// }
	//
	// public Map getMapData() {
	// if (mapData == null && map != null) {
	// try {
	//
	// ByteArrayInputStream bis = new ByteArrayInputStream(map);
	// ObjectInputStream ois = new ObjectInputStream(bis);
	// mapData = (Map) ois.readObject();
	// ois.close();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// return mapData;
	// }

	/**
	 * 取附件读取状态
	 */
	public int getStatus0ByPos(int pos) {

		if (pos == 0) {
			return this.status0 & 0x1;// 1
		} else if (pos == 1) {
			return (this.status0 & 0x2) >>> 1;// 10
		} else if (pos == 2) {
			return (this.status0 & 0x4) >>> 2;// 100
		} else if (pos == 3) {
			return (this.status0 & 0x8) >>> 3;// 1000
		} else if (pos == 4) {
			return (this.status0 & 0x10) >>> 4;// 10000
		} else if (pos == 5) {
			return (this.status0 & 0x20) >>> 5;// 100000;
		} else {
			throw new BaseException("附件为止异常");
		}
	}

	public int getItemNumByPos(int pos) {
		if (pos == 0) {
			return this.itemNum0;
		} else if (pos == 1) {
			return this.itemNum1;
		} else if (pos == 2) {
			return this.itemNum2;
		} else if (pos == 3) {
			return this.itemNum3;
		} else if (pos == 4) {
			return this.itemNum4;
		} else if (pos == 5) {
			return this.itemNum5;
		} else {
			throw new BaseException("附件为止异常");
		}
	}

	/**
	 * 取附件实体id
	 * 
	 * @param pos
	 * @return
	 */
	public int getEntityIdByPos(int pos) {
		if (pos == 0) {
			return this.entityId0 < 0 ? -1 * this.entityId0 : this.entityId0;
		} else if (pos == 1) {
			return this.entityId1 < 0 ? -1 * this.entityId1 : this.entityId1;
		} else if (pos == 2) {
			return this.entityId2 < 0 ? -1 * this.entityId2 : this.entityId2;
		} else if (pos == 3) {
			return this.entityId3 < 0 ? -1 * this.entityId3 : this.entityId3;
		} else if (pos == 4) {
			return this.entityId4 < 0 ? -1 * this.entityId4 : this.entityId4;
		} else if (pos == 5) {
			return this.entityId5 < 0 ? -1 * this.entityId5 : this.entityId5;
		} else {
			throw new BaseException("附件位置异常");
		}
	}

	public void setEntityIdByPos(int pos, int entId, int num) {
		if (pos == 0) {
			this.setEntityId0(entId);
			this.setItemNum0(num);
			this.setStatus0ByPos(0, MailMessage.APPENDIX_HAVE);
		} else if (pos == 1) {
			this.setEntityId1(entId);
			this.setItemNum1(num);
			this.setStatus0ByPos(1, MailMessage.APPENDIX_HAVE);

		} else if (pos == 2) {
			this.setEntityId2(entId);
			this.setItemNum2(num);
			this.setStatus0ByPos(2, MailMessage.APPENDIX_HAVE);
		} else if (pos == 3) {
			this.setEntityId3(entId);
			this.setItemNum3(num);
			this.setStatus0ByPos(3, MailMessage.APPENDIX_HAVE);
		} else if (pos == 4) {
			this.setEntityId4(entId);
			this.setItemNum4(num);
			this.setStatus0ByPos(4, MailMessage.APPENDIX_HAVE);
		} else if (pos == 5) {
			this.setEntityId5(entId);
			this.setItemNum5(num);
			this.setStatus0ByPos(5, MailMessage.APPENDIX_HAVE);
		} else {
			throw new BaseException("附件为止异常");
		}
	}

	/**
	 * 设置附件状态
	 * 
	 * @param pos
	 * @param status
	 */
	public void setStatus0ByPos(int pos, int status) {
		// TODO:wang,这块写的太土鳖了
		int value = 0x1 << pos;
		value = ~value;
		value = value & this.status0;
		this.status0 = (byte) (value | (status << pos));

	}

	public byte getIsGift() {
		return isGift;
	}

	public void setIsGift(byte isGift) {
		this.isGift = isGift;
	}

	public void setItem(int index, int itemId, int num) {
		switch (index) {
		case 0:
			this.entityId0 = itemId;
			this.itemNum0 = num;
			break;
		case 1:
			this.entityId1 = itemId;
			this.itemNum1 = num;
			break;
		case 2:
			this.entityId2 = itemId;
			this.itemNum2 = num;
			break;
		case 3:
			this.entityId3 = itemId;
			this.itemNum3 = num;
			break;
		case 4:
			this.entityId4 = itemId;
			this.itemNum4 = num;
			break;
		case 5:
			this.entityId5 = itemId;
			this.itemNum5 = num;
			break;
		default:
			throw new BaseException("错误的附件索引");
		}

	}
}

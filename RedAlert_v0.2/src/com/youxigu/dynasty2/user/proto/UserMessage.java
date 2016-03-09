package com.youxigu.dynasty2.user.proto;

import java.io.Serializable;

import com.google.protobuf.Message;
import com.youxigu.dynasty2.chat.EventMessage;
import com.youxigu.dynasty2.chat.ISendMessage;
import com.youxigu.dynasty2.chat.proto.CommonHead.ResponseHead;
import com.youxigu.dynasty2.user.domain.User;

/**
 * 推送君主信息变化
 * @author Dagangzi
 *
 */
public class UserMessage implements ISendMessage, Serializable {
	private String icon;
	private int usrLv;// 等级
	private int cash;// 元宝
	private int honor;// 经验值
	private int countryId;// 国家编号
	private long guildId;// 联盟ID, 如果没加入联盟则为0, 或-1
	private String guildName;// 联盟

	public UserMessage() {
	}

	public UserMessage(User user) {
		this.icon = user.getIcon();
		this.usrLv = user.getUsrLv();
		this.cash = user.getCash();
		this.honor = user.getHonor();
		this.countryId = user.getCountryId();
		this.guildId = user.getGuildId();
		//TODO 做联盟的时候加上
		this.guildName = "";
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getUsrLv() {
		return usrLv;
	}

	public void setUsrLv(int usrLv) {
		this.usrLv = usrLv;
	}

	public int getCash() {
		return cash;
	}

	public void setCash(int cash) {
		this.cash = cash;
	}

	public int getHonor() {
		return honor;
	}

	public void setHonor(int honor) {
		this.honor = honor;
	}

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	public long getGuildId() {
		return guildId;
	}

	public void setGuildId(long guildId) {
		this.guildId = guildId;
	}

	public String getGuildName() {
		return guildName;
	}

	public void setGuildName(String guildName) {
		this.guildName = guildName;
	}

	private UserMsg.UserEvent convertProto(){
		UserMsg.UserEvent.Builder uEvent = UserMsg.UserEvent.newBuilder();
		uEvent.setIcon(this.icon);
		uEvent.setUsrLv(this.usrLv);
		uEvent.setCash(this.cash);
		uEvent.setHonor(this.honor);
		uEvent.setCountryId(this.countryId);
		uEvent.setGuildId(this.guildId);
		uEvent.setGuildName(this.guildName);
		return uEvent.build();
	}

	@Override
	public Message build() {
		//必须包含responseHead
		UserMsg.UserSendEvent.Builder sEvent = UserMsg.UserSendEvent.newBuilder();
		ResponseHead.Builder headBr = ResponseHead.newBuilder();
		headBr.setCmd(EventMessage.TYPE_USER_CHANGED);
		headBr.setRequestCmd(EventMessage.TYPE_USER_CHANGED);
		sEvent.setResponseHead(headBr.build());
		
		sEvent.setUserEvent(convertProto());
		return sEvent.build();
	}
}

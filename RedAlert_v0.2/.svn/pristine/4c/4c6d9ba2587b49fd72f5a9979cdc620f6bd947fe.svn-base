package com.youxigu.dynasty2.friend.proto;

import java.io.Serializable;

import com.google.protobuf.Message;
import com.youxigu.dynasty2.chat.EventMessage;
import com.youxigu.dynasty2.chat.ISendMessage;
import com.youxigu.dynasty2.chat.proto.CommonHead.ResponseHead;
import com.youxigu.dynasty2.friend.proto.FriendMsg.DelFriendSendEvent;

public class SendDelFriend implements Serializable, ISendMessage {
	private static final long serialVersionUID = -5716015539927544366L;
	private long friendUserId;

	public SendDelFriend(long friendUserId) {
		super();
		this.friendUserId = friendUserId;
	}

	@Override
	public Message build() {
		DelFriendSendEvent.Builder msg = DelFriendSendEvent.newBuilder();
		ResponseHead.Builder hd = ResponseHead.newBuilder();
		hd.setCmd(EventMessage.TYPE_DEL_FRIEND);
		hd.setRequestCmd(EventMessage.TYPE_DEL_FRIEND);
		msg.setResponseHead(hd.build());
		msg.setFriendUserId(friendUserId);
		return msg.build();
	}

}

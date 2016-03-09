package com.youxigu.dynasty2.friend.proto;

import java.io.Serializable;

import com.google.protobuf.Message;
import com.youxigu.dynasty2.chat.EventMessage;
import com.youxigu.dynasty2.chat.ISendMessage;
import com.youxigu.dynasty2.chat.proto.CommonHead.ResponseHead;
import com.youxigu.dynasty2.friend.proto.FriendMsg.AddFriendSendEvent;
import com.youxigu.dynasty2.friend.proto.FriendMsg.AppFriendSendEvent;
import com.youxigu.dynasty2.friend.proto.FriendMsg.FriendInfo;

public class SendFriendMsg implements Serializable, ISendMessage {
	private static final long serialVersionUID = 2949379940836471209L;
	private FriendInfoMsg msg = null;
	private int cmd = 0;

	public SendFriendMsg(int cmd, FriendInfoMsg msg) {
		super();
		this.cmd = cmd;
		this.msg = msg;
	}

	@Override
	public Message build() {
		ResponseHead.Builder hd = ResponseHead.newBuilder();
		hd.setCmd(cmd);
		hd.setRequestCmd(cmd);
		if (EventMessage.TYPE_FRIEND_APP == cmd) {
			AppFriendSendEvent.Builder res = AppFriendSendEvent.newBuilder();
			res.setResponseHead(hd.build());
			res.setFriend((FriendInfo) msg.build());
			return res.build();
		} else if (EventMessage.TYPE_ADD_FRIEND == cmd) {
			AddFriendSendEvent.Builder res = AddFriendSendEvent.newBuilder();
			res.setResponseHead(hd.build());
			res.setFriend((FriendInfo) msg.build());
			return res.build();
		}
		return null;
	}
}

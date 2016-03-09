package com.youxigu.dynasty2.chat;

import com.google.protobuf.Message;

public class BroadMessage implements java.io.Serializable {
    private static final long serialVersionUID = -2767727434488779546L;

	private int eventType;
	private ISendMessage params;

	public int getEventType() {
		return eventType;
	}

	public void setEventType(int eventType) {
		this.eventType = eventType;
	}

	public ISendMessage getParams() {
		return params;
	}

	public void setParams(ISendMessage params) {
		this.params = params;
	}

	public String toString() {
		return new StringBuilder(64).append("Event,type=").append(eventType).append(",params=").append(params)
				.toString();
	}

	public Message build() {
//		Message response = null;
//
//		ResponseHead.Builder headBr = ResponseHead.newBuilder();
//		headBr.setCmd(this.eventType);
//		headBr.setErrCode(0);
//		headBr.setErr("");
//		headBr.setRequestCmd(this.eventType);
//
//		try {
////			Method m = this.params.getClass().getDeclaredMethod("build", new Class[] {});
////			response = (Message) m.invoke(this.params);
////			Field f = response.getClass().getDeclaredField("responseHead_");
////			boolean accessFlag = f.isAccessible();
////			if (!accessFlag) {
////				f.setAccessible(true);
////			}
////			f.set(response, headBr.build());
////			f.setAccessible(accessFlag);
//			response = params.build();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return params.build();
	}
}

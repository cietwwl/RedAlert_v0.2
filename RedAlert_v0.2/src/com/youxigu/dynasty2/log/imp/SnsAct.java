package com.youxigu.dynasty2.log.imp;

public enum SnsAct {
	
	
	SNSTYPE_SHOWOFF(0,"炫耀"),
	SNSTYPE_INVITE(1,"邀请"),
	SNSTYPE_SENDHEART(2,"体力"),
	SNSTYPE_RECEIVEHEART(3,"收体力"),
	SNSTYPE_SENDEMAIL(4,"发邮件"),
	SNSTYPE_RECEIVEEMAIL(5,"收邮件"),
	SNSTYPE_SHARE(6,"分享");
	public final  int value;
	public final  String desc;
	
	private SnsAct(int value,String desc){
		this.value = value;
		this.desc = desc;
	}
	
	public String toString(){
		return desc;
	}

}

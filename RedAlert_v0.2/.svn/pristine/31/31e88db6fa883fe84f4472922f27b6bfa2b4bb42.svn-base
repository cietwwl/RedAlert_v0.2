package com.youxigu.dynasty2.mail.domain;

import com.youxigu.wolf.net.AsyncWolfTask;

public class AsyncCmdWolfTask extends AsyncWolfTask {
    private int cmd;
    private long userId;

    public AsyncCmdWolfTask(){
    }

    public AsyncCmdWolfTask(int cmd, long userId, String serviceName, String methodName, Object[] params){
        super(serviceName, methodName, params);
        this.cmd = cmd;
        this.userId = userId;
    }

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[cmd:").append(cmd).append("][userId:").append(userId).append("]")
                .append(getServiceName()).append(".").append(getMethodName());
        for(Object param : getParams()){
            sb.append(param).append(";");
        }
		return sb.toString();
	}

}

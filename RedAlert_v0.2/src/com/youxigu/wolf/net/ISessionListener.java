package com.youxigu.wolf.net;

public interface ISessionListener {
	void open(Response response);
	void close(Response response);
}

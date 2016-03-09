package com.youxigu.dynasty2.core.event;

public class Event {

	public int eventType;
	public Object params;

	public Event(int eventType, Object params) {
		super();
		this.eventType = eventType;
		this.params = params;
	}

	public Event() {
		super();
	}

	public int getEventType() {
		return eventType;
	}

	public void setEventType(int eventType) {
		this.eventType = eventType;
	}

	public Object getParams() {
		return params;
	}

	public void setParams(Object params) {
		this.params = params;
	}

}

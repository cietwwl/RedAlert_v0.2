package com.youxigu.dynasty2.armyout.domain.qingbao;

import java.io.Serializable;
import java.util.Date;

/**
 * 所有的军情都要缓冲进数据库，这是重数据库中取出来之后的对象
 * */
public class MilitarySituation implements Serializable{
	private static final long serialVersionUID = -3266505751828724133L;
	private int id;
	private int miSiType; // 情报类型：被侦查1，被集结2，被进攻3，侦查4
	private String name; // 情报名称
	private String content; // 情报内容，简述
	private byte[] misiDetail; // 详细情报存储的是pb中定义的相信情报对象，这样取出返回即可
	private long userId; // 
	private byte hasView; // 是否已经查看
	
	private Date time; // 情报发生的时间，从misiDetail取出
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public int getMiSiType() {
		return miSiType;
	}
	public void setMiSiType(int miSiType) {
		this.miSiType = miSiType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public byte[] getMisiDetail() {
		return misiDetail;
	}
	public void setMisiDetail(byte[] misiDetail) {
		this.misiDetail = misiDetail;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public boolean hasView() {
		return hasView == (byte)1;
	}
	public void setHasView(byte hasView) {
		this.hasView = hasView;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	
	@Override
	public String toString(){
		return new StringBuilder("id:"+id).append(",miSiType:"+miSiType)
				.append(",name:"+name)
				.append(",content:"+content)
				.append(",misiDetail:"+misiDetail)
				.append(",userId:"+userId)
				.append(",hasView:"+hasView)
				.append(",time:"+time)
				.toString();
	}
}

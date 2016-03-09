package com.youxigu.dynasty2.gmtool.domain;

import java.sql.Timestamp;

/**
 * GM发放内部福利
 * 
 * @author Administrator
 *
 */
public class UserCashBonus implements java.io.Serializable {
	private static final long serialVersionUID = -6761537558652091622L;
	private String openId;
	private int num;// 每次发放的元宝数

	private Timestamp dttm;// 最后一次发放的时间
	private int total;// 总计发放的元宝数

	private int status;// =0 停止发放状态,=1可发放状态

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public Timestamp getDttm() {
		return dttm;
	}

	public void setDttm(Timestamp dttm) {
		this.dttm = dttm;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}

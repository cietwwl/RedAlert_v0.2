package com.youxigu.dynasty2.common.service;

import java.sql.Timestamp;
import java.util.List;

import com.youxigu.dynasty2.common.domain.Enumer;
import com.youxigu.dynasty2.common.domain.ServerInfo;
import com.youxigu.dynasty2.common.domain.SysPara;

public interface ICommonService {

	/**
	 * 取得系统参数的值
	 * 
	 * @param paraId
	 * @return
	 */
	String getSysParaValue(String paraId);
	String getSysParaValue(String paraId, String defaultValue);
	int getSysParaIntValue(String paraId, int defaultValue);
	Timestamp getSysParaTimestampValue(String paraId);

	/**
	 * 更新系统参数
	 * 
	 * @param paraId
	 * @param value
	 */
	void updateSysPara(String paraId, String value);
	void resetSysPara(String paraId, String value);

	/**
	 * 取得所有运营端配置的参数
	 * 
	 * @return
	 */
	List<SysPara> getSysParaGMs();

	/**
	 * 
	 * @param enumId
	 * @return
	 */
	Enumer getEnumById(String enumId);

	/**
	 * 
	 * @param enumId
	 * @return
	 */
	int getEnumIntValueById(String enumId, int defaultValue);

	/**
	 * 
	 * @param enumGroup
	 * @return
	 */
	List<Enumer> getEnumsByGroup(String enumGroup);

	/**
	 * 得到权重/值配表中某个id随机值
	 * 
	 * @param id
	 * @return
	 */
	int getRandomWeightValueById(int id);

	// /**
	// *
	// * 得到权重/值配表中某个id指定权重的值
	// * @param id
	// * @param percent
	// * @return
	// */
	// int getWeightValueById(int id,int percent);

	ServerInfo getServerInfo();

	/**
	 * 设置服务器开服时间
	 * 
	 * @param time
	 */
	public void setServerInfoTime(long time);

	public void reloadServerInfoTime(long time);

	/**
	 * 从运营管理端同步Server的名字信息
	 * @return
	 */
	@Deprecated
	void syncServerInfo();

	/**
	 * 
	 *重新加载数据库中的serverinfo
	 */
	@Deprecated
	void reloadServerInfo();
}

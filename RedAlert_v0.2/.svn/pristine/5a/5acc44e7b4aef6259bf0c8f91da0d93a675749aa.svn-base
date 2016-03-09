package com.youxigu.dynasty2.common.dao;

import java.util.List;
import java.util.Map;

import com.youxigu.dynasty2.common.domain.Enumer;
import com.youxigu.dynasty2.common.domain.ServerInfo;
import com.youxigu.dynasty2.common.domain.SysPara;

/**
 * 系统参数DAO
 * 
 * @author Administrator
 * 
 */
public interface ISysParaDao {

	/**
	 * 获取所有的系统参数，系统初始化调用
	 */
	List<SysPara> getAllSysPara();

	/**
	 * 
	 * @param paraId
	 * @return
	 */
	SysPara getSysParaById(String paraId);

	/**
	 * 更新系统参数
	 * 
	 * @param para
	 */
	void updateSysPara(SysPara para);
	
	/**
	 * 获取所有的系统参数，系统初始化调用
	 */
	List<SysPara> getAllSysPara_gm();

	/**
	 * 
	 * @param paraId
	 * @return
	 */
	SysPara getSysParaById_gm(String paraId);

	/**
	 * 更新系统参数
	 * 
	 * @param para
	 */
	void updateSysPara_gm(SysPara para);
	void insertSysPara_gm(SysPara para);	
	void deleteSysPara_gm(String paraId);	
	
	List<Enumer> listAllEnumer();

	/**
	 *配置权重/值的对应关系 
	 */
	List<Map<String, Object>> getWeightValueConf();
	
	
	void insertServerInfo(ServerInfo serverInfo);
	void updateServerInfo(ServerInfo serverInfo);
	ServerInfo getServerInfo(String serverInfo);	
	List<ServerInfo> getServerInfos();	
	
//	List<FuncUnLock> getFuncUnLocks();
}

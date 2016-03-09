package com.youxigu.dynasty2.common.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.manu.core.base.BaseDao;
import com.youxigu.dynasty2.common.dao.ISysParaDao;
import com.youxigu.dynasty2.common.domain.Enumer;
import com.youxigu.dynasty2.common.domain.ServerInfo;
import com.youxigu.dynasty2.common.domain.SysPara;

@SuppressWarnings("unchecked")
public class SysParaDao extends BaseDao implements ISysParaDao {

	@Override
	public List<SysPara> getAllSysPara() {
		return this.getSqlMapClientTemplate().queryForList("listSysPara");
	}

	@Override
	public SysPara getSysParaById(String paraId) {
		return (SysPara) this.getSqlMapClientTemplate().queryForObject(
				"getSysParaById", paraId);
	}

	@Override
	public void updateSysPara(SysPara para) {
		this.getSqlMapClientTemplate().update("updateSysParaById", para);

	}

	@Override
	public List<SysPara> getAllSysPara_gm() {
		return this.getSqlMapClientTemplate().queryForList("listSysPara_gm");
	}

	@Override
	public SysPara getSysParaById_gm(String paraId) {
		return (SysPara) this.getSqlMapClientTemplate().queryForObject(
				"getSysParaById_gm", paraId);
	}

	@Override
	public void updateSysPara_gm(SysPara para) {
		this.getSqlMapClientTemplate().update("updateSysParaById_gm", para);

	}

	@Override
	public void insertSysPara_gm(SysPara para) {
		this.getSqlMapClientTemplate().insert("insertSyspara_gm", para);
	}

	@Override
	public void deleteSysPara_gm(String paraId) {
		this.getSqlMapClientTemplate().update("deleteSameSyspara_gm", paraId);
	}

	@Override
	public List<Enumer> listAllEnumer() throws DataAccessException {
		return this.getSqlMapClientTemplate().queryForList("listEnumer");
	}

	@Override
	public List<Map<String, Object>> getWeightValueConf() {
		return this.getSqlMapClientTemplate()
				.queryForList("getWeightValueConf");
	}

	/**
	 * 积分奖励配数
	 * 
	 * @return
	 */
	// @Override
	// public List<PointAward> getAwards() {
	// return this.getSqlMapClientTemplate().queryForList("getPointAwards");
	// }

//	@Override
//	public List<OnlineUser> getOnlineUser(Timestamp start, Timestamp end) {
//		Map<String, Timestamp> map = new HashMap<String, Timestamp>(2);
//		map.put("start", start);
//		map.put("end", end);
//		return this.getSqlMapClientTemplate()
//				.queryForList("getOnlineUser", map);
//	}

	@Override
	public void insertServerInfo(ServerInfo serverInfo) {

		this.getSqlMapClientTemplate().insert("insertServerInfo", serverInfo);
	}

	@Override
	public ServerInfo getServerInfo(String serverId) {
		return (ServerInfo) this.getSqlMapClientTemplate().queryForObject(
				"getServerInfo", serverId);

	}

	@Override
	public List<ServerInfo> getServerInfos() {
		return this.getSqlMapClientTemplate().queryForList("getServerInfos");
	}

	// @Override
	// public List<FuncUnLock> getFuncUnLocks() {
	// return this.getSqlMapClientTemplate().queryForList("getFuncUnLocks");
	// }

	@Override
	public void updateServerInfo(ServerInfo serverInfo) {
		super.getSqlMapClientTemplate().update("updateServerInfo", serverInfo);

	}
}

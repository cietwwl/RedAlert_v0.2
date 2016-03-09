package com.youxigu.dynasty2.asyncdb.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.youxigu.dynasty2.asyncdb.service.IIDGenerator;

public class DBIDGenerator implements IIDGenerator {

	public static Logger log = LoggerFactory.getLogger(DBIDGenerator.class);
	
	public static final String TYPE_DEFAULT="DEF";
	
	private DataSource dataSource;
	/**
	 * 按类型缓存的ID的当前值
	 */
	private Map<String, DBID> idCaches = new HashMap<String, DBID>();

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void init() {
		if (dataSource == null) {
			log.error("ID生成器没有配置数据源");
		}
		IDUtil.setIdGenerator(this);
	}
	
	private void loadID(DBID id) {
		Connection _conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select * from idgen where idType=? for update";
		String updateSql = "update idgen set currValue = ? where  idType=?";
		try {
			_conn = dataSource.getConnection();
			_conn.setAutoCommit(false);
			pstmt = _conn.prepareStatement(sql);
			pstmt.setString(1, id.type);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				id.cacheCout = rs.getInt("cacheCount");
				id.current = rs.getLong("currValue");
				if (id.cacheCout<1){
					throw new RuntimeException("ID生成器表的cacheCout必须大于1");
				}
				id.max = id.current + id.cacheCout -1;
			} else {
				throw new RuntimeException("ID生成器表没有配置ID类型：" + id.type);
			}

			rs.close();
			pstmt.close();
			
			// 更新
			pstmt = _conn.prepareStatement(updateSql);
			pstmt.setLong(1, id.max+1);
			pstmt.setString(2, id.type);
			pstmt.executeUpdate();
			_conn.commit();
			
		} catch (Exception e) {
			try {
				_conn.rollback();
			} catch (Exception e1) {
			}
			throw new RuntimeException(e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e) {
				}
			}
			if (_conn != null) {
				try {
					_conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public long getID(String type) {
		DBID id = idCaches.get(type);
		if (id==null){
			synchronized (idCaches){
				id = idCaches.get(type);
				if (id==null){
					id = new DBID();
					id.type = type;
					loadID(id);
					idCaches.put(type, id);
				}
			}
		}
		
		
		return id.get();
	}

	@Override
	public long getID() {
		return getID(TYPE_DEFAULT);
	}

	private class DBID {

		public String type;
		// /当前值
		public long current;

		// 缓存的ID个数
		public int cacheCout;

		// 缓存ID最大值, current==max时，要更新数据库，并取出新的最大值
		public long max;
		
		public synchronized long get(){
			long  cur = this.current;
			if (this.current==this.max){
				loadID(this);
			}
			else{
				this.current++;
			}
			return cur;
		}

	}
}

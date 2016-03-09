package com.youxigu.wolf.node.job.dao;

import java.util.List;

import com.manu.core.base.BaseDao;
import com.youxigu.wolf.node.job.Job;

public class JobDao extends BaseDao implements IJobDao {

	public void deleteJob(int id) {
		this.getSqlMapClientTemplate().delete("deleteJob", id);
	}

	public List<Job> getJobList() {
		return this.getSqlMapClientTemplate().queryForList("getJobList");
	}

	public void insertJob(Job job) {
		this.getSqlMapClientTemplate().insert("insertJob", job);
	}

	public void updateJob(Job job) {
		this.getSqlMapClientTemplate().update("updateJob", job);
	}

	@Override
	public void insertErrorJob(Job job) {
		this.getSqlMapClientTemplate().insert("insertErrJob", job);
		
	}

}

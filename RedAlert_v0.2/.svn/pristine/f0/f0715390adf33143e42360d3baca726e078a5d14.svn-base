package com.youxigu.dynasty2.armyout.dao;

import java.util.List;

import com.youxigu.dynasty2.armyout.domain.qingbao.MilitarySituation;


public interface IMilitarySituationDao {
	public void insertMilitarySituation(MilitarySituation misi);
	
	/**
	 * 从数据库中获取具体情报信息
	 * */
	public MilitarySituation getMilitarySituationById(int id);
	/**
	 * 从数据库获取对应userId的对应情报信息
	 * */
	public List<MilitarySituation> getMilitarySituationListByUserId(long userId);
	/**
	 * 删除一个
	 * */
	public void deleteMilitarySituation(int id);
	/**
	 * 删除多个情报，id>
	 * */
	public void deleteMilitarySituations(int id , long userId);
	/**
	 * 设置已查看
	 * */
	public void setHasView(int id , boolean hasView);
	
}

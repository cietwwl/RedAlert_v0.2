package com.youxigu.dynasty2.entity.service;

import java.util.List;

import com.youxigu.dynasty2.common.AppConstants;

public class EntityConsumeResultSet {
	/**
	 * =true 资源足够 =false,资源不够
	 */
	private boolean match = true;

	/**
	 * 比较的结果
	 */
	private List<EntityConsumeResult> results;

	public boolean isMatch() {
		return match;
	}

	public void setMatch(boolean match) {
		this.match = match;
	}

	public List<EntityConsumeResult> getResults() {
		return results;
	}

	public void setResults(List<EntityConsumeResult> results) {
		this.results = results;
	}

	public int getTime() {
		if (results != null) {
			for (EntityConsumeResult result : results) {
				if (result.getNeedEntId() == AppConstants.ENT_PARTY_TIME) {
					return result.getActualNum();
				}
			}
		}
		return 0;
	}
	
//	public int getBuildPoint() {
//		if (results != null) {
//			for (EntityConsumeResult result : results) {
//				if (result.getNeedEntId() == AppConstants.ENT_BUILD_POINT) {
//					return result.getActualNum();
//				}
//			}
//		}
//		return 0;
//	}
}

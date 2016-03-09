package com.youxigu.dynasty2.entity.service;

import java.util.List;


public class EntityLimitResultSet {
	/**
	 * 满足限制条件
	 */
	private boolean match = true;

	private List<EntityLimitResult> results;

	public boolean isMatch() {
		return match;
	}

	public void setMatch(boolean match) {
		this.match = match;
	}

	public List<EntityLimitResult> getResults() {
		return results;
	}

	public void setResults(List<EntityLimitResult> results) {
		this.results = results;
	}

	public String getExceptionString() {
		StringBuffer sb = new StringBuffer();
		for (EntityLimitResult limit : results) {
			if (!limit.isMatch()) {
				sb.append("需要");
				sb.append(limit.getNeedEntName());
//				if (limit.getNeedEntId() != AppConstants.ENT_PARTY_CAS) {
//					sb.append(limit.getNeedEntName());
//				}
				if (limit.getNeedLevel() > 0) {
					sb.append(limit.getNeedLevel());
					sb.append("级，当前");
					sb.append(limit.getActualLevel());
					sb.append("级");
				}
				if (limit.getLeastNum() > 0) {
					sb.append("数量");
					sb.append(limit.getLeastNum());
					sb.append("，当前数量");
					sb.append(limit.getActualNum());
				}
			}
		}
		if (sb.length() > 0) {
			return sb.toString();
		} else
			return null;
	}

}

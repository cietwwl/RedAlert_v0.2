package com.youxigu.dynasty2.asyncdb.service.impl;

import com.youxigu.dynasty2.asyncdb.service.IIDGenerator;

public class IDUtil {

	private static IIDGenerator idGenerator;

	public static void setIdGenerator(IIDGenerator idGenerator) {
		IDUtil.idGenerator = idGenerator;
	}
	
	public static long getNextId(String type){
		return idGenerator.getID(type);
	}
}

package com.youxigu.dynasty2.log;

import java.util.Map;

public interface ITlogDemoDao {
	
	void insertTlog(int areaId,String sqlId,Map<String,Object> params);
	
}

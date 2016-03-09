package com.youxigu.dynasty2.openapi.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.youxigu.dynasty2.openapi.domain.Head;
import com.youxigu.dynasty2.openapi.service.IIdipProcessor;
import com.youxigu.dynasty2.openapi.service.IIdipService;
import com.youxigu.dynasty2.openapi.service.processor.NoCmdProcessor;


/**
 * 腾讯平台idip接口服务
 * 
 * @creator zhaiyong
 * @creatortime 2012-9-13 下午02:00:58
 * 
 */

public class IdipService implements IIdipService, ApplicationContextAware {

	public static final Logger log = LoggerFactory.getLogger(IdipService.class);


	private Map<Integer, IIdipProcessor> idipProcessors = new HashMap<Integer, IIdipProcessor>();


	/**
	 * 不存在的命令统一走这个
	 */
	private IIdipProcessor ERRPRO = new NoCmdProcessor();


	@Override
	public void setApplicationContext(ApplicationContext ctx)
			throws BeansException {
		while (ctx != null) {
			Map<String, IIdipProcessor> maps1 = ctx
					.getBeansOfType(IIdipProcessor.class);
			if (maps1.values() != null && maps1.values().size() > 0) {
				for (IIdipProcessor factory : maps1.values()) {
					idipProcessors.put(factory.getCmd(), factory);
				}
			}
			ctx = ctx.getParent();

		}

	}

	@Override
	public String doProcess(JsonNode head, JsonNode body) {
		int cmd = head.path("Cmdid").getIntValue();
		IIdipProcessor processor = idipProcessors.get(cmd);
		if(processor==null){
			processor = ERRPRO;
		}
		return processor.doIdipProcess(Head.buildHead(head),body);
	}

}

package com.youxigu.dynasty2.openapi.service;

import org.codehaus.jackson.JsonNode;

/**
 * 腾讯开放平台idip接口调用
 * 
 * @creator zhaiyong
 * @creatortime 2012-9-13 下午01:59:37
 * 
 */

public interface IIdipService {


	public String doProcess(JsonNode head,JsonNode body);
}

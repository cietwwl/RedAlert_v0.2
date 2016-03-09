/**
 * 
 */
package com.youxigu.dynasty2.openapi.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonNode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.manu.core.ServiceLocator;
import com.youxigu.dynasty2.core.JSONUtil;
import com.youxigu.dynasty2.openapi.service.IIdipService;
import com.youxigu.dynasty2.util.BaseException;

/**
 * 
 * 提供给腾讯开放平台idip接口调用
 * 
 * @creator zhaiyong
 * @creatortime 2012-9-13 下午04:40:21
 * 
 */

@Controller
@RequestMapping("/idip/")
public class IdipController {
	// private IIdipService idipService;
	IIdipService idipService = (IIdipService) ServiceLocator
			.getSpringBean("idipService");

	/**
	 * 统一处理idip的请求
	 * 
	 * @param request
	 * @param response
	 * @param context
	 * @return
	 */
	@RequestMapping(value = "portal.htm", method = RequestMethod.POST)
	public void portal(HttpServletRequest request, HttpServletResponse response) {
		/*
		 * request请求数据 data_packet={ "head" : { "PacketLen" : null , "Cmdid" :
		 * 4097 , "Seqid" : 10 , "ServiceName" : "OP_QXQZ" , "SendTime" :
		 * 20120917 , "Version" : 1.0 , "Authenticate" : "" , "Result" : 0 ,
		 * "RetErrMsg" : "" }, "body" : { "OpenId" : "123", "AreaId" : 666 } }
		 */
		 String type = request.getContentType();
		 response.setContentType(type);
		String jresult = null;
		JsonNode jdata = null;
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					request.getInputStream(),"utf-8"));
			String line = null;
			StringBuilder sb = new StringBuilder();
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
			in.close();
			
			String data = sb.substring(sb.indexOf("{"), sb.toString().length());
			data = java.net.URLDecoder.decode(data,"UTF-8");


			// 获取请求信息转换成JSON
			jdata = JSONUtil.getJsonNode(data);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			throw new BaseException("获取请求数据异常");
		}
		if (jdata == null || jdata.isNull()) {
			throw new BaseException("转换的json信息为空");
		}
		// 获取head和body信息
		JsonNode head = jdata.path("head");
		JsonNode body = jdata.path("body");
		// 获取请求类型,由腾讯idip接口提供
		jresult = idipService.doProcess(head, body);
		// 返回给idip接口数据
		PrintWriter pr = null;
		try {
			response.setCharacterEncoding("utf-8");
			pr = response.getWriter();
//			String result = "response="+jresult;
//			System.out.println(jresult);
			pr.print(jresult);
			// pr.print(jresult);//"{\"head\":{\"PacketLen\":\"\",\"Cmdid\":\"4098\",\"Seqid\":\"1\",\"ServiceName\":\"\",\"SendTime\":
			// \"\",\"Version\":\"1001\",\"Authenticate\":\"\",\"Result\":0,\"RetErrMsg\":\"\"},\"body\":{\"OpenId\":\"0\",\"NickName\":\"100001\",\"Level\":\"\",\"VipLevel\":\"\",\"Ingot\":\"\",\"Prestige\":\"\",\"Honor\":\"\",\"RegisterTime\":\"\",\"LastLoginTime\":\"\",\"OnlineTime\":\"136991111\",\"RoleID\":\"\"}}");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new BaseException("response返回数据错误");
		} finally {
			pr.close();
		}
	}

	/**
	 * 获取角色信息 data_packet={"head":
	 * {"uiPacketLen":,"uiCmdid":"1001","uiSeqid":1,"szServiceName"
	 * :"IDIP","uiSendTime":
	 * 20110820,"uiVersion":1001,"ucAuthenticate":"","iResult":0 ,"
	 * szRetErrMsg":""}, "body":{"uiUin":100001," uiAreaId":0,"uiWorldId":0}}
	 * 
	 * @return
	 */

}

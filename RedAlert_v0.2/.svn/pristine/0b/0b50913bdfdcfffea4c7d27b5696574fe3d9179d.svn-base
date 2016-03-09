package com.youxigu.dynasty2.openapi.domain;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonNode;

/**
 * 文件名    Head.java
 *
 * 描  述    头，
 *
 * 时  间    2014-8-22
 *
 * 作  者    huhaiquan
 *
 * 版  本   v2.3  
 */
public class Head {
	private int PacketLen;
	private int Cmdid;      /* 命令ID */
	private int Seqid;            /* 流水号 */
	private String ServiceName;     /* 服务名 */
	private int SendTime;          /* 发送时间YYYYMMDD对应的整数 */
	private int Version;          /* 版本号 */
	private String Authenticate;    /* 加密串 */
	private int Result;            /* 错误码,返回码类型：0：处理成功，需要解开包体获得详细信息,1：处理成功，但包体返回为空，不需要处理包体（eg：查询用户角色，用户角色不存在等），-1: 网络通信异常,-2：超时,-3：数据库操作异常,-4：API返回异常,-5：服务器忙,-6：其他错误,小于-100 ：用户自定义错误，需要填写szRetErrMsg */
	private String RetErrMsg;       /* 错误信息 */
	
	public final static Head buildHead(JsonNode node){
		Head head = new Head();
		head.setAuthenticate(node.path("Authenticate").getValueAsText());
		head.setCmdid(node.path("Cmdid").getIntValue());
		head.setPacketLen(node.path("PacketLen").getIntValue());
		head.setResult(node.path("Result").getIntValue());
		head.setRetErrMsg(node.path("RetErrMsg").getValueAsText());
		head.setSendTime(node.path("SendTime").getIntValue());
		head.setSeqid(node.path("Seqid").getIntValue());
		head.setServiceName(node.path("ServiceName").getValueAsText());
		head.setVersion(node.path("Version").getIntValue());
		return head;
	}
	
	/**
	 * @return the packetLen
	 */
	public int getPacketLen() {
		return PacketLen;
	}

	/**
	 * @param packetLen the packetLen to set
	 */
	public void setPacketLen(int packetLen) {
		PacketLen = packetLen;
	}

	/**
	 * @return the cmdid
	 */
	public int getCmdid() {
		return Cmdid;
	}

	/**
	 * @param cmdid the cmdid to set
	 */
	public void setCmdid(int cmdid) {
		Cmdid = cmdid;
	}

	/**
	 * @return the seqid
	 */
	public int getSeqid() {
		return Seqid;
	}

	/**
	 * @param seqid the seqid to set
	 */
	public void setSeqid(int seqid) {
		Seqid = seqid;
	}

	/**
	 * @return the serviceName
	 */
	public String getServiceName() {
		return ServiceName;
	}

	/**
	 * @param serviceName the serviceName to set
	 */
	public void setServiceName(String serviceName) {
		ServiceName = serviceName;
	}

	/**
	 * @return the sendTime
	 */
	public int getSendTime() {
		return SendTime;
	}

	/**
	 * @param sendTime the sendTime to set
	 */
	public void setSendTime(int sendTime) {
		SendTime = sendTime;
	}

	/**
	 * @return the version
	 */
	public int getVersion() {
		return Version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(int version) {
		Version = version;
	}

	/**
	 * @return the authenticate
	 */
	public String getAuthenticate() {
		return Authenticate;
	}

	/**
	 * @param authenticate the authenticate to set
	 */
	public void setAuthenticate(String authenticate) {
		Authenticate = authenticate;
	}

	/**
	 * @return the result
	 */
	public int getResult() {
		return Result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(int result) {
		Result = result;
	}

	/**
	 * @return the retErrMsg
	 */
	public String getRetErrMsg() {
		return RetErrMsg;
	}

	/**
	 * @param retErrMsg the retErrMsg to set
	 */
	public void setRetErrMsg(String retErrMsg) {
		RetErrMsg = retErrMsg;
	}

	/**
	 * <entry name="PacketLen" type="uint32" desc="包长"/>
    <entry name="Cmdid" type="uint32" desc="命令ID"/>
    <entry name="Seqid" type="uint32" desc="流水号"/>
    <entry name="ServiceName" type="string" size="SERVICE_NAME_LENGTH" desc="服务名"/>
    <entry name="SendTime" type="uint32" desc="发送时间YYYYMMDD对应的整数"/>
    <entry name="Version" type="uint32" desc="版本号"/>
    <entry name="Authenticate" type="string" size="AUTHENTICATE_LENGTH" desc="加密串"/>
    <entry name="Result" type="int32" desc="错误码,返回码类型：0：处理成功，需要解开包体获得详细信息,1：处理成功，但包体返回为空，不需要处理包体（eg：查询用户角色，用户角色不存在等），-1: 网络通信异常,-2：超时,-3：数据库操作异常,-4：API返回异常,-5：服务器忙,-6：其他错误,小于-100 ：用户自定义错误，需要填写szRetErrMsg"/>
    <entry name="RetErrMsg" type="string" size="ERROR_MSG_LENGTH" desc="错误信息"/>
	 * 
	 * */
	public Map<String,Object> toMap(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("PacketLen", PacketLen);
		map.put("Cmdid", Cmdid);
		map.put("Seqid", Seqid);
		map.put("ServiceName", ServiceName);
		map.put("SendTime", SendTime);
		map.put("Version", Version);
		map.put("Authenticate", Authenticate);
		map.put("Result", Result);
		map.put("RetErrMsg", RetErrMsg);
		return map;
	}
	
}

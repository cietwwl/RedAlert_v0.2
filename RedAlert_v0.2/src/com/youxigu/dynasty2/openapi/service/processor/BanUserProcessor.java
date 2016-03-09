package com.youxigu.dynasty2.openapi.service.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonNode;

import com.youxigu.dynasty2.user.domain.Account;
import com.youxigu.dynasty2.user.domain.User;

/**
 * 被封玩家信息
 *
 */

public class BanUserProcessor extends AIdipProcessor {
//	"body" :
//	{
//	    "AreaId" : ,       /* 服务器：微信（1），手Q（2） */
//	    "PlatId" : ,       /* 平台：IOS（0），安卓（1） */
//	    "Partition" : ,    /* 小区ID */
//	    "OpenId" : ""      /* openid */
//	}
	
//    "body" :
//    {
//        "BanUsrList_count" : ,    /* 玩家被封信息列表的最大数量 */
//        "BanUsrList" :            /* 玩家被封信息列表 */
//        [
//            {
//                "Partition" : ,      /* 所在小区ID */
//                "RoleName" : "",     /* 角色名 */
//                "RoleId" : ,         /* 角色ID */
//                "BeginTime" : "",    /* 封号时间点 */
//                "Time" : ,           /* 被封时长 */
//                "EndTime" : "",      /* 解封时间 */
//                "Banreason" : ""     /* 封号原因 */
//            }
//        ]
//    }
	
	@Override
	protected Status doProcessAndView(JsonNode body,
			Map<String, Object> returnBody) {
		//如果OPenid为空，查列表，否则只查单个信息
		int areaId = body.path("Partition").getIntValue();
//		String openId = body.path("OpenId").getValueAsText(); 
		List<Account> list = accountService.getBanList(areaId);
		int partition = body.path("Partition").getIntValue();
		List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>(list.size());
		for(Account account : list){
			Map<String, Object> data = this.createBanInfo(account,partition);
			if(data!=null){
				dataList.add(data);
			}
			
		}
		returnBody.put("BanUsrList_count", dataList.size());
		returnBody.put("BanUsrList", dataList);
		return new Status();
	}

	private Map<String, Object> createBanInfo(Account account,int partition) {
//		"Partition" : ,      /* 所在小区ID */
//      "RoleName" : "",     /* 角色名 */
//      "RoleId" : ,         /* 角色ID */
//      "BeginTime" : "",    /* 封号时间点 */
//      "Time" : ,           /* 被封时长 */
//      "EndTime" : "",      /* 解封时间 */
//      "Banreason" : ""     /* 封号原因 */
		Map<String,Object> dataMap = new HashMap<String, Object>(8);
		dataMap.put("Partition", partition);
		User user = userService.getUserByaccId(account.getAccId());
		if(user==null){
			return null;
		}
		dataMap.put("RoleName", user.getUserName());
		dataMap.put("RoleId", user.getUserId());
		dataMap.put("EndTime",account.getEnvelopDttm()==null?0: (int)(account.getEnvelopDttm().getTime()/1000));
		return dataMap;
	}

	@Override
	public int getCmd() {
		// TODO Auto-generated method stub
		return 4103;
	}

	@Override
	public int getRetrunCmd() {
		// TODO Auto-generated method stub
		return 4104;
	}

}

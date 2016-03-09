package com.youxigu.dynasty2.openapi.web;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.manu.core.ServiceLocator;
import com.youxigu.dynasty2.core.JSONUtil;
import com.youxigu.dynasty2.openapi.Constant;
import com.youxigu.dynasty2.openapi.domain.FaceBookRecharge;
import com.youxigu.dynasty2.openapi.service.IFaceBookService;
import com.youxigu.dynasty2.user.service.impl.UserService;
import com.youxigu.dynasty2.util.MD5;

/**
 * 文件名 FaceBookController.java
 * 
 * 描 述 海外对接
 * 
 * 时 间 2015-1-19
 * 
 * 作 者 huhaiquan
 * 
 * 版 本 v1.7
 */
@Controller
@RequestMapping("/fb/")
public class FaceBookController {
	private static Logger log = LoggerFactory.getLogger(UserService.class);
	private IFaceBookService faceBookService = (IFaceBookService) ServiceLocator
			.getSpringBean("faceBookService");

	/**
	 * 获得角色信息
	 * 
	 * 
	 * 参数名称 参数说明 备注 //userId EFUN账号ID 必填 //serverCode 游戏服id 必填 //sign
	 * MD5（key+userId +serverCode） 必填 大写 //gameCode 游戏代码 选填
	 * 
	 * 
	 * @param request
	 * @param response
	 * @param context
	 * @return
	 */
	@RequestMapping(value = "roleinfo.htm", method = { RequestMethod.GET,
			RequestMethod.POST })
	public void roleinfo(HttpServletRequest request,
			HttpServletResponse response) {
		PrintWriter pw = null;
		String context = null;
		try {
			String userId = request.getParameter("userId");
			String serverCode = request.getParameter("serverCode");
			String sign = request.getParameter("sign");
			if (log.isDebugEnabled()) {
				log.debug("userId=" + userId + ",serverCode=" + serverCode
						+ ",sign=" + sign);
			}
			String check = Constant.FACEBOOK_APP_KEY + userId + serverCode;
			check = MD5.getMD5(check);
			check = check.toUpperCase();
			Map<String, Object> json = new HashMap<String, Object>();
			String code;
			if (check.equals(sign)) {
				try {
					List<Map<String, Object>> list = faceBookService
							.getRoleList(userId, serverCode);
					if (list == null || list.size() == 0) {
						code = "0010";
					} else {
						code = "0000";
						json.put("list", list);
					}
				} catch (Exception e) {
					e.printStackTrace();
					code = "0100";

				}
			} else {
				code = "0011";
			}
			json.put("code", code);
			context = JSONUtil.toJson(json);

			// json { "code":"0000",list:[{"roleid":1058020," name":"xx xxx",
			// "level":5, subgame :”1”,onCard :”2014-11-11”}]}
			// name 角色名
			// Level 等级
			// roleid 角色Id
			// subgame 游戏服(serverCode)
			// onCard: 月卡、限购礼包的到期时间，无购买记录传”1970-01-01”,若游戏无月卡，可不传。
			// result 0000查询成功
			// 0010该用户无角色
			// 0100系统异常
			// 1000 传入参数为空
			// 0011 加密串不匹配
			// 0111 不存在的服务器id
			pw = response.getWriter();
			pw.write(context);
			pw.flush();
		} catch (Exception e) {
			e.printStackTrace();
			context = "{\"code\":\"" + 0100 + "\"}";
			pw.write(context);
			pw.flush();
		} finally {
			if (pw != null) {
				pw.close();
			}
		}

	}

	@RequestMapping(value = "recharge.htm", method = { RequestMethod.GET,
			RequestMethod.POST })
	public void recharge(HttpServletRequest request,
			HttpServletResponse response) {
		PrintWriter pw = null;
		String code = null;
		try {// {"code":"0011"}
			if (log.isDebugEnabled()) {
				log.debug(JSONUtil.toJson(request.getParameterMap()));
			}

			// pOrderId 合作商的订单id（EFUN订单号,确保唯一） 必填，20位
			// userId EFUN账号ID 必填，20位
			// creditId 储值id，调用储值时SDK传送的值，根据这个id可以发送游戏币到对应的角色) 必填，50位
			// currency 币种 必填，USD
			// amount 币种对应的总金额 必填，美金，两位小数
			// RCurrency 原币种 选填，原币种
			// RAmount 原币种对应金额 选填，金额
			// gameCode 游戏代码 选填
			// serverCode 服务器代码，正式服+1递增，测试服999 必填
			// stone 游戏币数量 必填
			// stoneType 游戏币类型 gold / diamond 金币/钻石
			// 游戏内存在可购买的多虚拟币种情况下使用。只有一种， 可传空 选填
			// md5Str MD5(pOrderId+serverCode+creditId+userId +amount +stone
			// +stoneType+time+key) 必填，大写
			// time 请求发游戏币时间 必填，时间戳
			// remark 自定义数据串 详见注意事项6 选填
			// activityExtra 储值活动额外赠送的游戏币，若无活动则默认传0 详见注意事项3 必填,不参与首冲多倍或者其他储值活动计算
			// orderStateMonth 对账月。 格式：201401 详见注意事项4 选填
			// point 平台点数。 1点等于1台币 详见注意事项9 必填
			// freePoint 是否免费点。1：付费点，0：免费点 必填
			String pOrderId = request.getParameter("pOrderId");
			String userId = request.getParameter("userId");
			String creditId = request.getParameter("creditId");
			String currency = request.getParameter("currency");
			String amount = request.getParameter("amount");
			String RCurrency = request.getParameter("RCurrency");
			String RAmount = request.getParameter("RAmount");
			String gameCode = request.getParameter("gameCode");
			String serverCode = request.getParameter("serverCode");
			String stone = request.getParameter("stone");
			String stoneType = request.getParameter("stoneType");
			String md5Str = request.getParameter("md5Str");
			String time = request.getParameter("time");

			String remark = request.getParameter("remark");
			String activityExtra = request.getParameter("activityExtra");
			String orderStateMonth = request.getParameter("orderStateMonth");
			String point = request.getParameter("point");
			String freePoint = request.getParameter("freePoint");
			StringBuilder checkContext = new StringBuilder(100);
			// MD5(pOrderId+serverCode+creditId+userId +amount +stone
			// +stoneType+time+key) 必填，大写
			
			checkContext.append(pOrderId).append(serverCode).append(creditId);
			checkContext.append(userId).append(amount).append(stone);
			checkContext.append(stoneType).append(time).append(
					Constant.FACEBOOK_APP_KEY);
			String check = MD5.getMD5(checkContext.toString()).toUpperCase();
			if(log.isDebugEnabled()){
				log.debug(checkContext.toString());
				log.debug(check);
			}
			
			
			// 0000发游戏成功(订单号重复的也请返回成功，但只发送一次游戏币)
			// 0010发游戏失败
			// 0100系统异常
			// 1000 传入参数为空
			// 0011 加密串不匹配
			if (check.equals(md5Str)) {
				FaceBookRecharge bookRecharge = new FaceBookRecharge();
				bookRecharge.setActivityExtra(Integer.parseInt(activityExtra));
				bookRecharge.setAmount(amount);
				bookRecharge.setCurrency(currency);
				bookRecharge.setFreePoint(Integer.parseInt(freePoint));
				bookRecharge.setGameCode(gameCode);
				bookRecharge.setMd5Str(md5Str);
				bookRecharge.setUserId(userId);
				bookRecharge.setOrderStateMonth(orderStateMonth);
				bookRecharge.setPoint(Integer.parseInt(point));
				bookRecharge.setPOrderId(pOrderId);
				bookRecharge.setRAmount(RAmount);
				bookRecharge.setRCurrency(RCurrency);
				bookRecharge.setRemark(remark);
				bookRecharge.setServerCode(serverCode);
				bookRecharge.setStone(Integer.parseInt(stone));
				bookRecharge.setStoneType(stoneType);
				bookRecharge.setTime(time);
				bookRecharge.setCreditId(creditId);
				code = faceBookService.doRecharge(bookRecharge);
			} else {
				code = "0011";
			}
			pw = response.getWriter();
			pw.write("{\"code\":\"" + code + "\"}");
			pw.flush();
		} catch (Exception e) {
			e.printStackTrace();
			code = "0100";
			pw.write("{\"code\":\"" + code + "\"}");
			pw.flush();
		} finally {
			if (pw != null) {
				pw.close();
			}
		}

	}

}

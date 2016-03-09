package com.youxigu.dynasty2.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @creator zhaiyong
 * @creatortime 2012-8-28 下午01:41:23
 *
 */
@Controller
@RequestMapping("/")
public class LoginController {

	// 登陆首页接受腾讯平台传入参数
	@RequestMapping(value = "login.htm", method = RequestMethod.GET)
	public String login(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false, value = "openId") String openId,
			@RequestParam(required = false, value = "openKey") String openKey,
			ModelMap modelMap) {
		// Map<String, Object> params = new HashMap<String,Object>();
		// 根据传入参数设置用户登陆信息
		// accountService.login(params);
		return "index";
	}

}

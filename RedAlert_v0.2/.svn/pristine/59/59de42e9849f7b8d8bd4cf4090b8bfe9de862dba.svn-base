package com.youxigu.demo.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.youxigu.dynasty2.user.service.IAccountService;

public class DemoControl {
	private IAccountService accountService;

	public void setAccountService(IAccountService accountService) {
		this.accountService = accountService;
	}

	public ModelAndView login(HttpServletRequest request,
			HttpServletResponse response) {

		// 这里指向hello.jsp
		ModelAndView mv = new ModelAndView("hello");
		mv.addObject("message", "hello,world");
		request.setAttribute("test", "test111");
		return mv;
	}

	public ModelAndView hello2(HttpServletRequest request,
			HttpServletResponse response) {

		// 这里指向hello.jsp
		ModelAndView mv = new ModelAndView("hello");
		mv.addObject("message", "hello2,world!!!!!");

		return mv;
	}

}

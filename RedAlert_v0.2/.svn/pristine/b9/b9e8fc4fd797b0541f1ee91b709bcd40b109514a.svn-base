package com.youxigu.wolf.node.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.youxigu.wolf.net.IWolfService;
import com.youxigu.wolf.net.Response;

/**
 * 外部应用嵌入service
 * 
 * @author Administrator
 * 
 */
public class AppInitService implements IWolfService {
	private static Logger log = LoggerFactory.getLogger(AppInitService.class);
	private String appSpringFile = null;

	public void setAppSpringFile(String appSpringFile) {
		this.appSpringFile = appSpringFile;
	}

	public AppInitService() {

		//
		// new Thread(new Runnable() {
		//
		// @Override
		// public void run() {
		// // 初始化应用嵌入的service
		//
		// if (appSpringFile != null) {
		// try {
		// log.info("加载嵌入用户服务......");
		// String[] fileArr = appSpringFile.split(",");
		// ApplicationContext context = new ClassPathXmlApplicationContext(
		// fileArr, true, ServiceLocator.getInstance()
		// .getApplicationContext());
		// if (context != null) {
		// ServiceLocator.getInstance().setApplicationContext(
		// context);
		// }
		// log.info("加载嵌入用户服务完成......");
		// } catch (Exception e) {
		// e.printStackTrace();
		// log.error(e.toString());
		// }
		// }
		// }
		// }).start();

	}

	public Object handleMessage(Response response, Object message) {
		return null;
	}

	@Override
	public void stop(boolean force) {
		// TODO Auto-generated method stub
		
	}

}

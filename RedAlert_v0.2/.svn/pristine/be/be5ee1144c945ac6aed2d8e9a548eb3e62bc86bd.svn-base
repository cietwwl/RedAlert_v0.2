package com.youxigu.demo.job;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.manu.core.ServiceLocator;
import com.youxigu.dynasty2.core.wolf.IWolfClientService;
import com.youxigu.wolf.node.job.Job;

public class JobTest {

	public static void main(String[] args) {
		ApplicationContext ctx = null;
		try {
			ctx = new ClassPathXmlApplicationContext("applicationContext.xml");

			final IWolfClientService wolfClient = (IWolfClientService) ServiceLocator
					.getSpringBean("wolfClientService");
			wolfClient.init();
			for (int i = 0; i < 10; i++) {
				Job job = new Job();
				job.setServiceName("demoJobService");
				job.setMethodName("processJob");
				job.setJobParams(new Object[] { i + 1 });
				job.setJobGroupName(Job.DEFAULT_GROUP_NAME);
				job.setJobExcuteTime(System.currentTimeMillis() + (i + 5)
						* 1000);
				job.setJobIdInGroup(String.valueOf(i + 1));
				// 启动一个任务是异步的
				wolfClient.startJob(job);
			}
			System.out.println("------------");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ctx != null)
				((ConfigurableApplicationContext) ctx).close();

		}
	}
}

package com.youxigu.demo.amf;

/**
 * 定义一个类
 * 
 * 
 * 通常命名为***Action
 * 
 * 在conf/applicationContext_***.xml中定义bean:<br>
 * <bean id="helloWorldAction" class="com.youxigu.amf.demo.HelloWorldAction" />
 * 
 * 在WEB-INF/flex-servlet.xml中定义flex:remoting-destination,<br>
 * <flex:remoting-destination ref="helloWorldAction"/>
 * 
 * 
 * helloWorldAction就可以在flash端使用
 * 
 * <mx:RemoteObject id="ro" destination="helloWorldAction"
 * result="resultHandler(event)" />
 * 
 * Action返回值需要与flash端约定好，用简单类型/集合类型/自定义对象等等
 * 
 * @author Administrator
 * 
 */
public class HelloWorldAction {

	public String sayHelloTo(String args) {
		System.out.println("测试。/。。。。。");
		int a = 1 / 0;
		// throw new RuntimeException("异常1111");
		return "Hello World!" + args;

	}

}

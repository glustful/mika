package com.yxst.epic.yixin.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DynamicProxyHandler implements InvocationHandler {

	private Object business; // 被代理对象

	private InterceptResponseClass interceptor = new InterceptResponseClass();

	/**
	 * 动态生成一个代理类对象,并绑定被代理类和代理处理器
	 * 
	 * @param business
	 * @return 代理类对象
	 */
	public Object bind(Object business) {
		this.business = business;
		// 被代理类的ClassLoader
		// 要被代理的接口,本方法返回对象会自动声称实现了这些接口
		// 代理处理器对象
		return Proxy.newProxyInstance(business.getClass().getClassLoader(),
				business.getClass().getInterfaces(), this);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		Object result = null;
		interceptor.before();
		result = method.invoke(business, args);
		interceptor.after();
		return null; // To change body of implemented methods use File |
						// Settings | File Templates.
	}

}

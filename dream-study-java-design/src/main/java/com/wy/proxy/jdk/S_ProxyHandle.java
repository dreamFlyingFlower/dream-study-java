package com.wy.proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 代理类,必须实现InvocationHandler接口
 *
 * @author ParadiseWY
 * @date 2020-09-26 23:46:44
 */
public class S_ProxyHandle implements InvocationHandler {

	private Object targetObject;

	public S_ProxyHandle(Object targetObject) {
		this.targetObject = targetObject;
	}

	/**
	 * 调用代理方法
	 * 
	 * @return 被代理的接口
	 */
	public Object createProxyInstance() {
		return Proxy.newProxyInstance(this.targetObject.getClass().getClassLoader(),
				this.targetObject.getClass().getInterfaces(), this);
	}

	/**
	 * 代理方法
	 * 
	 * @param proxy 生成的代理对象,不是真实的被代理对象,临时类
	 * @param method 被代理类中的方法
	 * @param args 调用被代理类中方法的参数
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.println("在调用方法之前用,就是前置拦截");
		Object invoke = method.invoke(targetObject, args);
		System.out.println("在调用方法之后用,就是后置拦截");
		return invoke;
	}
}
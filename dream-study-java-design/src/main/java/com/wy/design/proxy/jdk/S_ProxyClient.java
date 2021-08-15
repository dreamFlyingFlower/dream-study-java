package com.wy.design.proxy.jdk;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * 测试代理
 *
 * @author 飞花梦影
 * @date 2020-09-26 23:49:14
 */
public class S_ProxyClient {

	public static void main(String[] args) {
		S_ProxyHandle proxyHandle = new S_ProxyHandle(new S_ProxyClass());
		S_Proxy proxyExample = (S_Proxy) proxyHandle.createProxyInstance();
		proxyExample.print("sfdsfds");
	}

	public static Object ProxyBase(Class<?> clazz, Class<?>[] interfaces, InvocationHandler T) {
		if (null == T) {
			return null;
		}
		// 获得与指定类装载器和一组接口相关的代理类类型对象
		Class<?> proxyClass = Proxy.getProxyClass(clazz.getClassLoader(), interfaces);
		// 通过反射获取构造函数对象并生成代理类实例
		try {
			Constructor<?> constructor = proxyClass.getConstructor();
			return (Object) constructor.newInstance(T);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
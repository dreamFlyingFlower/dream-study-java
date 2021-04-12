package com.wy.common;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class BaseProxy{

	private BaseProxy(){
		
	}
	
	public static Object ProxyBase(Class<?> clazz, Class<?>[]interfaces,InvocationHandler T){
		if(null == T){
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

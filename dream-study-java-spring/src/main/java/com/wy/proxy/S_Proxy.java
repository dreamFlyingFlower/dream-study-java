package com.wy.proxy;

import java.lang.reflect.Method;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

public class S_Proxy implements MethodInterceptor {

	@Override
	public Object intercept(Object obj, Method method, Object[] params, MethodProxy methodProxy) throws Throwable {
		System.out.println(params.toString());
		return methodProxy.invokeSuper(obj, params);
	}
}
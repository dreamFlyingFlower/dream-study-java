package com.wy.proxy;

import org.springframework.cglib.proxy.Enhancer;

/**
 * CGLIB动态代理.和JDK动态代理不一样,被代理类不需要实现接口,但需要继承
 * 
 * @author 飞花梦影
 * @date 2021-08-15 09:52:51
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class S_ProxyClient {

	/**
	 * spring的反射代理
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		S_Proxy t1 = new S_Proxy();
		Enhancer e1 = new Enhancer();
		e1.setSuperclass(S_Pojo.class);
		e1.setCallback(t1);
		S_Pojo p = (S_Pojo) e1.create();
		p.setUsername("test1111");
		System.out.println("33333333" + p.getUsername());
	}
}
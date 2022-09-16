package com.wy.proxy.jdk;

/**
 * 必须实现被代理接口
 * 
 * @author 飞花梦影
 * @date 2020-09-26 23:50:53
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class S_ProxyClass implements S_Proxy {

	@Override
	public void print(String string) {
		System.out.println(string);
	}
}
package com.wy.proxy.jdk;

/**
 * 必须实现被代理接口
 * 
 * @author ParadiseWY
 * @date 2020-09-26 23:50:53
 */
public class S_ProxyClass implements S_Proxy {

	@Override
	public void print(String string) {
		System.out.println(string);
	}
}
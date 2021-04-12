package com.wy.design.proxy;

/**
 * 必须实现被代理接口
 *
 * @author ParadiseWY
 * @date 2020-09-26 23:54:24
 */
public class S_ProxyClass implements S_Proxy {

	@Override
	public void cry() {
		System.out.println("class cry.....");
	}

	@Override
	public void eat() {
		System.out.println("class eat...");
	}
}
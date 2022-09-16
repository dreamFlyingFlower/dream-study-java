package com.wy.proxy;

/**
 * 必须实现被代理接口
 *
 * @author 飞花梦影
 * @date 2020-09-26 23:54:24
 * @git {@link https://github.com/dreamFlyingFlower }
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
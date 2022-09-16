package com.wy.proxy;

/**
 * 默认的实现类,非代理类
 *
 * @author 飞花梦影
 * @date 2020-09-26 23:30:37
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class S_ProxyDefault implements S_Proxy {

	@Override
	public void cry() {
		System.out.println("Default cry...");
	}

	@Override
	public void eat() {
		System.out.println("Default eat...");
	}
}
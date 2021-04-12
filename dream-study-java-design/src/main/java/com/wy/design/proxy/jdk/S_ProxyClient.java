package com.wy.design.proxy.jdk;

/**
 * 测试代理
 *
 * @author ParadiseWY
 * @date 2020-09-26 23:49:14
 */
public class S_ProxyClient {

	public static void main(String[] args) {
		S_ProxyHandle proxyHandle = new S_ProxyHandle(new S_ProxyClass());
		S_Proxy proxyExample = (S_Proxy) proxyHandle.createProxyInstance();
		proxyExample.print("sfdsfds");
	}
}

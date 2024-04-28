package com.wy.netty.tcp.client;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2024-04-28 17:17:46
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class NettyClientHolder {

	private static final ConcurrentHashMap<String, NettyClient> clientMap = new ConcurrentHashMap<>();

	public static ConcurrentHashMap<String, NettyClient> get() {
		return clientMap;
	}
}
package com.wy.websocket.netty;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import io.netty.channel.Channel;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2024-04-30 10:49:33
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class UserConnectPool {

	private static ConcurrentHashMap<String, Channel> channelMap = new ConcurrentHashMap<>();

	private static CopyOnWriteArrayList<Channel> channelGroup = new CopyOnWriteArrayList<>();

	public static ConcurrentHashMap<String, Channel> getChannelMap() {
		return channelMap;
	}

	public static CopyOnWriteArrayList<Channel> getChannelGroup() {
		return channelGroup;
	}
}
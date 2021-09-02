package com.wy.util;

import io.netty.channel.ChannelFuture;

/**
 * Netty工具类
 * 
 * @author 飞花梦影
 * @date 2021-09-02 23:33:45
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class NettyUtils {

	public static void closeFuture(ChannelFuture future) {
		if (null != future) {
			try {
				future.channel().closeFuture().sync();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
package com.wy.netty.tcp;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * 本地队列,实际生产中应该使用消息中间件代替(rocketmq或rabbitmq)
 *
 * @author 飞花梦影
 * @date 2024-04-28 17:03:22
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class QueueHolder {

	private static final ArrayBlockingQueue<NettyMsgModel> queue = new ArrayBlockingQueue<>(100);

	public static ArrayBlockingQueue<NettyMsgModel> get() {
		return queue;
	}
}
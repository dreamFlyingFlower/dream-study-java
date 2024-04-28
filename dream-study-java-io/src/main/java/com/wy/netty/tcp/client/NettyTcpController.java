package com.wy.netty.tcp.client;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wy.netty.tcp.NettyMsgModel;
import com.wy.netty.tcp.QueueHolder;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2024-04-28 17:22:35
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@RestController
@RequestMapping("/nettyTcp")
public class NettyTcpController {

	/**
	 * 间隔发送两条消息
	 */
	@GetMapping("testOne")
	public void testOne() {
		QueueHolder.get().offer(NettyMsgModel.create("87654321", "Hello World!"));
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		QueueHolder.get().offer(NettyMsgModel.create("87654321", "Hello World Too!"));
	}

	/**
	 * 任意发送消息
	 *
	 * @param imei
	 * @param msg
	 */
	@GetMapping("testTwo")
	public void testTwo(@RequestParam String imei, @RequestParam String msg) {
		QueueHolder.get().offer(NettyMsgModel.create(imei, msg));
	}

	/**
	 * 连续发送两条消息 第二条由于redis锁将会重新放回队列延迟消费
	 */
	@GetMapping("testThree")
	public void testThree() {
		QueueHolder.get().offer(NettyMsgModel.create("12345678", "Hello World!"));
		QueueHolder.get().offer(NettyMsgModel.create("12345678", "Hello World Too!"));
	}
}
package com.wy.example.consumer;

import java.util.Random;

import com.wy.example.queue.MyQueue1;

/**
 * 消费者
 *
 * @author 飞花梦影
 * @date 2023-05-10 13:59:23
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class MyConsumer1 extends Thread {

	private final MyQueue1 myQueue;

	private final Random random = new Random();

	public MyConsumer1(MyQueue1 myQueue) {
		this.myQueue = myQueue;
	}

	@Override
	public void run() {
		while (true) {
			String s = myQueue.get();
			System.out.println("\t\t消费元素：" + s);
			try {
				Thread.sleep(random.nextInt(1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
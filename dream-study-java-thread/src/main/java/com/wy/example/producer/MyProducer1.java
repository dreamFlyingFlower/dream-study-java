package com.wy.example.producer;

import java.util.Random;

import com.wy.example.queue.MyQueue1;

/**
 * 生产者
 *
 * @author 飞花梦影
 * @date 2023-05-10 13:58:20
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class MyProducer1 extends Thread {

	private final MyQueue1 myQueue;

	private final Random random = new Random();

	private int index = 0;

	public MyProducer1(MyQueue1 myQueue) {
		this.myQueue = myQueue;
	}

	@Override
	public void run() {
		while (true) {
			String tmp = "ele-" + index;
			myQueue.put(tmp);
			System.out.println("添加元素：" + tmp);
			index++;
			try {
				Thread.sleep(random.nextInt(1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
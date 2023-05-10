package com.wy.example;

import com.wy.example.consumer.MyConsumer1;
import com.wy.example.producer.MyProducer1;
import com.wy.example.queue.MyQueue1;

/**
 * 单个生产者和消费者测试模式
 *
 * @author 飞花梦影
 * @date 2023-05-10 14:00:34
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class MyTest1 {

	public static void main(String[] args) {
		MyQueue1 myQueue = new MyQueue1();
		MyProducer1 producerThread = new MyProducer1(myQueue);
		MyConsumer1 consumerThread = new MyConsumer1(myQueue);
		producerThread.start();
		consumerThread.start();
	}
}
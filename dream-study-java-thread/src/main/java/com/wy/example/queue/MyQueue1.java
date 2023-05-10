package com.wy.example.queue;

/**
 * 自定义Queue
 *
 * @author 飞花梦影
 * @date 2023-05-10 13:57:35
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class MyQueue1 {

	private String[] data = new String[10];

	private int getIndex = 0;

	private int putIndex = 0;

	private int size = 0;

	public synchronized void put(String element) {
		if (size == data.length) {
			try {
				// 可能被生产者唤醒,也可能被消费者唤醒
				// 生产者唤醒时,需要重新判断size,防止超过队列长度
				// 消费者唤醒时,可以直接走下面的程序,但是无法判断是被谁唤醒,故而可以都重新走一次
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			put(element);
		} else {
			data[putIndex] = element;
			++putIndex;
			if (putIndex == data.length) {
				putIndex = 0;
			}
			++size;
			// 唤醒的可能是生产者,也可能是消费者
			notify();
		}
	}

	public synchronized String get() {
		if (size == 0) {
			try {
				// 可能被生产者唤醒,也可能被消费者唤醒,处理方式同put,也可以将if换成while
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return get();
		} else {
			String result = data[getIndex];
			++getIndex;
			if (getIndex == data.length) {
				getIndex = 0;
			}
			--size;
			notify();
			return result;
		}
	}
}
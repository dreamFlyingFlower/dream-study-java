package com.wy.study;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 多线程限流,即多线程时,限制只有N个线程能同时访问同一个资源
 * 
 * @author 飞花梦影
 * @date 2019-03-09 22:10:54
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class StudySemaphonre {

	private static ExecutorService pool = Executors.newCachedThreadPool();

	public static void main(String[] args) {
		StudySemaphonre ex = new StudySemaphonre();
		ex.execute();
	}

	private void execute() {
		// 设定同时只能有2个线程同时访问
		Semaphore s = new Semaphore(2);
		// 是否为公平锁,默认false非公平锁,一般非公平抢占效率较高
		new Semaphore(2, false);
		for (int i = 0; i < 20; i++) {
			pool.execute(new TaskRun(s, i + 1));
		}
		pool.shutdown();
	}
}

class TaskRun implements Runnable {

	private Semaphore semaphore;// 信号量

	private int num; // 记录第几个访问

	public TaskRun(Semaphore semaphore, int num) {
		this.semaphore = semaphore;
		this.num = num;
	}

	@Override
	public void run() {
		try {
			// 获取信号量许可,只能同时有semaphore设置的指定数量的线程数可访问该操作
			// 工作线程每获取一份资源,就在该对象上记下来
			semaphore.acquire();
			// 需要进行的操作
			System.out.println("第" + num + "个对象准备开始操作");
			Thread.sleep((long) (Math.random() * 10000));
			System.out.println("第" + num + "个对象开始操作");
			Thread.sleep((long) (Math.random() * 10000));
			System.out.println("第" + num + "个对象完成操作");
			Thread.sleep((long) (Math.random() * 10000));
			// 释放信号量.工作线程每归还一份资源,就在该对象上记下来,此时资源可以被其他线程使用
			semaphore.release();
			// 释放指定数目的许可,并将它们归还给信标
			// 如果线程需要获取N个许可,在有N个许可可用之前,该线程阻塞
			// 如果线程获取了N个许可,还有可用的许可,则依次将这些许可赋予等待获取许可的其他线程路
			semaphore.release(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
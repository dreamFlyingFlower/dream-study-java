package com.wy.study;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 多线程限流,即多线程时,限制只有N个线程能同时访问同一个资源
 * 
 * @author ParadiseWY
 * @date 2019-03-09 22:10:54
 * @git {@link https://github.com/mygodness100}
 */
public class S_Semaphonre {

	private static ExecutorService pool = Executors.newCachedThreadPool();

	public static void main(String[] args) {
		S_Semaphonre ex = new S_Semaphonre();
		ex.execute();
	}

	private void execute() {
		// 设定同时只能有2个线程同时访问
		Semaphore s = new Semaphore(2);
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
			semaphore.acquire();
			// 需要进行的操作
			System.out.println("第" + num + "个对象准备开始操作");
			Thread.sleep((long) (Math.random() * 10000));
			System.out.println("第" + num + "个对象开始操作");
			Thread.sleep((long) (Math.random() * 10000));
			System.out.println("第" + num + "个对象完成操作");
			Thread.sleep((long) (Math.random() * 10000));
			// 释放信号量
			semaphore.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
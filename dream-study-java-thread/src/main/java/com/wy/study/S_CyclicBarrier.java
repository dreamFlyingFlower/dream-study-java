package com.wy.study;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Cyclic可循环的障碍物,可多次使用,多线程运行时,当多线程的数量达到指定数目时才继续执行程序
 * 和CountDownLatch的区别在于使用次数,CountDownLatch只能用一次
 * 
 * 该类和Semaphonre都是为多线程高并发业务场景时使用
 *
 * @author ParadiseWY
 * @date 2019-03-09 22:42:50
 * @git {@link https://github.com/mygodness100}
 */
public class S_CyclicBarrier {

	private static ExecutorService pool = Executors.newCachedThreadPool();

	private static final int total = 3;

	public static void main(String[] args) {

		// 当多线程的数量达到3个时,才回继续执行后面的程序
		CyclicBarrier cb = new CyclicBarrier(total);
		for (int i = 0; i < total; i++) {
			int num = i + 1;
			Runnable r = new Runnable() {

				public void run() {
					try {
						// 模拟每个线程获得时间片的时间不一样
						Thread.sleep((long) (Math.random() * 10000));
						System.out.println(num + "之前已经有" + cb.getNumberWaiting() + "个线程到达");
						// 阻塞,当阻塞的线程数和定义Cyclic时相同时,才执行最后一步
						cb.await();
						// 线程数足够才执行后面的程序
						System.out.println("执行最后一步");
					} catch (InterruptedException | BrokenBarrierException e) {
						e.printStackTrace();
					}
				}
			};
			pool.execute(r);
		}

		// 该构造函数的作用是,当执行的线程数达到total时,先执行构造函数中的线程,再执行主函数中的线程
		CyclicBarrier cb1 = new CyclicBarrier(total, new Runnable() {

			@Override
			public void run() {
				System.out.println("先执行");
			}
		});
		for (int i = 0; i < total; i++) {
			int num = i + 1;
			Runnable r = new Runnable() {

				public void run() {
					try {
						// 模拟每个线程获得时间片的时间不一样
						Thread.sleep((long) (Math.random() * 10000));
						System.out.println(num + "之前已经有" + cb.getNumberWaiting() + "个线程到达");
						// 阻塞,当阻塞的线程数和定义Cyclic时相同时,先执行构造函数中的线程,再执行阻塞中的线程
						cb1.await();
						// 线程数足够才执行后面的程序
						System.out.println("执行最后一步");
					} catch (InterruptedException | BrokenBarrierException e) {
						e.printStackTrace();
					}
				}
			};
			pool.execute(r);
		}
		pool.shutdown();
	}
}
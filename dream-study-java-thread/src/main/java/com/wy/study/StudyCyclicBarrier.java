package com.wy.study;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * {@link CyclicBarrier}:Cyclic可循环的障碍物,可多次使用,多线程运行时,当多线程的数量达到指定数目时才继续执行程序
 * 和CountDownLatch的区别在于使用次数,CountDownLatch只能用一次,且CyclicBarrier不需要调用countDownLatch(),达到数量自动释放
 * 
 * CyclicBarrier会响应中断,10个线程没有到齐,如果有线程收到了中断信号,所有阻寒的线程也会被唤醒
 * 
 * {@link CyclicBarrier#await()}:基于ReentrantLock+Condition实现.每个线程调用一次await(),count都减1;当count减到0的时候,此线程唤醒其他所有线程
 * 
 * @author 飞花梦影
 * @date 2019-03-09 22:42:50
 * @git {@link https://github.com/mygodness100}
 */
public class StudyCyclicBarrier {

	/**
	 * 如果此处使用有界线程池,线程池的数量最好和任务数一致
	 */
	private static ExecutorService pool = Executors.newCachedThreadPool();

	private static final int total = 3;

	public static void main(String[] args) {

		// 当多线程的数量达到3个时,才回继续执行后面的程序
		CyclicBarrier cb = new CyclicBarrier(total);
		for (int i = 0; i < total; i++) {
			int num = i + 1;
			Runnable r = new Runnable() {

				@Override
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

				@Override
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
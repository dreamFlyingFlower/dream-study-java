package com.wy.study;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Phaser;

/**
 * 功能类似{@link CountDownLatch}和{@link CyclicBarrier},但功能更强大
 * 
 * <pre>
 * {@link Phaser#arriveAndAwaitAdvance()}:arrive()与awaitAdvance()的组合,表示线程已到达某个同步点,要等待其他线程都到达后再继续
 * Phaser可以在运行期间动态地调整要同步的线程个数
 * {@link Phaser#register()}:注册一个
 * {@link Phaser#bulkRegister()}:注册多个
 * {@link Phaser#arriveAndDeregister()}:解除注册
 * 
 * Phaser内存阻塞使用的是Treiber Stack,是一个无锁的栈,是一个单向链表,出栈,入栈都在链表头部,所以只需要一个head指针,而不需要tail指针
 * 
 * {@link Phaser#evenQ},{@link Phaser#oddQ}:两个引用表示链表的头部,避免线程冲突.
 * 		当phase为奇数轮的时候,阻塞线程放在oddQ里面;当phase为偶数轮的时候,阻塞线程放在evenQ里面
 * </pre>
 *
 * @author 飞花梦影
 * @date 2023-04-18 13:45:14
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class StudyPhaser {

	public static void main(String[] args) {
		Phaser phaser = new Phaser(5);
		for (int i = 0; i < 5; i++) {
			new Thread("线程-" + (i + 1)) {

				private final Random random = new Random();

				@Override
				public void run() {
					System.out.println(getName() + "- 开始运行");
					try {
						Thread.sleep(random.nextInt(1000));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println(getName() + "- 运行结束");
					// 相当于CountDownLatch#countDown()
					phaser.arrive();
				}
			}.start();
		}
		System.out.println("线程启动完毕");
		// 相当于CountDownLatch#await(),在此等待
		phaser.awaitAdvance(phaser.getPhase());
		// 等待其他线程
		// phaser.arriveAndAwaitAdvance();
		System.out.println("线程运行结束");
	}
}
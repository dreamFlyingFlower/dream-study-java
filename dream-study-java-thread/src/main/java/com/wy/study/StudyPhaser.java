package com.wy.study;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Phaser;

/**
 * 功能类似{@link CountDownLatch}和{@link CyclicBarrier},但功能更强大
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
package com.wy.study;

import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch :闭锁,共享锁,在完成某些运算时,只有countDown()被调用指定次数后,当前运算才继续执行
 * 
 * @author ParadiseWY
 * @date 2020-11-24 22:57:39
 * @git {@link https://github.com/mygodness100}
 */
public class S_CountDownLatch {

	public static void main(String[] args) {
		// 参数表示调用50次countDown方法,就可以继续执行,否则就在调用await的地方一直等待
		final CountDownLatch latch = new CountDownLatch(50);
		LatchDemo ld = new LatchDemo(latch);
		long start = System.currentTimeMillis();
		for (int i = 0; i < 50; i++) {
			new Thread(ld).start();
		}
		try {
			// 当latch.countDown()没有执行50次的时候,所有的线程全部都在这里等待
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println("耗费时间为:" + (end - start));
	}
}

class LatchDemo implements Runnable {

	private CountDownLatch latch;

	public LatchDemo(CountDownLatch latch) {
		this.latch = latch;
	}

	@Override
	public void run() {

		try {
			for (int i = 0; i < 50000; i++) {
				if (i % 2 == 0) {
					System.out.println(i);
				}
			}
		} finally {
			// 每执行一次CountDownLatch中的state就+1,直到+50,此处CountDownLatch就失效,CountDownLatch就不再await
			latch.countDown();
		}
	}
}
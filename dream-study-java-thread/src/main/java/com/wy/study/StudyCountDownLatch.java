package com.wy.study;

import java.util.concurrent.CountDownLatch;

/**
 * {@link CountDownLatch}:闭锁,共享锁,在完成某些运算时,只有countDown()被调用指定次数后,当前运算才继续执行,基于AQS,但是没有公平和非公平之分
 * 
 * <pre>
 * {@link CountDownLatch#await()}:线程等待,阻塞,调用的是AQS的acquireSharedInterruptibly(),之后调用tryAcquireShared()
 * ->{@link CountDownLatch.Sync#tryAcquireShared()}:重写了AQS的方法,只要state != 0,调用await()方法的线程便会被放入AQS的阻塞队列,进入阻塞状态
 * {@link CountDownLatch#countDown()}:调用的AQS的releaseShared(),里面的tryReleaseShared()由CountDownLatch.Sync实现
 * ->{@link CountDownLatch.Sync#tryReleaseShared()}:只有state==0,该方法才会返回true,执行doReleaseShared(),一次性唤醒队列中所有阻塞的线程.
 * 		由于是基于AQS阻塞队列来实现的,所以可以让多个线程都阻塞在state==0条件上,通过countDown()一直减state,减到0后一次性唤醒所有线程
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2020-11-24 22:57:39
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class StudyCountDownLatch {

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
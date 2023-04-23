package com.wy.study;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * 锁支持,主要作用类似于线程的suspend()(挂起)
 * 
 * 与 Object的 wait & notify 相比:
 * 
 * <pre>
 * 1.wait,notify 和notifyAll 必须配合 Object Monitor 一起使用,而 park,unpark 不必
 * 2.park & unpark 是以线程为单位来[阻塞]和[唤醒]线程,而notify 只能随机唤醒一个等待线程,notifyAll是唤醒所有等待线程,就不那么[精确]
 * 3.park & unpark 可以先unpark,而 wait & notify 不能先notify
 * </pre>
 * 
 * park & unpark原理
 * 
 * <pre>
 * 每个线程都有自己的一个Parker 对象,由三部分组成_counter,_cond和_mutex,如下类似:
 * 1.线程就像一个旅人,Parker 就像他随身携带的背包,条件变量就好比背包中的帐篷,_counter 就好比背包中的备用干粮(0为耗尽,1为充足)
 * 2.调用park就是要看需不需要停下来歇息:如果备用干粮耗尽,那么钻进帐篷歇息;如果备用干粮充足,那么不需停留,继续前进
 * 3.调用unpark,就好比令干粮充足:如果这时线程还在帐篷,就唤醒让他继续前进;
 * 		如果这时线程还在运行,那么下次他调用 park 时,仅是消耗掉备用干粮,不需停留继续前进;
 * 		因为背包空间有限,多次调用unpark 仅会补充一份备用干粮
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2020-12-09 20:43:17
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class StudyLockSupport {

	static Object u = new Object();

	static Inner t1 = new Inner("t1");

	static Inner t2 = new Inner("t2");

	public static void main(String[] args) {
		t1.start();
		try {
			TimeUnit.SECONDS.sleep(2);
			t2.start();
			// 解锁
			LockSupport.unpark(t1);
			LockSupport.unpark(t2);
			t1.join();
			t2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static class Inner extends Thread {

		public Inner(String name) {
			super.setName(name);
		}

		@Override
		public void run() {
			synchronized (u) {
				System.out.println("in " + getName());
				// 阻塞
				LockSupport.park();
			}
		}
	}
}
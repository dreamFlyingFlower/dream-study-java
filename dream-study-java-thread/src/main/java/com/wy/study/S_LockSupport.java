package com.wy.study;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * 锁支持,主要作用类似于线程的suspend()(挂起)
 * 
 * @author ParadiseWY
 * @date 2020-12-09 20:43:17
 * @git {@link https://github.com/mygodness100}
 */
public class S_LockSupport {

	static Object u = new Object();

	static Inner t1 = new Inner("t1");

	static Inner t2 = new Inner("t2");

	public static void main(String[] args) {
		t1.start();
		try {
			TimeUnit.SECONDS.sleep(2);
			t2.start();
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
				LockSupport.park();
			}
		}
	}
}
package com.wy.study;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ReadWriteLock:读写锁,写写和读写的时候需要互斥,读读的时候不需要互斥
 * 
 * @author ParadiseWY
 * @date 2020-11-24 23:33:16
 * @git {@link https://github.com/mygodness100}
 */
public class S_ReadWriteLock {

	public static void main(String[] args) {
		ReadWriteLockDemo rw = new ReadWriteLockDemo();

		new Thread(new Runnable() {

			@Override
			public void run() {
				rw.set((int) (Math.random() * 101));
			}
		}, "Write:").start();
		for (int i = 0; i < 100; i++) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					rw.get();
				}
			}).start();
		}
	}
}

class ReadWriteLockDemo {

	private int number = 0;

	private ReadWriteLock lock = new ReentrantReadWriteLock();

	// 读
	public void get() {
		lock.readLock().lock(); // 上锁
		try {
			System.out.println(Thread.currentThread().getName() + ":" + number);
		} finally {
			lock.readLock().unlock(); // 释放锁
		}
	}

	// 写
	public void set(int number) {
		lock.writeLock().lock();
		try {
			System.out.println(Thread.currentThread().getName());
			this.number = number;
		} finally {
			lock.writeLock().unlock();
		}
	}
}
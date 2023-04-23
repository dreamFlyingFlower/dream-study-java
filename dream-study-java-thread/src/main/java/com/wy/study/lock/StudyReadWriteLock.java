package com.wy.study.lock;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ReadWriteLock:读写锁.写写和读写的时候需要互斥,读读的时候不需要互斥
 * 
 * <pre>
 * 锁降级:写锁降级为读锁.在写锁木有释放的时候,获取到读锁,再释放写锁
 * 锁升级:读锁升级为写锁.在读锁米有释放的时候,获取到写锁,再释放读锁
 * 
 * {@link ReentrantReadWriteLock}:ReadWriteLock的实现类,当创建该对象实例时,可以设置锁是否为公平锁,默认为非公平锁.
 * 		该锁是可重入锁,Reader角色的线程可以获取用于写入的锁,Writer角色的线程可以获取用于读取的锁.
 * 		该锁可降级,可以将用于写入的锁降级为用于读取的锁
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2020-11-24 23:33:16
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class StudyReadWriteLock {

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
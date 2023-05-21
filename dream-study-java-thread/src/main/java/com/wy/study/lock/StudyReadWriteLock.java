package com.wy.study.lock;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ReadWriteLock:读写锁.写写和读写的时候需要互斥,读读的时候不需要互斥
 * 
 * <pre>
 * 锁降级:写锁降级为读锁.在写锁木有释放的时候,获取到读锁,再释放写锁
 * 锁升级:读锁升级为写锁.在读锁木有释放的时候,获取到写锁,再释放读锁
 * 
 * {@link ReentrantReadWriteLock}:ReadWriteLock的实现类,当创建该对象实例时,可以设置锁是否为公平锁,默认为非公平锁.
 * 		该锁是可重入锁,Reader角色的线程可以获取用于写入的锁,Writer角色的线程可以获取用于读取的锁.
 * 		该锁可降级,可以将用于写入的锁降级为用于读取的锁,但是不能将读锁升级为写锁
 * </pre>
 * 
 * {@link ReentrantReadWriteLock.ReadLock},{@link ReentrantReadWriteLock.WriteLock}:读锁,血锁,共用同一个{@link ReentrantReadWriteLock.Sync}对象
 * 同互斥锁一样,读写锁也是用state变量来表示锁的状态,只是state变量在这里的含义和互斥锁完全不同
 * 
 * 在{@link ReentrantReadWriteLock.Sync}中,对state变量进行了重新定义,将其拆成两半,低16位,用来记录写锁,高16位记录读锁
 * 不管是读锁还是血锁,都是可重入锁.读锁的大小表示有多少线程同时拿到了该锁;写锁表示重入次数
 * 之所以要把一个int拆成两半,而不是用两个int表示读锁和写锁,是因为无法用一次CAS同时操作两个int变量
 * 当state=0时,说明既没有线程持有读锁,也没有线程持有写锁;当state != 0时,要么有线程持有读锁,要么有线程持有写锁,两者不能同时成立,因为读写互斥.
 * 再进一步通过sharedCount(state)和exclusiveCount(state)判断到底是读线程还是写线程持有了该锁
 * 
 * {@link ReentrantReadWriteLock.Sync#tryReleaseShared}:因为读锁是共享锁,多个线程会同时持有读锁,所以对读锁的释放不能直接减1,
 * 		而是需要通过一个for循环+CAS操作不断重试,这是tryReleaseShared和tryRelease的根本差异所在
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
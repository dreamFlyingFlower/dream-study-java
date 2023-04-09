package com.wy.study;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock类似于synchronized,是利用object在对象上加锁,其实是在一个对象上加上标记信息,利用标记代表锁信息
 * 
 * 尝试锁:当锁被其他方法或代码块使用时,可使用尝试锁获取锁,尝试锁可以是阻塞的,也可以是非阻塞的
 * 
 * 阻塞状态:普通阻塞,等待队列,锁池队列
 * 
 * <pre>
 * 普通阻塞:如sleep,可以被打断,调用Thread.interrupt()方法可打断,会抛出异常
 * 等待队列:wait方法,只能由notify或notifyall方法唤醒,无法被打断
 * 锁池队列:无法获取锁标记,不是所有的锁池队列都可被打断
 * 使用ReentrantLock的lock()获取锁标记时,如果需要阻塞等待,无法被打断
 * 使用ReentrantLock的lockInterruptibly()获取锁标记时,如果需要阻塞等待,可以被打断
 * </pre>
 * 
 * @author DreamFlyingFlower
 * @date 2019-05-09 19:41:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class S_ReentrantLock {

	// 非公平锁,多 线程竞争机制
	ReentrantLock lock = new ReentrantLock();

	// 公平锁.当多个线程同时等待的时候,根据等待的先后顺序.当CPU分配时间片的时候,先等待的先拿锁
	ReentrantLock lock1 = new ReentrantLock(true);

	void m1() {
		int i = 0;
		// lock()加锁的时候也可能会出现异常.加锁之后的代码都是锁定的,直到调用unlock()
		try {
			lock.lock();
			while (i < 100) {
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 解锁
			lock.unlock();
		}
	}

	void m2() {
		boolean tryLock = false;
		try {
			// 尝试锁.如果有锁,无法获取锁标记,返回false;如果没有锁,则可以获取锁,返回true.无参锁不阻塞
			tryLock = lock.tryLock();
			// 加参数时,表示在某段时间内获取锁,若获取到了,则返回true.若超时,直接返回false,此时线程是阻塞的
			tryLock = lock.tryLock(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (tryLock) {
			System.out.println("有锁,同步的");
		} else {
			System.out.println("无锁,非同步的");
		}
		// 只有当有锁的时候才能调用解锁
		if (tryLock) {
			lock.unlock();
		}
	}

	/**
	 * m3和m4演示lock的打断机制
	 */
	void m3() {
		try {
			lock.lock();
			System.out.println("加锁了,别尝试了");
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	void m4() {
		try {
			// 阻塞等待锁标记,可尝试打断
			// 当m3方法运行时,若m3方法没有结束,那么m4方法就是阻塞状态
			// 若运行m4方法的线程调用interrupt,则会打断m4的阻塞状态,进而会触发lockInterruptibly()
			// 此时是非正常方式结束线程的阻塞状态,会抛出异常;若是正常的获取锁,那么就不会触发lockInterruptibly()
			lock.lockInterruptibly();
			System.out.println("打断1");
		} catch (InterruptedException e) {
			System.out.println("被打断了,呵呵");
			e.printStackTrace();
		} finally {
			// 判断当前锁是否上锁,若上锁该值为true,否则未上锁就解锁会抛异常
			if (lock.isHeldByCurrentThread()) {
				lock.unlock();
			}
		}
	}

	public static void main(String[] args) {
		S_ReentrantLock li = new S_ReentrantLock();
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				li.m3();
			}
		});
		t.start();
		Thread t1 = new Thread(new Runnable() {

			@Override
			public void run() {
				li.m4();
			}
		});
		t1.start();
		// 打断线程休眠,非正常结束阻塞状态会抛出异常
		// 当t1被打断的时候,m4方法中的等待状态就被打断,被打断就会抛出异常
		t1.interrupt();
	}
}
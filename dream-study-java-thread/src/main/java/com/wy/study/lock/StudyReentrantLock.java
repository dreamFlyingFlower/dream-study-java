package com.wy.study.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.ReentrantLock;

/**
 * {@link ReentrantLock}类似于synchronized,是利用object在对象上加锁,其实是在一个对象上加上标记信息,利用标记代表锁信息
 * {@link ReentrantLock.Sync}:一个抽象类,继承了AQS,它有两个子类FairSync与NonfairSync,分别对应公平锁和非公平锁
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
 * {@link ReentrantLock.FairSync#lock}:公平锁,不可中断.加锁时会先调用{@link AbstractQueuedSynchronizer#acquire(int)}
 * {@link ReentrantLock.NonfairSync#lock}:非公平锁,不可中断.加锁时会先直接CAS加锁,成功则设置当前锁持有的线程,失败则同公平锁
 * {@link AbstractQueuedSynchronizer#acquire(int)}:获取锁的入口,其中的tryAcquire()由子类重写,acquireQueued()不可重写
 * {@link AbstractQueuedSynchronizer#addWaiter}:为当前线程生成一个Node,然后把Node放入双向链表的尾部.只是把Thread放入队列,线程本身并未阻塞
 * {@link AbstractQueuedSynchronizer#acquireQueued}:无限阻塞,即使其他线程调用interrupt()也不能将其唤醒.除非其他线程释放了锁,且该线程拿到了锁,才会释放.
 * 		该方法有一个返回值,表示被阻塞期间是否有线程向它发送过中断信号.如果有,返回tru;否则,返回false
 * {@link AbstractQueuedSynchronizer#selfInterrupt}:acquireQueued()返回true时调用,自己给自己发送中断信号,即将自己的中断标志位设为true.
 * 		之所以要这么做,是因为自己在阻塞期间,收到其他线程中断信号没有及时响应,现在要进行补偿.
 * {@link AbstractQueuedSynchronizer#parkAndCheckInterrupt}:阻塞和响应中断.如果其他线程调用了LockSupport.unpark()或Thread.interrupt()会中断阻塞.
 * 		lock()不能响应中断,但LockSupport.park()会响应中断.也正因为park()可能被中断唤醒,acquireQueued()才写了一个for死循环.
 * 		唤醒之后,如果发现自己排在队列头部,就去拿锁;如果拿不到锁,则再次自己阻塞自己.不断重复此过程,直到拿到锁.
 * 		被唤醒之后,通过Thread.interrupted()来判断是否被中断唤醒.如果是调用了unpark(),返回false;如果调用了interrupt(),则返回true
 * {@link ReentrantLock.Sync#tryRelease}:释放锁是相同的方法,该方法只有由获取锁的线程调用,所以不用加锁
 * {@link AbstractQueuedSynchronizer#unparkSuccessor}:唤醒下一个线程.
 * 		先判断当前线程是否为抢占线程(非公平锁抢占),那么该线程没有下一个节点,因为抢占线程不需要入队,则从链表尾部开始向前查看线程的等待状态
 * 		如果是下一个Node存在,则直接唤醒下一个Node所在的线程
 * 
 * {@link ReentrantLock#lockInterruptibly}:可中断锁
 * {@link AbstractQueuedSynchronizer#doAcquireInterruptibly}:当parkAndCheckInterrupt()返回true时,说明有其他线程发送中断信号,
 * 		直接抛出InterruptedException,跳出for循环
 * 
 * {@link ReentrantLock#tryLock}:基于非公平锁的tryAcquire(),对state进行CAS操作,如果成功就拿到锁;如果不成功则直接返回false,也不阻塞
 *  
 * @author DreamFlyingFlower
 * @date 2019-05-09 19:41:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class StudyReentrantLock {

	/** 默认非公平锁,多线程竞争机制 */
	ReentrantLock lock = new ReentrantLock();

	/** 公平锁.当多个线程同时等待的时候,根据等待的先后顺序.当CPU分配时间片的时候,先等待的先拿锁 */
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
			// 此时是非正常方式结束线程的阻塞状态,会抛出异常;若没有竞争,则是正常的获取锁,那么就不会触发lockInterruptibly()
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
		StudyReentrantLock li = new StudyReentrantLock();
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
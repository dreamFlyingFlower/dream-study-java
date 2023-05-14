package com.wy.study.lock;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 生产者消费者演示条件重入锁
 * 
 * {@link Condition}:为lock增加条件,当条件满足时,做什么事情,如加锁,解锁,等待唤醒<br>
 * Condition中的await(),signal(),signalAll()刚好对应await(),notify(),notifyAll(),在某些时候可以替代他们
 * 
 * 由于Condition必须和Lock一起使用,所以Condition的实现也是Lock的一部分.
 * 读写锁中的 ReadLock 是不支持 Condition 的,读写锁的写锁和互斥锁都支持Condition.
 * 每一个Condition对象上面,都阻塞了多个线程,因此,在ConditionObject内部也有一个双向链表组成的队列
 *
 * @author 飞花梦影
 * @date 2019-05-10 09:42:16
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class StudyCondition {

	Lock lock = new ReentrantLock();

	final int COUNT = 10;

	int count = 0;

	Condition prod = lock.newCondition();

	Condition cons = lock.newCondition();

	LinkedList<String> list = new LinkedList<>();

	public static void main(String[] args) {
		StudyCondition s = new StudyCondition();
		for (int i = 0; i < 10; i++) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					for (int j = 0; j < 5; j++) {
						System.out.println(s.m2());
					}
				}
			}, "consumer" + i).start();
		}
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		for (int i = 0; i < 2; i++) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					for (int j = 0; j < 25; j++) {
						s.m1("container value " + j);
					}
				}
			}, "producer" + i).start();
		}
	}

	/**
	 * 生产者
	 */
	void m1(String s) {
		try {
			lock.lock();
			// 不能使用if,多线程下会产生虚假唤醒.即多线程都唤醒后,会连续调用count++方法,与初衷不符
			while (list.size() == COUNT) {
				// 进入等待队列.释放锁标记,借助条件,进入的等待队列
				prod.await();
			}
			list.add(s + Thread.currentThread().getName());
			// 唤醒自己的线程
			// prod.signal();
			count++;
			// 借助条件,唤醒所有等待着,必须在释放锁之前调用
			cons.signalAll();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 消费者
	 */
	String m2() {
		String res = null;
		try {
			lock.lock();
			while (list.size() == 0) {
				// 借助条件,消费者进入等待
				cons.await();
			}
			count--;
			res = list.removeFirst();
			// 借助条件,唤醒所有生产者
			prod.signalAll();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
		return res;
	}
}
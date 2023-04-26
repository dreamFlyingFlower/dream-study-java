package com.wy.study;

import java.util.concurrent.TimeUnit;

/**
 * volatile,多线程之间变量可见性以及禁止指令重排
 * 
 * <pre>
 * 线程可见性:多线程之间的变量可见性,但不保证原子性,必须是同一把锁的线程之间才能使用,可以在某些地方替代synchronized
 * 指令重排:在JVM执行字节码时可能会对执行顺序进行调整,volatile禁止JVM调整执行顺序
 * </pre>
 * 
 * MESI:CPU缓存一致性,用于保证多个CPU缓存之间缓存共享数据的一致性,是一种协议
 * 
 * 程序启动时,类的字节码会加载到线程工作空间中,而cpu会将这些信息从工作空间加载到cpu缓存中,并且首先从cpu缓存中读数据.
 * 当其他线程在其工作空间中对某个变量进行修改时,并不会立即将结果回写到内存中,而是继续在cpu缓存中停留一段时间.
 * 使用volatile修饰后,当变量被其他线程修改时,MESI协议将该变量的地址无效,同时将新的数据写到CPU缓存中.
 * 其他用到该变量的线程需要重新从内存中读取,保证数据的一致性
 * 
 * 对一个变量的写操作,在多线程下先行发生于后面对这个变量的读操作
 *
 * @author 飞花梦影
 * @date 2019-05-08 21:40:37
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class StudyVolatile {

	private volatile boolean flag = true;

	public static void main(String[] args) {
		StudyVolatile volatile1 = new StudyVolatile();
		new Thread(new Runnable() {

			@Override
			public void run() {
				volatile1.m1();
			}
		}).start();

		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		volatile1.flag = false;
	}

	void m1() {
		System.out.println("start");
		while (flag) {

		}
		System.out.println("end");
	}
}
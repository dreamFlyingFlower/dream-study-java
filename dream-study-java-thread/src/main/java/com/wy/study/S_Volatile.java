package com.wy.study;

import java.util.concurrent.TimeUnit;

/**
 * volatile:多线程之间的变量可见性,但不保证原子性,必须是同一把锁的线程之间才能使用,可以在某些地方替代synchronized
 * 
 * 程序启动时,类的字节码会加载到内存中,而cpu会将这些信息从内存加载到cpu缓存中,并且首先从cpu缓存中读数据.
 * 当cpu对类成员变量做出修改时,并不会立即将结果回写到内存中,而是继续在cpu缓存中停留一段时间.
 * 使用volatile修饰后,cpu会立即将缓存中的数据回写到内存中.
 * 回写之后会使cpu中其他缓存了该内存地址的数据失效,导致这些数据需要重新从内存中读取,保证数据的一致性
 *
 * @author ParadiseWY
 * @date 2019-05-08 21:40:37
 * @git {@link https://github.com/mygodness100}
 */
public class S_Volatile {

	private volatile boolean flag = true;

	public static void main(String[] args) {
		S_Volatile volatile1 = new S_Volatile();
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
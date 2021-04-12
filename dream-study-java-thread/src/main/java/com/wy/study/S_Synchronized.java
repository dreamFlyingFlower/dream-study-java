package com.wy.study;

import java.util.concurrent.TimeUnit;

/**
 * 线程安全->synchronized锁,同一时间只有一个线程能拿到锁
 * 
 * synchronized:修饰普通方法,默认是锁对象是类的实例;修饰静态方法,默认锁对象是类的字节码;代码块则根据锁对象上锁
 * 
 * 锁存在于对象头中,对象头中包含Mark Word,Class Metadata Address等,Mark Word中包含线程id,Epoch,
 * 对象的分代年龄信息,锁信息
 * 
 * 偏向锁:每次加锁和释放锁会浪费资源,如果同一个线程不停的访问一个加锁的资源,那么根据对象头中的信息,
 * 第2次及以后的访问都将不需要加锁和释放锁,只有等到不同的线程访问该资源时,才释放锁和重新加锁
 * 
 * 轻量级锁:多个线程都进入到加锁的资源中,但是只有一个线程能执行代码,这个是根据对象头中的信息决定.
 * 没有执行代码的线程就会发生自旋(类似于一个while循环,但是什么都不做),不停的请求锁资源,直到拿到锁资源
 * 
 * 重量级锁:若偏向锁和轻量级锁都无法满足加锁资源的利用,就会升级成重量级锁,此时所有的线程中只有一个线程能进入锁
 * 
 * 可重入锁:多个资源的加锁对象是相同的,当一个线程去访问这些资源时,不会发生死锁<br>
 * 可重入锁1:子类重写父类的同步方法,子类方法也是同步方法.当子类对象调用子类同步方法时,<br>
 * 子类同步方法中又调用了父类的同步方法,也可看作是重入锁<br>
 * 异常:当同步锁中发生异常时,会自动释放锁资源,不会影响其他线程的执行
 * 
 * @author ParadiseWY
 * @date 2020-10-06 18:39:02
 * @git {@link https://github.com/mygodness100}
 */
public class S_Synchronized {

	Object o1 = new Object();

	Object o2 = new Object();

	public static void main(String[] args) {
		S_Synchronized t1 = new S_Synchronized();
		new Thread(() -> {
			t1.m1();
		}).start();
		new Thread(() -> {
			t1.m3("直接调用m3方法...");
		}).start();

		// 可能会发生死锁,若不发生死锁,可调大等待时间
		new Thread(() -> {
			t1.m4();
		}).start();
		new Thread(() -> {
			t1.m5();
		}).start();

		System.out.println("main线程结束了...");
	}

	public void test() {
		// 锁的对象不同,锁的使用范围就不一样.锁可以是任意对象
		synchronized (S_Synchronized.class) {
			System.out.println("代码块锁");
		}
	}

	// 锁实例对象
	synchronized void m1() {
		try {
			System.out.println("m1方法执行了...");
			// 等同于Thread.sleep方法,但是推荐使用TimeUnit方法
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		m2();
	}

	// 锁实例对象
	synchronized void m2() {
		try {
			System.out.println("m2方法执行了...");
			TimeUnit.SECONDS.sleep(2);
			m3("m2调用m3方法...");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	void m3(String label) {
		synchronized (S_Synchronized.class) {
			try {
				TimeUnit.SECONDS.sleep(6);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("m3方法执行了..." + label);
		}
	}

	// 死锁,同步块中还有同步块,同一个对象拿锁的时候都需要拿对方的锁,结果锁只有一把,都拿不到
	void m4() {
		synchronized (o1) {
			try {
				TimeUnit.MILLISECONDS.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			synchronized (o2) {
				System.out.println("m4方法执行了...");
			}
		}
	}

	void m5() {
		synchronized (o2) {
			try {
				TimeUnit.MILLISECONDS.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			synchronized (o1) {
				System.out.println("m5方法执行了...");
			}
		}
	}
}
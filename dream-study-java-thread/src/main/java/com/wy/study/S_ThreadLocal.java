package com.wy.study;

import java.util.concurrent.TimeUnit;

/**
 * ThreadLocal是线程安全的,底层是 ThreadLocal.ThreadLocalMap ,相当于一个map,而key则是相当于该线程的地址
 * 
 * <pre>
 * ThreadLocal在高并发中,若是频繁的使用,可能会造成内存溢出.要注重删除ThreadLocal中的数据.
 * ThreadLocal在调用set()的时候才会与线程绑定,并不是new的时候就绑定
 * 
 * 在操作系统中,进程和线程的数量是有上限的,确定进程和线程的唯一条件就是进程或线程id.
 * 操作系统在回收进程或线程的时候,不是一定会kill进程或线程,在繁忙的时候,将重复使用进程或线程,
 * 只会做清空进程和线程栈数据的操作,但是会保留堆中数据,而ThreadLocal在堆中.当ThreadLocal中有数据时,
 * 而恰好CPU繁忙,则会清空线程的栈数据,保留线程.当该线程被其他对象使用,并从ThreadLocal中取数据时,
 * 就会取到该线程在被清空栈数据之前的数据,造成内存泄漏
 * </pre>
 * 
 * 强,弱,软,虚引用:
 * 
 * <pre>
 * 强引用:当内存不足时,JVM开始进行 GC(垃圾回收),对于强引用对象,就算是出现了OOM 也不会对该对象进行回收
 * 软引用:当内存充足时,不会被回收;当内存不足时,它会被回收,软引用通常用在对内存敏感的.程序中,比如高速缓存就用到软引用,内存够用时就保留,不够时就回收
 * 弱引用:需要用到java.langref.WeakReference来实现.只要有垃圾回收,不管JVM 的内存空间够不够用,都会回收该对象占用的内存空间,ThrealLocal就是弱引用
 * 虚引用:需要用到java.langref.Phantomreference来实现.虚引用就是形同虚设,与其它几种引用不同,虚引用并不会决定对象的声明周期
 * </pre>
 *
 * @author 飞花梦影
 * @date 2019-10-06 14:39:56
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class S_ThreadLocal {

	volatile static String name = "test";

	static ThreadLocal<String> t1 = new ThreadLocal<>();

	public static void main(String[] args) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					TimeUnit.SECONDS.sleep(2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(name);// fdsfdse
				System.out.println(t1.get());// null
			}
		}).start();
		new Thread(new Runnable() {

			@Override
			public void run() {
				name = "fdsfdse";
				t1.set("托尔斯泰2");
				t1.set("fdsfewrewrew");
				// fdsfewrewrew
				System.out.println(t1.get());
			}
		}).start();
	}
}
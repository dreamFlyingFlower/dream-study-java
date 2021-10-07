package com.wy.jdk5;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * JDK1.5新特性
 * 
 * <pre>
 * 1.基本数据类型自动拆装箱.自动拆装的Integer类有个内部静态类IntegerCache,
 * 该静态类会对byte范围被的数字直接从常量池获取,但是超过该范围的将采用new的方式生成
 * 2.枚举类,不能继承其他类,枚举已经继承了{@link Enum}类
 * 3.ReentranLock用来替换synchronized的同步锁
 * 4.ThreadGroup线程组,对同一批线程进行管理,同时对里面的线程进行管理
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2018-10-05 21:56:57
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class JDK5 {

	public static void main(String[] args) {
		testBox();
	}

	public static void testBox() {
		System.out.println(int.class == Integer.class);
		Integer a = 1;
		Integer b = 1;
		System.out.println(a == b);// true
		Integer c = 130;
		Integer d = 130;
		int e = 130;
		System.out.println(c == d);// false
		System.out.println(e == c);// true
		System.out.println(e == d);// true
	}

	public static void testReentratLock() {
		// 新建一个ReentranLock对象,在lock和unlock方法之间的代码块就相当于synchronized的同步锁
		ReentrantLock r = new ReentrantLock();
		// Condition必须依赖ReentrantLock对象,只有使用了lock之后才可以使用lock方法
		Condition con1 = r.newCondition();
		ReentrantLock r1 = new ReentrantLock();
		Condition con2 = r1.newCondition();
		r.lock();
		try {
			// 当满足某种条件的时候r代表的线程等待
			con1.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// do something
		// 当满足某种条件的时候唤醒r1的线程
		con2.signal();
		r.unlock();
	}
}
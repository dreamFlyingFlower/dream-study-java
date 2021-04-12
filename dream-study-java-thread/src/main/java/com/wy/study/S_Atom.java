package com.wy.study;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

import com.wy.model.User;

/**
 * 原子操作,线程安全的类
 * 
 * CAS:jvm底层的线程安全操作,即{@link AtomicInteger#compareAndSet}方法.<br>
 * ->AtomicInteger的valueOffset的值通过反射从内存中读取,这一步必须是线程安全的,否则后面的比较就没有意义<br>
 * ->在调用compareAndSet()之时利用本身的get()再次获得value的值<br>
 * ->valueOffset和get()获得的value进行比较,若是相等,则进行后面的操作,返回原值;不符合预期则重复操作
 *
 * @author ParadiseWY
 * @date 2019-05-06 22:41:53
 * @git {@link https://github.com/mygodness100}
 */
public class S_Atom {

	/** 原子整型,线程安全 */
	private static AtomicInteger atom = new AtomicInteger();

	/** 数组类原子操作 */
	AtomicIntegerArray array = new AtomicIntegerArray(new int[5]);

	/** 对引用类型的原子操作,是对引用类型的实例操作,而不是对实例中的属性进行原子操作 */
	AtomicReference<User> reference = new AtomicReference<>();

	/** 相比于AtomicReference多了一个类似时间戳的唯一标识,保证在修改对象的时候中间不会进行其他操作 */
	AtomicStampedReference<User> stampedReference = new AtomicStampedReference<User>(new User(), 1);

	/** 对对象实例中的属性进行原子操作,被操作的类中的属性必须是public,且是volatile修饰才行 */
	AtomicIntegerFieldUpdater<User> updater = AtomicIntegerFieldUpdater.newUpdater(User.class, "id");

	/** 参数表示调用一次countDown方法,就可以继续执行,否则就在调用await的地方一直等待 */
	private static CountDownLatch latch = new CountDownLatch(1);

	public static void main(String[] args) {
		for (int i = 0; i < 20; i++) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						System.out.println(atom.incrementAndGet());
						latch.await();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
		latch.countDown();
	}
}
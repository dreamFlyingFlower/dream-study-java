package com.wy.study;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.concurrent.atomic.AtomicStampedReference;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.concurrent.atomic.LongAdder;

import com.wy.model.User;

/**
 * 原子操作,线程安全的类
 * 
 * CAS:jvm底层的线程安全操作,即{@link AtomicInteger#compareAndSet}方法.<br>
 * ->AtomicInteger的valueOffset的值通过反射从内存中读取,这一步必须是线程安全的,否则后面的比较就没有意义<br>
 * ->在调用compareAndSet()之时利用本身的get()再次获得value的值<br>
 * ->valueOffset和get()获得的value进行比较,若是相等,则进行后面的操作,返回原值;不符合预期则重复操作
 * 
 * double的CAS操作
 * 
 * <pre>
 * 方式1:需要用到{@link Double#longBitsToDouble(long)}和{@link Double#doubleToRawLongBits(double)}进行转换
 * 方式2:使用 {@link DoubleAdder}
 * 方式3:直接使用{@link sun.misc.Unsafe#compareAndSetDouble()}
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2019-05-06 22:41:53
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class StudyAtomic {

	/** 原子整型,线程安全 */
	static AtomicInteger atomicInteger = new AtomicInteger();

	/** 数组类原子操作 */
	static AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(new int[5]);

	static AtomicLongArray atomicLongArray = new AtomicLongArray(new long[5]);

	static AtomicReferenceArray<User> atomicReferenceArray = new AtomicReferenceArray<>(new User[5]);

	/** 对引用类型的原子操作,是对引用类型的实例操作,而不是对实例中的属性进行原子操作 */
	static AtomicReference<User> atomicReference = new AtomicReference<>();

	/** 相比于AtomicReference多了一个类似时间戳的唯一标识,保证在修改对象的时候中间不会进行其他操作 */
	static AtomicStampedReference<User> atomicStampedReference = new AtomicStampedReference<>(new User(), 1);

	/** 解决Integer的ABA问题 */
	static AtomicStampedReference<Integer> atomicStampedReference1 = new AtomicStampedReference<>(1, 1);

	/** 功能和AtomicStampedReference类似,但是版本号是boolean,而不是int类型,不能完全避免ABA问题,只是降低了ABA问题 */
	static AtomicMarkableReference<User> atomicMarkableReference = new AtomicMarkableReference<>(new User(), false);

	/** 对对象实例中的属性进行原子操作,被操作的类中的属性必须是int,不能是Integer,且是volatile修饰才行 */
	static AtomicIntegerFieldUpdater<User> atomicIntegerFieldUpdater =
			AtomicIntegerFieldUpdater.newUpdater(User.class, "id");

	static AtomicLongFieldUpdater<User> atomicLongFieldUpdater = AtomicLongFieldUpdater.newUpdater(User.class, "id");

	static AtomicReferenceFieldUpdater<User, User> atomicReferenceFieldUpdater =
			AtomicReferenceFieldUpdater.newUpdater(User.class, User.class, "id");

	/** 针对Long型的原子操作LongAdder和LongAccumulator,继承自Striped64.在高并发下做运算比AtomicLong更快 */
	static LongAdder longAdder = new LongAdder();

	/** 针对Double类型的DoubleAdder,DoubleAccumulator,继承自Striped64,主要用于计算 */
	static DoubleAdder doubleAdder = new DoubleAdder();

	public static void main(String[] args) {
		// 参数:User旧值;User新值;版本旧值;版本新值
		atomicStampedReference.compareAndSet(null, null, 0, 0);
	}
}
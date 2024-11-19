package com.wy.study;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicInteger;

import com.wy.model.User;

import dream.flying.flower.reflect.ReflectHelper;
import sun.misc.Unsafe;

/**
 * {@link sun.misc.Unsafe}:并非是指该类不安全,而是指该类会操作内存,随意使用可能会造成内存问题.该类是CAS的基础
 * 
 * CAS:Compare And Swap,比较替换,操作包含三个操作数-内存位置(V),预期原值(A)和新值(B)
 * 如果内存位置的值与预期原值相配,那么处理器会自动将该位置值更新为新值;否则,处理器不做任何操作.
 * CAS存在三大问题:ABA问题,循环时间长开销大,以及只能保证一个共享变量的原子操作
 * 案例可见{@link AtomicInteger#compareAndSet()}
 * 
 * {@link sun.misc.Unsafe#compareAndSetInt}:参数依次为:要修改哪个对象的属性值;该对象属性在内存的偏移量;期望值;要设置为的目标值
 * {@link sun.misc.Unsafe#objectFieldOffset}:找到指定类中value属性所在的内存偏移量
 *
 * @author 飞花梦影
 * @date 2023-04-22 16:30:08
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class StudyUnsafe {

	public static void main(String[] args) {
		try {
			// 通过类中的私有单例对象获取,该字段可通过源码查看
			Field field = Unsafe.class.getDeclaredField("theUnsafe");
			ReflectHelper.fixAccessible(field);
			Unsafe unsafe = (Unsafe) field.get(null);
			System.out.println(unsafe);

			// 通过构造函数获取
			Constructor<Unsafe> constructor = Unsafe.class.getConstructor(new Class<?>[0]);
			Unsafe instance = constructor.newInstance(new Object[0]);
			System.out.println(instance);

			// 直接以静态方法调用获得单例对象
			Unsafe singleton = Unsafe.getUnsafe();
			// 直接操作内存
			singleton.allocateMemory(1L);
			singleton.freeMemory(1L);
			singleton.pageSize();
			// 所有以put开头的API也是直接操作内存
			singleton.putAddress(1L, 1L);
			// 直接生成类实例
			singleton.allocateInstance(User.class);
			// 直接操作类或实例对象
			singleton.objectFieldOffset(field);
			singleton.getInt(1L);
			singleton.getObject(new Object(), 1L);
			// CAS操作
			singleton.compareAndSwapInt(new Object(), 1, 1, 1);
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException | NoSuchFieldException e) {
			e.printStackTrace();
		}
	}
}
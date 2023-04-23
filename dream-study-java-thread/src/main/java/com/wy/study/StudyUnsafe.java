package com.wy.study;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import sun.misc.Unsafe;

/**
 * {@link sun.misc.Unsafe}:并非是指该类是不安全的,而是指该类会操作内存,随意使用可能会造成内存问题.只能通过反射实例化
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
			field.setAccessible(true);
			Unsafe unsafe = (Unsafe) field.get(null);
			System.out.println(unsafe);

			// 通过构造函数获取
			Constructor<Unsafe> constructor = Unsafe.class.getConstructor(new Class<?>[0]);
			Unsafe instance = constructor.newInstance(new Object[0]);
			System.out.println(instance);
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException | NoSuchFieldException e) {
			e.printStackTrace();
		}
	}
}
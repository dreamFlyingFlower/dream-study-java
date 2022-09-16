package com.wy.singleton;

/**
 * 单例模式,饿汉模式.懒汉模式会有线程安全问题,需要加锁,实际最好用饿汉模式
 *
 * @author 飞花梦影
 * @date 2020-10-06 19:59:07
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class SingletonHungry {

	// private static boolean flag = false;

	private SingletonHungry() {
		// // 当利用反射生成多个对象时,直接异常
		// synchronized (SingletonHungry.class) {
		// // 判断flag的值是否是true,如果是true,说明非第一次访问,直接抛一个异常,如果是false的话,说明第一次访问
		// if (flag) {
		// throw new RuntimeException("不能创建多个对象");
		// }
		// // 将flag的值设置为true
		// flag = true;
		// }
	}

	private static class SingletonInner {

		public static final SingletonHungry INSTANCE = new SingletonHungry();
	}

	public static SingletonHungry getInstance() {
		return SingletonInner.INSTANCE;
	}

	/**
	 * 破解序列化,反序列化时单例被修改的问题
	 * 
	 * @return 单例
	 */
	public Object readResolve() {
		return SingletonInner.INSTANCE;
	}
}
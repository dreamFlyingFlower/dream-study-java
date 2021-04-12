package com.wy.design.singleton;

/**
 * 单例模式,饿汉模式,懒汉模式会有线程安全问题,需要加锁,实际最好用饿汉模式
 *
 * @author ParadiseWY
 * @date 2020-10-06 19:59:07
 * @git {@link https://github.com/mygodness100}
 */
public class SingletonHungry {

	private SingletonHungry() {
	}

	private static class SingletonInner {

		public static final SingletonHungry instance = new SingletonHungry();
	}

	public static SingletonHungry getInstance() {
		return SingletonInner.instance;
	}
}
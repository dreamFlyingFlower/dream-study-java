package com.wy.singleton;

/**
 * 单例模式,懒汉模式
 * 
 * @apiNote 懒汉模式是需要的时候才初始化,饿汉则是一开始就初始化,volatile是为了防止虚拟机指令重排
 * @author ParadiseWY
 * @date 2020-10-06 19:58:51
 * @git {@link https://github.com/mygodness100}
 */
public class SingletonLazy {

	private SingletonLazy() {
	}

	private static volatile SingletonLazy singleton;

	public static SingletonLazy getInstance() {
		if (singleton == null) {
			synchronized (singleton) {
				if (singleton == null) {
					// 生成实例的时候可能发生虚拟机指令重排
					return new SingletonLazy();
				}
			}
		}
		return singleton;
	}
}
package com.wy.singleton;

/**
 * 单例模式,懒汉模式.懒汉模式是需要的时候才初始化,饿汉则是一开始就初始化,volatile是为了防止虚拟机指令重排
 * 
 * 普通对象初始化过程,如singleton = new SingletonLazy();:
 * 
 * <pre>
 * 1.给singleton分配内存
 * 2.调用 SingletonLazy 的构造函数来初始化成员变量
 * 3.给singleton对象指向分配的内存空间,此时singleton才不为null
 * </pre>
 * 
 * 如果不加volatile字段修饰singleton,则在超高并发下可能会产生132的顺序,在singleton内的变量未完全初始化,导致程序异常
 * 
 * @author 飞花梦影
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
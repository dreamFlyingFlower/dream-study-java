package com.wy.spi;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2024-04-26 16:23:15
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class MySpiContext {

	public static void main(String[] args) {
		ServiceLoader<MySpi> load = ServiceLoader.load(MySpi.class);
		Iterator<MySpi> iterator = load.iterator();
		while (iterator.hasNext()) {
			MySpi next = iterator.next();
			System.out.println(next.getName() + " 准备执行");
			next.handle();
		}
		System.out.println("执行结束");
	}
}
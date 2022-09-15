package com.wy.decorator;

/**
 * 需要装饰的接口
 * 
 * @author 飞花梦影
 * @date 2022-09-15 10:14:36
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public interface Animal {

	public void show();

	default void run() {

	}
}
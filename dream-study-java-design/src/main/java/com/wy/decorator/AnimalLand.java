package com.wy.decorator;

/**
 * 接口扩展,自定义的实现
 * 
 * @author 飞花梦影
 * @date 2022-09-15 10:15:23
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class AnimalLand implements Animal {

	@Override
	public void run() {
		System.out.println("能跑");
	}

	@Override
	public void show() {
		this.run();
	}
}
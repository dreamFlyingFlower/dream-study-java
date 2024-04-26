package com.wy.spi;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2024-04-26 16:19:25
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class MySpiImpl implements MySpi {

	@Override
	public String getName() {
		return "One";
	}

	@Override
	public void handle() {
		System.out.println(getName() + "执行");
	}
}
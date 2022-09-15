package com.wy.bridge;

/**
 * 礼物实现类
 * 
 * @author 飞花梦影
 * @date 2020-09-27 23:38:18
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class CarSend implements GiftSend {

	@Override
	public void price() {
		System.out.println(10000.0);
	}
}
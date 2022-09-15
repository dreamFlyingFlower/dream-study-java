package com.wy.bridge;

/**
 * 车类,需要传递一个礼物的实现类
 * 
 * @author 飞花梦影
 * @date 2020-09-27 23:29:36
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class CarGift extends Gift {

	public CarGift(GiftSend gift) {
		this.giftSend = gift;
	}

	@Override
	public void send() {
		this.giftSend.price();
	}
}
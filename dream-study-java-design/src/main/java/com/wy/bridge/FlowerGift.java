package com.wy.bridge;

/**
 * 花类,需要传递一个礼物的实现类
 * 
 * @author 飞花梦影
 * @date 2021-11-11 15:07:15
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class FlowerGift extends Gift {

	public FlowerGift(GiftSend gift) {
		this.giftSend = gift;
	}

	@Override
	public void price() {
		this.giftSend.price();
	}
}
package com.wy.bridge;

/**
 * 礼物抽象类,需要传递一个礼物的实现类
 * 
 * @author 飞花梦影
 * @date 2021-11-11 15:06:33
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public abstract class Gift implements GiftSend {

	GiftSend giftSend;

	public GiftSend getGiftSend() {
		return giftSend;
	}

	public void setGiftSend(GiftSend giftSend) {
		this.giftSend = giftSend;
	}
}
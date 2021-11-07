package com.wy.bridge;

/**
 * 礼物抽象类,需要传递一个礼物的实现类
 * 
 * @author ParadiseWY
 * @date 2020-09-27 23:28:33
 */
public abstract class Gift {

	GiftSend giftSend;

	public GiftSend getGiftSend() {
		return giftSend;
	}

	public void setGiftSend(GiftSend giftSend) {
		this.giftSend = giftSend;
	}
}
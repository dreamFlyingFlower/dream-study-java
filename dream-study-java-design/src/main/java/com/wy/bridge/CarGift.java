package com.wy.bridge;

/**
 * 车类,需要传递一个礼物的实现类
 * 
 * @author ParadiseWY
 * @date 2020-09-27 23:29:36
 */
public class CarGift extends Gift {

	public CarGift(GiftSend gift) {
		this.giftSend = gift;
	}
}
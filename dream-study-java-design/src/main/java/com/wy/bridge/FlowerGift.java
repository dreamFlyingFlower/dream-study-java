package com.wy.bridge;

/**
 * 花类,需要传递一个礼物的实现类
 * 
 * @author ParadiseWY
 * @date 2020-09-27 23:28:56
 */
public class FlowerGift extends Gift {

	public FlowerGift(GiftImpl gift) {
		this.impl = gift;
	}
}
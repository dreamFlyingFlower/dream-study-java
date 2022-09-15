package com.wy.adapter;

/**
 * SDCard使用类,兼容读写TFCard
 * 
 * 适配器模式:需要用到A中和B中(可能更多)的方法, 但是又不能改A和B,只好写1个或多个中间类或接口来实现使用AB中的方法
 * 
 * @author 飞花梦影
 * @date 2022-09-15 09:41:35
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class Computer {

	public String read(SDCard sdCard) {
		if (sdCard == null) {
			throw new NullPointerException("sd card null");
		}
		return sdCard.read();
	}

	public static void main(String[] args) {
		// 调用继承方式适配器
		Computer context = new Computer();
		SDCard sdCard = new SDCardImpl();
		System.out.println(context.read(sdCard));

		System.out.println("------------");

		// 调用类适配器
		AdapterExtend adapter = new AdapterExtend();
		System.out.println(context.read(adapter));

		System.out.println("------------");

		// 调用对象适配器
		TFCard tfCard = new TFCardImpl();
		AdapterObject adapterObject = new AdapterObject(tfCard);
		System.out.println(context.read(adapterObject));
	}
}
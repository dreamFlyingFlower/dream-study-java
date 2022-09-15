package com.wy.adapter;

/**
 * 适配器类:SDCard兼容TFCard
 * 
 * 委派模式或对象适配器:适配器实现主接口,同时传递需要适配的类或接口为参数;若需要适配多个接口,则需要新建多个对象适配器
 * 
 * @author 飞花梦影
 * @date 2022-09-15 09:35:05
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class AdapterObject implements SDCard {

	private TFCard tfCard;

	public AdapterObject(TFCard tfCard) {
		this.tfCard = tfCard;
	}

	@Override
	public String read() {
		System.out.println("adapter read tf card ");
		return this.tfCard.readTF();
	}

	@Override
	public void write(String msg) {
		System.out.println("adapter write tf card");
		this.tfCard.writeTF(msg);
	}
}
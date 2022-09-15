package com.wy.adapter;

/**
 * 适配器类:SDCard兼容TFCard
 * 
 * 类继承适配器:适配器实现主接口,同时继承需要适配的类
 * 
 * 类适配器模式违背了合成复用原则,若客户类有只一个接口规范的情况下可用,反之不可用
 *
 * @author 飞花梦影
 * @date 2022-09-15 09:35:05
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class AdapterExtend extends TFCardImpl implements SDCard {

	@Override
	public String read() {
		System.out.println("adapter read tf card ");
		return readTF();
	}

	@Override
	public void write(String msg) {
		System.out.println("adapter write tf card");
		writeTF(msg);
	}
}
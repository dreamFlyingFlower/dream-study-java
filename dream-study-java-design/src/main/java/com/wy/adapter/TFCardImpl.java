package com.wy.adapter;

/**
 * TFCard实现类
 *
 * @author 飞花梦影
 * @date 2022-09-15 09:39:21
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class TFCardImpl implements TFCard {

	@Override
	public String readTF() {
		String msg = "tf card read msg : hello word tf card";
		return msg;
	}

	@Override
	public void writeTF(String msg) {
		System.out.println("tf card write a msg : " + msg);
	}
}
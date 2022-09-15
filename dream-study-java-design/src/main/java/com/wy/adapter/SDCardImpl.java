package com.wy.adapter;

/**
 * SDCard实现类
 *
 * @author 飞花梦影
 * @date 2022-09-15 09:36:29
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class SDCardImpl implements SDCard {

	@Override
	public String read() {
		return "SDCard read a msg :hello word SD";
	}

	@Override
	public void write(String msg) {
		System.out.println("SDCard write msg:" + msg);
	}
}
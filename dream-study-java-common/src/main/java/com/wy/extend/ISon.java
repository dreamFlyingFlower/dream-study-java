package com.wy.extend;

/**
 * 子实现
 * 
 * @author 飞花梦影
 * @date 2020-09-29 10:02:09
 */
public class ISon implements IParent {

	int richer = 50;

	public static void name() {
		System.out.println("ison static func");
	}

	@Override
	public void name1() {
		System.out.println("ison default name1");
	}
}
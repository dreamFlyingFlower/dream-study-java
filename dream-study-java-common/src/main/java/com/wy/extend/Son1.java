package com.wy.extend;

/**
 * 子类
 * 
 * @author 飞花梦影
 * @date 2020-09-29 09:45:15
 */
public class Son1 extends Parent1 {

	public int richer = 500;

	public static int boy = 1;

	public Son1() {
		this.name();
		richer = 5000;
	}

	@Override
	public void name() {
		System.out.println("son1.richer:" + richer);
	}

	public static void name1() {
		System.out.println("son1");
	}
}
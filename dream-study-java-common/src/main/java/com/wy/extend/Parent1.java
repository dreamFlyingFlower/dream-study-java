package com.wy.extend;

/**
 * 父类
 * 
 * @author 飞花梦影
 * @date 2020-09-29 09:53:08
 */
public class Parent1 {

	public int richer = 500000;

	public static int boy = 10;

	public Parent1() {
		this.name();
		richer = 1000000;
	}

	public void name() {
		System.out.println("parent1.richer:" + richer);
	}

	public static void name1() {
		System.out.println("parent1");
	}
}

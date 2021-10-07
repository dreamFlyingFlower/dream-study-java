package com.wy.jdk9.example;

/**
 * JDK9接口中定义私有方法
 * 
 * @author 飞花梦影
 * @date 2021-10-06 07:12:57
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface MyInterface01 {

	default void test01() {
		System.out.println(test02());
		System.out.println(test04());
	}

	private String test02() {
		return "test02";
	}

	public static void test03() {
		System.out.println(test04());
	}

	private static String test04() {
		return "test04";
	}
}
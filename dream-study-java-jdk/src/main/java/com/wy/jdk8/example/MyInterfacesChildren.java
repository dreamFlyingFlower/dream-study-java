package com.wy.jdk8.example;

/**
 * 接口中的方法,包括静态方法和默认方法
 * 
 * @author 飞花梦影
 * @date 2019-08-22 21:36:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface MyInterfacesChildren extends MyInterfaces {

	default void test1() {
		System.out.println("JDK8_Interfaces_Children:默认方法1");
	}
	
	static void test2() {
		System.out.println(1111);
	}
	
	public static void main(String[] args) {
		MyInterfaces.test3();
	}
}
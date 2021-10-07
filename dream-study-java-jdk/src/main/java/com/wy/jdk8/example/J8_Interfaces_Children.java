package com.wy.jdk8.example;

/**
 * 接口中的方法,包括静态方法和默认方法
 * 
 * @author ParadiseWY
 * @date 2019-08-22 21:36:23
 * @git {@link https://github.com/mygodness100}
 */
public interface J8_Interfaces_Children extends J8_Interfaces {

	default void test1() {
		System.out.println("JDK8_Interfaces_Children:默认方法1");
	}
}

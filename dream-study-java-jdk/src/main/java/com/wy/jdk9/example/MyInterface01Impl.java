package com.wy.jdk9.example;

/**
 * JDK9接口中定义私有方法,并在default修饰的方法中调用
 * 
 * @author 飞花梦影
 * @date 2021-10-06 07:37:32
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class MyInterface01Impl implements MyInterface01 {

	public static void main(String[] args) {
		MyInterface01Impl impl01 = new MyInterface01Impl();
		impl01.test01();
	}
}
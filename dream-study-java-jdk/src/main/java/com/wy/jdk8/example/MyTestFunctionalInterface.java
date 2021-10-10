package com.wy.jdk8.example;

/**
 * 测试调用函数式接口.当接口是一个函数式接口时,可直接使用lambda表达式,该表达式就是对该接口中的抽象方法的实现
 * 
 * @author 飞花梦影
 * @date 2019-05-08 23:21:42
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class MyTestFunctionalInterface {

	public static void main(String[] args) {
		new MyTestFunctionalInterface(str -> {
			System.out.println(str);
		});
	}

	public MyTestFunctionalInterface(MyFunctionalInterface j8) {
		j8.test("test");
		test("T_J8_FunctionalInterface");
	}

	public void test(String param) {
		System.out.println(param);
	}
}
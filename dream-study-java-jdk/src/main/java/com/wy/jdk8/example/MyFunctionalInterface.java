package com.wy.jdk8.example;

/**
 * 函数式接口,需要在接口上声明@FunctionalInterface.
 * 若该接口中有且只有一个抽象方法,也没有其他default和static方法,不用该注解即可使用lambda表达式进行调用
 * 使用了该注解后,抽象方法有且只能有一个,非函数式接口可有多个抽象方法.default和static方法都可以有多个.
 * 
 * @author 飞花梦影
 * @date 2019-03-07 22:00:41
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@FunctionalInterface
public interface MyFunctionalInterface {

	void test(String param);

	default void syso(String str) {
		System.out.println(str);
	}
}
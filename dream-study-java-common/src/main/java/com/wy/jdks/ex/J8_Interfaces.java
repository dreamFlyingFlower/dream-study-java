package com.wy.jdks.ex;

/**
 * 不管是函数式接口还是普通接口,default方法和static方法都可有多个
 * default方法只能是实现接口的实例调用,静态方法是属于类的,所以只能是接口自身调用自身的静态方法.
 * 
 * @apiNote 当接口A中定义了default方法test1时,若是B继承A,同时也定义了default方法test1.
 *          当实体类C实现B时,调用test1方法,那么执行的是根据实现规则往上查找的最近一个test1方法,即调用B的方法.
 *          当B不继承A时,若是C同时实现了A,B,那么会编译器报错,不可同时有2个同名的default方法
 * @author ParadiseWY
 * @date 2019-08-22 19:23:36
 * @git {@link https://github.com/mygodness100}
 */
public interface J8_Interfaces {

	default void test1() {
		System.out.println("JDK8_Interfaces:默认方法test1");
	}

	default void test5() {
		System.out.println("JDK8_Interfaces:默认方法test5");
	}

	static void test2() {
	}

	static void test3() {
	}
}
package com.wy.jdk8.example;

import java.util.Arrays;
import java.util.List;

/**
 * Lambda核心接口:Predicate,Consumer,Supplier,Function.当核心接口不满足时,可使用其他子类接口,在java.util.function包中
 * 
 * <pre>
 *  {@link java.util.function.Function<T, R>}:函数式接口,实现方法为R apply(T t).
 *		调用该方法时传入一个类型实例参数,但是会返回另外一个类型的实例,这2个类型也可以是相同的
 *  {@link java.util.function.Predicate<T>}:函数式接口,实现方法为boolean test(T t).
 * 		调用该方法时,必须返回一个boolean结果.
 * 		该接口中的其他default方法:and类似&&,or类似||,negate是test方法取反,isEqual判断2个对象是否相同
 *		这些方法都可以链式调用,返回的都是Predicate
 *  {@link java.util.function.Consumer<T>}:函数式接口,实现方法为void accept(T t).
 *		调用该方法时是直接对参数进行操作,无返回值.其他方法类似Predicate
 *  {@link java.util.function.Supplier<T>}:函数式接口,实现方法为T get().
 *		调用该方法时会返回指定类型的对象实例,但是无参
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2021-10-09 20:08:42
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class MyLambda {

	public static void main(String[] args) {
		List<String> list = Arrays.asList("dsfds", "fdsfds", "wefdsfs");
		// Function
		list.stream().map(t -> "fdfd");
		// Predicate
		list.stream().filter(t -> true);
		// Consumer
		list.stream().forEach(t -> System.out.println(1));
		// Supplier
		list.stream().collect(() -> "fdsfds", null, null);
	}
}
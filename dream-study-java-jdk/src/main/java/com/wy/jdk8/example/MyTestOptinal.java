package com.wy.jdk8.example;

import java.util.Optional;

import com.wy.model.Pojo;

/**
 * Optional中主要方法是ofNullable,isPresent,ifPresent,orElse
 * 
 * @author 飞花梦影
 * @date 2021-10-09 20:05:29
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class MyTestOptinal {

	public static void main(String[] args) {
		Pojo t1 = null;
		// 当使用of方法生成optional实例时,若是t1为null,扔会报空指针异常,该方法仍然不安全
		// System.out.println(Optional.of(t1));
		// 生成optional实例,相比于of方法ofNullable方法即使参数为null也不会报错,更安全
		Optional<Pojo> o11 = Optional.ofNullable(t1);
		// 直接生成一个空的optional,但是似乎没什么用,不能往里面set值
		Optional<Object> empty = Optional.empty();
		// 判断optional中的实体类是否有值,有值则返回true,没有则返回false
		System.out.println(empty.isPresent());
		// 根据一个实例对象生成optional对象,之后对生成的optional对象进行操作
		Optional<Object> o1 = Optional.of(new Pojo());
		// optional实例
		System.out.println(o1);
		// optional.empty实例
		System.out.println(o11);
		// true
		System.out.println(o1.isPresent());
		// 当o1中有值时,进行的操作,无返回值
		o1.ifPresent(t -> System.out.println(11));
		// 不输出22
		o11.ifPresent(t -> System.out.println(22));
		// false
		System.out.println(o11.isPresent());
		// 当o11中的实例有值返回,没有值时返回参数中的实例
		System.out.println(o11.orElse(new Pojo()));
		// 获得optional中的值,此处因为o11为null,获取值时仍然抛异常
		System.out.println(o11.get());
	}
}
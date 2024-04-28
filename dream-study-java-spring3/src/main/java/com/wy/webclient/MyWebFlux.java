package com.wy.webclient;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import com.wy.model.User;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * WebClient方式调用WebFlux接口
 *
 * @author 飞花梦影
 * @date 2022-09-02 09:43:34
 */
public class MyWebFlux {

	public static void main(String[] args) {
		Mono.just(User.builder().userId(1l).build());
		// defaultIfEmpty()中的参数是Mono中的值.若调用者Mono中没有元素,则会将此方法
		// 中的参数作为调用者Mono中的元素,调用者Mono与返回值Mono是同一个对象
		Mono.just("sxxxx").defaultIfEmpty("xxxx");
		// switchIfEmpty()中的参数是一个Mono.若其调用者Mono中没有任何元素,则直接将参数Mono
		// 作为返回值,而摒弃了原来调用者Mono,即调用者Mono与返回值Mono不是同一个对象
		Mono.just("xxxxx").switchIfEmpty(Mono.just("xx"));

		List<User> users = new ArrayList<User>();
		users.add(User.builder().userId(2l).username("test1").build());
		users.add(User.builder().userId(3l).username("test2").build());
		// 直接将集合或数组转换为Flux
		Flux.just(users);
		// 将数组转换为Flux
		Flux.fromArray(new Integer[] { 2, 3 });
		// 将流转换为Flux
		Flux.fromStream(users.stream());
		// 将迭代转换为Flux
		Flux.fromIterable(users);
		// 默认情况下,集合中的元素每隔2秒拿到一个,但不返回给前端,等到所有元素都拿到之后一次性返回,请求完成
		// 当加上特定的produces时,每2秒返回一个值,直接在前端展示,直到所有元素全部展示完毕,请求完成
		Flux.fromIterable(users).delayElements(Duration.ofSeconds(2));

		Flux<Long> rets = Flux.fromStream(users.stream().map(t -> t.getUserId()));
		System.out.println(rets);
		// 订阅之后才会出发数据流操作
		rets.subscribe(t -> System.out.print(t));
	}
}
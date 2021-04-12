package com.wy.crl;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.wy.model.User;
import com.wy.service.UserService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 测试WebFlux的用户API接口
 * 
 * @author ParadiseWY
 * @date 2020-11-23 14:16:28
 * @git {@link https://github.com/mygodness100}
 */
@RestController
@RequestMapping("user")
public class UserCrl {

	@Autowired
	private UserService userService;

	@GetMapping("testMono")
	public Mono<User> testMono() {
		return Mono.just(User.builder().userId(1l).build());
	}

	@GetMapping(value = "testFlux", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Flux<User> testFlux() {
		// 默认情况下,集合中的元素每隔2秒拿到一个,但不返回给前端,等到所有元素都拿到之后一次性返回,请求完成
		// 当加上特定的produces时,没2秒返回一个值,直接在前端展示,直到所有元素全部展示完毕,请求完成
		return userService.testFlux().delayElements(Duration.ofSeconds(2));
	}

	/**
	 * 使用WebClient调用其他的WebFlux接口,不能本项目的调用本项目的,会抛阻塞异常
	 */
	@GetMapping("testWebClient")
	public void testWebClient() {
		Mono<String> user = WebClient.create().get().uri("http://localhost:8080/user/testMono")
				.accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(String.class);
		System.out.println(user.block());
	}
}
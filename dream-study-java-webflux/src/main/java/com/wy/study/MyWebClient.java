package com.wy.study;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersUriSpec;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

import com.wy.model.User;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * WebClient使用示例
 * 
 * @author 飞花梦影
 * @date 2021-10-02 11:22:52
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Slf4j
public class MyWebClient {

	public void test() {
		// 构建请求
		RequestHeadersUriSpec<?> requestHeadersUriSpec = WebClient.create().get();
		// 发送请求,获得结果
		ResponseSpec retrieve = requestHeadersUriSpec.retrieve();
		// 处理Http Status
		retrieve.onStatus(null, null);
		// 处理结果
		Flux<User> bodyToFlux = retrieve.bodyToFlux(User.class);
		User first = bodyToFlux.blockFirst();
		System.out.println(first);
		Mono<User> bodyToMono = retrieve.bodyToMono(User.class);
		System.out.println(bodyToMono.block());
		// 处理结果
		Mono<ClientResponse> exchange = requestHeadersUriSpec.exchangeToMono(null);
		exchange.flux().blockFirst().bodyToFlux(User.class);

		// 构建请求,设置HTTP请求地址
		WebClient webClient = WebClient.builder().baseUrl("http://ip:port").build();
		webClient.get()
				// 设置接口地址
				.uri("/user/getById/{id}", 1)
				// 设置ContentType
				.accept(MediaType.APPLICATION_JSON)
				// 发送请求,获得响应
				.retrieve()
				// 将结果转换为单条数据
				.bodyToMono(User.class)
				// 将结果转换为多条数据
				// .bodyToFlux(User.class).toStream()
				// 输出异常信息
				.doOnError(t -> log.error(t.getMessage())).doFinally(t -> System.out.println(11))
				// 指定以上操作是由单个线程完成
				.subscribeOn(Schedulers.single()).subscribe(t -> log.info("{}", t));
		webClient.post().uri("/user/add/")
				// 添加请求参数
				.body(User.builder().username("admin"), User.class).retrieve();
	}
}
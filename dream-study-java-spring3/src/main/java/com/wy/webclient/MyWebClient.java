package com.wy.webclient;

import java.time.Duration;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
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
		Mono<ClientResponse> exchange = retrieve.bodyToMono(ClientResponse.class);
		exchange.flux().blockFirst().bodyToFlux(User.class);

		// 构建请求,设置HTTP请求地址
		WebClient webClient = WebClient.builder().baseUrl("http://ip:port").build();
		webClient.get()
				// 设置接口地址
				.uri("/user/getById/{id}", 1)
				// 设置请求头参数
				.header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
				// 直接使用httpheaders
				.headers((httpheaders) -> {
					httpheaders.setContentType(MediaType.APPLICATION_JSON);
				})
				// 设置ContentType
				.accept(MediaType.APPLICATION_JSON)
				// 接收所有的请求类型
				.accept(MediaType.ALL)
				// 对请求做额外出来
				.httpRequest(requestConsumer -> {
				})
				// 发送请求,获得响应
				.retrieve()
				// 将结果转换为单条数据
				.bodyToMono(User.class)
				// 将结果转换为多条数据
				// .bodyToFlux(User.class).toStream()
				// 输出异常信息
				.doOnError(t -> log.error(t.getMessage())).doFinally(t -> System.out.println(11))

				// 指定以上操作是由单个线程完成
				.subscribeOn(Schedulers.single())
				// 同步完成
				// .block()
				// 异步完成,只处理成功
				// .subscribe(t -> log.info("{}", t));
				.subscribe(success -> {
					log.info("成功:{}", success);
				}, throwable -> {
					log.error("失败:{}", throwable.getMessage());
				});
		webClient.post().uri("/user/add/")
				// 添加请求参数
				.body(User.builder().username("admin"), User.class).retrieve()
				// 处理4XX请求和5XX请求
				.onStatus(status -> status.is4xxClientError(),
						clientResponse -> Mono.error(new RuntimeException("Client error")))
				.onStatus(status -> status.is5xxServerError(),
						clientResponse -> Mono.error(new RuntimeException("Server error")));

		// 自定义请求方法,请求头参数
		webClient.method(HttpMethod.POST)
				// 直接将字符串数据转换为可使用数据
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				// 请求参数
				.body(BodyInserters.fromFormData("data", "")).retrieve().bodyToMono(String.class)
				// 设置超时
				.timeout(Duration.ofMillis(60000)).block();
	}
}
package com.wy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import reactor.netty.http.client.HttpClient;

/**
 * WebFlux WebClient配置
 *
 * @author 飞花梦影
 * @date 2025-02-11 15:14:00
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class MyWebClientConfig {

	@Bean
	WebClient webClient() {
		HttpClient httpClient = HttpClient.create()
				.doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(1))
						.addHandlerLast(new WriteTimeoutHandler(1)));
		return WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient)).build();
	}
}
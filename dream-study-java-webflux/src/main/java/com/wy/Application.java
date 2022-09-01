package com.wy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.http.client.reactive.JettyClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * WebFlux:用于构建基于Reactive技术栈之上的Web应用程序,基于Reactive Stream API,运行于非阻塞服务器上
 * 
 * WebFlux在请求的耗时上并没有太大改善,仅需少量固定数量线程和较少内存即可实现扩展
 * 
 * {@link WebClient}:以Reactive方式处理HTTP请求的非阻塞式的客户端,支持如下:
 * Netty的{@link ReactorClientHttpConnector},Jetty的{@link JettyClientHttpConnector}
 * 
 * {@link WebClientAutoConfiguration}:自动配置WebClient,但是没有实例化WebClient,只实例化了WebClient.Builder
 * 
 * {@link Mono}:单个结果包装
 * {@link Flux}:多结果包装
 * 
 * @author 飞花梦影
 * @date 2021-09-30 13:26:46
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
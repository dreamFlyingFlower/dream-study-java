package com.wy.http;

import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.HttpComponentsClientHttpRequestFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.http.client.JettyClientHttpRequestFactory;
import org.springframework.http.client.ReactorClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

/**
 * SpringBoot3.4新特性
 * 
 * RestClient 和 RestTemplate
 * 
 * <pre>
 * 新特性:自动配置支持,RestClient和RestTemplate现在支持多种HTTP客户端的自动配置,不再需要手动配置 RestClient.builder() 包括:
 * 1. Apache HTTP Components
 * 2. Jetty Client
 * 3. Reactor Netty 的 HttpClient
 * 4. JDK 的 HttpClient
 * 
 * 客户端优先级顺序:
 * 1.{@link HttpComponentsClientHttpRequestFactory}:Apache HTTP Components
 * 2.{@link JettyClientHttpRequestFactory}:Jetty Client
 * 3.{@link ReactorClientHttpRequestFactory}:Reactor Netty HttpClient
 * 4.{@link JdkClientHttpRequestFactory}:JDK HttpClient
 * 5.{@link SimpleClientHttpRequestFactory}:简单的 JDK HttpURLConnection
 * </pre>
 * 
 * 结构化日志记录
 * 
 * <pre>
 * 支持的格式:
 * 1.Elastic Common Schema (ECS)
 * 2.Graylog Extended Log Format (GELF)
 * 3.Logstash
 * </pre>
 * 
 * 可观察性改进
 * 
 * <pre>
 * 应用程序分组:
 * 设置应用程序组:spring.application.group=order-processing
 * 在日志中包含组信息:logging.include-application.group=true
 * </pre>
 * 
 * OTLP 跟踪增强
 * 
 * <pre>
 * 启用 gRPC 传输:management.otlp.tracing.transport=grpc
 * 设置端点:management.otlp.tracing.endpoint=grpc://otel-collector:4317
 * </pre>
 *
 * @author 飞花梦影
 * @date 2024-12-09 09:39:11
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
public class Feature {

	/**
	 * 自定义客户端
	 * 
	 * @return HttpComponentsClientHttpRequestFactoryBuilder
	 */
	@Bean
	HttpComponentsClientHttpRequestFactoryBuilder httpComponentsClientHttpRequestFactoryBuilder() {
		return ClientHttpRequestFactoryBuilder.httpComponents()
				.withDefaultRequestConfigCustomizer(builder -> builder.setProtocolUpgradeEnabled(false));
	}
}
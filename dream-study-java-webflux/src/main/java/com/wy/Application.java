package com.wy;

import java.util.concurrent.Flow;
import java.util.concurrent.Flow.Processor;
import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.SubmissionPublisher;

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
 * <pre>
 * {@link Mono}:单个结果包装,包含0或1个元素的异步序列
 * {@link Flux}:多结果包装,包含多个元素的异步序列
 * </pre>
 * 
 * 反应式流:发布者发送多个元素的异步请求,发布者向订阅者异步发送多个或稍少的元素.反应式流会在 pull 模型和 push 模型流处理机制之间动态切换.
 * 当发布者快、订阅者慢时,它使用 pull 模型;当发布者慢、订阅者快时,它使用 push 模型.即谁慢谁占主动
 * 
 * <pre>
 * {@link Flow}:JDK中的反应式流,至少需要JDK10以上才可使用
 * {@link Publisher}:发布者,是有序消息的生产者,它根据收到的请求向订阅者发布消息
 * ->{@link SubmissionPublisher}:异步将已被提交的非空元素顺序地发布给订阅者,元素生成器是以符合反应式流的方式工作
 * {@link Subscriber}:订阅者,从发布者那里订阅并接收消息,一个发布者可能需要处理来自多个订阅者的请求
 * 		发布者向订阅者发送订阅令牌(Subscription),使用订阅令牌,订阅者可以从发布者那里请求多个消息.
 * 		当消息元素准备就绪时,发布者向订阅者发送多个或更少的元素,然后订阅者可以再次请求更多的消息元素,或取消订阅
 * {@link Subscription}:订阅费,订阅令牌.当订阅请求成功时,发布者将其传递给订阅者.订阅者使用订阅令牌与发布者进行交互
 * {@link Processor}:处理器,充当订阅者和发布者的处理阶段, 一个发布者可以拥有多个处理者
 * 		Processor 接口继承了 Publisher和 Subscriber 接口,用于转换发布者/订阅者管道中的元素
 * 		Processor<T, R>会将来自于发布者的 T 类型的消息数据,接收并转换为 R 类型的数据,并将转换后的 R 类型数据发布给订阅者
 * </pre>
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
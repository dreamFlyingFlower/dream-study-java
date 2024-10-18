package com.wy.webflux.websocket;

import org.springframework.lang.NonNull;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;

import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * WebFlux的WebSocket不需要额外的依赖包
 *
 * @author 飞花梦影
 * @date 2024-10-18 14:59:51
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class ReactiveWebSocketServerHandler implements WebSocketHandler {

	@NonNull
	@Override
	public Mono<Void> handle(WebSocketSession session) {
		Mono<Void> send = session.send(Flux.create(sink -> {
			// 可以持有sink对象在任意时候调用next发送消息
			WebSocketMessage message = new WebSocketMessage(null, null);
			sink.next(message);
		})).doOnError(it -> {
			// 异常处理
		});

		// 发送消息,持有一个FluxSink<WebSocketMessage>
		session.send(null);

		// 订阅消息
		Mono<Void> receive = session.receive().doOnNext(it -> {
			// 接收消息
		}).doOnError(it -> {
			// 异常处理
		}).then();

		// 订阅连接关闭事件
		@SuppressWarnings("all")
		Disposable disposable = session.closeStatus().doOnError(it -> {
			// 异常处理
		}).subscribe(it -> {
			// 连接关闭
		});

		return Mono.zip(send, receive).then();
	}
}
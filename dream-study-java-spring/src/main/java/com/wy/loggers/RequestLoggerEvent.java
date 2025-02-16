package com.wy.loggers;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.ServletRequestHandledEvent;

import lombok.extern.slf4j.Slf4j;

/**
 * 默认情况下,当一个请求到达后,SpringMVC底层会发布一个ServletRequestHandledEvent事件,只需要监听该事件也是可以获取到详细的请求/响应信息
 * 
 * 只根据Event事件无法获取详细的信息,还需要结合其他方式
 *
 * @author 飞花梦影
 * @date 2025-02-06 10:41:56
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Slf4j
@Component
public class RequestLoggerEvent implements ApplicationListener<ServletRequestHandledEvent> {

	@Override
	public void onApplicationEvent(ServletRequestHandledEvent event) {
		log.info("请求信息: {}", event);
		System.out.println(event.getMethod());
		System.out.println(event.getRequestUrl());
	}
}
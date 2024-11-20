package com.wy.event;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

/**
 * 监听器的实现方式,泛型为需要监听的事件类型
 * 
 * @author 飞花梦影
 * @date 2019-05-03 18:00:46
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
public class MyApplicationReadyEventListener implements ApplicationListener<ApplicationReadyEvent> {

	@Override
	public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
		// 此处的source就是需要监听的event中初始化时传入的source,此处为SpringApplication
		applicationReadyEvent.getSource();
		System.out.println("applicationReadyEvent...");
	}
}
package com.wy.listener;

import java.util.EventListener;

import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

/**
 * 监听器的实现方式1,泛型为需要监听的事件类型
 * 
 * ApplicationListener实现jdk的{@link EventListener},该类只是一个标记接口
 * 
 * @author 飞花梦影
 * @date 2019-05-03 18:00:46
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
public class SelfSpringListener1 implements ApplicationListener<SelfSpringEvent> {

	@Override
	public void onApplicationEvent(SelfSpringEvent event) {
		// 此处的source就是需要监听的event中初始化时传入的source
		event.getSource();
		System.out.println("SelfSpringListener...");
	}
}
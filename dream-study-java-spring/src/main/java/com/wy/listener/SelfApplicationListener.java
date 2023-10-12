package com.wy.listener;

import java.util.EventListener;

import org.springframework.context.ApplicationListener;

/**
 * Spring的事件监听器(ApplicationListener),实现jdk的{@link EventListener},该类只是一个标记接口,可以监听某个事件的event
 * 
 * @author 飞花梦影
 * @date 2019-05-03 18:00:46
 * @git {@link https://github.com/mygodness100}
 */
public class SelfApplicationListener implements ApplicationListener<SelfSpringEvent> {

	@Override
	public void onApplicationEvent(SelfSpringEvent event) {
		// 此处的source就是需要监听的event中初始化时传入的source
		event.getSource();
		System.out.println("SelfSpringListener...");
	}
}
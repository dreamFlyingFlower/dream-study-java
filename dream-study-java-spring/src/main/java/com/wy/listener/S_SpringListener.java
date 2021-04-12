package com.wy.listener;

import org.springframework.context.ApplicationListener;

/**
 * spring的事件监听器(ApplicationListener),实现jdk的{EventListener},该类只是一个标记接口
 * 
 * @author ParadiseWY
 * @date 2019-05-03 18:00:46
 * @git {@link https://github.com/mygodness100}
 */
public class S_SpringListener implements ApplicationListener<S_SpringEvent> {

	@Override
	public void onApplicationEvent(S_SpringEvent event) {
		// 此处的source就是需要监听的event中初始化时传入的source
		event.getSource();
		System.out.println("S_SpringListener...");
	}
}
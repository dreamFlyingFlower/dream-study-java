package com.wy.listener;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.event.EventListener;

/**
 * spring的事件监听
 * 
 * @author ParadiseWY
 * @date 2019-05-03 18:18:26
 * @git {@link https://github.com/mygodness100}
 */
public class S_SpringEventListener {

	public static void main(String[] args) {
		// 该类实现了{ApplicationEventPublisher},对所有的事件进行发布和监听
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		// 当用该方式实现监听时,不需要再次refresh方法
		// AnnotationConfigApplicationContext context1 = new
		// AnnotationConfigApplicationContext(S_SpringListener.class);
		// 增加监听器
		context.addApplicationListener(new S_SpringListener());
		// 上下文启动
		context.refresh();
		// 事件发布.必须是上下文启动之后发布,否则spring环境没准备好,无法监听该时间,就会报错
		context.publishEvent(new S_SpringEvent("this is e test!!"));

		context.close();
	}

	/**
	 * 启动监听器的第2种方法
	 * 
	 * @param eve 参数必须是继承了ApplicationEvent的类,仍然需要发布事件
	 */
	@EventListener
	public void event(S_SpringEvent eve) {

	}

	/**
	 * 启动监听器的第3种方法,直接在META-INF的factories中添加
	 */
}
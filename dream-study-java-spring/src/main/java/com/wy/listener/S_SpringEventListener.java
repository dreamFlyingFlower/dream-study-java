package com.wy.listener;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.context.event.EventListenerMethodProcessor;

/**
 * spring的发布事件监听
 * 
 * @author 飞花梦影
 * @date 2019-05-03 18:18:26
 * @git {@link https://github.com/mygodness100}
 */
@Configuration
public class S_SpringEventListener {

	public static void main(String[] args) {
		// 第一种发布监听的方式
		SpringApplication application = new SpringApplication(S_SpringEventListener.class);
		application.addListeners(new S_ApplicationListener());
		ConfigurableApplicationContext configurableApplicationContext = application.run(args);
		configurableApplicationContext.publishEvent(new S_SpringEvent("test"));
		configurableApplicationContext.close();
		// 同上
		// 直接在启动类中发布事件监听
		ConfigurableApplicationContext applicationContext = SpringApplication.run(S_SpringEventListener.class, args);
		// 添加监听器
		applicationContext.addApplicationListener(new S_ApplicationListener());
		// 启动完成之后发布监听
		applicationContext.publishEvent(new S_SpringEvent("test"));
		configurableApplicationContext.close();
		// 使用以下方式同上
		// 该类实现了{ApplicationEventPublisher},对所有的事件进行发布和监听
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		// 添加监听器
		context.addApplicationListener(new S_ApplicationListener());
		// 上下文启动
		context.refresh();
		// 事件发布.必须是上下文启动之后发布,否则spring环境没准备好,无法监听该时间,就会报错
		context.publishEvent(new S_SpringEvent("this is e test!!"));
		context.close();
		// 直接注入ApplicationEventPublisher或需要发布监听的类实现ApplicationEventPublisherAware
	}

	/**
	 * 启动监听器的第2种方法,注意该方法所在的类需要添加到spring容器中,主要处理类为{@link EventListenerMethodProcessor}
	 * 
	 * 参数必须有,如果是指定类型的监听器,那就只能监听指定类型事件;如果是Object,则可以监听所有事件
	 * 
	 * @param eve 参数必须是继承了ApplicationEvent的类,仍然需要发布事件 FIXME 注解方式无法启动
	 */
	@EventListener
	public void event(S_SpringEvent eve) {
		System.out.println("S_SpringEventListener" + eve.getSource());
	}

	/**
	 * 启动监听器的第3种方法,直接在META-INF的factories中添加
	 * 
	 * org.springframework.context.ApplicationListener=com.wy.listener.S_SpringListener
	 */
}
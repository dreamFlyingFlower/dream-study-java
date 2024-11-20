package com.wy.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 事件发布
 *
 * @author 飞花梦影
 * @date 2023-12-04 14:08:07
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class SelfSpringPublish {

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	@Autowired
	private ApplicationContext applicationContext;

	public void publish() {
		// 第一种
		SpringApplication application = new SpringApplication(SelfSpringListener2.class);
		application.addListeners(new SelfSpringListener1());
		ConfigurableApplicationContext configurableApplicationContext = application.run();
		configurableApplicationContext.publishEvent(new SelfSpringEvent("test"));
		configurableApplicationContext.close();
		// 同上
		// 直接在启动类中发布事件监听
		ConfigurableApplicationContext configurableApplicationContext1 =
				SpringApplication.run(SelfSpringListener2.class, new String[0]);
		// 添加监听器
		configurableApplicationContext1.addApplicationListener(new SelfSpringListener1());
		// 启动完成之后发布监听
		configurableApplicationContext1.publishEvent(new SelfSpringEvent("test"));
		configurableApplicationContext1.close();

		// 使用以下方式同上
		// 该类实现了{ApplicationEventPublisher},对所有的事件进行发布和监听
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		// 添加监听器
		context.addApplicationListener(new SelfSpringListener1());
		// 上下文启动
		context.refresh();
		// 事件发布.必须是上下文启动之后发布,否则spring环境没准备好,无法监听该时间,就会报错
		context.publishEvent(new SelfSpringEvent("this is e test!!"));
		context.close();

		// 第二种:直接注入ApplicationEventPublisher或需要发布监听的类实现ApplicationEventPublisherAware
		applicationEventPublisher.publishEvent(new SelfSpringEvent("test applicationEventPublisher"));
		// 如果不指定事件类型,则发布所有类型事件
		applicationEventPublisher.publishEvent(ApplicationEvent.class);
		// 同第二种方法
		applicationContext.publishEvent(ApplicationEvent.class);
	}
}
package com.wy.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.web.context.support.RequestHandledEvent;

/**
 * Spring的事件监听器(ApplicationListener),实现jdk的{EventListener},该类只是一个标记接口,可以监听某个事件的event
 * 
 * Spring主要的内置事件:
 * 
 * <pre>
 * {@link ContextRefreshedEvent}:ApplicationContext被初始化或刷新时发布,也可以在ConfigurableApplicationContext接口中使用refresh()触发.
 * 		此处的初始化是指所有的Bean被成功装载,后处理Bean被检测并激活,所有Singleton Bean 被预实例化,ApplicationContext容器已就绪可用
 * {@link ContextStartedEvent}:当使用 ConfigurableApplicationContext 启动 ApplicationContext时,该事件被发布.
 * 		可以调查自己的数据库,或者可以在接受到这个事件后重启任何停止的应用程序
 * {@link ContextStoppedEvent}:当使用 ConfigurableApplicationContext 停止ApplicationContext 时,发布这个事件.
 * 		可以在接受到这个事件后做必要的清理的工作
 * {@link ContextClosedEvent}:当使用 ConfigurableApplicationContext#close()关闭 ApplicationContext 时,该事件被发布.
 * 		一个已关闭的上下文到达生命周期末端,它不能被刷新或重启
 * {@link RequestHandledEvent}:这是一个 web-specific 事件,告诉所有 bean HTTP 请求已经被服务.只能应用于使用DispatcherServlet的Web应用.
 * 		在使用Spring作为前端的MVC控制器时,当Spring处理用户请求结束后,系统会自动触发该事件
 * </pre>
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
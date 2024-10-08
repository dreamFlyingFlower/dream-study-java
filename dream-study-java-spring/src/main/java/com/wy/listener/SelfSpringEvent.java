package com.wy.listener;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.web.context.support.RequestHandledEvent;
import org.springframework.web.context.support.ServletRequestHandledEvent;

/**
 * Spring的事件(ApplicationEvent),基于继承jdk的EventObject,需要发布,见{@link SelfSpringListener2}
 * 
 * Spring的主要内置事件,可以直接被监听,见{@link SelfSpringListener1}
 * 
 * <pre>
 * {@link ContextRefreshedEvent}:在{@link AbstractApplicationContext#finishRefresh()}中发布,也可以在ConfigurableApplicationContext中使用refresh()触发.
 * 		此时所有的Bean被成功装载,对应的属性,init(),Aware回调也全部执行,后处理Bean被检测并激活,所有Singleton Bean被预实例化,
 * 		ApplicationContext容器已就绪可用,适合做一些系统启动后的准备工作
 * {@link ContextStartedEvent}:在{@link AbstractApplicationContext#start()}中发布.该事件的触发是所有的单例bean创建完成后发布,
 * 		此时实现了Lifecycle接口的bean还没有回调start(),当这些start()被调用后,才会发布ContextStartedEvent事件
 * 		可以调用自己的数据库,或者可以在接受到这个事件后重启任何停止的应用程序.
 * {@link ContextStoppedEvent}:在{@link AbstractApplicationContext#stop()}中发布.此时单例bean还没有被销毁,要都停掉后才能释放资源,销毁bean
 * 		可以在接受到这个事件后做必要的清理的工作.
 * {@link ContextClosedEvent}:在{@link AbstractApplicationContext#close()}中发布.此时IOC容器已经关闭,但尚未销毁所有的bean
 * 		一个已关闭的上下文到达生命周期末端,它不能被刷新或重启
 * {@link RequestHandledEvent}:这是一个 web-specific 事件,告诉所有 bean HTTP 请求已经服务结束.只能应用于使用DispatcherServlet的Web应用.
 * 		在使用Spring作为前端的MVC控制器时,当Spring处理用户请求结束后,系统会自动触发该事件
 * {@link ServletRequestHandledEvent}:RequestHandledEvent的一个子类,用于添加特定于Servlet的上下文信息
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2019-05-03 18:08:50
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class SelfSpringEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;

	/**
	 * 必须实现的参数构造,该source源可以传任意值,在事件发布时传递
	 * 
	 * @param source 任意值
	 */
	public SelfSpringEvent(Object source) {
		super(source);
	}
}
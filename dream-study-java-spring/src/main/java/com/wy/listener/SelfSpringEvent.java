package com.wy.listener;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.context.support.AbstractApplicationContext;

/**
 * Spring的事件(ApplicationEvent),基于继承jdk的EventObject,需要发布,见{@link SelfSpringEventListener}
 * 
 * Spring内部启动时自带的监听事件
 * 
 * <pre>
 * {@link ContextStartedEvent}:在{@link AbstractApplicationContext#start()}中发布.该事件的触发是所有的单例bean创建完成后发布,
 * 		此时实现了Lifecycle接口的bean还没有回调start(),当这些start()被调用后,才会发布ContextStartedEvent事件
 * {@link ContextRefreshedEvent}:在{@link AbstractApplicationContext#finishRefresh()}中发布.此时容器已经启动完成,
 * 		容器中的bean已经创建完成,对应的属性,init(),Aware回调等也全部执行,很适合做一些系统启动后的准备工作
 * {@link ContextClosedEvent}:在{@link AbstractApplicationContext#close()}中发布.此时IOC容器已经关闭,但尚未销毁所有的bean
 * {@link ContextStoppedEvent}:在{@link AbstractApplicationContext#stop()}中发布.此时单例bean还没有被销毁,要都停掉后才能释放资源,销毁bean
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
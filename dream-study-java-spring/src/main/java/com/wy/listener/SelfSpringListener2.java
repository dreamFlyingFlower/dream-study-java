package com.wy.listener;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.context.event.EventListenerMethodProcessor;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * spring的发布事件监听
 * 
 * <pre>
 * {@link EventListener}:监听指定的事件,如果不指定,则监听所有类型事件.同步事件,在被监听事件执行完之后执行,不影响原有事务
 * {@link TransactionalEventListener}:事务监听事件,默认在事务执行成功之后执行,可指定执行时机
 * {@link TransactionalEventListener#phase()}:AFTER_COMPLETION指无论事务commit或rollback都执行监听
 * AFTER_COMMIT + AFTER_COMPLETION可以同时生效;AFTER_ROLLBACK + AFTER_COMPLETION可以同时生效
 * {@link TransactionalEventListener#fallbackExecution()}:若没有事务的时候,对应的event是否需要执行,false表示没事务就不执行了
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2019-05-03 18:18:26
 * @git {@link https://github.com/dreamFlyingFLower}
 */
@Configuration
public class SelfSpringListener2 {

	/**
	 * 启动监听器的第1种方法,注意该方法所在的类需要添加到spring容器中,主要处理类为{@link EventListenerMethodProcessor}
	 * 
	 * 参数必须有,如果是指定类型的监听器,那就只能监听指定类型事件;如果是Object,则可以监听所有事件
	 * 
	 * @param eve 参数必须是继承了ApplicationEvent的类,仍然需要发布,值必须是@EventListener中标注的类型
	 */
	@EventListener(SelfSpringEvent.class)
	public void event(SelfSpringEvent eve) {
		System.out.println("SelfSpringEventListener" + eve.getSource());
	}

	/**
	 * 启动监听器的第3种方法,直接在META-INF的factories中添加
	 * 
	 * org.springframework.context.ApplicationListener=com.wy.listener.SelfSpringListener
	 */
}
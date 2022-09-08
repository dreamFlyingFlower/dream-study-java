package com.wy.listener;

import org.springframework.context.ApplicationEvent;

/**
 * spring的事件(ApplicationEvent),基于继承jdk的EventObject
 * 
 * @author 飞花梦影
 * @date 2019-05-03 18:08:50
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class S_SpringEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;

	/**
	 * 必须实现的参数构造,该source源可以传任意值
	 * 
	 * @param source 任意值
	 */
	public S_SpringEvent(Object source) {
		super(source);
	}
}
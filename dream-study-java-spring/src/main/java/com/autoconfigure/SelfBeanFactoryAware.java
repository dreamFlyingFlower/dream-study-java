package com.autoconfigure;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * {@link BeanFactoryAware}:该接口只有一个触发点,发生在bean的实例化之后,注入属性之前,也就是Setter之前.该类的扩展点方法可以拿到BeanFactory
 * 
 * 可以在bean实例化之后,但还未初始化之前拿到BeanFactory,此时可以对每个bean作特殊化的定制,也或者可以把BeanFactory拿到进行缓存,之后使用
 *
 * @author 飞花梦影
 * @date 2022-10-18 00:00:05
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class SelfBeanFactoryAware implements BeanFactoryAware {

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		System.out.println(
				"[SelfBeanFactoryAware] " + beanFactory.getBean(SelfBeanFactoryAware.class).getClass().getSimpleName());
	}
}
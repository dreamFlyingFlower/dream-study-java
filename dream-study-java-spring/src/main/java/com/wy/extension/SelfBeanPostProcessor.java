package com.wy.extension;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * 在初始化Bean前后,实现一些自己的逻辑,只在bean初始化阶段扩展(注入spring上下文前后)
 *
 * @author 飞花梦影
 * @date 2023-01-13 10:46:26
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class SelfBeanPostProcessor implements BeanPostProcessor {

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		System.out.println("bean初始化之前....");
		return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		System.out.println("bean初始化之后.....");
		return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
	}
}
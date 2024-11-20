package com.wy.scalable.example;

import java.beans.PropertyDescriptor;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;

/**
 * {@link InstantiationAwareBeanPostProcessor}:继承BeanPostProcessor.BeanPostProcessor只在bean初始化阶段扩展(注入spring上下文前后),
 * 而InstantiationAwareBeanPostProcessor在此基础上增加了3个方法,把可扩展的范围增加了实例化阶段和属性注入阶段
 * 
 * 该类主要的扩展点有5个方法,主要在bean生命周期的实例化阶段和初始化阶段,按调用顺序为:
 * postProcessBeforeInstantiation:实例化bean之前,相当于new这个bean之前
 * postProcessAfterInstantiation:实例化bean之后,相当于new这个bean之后
 * postProcessPropertyValues:bean已经实例化完成,在属性注入时阶段触发,@Autowired,@Resource等注解原理基于此方法实现
 * postProcessBeforeInitialization:初始化bean之前,相当于把bean注入spring上下文之前
 * postProcessAfterInitialization:初始化bean之后,相当于把bean注入spring上下文之后
 * 
 * 该扩展点无论是写中间件或业务,都能利用.比如对实现了某一类接口的bean在各个生命期间进行收集,或对某个类型的bean进行统一的设值等
 *
 * @author 飞花梦影
 * @date 2022-10-17 23:39:22
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class SelfInstantiationAwareBeanPostProcessor implements InstantiationAwareBeanPostProcessor {

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		System.out.println("[SelfInstantiationAwareBeanPostProcessor] before initialization " + beanName);
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		System.out.println("[SelfInstantiationAwareBeanPostProcessor] after initialization " + beanName);
		return bean;
	}

	@Override
	public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
		System.out.println("[SelfInstantiationAwareBeanPostProcessor] before instantiation " + beanName);
		return null;
	}

	@Override
	public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
		System.out.println("[SelfInstantiationAwareBeanPostProcessor] after instantiation " + beanName);
		return true;
	}

	@Override
	public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean,
			String beanName) throws BeansException {
		System.out.println("[SelfInstantiationAwareBeanPostProcessor] postProcessPropertyValues " + beanName);
		return pvs;
	}
}
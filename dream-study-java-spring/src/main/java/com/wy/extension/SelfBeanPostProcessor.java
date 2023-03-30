package com.wy.extension;

import javax.annotation.PostConstruct;

import org.springframework.aop.aspectj.autoproxy.AspectJAwareAdvisorAutoProxyCreator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * 在初始化Bean前后,实现一些自己的逻辑,只在bean初始化阶段扩展(注入spring上下文前后)
 * 
 * afterPropertiesSet(),初始化init()等方法都是先调spring的,再调用用户自定义的
 * 
 * 常用实现类,调用流程见源码:
 * 
 * <pre>
 * {@link AutowiredAnnotationBeanPostProcessor}:解析{@link Autowired},{@link Value}
 * {@link AspectJAwareAdvisorAutoProxyCreator}:AOP代理
 * </pre>
 *
 * @author 飞花梦影
 * @date 2023-01-13 10:46:26
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class SelfBeanPostProcessor implements BeanPostProcessor, InitializingBean {

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		System.out.println("bean初始化之前....");
		return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("afterPropertiesSet在postProcessBeforeInitialization()之后调用");
	}

	@PostConstruct
	public void init() {
		System.out.println("初始化init()在afterPropertiesSet之后调用");
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		System.out.println("bean初始化之后.....在init()之后调用");
		return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
	}
}
package com.wy.scalable.example;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.context.event.EventListener;
import org.springframework.context.event.EventListenerFactory;
import org.springframework.context.event.EventListenerMethodProcessor;

/**
 * {@link BeanFactoryPostProcessor}:该接口是beanFactory的扩展接口,在spring读取beanDefinition信息之后,实例化bean之前调用.
 * 可以通过实现该扩展接口来自行处理一些东西,比如修改已经注册的beanDefinition的元信息
 * 
 * SpringIOC在实例化Bean对象之前,需要先读取Bean的相关属性,保存到BeanDefinition中,然后通过BeanDefinition实例化Bean对象.
 * 如果需要对Bean做些特殊处理,可以实现 {@link BeanFactoryPostProcessor}进行处理
 * 
 * 如果只是对beanDefinition做处理,可以继承该接口的子类{@link BeanDefinitionRegistryPostProcessor}
 * 
 * 常用实现类,各个类调用流程见源码:
 * 
 * <pre>
 * {@link BeanDefinitionRegistryPostProcessor}:往beanFactory中注册bean.该接口中的方法先于直接实现BeanFactoryPostProcessor调用
 * {@link EventListenerMethodProcessor#postProcessBeanFactory}:解析定义注册{@link EventListenerFactory},处理{@link EventListener}
 * </pre>
 *
 * @author 飞花梦影
 * @date 2022-10-17 23:37:56
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class SelfBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory)
			throws BeansException {
		DefaultListableBeanFactory defaultListableBeanFactory =
				(DefaultListableBeanFactory) configurableListableBeanFactory;
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(User.class);
		beanDefinitionBuilder.addPropertyValue("id", 123);
		beanDefinitionBuilder.addPropertyValue("name", "飞花梦影");
		defaultListableBeanFactory.registerBeanDefinition("user", beanDefinitionBuilder.getBeanDefinition());
	}
}
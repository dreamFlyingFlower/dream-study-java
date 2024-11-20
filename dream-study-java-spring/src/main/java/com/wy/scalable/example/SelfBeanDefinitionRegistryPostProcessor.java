package com.wy.scalable.example;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * {@link BeanDefinitionRegistryPostProcessor}:该接口是{@link BeanFactoryPostProcessor}子类,在读取项目中的beanDefinition之后执行,
 * 提供一个补充的扩展点,可以在这里动态注册自己的beanDefinition,可以加载classpath之外的bean,功能和BeanFactoryPostProcessor类似
 * 
 * @author 飞花梦影
 * @date 2022-10-17 23:36:16
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class SelfBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

	/**
	 * 实现{@link BeanFactoryPostProcessor}的方法,该方法先于直接实现BeanFactoryPostProcessor的方法调用
	 * 
	 * @param beanFactory DefaultListableBeanFactory
	 * @throws BeansException
	 */
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) beanFactory;
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition("需要注册的bean class");
		beanDefinitionBuilder.addPropertyValue("注入属性的key", "注入属性的value");
		defaultListableBeanFactory.registerBeanDefinition("beanName", beanDefinitionBuilder.getBeanDefinition());
	}

	/**
	 * 往容器中注入bean,该方法先于本类中的postProcessBeanFactory调用
	 * 
	 * @param registry DefaultListableBeanFactory
	 * @throws BeansException
	 */
	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition("需要注册的bean class");
		beanDefinitionBuilder.addPropertyValue("注入属性的key", "注入属性的value");
		registry.registerBeanDefinition("beanName", beanDefinitionBuilder.getBeanDefinition());
	}
}